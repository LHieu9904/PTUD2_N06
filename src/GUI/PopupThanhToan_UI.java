package GUI;

import Dao.*;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PopupThanhToan_UI extends JDialog {

    // ===== FIELD =====
    private TextField txtMaPhong, txtTenKhach, txtSDT;
    private TextField txtNhan, txtTra;
    private TextField txtTienPhong, txtTienDV, txtTienVAT, txtTong;

    private JComboBox<String> cbKM, cbThue;

    private JTable tableDV;
    private DefaultTableModel modelDV;

    private Button btnOK, btnCancel;

    // ===== DAO =====
    private KhuyenMaiDao kmDao = new KhuyenMaiDao();
    private ThueDao thueDao = new ThueDao();
    private PhongDao phongDao = new PhongDao();
    private HoaDonPhongDao hdDao = new HoaDonPhongDao();

    // ===== DATA =====
    private double tienPhong = 0;
    private double tienDV = 0;
    private String maNV;

    public PopupThanhToan_UI(JFrame parent, Object[] data, String maNV) {
        super(parent, true);

        setTitle("Thanh toán");
        setSize(1000, 650);
        setLocationRelativeTo(parent);
        this.maNV = maNV;

        initUI();
        setData(data);
        loadCombo();
        loadDichVu(data[0].toString());
        initEvent();
        tinhTien();

        setVisible(true);
    }

    // =====================================================
    // UI
    // =====================================================

    private void initUI(){

        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(Color.WHITE);

        // ===== TITLE =====
        JLabel title = new JLabel("THANH TOÁN TRẢ PHÒNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(10,0,10,0));
        add(title, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new BorderLayout(10,10));
        main.setBorder(new EmptyBorder(10,15,10,15));
        main.setBackground(Color.WHITE);
        add(main, BorderLayout.CENTER);

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,10,8,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaPhong = createField();
        txtTenKhach = createField();
        txtSDT = createField();
        txtNhan = createField();
        txtTra = createField();
        txtTienPhong = createField();
        txtTienDV = createField();
        txtTienVAT = createField();
        txtTong = createField();

        cbKM = new JComboBox<>();
        cbThue = new JComboBox<>();

        int y = 0;

        addRow(form, gbc, y++, "Mã phòng", txtMaPhong, "Khách", txtTenKhach);
        addRow(form, gbc, y++, "SĐT", txtSDT, "Giờ nhận", txtNhan);
        addRow(form, gbc, y++, "Giờ trả", txtTra, "Tiền phòng", txtTienPhong);
        addRow(form, gbc, y++, "Tiền DV", txtTienDV, "Khuyến mãi", cbKM);
        addRow(form, gbc, y++, "Thuế", cbThue, "VAT", txtTienVAT);
        addRow(form, gbc, y++, "Tổng tiền", txtTong, "", new JLabel());

        main.add(form, BorderLayout.NORTH);

        // ===== TABLE DV =====
        modelDV = new DefaultTableModel(
                new String[]{"Tên DV","SL","Giá","Thành tiền"},0
        );

        tableDV = new JTable(modelDV);
        tableDV.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(tableDV);
        scroll.setBorder(BorderFactory.createTitledBorder("Dịch vụ đã dùng"));

        main.add(scroll, BorderLayout.CENTER);

        // ===== BUTTON =====
        JPanel bottom = new JPanel();
        btnOK = new Button();
        btnOK.setText("Thanh toán");

        btnCancel = new Button();
        btnCancel.setText("Hủy");

        bottom.add(btnOK);
        bottom.add(btnCancel);

        add(bottom, BorderLayout.SOUTH);
    }

    // =====================================================
    // ADD ROW
    // =====================================================

    private void addRow(JPanel panel, GridBagConstraints gbc, int y,
                        String label1, Component c1,
                        String label2, Component c2){

        gbc.gridy = y;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(new JLabel(label1), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(c1, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        panel.add(new JLabel(label2), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        panel.add(c2, gbc);
    }

    // =====================================================
    // SET DATA
    // =====================================================

    private void setData(Object[] d){

        txtMaPhong.setText(str(d[0]));
        txtTenKhach.setText(str(d[1]));
        txtSDT.setText(str(d[2]));
        txtNhan.setText(str(d[3]));
        txtTra.setText(str(d[4]));

        tienDV = d[6] != null ? Double.parseDouble(d[6].toString()) : 0;
        txtTienDV.setText(format(tienDV));

        tienPhong = tinhTienPhong(d);
        txtTienPhong.setText(format(tienPhong));
    }
    // =====================================================
// TÍNH TIỀN PHÒNG (CHUẨN THEO NGÀY + GIỜ LẺ)
// =====================================================

    private double tinhTienPhong(Object[] d) {

        try {

            if (d[3] == null || d[4] == null) return 0;

            // ===== TIME =====
            LocalDateTime nhan =
                    ((java.sql.Timestamp) d[3]).toLocalDateTime();

            LocalDateTime tra =
                    ((java.sql.Timestamp) d[4]).toLocalDateTime();

            // ===== TÍNH PHÚT (KHÔNG TÍNH GIÂY) =====
            long totalMinutes =
                    Duration.between(nhan, tra).toMinutes();

            // ===== ĐỔI SANG GIỜ (LÀM TRÒN) =====
            long totalHours = totalMinutes / 60;
            if (totalMinutes % 60 != 0) totalHours++;

            // ===== LẤY GIÁ =====
            String maPhong = d[0].toString();

            Object[] gia = phongDao.getGiaPhong(maPhong);
            double giaGioDau = (double) gia[0];
            double giaGioTiep = (double) gia[1];
            double giaNgay = (double) gia[2];

            double tien = 0;

            // ===== TÍNH NGÀY =====
            long soNgay = totalHours / 24;
            long duGio = totalHours % 24;

            tien += soNgay * giaNgay;

            // ===== TÍNH 12H =====
            long soBlock12h = duGio / 12;
            long gioLe = duGio % 12;

            tien += soBlock12h * (giaNgay / 2);

            // ===== GIỜ LẺ =====
            if (gioLe > 0) {

                // giờ đầu
                tien += giaGioDau;

                // giờ tiếp
                if (gioLe > 1) {
                    tien += (gioLe - 1) * giaGioTiep;
                }
            }

            return tien;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // =====================================================
    // TÍNH TIỀN PHÒNG
    // =====================================================



    // =====================================================
    // LOAD
    // =====================================================

    private void loadCombo(){

        cbKM.removeAllItems();
        cbKM.addItem("Không áp dụng");

        List<String> listKM = kmDao.getAllTenKhuyenMai();

        for(String s : listKM){
            cbKM.addItem(s);
        }

        cbThue.removeAllItems();

        List<String> listThue = thueDao.getAllTenThue();

        for(String s : listThue){
            cbThue.addItem(s);
        }
    }

    private void loadDichVu(String maPhong){

        modelDV.setRowCount(0);

        List<Object[]> list = phongDao.getDichVuDaSuDung(maPhong);

        for(Object[] r: list){
            modelDV.addRow(r);
        }
    }

    // =====================================================
    // TÍNH TIỀN
    // =====================================================

    private void tinhTien(){

        double tong = tienPhong + tienDV;

        double giam = kmDao.getPhanTramGiamGia(
                cbKM.getSelectedItem().toString()
        );

        tong -= tong * giam / 100;

        double vat = thueDao.getPhanTramThue(
                cbThue.getSelectedItem().toString()
        );

        double tienVAT = tong * vat / 100;

        txtTienVAT.setText(format(tienVAT));
        txtTong.setText(format(tong + tienVAT));
    }

    // =====================================================
    // EVENT
    // =====================================================

    private void initEvent(){

        cbKM.addActionListener(e -> tinhTien());
        cbThue.addActionListener(e -> tinhTien());

        btnCancel.addActionListener(e -> dispose());

        btnOK.addActionListener(e -> {

            String maPhong = txtMaPhong.getText();

            boolean ok = hdDao.thanhToanHoaDon(
                    maPhong,
                    kmDao.getMaKhuyenMaiByTen(cbKM.getSelectedItem().toString()),
                    thueDao.getMaThueByTen(cbThue.getSelectedItem().toString()),
                    Double.parseDouble(txtTienVAT.getText().replace(",","")),
                    Double.parseDouble(txtTong.getText().replace(",",""))
            );

            if(ok){

                phongDao.updateTrangThaiPhong(maPhong,"Đang dọn dẹp");

                JOptionPane.showMessageDialog(this,"Thanh toán thành công!");

                String maHD = hdDao.getMaHDByPhong(maPhong);
                String tenKH = txtTenKhach.getText();

                double tienPhongVal = Double.parseDouble(txtTienPhong.getText().replace(",",""));
                double tienDVVal = Double.parseDouble(txtTienDV.getText().replace(",",""));
                double giam = kmDao.getPhanTramGiamGia(cbKM.getSelectedItem().toString());

                // 🔥 TRUYỀN maNV Ở ĐÂY
                new GUI.HoaDon_UI(
                        maHD,
                        tenKH,
                        maNV,
                        maPhong,
                        phongDao.getDichVuDaSuDung(maPhong),
                        java.math.BigDecimal.valueOf(tienPhongVal),
                        java.math.BigDecimal.valueOf(tienDVVal),
                        java.math.BigDecimal.valueOf(giam)
                ).setVisible(true);

                dispose();
            }else{
                JOptionPane.showMessageDialog(this,"Thanh toán thất bại!");
            }
        });
    }

    // =====================================================
    // SUPPORT
    // =====================================================

    private TextField createField(){
        TextField t = new TextField();
        t.setEditable(false);
        t.setBackground(new Color(245,245,245));
        return t;
    }

    private String str(Object o){
        return o==null?"":o.toString();
    }

    private String format(double d){
        return String.format("%,.0f",d);
    }
}