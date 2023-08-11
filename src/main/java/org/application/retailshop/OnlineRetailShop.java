package org.application.retailshop;

import java.util.ArrayList;
import java.util.List;

/**
 * The OnlineRetailShop class represents the main entry point of the online retail shop application.
 * It creates and manages the shopper and admin threads, and controls their execution.
 *
 *
 */
public class OnlineRetailShop {

	private static final int NO_OF_ADMINS = 8;
	private static final int NO_OF_SHOPPERS = 500;

	public static void main(String[] args) throws InterruptedException {

		Inventory inventory = new Inventory();
		List<Thread> shopperThreadArray = new ArrayList<>();
		List<Thread> adminThreadArray = new ArrayList<>();

		// Create and start admin threads
		for (int i = 1; i <= NO_OF_ADMINS; i++) {
			String adminName = "Admin" + i;
			SystemAdmin admin = new SystemAdmin(adminName, inventory);
			Thread adminThread = new Thread(admin, adminName);
			adminThreadArray.add(adminThread);
			adminThread.start();
		}

		// Create and start shopper threads
		for (int i = 1; i <= NO_OF_SHOPPERS; i++) {
			String shopperName = "Shopper" + i;
			Shopper shopper = new Shopper(shopperName, inventory);
			Thread shopperThread = new Thread(shopper, shopperName);
			shopperThreadArray.add(shopperThread);
			shopperThread.start();
		}

		// Wait for all shopper threads to terminate
		for (Thread shopperThread : shopperThreadArray) {
			shopperThread.join();
		}

		// Interrupt all the admin threads after shopper threads termination
		for (Thread adminThread : adminThreadArray) {
			adminThread.interrupt();
		}

	}

}
