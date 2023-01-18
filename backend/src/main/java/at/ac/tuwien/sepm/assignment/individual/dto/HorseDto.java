package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.type.Sex;

import java.time.LocalDate;

/**
 * DTO to bundle the parameters used for a simple horse dto.
 * Parents and owners are saved as a key.
 */
public record HorseDto(Long id,
                       String name,
                       String description,
                       LocalDate dateOfBirth,
                       Sex sex,
                       Long ownerID,
                       Long motherID,
                       Long fatherID) {
}
