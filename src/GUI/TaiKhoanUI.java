/*
package GUI;

import Dao.TaiKhoanDao;
import Entity.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaiKhoanUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    // giữ nguyên UI cũ, không thay đổi giao diện
    private JTextField txtUser;          // dùng hiển thị MaTK
    private JPasswordField txtPass;      // mật khẩu
    private JComboBox<String> cbRole;    // chức vụ
    private JComboBox<String> cbTrangThai; // trạng thái

    private TaiKhoanDao dao = new TaiKhoanDao();

    public TaiKhoanUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(2, 4, 30, 20));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        txtUser = new JTextField();
        txtUser.setEditable(false); // MaTK không cho sửa

        txtPass = new JPasswordField();

        cbRole = new JComboBox<>(new String[]{
                "Quản Lý",
                "Lễ Tân"
        });

        cbTrangThai = new JComboBox<>(new String[]{
                "Hoạt động",
                "Khóa"
        });

        form.add(label("Mã tài khoản"));
        form.add(txtUser);

        form.add(label("Mật khẩu"));
        form.add(txtPass);

        form.add(label("Chức vụ"));
        form.add(cbRole);

        form.add(label("Trạng thái"));
        form.add(cbTrangThai);

        // ===== BUTTON =====
        JPanel btnPanel = new JPanel();

        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnReload = new JButton("Làm mới");

        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{
                        "Mã TK",
                        "Tên đăng nhập",
                        "Mật khẩu",
                        "Trạng thái",
                        "Chức vụ"
                }, 0
        );

        table = new JTable(model);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        // ===== MAIN =====
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        main.add(form);
        main.add(btnPanel);
        main.add(scroll);

        add(title, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        loadData();

        // ===== EVENT SỬA =====
        btnUpdate.addActionListener(e -> {

            TaiKhoan tk = getFormData();
            if (tk == null) return;

            if (dao.updateOnlyAccount(tk)) {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật thành công");

                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật thất bại");
            }
        });

        // ===== EVENT XÓA =====
        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn tài khoản cần xóa");
                return;
            }

            // nếu MaNV không hiển thị trên table
            // thì lấy từ TenDangNhap luôn vì:
            // username = MaNV

            // lấy MaTK trước
            String maTK = model.getValueAt(row, 0).toString();

// vì UI không hiện MaNV
// nên lấy trực tiếp từ danh sách DAO
            List<TaiKhoan> list = dao.getAll();

            String maNV = null;

            for (TaiKhoan tk : list) {
                if (tk.getMaTK().equals(maTK)) {
                    maNV = tk.getMaNV();
                    break;
                }
            }

            if (maNV == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhân viên");
                return;
            }

            if (dao.delete(maNV)) {

                JOptionPane.showMessageDialog(this,
                        "Xóa thành công");

                loadData();
                clearForm();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Xóa thất bại");
            }
        });

        // ===== EVENT RELOAD =====
        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });
    }

    // ===== MAP CHỨC VỤ =====
    private String mapRoleToUI(String role) {

        if (role == null || role.trim().isEmpty()) {
            return "Lễ Tân";
        }

        return "CV01".equals(role)
                ? "Quản Lý"
                : "Lễ Tân";
    }
    private String mapRoleToDB(String role) {

        if (role == null || role.trim().isEmpty()) {
            return "CV02";
        }

        return "Quản Lý".equals(role)
                ? "CV01"
                : "CV02";
    }



    // ===== GET DATA =====
    private TaiKhoan getFormData() {

        String maTK = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (!validateMatKhau(pass)) {
            return null;
        }

        TaiKhoan tk = new TaiKhoan();

        tk.setMaTK(maTK);
        // username = MaNV lấy từ tài khoản đã chọn
        for (TaiKhoan item : dao.getAll()) {
            if (item.getMaTK().equals(maTK)) {
                tk.setTenDangNhap(item.getTenDangNhap());
                tk.setMaNV(item.getMaNV());
                break;
            }
        }
        tk.setMatKhau(pass);
        tk.setTrangThai(
                cbTrangThai.getSelectedItem().toString()
        );

        tk.setMaChucVu(
                mapRoleToDB(
                        cbRole.getSelectedItem().toString()
                )
        );

        return tk;
    }

    // ===== LOAD DATA =====
    private void loadData() {

        model.setRowCount(0);

        List<TaiKhoan> list = dao.getAll();

        for (TaiKhoan tk : list) {

            model.addRow(new Object[]{
                    tk.getMaTK(),
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getTrangThai(),
                    mapRoleToUI(tk.getMaChucVu())
            });
        }
    }

    // ===== FILL FORM =====
    private void fillForm() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        // Cột 0 = Mã TK
        txtUser.setText(
                model.getValueAt(row, 0).toString()
        );

        // Cột 1 = Tên đăng nhập (không cần set vào txtUser)
        // Cột 2 = Mật khẩu
        txtPass.setText(
                model.getValueAt(row, 2).toString()
        );

        // Cột 3 = Trạng thái
        cbTrangThai.setSelectedItem(
                model.getValueAt(row, 3).toString()
        );

        // Cột 4 = Chức vụ
        cbRole.setSelectedItem(
                model.getValueAt(row, 4).toString()
        );
    }


    // ===== CLEAR =====
    private void clearForm() {
        txtUser.setText("");
        txtPass.setText("");
        cbRole.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
    }
    private boolean validateMatKhau(String matKhau) {

        // 8 - 12 ký tự
        if (matKhau.length() < 8 || matKhau.length() > 12) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải từ 8 - 12 ký tự!"
            );

            return false;
        }

        // ít nhất 1 chữ hoa
        if (!matKhau.matches(".*[A-Z].*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 chữ HOA!"
            );

            return false;
        }

        // ít nhất 1 chữ thường
        if (!matKhau.matches(".*[a-z].*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 chữ thường!"
            );

            return false;
        }

        // ít nhất 1 số
        if (!matKhau.matches(".*\\d.*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 số!"
            );

            return false;
        }

        // ít nhất 1 ký tự đặc biệt
        if (!matKhau.matches(".*[@#$%^&+=!*?_\\-].*")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 ký tự đặc biệt!"
            );

            return false;
        }

        return true;
    }

    private JLabel label(String t) {
        return new JLabel(t);
    }
}*/
package GUI;

import Dao.TaiKhoanDao;
import Entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TaiKhoanUI extends JPanel {

    // ================= COLOR =================

    private final Color BACKGROUND =
            new Color(241,245,249);

    private final Color CARD =
            Color.WHITE;

    private final Color PRIMARY =
            new Color(37,99,235);

    private final Color SUCCESS =
            new Color(16,185,129);

    private final Color WARNING =
            new Color(245,158,11);

    private final Color DANGER =
            new Color(239,68,68);

    private final Color BORDER =
            new Color(226,232,240);

    private final Color TEXT =
            new Color(30,41,59);

    // ================= COMPONENT =================

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtUser;
    private JPasswordField txtPass;

    private JComboBox<String> cbRole;
    private JComboBox<String> cbTrangThai;

    private TaiKhoanDao dao =
            new TaiKhoanDao();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public TaiKhoanUI() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND);

        // ================= TITLE =================

        JLabel title = new JLabel(
                "QUẢN LÝ TÀI KHOẢN",
                JLabel.CENTER
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(TEXT);

        title.setBorder(
                new EmptyBorder(20,0,10,0)
        );

        // ================= FORM =================

        JPanel form = new JPanel(
                new GridLayout(2,4,30,20)
        );

        form.setBackground(CARD);

        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(
                                25,25,25,25
                        )
                )
        );

        // ================= INPUT =================

        txtUser = new JTextField();

        txtUser.setEditable(false);

        txtPass = new JPasswordField();

        styleTextField(txtUser);

        stylePassword(txtPass);

        txtUser.setBackground(
                new Color(226,232,240)
        );

        cbRole = new JComboBox<>(
                new String[]{
                        "Quản Lý",
                        "Lễ Tân"
                }
        );

        cbTrangThai = new JComboBox<>(
                new String[]{
                        "Hoạt động",
                        "Khóa"
                }
        );

        styleCombo(cbRole);
        styleCombo(cbTrangThai);

        form.add(label("Mã tài khoản"));
        form.add(txtUser);

        form.add(label("Mật khẩu"));
        form.add(txtPass);

        form.add(label("Chức vụ"));
        form.add(cbRole);

        form.add(label("Trạng thái"));
        form.add(cbTrangThai);

        // ================= BUTTON =================

        JPanel btnPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        18,
                        10
                )
        );

        btnPanel.setBackground(BACKGROUND);

        JButton btnUpdate =
                createButton(
                        "SỬA",
                        PRIMARY
                );

        JButton btnDelete =
                createButton(
                        "XÓA",
                        DANGER
                );

        JButton btnReload =
                createButton(
                        "LÀM MỚI",
                        WARNING
                );

        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);

        // ================= TABLE =================

        model = new DefaultTableModel(
                new String[]{
                        "Mã TK",
                        "Tên đăng nhập",
                        "Mật khẩu",
                        "Trạng thái",
                        "Chức vụ"
                },
                0
        );

        table = new JTable(model);

        table.setRowHeight(38);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.setGridColor(BORDER);

        table.setSelectionBackground(
                new Color(191,219,254)
        );

        table.setSelectionForeground(Color.BLACK);

        JTableHeader header =
                table.getTableHeader();

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        header.setBackground(TEXT);

        header.setForeground(Color.WHITE);

        header.setPreferredSize(
                new Dimension(0,42)
        );

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        fillForm();
                    }
                });

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        // ================= WRAPPER =================

        JPanel wrapper = new JPanel();

        wrapper.setLayout(
                new BoxLayout(
                        wrapper,
                        BoxLayout.Y_AXIS
                )
        );

        wrapper.setBackground(BACKGROUND);

        wrapper.setBorder(
                new EmptyBorder(
                        20,40,20,40
                )
        );

        wrapper.add(form);

        wrapper.add(
                Box.createVerticalStrut(20)
        );

        wrapper.add(btnPanel);

        wrapper.add(
                Box.createVerticalStrut(20)
        );

        wrapper.add(scroll);

        // ================= ADD =================

        add(title, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);

        // ================= LOAD =================

        loadData();

        // =====================================================
        // EVENT UPDATE
        // =====================================================

        btnUpdate.addActionListener(e -> {

            TaiKhoan tk =
                    getFormData();

            if (tk == null) return;

            if (dao.updateOnlyAccount(tk)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thành công"
                );

                loadData();

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thất bại"
                );
            }
        });

        // =====================================================
        // EVENT DELETE
        // =====================================================

        btnDelete.addActionListener(e -> {

            int row =
                    table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn tài khoản cần xóa"
                );

                return;
            }

            String maTK =
                    model.getValueAt(
                            row,
                            0
                    ).toString();

            List<TaiKhoan> list =
                    dao.getAll();

            String maNV = null;

            for (TaiKhoan tk : list) {

                if (tk.getMaTK()
                        .equals(maTK)) {

                    maNV = tk.getMaNV();

                    break;
                }
            }

            if (maNV == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy nhân viên"
                );

                return;
            }

            if (dao.delete(maNV)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thành công"
                );

                loadData();

                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thất bại"
                );
            }
        });

        // =====================================================
        // EVENT RELOAD
        // =====================================================

        btnReload.addActionListener(e -> {

            loadData();

            clearForm();
        });
    }

    // =====================================================
    // STYLE
    // =====================================================

    private JTextField styleTextField(
            JTextField txt
    ){

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txt.setPreferredSize(
                new Dimension(200,40)
        );

        txt.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                5,10,5,10
                        )
                )
        );

        return txt;
    }

    private void stylePassword(
            JPasswordField txt
    ){

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txt.setPreferredSize(
                new Dimension(200,40)
        );

        txt.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                5,10,5,10
                        )
                )
        );
    }

    private void styleCombo(
            JComboBox<String> cb
    ){

        cb.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cb.setBackground(Color.WHITE);

        cb.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );
    }

    private JButton createButton(
            String text,
            Color bg
    ){

        JButton btn = new JButton(text);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        btn.setForeground(Color.WHITE);

        btn.setBackground(bg);

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setBorder(
                new EmptyBorder(
                        12,22,12,22
                )
        );

        return btn;
    }

    private JLabel label(String t){

        JLabel lb = new JLabel(t);

        lb.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lb.setForeground(TEXT);

        return lb;
    }

    // =====================================================
    // MAP ROLE
    // =====================================================

    private String mapRoleToUI(
            String role
    ){

        if(role == null
                || role.trim().isEmpty()){

            return "Lễ Tân";
        }

        return "CV01".equals(role)
                ? "Quản Lý"
                : "Lễ Tân";
    }

    private String mapRoleToDB(
            String role
    ){

        if(role == null
                || role.trim().isEmpty()){

            return "CV02";
        }

        return "Quản Lý".equals(role)
                ? "CV01"
                : "CV02";
    }

    // =====================================================
    // GET FORM DATA
    // =====================================================

    private TaiKhoan getFormData(){

        String maTK =
                txtUser.getText().trim();

        String pass =
                new String(
                        txtPass.getPassword()
                ).trim();

        if(!validateMatKhau(pass)){

            return null;
        }

        TaiKhoan tk =
                new TaiKhoan();

        tk.setMaTK(maTK);

        for(TaiKhoan item : dao.getAll()){

            if(item.getMaTK()
                    .equals(maTK)){

                tk.setTenDangNhap(
                        item.getTenDangNhap()
                );

                tk.setMaNV(
                        item.getMaNV()
                );

                break;
            }
        }

        tk.setMatKhau(pass);

        tk.setTrangThai(
                cbTrangThai
                        .getSelectedItem()
                        .toString()
        );

        tk.setMaChucVu(
                mapRoleToDB(
                        cbRole
                                .getSelectedItem()
                                .toString()
                )
        );

        return tk;
    }

    // =====================================================
    // LOAD DATA
    // =====================================================

    private void loadData(){

        model.setRowCount(0);

        List<TaiKhoan> list =
                dao.getAll();

        for(TaiKhoan tk : list){

            model.addRow(new Object[]{

                    tk.getMaTK(),

                    tk.getTenDangNhap(),

                    tk.getMatKhau(),

                    tk.getTrangThai(),

                    mapRoleToUI(
                            tk.getMaChucVu()
                    )
            });
        }
    }

    // =====================================================
    // FILL FORM
    // =====================================================

    private void fillForm(){

        int row =
                table.getSelectedRow();

        if(row == -1) return;

        txtUser.setText(
                model.getValueAt(
                        row,
                        0
                ).toString()
        );

        txtPass.setText(
                model.getValueAt(
                        row,
                        2
                ).toString()
        );

        cbTrangThai.setSelectedItem(
                model.getValueAt(
                        row,
                        3
                ).toString()
        );

        cbRole.setSelectedItem(
                model.getValueAt(
                        row,
                        4
                ).toString()
        );
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm(){

        txtUser.setText("");

        txtPass.setText("");

        cbRole.setSelectedIndex(0);

        cbTrangThai.setSelectedIndex(0);
    }

    // =====================================================
    // VALIDATE PASSWORD
    // =====================================================

    private boolean validateMatKhau(
            String matKhau
    ){

        if(matKhau.length() < 8
                || matKhau.length() > 12){

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải từ 8 - 12 ký tự!"
            );

            return false;
        }

        if(!matKhau.matches(
                ".*[A-Z].*"
        )){

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 chữ HOA!"
            );

            return false;
        }

        if(!matKhau.matches(
                ".*[a-z].*"
        )){

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 chữ thường!"
            );

            return false;
        }

        if(!matKhau.matches(
                ".*\\d.*"
        )){

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 số!"
            );

            return false;
        }

        if(!matKhau.matches(
                ".*[@#$%^&+=!*?_\\-].*"
        )){

            JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu phải có ít nhất 1 ký tự đặc biệt!"
            );

            return false;
        }

        return true;
    }
}