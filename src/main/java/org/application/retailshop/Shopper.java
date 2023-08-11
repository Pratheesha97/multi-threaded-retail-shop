package org.application.retailshop;

import java.util.HashMap;
import java.util.Map;

/**
 * The Shopper class represents a shopper in the online retail shop.
 *
 * Assumptions: Every shopper purchases a random quantity of all the products available in the store.
 */
public class Shopper implements Runnable {

	private String shopperName;
	private Inventory inventory;
	private Map<String, Integer> cart;

	/**
	 * Creates a new instance of the Shopper class.
	 *
	 * @param shopperName The name of the shopper.
	 * @param inventory   The inventory of the online retail shop.
	 */
	public Shopper(String shopperName, Inventory inventory) {
		this.shopperName = shopperName;
		this.inventory = inventory;
		this.cart = new HashMap<>();
	}

	/**
	 * Retrieves the name of the shopper.
	 *
	 * @return The shopper name.
	 */
	public String getName() {
		return shopperName;
	}

	/**
	 * Retrieves the cart of the shopper.
	 *
	 * @return The cart containing the shopper's selected products and quantities.
	 */
	public Map<String, Integer> getCart() {
		return cart;
	}

	/**
	 * Executes the shopping process for the shopper.
	 */
	@Override
	public void run() {
		String[] productNames = inventory.getProductList().keySet().toArray(new String[0]);

		// Adding items to the cart concurrently
		for (String productName : productNames) {
			int quantity = (int) (Math.random() * 5) + 1; // Random quantity between 1 and 5
			inventory.addToCart(this, productName, quantity);
		}

		// Checkout
		try {
			inventory.readyToCheckout(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(shopperName + " successfully purchased products and left the store.");
	}
}
