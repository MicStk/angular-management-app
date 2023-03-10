package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerSearchDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepm.assignment.individual.service.OwnerService;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(OwnerEndpoint.BASE_PATH)
public class OwnerEndpoint {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  static final String BASE_PATH = "/owners";

  private final OwnerService service;

  public OwnerEndpoint(OwnerService service) {
    this.service = service;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Stream<OwnerDto> allOwners() {
    LOG.info("GET " + BASE_PATH);
    return service.allOwners();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OwnerDto post(@RequestBody OwnerCreateDto toCreate) throws ValidationException {
    LOG.info("POST " + BASE_PATH +  "/{}", toCreate.firstName() + " " + toCreate.lastName());
    try {
      return service.create(toCreate);
    } catch (ValidationException e) {
      LOG.error("[ValidationException]: Error while creating Owner " + toCreate.firstName() + " " + toCreate.lastName());
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
              "Error while creating owner", e);
    }

  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public Stream<OwnerDto> search(OwnerSearchDto searchParameters) {
    LOG.info("GET " + BASE_PATH + "/search" + " query parameters: {}", searchParameters);
    return service.search(searchParameters);
  }

}
