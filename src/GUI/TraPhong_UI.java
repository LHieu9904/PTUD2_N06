/*
package GUI;

import Dao.HoaDonPhongDao;
import Dao.KhuyenMaiDao;
import Dao.PhongDao;
import Dao.ThueDao;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TraPhong_UI extends JPanel {

    private JPanel panelPhongChuaTT;
    private JPanel panelPhongDaTT;
    private JPanel panelPhongDangChon;
    private String maNV;
    private TextField txtHoTen;
    private TextField txtSDT;
    private TextField txtMaPhong;
    private TextField txtNgayNhan;
    private TextField txtNgayTra;
    private TextField txtTongTien;
    private TextField txtConLai;

    private JTable tableDichVu;
    private DefaultTableModel modelDichVu;

    private Button btnThanhToan;
    private Button btnTraPhong;


    private final PhongDao phongDao =
            new PhongDao();

    public TraPhong_UI(String maNV) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        this.maNV = maNV;

        initUI();
        initEvent();
        loadDanhSachPhongDangThue();
    }



    private void initUI() {

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);

        JLabel lblTitle =
                new JLabel("TRẢ PHÒNG - THANH TOÁN");

        lblTitle.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        24
                )
        );

        lblTitle.setForeground(
                new Color(0, 153, 255)
        );

        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        JPanel main =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                12,
                                0
                        )
                );

        main.setBackground(Color.WHITE);
        add(main, BorderLayout.CENTER);


        JPanel col1 =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                8,
                                8
                        )
                );

        col1.setBackground(Color.WHITE);

        panelPhongChuaTT = new JPanel();
        panelPhongDaTT = new JPanel();

        col1.add(
                createListPanel(
                        "Phòng chưa thanh toán",
                        panelPhongChuaTT
                )
        );

        col1.add(
                createListPanel(
                        "Phòng đã thanh toán",
                        panelPhongDaTT
                )
        );

        main.add(col1);

        JPanel col2 =
                new JPanel(
                        new BorderLayout()
                );

        col2.setBackground(Color.WHITE);

        panelPhongDangChon = new JPanel();

        col2.add(
                createListPanel(
                        "Phòng đang chọn",
                        panelPhongDangChon
                )
        );

        main.add(col2);


        JPanel col3 =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        col3.setBackground(Color.WHITE);


        JPanel panelKH =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                8,
                                8
                        )
                );

        panelKH.setBackground(Color.WHITE);
        panelKH.setBorder(
                createBorder("Khách hàng")
        );

        txtHoTen = createField();
        txtSDT = createField();

        panelKH.add(new JLabel("Họ tên"));
        panelKH.add(txtHoTen);

        panelKH.add(new JLabel("SĐT"));
        panelKH.add(txtSDT);

        col3.add(
                panelKH,
                BorderLayout.NORTH
        );



        JPanel panelPhong =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        panelPhong.setBackground(Color.WHITE);
        panelPhong.setBorder(
                createBorder("Thông tin phòng")
        );

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                8,
                                8
                        )
                );

        infoPanel.setBackground(Color.WHITE);

        txtMaPhong = createField();
        txtNgayNhan = createField();
        txtNgayTra = createField();

        infoPanel.add(new JLabel("Mã phòng"));
        infoPanel.add(txtMaPhong);

        infoPanel.add(new JLabel("Ngày nhận"));
        infoPanel.add(txtNgayNhan);

        infoPanel.add(new JLabel("Ngày trả"));
        infoPanel.add(txtNgayTra);

        panelPhong.add(
                infoPanel,
                BorderLayout.NORTH
        );



        modelDichVu = new DefaultTableModel(
                        new String[]{
                                "Tên dịch vụ",
                                "Số lượng",
                                "Đơn giá",
                                "Thành tiền"}, 0);

        tableDichVu = new JTable(modelDichVu);

        tableDichVu.setRowHeight(32);

        JScrollPane scroll = new JScrollPane(tableDichVu);

        panelPhong.add(scroll, BorderLayout.CENTER);

        col3.add(panelPhong, BorderLayout.CENTER);
        main.add(col3);
        JPanel footer = new JPanel(new GridLayout(1, 3, 12, 0));
        footer.setBackground(Color.WHITE);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.setBackground(Color.WHITE);

        txtConLai = createField();
        txtConLai.setForeground(Color.RED);

        p1.add(new JLabel("Tiền phòng"));
        p1.add(txtConLai);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p2.setBackground(Color.WHITE);

        txtTongTien = createField();
        txtTongTien.setForeground(Color.RED);

        p2.add(new JLabel("Tiền dịch vụ "));
        p2.add(txtTongTien);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        p3.setBackground(Color.WHITE);

        btnThanhToan = new Button();
        btnThanhToan.setText("Thanh toán");

        btnTraPhong = new Button();
        btnTraPhong.setText("Trả phòng");

        p3.add(btnThanhToan);
        p3.add(btnTraPhong);

        footer.add(p1);
        footer.add(p2);
        footer.add(p3);

        add(footer, BorderLayout.SOUTH);
    }

    // =====================================================
    // EVENT
    // =====================================================

    private void initEvent() {

        btnThanhToan.addActionListener(
                e -> moPopupThanhToan()
        );

        btnTraPhong.addActionListener(
                e -> traPhong()
        );
    }

    // =====================================================
    // LOAD DANH SÁCH PHÒNG ĐANG THUÊ
    // =====================================================

    private void loadDanhSachPhongDangThue() {

        panelPhongChuaTT.removeAll();
        panelPhongDaTT.removeAll();
        panelPhongDangChon.removeAll();

        List<Object[]> list = new HoaDonPhongDao().getAllDangThue();

        if (list == null || list.isEmpty()) {

            panelPhongChuaTT.add(
                    new JLabel(
                            "Không có phòng đang thuê"
                    )
            );

            revalidate();
            repaint();
            return;
        }

        for (Object[] row : list) {

            String maPhong =
                    row[0] != null
                            ? row[0].toString()
                            : "";

            String trangThai =
                    row[5] != null
                            ? row[5].toString()
                            : "";

            JLabel lblPhong =
                    new JLabel(
                            "Phòng " + maPhong
                    );

            lblPhong.setOpaque(true);
            lblPhong.setBackground(Color.WHITE);
            lblPhong.setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            lblPhong.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(220, 220, 220)),
                            new EmptyBorder(10, 12, 10, 12)));

            lblPhong.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseClicked(
                                MouseEvent e
                        ) {
                            loadThongTinChiTietPhong(maPhong);
                            panelPhongDangChon.removeAll();
                            JLabel selected = new JLabel("Phòng " + maPhong);
                            selected.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                            panelPhongDangChon.add(selected);
                            panelPhongDangChon.revalidate();
                            panelPhongDangChon.repaint();
                        }
                    }
            );

            if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
                panelPhongDaTT.add(lblPhong);
            } else {
                panelPhongChuaTT.add(lblPhong);
            }
        }

        panelPhongChuaTT.revalidate();
        panelPhongDaTT.revalidate();
        repaint();
    }

    // =====================================================
    // LOAD CHI TIẾT PHÒNG
    // =====================================================

    private void loadThongTinChiTietPhong(String maPhong) {

        HoaDonPhongDao hdDao = new HoaDonPhongDao();
        hdDao.updateTienPhong(maPhong);
        hdDao.applyKhuyenMai(maPhong);
        Object[] data = phongDao.getChiTietTraPhong(maPhong);
        if (data == null) return;
        txtMaPhong.setText(data[0] != null ? data[0].toString() : "");
        txtHoTen.setText(data[1] != null ? data[1].toString() : "");
        txtSDT.setText(data[2] != null ? data[2].toString() : "");
        txtNgayNhan.setText(data[3] != null ? data[3].toString() : "");
        txtNgayTra.setText(data[4] != null ? data[4].toString() : "Chưa xác định");
        double tienPhong = data[5] != null ? Double.parseDouble(data[5].toString()) : 0;
        double tienDV = data[6] != null ? Double.parseDouble(data[6].toString()) : 0;
        txtConLai.setText(String.valueOf(tienPhong));
        txtTongTien.setText(String.valueOf(tienDV));
        loadDichVuDaSuDung(maPhong);
    }

    // =====================================================
    // LOAD DỊCH VỤ ĐÃ SỬ DỤNG
    // =====================================================

    private void loadDichVuDaSuDung(String maPhong) {
        modelDichVu.setRowCount(0);
        List<Object[]> list = phongDao.getDichVuDaSuDung(maPhong);
        for (Object[] row : list) {
            modelDichVu.addRow(
                    new Object[]{
                            row[0],
                            row[1],
                            row[2],
                            row[3]
                    }
            );
        }
    }

    // =====================================================
    // MỞ POPUP THANH TOÁN
    // =====================================================

    private void moPopupThanhToan() {

        String maPhong =
                txtMaPhong.getText().trim();

        if (maPhong.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng trước!"
            );

            return;
        }

        Object[] data =
                phongDao.getChiTietTraPhong(
                        maPhong
                );

        if (data == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không lấy được thông tin phòng!"
            );

            return;
        }

        JFrame parent =
                (JFrame)
                        SwingUtilities
                                .getWindowAncestor(
                                        this
                                );

        new PopupThanhToan_UI(
                parent,
                data,
                maNV
        );

        // refresh lại sau khi popup đóng
        clearForm();
        loadDanhSachPhongDangThue();
    }

    // =====================================================
    // TRẢ PHÒNG
    // =====================================================

    private void traPhong() {

        String maPhong = txtMaPhong.getText().trim();

        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận trả phòng?",
                "Trả phòng",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;
        HoaDonPhongDao hdDao = new HoaDonPhongDao();
        hdDao.updateTienPhong(maPhong);
        hdDao.applyKhuyenMai(maPhong);

        // ===== UPDATE TRẠNG THÁI =====
        boolean result =
                phongDao.updateTrangThaiPhong(
                        maPhong,
                        "Đang dọn dẹp"
                );

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Trả phòng thành công!"
            );

            clearForm();
            loadDanhSachPhongDangThue();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Trả phòng thất bại!"
            );
        }
    }


    private void clearForm() {

        txtHoTen.setText("");
        txtSDT.setText("");
        txtMaPhong.setText("");
        txtNgayNhan.setText("");
        txtNgayTra.setText("");
        txtTongTien.setText("");
        txtConLai.setText("");

        modelDichVu.setRowCount(0);

        panelPhongDangChon.removeAll();
        panelPhongDangChon.revalidate();
        panelPhongDangChon.repaint();
    }
    private JPanel createListPanel(String title, JPanel content) {

        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(title);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        lbl.setFont(new Font("Tahoma", Font.BOLD, 15));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(content), BorderLayout.CENTER);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        return panel;
    }

    private TitledBorder createBorder(String title) {
        return BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 13));
    }

    private TextField createField() {

        TextField tf = new TextField();

        tf.setPreferredSize(new Dimension(180, 32));
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));

        return tf;
    }
}*/
package GUI;

import Dao.HoaDonPhongDao;
import Dao.PhongDao;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TraPhong_UI extends JPanel {

    private JPanel panelPhongChuaTT;
    private JPanel panelPhongDangChon;

    private String maNV;

    private TextField txtHoTen;
    private TextField txtSDT;
    private TextField txtMaPhong;
    private TextField txtNgayNhan;
    private TextField txtNgayTra;

    private JTable tableDichVu;
    private DefaultTableModel modelDichVu;

    private Button btnThanhToan;
    private Button btnLamMoi;

    private final PhongDao phongDao = new PhongDao();

    // =====================================================
    // HỆ THỐNG MÀU SẮC DESIGN SYSTEM (FLAT UI)
    // =====================================================
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color SUCCESS = new Color(22, 163, 74);
    private final Color WARNING = new Color(234, 88, 12);
    private final Color TEXT = new Color(15, 23, 42);
    private final Color SUBTEXT = new Color(100, 116, 139);

    public TraPhong_UI(String maNV) {
        this.maNV = maNV;

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initUI();
        initEvent();
        loadDanhSachPhongDangThue();
    }

    // =====================================================
    // UI LAYOUT
    // =====================================================
    private void initUI() {

        // ================= HEADER PANEL =================
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TRẢ PHÒNG & THANH TOÁN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel sub = new JLabel("Hệ thống quản lý cập nhật thời gian thực");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(SUBTEXT);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(sub);
        header.add(titleBox, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ================= MAIN BODY =================
        JPanel main = new JPanel(new GridLayout(1, 3, 20, 0));
        main.setOpaque(false);
        add(main, BorderLayout.CENTER);

        // 1. CỘT TRÁI: DANH SÁCH CHỜ xử lý (Cần giữ lại ô Tìm kiếm)
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        panelPhongChuaTT = createRoomContainer();
        left.add(createListPanelWithSearch("Phòng chưa thanh toán", panelPhongChuaTT), BorderLayout.CENTER);
        main.add(left);

        // 2. CỘT GIỮA: PHÒNG ĐANG ĐƯỢC CHỌN THAO TÁC (ĐÃ XÓA Ô TÌM KIẾM XÁM THỪA THEO YÊU CẦU)
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        panelPhongDangChon = createRoomContainer();
        center.add(createListPanelNormal("Phòng đang xử lý", panelPhongDangChon));
        main.add(center);

        // 3. CỘT PHẢI: TOÀN BỘ FORM THÔNG TIN CHI TIẾT
        JPanel right = new JPanel(new BorderLayout(0, 20));
        right.setOpaque(false);

        // Khối thông tin khách hàng hành chính
        JPanel customerPanel = createRoundedPanel();
        customerPanel.setLayout(new GridLayout(2, 2, 12, 12));
        customerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtHoTen = createField(240);
        txtSDT = createField(240);

        customerPanel.add(createLabel("Họ tên khách hàng"));
        customerPanel.add(txtHoTen);
        customerPanel.add(createLabel("Số điện thoại liên hệ"));
        customerPanel.add(txtSDT);

        right.add(customerPanel, BorderLayout.NORTH);

        // Khối phòng & Bảng dịch vụ chi tiết
        JPanel roomPanel = createRoundedPanel();
        roomPanel.setLayout(new BorderLayout(0, 16));
        roomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        infoPanel.setOpaque(false);

        txtMaPhong = createField(240);
        txtNgayNhan = createField(240);
        txtNgayTra = createField(240);

        infoPanel.add(createLabel("Số phòng"));
        infoPanel.add(txtMaPhong);
        infoPanel.add(createLabel("Thời gian check-in"));
        infoPanel.add(txtNgayNhan);
        infoPanel.add(createLabel("Thời gian dự kiến check-out"));
        infoPanel.add(txtNgayTra);

        roomPanel.add(infoPanel, BorderLayout.NORTH);

        // Cấu hình bảng dịch vụ tiêu thụ
        modelDichVu = new DefaultTableModel(
                new String[]{"Tên dịch vụ sử dụng", "SL", "Đơn giá", "Thành tiền"}, 0
        );
        tableDichVu = new JTable(modelDichVu);
        styleTable();

        JScrollPane scroll = new JScrollPane(tableDichVu);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        roomPanel.add(scroll, BorderLayout.CENTER);
        right.add(roomPanel, BorderLayout.CENTER);

        main.add(right);

        // ================= FOOTER PANEL =================
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        footer.setOpaque(false);

        btnLamMoi = new Button();
        btnLamMoi.setText("🔄 LÀM MỚI");
        btnLamMoi.setBackground(Color.WHITE);
        btnLamMoi.setForeground(PRIMARY);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLamMoi.setPreferredSize(new Dimension(150, 50));
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnThanhToan = new Button();
        btnThanhToan.setText("TIẾN HÀNH XUẤT HÓA ĐƠN THANH TOÁN ➔");
        btnThanhToan.setBackground(SUCCESS);
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThanhToan.setPreferredSize(new Dimension(340, 50));
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        footer.add(btnLamMoi);
        footer.add(btnThanhToan);
        add(footer, BorderLayout.SOUTH);
    }

    // =====================================================
    // CONTROLLER EVENTS
    // =====================================================
    private void initEvent() {
        btnThanhToan.addActionListener(e -> moPopupThanhToan());

        btnLamMoi.addActionListener(e -> {
            loadDanhSachPhongDangThue();
            clearForm();
        });
    }

    // =====================================================
    // CORE LOGIC FUNCTIONS
    // =====================================================
    private void loadDanhSachPhongDangThue() {
        panelPhongChuaTT.removeAll();
        panelPhongDangChon.removeAll();

        List<Object[]> list = new HoaDonPhongDao().getAllDangThue();

        if (list == null || list.isEmpty()) {
            JLabel empty = new JLabel("Hiện không có phòng nào đang lưu trú");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setForeground(SUBTEXT);
            panelPhongChuaTT.add(empty);
            revalidate();
            repaint();
            return;
        }

        for (Object[] row : list) {
            String maPhong = row[0] != null ? row[0].toString() : "";
            String trangThai = row[5] != null ? row[5].toString() : "";

            if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
                continue;
            }

            JPanel roomCard = createRoomCard(maPhong, trangThai);

            roomCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    loadThongTinChiTietPhong(maPhong);
                    panelPhongDangChon.removeAll();

                    JPanel selected = createRoomCard(maPhong, "Đang xử lý");
                    panelPhongDangChon.add(selected);
                    panelPhongDangChon.revalidate();
                    panelPhongDangChon.repaint();
                }
            });

            panelPhongChuaTT.add(roomCard);
            panelPhongChuaTT.add(Box.createVerticalStrut(10));
        }
        revalidate();
        repaint();
    }

    private void loadThongTinChiTietPhong(String maPhong) {
        HoaDonPhongDao hdDao = new HoaDonPhongDao();
        hdDao.applyKhuyenMai(maPhong);

        Object[] data = phongDao.getChiTietTraPhong(maPhong);
        if (data == null) return;

        txtMaPhong.setText(data[0] + "");
        txtHoTen.setText(data[1] + "");
        txtSDT.setText(data[2] + "");
        txtNgayNhan.setText(data[3] + "");
        txtNgayTra.setText(data[4] + "");

        loadDichVuDaSuDung(maPhong);
    }

    private void loadDichVuDaSuDung(String maPhong) {
        modelDichVu.setRowCount(0);
        List<Object[]> list = phongDao.getDichVuDaSuDung(maPhong);
        for (Object[] row : list) {
            modelDichVu.addRow(new Object[]{row[0], row[1], row[2], row[3]});
        }
    }

    private void moPopupThanhToan() {
        String maPhong = txtMaPhong.getText().trim();
        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng cần thanh toán ở danh sách!");
            return;
        }

        Object[] data = phongDao.getChiTietTraPhong(maPhong);
        if (data == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phiên làm việc của phòng này!");
            return;
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        new PopupThanhToan_UI(parent, data, maNV);

        clearForm();
        loadDanhSachPhongDangThue();
    }

    private void clearForm() {
        txtHoTen.setText("");
        txtSDT.setText("");
        txtMaPhong.setText("");
        txtNgayNhan.setText("");
        txtNgayTra.setText("");
        modelDichVu.setRowCount(0);

        panelPhongDangChon.removeAll();
        panelPhongDangChon.revalidate();
        panelPhongDangChon.repaint();
    }

    // =====================================================
    // COMPONENT FACTORY SCHEMES
    // =====================================================
    private JPanel createRoundedPanel() {
        JPanel panel = new RoundedPanel(24);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JPanel createRoomContainer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    // THÀNH PHẦN 1: Panel chứa danh sách phòng có kèm thanh tìm kiếm (Cho cột trái)
    private JPanel createListPanelWithSearch(String title, JPanel content) {
        JPanel wrapper = new RoundedPanel(24);
        wrapper.setLayout(new BorderLayout(0, 16));
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(TEXT);
        header.add(lbl, BorderLayout.WEST);

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(160, 38));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(0, 12, 0, 12)
        ));
        txtSearch.setBackground(new Color(248, 250, 252));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm nhanh...");

        JPanel searchWrapper = new RoundedPanel(18);
        searchWrapper.setLayout(new BorderLayout());
        searchWrapper.setPreferredSize(new Dimension(160, 38));
        searchWrapper.add(txtSearch);
        header.add(searchWrapper, BorderLayout.EAST);

        wrapper.add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));

        wrapper.add(scroll, BorderLayout.CENTER);

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String keyword = txtSearch.getText().trim().toLowerCase();
                for (Component c : content.getComponents()) {
                    if (!(c instanceof JPanel)) continue;
                    JPanel card = (JPanel) c;
                    boolean visible = false;
                    for (Component child : card.getComponents()) {
                        if (child instanceof JLabel) {
                            if (((JLabel) child).getText().toLowerCase().contains(keyword)) {
                                visible = true;
                                break;
                            }
                        }
                    }
                    card.setVisible(visible);
                }
                content.revalidate();
                content.repaint();
            }
        });

        return wrapper;
    }

    // THÀNH PHẦN 2: Panel tiêu chuẩn đã LOẠI BỎ ô tìm kiếm xám rác (Cho cột giữa)
    private JPanel createListPanelNormal(String title, JPanel content) {
        JPanel wrapper = new RoundedPanel(24);
        wrapper.setLayout(new BorderLayout(0, 16));
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(TEXT);
        header.add(lbl, BorderLayout.WEST);

        wrapper.add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Ẩn thanh cuộn thừa

        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createRoomCard(String maPhong, String trangThai) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        card.setPreferredSize(new Dimension(260, 85));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel room = new JLabel("Phòng " + maPhong);
        room.setFont(new Font("Segoe UI", Font.BOLD, 18));
        room.setForeground(TEXT);
        card.add(room, BorderLayout.CENTER);

        if (!"Đang xử lý".equalsIgnoreCase(trangThai)) {
            JLabel status = new JLabel(trangThai);
            status.setFont(new Font("Segoe UI", Font.BOLD, 13));
            status.setForeground(WARNING);
            card.add(status, BorderLayout.SOUTH);
        }

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(SUBTEXT);
        return lbl;
    }

    private TextField createField(int width) {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(width, 42));
        tf.setEditable(false);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setForeground(TEXT);
        tf.setBackground(new Color(248, 250, 252));
        tf.setBorder(new EmptyBorder(0, 12, 0, 12));

        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 250, 252));
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 18, 18);
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(2, 2, 2, 2));
        wrapper.add(tf);
        tf.setOpaque(false);
        return tf;
    }

    private void styleTable() {
        tableDichVu.setRowHeight(40);
        tableDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDichVu.setGridColor(new Color(241, 245, 249));
        tableDichVu.setShowVerticalLines(false);
        tableDichVu.setSelectionBackground(new Color(219, 234, 254));

        JTableHeader header = tableDichVu.getTableHeader();
        header.setPreferredSize(new Dimension(0, 42));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    class RoundedPanel extends JPanel {
        private final int radius;
        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(15, 23, 42, 12));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
