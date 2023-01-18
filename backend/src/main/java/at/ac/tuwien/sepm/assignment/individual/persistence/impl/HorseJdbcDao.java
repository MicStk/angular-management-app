package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class HorseJdbcDao implements HorseDao {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String TABLE_NAME = "horse";
  private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
  private static final String SQL_SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
  private static final String SQL_SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
  private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME
          + " SET name = ?"
          + "  , description = ?"
          + "  , date_of_birth = ?"
          + "  , sex = ?"
          + "  , owner_id = ?"
          + "  , mother_id = ?"
          + "  , father_id = ?"
          + " WHERE id = ?";

  private static final String SQL_SELECT_SEARCH_BY_NAME = "SELECT * FROM " + TABLE_NAME
          + " WHERE UPPER(name||' ') like UPPER('%'|| ? ||'%')";
  private static final String SQL_SELECT_SEARCH_LIMIT_CLAUSE = " LIMIT ?";
  private static final String SQL_CREATE = "INSERT INTO " + TABLE_NAME
          + " (name, description, date_of_birth, sex, owner_id, mother_id, father_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

  private final JdbcTemplate jdbcTemplate;

  public HorseJdbcDao(
      JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Horse> getAll() {
    LOG.trace("getAll()");
    return jdbcTemplate.query(SQL_SELECT_ALL, this::mapRow);
  }

  @Override
  public Horse getById(long id) {
    LOG.trace("getById({})", id);
    List<Horse> horses;

    horses = jdbcTemplate.query(SQL_SELECT_BY_ID, this::mapRow, id);

    if (horses.size() > 1) {
      LOG.error("[FatalException]: Found more than 1 horse with id " + id);
      throw new FatalException("Too many horses with ID %d found".formatted(id));
    }

    return horses.get(0);
  }

  @Override
  public void deleteHorse(long id) throws NotFoundException {
    LOG.trace("deleteHorse({})", id);
    int updated = jdbcTemplate.update(SQL_DELETE, id);
    if (updated == 0) {
      throw new NotFoundException("Could not delete horse with ID " + id + ", because it does not exist");
    }
  }

  @Override
  public List<Horse> searchHorses(HorseSearchDto searchParameters) {
    LOG.trace("searchHorses({})", searchParameters);

    var params = new ArrayList<>();

    String query = "SELECT * FROM " + TABLE_NAME;

    boolean needsAnd = false;
    boolean needsWhere = true;

    if (searchParameters.name() != null) {
      if (needsWhere) {
        query += " WHERE";
        needsWhere = false;
      }
      if (needsAnd) {
        query += " And";
        needsAnd = false;
      }
      query += " UPPER(name||' ') like UPPER('%'|| ? ||'%')";
      needsAnd = true;
      params.add(searchParameters.name());
    }

    if (searchParameters.description() != null) {
      if (needsWhere) {
        query += " WHERE";
        needsWhere = false;
      }

      if (needsAnd) {
        query += " And";
        needsAnd = false;
      }
      query += " UPPER(description||' ') like UPPER('%'|| ? ||'%')";
      needsAnd = true;
      params.add(searchParameters.description());
    }

    if (searchParameters.bornBefore() != null) {
      if (needsWhere) {
        query += " WHERE";
        needsWhere = false;
      }

      if (needsAnd) {
        query += " And";
        needsAnd = false;
      }
      query += " date_of_birth <= ?";
      needsAnd = true;
      params.add(searchParameters.bornBefore());
    }

    if (searchParameters.sex() != null) {
      if (needsWhere) {
        query += " WHERE";
        needsWhere = false;
      }

      if (needsAnd) {
        query += " And";
        needsAnd = false;
      }
      query += " sex LIKE ?";
      needsAnd = true;
      params.add(searchParameters.sex().toString());
    }

    if (searchParameters.ownerID() != null) {
      if (needsWhere) {
        query += " WHERE";
        needsWhere = false;
      }

      if (needsAnd) {
        query += " And";
        needsAnd = false;
      }
      query += " owner_Id LIKE ?";
      needsAnd = true;
      params.add(searchParameters.ownerID());
    }

    LOG.debug("SQL statement has been completed.");
    return jdbcTemplate.query(query, this::mapRow, params.toArray());

  }

  @Override
  public Horse getHorseTree(Long id, Long generations) throws NotFoundException {
    LOG.trace("getHorseTree({}, {})", id, generations);

    try {
      Horse horse = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, this::mapRow, id);

      if (horse == null) {
        LOG.error("[NotFoundException:] Horse with {} not found", id);
        throw new NotFoundException("Horse with id '" + id + "' not found");
      }

      if (generations > 0) {
        if (horse.getMotherId() != null) {
          LOG.debug("generations: " + generations);
          horse.setMotherId(getHorseTree(horse.getMotherId(), generations - 1).getId());
        }

        if (horse.getFatherId() != null) {
          LOG.debug("generations: " + generations);
          horse.setFatherId(getHorseTree(horse.getFatherId(), generations - 1).getId());
        }
      }
      return horse;
    } catch (DataAccessException e) {
      LOG.error("[DataAccessException]: Error while accessing the database", e);
      return null;
    }

  }

  @Override
  public Horse create(HorseCreateDto newHorse) {
    LOG.trace("create({})", newHorse);

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement stmt = con.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, newHorse.name());
      stmt.setString(2, newHorse.description());
      stmt.setObject(3, newHorse.dateOfBirth());
      stmt.setString(4, String.valueOf(newHorse.sex()));
      stmt.setObject(5, newHorse.ownerId());
      stmt.setObject(6, newHorse.motherId());
      stmt.setObject(7, newHorse.fatherId());
      return stmt;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key == null) {
      // This should never happen. If it does, something is wrong with the DB or the way the prepared statement is set up.
      LOG.error("[FatalException]: Could not extract the key.");
      throw new FatalException("Could not extract key for newly created owner. There is probably a programming error…");
    }

    return new Horse()
            .setId(key.longValue())
            .setName(newHorse.name())
            .setDescription(newHorse.description())
            .setDateOfBirth(newHorse.dateOfBirth())
            .setSex(newHorse.sex())
            .setOwnerId(newHorse.ownerId())
            .setMotherId(newHorse.motherId())
            .setFatherId(newHorse.fatherId())
            ;
  }

  @Override
  public Collection<Horse> searchByName(HorseSearchDto searchParameters) {
    LOG.trace("searchByName({})", searchParameters);
    var query = SQL_SELECT_SEARCH_BY_NAME;
    var params = new ArrayList<>();
    params.add(searchParameters.name());
    var maxAmount = searchParameters.limit();
    if (maxAmount != null) {
      LOG.debug("maxAmount has been set for searchByName()");
      query += SQL_SELECT_SEARCH_LIMIT_CLAUSE;
      params.add(maxAmount);
    }
    return jdbcTemplate.query(query, this::mapRow, params.toArray());
  }


  @Override
  public Horse update(HorseDetailDto horse) throws NotFoundException {
    LOG.trace("update({})", horse);
    int updated = jdbcTemplate.update(SQL_UPDATE,
        horse.name(),
        horse.description(),
        horse.dateOfBirth(),
        horse.sex().toString(),
        horse.ownerId(),
        horse.motherId(),
        horse.fatherId(),
        horse.id());
    if (updated == 0) {
      throw new NotFoundException("Could not update horse with ID " + horse.id() + ", because it does not exist");
    }

    return new Horse()
        .setId(horse.id())
        .setName(horse.name())
        .setDescription(horse.description())
        .setDateOfBirth(horse.dateOfBirth())
        .setSex(horse.sex())
        .setOwnerId(horse.ownerId())
            .setMotherId(horse.motherId())
            .setFatherId(horse.fatherId())
        ;
  }


  private Horse mapRow(ResultSet result, int rownum) throws SQLException {
    LOG.trace("mapRow({}, {})", result, rownum);
    Horse horse = new Horse();
    horse.setId(result.getLong("id"));
    horse.setName(result.getString("name"));
    horse.setDescription(result.getString("description"));
    horse.setDateOfBirth(result.getDate("date_of_birth").toLocalDate());
    horse.setSex(Sex.valueOf(result.getString("sex")));
    horse.setOwnerId(result.getObject("owner_id", Long.class));
    horse.setMotherId(result.getObject("mother_id", Long.class));
    horse.setFatherId(result.getObject("father_id", Long.class));

    Long motherID = result.getObject("mother_id", Long.class);
    Long fatherID = result.getObject("father_id", Long.class);

    if (motherID != null) {
      horse.setMother(getById(motherID));
    }

    if (fatherID != null) {
      horse.setFather(getById(fatherID));
    }

    return horse;
  }

}
