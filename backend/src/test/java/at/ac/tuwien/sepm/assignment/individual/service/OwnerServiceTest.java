package at.ac.tuwien.sepm.assignment.individual.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import at.ac.tuwien.sepm.assignment.individual.dto.OwnerCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class OwnerServiceTest {
  @Autowired
  OwnerService ownerService;

  @Test
  @Transactional
  public void createOwnerAddsNewHorseToStorage() throws ValidationException, ConflictException {
    List<OwnerDto> owner = ownerService.allOwners()
            .toList();
    int sizeBeforeTest = owner.size();
    OwnerCreateDto createOwner = new OwnerCreateDto("Felix", "Steiner", null);
    OwnerDto ownerDto = ownerService.create(createOwner);
    List<OwnerDto> ownerList = ownerService.allOwners()
            .toList();
    int sizeAfterTest = ownerList.size();
    assertThat(sizeAfterTest).isGreaterThan(sizeBeforeTest);
    assertThat(ownerDto).extracting(OwnerDto::firstName).isEqualTo(createOwner.firstName());
  }

  @Test
  @Transactional
  public void getByIdReturnsOwner() throws NotFoundException {
    OwnerDto owner = ownerService.getById(-1);
    assertThat(owner).extracting(OwnerDto::id, OwnerDto::firstName).contains(-1L, "Max");
  }

}
