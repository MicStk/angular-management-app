package at.ac.tuwien.sepm.assignment.individual.dto;

import java.time.LocalDate;

/**
 * DTO for creating a family tree.
 * Includes all parameters necessary for creating a tree.
 */
public record HorseTreeDto(
        Long id,
        String name,
        LocalDate dateOfBirth,
        HorseTreeDto mother,
        HorseTreeDto father) {
}
