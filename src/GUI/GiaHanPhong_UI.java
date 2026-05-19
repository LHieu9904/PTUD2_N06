package GUI;

import Dao.ChiTietHoaDonPhongDao;
import Dao.PhongDao;
import Raven.button.Button;
import Raven.textfield.TextField;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GiaHanPhong_UI extends JPanel {

    // =====================================================
    // COLOR
    // =====================================================

    private final Color BG =
            new Color(245,247,250);

    private final Color CARD =
            Color.WHITE;

    private final Color PRIMARY =
            new Color(59,130,246);

    private final Color SUCCESS =
            new Color(16,185,129);

    private final Color DANGER =
            new Color(239,68,68);

    private final Color TEXT =
            new Color(15,23,42);

    private final Color SUBTEXT =
            new Color(100,116,139);

    private final Color BORDER =
            new Color(226,232,240);

    // =====================================================
    // COMPONENT
    // =====================================================

    private TextField txtSearch;

    private TextField txtMaPhong;
    private TextField txtTenKhach;
    private TextField txtSDT;
    private TextField txtNgayNhan;
    private TextField txtThoiGianTraCu;
    private TextField txtThoiGianTraMoi;
    private TextField txtPhiGiaHan;

    private JComboBox<String> cbThemGio;

    private Button btnSearch;
    private Button btnXacNhan;
    private Button btnHuy;

    private Button btn1H;
    private Button btn2H;
    private Button btn3H;

    // =====================================================
    // DAO
    // =====================================================

    private final PhongDao phongDao =
            new PhongDao();

    private final ChiTietHoaDonPhongDao ctHoaDonDao =
            new ChiTietHoaDonPhongDao();

    private Timestamp thoiGianTraHienTai;

    private Timestamp thoiGianTraMoi;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public GiaHanPhong_UI() {

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (Exception ignored) {
        }

        initUI();

        initEvent();
    }

    // =====================================================
    // UI
    // =====================================================

    private void initUI(){

        setLayout(new BorderLayout());

        setBackground(BG);

        setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        12,
                        0
                )
        );

        JLabel title =
                new JLabel("GIA HẠN PHÒNG");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        title.setForeground(TEXT);

        header.add(
                title,
                BorderLayout.WEST
        );

        add(header, BorderLayout.NORTH);

        // =====================================================
        // MAIN
        // =====================================================

        JPanel main =
                new JPanel();

        main.setOpaque(false);

        main.setLayout(
                new BoxLayout(
                        main,
                        BoxLayout.Y_AXIS
                )
        );

        add(main, BorderLayout.CENTER);

        // =====================================================
        // SEARCH CARD
        // =====================================================

        RoundedPanel searchCard =
                createCard();

        searchCard.setLayout(
                new BorderLayout(
                        12,
                        0
                )
        );

        searchCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        95
                )
        );

        JLabel lbSearch =
                createSectionTitle(
                        "TÌM KIẾM"
                );

        txtSearch =
                createInput(
                        "Nhập mã phòng hoặc SĐT khách"
                );

        btnSearch =
                createButton(
                        "TÌM KIẾM",
                        PRIMARY
                );

        JPanel searchBottom =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        searchBottom.setOpaque(false);

        searchBottom.add(
                txtSearch,
                BorderLayout.CENTER
        );

        searchBottom.add(
                btnSearch,
                BorderLayout.EAST
        );

        searchCard.add(
                lbSearch,
                BorderLayout.NORTH
        );

        searchCard.add(
                searchBottom,
                BorderLayout.CENTER
        );

        main.add(searchCard);

        main.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // INFO CARD
        // =====================================================

        RoundedPanel infoCard =
                createCard();

        infoCard.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JLabel lbInfo =
                createSectionTitle(
                        "THÔNG TIN PHÒNG"
                );

        infoCard.add(
                lbInfo,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtMaPhong =
                createReadOnlyField();

        txtTenKhach =
                createReadOnlyField();

        txtSDT =
                createReadOnlyField();

        txtNgayNhan =
                createReadOnlyField();

        txtThoiGianTraCu =
                createReadOnlyField();

        txtThoiGianTraMoi =
                createReadOnlyField();

        txtPhiGiaHan =
                createReadOnlyField();

        int y = 0;

        addField(
                form,
                gbc,
                y++,
                "Mã phòng",
                txtMaPhong,
                "Tên khách",
                txtTenKhach
        );

        addField(
                form,
                gbc,
                y++,
                "SĐT",
                txtSDT,
                "Ngày nhận",
                txtNgayNhan
        );

        addField(
                form,
                gbc,
                y++,
                "Giờ trả",
                txtThoiGianTraCu,
                "Giờ mới",
                txtThoiGianTraMoi
        );

        infoCard.add(form);

        main.add(infoCard);

        main.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // GIA HẠN CARD
        // =====================================================

        RoundedPanel extendCard =
                createCard();

        extendCard.setLayout(
                new BorderLayout(
                        0,
                        8
                )
        );

        extendCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        88
                )
        );

        JLabel lbExtend =
                createSectionTitle(
                        "GIA HẠN"
                );

        extendCard.add(
                lbExtend,
                BorderLayout.NORTH
        );

        JPanel extendContent =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                8,
                                0
                        )
                );

        extendContent.setOpaque(false);

        btn1H =
                createButton(
                        "+1 GIỜ",
                        PRIMARY
                );

        btn2H =
                createButton(
                        "+2 GIỜ",
                        PRIMARY
                );

        btn3H =
                createButton(
                        "+3 GIỜ",
                        PRIMARY
                );

        cbThemGio =
                new JComboBox<>(
                        new String[]{
                                "Khác",
                                "30 phút",
                                "1 giờ",
                                "2 giờ",
                                "3 giờ"
                        }
                );

        cbThemGio.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        extendContent.add(btn1H);
        extendContent.add(btn2H);
        extendContent.add(btn3H);
        extendContent.add(cbThemGio);

        extendCard.add(
                extendContent,
                BorderLayout.CENTER
        );

        main.add(extendCard);

        main.add(
                Box.createVerticalStrut(10)
        );

        // =====================================================
        // FEE CARD
        // =====================================================

        RoundedPanel feeCard =
                createCard();

        feeCard.setLayout(
                new BorderLayout(
                        0,
                        10
                )
        );

        feeCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        88
                )
        );

        JLabel lbFee =
                createSectionTitle(
                        "CHI PHÍ"
                );

        feeCard.add(
                lbFee,
                BorderLayout.NORTH
        );

        JPanel feeWrap =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        feeWrap.setOpaque(false);

        JLabel lbPhi =
                createLabel("Phí");

        feeWrap.add(
                lbPhi,
                BorderLayout.WEST
        );

        feeWrap.add(
                txtPhiGiaHan,
                BorderLayout.CENTER
        );

        feeCard.add(
                feeWrap,
                BorderLayout.CENTER
        );

        main.add(feeCard);

        main.add(
                Box.createVerticalStrut(8)
        );

        // =====================================================
        // FOOTER
        // =====================================================

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        footer.setOpaque(false);

        footer.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        btnHuy =
                createButton(
                        "HỦY",
                        DANGER
                );

        btnXacNhan =
                createButton(
                        "XÁC NHẬN",
                        SUCCESS
                );

        footer.add(btnHuy);

        footer.add(btnXacNhan);

        main.add(footer);
    }

    // =====================================================
    // EVENT
    // =====================================================

    private void initEvent(){

        btnSearch.addActionListener(
                e -> timPhong()
        );

        btn1H.addActionListener(
                e -> giaHanGio(
                        1,
                        100000
                )
        );

        btn2H.addActionListener(
                e -> giaHanGio(
                        2,
                        200000
                )
        );

        btn3H.addActionListener(
                e -> giaHanGio(
                        3,
                        300000
                )
        );

        btnXacNhan.addActionListener(
                e -> xacNhanGiaHan()
        );

        btnHuy.addActionListener(
                e -> clearForm()
        );

        cbThemGio.addActionListener(e -> {

            if(thoiGianTraHienTai == null){
                return;
            }

            String value =
                    cbThemGio.getSelectedItem()
                            .toString();

            switch (value){

                case "30 phút":
                    giaHanPhut(
                            30,
                            50000
                    );
                    break;

                case "1 giờ":
                    giaHanGio(
                            1,
                            100000
                    );
                    break;

                case "2 giờ":
                    giaHanGio(
                            2,
                            200000
                    );
                    break;

                case "3 giờ":
                    giaHanGio(
                            3,
                            300000
                    );
                    break;
            }
        });
    }

    // =====================================================
    // COMPONENT
    // =====================================================

    private RoundedPanel createCard(){

        RoundedPanel p =
                new RoundedPanel();

        p.setBackground(CARD);

        p.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        return p;
    }

    private JLabel createSectionTitle(
            String text
    ){

        JLabel lb =
                new JLabel(text);

        lb.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lb.setForeground(TEXT);

        return lb;
    }

    private JLabel createLabel(
            String text
    ){

        JLabel lb =
                new JLabel(text);

        lb.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lb.setForeground(TEXT);

        return lb;
    }

    private TextField createInput(
            String hint
    ){

        TextField tf =
                new TextField();

        tf.setHint(hint);

        tf.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        tf.setPreferredSize(
                new Dimension(
                        300,
                        40
                )
        );

        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        return tf;
    }

    private TextField createReadOnlyField(){

        TextField tf =
                new TextField();

        tf.setEditable(false);

        tf.setBackground(
                new Color(
                        248,
                        250,
                        252
                )
        );

        tf.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tf.setPreferredSize(
                new Dimension(
                        250,
                        40
                )
        );

        tf.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        return tf;
    }

    private Button createButton(
            String text,
            Color bg
    ){

        Button btn =
                new Button();

        btn.setText(text);

        btn.setBackground(bg);

        btn.setForeground(Color.WHITE);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        120,
                        36
                )
        );

        btn.setBorder(
                new EmptyBorder(
                        6,
                        14,
                        6,
                        14
                )
        );

        return btn;
    }

    private void addField(
            JPanel form,
            GridBagConstraints gbc,
            int y,
            String lb1,
            JComponent c1,
            String lb2,
            JComponent c2
    ){

        gbc.gridy = y;

        gbc.gridx = 0;

        form.add(
                createLabel(lb1),
                gbc
        );

        gbc.gridx = 1;

        form.add(
                c1,
                gbc
        );

        gbc.gridx = 2;

        form.add(
                createLabel(lb2),
                gbc
        );

        gbc.gridx = 3;

        form.add(
                c2,
                gbc
        );
    }

    // =====================================================
    // LOGIC
    // =====================================================

    private void timPhong(){

        String keyword =
                txtSearch.getText().trim();

        if(keyword.isEmpty()){

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã phòng hoặc SĐT khách!"
            );

            return;
        }

        Object[] data =
                phongDao.getThongTinGiaHanPhong(
                        keyword
                );

        if(data == null){

            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phòng đang thuê!"
            );

            clearForm();

            return;
        }

        txtMaPhong.setText(
                data[0].toString()
        );

        txtTenKhach.setText(
                data[1].toString()
        );

        txtSDT.setText(
                data[2].toString()
        );

        txtNgayNhan.setText(
                data[3].toString()
        );

        txtThoiGianTraCu.setText(
                data[4].toString()
        );

        txtThoiGianTraMoi.setText(
                data[4].toString()
        );

        thoiGianTraHienTai =
                (Timestamp) data[4];

        thoiGianTraMoi =
                (Timestamp) data[4];

        txtPhiGiaHan.setText("0 VNĐ");
    }

    private void giaHanGio(
            int gio,
            double phi
    ){

        Calendar cal =
                Calendar.getInstance();

        cal.setTime(
                thoiGianTraHienTai
        );

        cal.add(
                Calendar.HOUR_OF_DAY,
                gio
        );

        thoiGianTraMoi =
                new Timestamp(
                        cal.getTimeInMillis()
                );

        hienThiGiaHan(phi);
    }

    private void giaHanPhut(
            int phut,
            double phi
    ){

        Calendar cal =
                Calendar.getInstance();

        cal.setTime(
                thoiGianTraHienTai
        );

        cal.add(
                Calendar.MINUTE,
                phut
        );

        thoiGianTraMoi =
                new Timestamp(
                        cal.getTimeInMillis()
                );

        hienThiGiaHan(phi);
    }

    private void hienThiGiaHan(
            double phi
    ){

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                );

        txtThoiGianTraMoi.setText(
                sdf.format(
                        thoiGianTraMoi
                )
        );

        DecimalFormat df =
                new DecimalFormat(
                        "#,###"
                );

        txtPhiGiaHan.setText(
                df.format(phi) + " VNĐ"
        );
    }

    private void xacNhanGiaHan(){

        String maPhong =
                txtMaPhong.getText().trim();

        if(maPhong.isEmpty()){

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng tìm phòng trước!"
            );

            return;
        }

        boolean result =
                ctHoaDonDao.updateThoiGianTra(
                        maPhong,
                        thoiGianTraMoi
                );

        if(!result){

            JOptionPane.showMessageDialog(
                    this,
                    "Gia hạn thất bại!"
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Gia hạn thành công!"
        );

        txtThoiGianTraCu.setText(
                txtThoiGianTraMoi.getText()
        );

        thoiGianTraHienTai =
                thoiGianTraMoi;
    }

    private void clearForm(){

        txtSearch.setText("");

        txtMaPhong.setText("");

        txtTenKhach.setText("");

        txtSDT.setText("");

        txtNgayNhan.setText("");

        txtThoiGianTraCu.setText("");

        txtThoiGianTraMoi.setText("");

        txtPhiGiaHan.setText("");

        cbThemGio.setSelectedIndex(0);

        thoiGianTraHienTai = null;

        thoiGianTraMoi = null;
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    class RoundedPanel extends JPanel {

        private final int radius = 26;

        public RoundedPanel(){

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // SHADOW
            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            10
                    )
            );

            g2.fillRoundRect(
                    4,
                    4,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            // BACKGROUND
            g2.setColor(getBackground());

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth()-5,
                    getHeight()-5,
                    radius,
                    radius
            );

            // BORDER
            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth()-5,
                    getHeight()-5,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}