package at.ac.tuwien.sepm.assignment.individual.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import java.time.LocalDate;
import java.util.List;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseDaoTest {

  @Autowired
  HorseDao horseDao;

  @Test
  @Transactional
  public void getAllReturnsAllStoredHorses() {
    List<Horse> horses = horseDao.getAll();
    assertThat(horses.size()).isGreaterThanOrEqualTo(10);
    assertThat(horses)
        .extracting(Horse::getId, Horse::getName)
        .contains(tuple(-1L, "Wendy"));
  }

  @Test
  @Transactional
  public void deleteNonExistingHorseReturnsNotFoundException() throws NotFoundException {
    Assertions.assertThrows(NotFoundException.class, () -> {
      horseDao.deleteHorse(-100L);
    });
  }

  @Test
  @Transactional
  public void searchHorseEmptyReturnsAllHorses() {
    HorseSearchDto testHorse = new HorseSearchDto(null, null, null, null, null, null);
    List<Horse> horses = horseDao.searchHorses(testHorse);
    assertThat(horses.size()).isGreaterThanOrEqualTo(10);
    assertThat(horses)
            .extracting(Horse::getId, Horse::getName)
            .contains(tuple(-1L, "Wendy"))
            .contains(tuple(-10L, "Tanya"));

  }

  @Test
  @Transactional
  public void updateHorseChangesHorsesValues() throws NotFoundException {
    Horse beforeEdit = horseDao.getById(-5);

    HorseDetailDto horseDetail = new HorseDetailDto(-5L, "Felix", null, LocalDate.parse("2008-01-01"), Sex.MALE, null, null, null);

    Horse afterEdit = horseDao.update(horseDetail);
    assertThat(afterEdit).isNotEqualTo(beforeEdit);
    assertThat(afterEdit.getId()).isEqualTo(-5);
    assertThat(afterEdit.getName()).isEqualTo("Felix");
  }




}
