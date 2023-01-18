package at.ac.tuwien.sepm.assignment.individual.dto;

import at.ac.tuwien.sepm.assignment.individual.type.Sex;

import java.time.LocalDate;

/**
 * Dto for horses which are parents.
 * Contains all common properties but not the parents of a horse.
 */
public record HorseParentDto(
        Long id,
        String name,
        LocalDate dateOfBirth,
        Sex sex) {
}

