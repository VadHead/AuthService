package net.absoft;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
	
	@BeforeMethod
	public void baseSetup(){
		System.out.println("Base Setup");
	}
	
	@AfterMethod
	public void baseTearDown() {
		System.out.println("Base TearDown");
	}
	
}
