package at.ac.tuwien.sepm.assignment.individual.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OwnerEndpointTest {
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
  public void gettingAllOwners() throws Exception {
    byte[] body = mockMvc
              .perform(MockMvcRequestBuilders
                      .get("/owners/")
                      .accept(MediaType.APPLICATION_JSON)
              ).andExpect(status().isOk())
              .andReturn().getResponse().getContentAsByteArray();
    List<Object> ownerResult = objectMapper.readerFor(OwnerDto.class).readValues(body).readAll();
    assertThat(ownerResult).isNotNull();
    assertThat(ownerResult.size()).isGreaterThanOrEqualTo(10);
  }

  @Test
  @Transactional
  public void gettingNonExistentOwnerReturns404() throws Exception {
    mockMvc
            .perform(MockMvcRequestBuilders
                    .get("/owners/" + -100)
            ).andExpect(status().isNotFound());
  }


}
