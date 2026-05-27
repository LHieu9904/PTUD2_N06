package GUI;

import Dao.DichVuDao;
import Entity.DichVu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class DichVuUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtGia;
    private JComboBox<String> cbTrangThai;

    private DichVuDao dao = new DichVuDao();

    public DichVuUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ================= TITLE =================
        JLabel title = new JLabel("QUẢN LÝ DỊCH VỤ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(30, 41, 59));
        title.setBorder(new EmptyBorder(20, 30, 10, 0));

        // ================= FORM CARD =================
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 25, 15, 25),
                new LineBorder(new Color(230, 230, 230), 1, true)
        ));

        JLabel formTitle = new JLabel("Thông tin dịch vụ");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(new Color(51, 65, 85));
        formTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel form = new JPanel(new GridLayout(2, 4, 25, 18));
        form.setBackground(Color.WHITE);

        txtMa = createTextField();
        txtMa.setEditable(false); // KHÔNG CHO SỬA MÃ DỊCH VỤ
        txtMa.setBackground(new Color(241, 245, 249)); // Đổi màu nền để user biết là textfield bị khóa

        txtTen = createTextField();
        txtGia = createTextField();

        cbTrangThai = new JComboBox<>(new String[]{"Còn", "Hết"});
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTrangThai.setPreferredSize(new Dimension(100, 40));

        form.add(label("Mã dịch vụ (Tự sinh)")); form.add(txtMa);
        form.add(label("Tên dịch vụ")); form.add(txtTen);
        form.add(label("Đơn giá")); form.add(txtGia);
        form.add(label("Trạng thái")); form.add(cbTrangThai);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);

        // ================= BUTTON =================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        btnPanel.setBackground(new Color(245, 247, 250));

        JButton btnThem = createButton("THÊM", new Color(37, 99, 235));
        JButton btnSua = createButton("SỬA", new Color(234, 179, 8));
        JButton btnXoa = createButton("XÓA", new Color(220, 38, 38));
        JButton btnReload = createButton("LÀM MỚI", new Color(100, 116, 139));

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnReload);

        // ================= TABLE =================
        model = new DefaultTableModel(new String[]{"Mã DV", "Tên DV", "Đơn giá", "Trạng thái"}, 0);
        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(235, 235, 235));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(30, 41, 59));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 42));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 25, 25, 25),
                new EmptyBorder(0, 0, 0, 0)
        ));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("Danh sách dịch vụ");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableTitle.setForeground(new Color(51, 65, 85));
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        tableWrapper.add(tableTitle, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);
        tableCard.add(tableWrapper);

        // ================= MAIN =================
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(new EmptyBorder(0, 20, 20, 20));

        main.add(formCard);
        main.add(Box.createVerticalStrut(15));
        main.add(btnPanel);
        main.add(Box.createVerticalStrut(15));
        main.add(tableCard);

        add(title, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        // ================= EVENT =================

        // Click dòng nào đổ dữ liệu dòng đó lên Form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                txtMa.setText(getValue(row, 0));
                txtTen.setText(getValue(row, 1));
                txtGia.setText(getValue(row, 2));
                cbTrangThai.setSelectedItem(getValue(row, 3));
            }
        });

        // THÊM
        btnThem.addActionListener(e -> {
            if (!validateInput()) return;

            String ma = txtMa.getText().trim();

            // KIỂM TRA TRÙNG MÃ TRƯỚC KHI THÊM
            if (isMaTung(ma)) {
                JOptionPane.showMessageDialog(this, "Mã dịch vụ " + ma + " đã tồn tại! Vui lòng làm mới để tự sinh mã mới.");
                return;
            }

            DichVu dv = getForm();
            if (dao.insert(dv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                loadData();
                clearForm(); // Gọi clearForm() để tự phát sinh mã tiếp theo luôn
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        });

        // SỬA
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dịch vụ trong bảng để sửa!");
                return;
            }

            if (!validateInput()) return;

            DichVu dv = getForm();
            if (dao.update(dv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
            }
        });

        // XÓA
        btnXoa.addActionListener(e -> {
            String ma = txtMa.getText();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn dịch vụ cần xóa");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this, "Xóa dịch vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        });

        // RELOAD
        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });

        // Khởi tạo dữ liệu ban đầu
        loadData();
        clearForm();
    }

    // ================= UI COMPONENT =================
    private JLabel label(String t) {
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lb.setForeground(new Color(71, 85, 105));
        return lb;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(100, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 42));
        return btn;
    }

    // ================= SUPPORT =================
    private void loadData() {
        model.setRowCount(0);
        List<DichVu> list = dao.getAll();
        for (DichVu dv : list) {
            model.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getDonGia(),
                    dv.getTrangThai()
            });
        }
    }

    private String getValue(int row, int col) {
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private DichVu getForm() {
        DichVu dv = new DichVu();
        dv.setMaDichVu(txtMa.getText().trim());
        dv.setTenDichVu(txtTen.getText().trim());
        dv.setDonGia(Double.parseDouble(txtGia.getText().trim()));
        dv.setTrangThai(cbTrangThai.getSelectedItem().toString());
        return dv;
    }

    // Hàm tự động phát sinh mã DV tăng dần dựa vào danh sách hiện tại
    private String phatSinhMaDichVu() {
        int max = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String maHienTai = model.getValueAt(i, 0).toString(); // ví dụ: "DV005"
            try {
                // Cắt bỏ chữ "DV" lấy phần số phía sau để so sánh
                int so = Integer.parseInt(maHienTai.substring(2));
                if (so > max) {
                    max = so;
                }
            } catch (Exception e) {
                // Bỏ qua nếu lỗi format chuỗi
            }
        }
        // Trả về chuỗi tăng thêm 1 đơn vị, định dạng đủ 3 chữ số: DV001, DV002,...
        return String.format("DV%03d", max + 1);
    }

    private void clearForm() {
        txtMa.setText(phatSinhMaDichVu()); // TỰ ĐỘNG SINH MÃ MỚI CHO FORM KHÔNG BỊ TRÙNG
        txtTen.setText("");
        txtGia.setText("");
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection(); // Bỏ chọn dòng trên table nếu có
    }

    // Hàm bổ sung kiểm tra xem mã định thêm đã tồn tại trong bảng chưa
    private boolean isMaTung(String ma) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                return true;
            }
        }
        return false;
    }

    // ================= VALIDATE =================
    private boolean validateInput() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String gia = txtGia.getText().trim();

        if (!ma.matches("^DV\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã DV phải dạng DV001!");
            return false;
        }

        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không được rỗng!");
            return false;
        }

        try {
            double dg = Double.parseDouble(gia);
            if (dg <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải > 0!");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ!");
            return false;
        }
        return true;
    }
}