package br.com.cotiinformatica;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserResponseDto;
import br.com.cotiinformatica.domain.dtos.CreateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.CreateUserResponseDto;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersApiApplicationTests {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	private Faker faker;

	private static String userEmail;
	private static String userPass;

	@BeforeEach
	void setUp() {
		faker = new Faker(new Locale("pt-BR"));
	}

	@Test
	@Order(1)
	void shouldCreateUserSuccessfully() throws Exception {

		CreateUserRequestDto request = new CreateUserRequestDto();
		request.setName(faker.name().fullName());
		request.setEmail(faker.internet().emailAddress());
		request.setPassword(faker.internet().password(8, 10, true, true, true));

		MvcResult result = mockMvc.perform(post("/api/users/create")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andReturn();

		CreateUserResponseDto response = getResponse(result, CreateUserResponseDto.class);

		assertNotNull(response.getId());
		assertEquals(request.getName(), response.getName());
		assertEquals(request.getEmail(), response.getEmail());
		assertEquals("DEFAULT", response.getRole());
		assertNotNull(response.getCreatedAt());

		userEmail = request.getEmail();
		userPass = request.getPassword();
	}

	@Test
	@Order(2)
	void shouldAuthenticateUserSuccessfully() throws Exception {

		AuthenticateUserRequestDto request = new AuthenticateUserRequestDto();
		request.setEmail(userEmail);
		request.setPassword(userPass);

		MvcResult result = mockMvc.perform(post("/api/users/authenticate")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();

		AuthenticateUserResponseDto response = getResponse(result, AuthenticateUserResponseDto.class);

		assertNotNull(response.getId());
		assertEquals(request.getEmail(), response.getEmail());
		assertEquals("DEFAULT", response.getRole());
		assertNotNull(response.getAccessToken());
		assertNotNull(response.getExpiration());
	}

	private <T> T getResponse(MvcResult result, Class<T> responseType) throws Exception {
		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		return objectMapper.readValue(content, responseType);
	}
}