package net.absoft;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

public class AuthenticationServiceTest {
	
	private AuthenticationService authenticationService;
	
	@BeforeGroups(groups = {"positive", "negative"})
	public void setUp() {
		authenticationService = new AuthenticationService();
		System.out.println("Setup \"authenticationService\"");
	}
	
	@AfterSuite(groups = {"negative"})
		public void afterMessage(){
			System.out.println("\"Negative suite\" run finished");
		}
		
	@Test(
			groups = "positive"
	)
	public void testSample() {
		System.out.println("testSample:" + new Date());
		fail("Failing test");
	}
	
	@Test(
			description = "Successful Authentication Test",
			groups = "positive",
			dependsOnMethods = "testSample"
	)
	public void testSuccessfulAuthentication() {
		Response response = authenticationService.authenticate("user1@test.com", "password1");
		assertEquals(response.getCode(), 200, "Response code should be 200");
		assertTrue(validateToken(response.getMessage()),
				"Token should be the 32 digits string. Got: " + response.getMessage());
		System.out.println("testSuccessfulAuthentication:" + new Date());
	}
	
	@DataProvider(name = "invalidLogins", parallel = true)
	public Object[][] invalidLogins() {
		return new Object[][] {
				new Object[] {"user1@test.com", "wrong_password1", new Response(401, "Invalid email or password")},
				new Object[] {"", "password1", new Response(400, "Email should not be empty string")},
				new Object[] {"user1@test.com", "", new Response(400, "Password should not be empty string")},
				new Object[] {"user1", "password1", new Response(400, "Invalid email")}
		};
	}
	
	@Test(
			groups = "negative",
			dataProvider = "invalidLogins"
	)
	public void testInvalidAuthentication(String email, String password, Response expectedResponse) {
		Response actualResponse = authenticationService.authenticate("user1@test.com", "wrong_password1");
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(actualResponse.getCode(), expectedResponse.getCode(), "Response code should be 401");
		softAssert.assertEquals(actualResponse.getMessage(), expectedResponse.getMessage(),
				"Response message should be \"Invalid email or password\"");
		softAssert.assertAll();
		System.out.println("testInvalidAuthentication");
	}
	
	@Test(
			groups = "negative"
	)
	public void testAuthenticationWithEmptyEmail() {
		Response expectedResponse = new Response(400, "Email should not be empty string");
		Response actualResponse = authenticationService.authenticate("", "password1");
		assertEquals(actualResponse, expectedResponse, "Unexpected response");
		System.out.println("testAuthenticationWithEmptyEmail");
	}
	
	@Test(
			groups = "negative"
	)
	public void testAuthenticationWithInvalidEmail() {
		Response response = authenticationService.authenticate("user1", "password1");
		assertEquals(response.getCode(), 400, "Response code should be 200");
		assertEquals(response.getMessage(), "Invalid email", "Response message should be \"Invalid email\"");
		System.out.println("testAuthenticationWithInvalidEmail");
	}
	
	@Test(
			groups = "negative"
	)
	public void testAuthenticationWithEmptyPassword() {
		Response response = authenticationService.authenticate("user1@test", "");
		assertEquals(response.getCode(), 400, "Response code should be 400");
		assertEquals(response.getMessage(), "Password should not be empty string",
				"Response message should be \"Password should not be empty string\"");
		System.out.println("testAuthenticationWithEmptyPassword");
	}
	
	private boolean validateToken(String token) {
		final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(token); return matcher.matches();
	}
	
}
