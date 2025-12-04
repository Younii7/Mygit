package Vending;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

public class VendingMachineFrame extends JFrame {

    private final VendingMachine vendingMachine;
    private final AdminService adminService;

    private JLabel balanceLabel;
    private JLabel modeLabel;
    private JLabel messageLabel;

    private JPanel productsPanel;

    // productId -> "하단 가격 Bar" 패널 매핑 (색상 업데이트용)
    private Map<Integer, JPanel> indicatorPanelMap = new HashMap<>();

    // 관리자 모드 토글용
    private JButton adminButton;
    private boolean adminMode = false;

    public VendingMachineFrame(VendingMachine vendingMachine, AdminService adminService) {
        this.vendingMachine = vendingMachine;
        this.adminService = adminService;

        initUI();
        refreshIndicatorsAndBalance();
    }

    private void initUI() {
        setTitle("자판기 프로그램 (Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 상단 정보 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("잔액: 0원");
        modeLabel = new JLabel("결제 모드: NONE");
        messageLabel = new JLabel("메시지: ");
        topPanel.add(balanceLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(modeLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(messageLabel);

        add(topPanel, BorderLayout.NORTH);

        // 중앙 – 상품 그리드
        productsPanel = new JPanel(new GridLayout(5, 6, 8, 8)); // 5행 6열 = 30개
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        add(scrollPane, BorderLayout.CENTER);

        buildProductPanels(); // 실제 상품에서 패널 생성

        // 오른쪽 – 컨트롤 패널
        JPanel controlPanel = buildControlPanel();
        add(controlPanel, BorderLayout.EAST);
    }

    private void buildProductPanels() {
        productsPanel.removeAll();
        indicatorPanelMap.clear();

        List<Product> products = vendingMachine.getProducts();
        for (Product p : products) {
            JPanel panel = createSingleProductPanel(p);
            productsPanel.add(panel);
        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private JPanel createSingleProductPanel(Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(3, 3));
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        panel.setBackground(Color.WHITE); // 본체 패널은 흰색 고정

        // 이미지 로드
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (product.getImagePath() != null) {
            try {
                java.net.URL imgUrl = getClass().getClassLoader().getResource(product.getImagePath());
                if (imgUrl != null) {
                    ImageIcon icon = new ImageIcon(imgUrl);
                    // 이미지 크기 축소
                    Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaled));
                } else {
                    imageLabel.setText("이미지 없음");
                }
            } catch (Exception e) {
                imageLabel.setText("이미지 오류");
            }
        } else {
            imageLabel.setText("이미지 없음");
        }

        panel.add(imageLabel, BorderLayout.CENTER);

        // 아래쪽 가격 Bar (여기 색만 빨강/초록으로 변경)
        JPanel priceBar = new JPanel(new BorderLayout());
        priceBar.setPreferredSize(new Dimension(0, 25));
        JLabel textLabel = new JLabel(
                product.getId() + "번  " + product.getPrice() + "원",
                SwingConstants.CENTER
        );
        priceBar.add(textLabel, BorderLayout.CENTER);

        // 초기 색상 (기본적으로 빨간색으로 두고, refresh에서 다시 맞춰줌)
        priceBar.setBackground(new Color(255, 200, 200));

        panel.add(priceBar, BorderLayout.SOUTH);

        // productId -> 가격 Bar 패널 매핑
        indicatorPanelMap.put(product.getId(), priceBar);

        // 클릭 이벤트 – 상품 구매 시도
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleProductClick(product.getId());
            }
        });

        return panel;
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 0));

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel(" [결제/관리 컨트롤]"));
        panel.add(Box.createVerticalStrut(10));

        // 현금 투입 버튼
        JButton cash100Btn = new JButton("100원 투입");
        JButton cash500Btn = new JButton("500원 투입");
        JButton cash1000Btn = new JButton("1000원 투입");

        cash100Btn.addActionListener(e -> insertCash(100));
        cash500Btn.addActionListener(e -> insertCash(500));
        cash1000Btn.addActionListener(e -> insertCash(1000));

        panel.add(cash100Btn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cash500Btn);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cash1000Btn);
        panel.add(Box.createVerticalStrut(15));

        // 카드 결제 모드
        JButton cardModeBtn = new JButton("카드 결제 모드");
        cardModeBtn.addActionListener(e -> {
            vendingMachine.selectCardPayment();
            setMessage("카드 결제 모드로 전환.");
            refreshIndicatorsAndBalance();
        });
        panel.add(cardModeBtn);
        panel.add(Box.createVerticalStrut(15));

        // 거스름돈 반환
        JButton refundBtn = new JButton("거스름돈 반환");
        refundBtn.addActionListener(this::handleRefund);
        panel.add(refundBtn);
        panel.add(Box.createVerticalStrut(15));

        // 관리자 모드 토글 버튼
        adminButton = new JButton("관리자 모드");
        adminButton.addActionListener(e -> toggleAdminMode());
        panel.add(adminButton);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void insertCash(int amount) {
        vendingMachine.insertCash(amount);
        setMessage(amount + "원 투입됨.");
        refreshIndicatorsAndBalance();
    }

    private void handleRefund(ActionEvent e) {
        Map<Integer, Integer> changeMap = vendingMachine.refund();
        if (changeMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "반환할 금액이 없습니다.");
        } else {
            // 총액 + 각 동전/지폐 개수 정리
            int total = 0;
            for (Map.Entry<Integer, Integer> entry : changeMap.entrySet()) {
                total += entry.getKey() * entry.getValue();
            }

            StringBuilder sb = new StringBuilder();
            sb.append(total).append("원 반환\n");

            // 돈 단위 정렬 (큰 금액부터 보이게)
            java.util.List<Integer> denoms = new ArrayList<>(changeMap.keySet());
            Collections.sort(denoms, Collections.reverseOrder());

            boolean first = true;
            for (int d : denoms) {
                int cnt = changeMap.get(d);
                if (cnt <= 0) continue;
                if (!first) {
                    sb.append(", ");
                }
                sb.append(d).append("원 X ").append(cnt).append("개");
                first = false;
            }

            JOptionPane.showMessageDialog(this, sb.toString());
            setMessage("거스름돈 " + total + "원이 반환되었습니다.");
        }
        refreshIndicatorsAndBalance();
    }

    private void handleProductClick(int productId) {
        Product p = vendingMachine.findProductById(productId);
        if (p == null) {
            setMessage("존재하지 않는 상품입니다.");
            return;
        }

        if (p.isSoldOut()) {
            JOptionPane.showMessageDialog(this, "품절된 상품입니다.", "구매 불가", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 카드 모드 혹은 현금 모드에 따라 구매 가능 여부 확인
        boolean can = vendingMachine.canPurchase(p);
        if (!can) {
            JOptionPane.showMessageDialog(this, "잔액이 부족합니다.", "구매 불가", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = vendingMachine.purchaseProduct(productId);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    p.getName() + " 구매 완료!", "구매 성공", JOptionPane.INFORMATION_MESSAGE);
            setMessage(p.getName() + " 구매 완료.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "구매에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            setMessage("구매에 실패했습니다.");
        }

        refreshIndicatorsAndBalance();
    }

    private void refreshIndicatorsAndBalance() {
        // 잔액 / 모드 라벨 갱신
        balanceLabel.setText("잔액: " + vendingMachine.getCurrentBalance() + "원");
        modeLabel.setText("결제 모드: " + vendingMachine.getPaymentMode().name());

        // 상품 하단 가격 Bar 색 갱신
        List<ProductIndicator> indicators = vendingMachine.getProductIndicators();
        for (ProductIndicator pi : indicators) {
            JPanel bar = indicatorPanelMap.get(pi.getProductId());
            if (bar == null) continue;

            if (pi.isPurchasable()) {
                bar.setBackground(new Color(200, 255, 200)); // 연두 (구매 가능)
            } else {
                bar.setBackground(new Color(255, 200, 200)); // 연분홍 (구매 불가)
            }
        }

        productsPanel.repaint();
    }

    private void setMessage(String msg) {
        messageLabel.setText("메시지: " + msg);
    }

    // 관리자 모드 토글 (입장/종료 + 버튼 텍스트 변경 + AdminDialog 사용)
    private void toggleAdminMode() {
        if (!adminMode) {
            // 관리자 모드 진입 시도
            String pw = JOptionPane.showInputDialog(this, "관리자 비밀번호 입력", "관리자 모드", JOptionPane.PLAIN_MESSAGE);
            if (pw == null) {
                return; // 취소
            }

            vendingMachine.enterAdminMode(pw);
            if (!vendingMachine.isAdminMode()) {
                JOptionPane.showMessageDialog(this, "비밀번호가 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            adminMode = true;
            adminButton.setText("관리자 모드 종료");
            setMessage("관리자 모드로 진입했습니다.");

            // ★ 여기서 기존처럼 AdminDialog 열어서 재고/가격 수정 가능
            AdminDialog dialog = new AdminDialog(this, vendingMachine, adminService);
            dialog.setVisible(true);

            // 다이얼로그에서 재고/가격을 바꿨을 수 있으므로 화면 새로 그림
            buildProductPanels();
            refreshIndicatorsAndBalance();

        } else {
            // 관리자 모드 종료
            vendingMachine.exitAdminMode();
            adminMode = false;
            adminButton.setText("관리자 모드");
            setMessage("관리자 모드를 종료했습니다.");
            refreshIndicatorsAndBalance();
        }
    }
}
