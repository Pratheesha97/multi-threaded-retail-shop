package org.application.retailshop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Inventory class represents the inventory of a retail shop.
 * It manages the products and provides methods for updating the product quantity,
 * adding products to the cart, and handling the checkout process.
 *
 * Assumptions: Available Product Items are Apple, Mango and Banana.
 *
 */
public class Inventory {

	private volatile HashMap<String, Product> products = new HashMap<>();
	private BlockingQueue<Shopper> readyToCheckoutQueue;
	private Lock lock;

	/**
	 * Constructs an Inventory object with initial product inventory.
	 */
	public Inventory() {
		readyToCheckoutQueue = new LinkedBlockingQueue<>();
		lock = new ReentrantLock();

		//maxQuantity is the maximum number of product items that one stock can hold.
		int maxQuantity =10;

		Product apple = new Product("Apple", maxQuantity);
		Product mango = new Product("Mango", maxQuantity);
		Product banana = new Product("Banana", maxQuantity);

		products.put(apple.getProductName(), apple);
		products.put(mango.getProductName(), mango);
		products.put(banana.getProductName(), banana);
	}

	/**
	 * Updates the quantity of a product in the inventory.
	 *
	 * @param productName   the name of the product
	 * @param serialNumber  the serial number of the product to be added
	 */
	public void updateProductQuantity(String productName, int serialNumber) {
		Product product = products.get(productName);
		if (!product.stockFull()) {
			product.updateProductQuantity(serialNumber);
		}
	}

	/**
	 * Processes the checkout for shoppers in the readyToCheckoutQueue.
	 * Removes shoppers from the queue after the checkout is completed.
	 */
	public void checkout() {
		lock.lock();
		try {
			while (!readyToCheckoutQueue.isEmpty()) {
				Shopper nextShopper = readyToCheckoutQueue.peek();

				for (Map.Entry<String, Integer> cartItem : nextShopper.getCart().entrySet()) {
					String productName = cartItem.getKey();
					int quantity = cartItem.getValue();

					Product product = products.get(productName);
					for (int i = 0; i < quantity; i++) {
						product.checkout();
					}
					System.out.println(nextShopper.getName() + " purchased " + productName + ". Quantity: " + quantity);
				}

				Thread.sleep(10); // Simulating time taken for the checkout process
				readyToCheckoutQueue.take(); // Remove shopper from the queue after checkout
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Adds a shopper to the readyToCheckoutQueue and initiates the checkout process.
	 *
	 * @param shopper the shopper to be added to the queue
	 * @throws InterruptedException if the thread is interrupted during the operation
	 */
	public void readyToCheckout(Shopper shopper) throws InterruptedException {
		try {
			readyToCheckoutQueue.put(shopper);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkout();
	}

	/**
	 * Adds a specified quantity of a product to a shopper's cart.
	 *
	 * @param shopper      the shopper adding the product to the cart
	 * @param productName  the name of the product
	 * @param quantity     the quantity of the product to be added
	 */
	public void addToCart(Shopper shopper, String productName, int quantity) {
		Product product = products.get(productName);
		product.addToCart(shopper, quantity);
	}

	/**
	 * Returns the map of products in the inventory.
	 *
	 * @return the map of products
	 */
	public HashMap<String, Product> getProductList() {
		return products;
	}
}
