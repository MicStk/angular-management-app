package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import java.time.LocalDate;

/**
 * DTO to bundle the parameters used for creating a horse.
 * The description and the owner field are optional and can therefore be null.
 */
public record HorseCreateDto(
        String name,
        String description,
        LocalDate dateOfBirth,
        Sex sex,
        OwnerDto owner,
        HorseDto mother,
        HorseDto father
) { public Long ownerId() {
    return owner == null ? null : owner.id();
  }
  public Long motherId() {
    return mother == null ? null : mother.id();
  }
  public Long fatherId() {
    return father == null ? null : father.id();
  }

}
