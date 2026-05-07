/*
package GUI;

import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailUI extends JFrame {

    // ===== DATA ĐĂNG NHẬP =====
    private NhanVien nhanVien;

    // ===== UI STATE =====
    private JLabel titleLabel;        // "Chức năng được chọn"
    private JLabel currentPageLabel;  // nội dung hiển thị info
    private JButton btnAdmin;         // nút admin trên sidebar
    private final List<JPanel> allSubMenus = new ArrayList<>();

    // ===== CONSTRUCTOR MẶC ĐỊNH =====
    public AccountDetailUI() {
        initUI();
    }

    // ===== CONSTRUCTOR NHẬN NHÂN VIÊN (LOGIN) =====

    public AccountDetailUI(NhanVien nv) {
        this();              //
        this.nhanVien = nv;  //

        if (nv != null) {
            btnAdmin.setText(nv.getHoTen());
            titleLabel.setText("Thông tin tài khoản");
            hienThiThongTinNhanVien();
        }
    }


    // ===== KHỞI TẠO UI GỐC =====
    private void initUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // ================= MAIN (CENTER) =================
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 245, 245));

        titleLabel = new JLabel("Chức năng được chọn", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        main.add(titleLabel, BorderLayout.NORTH);

        currentPageLabel = new JLabel("Trang hiện tại: admin", JLabel.CENTER);
        currentPageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        main.add(currentPageLabel, BorderLayout.CENTER);

        return main;
    }

    // ================= SIDEBAR (GIỮ NGUYÊN) =================
    private JPanel createSidebar() {

        LinearColorPanel sidebar = new LinearColorPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // ADMIN (giữ style, chỉ đổi text)
        btnAdmin = menuButton("admin", true, "admin");
        sidebar.add(btnAdmin);
        sidebar.add(Box.createVerticalStrut(16));

        // TRANG CHỦ
        sidebar.add(parentMenu(
                "Trang Chủ",
                subMenu("Tổng quan", "Trang Chủ - Tổng quan"),
                subMenu("Phòng trống", "Trang Chủ - Phòng trống")
        ));

        // QUẢN LÝ
        sidebar.add(parentMenu(
                "Quản Lý",
                subMenu("Khách hàng", "Quản Lý - Khách hàng"),
                subMenu("Nhân viên", "Quản Lý - Nhân viên"),
                subMenu("Phòng", "Quản Lý - Phòng"),
                subMenu("Dịch Vụ", "Quản Lý - Dịch Vụ"),
                subMenu("Tài Khoản", "Quản Lý - Tài Khoản"),
                subMenu("Hóa Đơn", "Quản Lý - Hóa Đơn")
        ));

        // THỐNG KÊ
        sidebar.add(parentMenu(
                "Thống Kê",
                subMenu("Doanh thu Ngày", "Thống Kê - Doanh thu Ngày"),
                subMenu("Doanh Thu Tháng", "Thống Kê - Doanh Thu Tháng")
        ));

        // TÌM KIẾM
        sidebar.add(parentMenu(
                "Tìm kiếm",
                subMenu("Tài Khoản", "Tìm kiếm - Tài Khoản"),
                subMenu("Khách Hàng", "Tìm kiếm - Khách Hàng"),
                subMenu("Nhân Viên", "Tìm kiếm - Nhân Viên")
        ));

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(menuButton("Đăng Xuất", false, "Đăng Xuất"));

        return sidebar;
    }

    // ================= MENU COMPONENTS =================
    private JButton menuButton(String text, boolean active, String page) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(active ? new Color(0, 90, 200) : new Color(0, 70, 160));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setRolloverEnabled(false);

        btn.addActionListener(e -> {
            titleLabel.setText(page);
            currentPageLabel.setText("Trang hiện tại: " + page);
        });

        return btn;
    }

    // MENU CHA
    private JPanel parentMenu(String title, JButton... subItems) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JButton parentBtn = menuButton(title, false, title);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(0, 95, 190));
        subPanel.setBorder(new EmptyBorder(5, 18, 8, 0));
        subPanel.setVisible(false);
        subPanel.setOpaque(true);

        for (JButton btn : subItems) {
            subPanel.add(btn);
            subPanel.add(Box.createVerticalStrut(4));
        }

        allSubMenus.add(subPanel);

        parentBtn.addActionListener(e -> {
            boolean willOpen = !subPanel.isVisible();
            for (JPanel p : allSubMenus) p.setVisible(false);
            subPanel.setVisible(willOpen);

            titleLabel.setText(title);
            currentPageLabel.setText("Trang hiện tại: " + title);
        });

        container.add(parentBtn);
        container.add(subPanel);

        return container;
    }

    // MENU CON
    private JButton subMenu(String text, String page) {
        JButton btn = new JButton("• " + text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0,110,220));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setRolloverEnabled(false);

        btn.addActionListener(e -> {
            titleLabel.setText(page);
            currentPageLabel.setText("Trang hiện tại: " + page);
        });

        return btn;
    }

    // ================= HIỂN THỊ THÔNG TIN NHÂN VIÊN =================
    private void hienThiThongTinNhanVien() {
        if (nhanVien == null) return;

        String info =
                "<html>"
                        + "Họ tên: " + nhanVien.getHoTen() + "<br>"
                        + "Mã NV: " + nhanVien.getMaNV() + "<br>"
                        + "Giới tính: " + (nhanVien.getGioiTinh() == 1 ? "Nam" : "Nữ") + "<br>"
                        + "Ngày sinh: " + nhanVien.getNgaySinh() + "<br>"
                        + "CCCD: " + nhanVien.getCccd() + "<br>"
                        + "SĐT: " + nhanVien.getSdt() + "<br>"
                        + "Chức vụ: " + nhanVien.getChucVu().getTenChucVu()
                        + "</html>";

        currentPageLabel.setText(info);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountDetailUI().setVisible(true));
    }
}
*/
/*
package GUI;

import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailUI extends JFrame {

    private NhanVien nhanVien;

    private JPanel mainContent;
    private JButton btnAdmin;
    private final List<JPanel> allSubMenus = new ArrayList<>();

    public AccountDetailUI(NhanVien nv) {
        this.nhanVien = nv;
        initUI();
    }

    private void initUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        add(mainContent, BorderLayout.CENTER);

        showHomePanel();
    }

    // ================= TRANG CHỦ =================
    private void showHomePanel() {
        mainContent.removeAll();
        mainContent.add(new HomeUI(), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= ACCOUNT =================
    private void showAccountPanel() {
        mainContent.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Chi Tiết Tài Khoản", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));
        wrapper.setBackground(new Color(245, 245, 245));

        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(300, 400));
        left.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(new ImageIcon("photo/avatar.png"));
        avatar.setHorizontalAlignment(JLabel.CENTER);
        left.add(avatar, BorderLayout.CENTER);

        JLabel name = new JLabel(nhanVien.getHoTen(), JLabel.CENTER);
        name.setFont(new Font("Segoe UI", Font.BOLD, 18));
        left.add(name, BorderLayout.SOUTH);

        JPanel right = new JPanel(new GridLayout(6, 2, 20, 20));
        right.setBackground(new Color(245, 245, 245));

        right.add(new JLabel("Mã NV:"));
        right.add(new JLabel(nhanVien.getMaNV()));

        right.add(new JLabel("Giới tính:"));
        right.add(new JLabel(nhanVien.getGioiTinh() == 1 ? "Nam" : "Nữ"));

        right.add(new JLabel("Ngày sinh:"));
        right.add(new JLabel(nhanVien.getNgaySinh().toString()));

        right.add(new JLabel("CCCD:"));
        right.add(new JLabel(nhanVien.getCccd()));

        right.add(new JLabel("SĐT:"));
        right.add(new JLabel(nhanVien.getSdt()));

        right.add(new JLabel("Chức vụ:"));
        right.add(new JLabel(nhanVien.getChucVu().getTenChucVu()));

        wrapper.add(left, BorderLayout.WEST);
        wrapper.add(right, BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);

        mainContent.add(panel, BorderLayout.CENTER);

        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= SIDEBAR =================
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(0, 90, 200));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // ===== ADMIN =====
        btnAdmin = menuButton(nhanVien.getHoTen(), true);
        btnAdmin.addActionListener(e -> showAccountPanel());

        sidebar.add(btnAdmin);
        sidebar.add(Box.createVerticalStrut(20));

        // ===== TRANG CHỦ =====
        JButton tongQuan = subMenu("Tổng quan");
        tongQuan.addActionListener(e -> showHomePanel());

        sidebar.add(parentMenu("Phòng", tongQuan));

        // ===== QUẢN LÝ =====
        JButton btnKhachHang = subMenu("Khách hàng");
        btnKhachHang.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new KhachHangUI(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
        JButton btnNhanVien = subMenu("Nhân viên");
        btnNhanVien.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new NhanVienUI(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        JButton btnPhong = subMenu("Phòng");
        JButton btnDichVu = subMenu("Dịch vụ");
        btnDichVu.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new DichVuUI(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });
        JButton btnTaiKhoan = subMenu("Tài Khoản");
        btnTaiKhoan.addActionListener(e -> {
            mainContent.removeAll();
            mainContent.add(new TaiKhoanUI(), BorderLayout.CENTER);
            mainContent.revalidate();
            mainContent.repaint();
        });

        JButton btnHoaDon = subMenu("Hóa Đơn");

        sidebar.add(parentMenu("Nhân Viên",
                btnKhachHang,
                btnNhanVien,
                btnPhong,
                btnDichVu,
                btnTaiKhoan,
                btnHoaDon
        ));

        // ===== THỐNG KÊ =====
        sidebar.add(parentMenu("Thống Kê",
                subMenu("Doanh thu Ngày"),
                subMenu("Doanh Thu Tháng")
        ));

        sidebar.add(Box.createVerticalGlue());

        // ===== LOGOUT =====
        JButton btnLogout = menuButton("Đăng Xuất", false);
        btnLogout.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });

        sidebar.add(btnLogout);

        return sidebar;
    }

    // ================= BUTTON =================
    private JButton menuButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(active ? new Color(0, 120, 255) : new Color(0, 90, 200));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    // ================= MENU CHA =================
    private JPanel parentMenu(String title, JButton... subs) {

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JButton parent = menuButton(title, false);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(0, 110, 220));
        subPanel.setVisible(false);

        for (JButton b : subs) {
            subPanel.add(b);
        }

        allSubMenus.add(subPanel);

        parent.addActionListener(e -> {
            boolean open = !subPanel.isVisible();
            for (JPanel p : allSubMenus) p.setVisible(false);
            subPanel.setVisible(open);
        });

        container.add(parent);
        container.add(subPanel);

        return container;
    }

    // ================= MENU CON =================
    private JButton subMenu(String text) {
        JButton btn = new JButton("• " + text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 140, 255));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}
*/
/*package GUI;

import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountDetailUI extends JFrame {

    private NhanVien nhanVien;
    private JPanel mainContent;

    public AccountDetailUI(NhanVien nv) {
        this.nhanVien = nv;
        initUI();
    }

    private void initUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // MENU NGANG
        add(createNavbar(), BorderLayout.NORTH);

        // MAIN CONTENT
        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        add(mainContent, BorderLayout.CENTER);

        showHomePanel();
    }

    // ================= NAVBAR =================
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navbar.setBackground(new Color(0, 90, 200));
        navbar.setPreferredSize(new Dimension(0, 60));

        // ===== ADMIN =====
        JButton btnAdmin = menuButton(nhanVien.getHoTen(), true);
        btnAdmin.addActionListener(e -> showAccountPanel());

        // ===== TRANG CHỦ =====
        JButton btnHome = menuButton("Trang Chủ", false);
        btnHome.addActionListener(e -> showHomePanel());

        // ===== KHÁCH HÀNG =====
        JButton btnKhachHang = menuButton("Khách Hàng", false);
        btnKhachHang.addActionListener(e -> loadPanel(new KhachHangUI()));

        // ===== NHÂN VIÊN =====
        JButton btnNhanVien = menuButton("Nhân Viên", false);
        btnNhanVien.addActionListener(e -> loadPanel(new NhanVienUI()));

        // ===== DỊCH VỤ =====
        JButton btnDichVu = menuButton("Dịch Vụ", false);
        btnDichVu.addActionListener(e -> loadPanel(new DichVuUI()));

        // ===== TÀI KHOẢN =====
        JButton btnTaiKhoan = menuButton("Tài Khoản", false);
        btnTaiKhoan.addActionListener(e -> loadPanel(new TaiKhoanUI()));

        // ===== ĐĂNG XUẤT =====
        JButton btnLogout = menuButton("Đăng Xuất", false);
        btnLogout.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });

        // ADD VÀO NAVBAR
        navbar.add(btnAdmin);
        navbar.add(btnHome);
        navbar.add(btnKhachHang);
        navbar.add(btnNhanVien);
        navbar.add(btnDichVu);
        navbar.add(btnTaiKhoan);
        navbar.add(btnLogout);

        return navbar;
    }

    // ================= BUTTON STYLE =================
    private JButton menuButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(active ? new Color(0, 120, 255) : new Color(0, 90, 200));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    // ================= LOAD PANEL =================
    private void loadPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= TRANG CHỦ =================
    private void showHomePanel() {
        loadPanel(new HomeUI());
    }

    // ================= ACCOUNT =================
    private void showAccountPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // TITLE
        JLabel title = new JLabel("Chi Tiết Tài Khoản", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));

        // WRAPPER
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));
        wrapper.setBackground(new Color(245, 245, 245));

        // ===== LEFT (AVATAR) =====
        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(300, 400));
        left.setBackground(Color.WHITE);

        JLabel avatar = new JLabel(new ImageIcon("photo/avatar.png"));
        avatar.setHorizontalAlignment(JLabel.CENTER);

        JLabel name = new JLabel(nhanVien.getHoTen(), JLabel.CENTER);
        name.setFont(new Font("Segoe UI", Font.BOLD, 18));

        left.add(avatar, BorderLayout.CENTER);
        left.add(name, BorderLayout.SOUTH);

        // ===== RIGHT (INFO) =====
        JPanel right = new JPanel(new GridLayout(6, 2, 20, 20));
        right.setBackground(new Color(245, 245, 245));

        right.add(new JLabel("Mã NV:"));
        right.add(new JLabel(nhanVien.getMaNV()));

        right.add(new JLabel("Giới tính:"));
        right.add(new JLabel(nhanVien.getGioiTinh() == 1 ? "Nam" : "Nữ"));

        right.add(new JLabel("Ngày sinh:"));
        right.add(new JLabel(nhanVien.getNgaySinh().toString()));

        right.add(new JLabel("CCCD:"));
        right.add(new JLabel(nhanVien.getCccd()));

        right.add(new JLabel("SĐT:"));
        right.add(new JLabel(nhanVien.getSdt()));

        right.add(new JLabel("Chức vụ:"));
        right.add(new JLabel(nhanVien.getChucVu().getTenChucVu()));

        wrapper.add(left, BorderLayout.WEST);
        wrapper.add(right, BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);

        loadPanel(panel);
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AccountDetailUI(new NhanVien()).setVisible(true);
        });
    }
}*/
package GUI;

import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountDetailUI extends JFrame {

    private NhanVien nhanVien;
    private JPanel mainContent;

    public AccountDetailUI(NhanVien nv) {
        this.nhanVien = nv;
        initUI();
    }

    // ================= INIT =================
    private void initUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createNavbar(), BorderLayout.NORTH);

        mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 245, 245));
        add(mainContent, BorderLayout.CENTER);

        showHomePanel();
    }

    // ================= NAVBAR =================
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navbar.setBackground(new Color(0, 90, 200));
        navbar.setPreferredSize(new Dimension(0, 60));

        // ===== ADMIN =====
        JButton btnAdmin = menuButton(nhanVien.getHoTen(), true);
        btnAdmin.addActionListener(e -> showAccountPanel());

        // ===== HỆ THỐNG =====
        JButton btnHeThong = menuButton("Hệ thống", false);
        btnHeThong.addActionListener(e -> showHomePanel());

        // ===== DANH MỤC (CÓ DROPDOWN) =====
        // ===== sửa phần này trong createNavbar() của AccountDetailUI.java =====

// ===== DANH MỤC (CÓ DROPDOWN) =====
        JButton btnDanhMuc = menuButton("Danh mục", false);

        JPopupMenu danhMucMenu = new JPopupMenu();

        JMenuItem khachHang = new JMenuItem("Khách hàng");
        khachHang.addActionListener(e -> loadPanel(new KhachHangUI()));

// đổi tên biến để không trùng với private NhanVien nhanVien;
        JMenuItem menuNhanVien = new JMenuItem("Nhân viên");
        menuNhanVien.addActionListener(e -> loadPanel(new NhanVienUI()));

        JMenuItem taiKhoan = new JMenuItem("Tài khoản");
        taiKhoan.addActionListener(e -> loadPanel(new TaiKhoanUI()));

        JMenuItem dichVu = new JMenuItem("Dịch vụ");
        dichVu.addActionListener(e -> loadPanel(new DichVuUI()));

        JMenuItem khuyenMai = new JMenuItem("Khuyến mãi");
        khuyenMai.addActionListener(e -> loadPanel(new KhuyenMaiUI()));

        JMenuItem phong = new JMenuItem("Phòng");
        phong.addActionListener(e -> loadPanel(new PhongUI()));

        danhMucMenu.add(khachHang);
        danhMucMenu.add(menuNhanVien); // sửa chỗ này
        danhMucMenu.add(taiKhoan);
        danhMucMenu.add(dichVu);
        danhMucMenu.add(khuyenMai);
        danhMucMenu.add(phong);

        btnDanhMuc.addActionListener(e -> {
            danhMucMenu.show(btnDanhMuc, 0, btnDanhMuc.getHeight());
        });


// ===== XỬ LÝ =====
        JButton btnXuLy = menuButton("Xử lý", false);

        JPopupMenu xuLyMenu = new JPopupMenu();

// sửa constructor DatPhongUI phải truyền maNV
        JMenuItem datPhong = new JMenuItem("Đặt phòng");
        datPhong.addActionListener(e ->
                loadPanel(new DatPhongUI(nhanVien.getMaNV()))
        );

// sửa constructor NhanPhongUI phải truyền maNV
        JMenuItem nhanPhong = new JMenuItem("Nhận Phòng");
        nhanPhong.addActionListener(e ->
                loadPanel(new NhanPhongUI(nhanVien.getMaNV()))
        );

        JMenuItem giahanPhong = new JMenuItem("Gia Hạn Phòng");
        giahanPhong.addActionListener(e ->
                loadPanel(new GiaHanPhong_UI())
        );

        JMenuItem thanhToan = new JMenuItem("Thanh toán - Trả Phòng");
        thanhToan.addActionListener(e ->
                loadPanel(new TraPhong_UI(nhanVien.getMaNV()))
        );
        JMenuItem dichVuPhong = new JMenuItem("Dịch Vụ Phòng");
        dichVuPhong.addActionListener(e ->loadPanel(new DichVuPhong_UI()));
        JMenuItem thuePhong = new JMenuItem("Thuê Phòng");
        thuePhong.addActionListener(e ->loadPanel(new ThuePhong_UI()));

        xuLyMenu.add(nhanPhong);
        xuLyMenu.add(giahanPhong);
        xuLyMenu.add(thanhToan);
        xuLyMenu.add(datPhong);
        xuLyMenu.add(dichVuPhong);
        xuLyMenu.add(thuePhong);

        btnXuLy.addActionListener(e -> {
            xuLyMenu.show(btnXuLy, 0, btnXuLy.getHeight());
        });

        // ===== TÌM KIẾM =====
        // ===== TÌM KIẾM (CÓ DROPDOWN) =====
        JButton btnTimKiem = menuButton("Tìm kiếm", false);


        JPopupMenu timKiemMenu = new JPopupMenu();

        JMenuItem tkHoaDon = new JMenuItem("Hóa đơn");
        tkHoaDon.addActionListener(e -> loadPanel(new Search_HoaDon_UI()));
        JMenuItem tkkhachHang = new JMenuItem("Khách Hàng");
        tkkhachHang.addActionListener(e -> loadPanel(new Search_KhachHang_UI()));
        JMenuItem tktaiKhoan = new JMenuItem("Tài Khoản");
        tktaiKhoan.addActionListener(e -> loadPanel(new Search_TaiKhoan_UI()));
        JMenuItem tkphongKS = new JMenuItem("Phòng");
        tkphongKS.addActionListener(e -> loadPanel(new Search_Phong_UI()));
        JMenuItem tknhanVien = new JMenuItem("Nhân Viên");
        tknhanVien.addActionListener(e -> loadPanel(new Search_NhanVien_UI()));


        // nếu bạn muốn thêm mục nữa thì thêm ở đây
        // JMenuItem tkKhac = new JMenuItem("...");
        // timKiemMenu.add(tkKhac);

        timKiemMenu.add(tkHoaDon);
        timKiemMenu.add(tkkhachHang);
        timKiemMenu.add(tktaiKhoan);
        timKiemMenu.add(tknhanVien);
        timKiemMenu.add(tkphongKS);


        btnTimKiem.addActionListener(e -> {
            timKiemMenu.show(btnTimKiem, 0, btnTimKiem.getHeight());
        });

        // ===== THỐNG KÊ =====
        JButton btnThongKe = menuButton("Thống kê", false);

        JPopupMenu thongKeMenu = new JPopupMenu();

        JMenuItem tkDoanhThu = new JMenuItem("Thống kê doanh thu");
        tkDoanhThu.addActionListener(e -> loadPanel(new Manager_Stastics_UI()));

        JMenuItem tkPhong = new JMenuItem("Thống kê phòng");
        tkPhong.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.add(new JLabel("Thống kê phòng"));
            loadPanel(panel);
        });

        thongKeMenu.add(tkDoanhThu);
        thongKeMenu.add(tkPhong);

        btnThongKe.addActionListener(e -> {
            thongKeMenu.show(btnThongKe, 0, btnThongKe.getHeight());
        });

        // ===== LOGOUT =====
        JButton btnLogout = menuButton("Đăng Xuất", false);
        btnLogout.addActionListener(e -> {
            new LoginUI().setVisible(true);
            dispose();
        });

        // ADD
        navbar.add(btnAdmin);
        navbar.add(btnHeThong);
        navbar.add(btnDanhMuc);
        navbar.add(btnXuLy);
        navbar.add(btnTimKiem);
        navbar.add(btnThongKe);
        navbar.add(btnLogout);

        return navbar;
    }


    // ================= BUTTON STYLE =================
    private JButton menuButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(active ? new Color(0, 120, 255) : new Color(0, 90, 200));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    // ================= LOAD PANEL =================
    private void loadPanel(JPanel panel) {
        mainContent.removeAll();
        mainContent.add(panel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    // ================= TRANG CHỦ =================
    private void showHomePanel  () {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 235, 255)); // xanh nhạt

        // ===== LEFT (LOGO TO) =====
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(220, 235, 255));
        left.setPreferredSize(new Dimension(600, 0)); // tăng kích thước logo

        ImageIcon icon = new ImageIcon("photo/img_2.png");
        Image img = icon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setHorizontalAlignment(JLabel.CENTER);

        left.add(logo, BorderLayout.CENTER);

        // ===== RIGHT (NỘI DUNG GỌN) =====
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(220, 235, 255));
        right.setBorder(new EmptyBorder(50, 20, 50, 20));

        JLabel title = new JLabel("Hệ thống khách sạn Luxury");
        title.setFont(new Font("Arial", Font.BOLD, 25)); // font đẹp hơn + size 25
        title.setForeground(new Color(0, 60, 130));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel clock = new JLabel("");
        clock.setFont(new Font("Segoe UI", Font.BOLD, 42));
        clock.setForeground(new Color(0, 90, 200));
        clock.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel date = new JLabel("");
        date.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        right.add(Box.createVerticalGlue());
        right.add(title);
        right.add(Box.createVerticalStrut(25));
        right.add(clock);
        right.add(Box.createVerticalStrut(10));
        right.add(date);
        right.add(Box.createVerticalGlue());

        // ===== TIMER =====
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();

            java.time.format.DateTimeFormatter timeFormat =
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

            java.time.format.DateTimeFormatter dateFormat =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            clock.setText(now.format(timeFormat));
            date.setText(" " + now.format(dateFormat));
        });
        timer.start();

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.CENTER);

        loadPanel(panel);
    }

    // ================= ACCOUNT =================
    private void showAccountPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // TITLE
        JLabel title = new JLabel("Chi Tiết Tài Khoản", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));

        // WRAPPER
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));
        wrapper.setBackground(new Color(245, 245, 245));

        // ===== LEFT (AVATAR) =====
        JPanel left = new JPanel(new BorderLayout());
        left.setPreferredSize(new Dimension(300, 400));
        left.setBackground(Color.WHITE);

        String duongDanAnh = nhanVien.getAnhNhanVien(); // hoặc getHinhAnh()

        ImageIcon icon;

        if (duongDanAnh != null
                && !duongDanAnh.trim().isEmpty()) {

            Image img = new ImageIcon(duongDanAnh)
                    .getImage()
                    .getScaledInstance(
                            250,
                            300,
                            Image.SCALE_SMOOTH
                    );

            icon = new ImageIcon(img);

        } else {

            Image img = new ImageIcon("photo/default.png")
                    .getImage()
                    .getScaledInstance(
                            250,
                            300,
                            Image.SCALE_SMOOTH
                    );

            icon = new ImageIcon(img);
        }

        JLabel avatar = new JLabel(icon);
        avatar.setHorizontalAlignment(JLabel.CENTER);

        JLabel name = new JLabel(nhanVien.getHoTen(), JLabel.CENTER);
        name.setFont(new Font("Segoe UI", Font.BOLD, 18));

        left.add(avatar, BorderLayout.CENTER);
        left.add(name, BorderLayout.SOUTH);

        // ===== RIGHT (INFO) =====
        JPanel right = new JPanel(new GridLayout(6, 2, 20, 20));
        right.setBackground(new Color(245, 245, 245));

        right.add(new JLabel("Mã NV:"));
        right.add(new JLabel(nhanVien.getMaNV() != null ? nhanVien.getMaNV() : ""));

        right.add(new JLabel("Giới tính:"));
        right.add(new JLabel(nhanVien.getGioiTinh() == 1 ? "Nam" : "Nữ"));

        right.add(new JLabel("Ngày sinh:"));
        right.add(new JLabel(
                nhanVien.getNgaySinh() != null
                        ? nhanVien.getNgaySinh().toString()
                        : "Chưa có"
        ));

        right.add(new JLabel("CCCD:"));
        right.add(new JLabel(
                nhanVien.getCccd() != null ? nhanVien.getCccd() : "Chưa có"
        ));

        right.add(new JLabel("SĐT:"));
        right.add(new JLabel(
                nhanVien.getSdt() != null ? nhanVien.getSdt() : "Chưa có"
        ));

        right.add(new JLabel("Chức vụ:"));
        right.add(new JLabel(
                nhanVien.getChucVu() != null
                        ? nhanVien.getChucVu().getTenChucVu()
                        : "Chưa có"
        ));

        wrapper.add(left, BorderLayout.WEST);
        wrapper.add(right, BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);

        loadPanel(panel);
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AccountDetailUI(new NhanVien()).setVisible(true);
        });
    }
}