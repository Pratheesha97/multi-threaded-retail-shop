package org.application.retailshop;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InventoryTest {

	private Inventory inventory;
	private Shopper shopper1;
	private Shopper shopper2;
	private String[] productNames;

	@Before
	public void setUp() {
		inventory = new Inventory();

		shopper1 = new Shopper("John", inventory);
		shopper2 = new Shopper("Alice", inventory);

		Product apple = new Product("Apple", 10);
		Product mango = new Product("Mango", 12);
		Product banana = new Product("Banana", 10);

		inventory.getProductList().put(apple.getProductName(), apple);
		inventory.getProductList().put(mango.getProductName(), mango);
		inventory.getProductList().put(banana.getProductName(), banana);

		productNames = inventory.getProductList().keySet().toArray(new String[0]);

	}

	@Test
	public void testUpdateProductQuantity() {

		for (int i=0; i<10; i++) {
			for (String productName : productNames) {
				inventory.updateProductQuantity(productName, i);
			}
		}

		HashMap<String, Product> productList = inventory.getProductList();
		assertEquals(10, productList.get("Apple").getProductQuantity());
		assertEquals(10, productList.get("Mango").getProductQuantity());
		assertEquals(10, productList.get("Banana").getProductQuantity());
	}

	@Test
	public void testAddToCart() {

		for (int i=0; i<10; i++) {
			for (String productName : productNames) {
				inventory.updateProductQuantity(productName, i);
			}
		}

		inventory.addToCart(shopper1, "Apple", 2);
		inventory.addToCart(shopper1, "Mango", 1);
		inventory.addToCart(shopper2, "Banana", 3);

		Map<String, Integer> expectedCart1 = new HashMap<>();
		expectedCart1.put("Apple", 2);
		expectedCart1.put("Mango", 1);
		Map<String, Integer> expectedCart2 = new HashMap<>();
		expectedCart2.put("Banana", 3);

		Map<String, Integer> actualCart1 = shopper1.getCart();
		Map<String, Integer> actualCart2 = shopper2.getCart();

		Assertions.assertEquals(expectedCart1, actualCart1);
		Assertions.assertEquals(expectedCart2, actualCart2);
	}

	@Test
	public void testReadyToCheckout() throws InterruptedException {

		for (int i=0; i<10; i++) {
			for (String productName : productNames) {
				inventory.updateProductQuantity(productName, i);
			}
		}

		inventory.addToCart(shopper1, "Apple", 2);
		inventory.addToCart(shopper2, "Banana", 3);

		inventory.readyToCheckout(shopper1);
		inventory.readyToCheckout(shopper2);

		Map<String, Integer> actualCart1 = shopper1.getCart();
		Map<String, Integer> actualCart2 = shopper2.getCart();

		Assertions.assertEquals(1, actualCart1.size());
		Assertions.assertEquals(1, actualCart2.size());

		Product apple = inventory.getProductList().get("Apple");
		Product banana = inventory.getProductList().get("Banana");

		Assertions.assertEquals(8, apple.getProductQuantity());
		Assertions.assertEquals(7, banana.getProductQuantity());
	}

}
