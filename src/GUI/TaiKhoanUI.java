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
        JButton btnReload = new JButton("Reload");

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

        if (maTK.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không được để trống");
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

    private JLabel label(String t) {
        return new JLabel(t);
    }
}