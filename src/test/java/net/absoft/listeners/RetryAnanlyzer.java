package net.absoft.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnanlyzer implements IRetryAnalyzer {
	
	private int counter = 0;
	private int MAX_RETRIES_COUNT = 3;
	
	@Override
	public boolean retry(ITestResult result) {
		if (counter < MAX_RETRIES_COUNT) {
			counter++;
			return true;
		}
		return false;
	}
	
}
