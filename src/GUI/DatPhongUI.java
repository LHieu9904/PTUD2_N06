/*package GUI;

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
        if (!validateInput()) {
            return;
        }

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
            HoaDonPhongDao hdDao = new HoaDonPhongDao();
            DichVuDao dvDao = new DichVuDao();
            String maHD = hdDao.getNextMaHD();
            hdDao.insertCoPhieuDatPhong(
                    maHD,
                    maNV,
                    maKH,
                    maPhieu
            );
            String maHDDV = dvDao.createHDDV(maHD);
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
            JOptionPane.showMessageDialog(
                    this,
                    "Đặt phòng thành công!\n" +
                            "Phòng: " + selectedMaPhong + "\n" +
                            "Trạng thái: Đã đặt (Màu đỏ)"
            );
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
    private boolean validateInput() {

        String ten = tfHoTen.getText().trim();
        String sdt = tfSDT.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String soNguoi = tfSoLuongNguoi.getText().trim();

        // ===== RỖNG =====
        if (ten.isEmpty()
                || sdt.isEmpty()
                || cccd.isEmpty()
                || soNguoi.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin!"
            );

            return false;
        }

        // ===== HỌ TÊN =====
        if (!ten.matches(
                "^([A-ZÀ-Ỹ][a-zà-ỹ]+\\s?)+$"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tên không hợp lệ!"
            );

            return false;
        }

        // ===== SĐT =====
        if (!sdt.matches("^0\\d{9}$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "SĐT phải gồm 10 số và bắt đầu bằng 0!"
            );

            return false;
        }

        // ===== CCCD =====
        if (!cccd.matches("^\\d{12}$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "CCCD phải gồm đúng 12 số!"
            );

            return false;
        }

        // ===== SỐ NGƯỜI =====
        if (!soNguoi.matches("^\\d+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Số lượng người không hợp lệ!"
            );

            return false;
        }

        return true;
    }

}
*/
package GUI;

import Dao.*;
import Entity.Phong;

import Raven.button.Button;
import Raven.combobox.Combobox;
import Raven.textfield.TextField;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class DatPhongUI extends JPanel {

    private JTextField tfHoTen, tfSDT, tfCCCD;
    private Combobox<String> cbbLoaiPhong;
    private JComboBox<String> cbGioiTinh;

    private TextField tfGioTu, tfGioDen, tfNgayTu, tfNgayDen, tfSoLuongNguoi;

    private JPanel availableRoomPanel = new JPanel();
    private JPanel bookedRoomPanel = new JPanel();

    // ===== POPUP ĐỀ XUẤT SĐT =====
    private JPopupMenu suggestPopup;
    private JList<String> suggestList;
    private DefaultListModel<String> listModel;

    // ===== BỘ MÀU CHUẨN DOANH NGHIỆP =====
    private final Color COLOR_PRIMARY = new Color(63, 131, 248);
    private final Color COLOR_SUCCESS = new Color(14, 159, 110);
    private final Color COLOR_BG_MAIN = new Color(243, 244, 246);
    private final Color COLOR_TEXT_MAIN = new Color(17, 24, 39);
    private final Color COLOR_TEXT_MUTED = new Color(107, 114, 128);
    private final Color COLOR_BORDER = new Color(229, 231, 235);

    private String selectedMaPhong = null;
    private String maNV;

    public DatPhongUI(String maNV) {
        this.maNV = maNV;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Khởi tạo Popup trước khi tạo Form để tránh lỗi NullPointerException
        initSuggestionPopup();

        // ================= TOP HEADER BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("Quản Lý Đặt Phòng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel subtitle = new JLabel("Tạo phiếu đặt phòng mới và kiểm tra tình trạng phòng thời gian thực");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_MUTED);

        topBar.add(title, BorderLayout.NORTH);
        topBar.add(subtitle, BorderLayout.SOUTH);
        add(topBar, BorderLayout.NORTH);

        // ================= MAIN CONTENT LAYOUT =================
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // CỘT 1: THÔNG TIN KHÁCH HÀNG
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; gbc.weighty = 1.0;
        mainContent.add(createLeftCustomerCard(), gbc);

        // CỘT 2: THỜI GIAN & ĐẶT PHÒNG
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 20, 0, 20);
        mainContent.add(createCenterBookingCard(), gbc);

        // CỘT 3: TRẠNG THÁI DANH SÁCH PHÒNG
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.3; gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(createRightRoomStatusCard(), gbc);

        add(mainContent, BorderLayout.CENTER);

        // ===== CÁC SỰ KIỆN GỐC =====
        loadDatPhong();
        loadPhongTrong();
    }

    // ================= KHỞI TẠO POPUP GỢI Ý =================
    private void initSuggestionPopup() {
        suggestPopup = new JPopupMenu();
        suggestPopup.setFocusable(false);
        listModel = new DefaultListModel<>();
        suggestList = new JList<>(listModel);
        suggestList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        suggestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(suggestList);
        scrollPane.setBorder(null);
        suggestPopup.add(scrollPane);

        // Sự kiện click chuột chọn SĐT đề xuất
        suggestList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedValue = suggestList.getSelectedValue();
                if (selectedValue != null) {
                    String cleanSDT = selectedValue;
                    // Tách chuỗi lấy riêng SĐT nếu chuỗi có định dạng "SĐT - Tên"
                    if (selectedValue.contains(" - ")) {
                        cleanSDT = selectedValue.split(" - ")[0].trim();
                    }

                    // Điền SĐT sạch lên trường nhập và đóng popup ngay
                    tfSDT.setText(cleanSDT);
                    suggestPopup.setVisible(false);

                    // Đổ dữ liệu Họ Tên, CCCD, Giới tính tương ứng lên form
                    fillCustomerInfoBySDT(cleanSDT);
                }
            }
        });
    }

    // Hàm thực hiện Auto-fill dữ liệu từ Database lên các ô Text còn lại
    private void fillCustomerInfoBySDT(String sdt) {
        try {
            KhachHangDao khDao = new KhachHangDao();
            Object[] khInfo = khDao.getKhachHangBySDT(sdt);

            if (khInfo != null) {
                if (khInfo[1] != null) tfHoTen.setText(khInfo[1].toString()); // Đổ họ tên
                if (khInfo[2] != null) cbGioiTinh.setSelectedItem(khInfo[2].toString()); // Đổ giới tính ("Nam"/"Nữ")
                if (khInfo[3] != null) tfCCCD.setText(khInfo[3].toString()); // Đổ CCCD
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= THÔNG TIN KHÁCH HÀNG =================
    private JPanel createLeftCustomerCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout(0, 16));

        JLabel header = new JLabel("Thông Tin Khách Hàng");
        header.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.setForeground(COLOR_PRIMARY);
        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        body.add(Box.createVerticalGlue());

        tfHoTen = createText("Họ và tên khách hàng");
        body.add(wrap(tfHoTen));
        body.add(Box.createVerticalGlue());

        tfSDT = createText("Số điện thoại liên lạc");
        body.add(wrap(tfSDT));
        body.add(Box.createVerticalGlue());

        // LẮNG NGHE SỰ KIỆN GÕ PHÍM TRÊN Ô SĐT ĐỂ ĐỀ XUẤT THÔNG TIN KHÁCH HÀNG
        tfSDT.getDocument().addDocumentListener(new DocumentListener() {
            private void updateSuggest() {
                SwingUtilities.invokeLater(() -> {
                    String input = tfSDT.getText().trim();
                    // Khi xóa trắng hoặc gõ ít hơn 2 số thì ẩn popup đề xuất đi
                    if (input.length() < 2) {
                        suggestPopup.setVisible(false);
                        return;
                    }

                    try {
                        KhachHangDao khDao = new KhachHangDao();
                        List<String> listSDT = khDao.getDanhSachSDTGoiY(input);

                        listModel.clear();
                        if (listSDT != null && !listSDT.isEmpty()) {
                            for (String sdtGoiY : listSDT) {
                                listModel.addElement(sdtGoiY);
                            }
                            // Thiết lập kích thước popup rộng bằng ô nhập text sđt
                            suggestPopup.setPreferredSize(new Dimension(tfSDT.getWidth(), Math.min(130, listSDT.size() * 26 + 8)));
                            // Hiển thị ngay dưới chân ô nhập SĐT
                            if (tfSDT.isShowing()) {
                                suggestPopup.show(tfSDT, 0, tfSDT.getHeight());
                                tfSDT.requestFocus(); // Giữ Focus lại cho ô text để gõ tiếp liên tục
                            }
                        } else {
                            suggestPopup.setVisible(false);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            @Override public void insertUpdate(DocumentEvent e) { updateSuggest(); }
            @Override public void removeUpdate(DocumentEvent e) { updateSuggest(); }
            @Override public void changedUpdate(DocumentEvent e) { updateSuggest(); }
        });

        JPanel pGT = new JPanel(new BorderLayout(0, 4));
        pGT.setOpaque(false);
        pGT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        JLabel lbGT = new JLabel("Giới tính");
        lbGT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbGT.setForeground(COLOR_TEXT_MUTED);
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cbGioiTinh.setPreferredSize(new Dimension(0, 38));
        cbGioiTinh.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        pGT.add(lbGT, BorderLayout.NORTH);
        pGT.add(cbGioiTinh, BorderLayout.CENTER);
        body.add(pGT);
        body.add(Box.createVerticalGlue());

        tfCCCD = createText("Số Căn cước công dân (CCCD)");
        body.add(wrap(tfCCCD));
        body.add(Box.createVerticalGlue());

        JPanel pLoai = new JPanel(new BorderLayout());
        pLoai.setOpaque(false);
        pLoai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        cbbLoaiPhong = new Combobox<>();
        cbbLoaiPhong.setLabeText("Hạng phòng / Loại phòng");
        cbbLoaiPhong.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cbbLoaiPhong.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        for (String lp : new PhongDao().getAllLoaiPhong()) {
            cbbLoaiPhong.addItem(lp);
        }
        pLoai.add(cbbLoaiPhong, BorderLayout.CENTER);
        body.add(pLoai);

        body.add(Box.createVerticalGlue());

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // ================= THỜI GIAN CƯ TRÚ & KHỞI TẠO =================
    private JPanel createCenterBookingCard() {
        JPanel card = createStyledCard();
        card.setLayout(new BorderLayout(0, 16));

        JLabel header = new JLabel("Thời Gian Cư Trú & Khởi Tạo");
        header.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.setForeground(COLOR_PRIMARY);
        card.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);

        body.add(Box.createVerticalGlue());

        body.add(createActionTimeRow("Thời gian nhận phòng (Từ ngày/giờ):", true));
        body.add(Box.createVerticalGlue());

        body.add(createActionTimeRow("Thời gian trả phòng dự kiến (Đến ngày/giờ):", false));
        body.add(Box.createVerticalGlue());

        JPanel pSoNguoi = new JPanel(new BorderLayout(0, 6));
        pSoNguoi.setOpaque(false);
        pSoNguoi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        pSoNguoi.setPreferredSize(new Dimension(0, 68));

        JLabel lbSN = new JLabel("Số lượng thành viên cư trú:");
        lbSN.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbSN.setForeground(COLOR_TEXT_MUTED);

        tfSoLuongNguoi = new TextField();
        tfSoLuongNguoi.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tfSoLuongNguoi.setPreferredSize(new Dimension(0, 42));
        tfSoLuongNguoi.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        pSoNguoi.add(lbSN, BorderLayout.NORTH);
        pSoNguoi.add(tfSoLuongNguoi, BorderLayout.CENTER);
        body.add(pSoNguoi);

        body.add(Box.createVerticalGlue());

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        actionPanel.setOpaque(false);
        actionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        actionPanel.setPreferredSize(new Dimension(0, 42));

        Button btnTim = createBtn("Tìm Phòng", new Color(243, 244, 246));
        btnTim.setForeground(COLOR_TEXT_MAIN);
        btnTim.addActionListener(e -> loadPhongTrong());

        Button btnHuy = createBtn("Làm mới", new Color(254, 242, 242));
        btnHuy.setForeground(new Color(220, 38, 38));
        btnHuy.addActionListener(e -> clearForm());

        Button btnDat = createBtn("Xác Nhận Đặt", COLOR_SUCCESS);
        btnDat.addActionListener(e -> handleDatPhong());

        actionPanel.add(btnTim);
        actionPanel.add(btnHuy);
        actionPanel.add(btnDat);
        body.add(actionPanel);

        body.add(Box.createVerticalGlue());

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRightRoomStatusCard() {
        JPanel container = new JPanel(new GridLayout(2, 1, 0, 20));
        container.setOpaque(false);

        // BƯỚC QUAN TRỌNG: Khởi tạo panel với GridLayout(0, 5) ngay từ đầu
        availableRoomPanel = new JPanel(new GridLayout(0, 5, 5, 5));
        availableRoomPanel.setBackground(Color.WHITE);

        // Bọc vào ScrollPane
        JScrollPane scroll = new JScrollPane(availableRoomPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(280, 400)); // Cố định chiều rộng 280px

        container.add(createListPanel("Hạng Phòng Trống Khả Dụng", scroll));
        container.add(createListPanel("Sổ Nhật Ký Đặt Phòng Gần Đây", bookedRoomPanel));

        return container;
    }

    private JPanel createActionTimeRow(String rowTitle, boolean isFrom) {
        JPanel container = new JPanel(new BorderLayout(0, 6));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        container.setPreferredSize(new Dimension(0, 68));

        JLabel lbl = new JLabel(rowTitle);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT_MUTED);
        container.add(lbl, BorderLayout.NORTH);

        JPanel fieldsRow = new JPanel(new GridBagLayout());
        fieldsRow.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        TextField tfGio = new TextField();
        tfGio.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tfGio.setPreferredSize(new Dimension(0, 42));
        tfGio.setMinimumSize(new Dimension(0, 42));
        tfGio.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        c.gridx = 0; c.weightx = 0.35; c.gridy = 0; c.weighty = 1.0; c.insets = new Insets(0, 0, 0, 4);
        fieldsRow.add(tfGio, c);

        JButton btnGio = new JButton("🕒");
        btnGio.setPreferredSize(new Dimension(46, 42));
        btnGio.setMinimumSize(new Dimension(46, 42));
        btnGio.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnGio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGio.addActionListener(e -> {
            JSpinner spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
            if (JOptionPane.showConfirmDialog(this, spinner, "Chọn giờ", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                tfGio.setText(new java.text.SimpleDateFormat("HH:mm").format((java.util.Date) spinner.getValue()));
            }
        });
        c.gridx = 1; c.weightx = 0.0; c.gridy = 0; c.weighty = 1.0; c.insets = new Insets(0, 0, 0, 12);
        fieldsRow.add(btnGio, c);

        TextField tfNgay = new TextField();
        tfNgay.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tfNgay.setPreferredSize(new Dimension(0, 42));
        tfNgay.setMinimumSize(new Dimension(0, 42));
        tfNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        c.gridx = 2; c.weightx = 0.55; c.gridy = 0; c.weighty = 1.0; c.insets = new Insets(0, 0, 0, 4);
        fieldsRow.add(tfNgay, c);

        JButton btnNgay = new JButton("📅");
        btnNgay.setPreferredSize(new Dimension(46, 42));
        btnNgay.setMinimumSize(new Dimension(46, 42));
        btnNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnNgay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNgay.addActionListener(e -> {
            JSpinner spinner = new JSpinner(new SpinnerDateModel());
            spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
            if (JOptionPane.showConfirmDialog(this, spinner, "Chọn ngày", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                tfNgay.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format((java.util.Date) spinner.getValue()));
            }
        });
        c.gridx = 3; c.weightx = 0.0; c.gridy = 0; c.weighty = 1.0; c.insets = new Insets(0, 0, 0, 0);
        fieldsRow.add(btnNgay, c);

        if (isFrom) {
            tfGioTu = tfGio; tfNgayTu = tfNgay;
        } else {
            tfGioDen = tfGio; tfNgayDen = tfNgay;
        }

        container.add(fieldsRow, BorderLayout.CENTER);
        return container;
    }

    private void loadPhongTrong() {
        availableRoomPanel.removeAll();
        // Dùng BoxLayout xếp dọc
        availableRoomPanel.setLayout(new BoxLayout(availableRoomPanel, BoxLayout.Y_AXIS));

        List<Phong> list = new PhongDao().getPhongTrongTheoLoai(cbbLoaiPhong.getSelectedItem().toString());

        for (Phong p : list) {
            // Kiểm tra xem phòng này có đang là phòng đang chọn không
            boolean isSelected = p.getMaPhong().equals(selectedMaPhong);

            JPanel item = createRoomListItem(p.getMaPhong(), p.getTrangThai(), isSelected);

            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 1. Cập nhật mã phòng được chọn
                    selectedMaPhong = p.getMaPhong();

                    // 2. GỌI LẠI LOAD ĐỂ VẼ LẠI TOÀN BỘ DANH SÁCH (Trick này đảm bảo chỉ 1 phòng được chọn)
                    loadPhongTrong();
                }
            });

            availableRoomPanel.add(item);
            availableRoomPanel.add(Box.createVerticalStrut(5)); // Khoảng cách giữa các dòng
        }

        availableRoomPanel.revalidate();
        availableRoomPanel.repaint();
    }

    private void clearForm() {
        tfHoTen.setText(""); tfSDT.setText(""); tfCCCD.setText(""); tfSoLuongNguoi.setText("");
        tfGioTu.setText(""); tfGioDen.setText(""); tfNgayTu.setText(""); tfNgayDen.setText("");
        cbGioiTinh.setSelectedIndex(0);

        if (cbbLoaiPhong.getItemCount() > 0) cbbLoaiPhong.setSelectedIndex(0);
        selectedMaPhong = null;

        // SỬA TẠI ĐÂY: Thêm kiểm tra instanceof
        for (Component c : availableRoomPanel.getComponents()) {
            if (c instanceof JPanel) { // Chỉ ép kiểu nếu nó là JPanel
                JPanel p = (JPanel) c;
                p.setBackground(Color.WHITE);
                p.setBorder(new LineBorder(COLOR_BORDER, 1, true));
            }
        }
    }

    private void handleDatPhong() {
        // 1. Kiểm tra định dạng dữ liệu đầu vào (Họ tên, SĐT, CCCD, Số người)
        if (!validateInput()) {
            return;
        }

        // 2. Kiểm tra xem nhân viên đã chọn phòng trống trên giao diện chưa
        if (selectedMaPhong == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng trước khi tiến hành đặt!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 3. Kiểm tra các trường thông tin bắt buộc không được bỏ trống
        if (tfHoTen.getText().trim().isEmpty()
                || tfSDT.getText().trim().isEmpty()
                || tfSoLuongNguoi.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin khách hàng!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            KhachHangDao khDao = new KhachHangDao();
            PhieuDatPhongDao pdDao = new PhieuDatPhongDao();
            ChiTietPhieuDatPhongDao ctDao = new ChiTietPhieuDatPhongDao();
            PhongDao phongDao = new PhongDao();
            HoaDonPhongDao hdDao = new HoaDonPhongDao();

            // =====================================================
            // STEP 1: XỬ LÝ THÔNG TIN KHÁCH HÀNG
            // =====================================================
            String sdt = tfSDT.getText().trim();
            String ten = tfHoTen.getText().trim();
            String cccd = tfCCCD.getText().trim();

            String maKH = khDao.getMaKHBySDT(sdt);

            if (maKH == null) {
                // Nếu khách mới, truyền CCCD vào (không cần dùng toán tử ? ở đây vì bạn đã có giá trị cccd)
                maKH = khDao.insertKhach(ten, sdt, cccd);
            } else {
                // Nếu khách cũ, cập nhật lại CCCD (Logic tùy chọn của bạn)
                khDao.updateCCCD(maKH, cccd);
            }

            if (maKH == null) {
                JOptionPane.showMessageDialog(this, "Không thể khởi tạo hoặc truy xuất thông tin khách hàng!");
                return;
            }

            // =====================================================
            // STEP 2: KHỞI TẠO PHIẾU ĐẶT PHÒNG
            // =====================================================
            String maPhieu = pdDao.insert(maKH, maNV);
            if (maPhieu == null) {
                JOptionPane.showMessageDialog(this, "Khởi tạo phiếu đặt phòng thất bại!");
                return;
            }

            // =====================================================
            // STEP 3: KIỂM TRA THỜI GIAN VÀ THÊM CHI TIẾT ĐẶT PHÒNG
            // =====================================================
            int soNguoi = Integer.parseInt(tfSoLuongNguoi.getText().trim());
            String ngayTu = tfNgayTu.getText().trim();
            String gioTu = tfGioTu.getText().trim();
            String ngayDen = tfNgayDen.getText().trim();
            String gioDen = tfGioDen.getText().trim();

            if (ngayTu.isEmpty() || gioTu.isEmpty() || ngayDen.isEmpty() || gioDen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng cấu hình đầy đủ thời gian nhận và trả phòng!");
                return;
            }

            // Định dạng thời gian đồng bộ với Spinner
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime tu = LocalDateTime.parse(ngayTu + " " + gioTu, formatter);
            LocalDateTime den = LocalDateTime.parse(ngayDen + " " + gioDen, formatter);

            if (den.isBefore(tu)) {
                JOptionPane.showMessageDialog(this, "Thời gian trả phòng dự kiến bắt buộc phải lớn hơn thời gian nhận phòng!");
                return;
            }

            // Ghi nhận chi tiết đặt phòng
            boolean ok = ctDao.insert(maPhieu, selectedMaPhong, tu, den, soNguoi);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Ghi nhận chi tiết phiếu đặt phòng thất bại!");
                return;
            }

            // =====================================================
            // STEP 4: CẬP NHẬT TRẠNG THÁI PHÒNG SANG "ĐÃ ĐẶT"
            // =====================================================
            boolean updated = phongDao.updateTrangThai(selectedMaPhong, "Đã đặt");
            if (!updated) {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái phòng sang 'Đã đặt'!");
                return;
            }

            // =====================================================
            // STEP 5: TẠO HÓA ĐƠN TIỀN PHÒNG CHÍNH (DUY NHẤT 1 BẢN GHI)
            // =====================================================
            String maHD = hdDao.getNextMaHD();
            boolean insertHDSuccess = hdDao.insertCoPhieuDatPhong(
                    maHD,
                    maNV,
                    maKH,
                    maPhieu
            );

            if (!insertHDSuccess) {
                JOptionPane.showMessageDialog(this, "Lỗi nghiêm trọng: Không thể khởi tạo hóa đơn thanh toán!");
                return;
            }

            // ĐÃ BỎ HOÀN TOÀN STEP 6 THEO YÊU CẦU

            // Hoàn tất quy trình hoàn chỉnh
            JOptionPane.showMessageDialog(
                    this,
                    "Đặt phòng thành công!\nPhòng số: " + selectedMaPhong + "\nTrạng thái hệ thống: Đã đặt",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Làm mới dữ liệu hiển thị trên giao diện phẳng
            loadDatPhong();
            loadPhongTrong();
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Hệ thống gặp lỗi trong quá trình xử lý đặt phòng:\n" + e.getMessage(),
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private JPanel createRoomListItem(String maPhong, String status, boolean isSelected) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Chiều cao cố định
        item.setPreferredSize(new Dimension(250, 45));

        // Logic đổi màu khi chọn
        item.setBackground(isSelected ? new Color(220, 235, 255) : Color.WHITE);
        item.setBorder(new CompoundBorder(
                new LineBorder(isSelected ? COLOR_PRIMARY : COLOR_BORDER, isSelected ? 2 : 1, true),
                new EmptyBorder(0, 10, 0, 10)
        ));

        JLabel lbMa = new JLabel("Phòng: " + maPhong);
        lbMa.setFont(new Font("Segoe UI", isSelected ? Font.BOLD : Font.PLAIN, 14));

        item.add(lbMa, BorderLayout.CENTER);
        return item;
    }

    private void loadDatPhong() {
        bookedRoomPanel.removeAll();
        bookedRoomPanel.setLayout(new BoxLayout(bookedRoomPanel, BoxLayout.Y_AXIS));
        List<Object[]> list = new PhieuDatPhongDao().getAllDatPhong();
        for (Object[] o : list) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setOpaque(false);
            JLabel lblText = new JLabel("📝 Hóa đơn: " + o[0] + "  |  Phòng: " + o[2] + "  |  " + o[3]);
            lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblText.setForeground(COLOR_TEXT_MAIN);
            p.add(lblText);
            bookedRoomPanel.add(p);
        }
        bookedRoomPanel.revalidate();
        bookedRoomPanel.repaint();
    }

    private JPanel createStyledCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }

    private JTextField createText(String title) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setPreferredSize(new Dimension(0, 42));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        tf.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        tf.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                title, 0, 0, new Font("Segoe UI", Font.BOLD, 12), COLOR_TEXT_MUTED
        ));
        return tf;
    }

    private JPanel wrap(Component c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(c, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return p;
    }

    private Button createBtn(String text, Color color) {
        Button b = new Button();
        b.setText(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(0, 42));
        b.setMinimumSize(new Dimension(0, 42));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        return b;
    }

    private JPanel createListPanel(String title, java.awt.Component content) { // Đổi thành Component
        JPanel panel = createStyledCard();
        panel.setLayout(new BorderLayout(0, 10));

        JLabel lb = new JLabel(title);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lb.setForeground(COLOR_TEXT_MAIN);

        panel.add(lb, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER); // Component sẽ được add vào đây
        return panel;
    }

    private boolean validateInput() {
        String ten = tfHoTen.getText().trim();
        String sdt = tfSDT.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String soNguoi = tfSoLuongNguoi.getText().trim();

        if (ten.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || soNguoi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền toàn bộ trường thông tin!");
            return false;
        }
        if (!ten.matches("^([A-ZÀ-Ỹ][a-zà-ỹ]+\\s?)+$")) {
            JOptionPane.showMessageDialog(this, "Định dạng họ và tên không hợp lệ!");
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải bao gồm 10 chữ số (bắt đầu bằng số 0)!");
            return false;
        }
        if (!cccd.matches("^\\d{12}$")) {
            JOptionPane.showMessageDialog(this, "Căn cước công dân phải chính xác 12 ký tự số!");
            return false;
        }
        if (!soNguoi.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this, "Số lượng người bắt buộc phải nhập số!");
            return false;
        }
        return true;
    }
}