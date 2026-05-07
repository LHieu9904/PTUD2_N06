package GUI;

import Dao.ChiTietHoaDonPhongDao;
import Dao.PhongDao;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GiaHanPhong_UI extends JPanel {

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

    private final PhongDao phongDao = new PhongDao();
    private final ChiTietHoaDonPhongDao ctHoaDonDao =
            new ChiTietHoaDonPhongDao();

    private Timestamp thoiGianTraHienTai;
    private Timestamp thoiGianTraMoi;

    public GiaHanPhong_UI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel();
        header.setBackground(new Color(0, 102, 204));
        header.setPreferredSize(new Dimension(0, 60));

        JLabel title = new JLabel("GIA HẠN PHÒNG");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // =====================================================
        // BODY
        // =====================================================

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(15, 20, 15, 20));

        add(body, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        body.add(mainPanel, BorderLayout.CENTER);

        // =====================================================
        // SEARCH PANEL
        // =====================================================

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(
                BorderFactory.createTitledBorder("Tìm kiếm phòng")
        );

        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.insets = new Insets(10, 10, 10, 10);
        searchGbc.fill = GridBagConstraints.HORIZONTAL;

        txtSearch = new TextField();
        txtSearch.setHint("Nhập mã phòng / SĐT khách");
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setForeground(Color.BLACK);

        txtSearch.setMinimumSize(
                new Dimension(300, 40)
        );

        txtSearch.setPreferredSize(
                new Dimension(400, 40)
        );

        txtSearch.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        btnSearch = new Button();
        btnSearch.setText("Tìm");
        btnSearch.setPreferredSize(
                new Dimension(120, 40)
        );

        searchGbc.gridx = 0;
        searchGbc.gridy = 0;
        searchGbc.weightx = 1;
        searchPanel.add(txtSearch, searchGbc);

        searchGbc.gridx = 1;
        searchGbc.weightx = 0;
        searchPanel.add(btnSearch, searchGbc);

        mainPanel.add(searchPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // =====================================================
        // INFO PANEL
        // =====================================================

        txtMaPhong = createReadOnlyField();
        txtTenKhach = createReadOnlyField();
        txtSDT = createReadOnlyField();
        txtNgayNhan = createReadOnlyField();
        txtThoiGianTraCu = createReadOnlyField();
        txtThoiGianTraMoi = createReadOnlyField();
        txtPhiGiaHan = createReadOnlyField();

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Thông tin phòng đang thuê"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Row 1

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Mã phòng"), gbc);

        gbc.gridx = 1;
        infoPanel.add(txtMaPhong, gbc);

        gbc.gridx = 2;
        infoPanel.add(new JLabel("Tên khách"), gbc);

        gbc.gridx = 3;
        infoPanel.add(txtTenKhach, gbc);

        // Row 2

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Số điện thoại"), gbc);

        gbc.gridx = 1;
        infoPanel.add(txtSDT, gbc);

        gbc.gridx = 2;
        infoPanel.add(new JLabel("Thời gian nhận"), gbc);

        gbc.gridx = 3;
        infoPanel.add(txtNgayNhan, gbc);

        // Row 3

        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(
                new JLabel("Thời gian trả hiện tại"),
                gbc
        );

        gbc.gridx = 1;
        infoPanel.add(txtThoiGianTraCu, gbc);

        gbc.gridx = 2;
        infoPanel.add(
                new JLabel("Thời gian trả mới"),
                gbc
        );

        gbc.gridx = 3;
        infoPanel.add(txtThoiGianTraMoi, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // =====================================================
        // GIA HẠN THỜI GIAN
        // =====================================================

        JPanel extendPanel = new JPanel(new BorderLayout());
        extendPanel.setBackground(Color.WHITE);
        extendPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Gia hạn thêm thời gian"
                )
        );

        JPanel btnTimePanel = new JPanel(
                new GridLayout(1, 4, 12, 0)
        );
        btnTimePanel.setBackground(Color.WHITE);

        btn1H = createTimeButton("+1 Giờ");
        btn2H = createTimeButton("+2 Giờ");
        btn3H = createTimeButton("+3 Giờ");

        cbThemGio = new JComboBox<>(
                new String[]{
                        "Chọn thời gian thêm",
                        "30 phút",
                        "1 giờ",
                        "2 giờ",
                        "3 giờ",
                        "5 giờ"
                }
        );

        cbThemGio.setPreferredSize(
                new Dimension(180, 40)
        );

        btnTimePanel.add(btn1H);
        btnTimePanel.add(btn2H);
        btnTimePanel.add(btn3H);
        btnTimePanel.add(cbThemGio);

        extendPanel.add(
                btnTimePanel,
                BorderLayout.CENTER
        );

        mainPanel.add(extendPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // =====================================================
        // PHÍ GIA HẠN
        // =====================================================

        JPanel feePanel = new JPanel(new GridBagLayout());
        feePanel.setBackground(Color.WHITE);
        feePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Chi phí gia hạn"
                )
        );

        GridBagConstraints fee = new GridBagConstraints();
        fee.insets = new Insets(10, 12, 10, 12);
        fee.fill = GridBagConstraints.HORIZONTAL;
        fee.weightx = 1;

        fee.gridx = 0;
        fee.gridy = 0;
        feePanel.add(new JLabel("Phí gia hạn"), fee);

        fee.gridx = 1;
        feePanel.add(txtPhiGiaHan, fee);

        mainPanel.add(feePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // =====================================================
        // FOOTER
        // =====================================================

        JPanel footer = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        12,
                        10
                )
        );
        footer.setBackground(Color.WHITE);

        btnHuy = new Button();
        btnHuy.setText("Hủy");

        btnXacNhan = new Button();
        btnXacNhan.setText("Xác nhận");

        footer.add(btnHuy);
        footer.add(btnXacNhan);

        mainPanel.add(footer);

        // =====================================================
        // EVENT
        // =====================================================

        btnSearch.addActionListener(
                e -> timPhong()
        );

        btn1H.addActionListener(
                e -> giaHanGio(1, 100000)
        );

        btn2H.addActionListener(
                e -> giaHanGio(2, 200000)
        );

        btn3H.addActionListener(
                e -> giaHanGio(3, 300000)
        );

        btnXacNhan.addActionListener(
                e -> xacNhanGiaHan()
        );

        btnHuy.addActionListener(
                e -> clearForm()
        );
    }

    // TÌM PHÒNG

    private void timPhong() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã phòng hoặc SĐT khách!"
            );
            return;
        }

        Object[] data =
                phongDao.getThongTinGiaHanPhong(keyword);

        if (data == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phòng đang thuê!"
            );

            clearForm();
            return;
        }

        // =====================================================
        // GÁN DỮ LIỆU LÊN UI
        // =====================================================

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

        // lưu thời gian hiện tại để cộng thêm giờ

        thoiGianTraHienTai =
                (Timestamp) data[4];

        thoiGianTraMoi =
                (Timestamp) data[4];

        txtPhiGiaHan.setText("0");
    }

    // =====================================================
    // GIA HẠN GIỜ
    // =====================================================

    private void giaHanGio(
            int gio,
            double phi
    ) {

        if (thoiGianTraHienTai == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng tìm phòng trước!"
            );
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(thoiGianTraHienTai);

        cal.add(Calendar.HOUR_OF_DAY, gio);

        thoiGianTraMoi = new Timestamp(
                cal.getTimeInMillis()
        );

        hienThiGiaHan(phi);
    }

    // =====================================================
    // HIỂN THỊ GIA HẠN
    // =====================================================

    private void hienThiGiaHan(double phi) {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                );

        txtThoiGianTraMoi.setText(
                sdf.format(thoiGianTraMoi)
        );

        txtPhiGiaHan.setText(
                String.valueOf(phi)
        );
    }

    // =====================================================
    // XÁC NHẬN
    // =====================================================

    // =====================================================
// XÁC NHẬN GIA HẠN
// =====================================================

    private void xacNhanGiaHan() {

        String maPhong =
                txtMaPhong.getText().trim();

        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng tìm phòng trước!"
            );
            return;
        }

        if (thoiGianTraMoi == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn thời gian gia hạn!"
            );
            return;
        }

        boolean result =
                ctHoaDonDao.updateThoiGianTra(
                        maPhong,
                        thoiGianTraMoi
                );

        if (!result) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gia hạn phòng thất bại!"
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Gia hạn phòng thành công!"
        );

        // cập nhật lại thời gian hiện tại

        txtThoiGianTraCu.setText(
                txtThoiGianTraMoi.getText()
        );

        thoiGianTraHienTai =
                thoiGianTraMoi;
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================

    private void clearForm() {

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
    // COMPONENT
    // =====================================================

    private TextField createReadOnlyField() {

        TextField tf = new TextField();

        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.BLACK);

        tf.setPreferredSize(
                new Dimension(230, 40)
        );

        return tf;
    }

    private Button createTimeButton(String text) {

        Button btn = new Button();

        btn.setText(text);

        btn.setPreferredSize(
                new Dimension(120, 40)
        );

        return btn;
    }

    // =====================================================
    // MAIN TEST
    // =====================================================

    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setTitle("Gia hạn phòng");
        f.setSize(1100, 750);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        f.add(new GiaHanPhong_UI());
        f.setVisible(true);
    }
}