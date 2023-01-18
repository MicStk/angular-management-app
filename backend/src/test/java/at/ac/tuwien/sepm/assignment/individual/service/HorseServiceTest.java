package at.ac.tuwien.sepm.assignment.individual.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepm.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepm.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseServiceTest {

  @Autowired
  HorseService horseService;

  @Test
  @Transactional
  public void getAllReturnsAllStoredHorses() {
    List<HorseListDto> horses = horseService.allHorses()
        .toList();
    assertThat(horses.size()).isGreaterThanOrEqualTo(10); // TODO adapt to exact number of elements in test data later
    assertThat(horses)
        .map(HorseListDto::id, HorseListDto::sex)
        .contains(tuple(-1L, Sex.FEMALE))
        .contains(tuple(-10L, Sex.FEMALE));
  }

  @Test
  @Transactional
  public void getByIdReturnsHorse() throws NotFoundException {
    HorseDetailDto horse = horseService.getById(-1);
    assertThat(horse).extracting(HorseDetailDto::id, HorseDetailDto::name).contains(-1L, "Wendy");
  }

  @Test
  @Transactional
  public void createHorseAddsNewHorseToStorage() throws ValidationException, ConflictException {
    List<HorseListDto> horses = horseService.allHorses()
            .toList();
    int sizeBeforeTest = horses.size();
    HorseCreateDto createHorse = new HorseCreateDto("Felix", null, LocalDate.parse("2008-01-01"), Sex.MALE, null, null, null);
    HorseDto horseDto = horseService.create(createHorse);
    List<HorseListDto> horseList = horseService.allHorses()
            .toList();
    int sizeAfterTest = horseList.size();
    assertThat(sizeAfterTest).isGreaterThan(sizeBeforeTest);
    assertThat(horseDto).extracting(HorseDto::name).isEqualTo(horseDto.name());
  }

  @Test
  @Transactional
  public void updateHorseWithInvalidErrorsThrowsValidationError() throws ValidationException, ConflictException {

    HorseDetailDto updateHorse = new HorseDetailDto(-6L, "Felix", null, LocalDate.parse("2024-01-01"), Sex.MALE, null, null, null);
    Assertions.assertThrows(ValidationException.class, () -> {
      horseService.update(updateHorse);
    });
  }

}
