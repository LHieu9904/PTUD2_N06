package GUI;

import Dao.DichVuDao;
import Dao.HoaDonDichVuDao;
import Dao.PhongDao;
import Entity.DichVu;
import Entity.Phong;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DichVuPhong_UI extends JPanel {

    private JTable tablePhong;
    private DefaultTableModel modelPhong;

    private TextField txtMaPhong;
    private TextField txtTenKH;
    private TextField txtTrangThai;
    private TextField txtTongDV;

    private JTable tableDichVuDangDung;
    private DefaultTableModel modelDichVuDangDung;

    private final PhongDao phongDao = new PhongDao();
    private final DichVuDao dichVuDao = new DichVuDao();
    private final HoaDonDichVuDao hoaDonDichVuDao = new HoaDonDichVuDao();

    public DichVuPhong_UI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("DỊCH VỤ PHÒNG");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0, 153, 255));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 0));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.setBackground(Color.WHITE);
        add(main, BorderLayout.CENTER);

        // =====================================================
        // LEFT: DANH SÁCH PHÒNG
        // =====================================================
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.WHITE);

        JLabel lblPhong = new JLabel("PHÒNG ĐANG THUÊ / ĐÃ ĐẶT");
        lblPhong.setHorizontalAlignment(SwingConstants.CENTER);
        lblPhong.setFont(new Font("Tahoma", Font.BOLD, 16));
        left.add(lblPhong, BorderLayout.NORTH);

        modelPhong = new DefaultTableModel(
                new String[]{"Mã phòng", "Khách hàng", "Trạng thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhong = new JTable(modelPhong);
        tablePhong.setRowHeight(35);

        JScrollPane scrollPhong = new JScrollPane(tablePhong);
        scrollPhong.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        left.add(scrollPhong, BorderLayout.CENTER);
        main.add(left);

        // =====================================================
        // RIGHT: CHI TIẾT + DỊCH VỤ
        // =====================================================
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);

        JLabel lblRight = new JLabel("QUẢN LÝ DỊCH VỤ PHÒNG");
        lblRight.setHorizontalAlignment(SwingConstants.CENTER);
        lblRight.setFont(new Font("Tahoma", Font.BOLD, 16));
        right.add(lblRight, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtMaPhong = createField();
        txtTenKH = createField();
        txtTrangThai = createField();
        txtTongDV = createField();

        modelDichVuDangDung = new DefaultTableModel(
                new String[]{"Dịch vụ", "Số lượng", "Thành tiền"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDichVuDangDung = new JTable(modelDichVuDangDung);
        tableDichVuDangDung.setRowHeight(28);

        JScrollPane spDV = new JScrollPane(tableDichVuDangDung);
        spDV.setPreferredSize(new Dimension(220, 120));

        form.add(new JLabel("Mã phòng"));
        form.add(txtMaPhong);

        form.add(new JLabel("Khách hàng"));
        form.add(txtTenKH);

        form.add(new JLabel("Trạng thái"));
        form.add(txtTrangThai);

        form.add(new JLabel("Dịch vụ đang dùng"));
        form.add(spDV);

        form.add(new JLabel("Tổng tiền DV"));
        form.add(txtTongDV);

        right.add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        Button btnThemDV = new Button();
        btnThemDV.setText("Thêm dịch vụ");

        Button btnSuaDV = new Button();
        btnSuaDV.setText("Sửa dịch vụ");

        Button btnXoaDV = new Button();
        btnXoaDV.setText("Xóa dịch vụ");

        Button btnRefresh = new Button();
        btnRefresh.setText("Làm mới");

        buttonPanel.add(btnThemDV);
        buttonPanel.add(btnSuaDV);
        buttonPanel.add(btnXoaDV);
        buttonPanel.add(btnRefresh);

        right.add(buttonPanel, BorderLayout.SOUTH);
        main.add(right);

        // ===== LOAD =====
        loadPhongFromDB();

        // ===== EVENTS =====
        tablePhong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadChiTietPhong();
            }
        });

        btnRefresh.addActionListener(e -> {
            loadPhongFromDB();
            clearForm();
        });

        btnThemDV.addActionListener(e -> moPopupThemDichVu());

        btnSuaDV.addActionListener(e -> suaDichVuDangDung());

        btnXoaDV.addActionListener(e -> xoaDichVuDangDung());
    }

    private void loadPhongFromDB() {
        modelPhong.setRowCount(0);

        try {
            List<Phong> list = phongDao.getAllPhong();

            for (Phong p : list) {
                if ("Đang thuê".equals(p.getTrangThai()) ||
                        "Đã đặt".equals(p.getTrangThai())) {

                    modelPhong.addRow(new Object[]{
                            p.getMaPhong(),
                            p.getTenKhach() != null ? p.getTenKhach() : "",
                            p.getTrangThai()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChiTietPhong() {
        int row = tablePhong.getSelectedRow();
        if (row == -1) return;

        String maPhong = modelPhong.getValueAt(row, 0).toString();

        txtMaPhong.setText(maPhong);
        txtTenKH.setText(modelPhong.getValueAt(row, 1).toString());
        txtTrangThai.setText(modelPhong.getValueAt(row, 2).toString());

        loadDichVuDangDung(maPhong);

        txtTongDV.setText(
                String.valueOf(
                        hoaDonDichVuDao.getTongTienDV(maPhong)
                )
        );
    }

    private void moPopupThemDichVu() {
        int rowPhong = tablePhong.getSelectedRow();

        if (rowPhong == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng trước!"
            );
            return;
        }

        String maPhong = modelPhong.getValueAt(rowPhong, 0).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Chọn dịch vụ");
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel modelPopup = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablePopup = new JTable(modelPopup);
        tablePopup.setRowHeight(30);

        List<DichVu> list = dichVuDao.getDichVuCon();
        for (DichVu dv : list) {
            modelPopup.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getDonGia()
            });
        }

        dialog.add(new JScrollPane(tablePopup), BorderLayout.CENTER);

        JPanel south = new JPanel();
        JTextField txtSL = new JTextField(10);
        JButton btnXacNhan = new JButton("Thêm dịch vụ");

        south.add(new JLabel("Số lượng:"));
        south.add(txtSL);
        south.add(btnXacNhan);

        dialog.add(south, BorderLayout.SOUTH);

        btnXacNhan.addActionListener(ev -> {
            int rowDV = tablePopup.getSelectedRow();

            if (rowDV == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dịch vụ!");
                return;
            }

            int soLuong;
            try {
                soLuong = Integer.parseInt(txtSL.getText().trim());
                if (soLuong <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng phải > 0");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng không hợp lệ!");
                return;
            }

            String maDV = modelPopup.getValueAt(rowDV, 0).toString();
            double donGia = Double.parseDouble(
                    modelPopup.getValueAt(rowDV, 2).toString()
            );

            String maHDDV =
                    hoaDonDichVuDao
                            .getOrCreateHoaDonDVByMaPhong(maPhong);

            if (maHDDV == null) {
                JOptionPane.showMessageDialog(dialog, "Phòng chưa có hóa đơn dịch vụ!");
                return;
            }

            boolean ok = hoaDonDichVuDao.addDichVuPhong(
                    maHDDV,
                    maDV,
                    soLuong,
                    donGia
            );

            if (ok) {
                JOptionPane.showMessageDialog(dialog, "Thêm dịch vụ thành công!");
                loadChiTietPhong();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm dịch vụ thất bại!");
            }
        });

        dialog.setVisible(true);
    }

    private void loadDichVuDangDung(String maPhong) {
        modelDichVuDangDung.setRowCount(0);

        try {
            java.util.List<Object[]> list = hoaDonDichVuDao.getDanhSachDichVuDangDung(maPhong);

            for (Object[] row : list) {
                modelDichVuDangDung.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void suaDichVuDangDung() {
        int rowPhong = tablePhong.getSelectedRow();
        int rowDV = tableDichVuDangDung.getSelectedRow();

        if (rowPhong == -1 || rowDV == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng và dịch vụ cần sửa!");
            return;
        }

        String maPhong = txtMaPhong.getText();
        String tenDV = modelDichVuDangDung.getValueAt(rowDV, 0).toString();

        String slMoi = JOptionPane.showInputDialog(this, "Nhập số lượng mới:");
        if (slMoi == null || slMoi.trim().isEmpty()) return;

        try {
            int soLuongMoi = Integer.parseInt(slMoi.trim());
            if (soLuongMoi <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0");
                return;
            }

            boolean ok = hoaDonDichVuDao.updateSoLuongDichVu(maPhong, tenDV, soLuongMoi);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadChiTietPhong();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
        }
    }

    private void xoaDichVuDangDung() {
        int rowDV = tableDichVuDangDung.getSelectedRow();

        if (rowDV == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!");
            return;
        }

        String maPhong = txtMaPhong.getText();
        String tenDV = modelDichVuDangDung.getValueAt(rowDV, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa dịch vụ này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = hoaDonDichVuDao.deleteDichVuDangDung(maPhong, tenDV);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
            loadChiTietPhong();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa dịch vụ thất bại!");
        }
    }

    private void clearForm() {
        txtMaPhong.setText("");
        txtTenKH.setText("");
        txtTrangThai.setText("");
        txtTongDV.setText("");
        modelDichVuDangDung.setRowCount(0);
    }

    private TextField createField() {
        TextField tf = new TextField();
        tf.setEditable(false);
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setBackground(new Color(245, 245, 245));
        return tf;
    }
}
