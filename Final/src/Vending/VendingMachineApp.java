package Vending;

import java.util.List;
import java.util.Scanner;

public class VendingMachineApp {

    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        AdminService adminService = new AdminService(vm);

        // 샘플 상품 등록
        adminService.addNewProduct(1,  "오아시스",     800,  10, "images/오아시스.png");
        adminService.addNewProduct(2,  "오아시스",     800,  10, "images/오아시스.png");
        adminService.addNewProduct(3,  "아쿠아제로",  2000,  10, "images/아쿠아제로.png");
        adminService.addNewProduct(4,  "레몬워터",    1800,  10, "images/레몬워터.png");
        adminService.addNewProduct(5,  "레몬워터",    1800,  10, "images/레몬워터.png");
        adminService.addNewProduct(6,  "옥수수수염차", 1600, 10, "images/옥수수수염차.png");
        adminService.addNewProduct(7,  "옥수수수염차", 1600, 10, "images/옥수수수염차.png");
        adminService.addNewProduct(8,  "황금보리",    1600,  10, "images/황금보리.png");
        adminService.addNewProduct(9,  "트레비",      1300,  10, "images/트레비.png");
        adminService.addNewProduct(10, "트레비",      1300,  10, "images/트레비.png");
        adminService.addNewProduct(11, "밀키스",      1100,  10, "images/밀키스.png");
        adminService.addNewProduct(12, "펩시콜라",    1100,  10, "images/펩시콜라.png");
        adminService.addNewProduct(13, "핫식스",      1300,  10, "images/핫식스.png");
        adminService.addNewProduct(14, "칠성사이다",  1300,  10, "images/칠성사이다.png");
        adminService.addNewProduct(15, "델몬트망고",  1200,  10, "images/델몬트망고.png");
        adminService.addNewProduct(16, "델몬트망고",  1200,  10, "images/델몬트망고.png");
        adminService.addNewProduct(17, "립톤",        1200,  10, "images/립톤.png");
        adminService.addNewProduct(18, "델몬트사과",  1100,  10, "images/델몬트사과.png");
        adminService.addNewProduct(19, "델몬트사과",  1100,  10, "images/델몬트사과.png");
        adminService.addNewProduct(20, "델몬트포도",  1100,  10, "images/델몬트포도.png");
        adminService.addNewProduct(21, "가나초코",     900,  10, "images/가나초코.png");
        adminService.addNewProduct(22, "레쓰비",       900,  10, "images/레쓰비.png");
        adminService.addNewProduct(23, "펩시제로",    1100,  10, "images/펩시제로.png");
        adminService.addNewProduct(24, "핫6제로",     1300,  10, "images/핫6제로.png");
        adminService.addNewProduct(25, "솔의눈",      1200,  10, "images/솔의눈.png");
        adminService.addNewProduct(26, "레쓰비라떼",  1200,  10, "images/레쓰비라떼.png");
        adminService.addNewProduct(27, "게토레이",    1000,  10, "images/게토레이.png");
        adminService.addNewProduct(28, "게토레이",    1000,  10, "images/게토레이.png");
        adminService.addNewProduct(29, "코코리치포도", 1000, 10, "images/코코리치포도.png");
        adminService.addNewProduct(30, "잔치집식혜",  1000,  10, "images/잔치집식혜.png");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== 자판기 메뉴 =====");
            System.out.println("1. 현금 투입");
            System.out.println("2. 카드 결제 모드");
            System.out.println("3. 상품 목록 및 Indicator 보기");
            System.out.println("4. 상품 구매");
            System.out.println("5. 거스름돈 반환");
            System.out.println("6. 관리자 모드 진입");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");

            int menu = sc.nextInt();

            if (menu == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            switch (menu) {
                case 1:
                    System.out.print("투입할 금액(100/500/1000): ");
                    int amount = sc.nextInt();
                    vm.insertCash(amount);
                    break;
                case 2:
                    vm.selectCardPayment();
                    break;
                case 3:
                    List<ProductIndicator> indicators = vm.getProductIndicators();
                    for (ProductIndicator pi : indicators) {
                        Product p = vm.findProductById(pi.getProductId());
                        if (p == null) continue;
                        System.out.printf("%d번 %s (%d원) - %s\n",
                                p.getId(), p.getName(), p.getPrice(), pi.getIndicatorColor());
                    }
                    break;
                case 4:
                    System.out.print("구매할 상품 번호: ");
                    int id = sc.nextInt();
                    vm.purchaseProduct(id);
                    break;
                case 5:
                    vm.refund();
                    break;
                case 6:
                    System.out.print("관리자 비밀번호 입력: ");
                    String pw = sc.next();
                    vm.enterAdminMode(pw);
                    if (vm.isAdminMode()) {
                        System.out.println("=== 관리자 재고 현황 ===");
                        for (String s : vm.getProductStatusText()) {
                            System.out.println(s);
                        }
                        System.out.println("총 매출: " + vm.getTotalSales() + "원");
                        vm.exitAdminMode();
                    }
                    break;
                default:
                    System.out.println("잘못된 메뉴입니다.");
            }
        }

        sc.close();
    }
}
