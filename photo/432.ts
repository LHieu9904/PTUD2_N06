/*package GUI;

import Raven.button.Button;
import Raven.passwordfield.PasswordField;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        // ================= LEFT IMAGE (FULL) =================
        JPanel left = new JPanel() {
            Image img = new ImageIcon("photo/img_2.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(), h = getHeight();
                int iw = img.getWidth(null), ih = img.getHeight(null);

                double pr = (double) w / h;
                double ir = (double) iw / ih;

                int dw, dh, x, y;
                if (ir > pr) {
                    dh = h;
                    dw = (int) (h * ir);
                    x = (w - dw) / 2;
                    y = 0;
                } else {
                    dw = w;
                    dh = (int) (w / ir);
                    x = 0;
                    y = (h - dh) / 2;
                }
                g.drawImage(img, x, y, dw, dh, this);
            }
        };
        add(left);

        // ================= RIGHT FORM =================
        LinearColorPanel right = new LinearColorPanel();
        right.setStartColor(new Color(64, 164, 255));
        right.setEndColor(new Color(219, 241, 255));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(30, 60, 30, 60));
        add(right);

        // ===== TITLE (CENTER) =====
        JLabel title = new JLabel("Đăng Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(title);

        right.add(Box.createVerticalStrut(45));

        // ===== FORM PANEL (CENTER BLOCK) =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(460, 280));
        right.add(formPanel);

        // ---------- USER ----------
        JLabel lbUser = new JLabel("Tên đăng nhập");
        lbUser.setForeground(Color.WHITE);
        lbUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lbUser);

        formPanel.add(Box.createVerticalStrut(8));

        TextField txtUser = new TextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtUser.setMaximumSize(new Dimension(440, 50));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtUser);

        formPanel.add(Box.createVerticalStrut(22));

        // ---------- PASSWORD ----------
        JLabel lbPass = new JLabel("Mật khẩu");
        lbPass.setForeground(Color.WHITE);
        lbPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lbPass);

        formPanel.add(Box.createVerticalStrut(8));

        PasswordField txtPass = new PasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtPass.setMaximumSize(new Dimension(440, 50));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtPass);

        // ===== OPTIONS (LOWER) =====
        formPanel.add(Box.createVerticalStrut(30));

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.setOpaque(false);
        optionPanel.setMaximumSize(new Dimension(440, 30));
        optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox chkShow = new JCheckBox("Hiện mật khẩu");
        chkShow.setOpaque(false);
        chkShow.setForeground(Color.WHITE);
        chkShow.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkShow.addActionListener(e ->
                txtPass.setEchoChar(chkShow.isSelected() ? (char) 0 : '*')
        );

        Button btnForgot = new Button();
        btnForgot.setText("Quên mật khẩu");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        optionPanel.add(chkShow, BorderLayout.WEST);
        optionPanel.add(btnForgot, BorderLayout.EAST);
        formPanel.add(optionPanel);

        // ===== PUSH LOGIN BUTTON DOWN =====
        right.add(Box.createVerticalGlue());

        // ===== LOGIN BUTTON (CENTER) =====
        Button btnLogin = new Button();
        btnLogin.setText("Đăng Nhập Ngay");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnLogin.setRound(45);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(320, 60));

        right.add(btnLogin);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}*/

/*
package GUI;

import Raven.button.Button;
import Raven.passwordfield.PasswordField;
import Raven.textfield.TextField;

import ConnectDB.database;
import Dao.TaiKhoanDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;

public class LoginUI extends JFrame {

    // ===== CHỈ THÊM =====
    private TaiKhoanDao taiKhoanDao = new TaiKhoanDao();

    public LoginUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        // ================= LEFT IMAGE (FULL) =================
        JPanel left = new JPanel() {
            Image img = new ImageIcon("photo/img_2.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(), h = getHeight();
                int iw = img.getWidth(null), ih = img.getHeight(null);

                double pr = (double) w / h;
                double ir = (double) iw / ih;

                int dw, dh, x, y;
                if (ir > pr) {
                    dh = h;
                    dw = (int) (h * ir);
                    x = (w - dw) / 2;
                    y = 0;
                } else {
                    dw = w;
                    dh = (int) (w / ir);
                    x = 0;
                    y = (h - dh) / 2;
                }
                g.drawImage(img, x, y, dw, dh, this);
            }
        };
        add(left);

        // ================= RIGHT FORM =================
        LinearColorPanel right = new LinearColorPanel();
        right.setStartColor(new Color(64, 164, 255));
        right.setEndColor(new Color(219, 241, 255));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(30, 60, 30, 60));
        add(right);

        // ===== TITLE (CENTER) =====
        JLabel title = new JLabel("Đăng Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(title);

        right.add(Box.createVerticalStrut(45));

        // ===== FORM PANEL (CENTER BLOCK) =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(460, 280));
        right.add(formPanel);

        // ---------- USER ----------
        JLabel lbUser = new JLabel("Tên đăng nhập");
        lbUser.setForeground(Color.WHITE);
        lbUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lbUser);

        formPanel.add(Box.createVerticalStrut(8));

        TextField txtUser = new TextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtUser.setMaximumSize(new Dimension(440, 50));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtUser);

        formPanel.add(Box.createVerticalStrut(22));

        // ---------- PASSWORD ----------
        JLabel lbPass = new JLabel("Mật khẩu");
        lbPass.setForeground(Color.WHITE);
        lbPass.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lbPass);

        formPanel.add(Box.createVerticalStrut(8));

        PasswordField txtPass = new PasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtPass.setMaximumSize(new Dimension(440, 50));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtPass);

        // ===== OPTIONS (LOWER) =====
        formPanel.add(Box.createVerticalStrut(30));

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.setOpaque(false);
        optionPanel.setMaximumSize(new Dimension(440, 30));
        optionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox chkShow = new JCheckBox("Hiện mật khẩu");
        chkShow.setOpaque(false);
        chkShow.setForeground(Color.WHITE);
        chkShow.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkShow.addActionListener(e ->
                txtPass.setEchoChar(chkShow.isSelected() ? (char) 0 : '*')
        );

        Button btnForgot = new Button();
        btnForgot.setText("Quên mật khẩu");
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        optionPanel.add(chkShow, BorderLayout.WEST);
        optionPanel.add(btnForgot, BorderLayout.EAST);
        formPanel.add(optionPanel);

        // ===== PUSH LOGIN BUTTON DOWN =====
        right.add(Box.createVerticalGlue());

        // ===== LOGIN BUTTON (CENTER) =====
        Button btnLogin = new Button();
        btnLogin.setText("Đăng Nhập Ngay");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnLogin.setRound(45);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(320, 60));

        // ===== CHỈ THÊM LOGIC LOGIN =====
        btnLogin.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = String.valueOf(txtPass.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                        "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                database.getInstance().connect();
                ResultSet rs = taiKhoanDao.dangNhap(username, password);

                if (rs != null && rs.next()) {
                    String hoTen = rs.getString("HoTen");
                    String chucVu = rs.getString("TenChucVu"); //

                    JOptionPane.showMessageDialog(
                            this,
                            " Đăng nhập thành công!\n\n"
                                    + "Họ tên: " + hoTen + "\n"
                                    + "Chức vụ: " + chucVu,
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            " Sai tên đăng nhập hoặc mật khẩu!",
                            "Đăng nhập thất bại",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        " Không thể kết nối database!",
                        "Lỗi hệ thống",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        right.add(btnLogin);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
*/
/*package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDetailUI extends JFrame {

    private JLabel currentPageLabel;
    private final List<JPanel> allSubMenus = new ArrayList<>();

    public AccountDetailUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);
    }

    public AccountDetailUI(String hoTen, String chucVu) {
    }

    // ================= MAIN =================
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Chức năng được chọn", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        main.add(title, BorderLayout.NORTH);

        currentPageLabel = new JLabel("Trang hiện tại: admin", JLabel.CENTER);
        currentPageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        main.add(currentPageLabel, BorderLayout.CENTER);

        return main;
    }

    // ================= SIDEBAR =================
    private JPanel createSidebar() {

        LinearColorPanel sidebar = new LinearColorPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // ADMIN
        sidebar.add(menuButton("admin", true, "admin"));
        sidebar.add(Box.createVerticalStrut(16));

        // TRANG CHỦ
        sidebar.add(parentMenu(
                "Trang Chủ",
                subMenu("Tổng quan", "Trang Chủ / Tổng quan"),
                subMenu("Phòng trống", "Trang Chủ / Phòng trống")
        ));

        // QUẢN LÝ
        sidebar.add(parentMenu(
                "Quản Lý",
                subMenu("Khách hàng", "Quản Lý / Khách hàng"),
                subMenu("Nhân viên", "Quản Lý / Nhân viên"),
                subMenu("Phòng", "Quản Lý / Phòng"),
                subMenu("Dịch Vụ", "Quản Lý / Dịch Vụ"),
                subMenu("Tài Khoản", "Quản Lý / Tài Khoản"),
                subMenu("Hóa Đơn", "Quản Lý / Hóa Đơn")
        ));

        // THỐNG KÊ
        sidebar.add(parentMenu(
                "Thống Kê",
                subMenu("Doanh thu Ngày", "Thống Kê / Doanh thu Ngày"),
                subMenu("Doanh Thu Tháng", "Thống Kê / Doanh Thu Tháng")
        ));


        sidebar.add(parentMenu(
                "Tìm kiếm",
                subMenu("Tài Khoản", "Thống Kê / Doanh thu"),
                subMenu("Khách Hàng", "Thống Kê / Doanh thu"),
                subMenu("Nhân Viên", "Thống Kê / Doanh thu")

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

        btn.addActionListener(e ->
                currentPageLabel.setText("Trang hiện tại: " + page)
        );

        return btn;
    }

    /**
     * MENU CHA (Accordion)
     */
/*
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


            for (JPanel p : allSubMenus) {
                p.setVisible(false);
            }


            subPanel.setVisible(willOpen);

            revalidate();
            repaint();
            currentPageLabel.setText("Trang hiện tại: " + title);
        });

        container.add(parentBtn);
        container.add(subPanel);

        return container;
    }

    /**
     * MENU CON
     */
/*
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

        btn.addActionListener(e ->
                currentPageLabel.setText("Trang hiện tại: " + page)
        );

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new AccountDetailUI().setVisible(true)
        );
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

    // ===== LABEL GIỮ TRẠNG THÁI =====
    private JLabel titleLabel;        // "Chức năng được chọn"
    private JLabel currentPageLabel;  // "Trang hiện tại: ..."
    private final List<JPanel> allSubMenus = new ArrayList<>();

    // ===== CONSTRUCTOR KHÔNG THAM SỐ (GIỮ NGUYÊN) =====
    public AccountDetailUI() {
        initUI();
    }

    // ===== CONSTRUCTOR CÓ THAM SỐ (KHÔNG ĐỔI UI) =====
    public AccountDetailUI(String hoTen, String chucVu) {
        initUI();
        // chỉ đổi text, không đổi layout
        titleLabel.setText("Thông tin tài khoản");
        currentPageLabel.setText("Trang hiện tại: " + hoTen + " - " + chucVu);
    }

    public AccountDetailUI(NhanVien nv) {
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

        // ✅ DÒNG "CHỨC NĂNG ĐƯỢC CHỌN"
        titleLabel = new JLabel("Chức năng được chọn", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        main.add(titleLabel, BorderLayout.NORTH);

        // ✅ DÒNG "TRANG HIỆN TẠI"
        currentPageLabel = new JLabel("Trang hiện tại: admin", JLabel.CENTER);
        currentPageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        main.add(currentPageLabel, BorderLayout.CENTER);

        return main;
    }

    // ================= SIDEBAR (GIỮ NGUYÊN 100%) =================
    private JPanel createSidebar() {

        LinearColorPanel sidebar = new LinearColorPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // ADMIN
        sidebar.add(menuButton("admin", true, "admin"));
        sidebar.add(Box.createVerticalStrut(16));

        // TRANG CHỦ
        sidebar.add(parentMenu(
                "Trang Chủ",
                subMenu("Tổng quan", "Trang Chủ Tổng quan"),
                subMenu("Phòng trống", "Trang Chủ Phòng trống")
        ));

        // QUẢN LÝ
        sidebar.add(parentMenu(
                "Quản Lý",
                subMenu("Khách hàng", "Quản Lý Khách hàng"),
                subMenu("Nhân viên", "Quản Lý Nhân viên"),
                subMenu("Phòng", "Quản Lý Phòng"),
                subMenu("Dịch Vụ", "Quản Lý Dịch Vụ"),
                subMenu("Tài Khoản", "Quản Lý Tài Khoản"),
                subMenu("Hóa Đơn", "Quản Lý Hóa Đơn")
        ));

        // THỐNG KÊ
        sidebar.add(parentMenu(
                "Thống Kê",
                subMenu("Doanh thu Ngày", "Thống Kê Doanh thu Ngày"),
                subMenu("Doanh Thu Tháng", "Thống Kê Doanh Thu Tháng")
        ));

        // TÌM KIẾM
        sidebar.add(parentMenu(
                "Tìm kiếm",
                subMenu("Tài Khoản", "Tìm kiếm Tài Khoản"),
                subMenu("Khách Hàng", "Tìm kiếm Khách Hàng"),
                subMenu("Nhân Viên", "Tìm kiếm Nhân Viên")
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

    /**
     * MENU CHA
     */
/**
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

 for (JPanel p : allSubMenus) {
 p.setVisible(false);
 }

 subPanel.setVisible(willOpen);

 // ✅ CẬP NHẬT "CHỨC NĂNG ĐƯỢC CHỌN"
 titleLabel.setText(title);
 currentPageLabel.setText("Trang hiện tại: " + title);
 });

 container.add(parentBtn);
 container.add(subPanel);

 return container;
 }

 /**
 * MENU CON
 */
/**
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
 // ✅ CHỈ ĐỔI TEXT
 titleLabel.setText(page);
 currentPageLabel.setText("Trang hiện tại: " + page);
 });

 return btn;
 }

 public static void main(String[] args) {
 SwingUtilities.invokeLater(() ->
 new AccountDetailUI().setVisible(true)
 );
 }
 }**/