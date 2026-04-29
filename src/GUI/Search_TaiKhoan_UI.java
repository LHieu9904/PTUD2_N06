package GUI;

import Dao.TaiKhoanDao;
import Entity.TaiKhoan;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Search_TaiKhoan_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;

    private TextField txtUsername;
    private TextField txtMatKhau;
    private TextField txtNhanVien;
    private TextField txtChucVu;
    private TextField txtTrangThai;
    private TextField txtNgayTao;

    public Search_TaiKhoan_UI() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRA CỨU TÀI KHOẢN");
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
        tfSearch.setHint("Nhập username / tên nhân viên");

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");

        search.add(tfSearch);
        search.add(Box.createHorizontalStrut(10));
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
                        "Username",
                        "Nhân viên",
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
        // Chỉ sửa phần RIGHT để ô text xám nhỏ lại giống bên khách hàng :contentReference[oaicite:0]{index=0}

// ===== RIGHT DETAIL =====

        // Trải đều phần RIGHT, ô text không dồn vào giữa và vẫn nhỏ :contentReference[oaicite:0]{index=0}

// ===== RIGHT DETAIL =====

        JPanel right = new JPanel(new BorderLayout());

        JLabel lbl2 = new JLabel("CHI TIẾT TÀI KHOẢN");
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl2.setHorizontalAlignment(SwingConstants.CENTER);

        right.add(lbl2, BorderLayout.NORTH);

// GridBagLayout để giãn đều toàn bộ chiều cao
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

// khoảng cách đều giữa các dòng
        gbc.insets = new Insets(18, 10, 18, 10);

// tạo field
        txtUsername = createField();
        txtMatKhau = createField();
        txtNhanVien = createField();
        txtChucVu = createField();
        txtTrangThai = createField();
        txtNgayTao = createField();

// giữ ô text nhỏ
        Dimension smallField = new Dimension(220, 28);

        txtUsername.setPreferredSize(smallField);
        txtMatKhau.setPreferredSize(smallField);
        txtNhanVien.setPreferredSize(smallField);
        txtChucVu.setPreferredSize(smallField);
        txtTrangThai.setPreferredSize(smallField);
        txtNgayTao.setPreferredSize(smallField);

// cho cột text giãn hợp lý
        gbc.weightx = 1;

// ===== Dòng 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username"), gbc);

        gbc.gridx = 1;
        form.add(txtUsername, gbc);

// ===== Dòng 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Mật khẩu"), gbc);

        gbc.gridx = 1;
        form.add(txtMatKhau, gbc);

// ===== Dòng 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Mã Nhân viên"), gbc);

        gbc.gridx = 1;
        form.add(txtNhanVien, gbc);

// ===== Dòng 4
        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(new JLabel("Chức vụ"), gbc);

        gbc.gridx = 1;
        form.add(txtChucVu, gbc);

// ===== Dòng 5
        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(new JLabel("Trạng thái"), gbc);

        gbc.gridx = 1;
        form.add(txtTrangThai, gbc);

// ===== Dòng 6
        gbc.gridx = 0;
        gbc.gridy = 5;
        form.add(new JLabel("Mã Tài Khoản"), gbc);

        gbc.gridx = 1;
        form.add(txtNgayTao, gbc);

// dòng cuối để đẩy form phủ đều toàn bộ chiều cao RIGHT
        gbc.gridy = 6;
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

                String username = model.getValueAt(row, 0).toString();

                TaiKhoan tk = new TaiKhoanDao()
                        .getByUsername(username);

                if (tk != null) {

                    txtUsername.setText(tk.getTenDangNhap());
                    txtMatKhau.setText(tk.getMatKhau());
                    txtNhanVien.setText(tk.getMaNV());
                    txtChucVu.setText(tk.getMaChucVu());
                    txtTrangThai.setText(tk.getTrangThai());

                    // tạm dùng MaTK hiển thị ở ô ngày tạo
                    txtNgayTao.setText(tk.getMaTK());
                }
            }
        });

        // ===== SEARCH =====
        btnSearch.addActionListener(e -> {

            String keyword = tfSearch.getText().trim();

            model.setRowCount(0);

            List<TaiKhoan> list;

            if (keyword.isEmpty()) {
                list = new TaiKhoanDao().getAll();
            } else {
                list = new TaiKhoanDao()
                        .searchTaiKhoan(keyword);
            }

            for (TaiKhoan tk : list) {

                model.addRow(new Object[]{
                        tk.getTenDangNhap(),
                        tk.getMaNV(),
                        tk.getMaChucVu(),
                        tk.getTrangThai()
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy tài khoản!"
                );
            }
        });
    }

    // ===== LOAD DATA =====
    private void loadData() {

        model.setRowCount(0);

        List<TaiKhoan> list = new TaiKhoanDao().getAll();

        for (TaiKhoan tk : list) {

            model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getMaNV(),
                    tk.getMaChucVu(),
                    tk.getTrangThai()
            });
        }
    }

    // ===== COMPONENT =====
    private TextField createField() {
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setEditable(false);
        tf.setBackground(new Color(245, 245, 245));
        return tf;
    }

    // ===== TEST =====
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1100, 650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Search_TaiKhoan_UI());
        f.setVisible(true);
    }
}