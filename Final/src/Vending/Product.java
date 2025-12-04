package Vending;

public class Product {

    private int id;              // 상품 번호
    private String name;         // 상품 이름
    private int price;           // 상품 가격
    private int stock;           // 현재 재고 수량
    private int soldCount;       // 판매된 개수
    private String imagePath;    // 이미지 파일 경로

    // (상품 번호, 이름, 가격, 재고, 판매량, 이미지 파일 경로)
    public Product(int id, String name, int price, int stock, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
        this.soldCount = 0;
    }

    // --- Getter / Setter ---

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price < 0) price = 0;
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) stock = 0;
        this.stock = stock;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void increaseSoldCount() {
        this.soldCount++;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // --- 재고 관련 편의 메서드 ---

    public void addStock(int amount) {
        if (amount <= 0) return;
        this.stock += amount;
    }

    /**
     * 재고에서 amount만큼 차감.
     * 충분한 재고가 있으면 true, 아니면 false.
     */
    public boolean reduceStock(int amount) {
        if (amount <= 0) {
            return false;
        }
        if (this.stock < amount) {
            return false;
        }
        this.stock -= amount;
        return true;
    }

    public boolean isSoldOut() {
        return stock <= 0;
    }
}
