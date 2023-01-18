package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HorseValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void validateForUpdate(HorseDetailDto horse) throws ValidationException, ConflictException {
    LOG.trace("validateForUpdate({})", horse);
    List<String> validationErrors = new ArrayList<>();
    List<String> conflictErrors = new ArrayList<>();

    if (horse.id() == null) {
      validationErrors.add("No ID given");
    }

    if (horse.name() == null) {
      validationErrors.add("No name given");
    } else {
      if (horse.name().isBlank()) {
        validationErrors.add("Horse name is given but blank");
      }
      if (horse.name().length() > 255) {
        validationErrors.add("Horse name too long: longer than 255 characters");
      }
    }

    if (horse.description() != null) {
      if (horse.description().isBlank()) {
        validationErrors.add("Horse description is given but blank");
      }
      if (horse.description().length() > 4095) {
        validationErrors.add("Horse description too long: longer than 4095 characters");
      }
    }

    if (horse.dateOfBirth() == null) {
      validationErrors.add("No dateOfBirth given");
    } else {
      if (horse.dateOfBirth().isAfter(LocalDate.now())) {
        validationErrors.add("Horse birthday is in the future");
      }
    }

    if (horse.sex() == null) {
      validationErrors.add("No sex given");
    }

    if (horse.mother() != null) {
      if (!horse.mother().sex().toString().equals("FEMALE")) {
        conflictErrors.add("Sex of mother is not female");
      }
      if (horse.dateOfBirth() != null) {
        if (!horse.mother().dateOfBirth().isBefore(horse.dateOfBirth())) {
          conflictErrors.add("Mother horse has to be born before the child horse.");
        }
      }

    }

    if (horse.father() != null) {
      if (!horse.father().sex().toString().equals("MALE")) {
        conflictErrors.add("Sex of father is not male");
      }
      if (horse.dateOfBirth() != null) {
        if (!horse.father().dateOfBirth().isBefore(horse.dateOfBirth())) {
          conflictErrors.add("Father horse has to be born before the child horse.");
        }
      }

    }

    if (!validationErrors.isEmpty()) {
      LOG.error("[ValidationException]: Error while updating horse");
      throw new ValidationException("Validation of horse for update failed", validationErrors);
    }


    if (!conflictErrors.isEmpty()) {
      LOG.error("[ConflictException]: Error while updating horse");
      throw new ConflictException("Validation of horse for update failed", conflictErrors);
    }
  }

  public void validateForCreate(HorseCreateDto horse) throws ValidationException, ConflictException {
    LOG.trace("validateForCreate({})", horse);
    List<String> validationErrors = new ArrayList<>();
    List<String> conflictErrors = new ArrayList<>();

    if (horse.name() == null) {
      validationErrors.add("No name given");
    } else {
      if (horse.name().isBlank()) {
        validationErrors.add("Horse name is given but blank");
      }
      if (horse.name().length() > 255) {
        validationErrors.add("Horse name too long: longer than 255 characters");
      }
    }

    if (horse.description() != null) {
      if (horse.description().isBlank()) {
        validationErrors.add("Horse description is given but blank");
      }
      if (horse.description().length() > 4095) {
        validationErrors.add("Horse description too long: longer than 4095 characters");
      }
    }

    if (horse.dateOfBirth() == null) {
      validationErrors.add("No dateOfBirth given");
    } else {
      if (horse.dateOfBirth().isAfter(LocalDate.now())) {
        validationErrors.add("Horse birthday is in the future");
      }
    }

    if (horse.sex() == null) {
      validationErrors.add("No sex given");
    }

    if (horse.mother() != null) {
      if (!horse.mother().sex().toString().equals("FEMALE")) {
        conflictErrors.add("Sex of mother is not female");
      }
      if (horse.dateOfBirth() != null) {
        if (!horse.mother().dateOfBirth().isBefore(horse.dateOfBirth())) {
          conflictErrors.add("Mother horse has to be born before the child horse.");
        }
      }

    }

    if (horse.father() != null) {
      if (!horse.father().sex().toString().equals("MALE")) {
        conflictErrors.add("Sex of father is not male");
      }
      if (horse.dateOfBirth() != null) {
        if (!horse.father().dateOfBirth().isBefore(horse.dateOfBirth())) {
          conflictErrors.add("Father horse has to be born before the child horse.");
        }
      }

    }

    // TODO this is not complete…

    if (!validationErrors.isEmpty()) {
      LOG.error("[ValidationException]: Error while creating horse");
      throw new ValidationException("Validation of horse for create failed", validationErrors);
    }


    if (!conflictErrors.isEmpty()) {
      LOG.error("[ConflictException]: Error while create horse");
      throw new ConflictException("Validation of horse for create failed", conflictErrors);
    }

  }

}
