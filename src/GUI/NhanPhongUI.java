package GUI;

import Dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NhanPhongUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtSearch;
    private JTextField txtTen, txtSDT, txtMa, txtSoNguoi, txtNhan, txtTra;
    private JComboBox<String> cbLoai;

    private PhongDao phongDao = new PhongDao();
    private KhachHangDao khDao = new KhachHangDao();
    private HoaDonPhongDao hdDao = new HoaDonPhongDao();
    private ChiTietHoaDonPhongDao ctDao = new ChiTietHoaDonPhongDao();

    // lấy mã nhân viên đăng nhập
    private String maNV;

    public NhanPhongUI(String maNV) {

        this.maNV = maNV;

        setLayout(new BorderLayout());
        setBackground(new Color(235, 245, 255));

        // ===== TOP =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(220, 240, 255));

        JLabel title = new JLabel("NHẬN PHÒNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(220, 240, 255));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setBorder(
                BorderFactory.createTitledBorder("Tìm khách hàng")
        );

        JButton btnSearch = new JButton("🔍");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        top.add(title, BorderLayout.NORTH);
        top.add(searchPanel, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{
                        "Mã phòng",
                        "Loại phòng",
                        "Tên KH",
                        "SĐT"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(235, 245, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTen = new JTextField();
        txtSDT = new JTextField();
        txtMa = new JTextField();
        txtSoNguoi = new JTextField();
        txtNhan = new JTextField();
        txtTra = new JTextField();

        cbLoai = new JComboBox<>(
                new String[]{"Phòng đơn", "Phòng đôi", "VIP"}
        );

        Dimension size = new Dimension(250, 35);

        JTextField[] fields = {
                txtTen, txtSDT, txtMa,
                txtSoNguoi, txtNhan, txtTra
        };

        for (JTextField f : fields) {
            f.setPreferredSize(size);
        }

        cbLoai.setPreferredSize(size);

        // khóa field
        txtMa.setEditable(false);
        txtNhan.setEditable(false);
        txtTra.setEditable(false);
        cbLoai.setEnabled(false);

        // ===== FORM UI =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Họ tên"), gbc);

        gbc.gridx = 1;
        form.add(txtTen, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("SĐT"), gbc);

        gbc.gridx = 3;
        form.add(txtSDT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Mã phòng"), gbc);

        gbc.gridx = 1;
        form.add(txtMa, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Loại phòng"), gbc);

        gbc.gridx = 3;
        form.add(cbLoai, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Số người"), gbc);

        gbc.gridx = 1;
        form.add(txtSoNguoi, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Giờ nhận"), gbc);

        gbc.gridx = 3;
        form.add(txtNhan, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        form.add(new JLabel("Giờ trả"), gbc);

        gbc.gridx = 3;
        form.add(txtTra, gbc);

        JPanel btnPanel = new JPanel();

        JButton btnOK = new JButton("Xác nhận");
        JButton btnCancel = new JButton("Hủy");

        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;

        form.add(btnPanel, gbc);

        add(form, BorderLayout.SOUTH);

        // ===== LOAD DATA =====
        loadPhongDat();

        // ===== CLICK TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                String maPhong =
                        model.getValueAt(row, 0).toString();

                txtMa.setText(maPhong);

                cbLoai.setSelectedItem(
                        model.getValueAt(row, 1).toString()
                );

                txtTen.setText(
                        model.getValueAt(row, 2).toString()
                );

                txtSDT.setText(
                        model.getValueAt(row, 3).toString()
                );

                Object[] info =
                        new PhieuDatPhongDao()
                                .getThongTinNhanPhong(maPhong);

                if (info != null) {

                    txtNhan.setText(
                            info[0] != null
                                    ? info[0].toString()
                                    : ""
                    );

                    txtTra.setText(
                            info[1] != null
                                    ? info[1].toString()
                                    : ""
                    );
                }
            }
        });

        // ===== VALIDATE SĐT =====
        txtSDT.addKeyListener(
                new java.awt.event.KeyAdapter() {
                    public void keyTyped(
                            java.awt.event.KeyEvent evt
                    ) {
                        if (!Character.isDigit(
                                evt.getKeyChar()
                        )) {
                            evt.consume();
                        }
                    }
                }
        );

        // ===== BUTTON =====
        btnOK.addActionListener(e -> handleNhanPhong());
        btnCancel.addActionListener(e -> clearForm());
    }

    // =================================================
    // LOAD DANH SÁCH PHÒNG ĐÃ ĐẶT
    // =================================================

    private void loadPhongDat() {

        model.setRowCount(0);

        List<Object[]> list =
                new PhieuDatPhongDao()
                        .getAllDatPhong();

        for (Object[] row : list) {

            model.addRow(new Object[]{
                    row[0],
                    row[1],
                    row[2],
                    row[3]
            });
        }
    }

    // =================================================
    // VALIDATE
    // =================================================

    private boolean validateForm() {

        if (txtMa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Chưa chọn phòng!"
            );
            return false;
        }

        if (txtTen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nhập tên!"
            );
            return false;
        }

        if (!txtSDT.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "SĐT sai!"
            );
            return false;
        }

        try {
            int soNguoi =
                    Integer.parseInt(
                            txtSoNguoi.getText()
                    );

            if (soNguoi <= 0) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Số người sai!"
            );

            return false;
        }

        return true;
    }

    // =================================================
    // CLEAR FORM
    // =================================================

    private void clearForm() {

        txtTen.setText("");
        txtSDT.setText("");
        txtMa.setText("");
        txtSoNguoi.setText("");
        txtNhan.setText("");
        txtTra.setText("");
    }

    // =================================================
    // NHẬN PHÒNG
    // =================================================

    private void handleNhanPhong() {

        if (!validateForm()) return;

        try {

            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String maPhong = txtMa.getText().trim();

            // kiểm tra khách
            String maKH =
                    khDao.getMaKHBySDT(sdt);

            if (maKH == null) {
                maKH = khDao.insertKhach(
                        ten,
                        sdt
                );
            }

            if (maKH == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tạo được khách hàng!"
                );
                return;
            }

            // lấy mã phiếu đặt phòng
            String maPhieuDatPhong =
                    new PhieuDatPhongDao()
                            .getMaPhieuDatPhongByMaPhong(
                                    maPhong
                            );

            // tạo hóa đơn bằng mã NV đăng nhập
            String maHD =
                    hdDao.createHoaDon(
                            maKH,
                            maPhieuDatPhong,
                            maNV
                    );

            if (maHD == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tạo được hóa đơn!"
                );
                return;
            }

            // thêm chi tiết hóa đơn
            boolean ok =
                    ctDao.addChiTiet(
                            maHD,
                            maPhong
                    );

            if (!ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tạo được chi tiết hóa đơn!"
                );
                return;
            }

            // cập nhật trạng thái phòng
            phongDao.updateTrangThai(
                    maPhong,
                    "Đang thuê"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Nhận phòng thành công!"
            );

            loadPhongDat();
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi nhận phòng!"
            );
        }
    }
}