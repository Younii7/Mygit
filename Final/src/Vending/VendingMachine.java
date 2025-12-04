package Vending;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachine {

    private List<Product> products;   // 자판기 내 상품 목록
    private int currentBalance;       // 현금 결제 시 현재 잔액
    private PaymentMode paymentMode;  // 현재 결제 모드
    private int totalSales;           // 총 매출액
    private boolean adminMode;        // 관리자 모드 여부

    public VendingMachine() {
        this.products = new ArrayList<>();
        this.currentBalance = 0;
        this.paymentMode = PaymentMode.NONE;
        this.totalSales = 0;
        this.adminMode = false;
    }

    // --- 상품 목록 관리 ---

    public void addProduct(Product product) {
        if (product == null) return;
        this.products.add(product);
    }

    public Product findProductById(int productId) {
        for (Product p : products) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return products;
    }

    // --- 결제 모드 및 잔액 관리 ---

    /**
     * 현금 투입 (100 / 500 / 1000원만 허용)
     */
    public void insertCash(int amount) {
        if (amount != 100 && amount != 500 && amount != 1000) {
            System.out.println("허용되지 않는 금액입니다. (100, 500, 1000만 투입 가능)");
            return;
        }
        if (paymentMode == PaymentMode.NONE) {
            paymentMode = PaymentMode.CASH;
        }
        currentBalance += amount;
        System.out.println("현재 잔액: " + currentBalance + "원");
    }

    public void selectCardPayment() {
        paymentMode = PaymentMode.CARD;
        System.out.println("카드 결제 모드로 전환되었습니다.");
    }

    public void exitCardPayment() {
        paymentMode = PaymentMode.NONE;
        System.out.println("카드 결제가 완료되었습니다.");
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public int getTotalSales() {
        return totalSales;
    }

    // --- 구매 가능 여부 및 Indicator ---

    /**
     * 특정 상품이 현재 상태에서 구매 가능한지 판단
     * - 현금 모드: 잔액 >= 가격 && 재고 > 0
     * - 카드 모드: 재고 > 0
     */
    // 현재 결제 모드/잔액/재고 기준으로 구매 가능 여부 판단
    public boolean canPurchase(Product product) {
        if (product == null) return false;
        if (product.isSoldOut()) {
            return false;
        }

        if (paymentMode == PaymentMode.CARD) {
            // 카드 모드는 재고만 있으면 OK
            return true;
        } else {
            // NONE, CASH 모두 잔액 기준
            return currentBalance >= product.getPrice();
        }
    }

    // 모든 상품에 대해 초록불/빨간불 상태 리스트 생성
    public List<ProductIndicator> getProductIndicators() {
        List<ProductIndicator> indicators = new ArrayList<>();
        for (Product p : products) {
            boolean purchasable = canPurchase(p);
            String color = purchasable ? "GREEN" : "RED";
            indicators.add(new ProductIndicator(p.getId(), purchasable, color));
        }
        return indicators;
    }

    // --- 상품 구매 ---

    /**
     * 상품 번호를 받아서 구매 처리
     * - 현금 모드: 잔액 차감, 재고 감소, 매출 증가
     * - 카드 모드: 잔액 사용 X, 재고 감소, 매출 증가, 모드 종료
     */
    public boolean purchaseProduct(int productId) {
        Product product = findProductById(productId);
        if (product == null) {
            System.out.println("해당 상품 번호가 존재하지 않습니다.");
            return false;
        }

        if (product.isSoldOut()) {
            System.out.println("품절된 상품입니다.");
            return false;
        }

        boolean result;
        if (paymentMode == PaymentMode.CARD) {
            result = processCardPurchase(product);
        } else {
            if (paymentMode == PaymentMode.NONE && currentBalance > 0) {
                paymentMode = PaymentMode.CASH;
            }
            result = processCashPurchase(product);
        }

        if (result) {
            System.out.println(product.getName() + " 구매 완료!");
        }
        return result;
    }

    private boolean processCashPurchase(Product product) {
        int price = product.getPrice();
        if (currentBalance < price) {
            System.out.println("잔액이 부족합니다. 현재 잔액: " + currentBalance + "원");
            return false;
        }

        if (!product.reduceStock(1)) {
            System.out.println("재고가 부족합니다.");
            return false;
        }

        currentBalance -= price;
        product.increaseSoldCount();
        totalSales += price;

        if (currentBalance == 0) {
            paymentMode = PaymentMode.NONE;
        }

        return true;
    }

    private boolean processCardPurchase(Product product) {
        if (!product.reduceStock(1)) {
            System.out.println("재고가 부족합니다.");
            return false;
        }

        product.increaseSoldCount();
        totalSales += product.getPrice();

        // 카드 결제 1회에 1개만 구매 가능 → 결제 후 모드 종료
        exitCardPayment();

        return true;
    }

    // --- 거스름돈 반환 ---

    /**
     * 거스름돈 반환 (1000, 500, 100원 단위)
     * key: 화폐 단위, value: 개수
     */
    public Map<Integer, Integer> refund() {
        Map<Integer, Integer> changeMap = new HashMap<>();
        if (currentBalance <= 0) {
            System.out.println("반환할 금액이 없습니다.");
            return changeMap;
        }

        int amount = currentBalance;
        int count1000 = amount / 1000;
        amount %= 1000;

        int count500 = amount / 500;
        amount %= 500;

        int count100 = amount / 100;
        amount %= 100;

        if (count1000 > 0) changeMap.put(1000, count1000);
        if (count500 > 0)  changeMap.put(500, count500);
        if (count100 > 0)  changeMap.put(100, count100);

        System.out.println("거스름돈 반환:");
        if (count1000 > 0) System.out.println("1000원 x " + count1000);
        if (count500 > 0) System.out.println("500원 x " + count500);
        if (count100 > 0) System.out.println("100원 x " + count100);

        currentBalance = 0;
        paymentMode = PaymentMode.NONE;

        return changeMap;
    }

    // --- 관리자 모드 ---

    public void enterAdminMode(String password) {
        // 예시: 비밀번호 "1234" 고정
        if ("1234".equals(password)) {
            adminMode = true;
            System.out.println("관리자 모드로 진입했습니다.");
        } else {
            System.out.println("비밀번호가 올바르지 않습니다.");
        }
    }

    public void exitAdminMode() {
        adminMode = false;
        System.out.println("관리자 모드를 종료했습니다.");
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void updateProductInfo(int productId, String newName, int newPrice, String newImagePath) {
        Product p = findProductById(productId);
        if (p == null) return;

        if (newName != null && !newName.trim().isEmpty()) {
            p.setName(newName);
        }
        if (newPrice > 0) {
            p.setPrice(newPrice);
        }
        if (newImagePath != null && !newImagePath.trim().isEmpty()) {
            p.setImagePath(newImagePath);
        }
    }

    public void increaseProductStock(int productId, int amount) {
        Product p = findProductById(productId);
        if (p == null) return;
        p.addStock(amount);
    }

    public void setProductStock(int productId, int newStock) {
        Product p = findProductById(productId);
        if (p == null) return;
        p.setStock(newStock);
    }

    public List<String> getProductStatusText() {
        List<String> statusList = new ArrayList<>();
        for (Product p : products) {
            String s = String.format(
                    "%d번 %s : 잔여갯수 : %d개, 판매된 갯수 : %d개",
                    p.getId(), p.getName(), p.getStock(), p.getSoldCount()
            );
            statusList.add(s);
        }
        return statusList;
    }
}
