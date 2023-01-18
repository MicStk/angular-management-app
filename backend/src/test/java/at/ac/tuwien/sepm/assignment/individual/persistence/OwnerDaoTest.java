package at.ac.tuwien.sepm.assignment.individual.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import at.ac.tuwien.sepm.assignment.individual.entity.Owner;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class OwnerDaoTest {

  @Autowired
  OwnerDao ownerDao;

  @Test
  @Transactional
  public void getOwnerByIDReturnsOwner() throws NotFoundException {
    Owner owner = ownerDao.getById(-1);
    assertThat(owner.getFirstName()).isEqualTo("Max");
    assertThat(owner.getLastName()).isEqualTo("Mustermann");
    assertThat(owner.getEmail()).isEqualTo("max@email.com");
    assertThat(owner.getId()).isEqualTo(-1);
  }


  @Test
  @Transactional
  public void getOwnerByNonExistentIDThrowsNotFoundException() throws NotFoundException {
    Assertions.assertThrows(NotFoundException.class, () -> {
      ownerDao.getById(-100L);
    });
  }



}
