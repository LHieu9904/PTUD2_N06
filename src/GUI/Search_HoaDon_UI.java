package GUI;

import Dao.HoaDonPhongDao;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Search_HoaDon_UI extends JPanel {

    // =========================
    // TABLE
    // =========================

    private DefaultTableModel model;
    private JTable table;

    // =========================
    // SEARCH
    // =========================

    private JDateChooser dcNgay;
    private TextField tfMa;

    private JRadioButton rHoaDon;
    private JRadioButton rPhieu;

    private Button btnSearch;
    private Button btnSearchNgay;

    // =========================
    // DETAIL
    // =========================

    private TextField txtMaHD;
    private TextField txtNgayLap;
    private TextField txtNhanVien;
    private TextField txtKhachHang;
    private TextField txtTienPhong;
    private TextField txtTienDV;
    private TextField txtTienThue;
    private TextField txtTongTien;

    private JTextArea taPhong;
    private JTextArea taDichVu;

    // =========================
    // DAO
    // =========================

    private final HoaDonPhongDao hoaDonDao =
            new HoaDonPhongDao();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Search_HoaDon_UI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initUI();
        loadData();
        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            loadChiTietHoaDon(model.getValueAt(0, 0).toString());
        }
        initEvent();
    }

    // =====================================================
    // UI
    // =====================================================

    private void initUI() {

        // HEADER

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("TRA CỨU HÓA ĐƠN");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setForeground(new Color(0, 153, 255));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // BODY

        JPanel body = new JPanel(new BorderLayout(0, 10));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(body, BorderLayout.CENTER);

        // =================================================
        // SEARCH PANEL
        // =================================================

        JPanel search = new JPanel();
        search.setBackground(Color.WHITE);
        search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));

        // tìm theo ngày

        search.add(new JLabel("Ngày: "));

        dcNgay = new JDateChooser();

        dcNgay.setDateFormatString("dd/MM/yyyy");
        dcNgay.setPreferredSize(new Dimension(180, 40));

        btnSearchNgay = new Button();
        btnSearchNgay.setText("Tìm ngày");

        search.add(dcNgay);
        search.add(Box.createHorizontalStrut(10));
        search.add(btnSearchNgay);

        search.add(Box.createHorizontalStrut(30));

        // tìm theo mã

        rHoaDon = new JRadioButton("Hóa đơn", true);
        rPhieu = new JRadioButton("Phiếu đặt");

        rHoaDon.setBackground(Color.WHITE);
        rPhieu.setBackground(Color.WHITE);

        ButtonGroup group = new ButtonGroup();
        group.add(rHoaDon);
        group.add(rPhieu);

        tfMa = new TextField();
        tfMa.setPreferredSize(new Dimension(250, 40));
        tfMa.setHint("Nhập mã...");

        btnSearch = new Button();
        btnSearch.setText("Tìm");

        search.add(rHoaDon);
        search.add(rPhieu);
        rHoaDon.addActionListener(e -> loadData());
        rPhieu.addActionListener(e -> loadData());
        search.add(Box.createHorizontalStrut(10));
        search.add(tfMa);
        search.add(Box.createHorizontalStrut(10));
        search.add(btnSearch);

        body.add(search, BorderLayout.NORTH);

        // =================================================
        // MAIN
        // =================================================

        JPanel main = new JPanel(new GridLayout(1, 2, 10, 0));
        main.setBackground(Color.WHITE);

        body.add(main, BorderLayout.CENTER);

        // =================================================
        // LEFT - TABLE
        // =================================================

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(Color.WHITE);

        JLabel lblLeft = new JLabel("DANH SÁCH HÓA ĐƠN");
        lblLeft.setHorizontalAlignment(SwingConstants.CENTER);
        lblLeft.setFont(new Font("Tahoma", Font.BOLD, 16));

        left.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã HĐ",
                        "Khách hàng",
                        "Trạng thái",
                        "Tổng tiền"
                },
                0
        );

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false; // ❌ KHÔNG CHO SỬA
            }
        };
        table.setRowHeight(35);

        JScrollPane scroll =
                new JScrollPane(table);

        left.add(scroll, BorderLayout.CENTER);

        main.add(left);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // =================================================
        // RIGHT - DETAIL
        // =================================================

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);

        JLabel lblRight = new JLabel("CHI TIẾT HÓA ĐƠN");
        lblRight.setHorizontalAlignment(SwingConstants.CENTER);
        lblRight.setFont(new Font("Tahoma", Font.BOLD, 16));

        right.add(lblRight, BorderLayout.NORTH);

        JPanel form = new JPanel(
                new GridLayout(0, 2, 10, 10)
        );

        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtMaHD = createField();
        txtNgayLap = createField();
        txtNhanVien = createField();
        txtKhachHang = createField();
        txtTienPhong = createField();
        txtTienDV = createField();
        txtTienThue = createField();
        txtTongTien = createField();

        taPhong = new JTextArea(4, 20);
        taPhong.setEditable(false);

        taDichVu = new JTextArea(4, 20);
        taDichVu.setEditable(false);

        form.add(new JLabel("Mã hóa đơn"));
        form.add(txtMaHD);

        form.add(new JLabel("Ngày lập"));
        form.add(txtNgayLap);

        form.add(new JLabel("Nhân viên"));
        form.add(txtNhanVien);

        form.add(new JLabel("Khách hàng"));
        form.add(txtKhachHang);

        form.add(new JLabel("Phòng"));
        form.add(new JScrollPane(taPhong));

        form.add(new JLabel("Dịch vụ"));
        form.add(new JScrollPane(taDichVu));

        form.add(new JLabel("Tiền phòng"));
        form.add(txtTienPhong);

        form.add(new JLabel("Tiền dịch vụ"));
        form.add(txtTienDV);

        form.add(new JLabel("Tiền thuế"));
        form.add(txtTienThue);

        form.add(new JLabel("Tổng tiền"));
        form.add(txtTongTien);

        right.add(form, BorderLayout.CENTER);

        main.add(right);
    }

    // =====================================================
    // LOAD DATA
    // =====================================================

    private void loadData() {

        model.setRowCount(0);

        List<Object[]> list;

        if (rHoaDon.isSelected()) {
            list = hoaDonDao.getAllHoaDon();
        } else {
            list = hoaDonDao.getAllPhieuDat();
        }

        for (Object[] row : list) {
            model.addRow(row);
        }

        // AUTO SELECT DÒNG ĐẦU
        if (model.getRowCount() > 0) {

            table.setRowSelectionInterval(0, 0);

            String ma =
                    model.getValueAt(0, 0).toString();

            if (rHoaDon.isSelected()) {
                loadChiTietHoaDon(ma);
            } else {
                loadChiTietPhieuDat(ma);
            }
        }
    }
    private void loadChiTietPhieuDat(String maPhieu){

        Object[] ct = hoaDonDao.getChiTietPhieuDat(maPhieu);

        if(ct == null) return;

        txtMaHD.setText(ct[0].toString());
        txtNgayLap.setText(ct[1].toString());
        txtNhanVien.setText(ct[2].toString());
        txtKhachHang.setText(ct[3].toString());

        txtTienPhong.setText("0");
        txtTienDV.setText("0");
        txtTienThue.setText("0");
        txtTongTien.setText("0");

        taPhong.setText(
                hoaDonDao.getPhongPhieuDat(maPhieu)
        );

        taDichVu.setText(
                hoaDonDao.getDVPhieuDat(maPhieu)
        );
    }

    // =====================================================
    // EVENT
    // =====================================================

    private void initEvent() {

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                String ma = model.getValueAt(row, 0).toString();

                if (rHoaDon.isSelected()) {
                    loadChiTietHoaDon(ma);
                } else {
                    loadChiTietPhieuDat(ma); // 🔥 thêm
                }
            }
        });

        btnSearch.addActionListener(e -> {

            String keyword =
                    tfMa.getText().trim();

            if (keyword.isEmpty()) {
                loadData();
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {

                String ma =
                        model.getValueAt(i, 0).toString();

                if (ma.equalsIgnoreCase(keyword)) {

                    table.setRowSelectionInterval(i, i);

                    table.scrollRectToVisible(
                            table.getCellRect(i, 0, true)
                    );

                    loadChiTietHoaDon(ma);
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy hóa đơn!"
            );
        });

        btnSearchNgay.addActionListener(e -> {

            Date date = dcNgay.getDate();

            if (date == null) {

                loadData();
                return;
            }

            SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy");

            String ngay =
                    sdf.format(date);

            List<Object[]> list =
                    hoaDonDao.timHoaDonTheoNgay(ngay);

            model.setRowCount(0);

            for (Object[] row : list) {
                model.addRow(row);
            }

            model.fireTableDataChanged();

            if (model.getRowCount() > 0) {

                table.setRowSelectionInterval(0, 0);

                String ma =
                        model.getValueAt(0,0).toString();

                loadChiTietHoaDon(ma);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy hóa đơn!"
                );
            }
        });
    }

    // =====================================================
    // LOAD CHI TIẾT
    // =====================================================

    private void loadChiTietHoaDon(String maHD) {

        Object[] ct =
                hoaDonDao.getChiTietHoaDon(maHD);

        if (ct == null) return;

        txtMaHD.setText(
                ct[0] != null ? ct[0].toString() : ""
        );

        txtNgayLap.setText(
                ct[1] != null ? ct[1].toString() : ""
        );

        txtNhanVien.setText(
                ct[2] != null ? ct[2].toString() : ""
        );

        txtKhachHang.setText(
                ct[3] != null ? ct[3].toString() : ""
        );

        txtTienPhong.setText(
                ct[4] != null ? ct[4].toString() : "0"
        );

        txtTienDV.setText(
                ct[5] != null ? ct[5].toString() : "0"
        );

        txtTienThue.setText(
                ct[6] != null ? ct[6].toString() : "0"
        );

        txtTongTien.setText(
                ct[7] != null ? ct[7].toString() : "0"
        );

        taPhong.setText(
                hoaDonDao.getDanhSachPhong(maHD)
        );

        taDichVu.setText(
                hoaDonDao.getDanhSachDichVu(maHD)
        );
    }
    private TextField createField() {

        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));

        return tf;
    }
}