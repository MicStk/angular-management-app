package at.ac.tuwien.sepm.assignment.individual.mapper;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseParentDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseTreeDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.FatalException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a horse entity object to a {@link HorseListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse  the horse to convert
   * @param owners a map of horse owners by their id, which needs to contain the owner referenced by {@code horse}
   * @return the converted {@link HorseListDto}
   */
  public HorseListDto entityToListDto(Horse horse, Map<Long, OwnerDto> owners) {
    LOG.trace("entityToDto({})", horse);
    if (horse == null) {
      return null;
    }

    return new HorseListDto(
            horse.getId(),
            horse.getName(),
            horse.getDescription(),
            horse.getDateOfBirth(),
            horse.getSex(),
            getOwner(horse, owners)
    );
  }

  /**
   * Convert a horse entity object to a {@link HorseListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse  the horse to convert
   * @param owners a map of horse owners by their id, which needs to contain the owner referenced by {@code horse}
   * @return the converted {@link HorseListDto}
   */
  public HorseDetailDto entityToDetailDto(
          Horse horse,
          Map<Long, OwnerDto> owners, HorseParentDto mother, HorseParentDto father) {
    LOG.trace("entityToDto({})", horse);
    if (horse == null) {
      return null;
    }

    return new HorseDetailDto(
            horse.getId(),
            horse.getName(),
            horse.getDescription(),
            horse.getDateOfBirth(),
            horse.getSex(),
            getOwner(horse, owners),
            mother,
            father
    );
  }

  private HorseDetailDto getMother(Horse horse, Map<Long, HorseDetailDto> horses) {
    LOG.trace("getMother({}, {})", horse, horses);
    HorseDetailDto mother = null;
    var motherId = horse.getMotherId();
    if (motherId != null) {
      if (!horses.containsKey(motherId)) {
        throw new FatalException("Given horse map does not contain mother of this Horse (%d)".formatted(horse.getId()));
      }
      mother = horses.get(motherId);
    }
    return mother;
  }

  private HorseDetailDto getFather(Horse horse, Map<Long, HorseDetailDto> horses) {
    LOG.trace("getFather({}, {})", horse, horses);
    HorseDetailDto father = null;
    var fatherId = horse.getFatherId();
    if (fatherId != null) {
      if (!horses.containsKey(fatherId)) {
        throw new FatalException("Given horse map does not contain father of this Horse (%d)".formatted(horse.getId()));
      }
      father = horses.get(fatherId);
    }
    return father;
  }

  private OwnerDto getOwner(Horse horse, Map<Long, OwnerDto> owners) {
    LOG.trace("getOwner({}, {})", horse, owners);
    OwnerDto owner = null;
    var ownerId = horse.getOwnerId();
    if (ownerId != null) {
      if (!owners.containsKey(ownerId)) {
        throw new FatalException("Given owner map does not contain owner of this Horse (%d)".formatted(horse.getId()));
      }
      owner = owners.get(ownerId);
    }
    return owner;
  }


  /**
   * Convert a horse entity object to a {@link HorseDto}.
   *
   * @param horse  the horse to convert
   * @return the converted {@link HorseDto}
   */
  public HorseDto entityToDto(Horse horse) {
    LOG.trace("entityToDto({})", horse);
    if (horse == null) {
      return null;
    }
    return new HorseDto(
            horse.getId(),
            horse.getName(),
            horse.getDescription(),
            horse.getDateOfBirth(),
            horse.getSex(),
            horse.getOwnerId(),
            horse.getMotherId(),
            horse.getFatherId());
  }


  /**
   * Convert a horse entity object to a {@link HorseParentDto}.
   *
   * @param horse  the horse to convert
   * @return the converted {@link HorseParentDto}
   */
  public HorseParentDto entityToParentDto(Horse horse) {
    LOG.trace("entityToParentDto({})", horse);
    if (horse == null) {
      return null;
    }
    return new HorseParentDto(
            horse.getId(),
            horse.getName(),
            horse.getDateOfBirth(),
            horse.getSex()
    );
  }


  /**
   * Convert a horse entity object to a {@link HorseTreeDto}.
   *
   * @param horse  the horse to convert
   * @param generations number of parents generations included in the tree
   * @return the converted {@link HorseTreeDto}
   */
  public HorseTreeDto entityToTreeDto(Horse horse, Long generations) throws NotFoundException {
    LOG.trace("entityToTreeDto({}, {})", horse, generations);

    if (horse == null) {
      return null;
    }

    HorseTreeDto mother = null;
    HorseTreeDto father = null;

    if (generations > 0) {
      mother = entityToTreeDto(horse.getMother(), generations - 1);
      father = entityToTreeDto(horse.getFather(), generations - 1);
    }

    return new HorseTreeDto(horse.getId(), horse.getName(), horse.getDateOfBirth(), mother, father);

  }

}
