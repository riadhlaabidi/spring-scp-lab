package accounts.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import accounts.AccountManager;
import common.money.Percentage;
import rewards.internal.account.Account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(AccountController.class)
public class AccountControllerBootTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountManager accountManager;

	@Test
	public void accountDetails() throws Exception {
		given(accountManager.getAccount(0L))
				.willReturn(new Account("1234567890", "John Doe"));

		mockMvc.perform(get("/accounts/0"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name").value("John Doe"))
				.andExpect(jsonPath("number").value("1234567890"));

		verify(accountManager).getAccount(0L);

	}

	@Test
	public void accountDetailsFail() throws Exception {

		given(accountManager.getAccount(any(Long.class)))
				.willThrow(new IllegalArgumentException("No such account with id " + 0L));

		mockMvc.perform(get("/accounts/9999"))
				.andExpect(status().isNotFound());

		verify(accountManager).getAccount(any(Long.class));
	}

	@Test
	public void createAccount() throws Exception {

		Account testAccount = new Account("1234512345", "Mary Jones");
		testAccount.setEntityId(21L);

		given(accountManager.save(any(Account.class)))
				.willReturn(testAccount);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testAccount)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/21"));

		verify(accountManager).save(any(Account.class));
	}

	@Test
	public void getAllAccounts() throws Exception {
		List<Account> accounts = List.of(
				new Account("11223344", "John Doe"),
				new Account("22334455", "Foo Bar"),
				new Account("33445566", "Bar Baz"));

		given(accountManager.getAllAccounts()).willReturn(accounts);

		mockMvc.perform(get("/accounts"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].number", is("11223344")))
				.andExpect(jsonPath("$[0].name", is("John Doe")))
				.andExpect(jsonPath("$[1].number", is("22334455")))
				.andExpect(jsonPath("$[1].name", is("Foo Bar")))
				.andExpect(jsonPath("$[2].number", is("33445566")))
				.andExpect(jsonPath("$[2].name", is("Bar Baz")));

		verify(accountManager).getAllAccounts();
	}

	@Test
	public void getBeneficiary() throws Exception {
		Account account = new Account("11223344", "John Doe");
		account.addBeneficiary("Jane Doe");

		given(accountManager.getAccount(any(Long.class))).willReturn(account);

		mockMvc.perform(get("/accounts/{id}/beneficiaries/{name}", 1, "Jane Doe"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name", is("Jane Doe")))
				.andExpect(jsonPath("$.allocationPercentage", is(Percentage.oneHundred().asDouble())));

		verify(accountManager).getAccount(any(Long.class));
	}

	@Test
	public void getNonExistantBeneficiary() throws Exception {
		Account account = new Account("11223344", "John Doe");

		given(accountManager.getAccount(any(Long.class))).willReturn(account);

		mockMvc.perform(get("/accounts/{id}/beneficiaries/{name}", 1, "Jane Doe"))
				.andExpect(status().isNotFound());

		verify(accountManager).getAccount(any(Long.class));
	}

	@Test
	public void addNewBeneficiary() throws Exception {
		doNothing().when(accountManager).addBeneficiary(any(Long.class), any(String.class));

		mockMvc.perform(post("/accounts/{id}/beneficiaries", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content("someone"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/accounts/1/beneficiaries/someone"));

		verify(accountManager).addBeneficiary(any(Long.class), any(String.class));
	}

	@Test
	public void removeBeneficiary() throws Exception {
		Account account = new Account("11223344", "John Doe");
		account.setEntityId(1L);
		account.addBeneficiary("someone");

		given(accountManager.getAccount(1L)).willReturn(account);

		mockMvc.perform(delete("/accounts/{id}/beneficiaries/{beneficiaryName}", 1L, "someone"))
				.andExpect(status().isNoContent());

		verify(accountManager).getAccount(1L);
	}

	@Test
	public void removeNonExistantBeneficiary() throws Exception {
		Account account = new Account("11223344", "John Doe");
		account.setEntityId(1L);

		given(accountManager.getAccount(1L)).willReturn(account);

		mockMvc.perform(get("/accounts/{id}/beneficiaries/{beneficiaryName}", 1L, "someone"))
				.andExpect(status().isNotFound());

		verify(accountManager).getAccount(1L);
	}

	// Utility class for converting an object into JSON string
	protected static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
