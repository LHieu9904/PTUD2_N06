package GUI;

import Dao.DichVuDao;
import Dao.HoaDonDichVuDao;
import Dao.PhongDao;
import Entity.DichVu;
import Entity.Phong;
import Raven.button.Button;
import Raven.textfield.TextField;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
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

    // ===== PALETTE MÀU DASHBOARD SANG TRỌNG =====
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);     // Xanh Cobalt
    private final Color COLOR_SUCCESS = new Color(22, 163, 74);     // Xanh lá Emerald
    private final Color COLOR_DANGER = new Color(220, 38, 38);      // Đỏ Crimson
    private final Color COLOR_BG_MAIN = new Color(243, 244, 246);   // Xám trắng Slate nhạt
    private final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);     // Đen sẫm nội dung
    private final Color COLOR_TEXT_MUTED = new Color(107, 114, 128);  // Xám phụ đề
    private final Color COLOR_BORDER = new Color(229, 231, 235);      // Viền mảnh tinh tế

    public DichVuPhong_UI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // ===== NORTH: HEADER TITLE =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Dịch Vụ Tại Phòng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel subtitle = new JLabel("Cấp phát thêm dịch vụ, theo dõi tiêu dùng và cập nhật chi phí cho các phòng lưu trú");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_MUTED);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // ===== CENTER: MAIN WORKSPACE =====
        JPanel main = new JPanel(new GridLayout(1, 2, 20, 0));
        main.setOpaque(false);
        add(main, BorderLayout.CENTER);

        // =====================================================
        // LEFT CARD: DANH SÁCH PHÒNG
        // =====================================================
        JPanel leftCard = createStyledCard();
        leftCard.setLayout(new BorderLayout(0, 14));

        JLabel lblPhong = new JLabel("Phòng Đang Thuê / Đã Đặt");
        lblPhong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPhong.setForeground(COLOR_PRIMARY);
        leftCard.add(lblPhong, BorderLayout.NORTH);

        modelPhong = new DefaultTableModel(
                new String[]{"Mã phòng", "Khách hàng", "Trạng thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhong = new JTable(modelPhong);
        styleTable(tablePhong);

        JScrollPane scrollPhong = new JScrollPane(tablePhong);
        scrollPhong.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        leftCard.add(scrollPhong, BorderLayout.CENTER);
        main.add(leftCard);

        // =====================================================
        // RIGHT CARD: CHI TIẾT QUẢN LÝ DỊCH VỤ
        // =====================================================
        JPanel rightCard = createStyledCard();
        rightCard.setLayout(new BorderLayout(0, 16));

        JLabel lblRight = new JLabel("Chi Tiết Tiêu Dùng & Thao Tác");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRight.setForeground(COLOR_PRIMARY);
        rightCard.add(lblRight, BorderLayout.NORTH);

        // Form bọc GridBagLayout giữ các ô nhập liệu cân đối, tránh méo mó
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(6, 0, 6, 0);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1.0;

        txtMaPhong = createField("Mã phòng");
        txtTenKH = createField("Khách hàng");
        txtTrangThai = createField("Trạng thái");
        txtTongDV = createField("Tổng tiền dịch vụ (VND)");
        txtTongDV.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtTongDV.setForeground(COLOR_DANGER);

        modelDichVuDangDung = new DefaultTableModel(
                new String[]{"Dịch vụ", "Số lượng", "Thành tiền"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDichVuDangDung = new JTable(modelDichVuDangDung);
        styleTable(tableDichVuDangDung);

        // Căn lề phải cho cột thành tiền và số lượng
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableDichVuDangDung.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableDichVuDangDung.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        JScrollPane spDV = new JScrollPane(tableDichVuDangDung);
        spDV.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        spDV.setPreferredSize(new Dimension(0, 140));

        // Đổ các thành phần vào form theo chiều dọc
        formPanel.add(wrapField(txtMaPhong), c);
        formPanel.add(wrapField(txtTenKH), c);
        formPanel.add(wrapField(txtTrangThai), c);

        c.weighty = 1.0;
        formPanel.add(createTableLabelWrapper("Bảng danh sách dịch vụ phòng đang dùng:", spDV), c);

        c.weighty = 0.0;
        formPanel.add(wrapField(txtTongDV), c);

        rightCard.add(formPanel, BorderLayout.CENTER);

        // Khối nút bấm bên dưới thẻ phải
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        buttonPanel.setOpaque(false);

        Button btnThemDV = createButton("Thêm Dịch Vụ", COLOR_SUCCESS);
        Button btnSuaDV = createButton("Sửa Số Lượng", COLOR_PRIMARY);
        Button btnXoaDV = createButton("Xóa Dịch Vụ", COLOR_DANGER);
        Button btnRefresh = createButton("Làm Mới", new Color(100, 116, 139));

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnXoaDV);
        buttonPanel.add(btnSuaDV);
        buttonPanel.add(btnThemDV);

        rightCard.add(buttonPanel, BorderLayout.SOUTH);
        main.add(rightCard);

        // ===== LOAD BAN ĐẦU =====
        loadPhongFromDB();

        // ===== BIỂU DIỄN SỰ KIỆN GỐC =====
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

    // =====================================================
    // POPUP CHỌN DỊCH VỤ (ĐÃ ĐƯỢC HIỆN ĐẠI HÓA UI)
    // =====================================================
    private void moPopupThemDichVu() {
        int rowPhong = tablePhong.getSelectedRow();

        if (rowPhong == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPhong = modelPhong.getValueAt(rowPhong, 0).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Kho Dịch Vụ Khả Dụng");
        dialog.setSize(680, 420);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        ((JPanel)dialog.getContentPane()).setBorder(new EmptyBorder(16, 16, 16, 16));
        dialog.setLayout(new BorderLayout(0, 16));

        DefaultTableModel modelPopup = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá (VND)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablePopup = new JTable(modelPopup);
        styleTable(tablePopup);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tablePopup.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        List<DichVu> list = dichVuDao.getDichVuCon();
        for (DichVu dv : list) {
            modelPopup.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getDonGia()
            });
        }

        JScrollPane spPopup = new JScrollPane(tablePopup);
        spPopup.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        dialog.add(spPopup, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputPanel.setOpaque(false);
        JLabel lblSL = new JLabel("Nhập số lượng cấp:");
        lblSL.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField txtSL = new JTextField("1", 8);
        txtSL.setPreferredSize(new Dimension(0, 36));
        txtSL.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        inputPanel.add(lblSL);
        inputPanel.add(txtSL);

        JButton btnXacNhan = new JButton("Xác Nhận Thêm");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(COLOR_SUCCESS);
        btnXacNhan.setPreferredSize(new Dimension(140, 36));
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        south.add(inputPanel, BorderLayout.CENTER);
        south.add(btnXacNhan, BorderLayout.EAST);
        dialog.add(south, BorderLayout.SOUTH);

        btnXacNhan.addActionListener(ev -> {
            int rowDV = tablePopup.getSelectedRow();

            if (rowDV == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dịch vụ từ bảng!");
                return;
            }

            int soLuong;
            try {
                soLuong = Integer.parseInt(txtSL.getText().trim());
                if (soLuong <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Số lượng cấp phát phải lớn hơn 0!");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng nhập vào không hợp lệ!");
                return;
            }

            String maDV = modelPopup.getValueAt(rowDV, 0).toString();
            double donGia = Double.parseDouble(modelPopup.getValueAt(rowDV, 2).toString());

            String maHDDV = hoaDonDichVuDao.getOrCreateHoaDonDVByMaPhong(maPhong);

            if (maHDDV == null) {
                JOptionPane.showMessageDialog(dialog, "Phòng chưa được liên kết hóa đơn dịch vụ!");
                return;
            }

            boolean ok = hoaDonDichVuDao.addDichVuPhong(maHDDV, maDV, soLuong, donGia);

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

    // =====================================================
    // LOGIC NGHIỆP VỤ NỘI BỘ (GIỮ NGUYÊN HOÀN TOÀN)
    // =====================================================
    private void loadPhongFromDB() {
        modelPhong.setRowCount(0);
        try {
            List<Phong> list = phongDao.getAllPhong();
            for (Phong p : list) {
                if ("Đang thuê".equals(p.getTrangThai()) || "Đã đặt".equals(p.getTrangThai())) {
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
        txtTongDV.setText(String.valueOf(hoaDonDichVuDao.getTongTienDV(maPhong)));
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

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dịch vụ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
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

    // =====================================================
    // DESIGN SYSTEM HELPERS (BỘ CẤU HÌNH GIAO DIỆN PHẲNG)
    // =====================================================
    private JPanel createStyledCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(36);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.setShowVerticalLines(false);
        table.setGridColor(COLOR_BORDER);
        table.setIntercellSpacing(new Dimension(0, 1));
    }

    private TextField createField(String titleLabel) {
        TextField tf = new TextField();
        tf.setEditable(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(0, 40));
        tf.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        tf.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                titleLabel, 0, 0, new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_MUTED
        ));
        return tf;
    }

    private JPanel wrapField(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTableLabelWrapper(String title, Component scroll) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private Button createButton(String text, Color color) {
        Button b = new Button();
        b.setText(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(0, 40));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        return b;
    }
}