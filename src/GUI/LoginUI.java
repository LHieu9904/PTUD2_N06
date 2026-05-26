package GUI;

import Dao.TaiKhoanDao;
import Entity.NhanVien;
import Entity.TaiKhoan;

import Raven.button.Button;
import Raven.passwordfield.PasswordField;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Dao.NhanVienDao;

public class LoginUI extends JFrame {

    private TaiKhoanDao taiKhoanDao = new TaiKhoanDao();

    public LoginUI() {
        setTitle("Quản Lý Khách Sạn");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        // ================= LEFT IMAGE (GIỮ NGUYÊN) =================
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

        // ================= RIGHT FORM (GIỮ NGUYÊN) =================
        LinearColorPanel right = new LinearColorPanel();
        right.setStartColor(new Color(64, 164, 255));
        right.setEndColor(new Color(219, 241, 255));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(30, 60, 30, 60));
        add(right);

        JLabel title = new JLabel("Đăng Nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(title);

        right.add(Box.createVerticalStrut(45));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(300, 150));
        right.add(formPanel);

        JLabel lbUser = new JLabel("Tên đăng nhập");
        lbUser.setForeground(Color.WHITE);
        lbUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lbUser);

        formPanel.add(Box.createVerticalStrut(8));
        PasswordField txtUser = new PasswordField();
        txtUser.setEchoChar((char)0);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtUser.setMaximumSize(new Dimension(440, 50));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(txtUser);

        formPanel.add(Box.createVerticalStrut(30));

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

        // ===== OPTIONS (GIỮ NGUYÊN) =====
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
        btnForgot.addActionListener(e -> {

            JTextField txtUserForgot = new JTextField();
            JTextField txtCCCD = new JTextField();
            JPasswordField txtNewPass = new JPasswordField();

            Object[] fields = {
                    "Tên đăng nhập:", txtUserForgot,
                    "CCCD:", txtCCCD,
                    "Mật khẩu mới:", txtNewPass
            };

            int option = JOptionPane.showConfirmDialog(
                    this,
                    fields,
                    "Quên mật khẩu",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {

                String userF = txtUserForgot.getText();
                String cccd = txtCCCD.getText();
                String newPass = new String(txtNewPass.getPassword());

                if (userF.isEmpty() || cccd.isEmpty() || newPass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không được để trống!");
                    return;
                }

                TaiKhoanDao dao = new TaiKhoanDao();
                boolean ok = dao.resetPassword(userF, cccd, newPass);

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Sai thông tin!");
                }
            }
        });

        optionPanel.add(chkShow, BorderLayout.WEST);
        optionPanel.add(btnForgot, BorderLayout.EAST);
        formPanel.add(optionPanel);

        right.add(Box.createVerticalGlue());

        Button btnLogin = new Button();
        btnLogin.setText("Đăng Nhập Ngay");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnLogin.setRound(45);
        btnLogin.setBackground(new Color(0, 122, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(320, 60));
        // ===== ENTER LOGIN =====
        txtUser.addActionListener(e -> txtPass.requestFocus());

        txtPass.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (!user.isEmpty() && !pass.isEmpty()) {
                btnLogin.doClick();
            }
        });
        // ================= LOGIN + PHÂN QUYỀN =================
        btnLogin.addActionListener(e -> {

            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không được để trống!"
                );  
                return;
            }

            TaiKhoan tk = taiKhoanDao.login(user, pass);

            if (tk != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Đăng nhập thành công - " + tk.getHoTen()
                );

                // phân quyền theo Mã Chức Vụ
                String maChucVu = tk.getMaChucVu();

                if ("CV01".equalsIgnoreCase(maChucVu)) {

                    // QUẢN LÝ
                    NhanVien nv = new NhanVienDao()
                            .getById(tk.getMaNV());

                    new AccountDetailUI(nv).setVisible(true);

                }
                else if ("CV02".equalsIgnoreCase(maChucVu)) {

                    NhanVien nv = new NhanVienDao()
                            .getById(tk.getMaNV());

                    new AccountDetailUI(nv).setVisible(true);
                }
                else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Tài khoản chưa được phân quyền!"
                    );
                }

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Sai tài khoản hoặc mật khẩu!"
                );
            }
        });

        right.add(btnLogin);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}