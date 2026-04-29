package GUI;

import Dao.HoaDonPhongDao;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Search_HoaDon_UI extends JPanel {

    // ===== GLOBAL =====
    private DefaultTableModel model;
    private JTable table;

    private TextField tfNgay;
    private TextField tfMa;

    private TextField txtMaHD;
    private TextField txtNgayLap;
    private TextField txtNhanVien;
    private TextField txtKhachHang;
    private TextField txtTienPhong;
    private TextField txtTienDV;
    private TextField txtTongTien;

    private JTextArea taPhong;
    private JTextArea taDichVu;

    private JRadioButton rHoaDon;
    private JRadioButton rPhieu;

    private Button btnSearch;
    private Button btnSearchNgay;

    public Search_HoaDon_UI() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRA CỨU HÓA ĐƠN");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0, 153, 255));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== BODY =====
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new EmptyBorder(10, 10, 10, 10));
        body.setBackground(Color.WHITE);
        add(body, BorderLayout.CENTER);

        // ===== SEARCH =====
        JPanel search = new JPanel();
        search.setBackground(Color.WHITE);
        search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));

        // ===== TÌM THEO NGÀY =====
        search.add(new JLabel("Ngày:"));

        tfNgay = new TextField();
        tfNgay.setPreferredSize(new Dimension(200, 40));
        tfNgay.setHint("dd/MM/yyyy");

        Button btnCalendar = new Button();
        btnCalendar.setText("📅");

        btnSearchNgay = new Button();
        btnSearchNgay.setText("Tìm");

        search.add(tfNgay);
        search.add(btnCalendar);
        search.add(btnSearchNgay);

        search.add(Box.createHorizontalStrut(30));

        // ===== TÌM THEO MÃ =====
        rHoaDon = new JRadioButton("Hóa đơn", true);
        rPhieu = new JRadioButton("Phiếu đặt");

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
        search.add(tfMa);
        search.add(btnSearch);

        body.add(search, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 0));
        body.add(main, BorderLayout.CENTER);

        // ===== LEFT TABLE =====
        JPanel left = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel("KẾT QUẢ");
        lbl.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(lbl, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã HĐ",
                        "Khách",
                        "Trạng thái",
                        "Tổng tiền"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(35);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        left.add(scroll, BorderLayout.CENTER);
        main.add(left);

        // ===== RIGHT DETAIL =====
        JPanel right = new JPanel(new BorderLayout());

        JLabel lbl2 = new JLabel("CHI TIẾT");
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(lbl2, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.setBackground(Color.WHITE);

        txtMaHD = createField();
        txtNgayLap = createField();
        txtNhanVien = createField();
        txtKhachHang = createField();
        txtTienPhong = createField();
        txtTienDV = createField();
        txtTongTien = createField();

        taPhong = new JTextArea();
        taPhong.setEditable(false);

        taDichVu = new JTextArea();
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

        form.add(new JLabel("Tổng tiền"));
        form.add(txtTongTien);

        right.add(form, BorderLayout.CENTER);
        main.add(right);

        // ===== LOAD =====
        loadData();
        addEvent();
    }

    // ===================================================
    // LOAD DATA
    // ===================================================

    private void loadData() {

        model.setRowCount(0);

        List<Object[]> list =
                new HoaDonPhongDao().getAllHoaDon();

        for (Object[] row : list) {
            model.addRow(row);
        }
    }

    // ===================================================
    // EVENT
    // ===================================================

    private void addEvent() {

        // click row -> đổ chi tiết
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                String maHD =
                        model.getValueAt(row, 0).toString();

                Object[] ct =
                        new HoaDonPhongDao()
                                .getChiTietHoaDon(maHD);

                if (ct != null) {

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

                    txtTongTien.setText(
                            ct[6] != null ? ct[6].toString() : "0"
                    );

                    taPhong.setText("Danh sách phòng");
                    taDichVu.setText("Danh sách dịch vụ");
                }
            }
        });

        // tìm kiếm đơn giản theo mã
        btnSearch.addActionListener(e -> {

            String keyword = tfMa.getText().trim();

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
                    return;
                }
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy hóa đơn!"
            );
        });

        // tìm theo ngày (demo)
        btnSearchNgay.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Chức năng tìm theo ngày sẽ mở rộng sau"
            );
        });
    }

    // ===================================================
    // COMPONENT
    // ===================================================

    private TextField createField() {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));
        return tf;
    }

    // ===================================================
    // TEST
    // ===================================================

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1100, 650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Search_HoaDon_UI());
        f.setVisible(true);
    }
}