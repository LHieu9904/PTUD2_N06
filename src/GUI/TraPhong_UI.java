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
}