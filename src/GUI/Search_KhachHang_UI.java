package GUI;

import Dao.KhachHangDao;
import Entity.KhachHang;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Search_KhachHang_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;

    private TextField txtMaKH;
    private TextField txtHoTen;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtGioiTinh;

    public Search_KhachHang_UI() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRA CỨU KHÁCH HÀNG");
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
        tfSearch.setHint("Nhập tên / SĐT / CCCD / Mã KH");

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");

        search.add(tfSearch);
        search.add(Box.createHorizontalStrut(10));
        search.add(btnSearch);

        body.add(search, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new GridLayout(1,2,10,0));
        body.add(main, BorderLayout.CENTER);

        // ===== LEFT TABLE =====
        JPanel left = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel("KẾT QUẢ");
        lbl.setFont(new Font("Tahoma", Font.BOLD,16));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(lbl, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã KH",
                        "Họ tên",
                        "SĐT",
                        "CCCD"
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
        // Giữ ô text nhỏ nhưng trải đều phần RIGHT, không dồn vào giữa

// ===== RIGHT DETAIL =====

        JPanel right = new JPanel(new BorderLayout());

        JLabel lbl2 = new JLabel("CHI TIẾT KHÁCH HÀNG");
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(lbl2, BorderLayout.NORTH);

// dùng GridBagLayout để giãn đều theo chiều dọc
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10); // giãn đều theo chiều dọc
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

// tạo field
        txtMaKH = createField();
        txtHoTen = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtGioiTinh = createField();

// giữ ô text nhỏ
        Dimension smallField = new Dimension(220, 28);

        txtMaKH.setPreferredSize(smallField);
        txtHoTen.setPreferredSize(smallField);
        txtSDT.setPreferredSize(smallField);
        txtCCCD.setPreferredSize(smallField);
        txtGioiTinh.setPreferredSize(smallField);

// cho cột phải giãn hợp lý
        gbc.weightx = 1;

// ===== Dòng 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Mã khách"), gbc);

        gbc.gridx = 1;
        form.add(txtMaKH, gbc);

// ===== Dòng 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Họ tên"), gbc);

        gbc.gridx = 1;
        form.add(txtHoTen, gbc);

// ===== Dòng 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Số điện thoại"), gbc);

        gbc.gridx = 1;
        form.add(txtSDT, gbc);

// ===== Dòng 4
        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(new JLabel("CCCD"), gbc);

        gbc.gridx = 1;
        form.add(txtCCCD, gbc);

// ===== Dòng 5
        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(new JLabel("Giới tính"), gbc);

        gbc.gridx = 1;
        form.add(txtGioiTinh, gbc);

// đẩy form phủ đều chiều cao phần right
        gbc.gridy = 5;
        gbc.weighty = 1;
        form.add(Box.createVerticalGlue(), gbc);

        right.add(form, BorderLayout.CENTER);
        main.add(right);

        // ===== LOAD DATA =====
        loadData();

        // ===== CLICK TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if (row == -1) return;

                String maKH = model.getValueAt(row, 0).toString();

                KhachHang kh = new KhachHangDao().getById(maKH);

                if (kh != null) {

                    txtMaKH.setText(kh.getMaKH());
                    txtHoTen.setText(kh.getHoTen());
                    txtSDT.setText(kh.getSdt());
                    txtCCCD.setText(kh.getCccd());

                    txtGioiTinh.setText(
                            kh.getGioiTinh() == 1 ? "Nam" : "Nữ"
                    );


                }
            }
        });

        // ===== SEARCH =====
        btnSearch.addActionListener(e -> {

            String keyword = tfSearch.getText().trim();

            model.setRowCount(0);

            List<KhachHang> list;

            if (keyword.isEmpty()) {
                list = new KhachHangDao().getAll();
            } else {
                list = new KhachHangDao()
                        .searchKhachHang(keyword);
            }

            for (KhachHang kh : list) {

                model.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getSdt(),
                        kh.getCccd()
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy khách hàng!"
                );
            }
        });
    }

    // ===== LOAD DATA =====
    private void loadData() {

        model.setRowCount(0);

        List<KhachHang> list = new KhachHangDao().getAll();

        for (KhachHang kh : list) {

            model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getSdt(),
                    kh.getCccd()
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
        f.add(new Search_KhachHang_UI());
        f.setVisible(true);
    }
}