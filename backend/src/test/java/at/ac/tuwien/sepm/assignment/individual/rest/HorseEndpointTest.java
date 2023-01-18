package at.ac.tuwien.sepm.assignment.individual.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@EnableWebMvc
@WebAppConfiguration
@SpringBootTest
public class HorseEndpointTest {

  @Autowired
  private WebApplicationContext webAppContext;
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
  }

  @Test
  @Transactional
  public void gettingAllHorses() throws Exception {
    byte[] body = mockMvc
        .perform(MockMvcRequestBuilders
            .get("/horses/all")
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsByteArray();

    List<HorseListDto> horseResult = objectMapper.readerFor(HorseListDto.class).<HorseListDto>readValues(body).readAll();

    assertThat(horseResult).isNotNull();
    assertThat(horseResult.size()).isGreaterThanOrEqualTo(10);
    assertThat(horseResult)
        .extracting(HorseListDto::id, HorseListDto::name)
        .contains(tuple(-1L, "Wendy"));
  }

  @Test
  @Transactional
  public void gettingNonexistentUrlReturns404() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders
            .get("/asdf123")
        ).andExpect(status().isNotFound());
  }


  @Test
  @Transactional
  public void deletingNonExistentHorseReturns404() throws Exception {
    mockMvc
            .perform(MockMvcRequestBuilders
                    .delete("/horses/" + -100)
            ).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void gettingHorseReturnsOkAndHorse() throws Exception {
    mockMvc
            .perform(MockMvcRequestBuilders
                    .get("/horses/" + -1)
            ).andExpect(status().isOk());

    byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                    .get("/horses/-1")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

    List<HorseListDto> horseResult = objectMapper.readerFor(HorseListDto.class).<HorseListDto>readValues(body).readAll();

    assertThat(horseResult).isNotNull();
    assertThat(horseResult.size()).isEqualTo(1);
    assertThat(horseResult)
            .extracting(HorseListDto::id, HorseListDto::name)
            .contains(tuple(-1L, "Wendy"));
  }

  @Test
  @Transactional
  public void postHorseReturnsCreated() throws Exception {
    HorseCreateDto newHorse = new HorseCreateDto("Jacob", null, LocalDate.parse("2011-01-01"), Sex.MALE, null, null, null);
    byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                    .post("/horses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newHorse)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsByteArray();

    List<HorseDto> horses = objectMapper.readerFor(HorseDto.class).<HorseDto>readValues(body).readAll();
    assertThat(horses).extracting(HorseDto::name)
            .contains("Jacob");
  }

}
