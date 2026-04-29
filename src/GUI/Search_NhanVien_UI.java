package GUI;

import Dao.NhanVienDao;
import Entity.NhanVien;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Search_NhanVien_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;

    private TextField txtMaNV;
    private TextField txtHoTen;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtGioiTinh;
    private TextField txtChucVu;
    private TextField txtTrangThai;
    private TextField txtDiaChi;

    public Search_NhanVien_UI() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRA CỨU NHÂN VIÊN");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(new Color(0,153,255));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== BODY =====
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(new EmptyBorder(10,10,10,10));
        body.setBackground(Color.WHITE);
        add(body, BorderLayout.CENTER);

        // ===== SEARCH =====
        JPanel search = new JPanel();
        search.setBackground(Color.WHITE);
        search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));

        tfSearch = new TextField();
        tfSearch.setPreferredSize(new Dimension(300,40));
        tfSearch.setHint("Nhập tên / SĐT / CCCD");

        JComboBox<String> cbChucVu = new JComboBox<>(new String[]{
                "Tất cả chức vụ", "Quản lý", "Lễ tân"
        });

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");

        search.add(tfSearch);
        search.add(Box.createHorizontalStrut(10));
        search.add(cbChucVu);
        search.add(Box.createHorizontalStrut(10));
        search.add(btnSearch);

        body.add(search, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1,2,10,0));
        body.add(main, BorderLayout.CENTER);

        // ===== LEFT TABLE =====
        JPanel left = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel("DANH SÁCH NHÂN VIÊN");
        lbl.setFont(new Font("Tahoma", Font.BOLD,16));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(lbl, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã NV",
                        "Họ tên",
                        "SĐT",
                        "Chức vụ",
                        "Trạng thái"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        left.add(scrollPane, BorderLayout.CENTER);

        main.add(left);

        // ===== RIGHT DETAIL =====
        JPanel right = new JPanel(new BorderLayout());

        JLabel lbl2 = new JLabel("CHI TIẾT NHÂN VIÊN");
        lbl2.setFont(new Font("Tahoma", Font.BOLD,16));
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(lbl2, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        form.setBorder(new EmptyBorder(10,10,10,10));
        form.setBackground(Color.WHITE);

        txtMaNV = createField();
        txtHoTen = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtGioiTinh = createField();
        txtChucVu = createField();
        txtTrangThai = createField();
        txtDiaChi = createField();

        form.add(new JLabel("Mã nhân viên"));
        form.add(txtMaNV);

        form.add(new JLabel("Họ tên"));
        form.add(txtHoTen);

        form.add(new JLabel("SĐT"));
        form.add(txtSDT);

        form.add(new JLabel("CCCD"));
        form.add(txtCCCD);

        form.add(new JLabel("Giới tính"));
        form.add(txtGioiTinh);

        form.add(new JLabel("Chức vụ"));
        form.add(txtChucVu);

        form.add(new JLabel("Trạng thái"));
        form.add(txtTrangThai);

        form.add(new JLabel("Địa chỉ Email"));
        form.add(txtDiaChi);

        right.add(form, BorderLayout.CENTER);

        main.add(right);

        // ===== LOAD DATA =====
        loadData();

        // ===== CLICK TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                String maNV = model.getValueAt(row, 0).toString();

                NhanVien nv = new NhanVienDao().getById(maNV);

                if (nv != null) {

                    txtMaNV.setText(nv.getMaNV());
                    txtHoTen.setText(nv.getHoTen());
                    txtSDT.setText(nv.getSdt());
                    txtCCCD.setText(nv.getCccd());

                    txtGioiTinh.setText(
                            nv.getGioiTinh() == 1 ? "Nam" : "Nữ"
                    );

                    txtChucVu.setText(
                            nv.getChucVu().getTenChucVu()
                    );

                    txtTrangThai.setText(
                            nv.getTrangThaiLamViec()
                    );

                    txtDiaChi.setText(
                            nv.getEmail()
                    );
                }
            }
        });

        // ===== SEARCH =====
        btnSearch.addActionListener(e -> {

            String keyword = tfSearch.getText().trim();

            model.setRowCount(0);

            java.util.List<NhanVien> list =
                    new NhanVienDao().searchNhanVien(keyword);

            if (keyword.isEmpty()) {
                list = new NhanVienDao().getAll();
            }

            for (NhanVien nv : list) {

                model.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getHoTen(),
                        nv.getSdt(),
                        nv.getChucVu().getTenChucVu(),
                        nv.getTrangThaiLamViec()
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy nhân viên!"
                );
            }
        });
    }

    // ===== LOAD DANH SÁCH =====
    private void loadData() {

        model.setRowCount(0);

        List<NhanVien> list = new NhanVienDao().getAll();

        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    nv.getSdt(),
                    nv.getChucVu().getTenChucVu(),
                    nv.getTrangThaiLamViec()
            });
        }
    }

    // ===== COMPONENT =====
    private TextField createField() {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200,35));
        tf.setEditable(false);
        tf.setBackground(new Color(245,245,245));
        return tf;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1100,650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Search_NhanVien_UI());
        f.setVisible(true);
    }
}