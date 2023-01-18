package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseParentDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseTreeDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepm.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepm.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepm.assignment.individual.service.HorseService;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HorseServiceImpl implements HorseService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final HorseDao dao;
  private final HorseMapper mapper;
  private final HorseValidator validator;
  private final OwnerService ownerService;

  public HorseServiceImpl(HorseDao dao, HorseMapper mapper, HorseValidator validator, OwnerService ownerService) {
    this.dao = dao;
    this.mapper = mapper;
    this.validator = validator;
    this.ownerService = ownerService;
  }

  @Override
  public Stream<HorseListDto> allHorses() {
    LOG.trace("allHorses()");
    var horses = dao.getAll();
    var ownerIds = horses.stream()
            .map(Horse::getOwnerId)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableSet());
    Map<Long, OwnerDto> ownerMap;
    try {
      ownerMap = ownerService.getAllById(ownerIds);
    } catch (NotFoundException e) {
      throw new FatalException("Horse, that is already persisted, refers to non-existing owner", e);
    }

    return horses.stream()
            .map(horse -> mapper.entityToListDto(horse, ownerMap));
  }


  @Override
  public HorseDetailDto update(HorseDetailDto horse) throws NotFoundException, ValidationException, ConflictException {
    LOG.trace("update({})", horse);
    validator.validateForUpdate(horse);
    var updatedHorse = dao.update(horse);
    return mapper.entityToDetailDto(
            updatedHorse,
            ownerMapForSingleId(updatedHorse.getOwnerId()), getParentsById(updatedHorse.getMotherId()), getParentsById(updatedHorse.getFatherId()));
  }

  @Override
  public HorseDetailDto getById(long id) throws NotFoundException {
    LOG.trace("details({})", id);
    Horse horse = dao.getById(id);
    return mapper.entityToDetailDto(
            horse,
            ownerMapForSingleId(horse.getOwnerId()), getParentsById(horse.getMotherId()), getParentsById(horse.getFatherId()));
  }


  @Override
  public void deleteHorse(long id) throws NotFoundException {
    LOG.trace("deleteHorse({})", id);
    dao.deleteHorse(id);
  }

  @Override
  public Stream<HorseListDto> searchHorses(HorseSearchDto searchParameters) {
    LOG.trace("searchHorses({})", searchParameters);
    var horses = dao.searchHorses(searchParameters);
    var ownerIds = horses.stream()
            .map(Horse::getOwnerId)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableSet());
    Map<Long, OwnerDto> ownerMap;
    try {
      ownerMap = ownerService.getAllById(ownerIds);
    } catch (NotFoundException e) {
      throw new FatalException("Horse, that is already persisted, refers to non-existing owner", e);
    }
    return horses.stream()
            .map(horse -> mapper.entityToListDto(horse, ownerMap));
  }

  @Override
  public HorseTreeDto getHorseTree(Long id, Long generations) throws NotFoundException {
    LOG.trace("getHorseTree({})", id);
    return mapper.entityToTreeDto(dao.getHorseTree(id, generations), generations);
  }

  @Override
  public HorseDto create(HorseCreateDto newHorse) throws ValidationException, ConflictException {
    LOG.trace("create({})", newHorse);
    validator.validateForCreate(newHorse);
    return mapper.entityToDto(dao.create(newHorse));
  }

  @Override
  public Stream<HorseDto> searchByName(HorseSearchDto searchParameters) {
    LOG.trace("searchByName({})", searchParameters);
    return dao.searchByName(searchParameters).stream()
            .map(mapper::entityToDto);
  }

  private Map<Long, OwnerDto> ownerMapForSingleId(Long ownerId) {
    LOG.trace("ownerMapForSingleId({})", ownerId);
    try {
      return ownerId == null
              ? null
              : Collections.singletonMap(ownerId, ownerService.getById(ownerId));
    } catch (NotFoundException e) {
      throw new FatalException("Owner %d referenced by horse not found".formatted(ownerId));
    }
  }

  private HorseParentDto getParentsById(Long parentId) {
    LOG.trace("getParentsById({})", parentId);
    if (parentId != null) {
      try {
        return mapper.entityToParentDto(dao.getById(parentId));
      } catch (NotFoundException e) {
        throw new FatalException("Horse %d referenced by horse not found".formatted(parentId));
      }
    } else {
      return null;
    }
  }
}
