package Vending;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDialog extends JDialog {

    private final VendingMachine vendingMachine;
    private final AdminService adminService;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField stockField;
    private JTextField priceField;
    private JTextArea statusArea;
    private JLabel totalSalesLabel;

    public AdminDialog(Frame owner, VendingMachine vendingMachine, AdminService adminService) {
        super(owner, "관리자 모드", true);
        this.vendingMachine = vendingMachine;
        this.adminService = adminService;

        initUI();
        loadTableData();
    }

    private void initUI() {
        setSize(800, 550);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // ===== 테이블 영역 =====
        String[] columnNames = {"ID", "이름", "가격", "재고", "판매량"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 직접 테이블 셀 수정은 막고, 아래 버튼으로만 수정
            }
        };
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.CENTER);

        // ===== 하단 컨트롤 패널 =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // 재고 설정 패널
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockPanel.add(new JLabel("[재고 설정] 선택된 상품 새 재고: "));
        stockField = new JTextField(5);
        stockPanel.add(stockField);
        JButton stockBtn = new JButton("재고 설정");
        stockBtn.addActionListener(e -> applyStockChange());
        stockPanel.add(stockBtn);

        // 가격 수정 패널
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.add(new JLabel("[가격 수정] 선택된 상품 새 가격: "));
        priceField = new JTextField(5);
        pricePanel.add(priceField);
        JButton priceBtn = new JButton("가격 수정");
        priceBtn.addActionListener(e -> applyPriceChange());
        pricePanel.add(priceBtn);

        // ★ 상품 설정 패널 (이름+가격+재고 한 번에)
        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton productBtn = new JButton("상품 설정");   // 요구한 버튼 이름
        productBtn.addActionListener(e -> applyProductSetting());
        productPanel.add(new JLabel("[상품 설정] 선택된 상품의 이름/가격/재고 변경: "));
        productPanel.add(productBtn);

        // 총 매출 + 상태 텍스트
        JPanel salesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalSalesLabel = new JLabel("총 매출: 0원");
        salesPanel.add(totalSalesLabel);

        statusArea = new JTextArea(5, 60);
        statusArea.setEditable(false);
        JScrollPane statusScroll = new JScrollPane(statusArea);

        bottomPanel.add(stockPanel);
        bottomPanel.add(pricePanel);
        bottomPanel.add(productPanel);
        bottomPanel.add(salesPanel);
        bottomPanel.add(statusScroll);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 테이블/텍스트 영역 갱신
    private void loadTableData() {
        tableModel.setRowCount(0);
        StringBuilder sb = new StringBuilder();

        List<Product> products = vendingMachine.getProducts();
        for (Product p : products) {
            Object[] row = {
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStock(),
                    p.getSoldCount()
            };
            tableModel.addRow(row);

            sb.append(String.format(
                    "%d번 %s : 잔여갯수 : %d개, 판매된 갯수 : %d개%n",
                    p.getId(), p.getName(), p.getStock(), p.getSoldCount()
            ));
        }

        totalSalesLabel.setText("총 매출: " + vendingMachine.getTotalSales() + "원");
        statusArea.setText(sb.toString());
    }

    private Integer getSelectedProductId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "먼저 상품을 선택하세요.");
            return null;
        }
        Object value = tableModel.getValueAt(row, 0); // ID 열
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    // [재고 설정]
    private void applyStockChange() {
        Integer id = getSelectedProductId();
        if (id == null) return;

        String text = stockField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "재고 수량을 입력하세요.");
            return;
        }
        try {
            int newStock = Integer.parseInt(text);
            if (newStock < 0) {
                JOptionPane.showMessageDialog(this, "재고는 0 이상이어야 합니다.");
                return;
            }
            vendingMachine.setProductStock(id, newStock);
            stockField.setText("");
            loadTableData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "올바른 숫자를 입력하세요.");
        }
    }

    // [가격 수정]
    private void applyPriceChange() {
        Integer id = getSelectedProductId();
        if (id == null) return;

        String text = priceField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "가격을 입력하세요.");
            return;
        }
        try {
            int newPrice = Integer.parseInt(text);
            if (newPrice <= 0) {
                JOptionPane.showMessageDialog(this, "가격은 0보다 커야 합니다.");
                return;
            }
            // 이름/이미지는 유지하고 가격만 수정
            vendingMachine.updateProductInfo(id, null, newPrice, null);
            priceField.setText("");
            loadTableData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "올바른 숫자를 입력하세요.");
        }
    }

    // ★ [상품 설정] – 이름/가격/재고를 한 번에 수정
    // ★ [상품 설정] – 이름/가격/재고(+이미지) 한 번에 수정
    private void applyProductSetting() {
        Integer id = getSelectedProductId();
        if (id == null) return;

        Product p = vendingMachine.findProductById(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "선택된 상품을 찾을 수 없습니다.");
            return;
        }

        // 현재 값으로 초기화된 입력창 구성 (이름, 가격, 재고)
        JTextField nameField = new JTextField(p.getName());
        JTextField priceField = new JTextField(String.valueOf(p.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(p.getStock()));

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("상품 이름:"));
        panel.add(nameField);
        panel.add(new JLabel("상품 가격:"));
        panel.add(priceField);
        panel.add(new JLabel("상품 재고:"));
        panel.add(stockField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "상품 설정 (이름/가격/재고 변경)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // 취소
        }

        String newName = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();

        if (newName.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름, 가격, 재고를 모두 입력하세요.");
            return;
        }

        try {
            int newPrice = Integer.parseInt(priceText);
            int newStock = Integer.parseInt(stockText);

            if (newPrice <= 0 || newStock < 0) {
                JOptionPane.showMessageDialog(this, "가격은 0보다 커야 하고, 재고는 0 이상이어야 합니다.");
                return;
            }

            // 🔹 새 이름 기준으로 이미지 파일 경로도 같이 변경
            //    → resources/images/새이름.png 형식으로 맞춰서 저장되어 있어야 함
            String newImagePath = "images/" + newName + ".png";

            // 이름 + 가격 + 이미지 경로 수정
            vendingMachine.updateProductInfo(id, newName, newPrice, newImagePath);
            // 재고 수정
            vendingMachine.setProductStock(id, newStock);

            // 테이블/텍스트 갱신
            loadTableData();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해야 합니다.");
        }
    }

}
