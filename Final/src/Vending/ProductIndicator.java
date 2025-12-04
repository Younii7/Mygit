package Vending;

public class ProductIndicator {

    private int productId;
    private boolean purchasable;      // 구매 가능 여부
    private String indicatorColor;    // "GREEN" or "RED"

    public ProductIndicator(int productId, boolean purchasable, String indicatorColor) {
        this.productId = productId;
        this.purchasable = purchasable;
        this.indicatorColor = indicatorColor;
    }

    public int getProductId() {
        return productId;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public String getIndicatorColor() {
        return indicatorColor;
    }
}
