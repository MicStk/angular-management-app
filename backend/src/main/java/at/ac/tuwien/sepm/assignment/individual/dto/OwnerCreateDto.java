package at.ac.tuwien.sepm.assignment.individual.dto;

/**
 * DTO to bundle the parameters used for creating a owner.
 */
public record OwnerCreateDto(
    String firstName,
    String lastName,
    String email
) {
}
