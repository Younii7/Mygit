package Vending;

import java.util.List;

public class AdminService {

    private final VendingMachine vendingMachine;

    public AdminService(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public void addNewProduct(int id, String name, int price, int stock, String imagePath) {
        Product p = new Product(id, name, price, stock, imagePath);
        vendingMachine.addProduct(p);
    }

    public void updateProductInfo(int productId, String name, int price, String imagePath) {
        vendingMachine.updateProductInfo(productId, name, price, imagePath);
    }

    public void addStock(int productId, int amount) {
        vendingMachine.increaseProductStock(productId, amount);
    }

    public void setStock(int productId, int newStock) {
        vendingMachine.setProductStock(productId, newStock);
    }

    public List<String> getInventoryStatusText() {
        return vendingMachine.getProductStatusText();
    }

    public int getTotalSales() {
        return vendingMachine.getTotalSales();
    }
}
