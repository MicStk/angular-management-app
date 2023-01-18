package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseTreeDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;

import java.util.stream.Stream;

/**
 * Service for working with horses.
 */
public interface HorseService {
  /**
   * Lists all horses stored in the system.
   *
   * @return list of all stored horses
   */
  Stream<HorseListDto> allHorses();


  /**
   * Updates the horse with the ID given in {@code horse}
   * with the data given in {@code horse}
   * in the persistent data store.
   *
   * @param horse the horse to update
   * @return he updated horse
   * @throws NotFoundException if the horse with given ID does not exist in the persistent data store
   * @throws ValidationException if the update data given for the horse is in itself incorrect (description too long, no name, …)
   * @throws ConflictException if the update data given for the horse is in conflict the data currently in the system (owner does not exist, …)
   */
  HorseDetailDto update(HorseDetailDto horse) throws NotFoundException, ValidationException, ConflictException;


  /**
   * Get the horse with given ID, with more detail information.
   * This includes the owner of the horse, and its parents.
   * The parents of the parents are not included.
   *
   * @param id the ID of the horse to get
   * @return the horse with ID {@code id}
   * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
   */
  HorseDetailDto getById(long id) throws NotFoundException;

  /**
   * Create a new horse in the persistent data store.
   *
   * @param newHorse the data for the new horse
   * @return the horse, that was just newly created in the persistent data store
   * @throws ValidationException if the parameters are not allowed
   */
  HorseDto create(HorseCreateDto newHorse) throws ValidationException, ConflictException;

  /**
   * Search for horses matching the criteria in {@code searchParameters}.
   * <p>
   * A horse is considered matched, if its name contains {@code searchParameters.name} as a substring.
   * The returned stream of horses never contains more than {@code searchParameters.maxAmount} elements,
   *  even if there would be more matches in the persistent data store.
   * </p>
   *
   * @param searchParameters object containing the search parameters to match
   * @return a stream containing owners matching the criteria in {@code searchParameters}
   */
  Stream<HorseDto> searchByName(HorseSearchDto searchParameters);

  /**
   * Delete the horse with given ID.
   * Parents/child relationships gets removed once a horse is deleted.
   *
   * @param id the ID of the horse to delete
   * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
   */
  void deleteHorse(long id) throws NotFoundException;

  /**
   * Search for horses matching the criteria in {@code searchParameters}.
   * <p>
   * A horse is considered matched, if its values contain the search parameters as a substring. The search is
   * case insensitive. The possible values are {@code searchParameters.name}, {@code searchParameters.description},
   * {@code searchParameters.bornBefore}, {@code searchParameters.sex} and {@code searchParameters.ownerName}.
   * </p>
   *
   * @param searchParameters object containing the search parameters to match
   * @return a stream containing owners matching the criteria in {@code searchParameters}
   */
  Stream<HorseListDto> searchHorses(HorseSearchDto searchParameters);

  /**
   * Get the horse with given ID.
   *
   * @param id the ID of the horse to get
   * @param generations number of horse generations to get the horses from
   * @return the horses with ID {@code id} and their parent horses within the choosen generations
   * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
   */
  HorseTreeDto getHorseTree(Long id, Long generations) throws NotFoundException;
}
