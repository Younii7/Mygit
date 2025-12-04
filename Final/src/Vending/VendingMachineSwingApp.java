package Vending;

import javax.swing.SwingUtilities;

public class VendingMachineSwingApp {

    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        AdminService adminService = new AdminService(vm);

        // 파이썬 juArr 기반 초기 상품 세팅
        adminService.addNewProduct(1,  "오아시스",       800, 10, "images/오아시스.png");
        adminService.addNewProduct(2,  "오아시스",       800, 10, "images/오아시스.png");
        adminService.addNewProduct(3,  "아쿠아제로",    2000, 10, "images/아쿠아제로.png");
        adminService.addNewProduct(4,  "레몬워터",      1800, 10, "images/레몬워터.png");
        adminService.addNewProduct(5,  "레몬워터",      1800, 10, "images/레몬워터.png");
        adminService.addNewProduct(6,  "옥수수수염차",  1600, 10, "images/옥수수수염차.png");
        adminService.addNewProduct(7,  "옥수수수염차",  1600, 10, "images/옥수수수염차.png");
        adminService.addNewProduct(8,  "황금보리",      1600, 10, "images/황금보리.png");
        adminService.addNewProduct(9,  "트레비",        1300, 10, "images/트레비.png");
        adminService.addNewProduct(10, "트레비",        1300, 10, "images/트레비.png");
        adminService.addNewProduct(11, "밀키스",        1100, 10, "images/밀키스.png");
        adminService.addNewProduct(12, "펩시콜라",      1100, 10, "images/펩시콜라.png");
        adminService.addNewProduct(13, "핫식스",        1300, 10, "images/핫식스.png");
        adminService.addNewProduct(14, "칠성사이다",    1300, 10, "images/칠성사이다.png");
        adminService.addNewProduct(15, "델몬트망고",    1200, 10, "images/델몬트망고.png");
        adminService.addNewProduct(16, "델몬트망고",    1200, 10, "images/델몬트망고.png");
        adminService.addNewProduct(17, "립톤",          1200, 10, "images/립톤.png");
        adminService.addNewProduct(18, "델몬트사과",    1100, 10, "images/델몬트사과.png");
        adminService.addNewProduct(19, "델몬트사과",    1100, 10, "images/델몬트사과.png");
        adminService.addNewProduct(20, "델몬트포도",    1100, 10, "images/델몬트포도.png");
        adminService.addNewProduct(21, "가나초코",       900, 10, "images/가나초코.png");
        adminService.addNewProduct(22, "레쓰비",         900, 10, "images/레쓰비.png");
        adminService.addNewProduct(23, "펩시제로",      1100, 10, "images/펩시제로.png");
        adminService.addNewProduct(24, "핫6제로",       1300, 10, "images/핫6제로.png");
        adminService.addNewProduct(25, "솔의눈",        1200, 10, "images/솔의눈.png");
        adminService.addNewProduct(26, "레쓰비라떼",    1200, 10, "images/레쓰비라떼.png");
        adminService.addNewProduct(27, "게토레이",      1000, 10, "images/게토레이.png");
        adminService.addNewProduct(28, "게토레이",      1000, 10, "images/게토레이.png");
        adminService.addNewProduct(29, "코코리치포도",  1000, 10, "images/코코리치포도.png");
        adminService.addNewProduct(30, "잔치집식혜",    1000, 10, "images/잔치집식혜.png");

        SwingUtilities.invokeLater(() -> {
            VendingMachineFrame frame = new VendingMachineFrame(vm, adminService);
            frame.setVisible(true);
        });
    }
}
