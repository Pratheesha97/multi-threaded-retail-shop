package org.application.retailshop;

/**
 * The SystemAdmin class represents a system administrator in the online retail shop.
 */
public class SystemAdmin implements Runnable {

	private String adminName;
	private Inventory inventory;

	/**
	 * Creates a new instance of the SystemAdmin class.
	 *
	 * @param adminName  The name of the system administrator.
	 * @param inventory  The inventory of the online retail shop.
	 */
	public SystemAdmin(String adminName, Inventory inventory) {
		this.adminName = adminName;
		this.inventory = inventory;
	}

	/**
	 * Retrieves the name of the system administrator.
	 *
	 * @return The system administrator name.
	 */
	public String getName() {
		return adminName;
	}

	/**
	 * Executes the task of the system administrator.
	 */
	@Override
	public void run() {
		String[] productNames = inventory.getProductList().keySet().toArray(new String[0]);

		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			for (String productName : productNames) {
				inventory.updateProductQuantity(productName, i);
				i++;
			}
		}
		System.out.println(adminName + " Stopped.");
	}
}