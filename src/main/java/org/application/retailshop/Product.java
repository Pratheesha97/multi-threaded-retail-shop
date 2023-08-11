package org.application.retailshop;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Product class represents a product in the inventory.
 */
public class Product {

	private String productName;
	private Queue<Integer> productQuantity;
	private final int MAX_QUANTITY;

	private Lock lock;
	private Condition stockFull;
	private Condition stockEmpty;
	private Condition insufficientStock;

	/**
	 * Creates a new instance of the Product class.
	 *
	 * @param productName The name of the product.
	 * @param maxQuantity The maximum quantity of the product.
	 */
	public Product(String productName, int maxQuantity) {
		this.productName = productName;
		this.productQuantity = new LinkedList<>();
		this.MAX_QUANTITY = maxQuantity;

		lock = new ReentrantLock();
		stockFull = lock.newCondition();
		stockEmpty = lock.newCondition();
		insufficientStock = lock.newCondition();
	}

	/**
	 * Updates the product quantity by adding a new serial number.
	 *
	 * @param serialNumber The serial number to be added.
	 */
	public void updateProductQuantity(int serialNumber) {
		try {
			lock.lock();
			while (stockFull()) {
				stockFull.await(1000, TimeUnit.MILLISECONDS);
			}
			productQuantity.offer(serialNumber);
			System.out.println(Thread.currentThread().getName() + " produced 1 " + productName);
			stockEmpty.signalAll();
			insufficientStock.signalAll();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Adds a specified quantity of the product to a shopper's cart.
	 *
	 * @param shopper  The shopper adding the product to the cart.
	 * @param quantity The quantity to be added to the cart.
	 */
	public void addToCart(Shopper shopper, int quantity) {
		lock.lock();
		try {
			while (quantity > productQuantity.size()) {
				System.out.println(shopper.getName() + " trying to add " + quantity + " " + productName +
						" to the cart. But not enough stock available");
				insufficientStock.await(1000, TimeUnit.MILLISECONDS);
			}
			shopper.getCart().put(productName, quantity);
			System.out.println(shopper.getName() + " added " + quantity + " " + productName + " to the cart.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Performs the checkout of a product from the inventory.
	 *
	 * @return The serial number of the checked out product.
	 */
	public Integer checkout() {
		try {
			lock.lock();
			while (productQuantity.size() == 0) {
				stockEmpty.await(1000, TimeUnit.MILLISECONDS);
			}
			int value = productQuantity.poll();
			stockFull.signalAll();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return null;
	}

	/**
	 * Retrieves the name of the product.
	 *
	 * @return The product name.
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Checks if the product is at its maximum quantity.
	 *
	 * @return True if the product is at its maximum quantity, false otherwise.
	 */
	public boolean stockFull() {
		try {
			lock.lock();
			if (productQuantity.size() == MAX_QUANTITY) {
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Retrieves the quantity of the product.
	 *
	 * @return the product quantity.
	 */
	public int getProductQuantity() {
		try {
			lock.lock();
			return this.productQuantity.size();
		} finally {
			lock.unlock();
		}
	}
}
