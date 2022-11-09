package net.absoft;

import net.absoft.services.AuthenticationService;
import org.testng.annotations.Factory;

public class DummyTestFactory {
	
	@Factory
	public Object[] factory() {
		return new Object[] {
				new AuthenticationServiceTest("one"),
				new AuthenticationServiceTest("two")
		};
	}
	
}
