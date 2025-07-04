import java.util.*;

interface Shippable {
    String getName();
    double getWeight();
}

abstract class Product {
    String name;
    double price;
    int quantity;
    boolean isExpired;

    public Product(String name, double price, int quantity, boolean isExpired) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isExpired = isExpired;
    }

    public boolean isShippable() {
        return this instanceof Shippable;
    }

    public boolean isAvailable(int amount) {
        return quantity >= amount && !isExpired;
    }

    public void decreaseQuantity(int amount) {
        quantity -= amount;
    }
}

class Cheese extends Product implements Shippable {
    double weight;

    public Cheese(String name, double price, int quantity, boolean isExpired, double weight) {
        super(name, price, quantity, isExpired);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}

class Biscuits extends Product implements Shippable {
    double weight;

    public Biscuits(String name, double price, int quantity, boolean isExpired, double weight) {
        super(name, price, quantity, isExpired);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}

class TV extends Product implements Shippable {
    double weight;

    public TV(String name, double price, int quantity, boolean isExpired, double weight) {
        super(name, price, quantity, isExpired);
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}

class ScratchCard extends Product {
    public ScratchCard(String name, double price, int quantity) {
        super(name, price, quantity, false);
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

class Cart {
    List<CartItem> items = new ArrayList<>();

    public void add(Product product, int quantity) {
        if (!product.isAvailable(quantity)) {
            System.out.println("Cannot add " + product.name + " - Not enough quantity or expired.");
            return;
        }

        for (CartItem item : items) {
            if (item.product == product) {
                item.quantity += quantity;
                product.decreaseQuantity(quantity);
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        product.decreaseQuantity(quantity);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

class Customer {
    String name;
    double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public boolean canAfford(double amount) {
        return balance >= amount;
    }

    public void pay(double amount) {
        balance -= amount;
    }
}

class ShippingService {
    public static void ship(List<Shippable> items) {
        Map<String, Integer> itemCount = new HashMap<>();
        double totalWeight = 0;

        System.out.println("** Shipment notice **");
        for (Shippable item : items) {
            String key = item.getName() + " " + (int)(item.getWeight() * 1000) + "g";
            itemCount.put(key, itemCount.getOrDefault(key, 0) + 1);
            totalWeight += item.getWeight();
        }

        for (String key : itemCount.keySet()) {
            System.out.println(itemCount.get(key) + "x " + key);
        }

        System.out.printf("Total package weight %.1fkg\\n\\n", totalWeight);
    }
}

class CheckoutService {
    public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Error: Cart is empty");
            return;
        }

        double subtotal = 0;
        List<Shippable> shippingItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            if (item.product.isExpired) {
                System.out.println("Error: " + item.product.name + " is expired.");
                return;
            }
            subtotal += item.product.price * item.quantity;

            if (item.product.isShippable()) {
                for (int i = 0; i < item.quantity; i++) {
                    shippingItems.add((Shippable)item.product);
                }
            }
        }

        double shippingCost = 30;
        double total = subtotal + shippingCost;

        if (!customer.canAfford(total)) {
            System.out.println("Error: Insufficient balance.");
            return;
        }

        if (!shippingItems.isEmpty()) {
            ShippingService.ship(shippingItems);
        }

        customer.pay(total);

        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.println(item.quantity + "x " + item.product.name + "\\t" + (item.product.price * item.quantity));
        }
        System.out.println("----------------------");
        System.out.println("Subtotal\\t" + subtotal);
        System.out.println("Shipping\\t" + shippingCost);
        System.out.println("Amount\\t\\t" + total);
        System.out.println("Customer balance after payment: " + customer.balance);
    }
}

public class EcommerceSystem {
    public static void main(String[] args) {
        Customer customer = new Customer("Yousef", 1000);

        Product cheese = new Cheese("Cheese", 100, 10, false, 0.2);
        Product biscuits = new Biscuits("Biscuits", 150, 5, false, 0.7);

        Cart cart = new Cart();
        cart.add(cheese, 2);
        cart.add(biscuits, 1);

        CheckoutService.checkout(customer, cart);
    }
}
