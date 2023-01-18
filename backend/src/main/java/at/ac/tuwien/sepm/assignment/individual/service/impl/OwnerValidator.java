package at.ac.tuwien.sepm.assignment.individual.service.impl;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerCreateDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OwnerValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  public void validateForCreate(OwnerCreateDto owner) throws ValidationException {
    LOG.trace("validateForCreate({})", owner);
    List<String> validationErrors = new ArrayList<>();


    if (owner.firstName() == null) {
      validationErrors.add("No first name given");
    } else {
      if (owner.firstName().isBlank()) {
        validationErrors.add("Owner first name is given but blank");
      }
      if (owner.firstName().length() > 255) {
        validationErrors.add("Owner first name too long: longer than 255 characters");
      }
    }

    if (owner.lastName() == null) {
      validationErrors.add("No last name given");
    } else {
      if (owner.lastName().isBlank()) {
        validationErrors.add("Owner last name is given but blank");
      }
      if (owner.lastName().length() > 255) {
        validationErrors.add("Owner last name too long: longer than 255 characters");
      }
    }
    if (owner.email() != null) {
      if (owner.email().length() > 255) {
        validationErrors.add("Owner email too long: longer than 255 characters");
      }
      if (!owner.email().contains("@")) {
        validationErrors.add("Owner email must be in the format xxx@xxx.xx");
      } else {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(owner.email());
        if (!matcher.matches()) {
          validationErrors.add("Owner email must be in a valid email format: The format xxx@xxx.xx");
        }
      }
    }

    if (!validationErrors.isEmpty()) {
      LOG.error("[ValidationException]: Error while creating owner");
      throw new ValidationException("Validation of owner for create failed", validationErrors);
    }
  }
}
