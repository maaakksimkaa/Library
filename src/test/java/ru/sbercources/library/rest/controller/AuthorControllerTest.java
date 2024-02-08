package ru.sbercources.library.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.sbercources.library.CommonTest;
import ru.sbercources.library.dto.AuthorDto;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorControllerTest extends CommonTest {


  private final AuthorDto authorDto = new AuthorDto("testFIO", "testLifePeriod", "testDesc", new HashSet<>());

  @Order(2)
  @Test
  void getAuthorList() throws Exception {
    String result = mvc.perform(get("/rest/author/list").headers(headers))
        .andExpect(status().is2xxSuccessful())
        .andReturn()
        .getResponse()
        .getContentAsString();
    System.out.println(result);
  }

  @Order(1) //очередность выполнения
  @Test
  void createAuthor() throws Exception {
    headers.add("Authorization", "Bearer " + token);

    String result = mvc.perform(
            post("/rest/author")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headers)
                .content(asJsonString(authorDto)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();
    System.out.println(result);
  }

  @Test
  void deleteAuthor() throws Exception {
    mvc.perform(delete("/rest/author/54").headers(headers))
        .andExpect(status().is2xxSuccessful());
  }

  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
