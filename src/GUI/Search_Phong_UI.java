package GUI;

import Dao.PhongDao;
import Entity.Phong;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Search_Phong_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;

    // ô text chi tiết
    private TextField txtMaPhong;
    private TextField txtTang;
    private TextField txtLoaiPhong;
    private TextField txtTrangThai;
    private TextField txtTenKhach;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtNgayNhan;
    private TextField txtNgayTra;

    public Search_Phong_UI() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRA CỨU PHÒNG");
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

        tfSearch = new TextField();
        tfSearch.setPreferredSize(new Dimension(300, 40));
        tfSearch.setHint("Nhập mã phòng");

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");

        btnSearch.addActionListener(e -> {

            String maPhong = tfSearch.getText().trim();

            model.setRowCount(0);

            List<Phong> list;

            if (maPhong.isEmpty()) {
                list = new PhongDao().getAllPhong();
            } else {
                list = new PhongDao().searchPhongByMa(maPhong);
            }

            for (Phong p : list) {
                model.addRow(new Object[]{
                        p.getMaPhong(),
                        p.getTang(),
                        p.getLoaiPhong().getTenLP(),
                        p.getTrangThai()
                });
            }

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy phòng!"
                );
            }
        });

        search.add(tfSearch);
        search.add(Box.createHorizontalStrut(10));
        search.add(btnSearch);

        body.add(search, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1, 2, 10, 0));
        body.add(main, BorderLayout.CENTER);

        // ===== LEFT TABLE =====
        JPanel left = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel("DANH SÁCH PHÒNG");
        lbl.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(lbl, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã phòng",
                        "Tầng",
                        "Loại",
                        "Trạng thái",

                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(35);

        // ===== THANH CUỘN TABLE =====
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        left.add(scrollPane, BorderLayout.CENTER);

        main.add(left);

        // ===== RIGHT DETAIL =====
        JPanel right = new JPanel(new BorderLayout());

        JLabel lbl2 = new JLabel("CHI TIẾT PHÒNG");
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(lbl2, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.setBackground(Color.WHITE);

        txtMaPhong = createField();
        txtTang = createField();
        txtLoaiPhong = createField();
        txtTrangThai = createField();
        txtTenKhach = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtNgayNhan = createField();
        txtNgayTra = createField();

        form.add(new JLabel("Mã phòng"));
        form.add(txtMaPhong);

        form.add(new JLabel("Tầng"));
        form.add(txtTang);

        form.add(new JLabel("Loại phòng"));
        form.add(txtLoaiPhong);

        form.add(new JLabel("Trạng thái"));
        form.add(txtTrangThai);

        form.add(new JLabel("Tên khách"));
        form.add(txtTenKhach);

        form.add(new JLabel("SĐT"));
        form.add(txtSDT);

        form.add(new JLabel("CCCD"));
        form.add(txtCCCD);

        form.add(new JLabel("Ngày nhận"));
        form.add(txtNgayNhan);

        form.add(new JLabel("Ngày trả"));
        form.add(txtNgayTra);

        right.add(form, BorderLayout.CENTER);

        main.add(right);

        // ===== CLICK TABLE → ĐỔ DỮ LIỆU =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                txtMaPhong.setText(
                        model.getValueAt(row, 0).toString()
                );

                txtTang.setText(
                        model.getValueAt(row, 1).toString()
                );

                txtLoaiPhong.setText(
                        model.getValueAt(row, 2).toString()
                );

                txtTrangThai.setText(
                        model.getValueAt(row, 3).toString()
                );

                // tạm thời chưa lấy từ DB
                String maPhong = model.getValueAt(row, 0).toString();

                Object[] info = new PhongDao()
                        .getThongTinPhongDangThue(maPhong);

                if (info != null) {

                    txtTenKhach.setText(
                            info[0] != null ? info[0].toString() : ""
                    );

                    txtSDT.setText(
                            info[1] != null ? info[1].toString() : ""
                    );

                    txtCCCD.setText(
                            info[2] != null ? info[2].toString() : ""
                    );

                    txtNgayNhan.setText(
                            info[4] != null ? info[4].toString() : ""
                    );

                    txtNgayTra.setText(
                            info[5] != null ? info[5].toString() : ""
                    );

                } else {

                    txtTenKhach.setText("");
                    txtSDT.setText("");
                    txtCCCD.setText("");
                    txtNgayNhan.setText("");
                    txtNgayTra.setText("");
                }
            }
        });

        // ===== LOAD DANH SÁCH =====
        loadAllPhong();
    }

    private void loadAllPhong() {

        model.setRowCount(0);

        List<Phong> list = new PhongDao().getAllPhong();

        for (Phong p : list) {
            model.addRow(new Object[]{
                    p.getMaPhong(),
                    p.getTang(),
                    p.getLoaiPhong().getTenLP(),
                    p.getTrangThai()
            });
        }
    }

    private TextField createField() {

        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));

        return tf;
    }

    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setSize(1100, 650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Search_Phong_UI());
        f.setVisible(true);
    }
}