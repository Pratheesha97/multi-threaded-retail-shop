package org.application.retailshop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProductTest {

	private Product product;
	private Inventory inventory = new Inventory();
	private int maxStockQuantity = 10;

	@Before
	public void setUp() {
		product = new Product("Apple", maxStockQuantity);
	}

	@Test
	public void testUpdateProductQuantity() {
		for (int i = 1; i <= 10; i++) {
			product.updateProductQuantity(i);
		}
		assertEquals(10, product.getProductQuantity());
	}

	@Test
	public void testAddToCart() {
		Shopper shopper = new Shopper("John", inventory);

		for (int i = 1; i <= 10; i++) {
			product.updateProductQuantity(i);
		}

		product.addToCart(shopper, 5);
		assertEquals(5, shopper.getCart().get("Apple").intValue());
	}

	@Test
	public void testCheckout() {
		product.updateProductQuantity(1);
		Integer serialNumber = product.checkout();
		assertEquals(1, serialNumber.intValue());
	}

	@Test
	public void testStockFull() {

		for (int i = 1; i <= maxStockQuantity-4; i++) {
			product.updateProductQuantity(i);
		}

		assertEquals(false, product.stockFull());
	}
}
