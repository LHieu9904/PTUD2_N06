package GUI;

import Dao.ChiTietHoaDonPhongDao;
import Dao.HoaDonPhongDao;
import Dao.KhachHangDao;
import Dao.PhongDao;
import Entity.KhachHang;
import Entity.Phong;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class ThuePhong_UI extends JPanel {

    private TextField txtTenKH;
    private TextField txtSDT;
    private TextField txtCCCD;

    private JComboBox<String> cbGioiTinh;
    private JComboBox<String> cbLoaiPhong;

    private JTable tablePhong;
    private DefaultTableModel modelPhong;

    private final KhachHangDao khDao = new KhachHangDao();
    private final PhongDao phongDao = new PhongDao();
    private final HoaDonPhongDao hoaDonDao = new HoaDonPhongDao();
    private final ChiTietHoaDonPhongDao ctHoaDonDao = new ChiTietHoaDonPhongDao();

    public ThuePhong_UI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================= HEADER =================
        JPanel header = new JPanel();
        JLabel title = new JLabel("THUÊ PHÒNG");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0, 153, 255));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ================= BODY =================
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(body, BorderLayout.CENTER);

        // ================= TOP FORM =================
        JPanel top = new JPanel(new GridLayout(3, 4, 15, 12));
        top.setBackground(Color.WHITE);

        txtTenKH = createField();
        txtSDT = createField();
        txtCCCD = createField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});

        cbLoaiPhong = new JComboBox<>(new String[]{
                "Tất cả",
                "Đơn",
                "Đôi",
                "VIP"
        });

        top.add(new JLabel("Họ tên khách hàng"));
        top.add(txtTenKH);
        top.add(new JLabel("Số điện thoại"));
        top.add(txtSDT);

        top.add(new JLabel("CCCD"));
        top.add(txtCCCD);
        top.add(new JLabel("Giới tính"));
        top.add(cbGioiTinh);

        top.add(new JLabel("Loại phòng"));
        top.add(cbLoaiPhong);

        body.add(top, BorderLayout.NORTH);

        // ================= CENTER TABLE =================
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblTable = new JLabel("DANH SÁCH PHÒNG TRỐNG");
        lblTable.setHorizontalAlignment(SwingConstants.CENTER);
        lblTable.setFont(new Font("Tahoma", Font.BOLD, 16));
        center.add(lblTable, BorderLayout.NORTH);

        modelPhong = new DefaultTableModel(
                new String[]{"Mã phòng", "Loại phòng", "Tầng", "Trạng thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePhong = new JTable(modelPhong);
        tablePhong.setRowHeight(35);

        JScrollPane scroll = new JScrollPane(tablePhong);
        center.add(scroll, BorderLayout.CENTER);

        body.add(center, BorderLayout.CENTER);

        // ================= BUTTON =================
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);

        Button btnThuePhong = new Button();
        btnThuePhong.setText("Thuê phòng");

        Button btnLamMoi = new Button();
        btnLamMoi.setText("Làm mới");

        bottom.add(btnThuePhong);
        bottom.add(btnLamMoi);

        body.add(bottom, BorderLayout.SOUTH);

        // ================= EVENT =================
        cbLoaiPhong.addActionListener(e -> {
            String loai = cbLoaiPhong.getSelectedItem().toString();
            loadPhongTheoLoai(loai);
        });

        btnLamMoi.addActionListener(e -> clearForm());
        btnThuePhong.addActionListener(e -> thuePhong());

        // load mặc định
        loadPhongTheoLoai("Tất cả");
    }

    // ================= LOAD PHÒNG =================
    private void loadPhongTheoLoai(String loaiPhong) {

        modelPhong.setRowCount(0);

        List<Phong> list;

        if (loaiPhong.equals("Tất cả")) {
            list = phongDao.getPhongTrongChoThue();
        } else {
            list = phongDao.getPhongTrongTheoLoaiChoThue(loaiPhong);
        }

        for (Phong p : list) {
            modelPhong.addRow(new Object[]{
                    p.getMaPhong(),
                    p.getLoaiPhong().getTenLP(),
                    p.getTang(),
                    p.getTrangThai()
            });
        }
    }

    // ================= THUÊ PHÒNG =================
    private void thuePhong() {
        int row = tablePhong.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!");
            return;
        }

        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String cccd = txtCCCD.getText().trim();

        if (tenKH.isEmpty() || sdt.isEmpty() || cccd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khách hàng!");
            return;
        }

        int gioiTinh = cbGioiTinh.getSelectedItem().toString().equals("Nam") ? 1 : 0;
        String maPhong = modelPhong.getValueAt(row, 0).toString();

        String maKH;
        KhachHang kh = khDao.timKhachHangTonTai(sdt, cccd);

        if (kh != null) {
            maKH = kh.getMaKH();
        } else {
            maKH = khDao.getNextMaKH();

            KhachHang khMoi = new KhachHang();
            khMoi.setMaKH(maKH);
            khMoi.setHoTen(tenKH);
            khMoi.setSdt(sdt);
            khMoi.setCccd(cccd);
            khMoi.setGioiTinh(gioiTinh);

            if (!khDao.create(khMoi)) {
                JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng!");
                return;
            }
        }

        String maHD = hoaDonDao.taoMaHoaDonTuDong();

        if (!hoaDonDao.insert(maHD, "NV02", maKH)) {
            JOptionPane.showMessageDialog(this, "Không thể tạo hóa đơn phòng!");
            return;
        }

        if (!ctHoaDonDao.insert(
                maHD,
                maPhong,
                new Timestamp(System.currentTimeMillis())
        )) {
            JOptionPane.showMessageDialog(this, "Không thể tạo chi tiết hóa đơn!");
            return;
        }

        if (!phongDao.updateTrangThaiPhong(maPhong, "Đang thuê")) {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái phòng!");
            return;
        }

        JOptionPane.showMessageDialog(this, "Thuê phòng thành công!");

        clearForm();
        loadPhongTheoLoai(cbLoaiPhong.getSelectedItem().toString());
    }

    // ================= CLEAR =================
    private void clearForm() {
        txtTenKH.setText("");
        txtSDT.setText("");
        txtCCCD.setText("");
        cbGioiTinh.setSelectedIndex(0);
        tablePhong.clearSelection();
    }

    // ================= TEXT FIELD =================
    private TextField createField() {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setBackground(Color.WHITE);
        return tf;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setTitle("Thuê phòng");
        f.setSize(1100, 650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ThuePhong_UI());
        f.setVisible(true);
    }
}