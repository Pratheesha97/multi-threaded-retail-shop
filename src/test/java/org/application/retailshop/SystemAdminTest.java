package org.application.retailshop;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class SystemAdminTest {

	private SystemAdmin admin;
	private Inventory inventory;
	private ByteArrayOutputStream output;

	@Before
	public void setUp() {
		inventory = new Inventory();
		admin = new SystemAdmin("Admin1", inventory);
		output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
	}

	@Test(timeout = 5000)
	public void testRun() throws InterruptedException {
		Thread adminThread = new Thread(admin);
		adminThread.start();
		Thread.sleep(2000);
		adminThread.interrupt();
		Thread.sleep(200);
		assertTrue(output.toString().contains("Admin1 Stopped."));
	}
}
