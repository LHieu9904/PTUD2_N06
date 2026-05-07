package GUI;

import Dao.*;
import Entity.Phong;

import Raven.button.Button;
import Raven.combobox.Combobox;
import Raven.textfield.TextField;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class DatPhongUI extends JPanel {

    private JTextField tfHoTen, tfSDT, tfCCCD;
    private Combobox<String> cbbLoaiPhong;
    private JComboBox<String> cbGioiTinh;

    private TextField tfGioTu, tfGioDen, tfNgayTu, tfNgayDen, tfSoLuongNguoi;

    private JPanel availableRoomPanel = new JPanel();
    private JPanel bookedRoomPanel = new JPanel();

    // ===== LOGIC =====
    private String selectedMaPhong = null;
    private String maNV;

    public DatPhongUI(String maNV) {
        this.maNV = maNV;

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JLabel title = new JLabel("Đặt Phòng");
        title.setFont(new Font("Tahoma", Font.BOLD, 25));
        top.add(title);
        add(top, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(10,10));
        main.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(main, BorderLayout.CENTER);

        JPanel left = new JPanel(new BorderLayout());

        JLabel lbKH = new JLabel("Thông Tin Khách Hàng");
        lbKH.setFont(new Font("Tahoma", Font.BOLD, 20));

        JPanel topLeft = new JPanel();
        topLeft.setBackground(Color.WHITE);
        topLeft.add(lbKH);

        left.add(topLeft, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        tfHoTen = createText("Họ tên");
        form.add(wrap(tfHoTen));

        tfSDT = createText("Số điện thoại");
        form.add(wrap(tfSDT));

        JPanel pGT = new JPanel();
        pGT.setBackground(Color.WHITE);

        cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        cbGioiTinh.setPreferredSize(tfHoTen.getPreferredSize());
        pGT.add(cbGioiTinh);

        form.add(pGT);

        tfCCCD = createText("CCCD");
        form.add(wrap(tfCCCD));

        JPanel pLoai = new JPanel();
        pLoai.setBackground(Color.WHITE);

        cbbLoaiPhong = new Combobox<>();
        cbbLoaiPhong.setLabeText("Loại phòng");
        for(String lp : new PhongDao().getAllLoaiPhong()){
            cbbLoaiPhong.addItem(lp);
        }

        cbbLoaiPhong.setPreferredSize(tfHoTen.getPreferredSize());
        pLoai.add(cbbLoaiPhong);
        form.add(pLoai);

        JPanel pBtn = new JPanel();
        pBtn.setBackground(Color.WHITE);

        Button btnDat = createBtn("Đặt", new Color(0,153,255));
        Button btnHuy = createBtn("Hủy", Color.LIGHT_GRAY);

        pBtn.add(btnDat);
        pBtn.add(btnHuy);

        form.add(pBtn);

        left.add(form, BorderLayout.CENTER);
        main.add(left, BorderLayout.WEST);

        // ===== CENTER =====
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);

        JLabel lbTime = new JLabel("Thông Tin Đặt Phòng");
        lbTime.setFont(new Font("Tahoma", Font.BOLD, 20));

        JPanel pTitle = new JPanel();
        pTitle.setBackground(Color.WHITE);
        pTitle.add(lbTime);

        center.add(pTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3,2));
        grid.setBackground(Color.WHITE);

        grid.add(rowTime("Từ:", true));
        grid.add(rowDate());

        grid.add(rowTime("Đến:", false));
        grid.add(rowDate());

        grid.add(rowSoNguoi());
        grid.add(rowSearch());

        center.add(grid, BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        // ===== RIGHT =====
        JPanel right = new JPanel(new GridLayout(2,1,0,5));
        right.setPreferredSize(new Dimension(300,0));

        right.add(createListPanel("Danh sách phòng trống", availableRoomPanel));
        right.add(createListPanel("Danh sách phòng đặt", bookedRoomPanel));

        main.add(right, BorderLayout.EAST);

        // ================= LOGIC =================

        btnDat.addActionListener(e -> handleDatPhong());
        btnHuy.addActionListener(e -> clearForm());

        loadDatPhong();
        loadPhongTrong();
    }

    // ================= LOGIC =================

    private void loadPhongTrong() {

        availableRoomPanel.removeAll();

        String loaiPhong = cbbLoaiPhong
                .getSelectedItem()
                .toString();

        List<Phong> list = new PhongDao()
                .getPhongTrongTheoLoai(loaiPhong);

        for (Phong p : list) {

            JPanel card = new JPanel();
            card.setPreferredSize(new Dimension(120, 60));
            card.setBorder(
                    BorderFactory.createLineBorder(Color.GRAY)
            );
            card.setBackground(Color.WHITE);

            JLabel lb = new JLabel(
                    p.getMaPhong(),
                    JLabel.CENTER
            );

            card.add(lb);
            card.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            card.addMouseListener(
                    new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(
                                java.awt.event.MouseEvent e
                        ) {

                            selectedMaPhong = p.getMaPhong();

                            for (Component c :
                                    availableRoomPanel.getComponents()) {
                                c.setBackground(Color.WHITE);
                            }

                            card.setBackground(Color.CYAN);
                        }
                    }
            );

            availableRoomPanel.add(card);
        }

        availableRoomPanel.revalidate();
        availableRoomPanel.repaint();

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không có phòng trống cho loại phòng này!"
            );
        }
    }
    private void clearForm() {

        tfHoTen.setText("");
        tfSDT.setText("");
        tfCCCD.setText("");
        tfSoLuongNguoi.setText("");

        tfGioTu.setText("");
        tfGioDen.setText("");
        tfNgayTu.setText("");
        tfNgayDen.setText("");

        cbGioiTinh.setSelectedIndex(0);

        if (cbbLoaiPhong.getItemCount() > 0) {
            cbbLoaiPhong.setSelectedIndex(0);
        }
        cbbLoaiPhong.addActionListener(e -> {
            loadPhongTrong();
        });

        selectedMaPhong = null;

        for (Component c : availableRoomPanel.getComponents()) {
            c.setBackground(null);
        }
    }

    private void handleDatPhong() {

        // ================= KIỂM TRA CHỌN PHÒNG =================
        if (selectedMaPhong == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng trước!"
            );
            return;
        }

        // ================= KIỂM TRA DỮ LIỆU =================
        if (tfHoTen.getText().trim().isEmpty()
                || tfSDT.getText().trim().isEmpty()
                || tfSoLuongNguoi.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin khách hàng!"
            );
            return;
        }

        try {

            KhachHangDao khDao = new KhachHangDao();
            PhieuDatPhongDao pdDao = new PhieuDatPhongDao();
            ChiTietPhieuDatPhongDao ctDao = new ChiTietPhieuDatPhongDao();
            PhongDao phongDao = new PhongDao();

            // =====================================================
            // 1. THÊM KHÁCH HÀNG
            // =====================================================

            String sdt = tfSDT.getText().trim();

            String maKH = khDao.getMaKHBySDT(sdt);

            if (maKH == null) {
                maKH = khDao.insertKhach(
                        tfHoTen.getText().trim(),
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

            // 2. TẠO PHIẾU ĐẶT PHÒNG


            String maPhieu = pdDao.insert(
                    maKH,
                    maNV
            );
            if(maPhieu == null){

                JOptionPane.showMessageDialog(
                        this,
                        "Không tạo được phiếu đặt!"
                );

                return;
            }

            // =====================================================
            // 3. THÊM CHI TIẾT ĐẶT PHÒNG
            // =====================================================

            int soNguoi = Integer.parseInt(
                    tfSoLuongNguoi.getText().trim()
            );

            String ngayTu = tfNgayTu.getText().trim();
            String gioTu = tfGioTu.getText().trim();

            String ngayDen = tfNgayDen.getText().trim();
            String gioDen = tfGioDen.getText().trim();

            if (ngayTu.isEmpty() || gioTu.isEmpty()
                    || ngayDen.isEmpty() || gioDen.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn đầy đủ thời gian nhận và trả phòng!"
                );
                return;
            }

            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm"
                    );

            LocalDateTime tu =
                    LocalDateTime.parse(
                            ngayTu + " " + gioTu,
                            formatter
                    );

            LocalDateTime den =
                    LocalDateTime.parse(
                            ngayDen + " " + gioDen,
                            formatter
                    );

            if (den.isBefore(tu)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Thời gian trả phải lớn hơn thời gian nhận!"
                );
                return;
            }

            boolean ok = ctDao.insert(
                    maPhieu,
                    selectedMaPhong,
                    tu,
                    den,
                    soNguoi
            );

            if (!ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đặt phòng thất bại!"
                );
                return;
            }

            // =====================================================
            // 4. CẬP NHẬT TRẠNG THÁI PHÒNG -> ĐÃ ĐẶT
            // (PHÒNG SẼ HIỆN MÀU ĐỎ)
            // =====================================================

            boolean updated =
                    phongDao.updateTrangThai(
                            selectedMaPhong,
                            "Đã đặt"
                    );

            if(!updated){

                JOptionPane.showMessageDialog(
                        this,
                        "Không cập nhật được trạng thái phòng!"
                );

                return;
            }
            // =====================================================
// 5. TẠO HÓA ĐƠN + HỎI DỊCH VỤ
// =====================================================

            HoaDonPhongDao hdDao = new HoaDonPhongDao();
            DichVuDao dvDao = new DichVuDao();

// tạo mã hóa đơn
            String maHD = hdDao.getNextMaHD();

// tạo hóa đơn phòng (gắn với phiếu)
            hdDao.insertCoPhieuDatPhong(
                    maHD,
                    maNV,
                    maKH,
                    maPhieu
            );

// tạo chi tiết hóa đơn phòng
            String maHDDV = dvDao.createHDDV(maHD);


// tạo hóa đơn dịch vụ


// ===== HỎI THÊM DV =====
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có muốn thêm dịch vụ không?",
                    "Thêm dịch vụ",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm == JOptionPane.YES_OPTION){
                new ChonDichVuDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        selectedMaPhong,
                        maHDDV
                ).setVisible(true);
            }

            // =====================================================
            // 5. THÔNG BÁO
            // =====================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Đặt phòng thành công!\n" +
                            "Phòng: " + selectedMaPhong + "\n" +
                            "Trạng thái: Đã đặt (Màu đỏ)"
            );

            // =====================================================
            // 6. RELOAD GIAO DIỆN
            // =====================================================

            loadDatPhong();
            loadPhongTrong();
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi đặt phòng: " + e.getMessage()
            );
        }
    }

    private void loadDatPhong() {

        bookedRoomPanel.removeAll();

        List<Object[]> list =
                new PhieuDatPhongDao().getAllDatPhong();

        for (Object[] o : list) {

            JPanel p = new JPanel();

            p.add(new JLabel(
                    o[0] + " - " + o[2] + " - " + o[3]
            ));

            bookedRoomPanel.add(p);
        }

        bookedRoomPanel.revalidate();
        bookedRoomPanel.repaint();
    }

    // ===== UI (GIỮ NGUYÊN) =====

    private JTextField createText(String title){
        JTextField tf = new JTextField();
        tf.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tf.setColumns(15);
        tf.setBorder(BorderFactory.createTitledBorder(title));
        return tf;
    }

    private JPanel wrap(Component c){
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.add(c);
        return p;
    }

    private Button createBtn(String text, Color color){
        Button b = new Button();
        b.setText(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Tahoma", Font.BOLD, 18));
        return b;
    }

    private JPanel rowTime(String text, boolean isFrom){
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Tahoma", Font.BOLD, 20));

        TextField tf = new TextField();
        tf.setColumns(10);

        JButton btn = new JButton("🕒");
        btn.addActionListener(e -> {

            JSpinner spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));

            int result = JOptionPane.showConfirmDialog(
                    this,
                    spinner,
                    "Chọn giờ",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if(result == JOptionPane.OK_OPTION){
                java.util.Date date = (java.util.Date) spinner.getValue();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                tf.setText(sdf.format(date));
            }
        });

        if(isFrom) tfGioTu = tf;
        else tfGioDen = tf;

        p.add(lb); p.add(tf); p.add(btn);
        return p;
    }

    private JPanel rowDate(){
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        TextField tf = new TextField();
        tf.setColumns(10);

        JButton btn = new JButton("📅");
        btn.addActionListener(e -> {

            JSpinner spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));

            int result = JOptionPane.showConfirmDialog(
                    this,
                    spinner,
                    "Chọn ngày",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if(result == JOptionPane.OK_OPTION){
                java.util.Date date = (java.util.Date) spinner.getValue();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                tf.setText(sdf.format(date));
            }
        });

        if(tfNgayTu == null) tfNgayTu = tf;
        else tfNgayDen = tf;

        p.add(tf); p.add(btn);
        return p;
    }

    private JPanel rowSoNguoi(){
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        JLabel lb = new JLabel("Số lượng người:");
        lb.setFont(new Font("Tahoma", Font.BOLD, 20));

        tfSoLuongNguoi = new TextField();
        tfSoLuongNguoi.setColumns(10);

        p.add(lb); p.add(tfSoLuongNguoi);
        return p;
    }

    private JPanel rowSearch(){
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        Button btn = createBtn("Tìm phòng trống", new Color(0,153,255));

        btn.addActionListener(e -> loadPhongTrong());

        p.add(btn);
        return p;
    }

    private JPanel createListPanel(String title, JPanel content){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lb = new JLabel(title);
        lb.setFont(new Font("Tahoma", Font.BOLD, 20));

        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        top.add(lb);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(content);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

}