package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import java.util.Collection;
import java.util.List;

/**
 * Data Access Object for horses.
 * Implements access functionality to the application's persistent data store regarding horses.
 */
public interface HorseDao {
  /**
   * Get all horses stored in the persistent data store.
   *
   * @return a list of all stored horses
   */
  List<Horse> getAll();


  /**
   * Update the horse with the ID given in {@code horse}
   *  with the data given in {@code horse}
   *  in the persistent data store.
   *
   * @param horse the horse to update
   * @return the updated horse
   * @throws NotFoundException if the Horse with the given ID does not exist in the persistent data store
   */
  Horse update(HorseDetailDto horse) throws NotFoundException;

  /**
   * Get a horse by its ID from the persistent data store.
   *
   * @param id the ID of the horse to get
   * @return the horse
   * @throws NotFoundException if the Horse with the given ID does not exist in the persistent data store
   */
  Horse getById(long id) throws NotFoundException;

  /**
   * Create a new horse in the persistent data store.
   *
   * @param newHorse the data to create the new horse from
   * @return the newly created horse
   */
  Horse create(HorseCreateDto newHorse);

  /**
   * Search for horse matching the criteria in {@code searchParameters}.
   * <p>
   * A horse is considered matched, if its name contains {@code searchParameters.name} as a substring.
   * The returned stream of owners never contains more than {@code searchParameters.maxAmount} elements,
   *  even if there would be more matches in the persistent data store.
   * </p>
   *
   * @param searchParameters object containing the search parameters to match
   * @return a collection containing horses matching the criteria in {@code searchParameters}
   */
  Collection<Horse> searchByName(HorseSearchDto searchParameters);

  /**
   * Delete a horse with a given ID from the persistent data store.
   *
   * @param id the ID of the horse to delete
   * @throws NotFoundException if the Horse with the given ID does not exist in the persistent data store
   */
  void deleteHorse(long id) throws NotFoundException;

  /**
   * Search for horses matching the criteria in {@code searchParameters} in the persistent data store.
   * <p>
   * A horse is considered matched, if its values contain the search parameters as a substring. The search is
   * case insensitive. The possible values are {@code searchParameters.name}, {@code searchParameters.description},
   * {@code searchParameters.bornBefore}, {@code searchParameters.sex} and {@code searchParameters.ownerName}.
   * </p>
   *
   * @param searchParameters object containing the search parameters to match
   * @return a list containing owners matching the criteria in {@code searchParameters}
   */
  List<Horse> searchHorses(HorseSearchDto searchParameters);

  /**
   * Get the horse with given ID.
   *
   * @param id the ID of the horse to get
   * @param generations number of horse generations to get the horses from
   * @return the horses with ID {@code id} and their parent horses within the choosen generations
   * @throws NotFoundException if the horse with the given ID does not exist in the persistent data store
   */
  Horse getHorseTree(Long id, Long generations) throws NotFoundException;
}
