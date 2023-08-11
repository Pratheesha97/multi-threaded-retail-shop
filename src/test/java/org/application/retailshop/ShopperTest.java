package org.application.retailshop;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ShopperTest {

	private Shopper shopper;
	private Inventory inventory;

	@Before
	public void setUp() {
		inventory = new Inventory();
		shopper = new Shopper("John", inventory);


		Product apple = new Product("Apple", 10);
		Product mango = new Product("Mango", 12);
		Product banana = new Product("Banana", 10);

		inventory.getProductList().put(apple.getProductName(), apple);
		inventory.getProductList().put(mango.getProductName(), mango);
		inventory.getProductList().put(banana.getProductName(), banana);


		String[] productNames = inventory.getProductList().keySet().toArray(new String[0]);

		for (int i=0; i<10; i++) {
			for (String productName : productNames) {
				inventory.updateProductQuantity(productName, i);
			}
		}
	}

	@Test
	public void testGetName() {
		String name = shopper.getName();
		Assertions.assertEquals("John", name);
	}

	@Test
	public void testGetCart() {
		Map<String, Integer> cart = shopper.getCart();
		Assertions.assertEquals(new HashMap<>(), cart);
	}


	@Test
	public void testAddToCart() {
		inventory.addToCart(shopper, "Apple", 3);
		inventory.addToCart(shopper,"Mango", 2);

		Map<String, Integer> cart = shopper.getCart();
		assertEquals(3, cart.get("Apple").intValue());
		assertEquals(2, cart.get("Mango").intValue());
	}

}
