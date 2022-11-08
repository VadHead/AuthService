package net.absoft;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

public class AuthenticationServiceTest {
	
	public static int count = 0;
	
	@Test(
			description = "Successful Authentication Test",
			groups = "positive",
			invocationCount = 3,
			successPercentage = 60
	)
	public void testSuccessfulAuthentication()
			throws InterruptedException {
		Response response = new AuthenticationService().authenticate("user1@test.com", "password1");
		assertEquals(response.getCode(), 200, "Response code should be 200");
		assertTrue(validateToken(response.getMessage()),
				"Token should be the 32 digits string. Got: " + response.getMessage());
		System.out.println("testSuccessfulAuthentication");
		
		fail("Fail");
	}
	
	@Test(
			enabled = false,
			groups = "negative"
	)
	public void testAuthenticationWithWrongPassword() {
		validateErrorResponse(new AuthenticationService().authenticate("user1@test.com", "wrong_password1"), 401,
				"Invalid email or password");
		System.out.println("testAuthenticationWithWrongPassword");
	}
	
	private void validateErrorResponse(Response response, int code, String message) {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(response.getCode(), code, "Response code should be 401");
		softAssert.assertEquals(response.getMessage(), message,
				"Response message should be \"Invalid email or password\""); softAssert.assertAll();
	}
	
	@Test(
			priority = 3,
			groups = "negative",
			dependsOnMethods = {"testAuthenticationWithEmptyPassword"},
			alwaysRun = true
	)
	public void testAuthenticationWithEmptyEmail() {
		Response expectedResponse = new Response(400, "Email should not be empty string");
		Response actualResponse = new AuthenticationService().authenticate("", "password1");
		assertEquals(actualResponse, expectedResponse, "Unexpected response");
		System.out.println("testAuthenticationWithEmptyEmail");
	}
	
	@Test(
			groups = "negative",
			timeOut = 2000
	)
	public void testAuthenticationWithInvalidEmail() throws InterruptedException {
		Response response = new AuthenticationService().authenticate("user1", "password1");
		assertEquals(response.getCode(), 400, "Response code should be 200");
		assertEquals(response.getMessage(), "Invalid email", "Response message should be \"Invalid email\"");
		System.out.println("testAuthenticationWithInvalidEmail");
		Thread.sleep(3000);
	}
	
	@Test(
			groups = "negative",
			priority = 2,
			dependsOnMethods = {"testAuthenticationWithInvalidEmail"}
	)
	public void testAuthenticationWithEmptyPassword() {
		Response response = new AuthenticationService().authenticate("user1@test", "");
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
