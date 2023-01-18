package at.ac.tuwien.sepm.assignment.individual.dto;

/**
 * DTO to bundle all owner parameters.
 */
public record OwnerDto(
    long id,
    String firstName,
    String lastName,
    String email
) {
}
