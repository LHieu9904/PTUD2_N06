/*package GUI;

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
}*/
package GUI;

import Dao.ChiTietHoaDonPhongDao;
import Dao.HoaDonPhongDao;
import Dao.KhachHangDao;
import Dao.PhieuDatPhongDao;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThuePhong_UI extends JPanel {

    private TextField txtTenKH;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtThoiGianNhan;

    private JComboBox<String> cbGioiTinh;
    private JComboBox<String> cbLoaiPhong;

    // Ngày trả + giờ trả
    private JSpinner spNgayTra;
    private JSpinner spGioTra;

    private JTable tablePhong;
    private DefaultTableModel modelPhong;

    private final KhachHangDao khDao = new KhachHangDao();
    private final PhongDao phongDao = new PhongDao();
    private final HoaDonPhongDao hoaDonDao = new HoaDonPhongDao();
    private final ChiTietHoaDonPhongDao ctHoaDonDao = new ChiTietHoaDonPhongDao();
    private final PhieuDatPhongDao phieuDatPhongDao = new PhieuDatPhongDao();

    public ThuePhong_UI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("THUÊ PHÒNG");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0, 153, 255));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // =====================================================
        // BODY
        // =====================================================

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(10, 15, 10, 15));

        add(body, BorderLayout.CENTER);

        // =====================================================
        // KHỞI TẠO FIELD
        // =====================================================

        txtTenKH = createField();
        txtSDT = createField();
        txtCCCD = createField();

        txtThoiGianNhan = createField();
        txtThoiGianNhan.setEditable(false);
        txtThoiGianNhan.setText("Tự động khi thuê phòng");

        cbGioiTinh = new JComboBox<>(new String[]{
                "Nam",
                "Nữ"
        });

        cbLoaiPhong = new JComboBox<>(new String[]{
                "Tất cả",
                "Đơn",
                "Đôi",
                "VIP"
        });

        cbGioiTinh.setPreferredSize(new Dimension(250, 38));
        cbLoaiPhong.setPreferredSize(new Dimension(250, 38));

        // =====================================================
        // SPINNER NGÀY TRẢ
        // =====================================================

        SpinnerDateModel modelNgay = new SpinnerDateModel();
        spNgayTra = new JSpinner(modelNgay);

        JSpinner.DateEditor editorNgay =
                new JSpinner.DateEditor(
                        spNgayTra,
                        "dd-MM-yyyy"
                );

        spNgayTra.setEditor(editorNgay);
        spNgayTra.setPreferredSize(
                new Dimension(250, 38)
        );

        // =====================================================
        // SPINNER GIỜ TRẢ
        // =====================================================

        SpinnerDateModel modelGio = new SpinnerDateModel();
        spGioTra = new JSpinner(modelGio);

        JSpinner.DateEditor editorGio =
                new JSpinner.DateEditor(
                        spGioTra,
                        "HH:mm"
                );

        spGioTra.setEditor(editorGio);
        spGioTra.setPreferredSize(
                new Dimension(250, 38)
        );

        // =====================================================
        // FORM TOP
        // =====================================================

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // DÒNG 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(new JLabel("Họ tên khách hàng"), gbc);

        gbc.gridx = 1;
        top.add(txtTenKH, gbc);

        gbc.gridx = 2;
        top.add(new JLabel("Số điện thoại"), gbc);

        gbc.gridx = 3;
        top.add(txtSDT, gbc);

        // DÒNG 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        top.add(new JLabel("CCCD"), gbc);

        gbc.gridx = 1;
        top.add(txtCCCD, gbc);

        gbc.gridx = 2;
        top.add(new JLabel("Giới tính"), gbc);

        gbc.gridx = 3;
        top.add(cbGioiTinh, gbc);

        // DÒNG 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        top.add(new JLabel("Thời gian nhận"), gbc);

        gbc.gridx = 1;
        top.add(txtThoiGianNhan, gbc);

        gbc.gridx = 2;
        top.add(new JLabel("Loại phòng"), gbc);

        gbc.gridx = 3;
        top.add(cbLoaiPhong, gbc);

        // DÒNG 4
        gbc.gridx = 0;
        gbc.gridy = 3;
        top.add(new JLabel("Ngày trả"), gbc);

        gbc.gridx = 1;
        top.add(spNgayTra, gbc);

        // DÒNG 5
        gbc.gridx = 0;
        gbc.gridy = 4;
        top.add(new JLabel("Giờ trả"), gbc);

        gbc.gridx = 1;
        top.add(spGioTra, gbc);

        body.add(top, BorderLayout.NORTH);

        // =====================================================
        // TABLE CENTER
        // =====================================================

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblTable = new JLabel("DANH SÁCH PHÒNG TRỐNG");
        lblTable.setHorizontalAlignment(SwingConstants.CENTER);
        lblTable.setFont(new Font("Tahoma", Font.BOLD, 16));

        center.add(lblTable, BorderLayout.NORTH);

        modelPhong = new DefaultTableModel(
                new String[]{
                        "Mã phòng",
                        "Loại phòng",
                        "Tầng",
                        "Trạng thái"
                }, 0
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

        // =====================================================
        // BUTTON
        // =====================================================

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);

        Button btnThuePhong = new Button();
        btnThuePhong.setText("Thuê phòng");

        Button btnLamMoi = new Button();
        btnLamMoi.setText("Làm mới");

        bottom.add(btnThuePhong);
        bottom.add(btnLamMoi);

        body.add(bottom, BorderLayout.SOUTH);

        // =====================================================
        // EVENT
        // =====================================================

        cbLoaiPhong.addActionListener(e -> {
            String loai =
                    cbLoaiPhong
                            .getSelectedItem()
                            .toString();

            loadPhongTheoLoai(loai);
        });

        btnLamMoi.addActionListener(e -> clearForm());
        btnThuePhong.addActionListener(e -> thuePhong());

        loadPhongTheoLoai("Tất cả");
    }

    // =====================================================
    // LOAD PHÒNG
    // =====================================================

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

    // =====================================================
    // THUÊ PHÒNG
    // =====================================================

    private void thuePhong() {

        int row = tablePhong.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng!"
            );
            return;
        }

        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSDT.getText().trim();
        String cccd = txtCCCD.getText().trim();

        if (tenKH.isEmpty() || sdt.isEmpty() || cccd.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin khách hàng!"
            );
            return;
        }

        int gioiTinh =
                cbGioiTinh.getSelectedItem()
                        .toString()
                        .equals("Nam") ? 1 : 0;

        String maPhong =
                modelPhong.getValueAt(row, 0).toString();

        // =====================================================
        // KHÁCH HÀNG
        // =====================================================

        String maKH;

        KhachHang kh =
                khDao.timKhachHangTonTai(sdt, cccd);

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
                JOptionPane.showMessageDialog(
                        this,
                        "Không thể thêm khách hàng!"
                );
                return;
            }
        }

        // =====================================================
        // TẠO PHIẾU ĐẶT PHÒNG GIẢ
        // =====================================================

        String maPDP =
                phieuDatPhongDao.taoPhieuDatPhongTuDong(
                        maKH,
                        "NV02"
                );

        if (maPDP == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tạo phiếu đặt phòng!"
            );
            return;
        }

        // =====================================================
        // TẠO HÓA ĐƠN PHÒNG
        // =====================================================

        String maHD =
                hoaDonDao.taoMaHoaDonTuDong();

        if (!hoaDonDao.insertCoPhieuDatPhong(
                maHD,
                "NV02",
                maKH,
                maPDP
        )) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tạo hóa đơn phòng!"
            );
            return;
        }
        // =====================================================
        // THỜI GIAN NHẬN
        // =====================================================
        Timestamp thoiGianNhan = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        txtThoiGianNhan.setText(sdf.format(thoiGianNhan));

        // =====================================================
        // THỜI GIAN TRẢ
        // =====================================================

        Date ngay = (Date) spNgayTra.getValue();
        Date gio = (Date) spGioTra.getValue();

        Calendar calNgay = Calendar.getInstance();
        calNgay.setTime(ngay);

        Calendar calGio = Calendar.getInstance();
        calGio.setTime(gio);
        calNgay.set(Calendar.HOUR_OF_DAY, calGio.get(Calendar.HOUR_OF_DAY));
        calNgay.set(Calendar.MINUTE, calGio.get(Calendar.MINUTE));
        calNgay.set(Calendar.SECOND, 0);
        Timestamp thoiGianTra = new Timestamp(calNgay.getTimeInMillis());

        if (thoiGianTra.before(thoiGianNhan)) {
            JOptionPane.showMessageDialog(this, "Thời gian trả phải lớn hơn thời gian hiện tại!");
            return;
        }


        if (!ctHoaDonDao.insert(maHD, maPhong, thoiGianNhan, thoiGianTra)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tạo chi tiết hóa đơn!"
            );
            return;
        }

        // =====================================================
        // CẬP NHẬT TRẠNG THÁI PHÒNG
        // =====================================================

        if (!phongDao.updateTrangThaiPhong(
                maPhong,
                "Đang thuê"
        )) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể cập nhật trạng thái phòng!"
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Thuê phòng thành công!"
        );
        clearForm();
        loadPhongTheoLoai(cbLoaiPhong.getSelectedItem().toString());
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

        txtTenKH.setText("");
        txtSDT.setText("");
        txtCCCD.setText("");
        txtThoiGianNhan.setText("Tự động khi thuê phòng");
        cbGioiTinh.setSelectedIndex(0);
        cbLoaiPhong.setSelectedIndex(0);

        spNgayTra.setValue(new Date());
        spGioTra.setValue(new Date());

        tablePhong.clearSelection();
    }

    private TextField createField() {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(250, 38));
        tf.setBackground(Color.WHITE);
        return tf;
    }
    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setTitle("Thuê phòng");
        f.setSize(1100, 700);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        f.add(new ThuePhong_UI());
        f.setVisible(true);
    }
}