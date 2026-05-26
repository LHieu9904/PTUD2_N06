package GUI;

import Dao.TaiKhoanDao;
import Entity.TaiKhoan;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

    // Hệ màu Material Design Modern UX đồng bộ toàn bộ hệ thống
    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);   // Xanh thương hiệu
    private final Color COLOR_DARK        = new Color(30, 41, 59);     // Chữ chính (Charcoal)
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);    // Chữ phụ / Nhãn
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);  // Nền card nhẹ tinh tế
    private final Color COLOR_BORDER      = new Color(226, 232, 240);  // Đường viền mảnh phẳng

    public Search_TaiKhoan_UI() {

        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Đệm biên rộng giúp giao diện thoáng đãng

        // =================================================
        // 1. TOP SECTION: HEADER & SEARCH BAR
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRA CỨU TÀI KHOẢN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topPanel.add(title, BorderLayout.NORTH);

        // Thanh công cụ tìm kiếm thiết kế phẳng (Premium Search Toolbar)
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchBar.setBackground(COLOR_BG_CARD);
        searchBar.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(COLOR_TEXT_MAIN);
        searchBar.add(lblSearch);

        tfSearch = new TextField();
        tfSearch.setPreferredSize(new Dimension(350, 35));
        tfSearch.setHint("Nhập username / tên nhân viên...");
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchBar.add(tfSearch);

        Button btnSearch = new Button();
        btnSearch.setText("Tìm Kiếm");
        btnSearch.setBackground(COLOR_PRIMARY);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBar.add(btnSearch);

        topPanel.add(searchBar, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // =================================================
        // 2. MAIN CONTENT GRAPHICS (GRID 1x2)
        // =================================================
        JPanel mainContentGrid = new JPanel(new GridLayout(1, 2, 24, 0));
        mainContentGrid.setBackground(Color.WHITE);
        add(mainContentGrid, BorderLayout.CENTER);

        // --- BÊN TRÁI: DỮ LIỆU BẢNG PHẲNG KHÔNG LAG ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblLeft = new JLabel("DANH SÁCH TÀI KHOẢN HỆ THỐNG");
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(COLOR_TEXT_MAIN);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Username",
                        "Nhân viên",
                        "Chức vụ",
                        "Trạng thái"
                }, 0
        );

        // Khóa tính năng nhấp đúp chỉnh sửa text trực tiếp trên ô để triệt tiêu lag
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(42); // Hàng cao thoáng ráo
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 255)); // Nền xanh pastel dịu khi select
        table.setSelectionForeground(COLOR_DARK);
        table.setShowGrid(false); // Triệt tiêu đường lưới chia ô truyền thống
        table.setIntercellSpacing(new Dimension(0, 0));

        // Ép chế độ click bôi đen nguyên hàng mượt mà
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // Thiết kế Header Table thanh lịch
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(COLOR_BG_CARD);
        th.setForeground(COLOR_DARK);
        th.setPreferredSize(new Dimension(100, 42));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        leftPanel.add(scrollPane, BorderLayout.CENTER);
        mainContentGrid.add(leftPanel);

        // --- BÊN PHẢI: KHỐI CARD CHI TIẾT SANG TRỌNG ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);

        JLabel lblRight = new JLabel("THÔNG TIN CHI TIẾT TÀI KHOẢN");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(COLOR_TEXT_MAIN);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        // Khối Card độc lập bao bọc toàn bộ form dọc gọn gàng
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(COLOR_BG_CARD);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        // Khởi tạo các ô Textfield
        txtUsername = createField();
        txtMatKhau = createField();
        txtNhanVien = createField();
        txtChucVu = createField();
        txtTrangThai = createField();
        txtNgayTao = createField();

        // Áp dụng định dạng UI phẳng cho TextField
        applyFlatTextField(txtUsername);
        applyFlatTextField(txtMatKhau);
        applyFlatTextField(txtNhanVien);
        applyFlatTextField(txtChucVu);
        applyFlatTextField(txtTrangThai);
        applyFlatTextField(txtNgayTao);

        // Sắp xếp các cụm Nhãn - Ô chữ theo chiều dọc vuông vức, không bị dẹt méo
        formContainer.add(createFieldGroup("Tên đăng nhập (Username)", txtUsername));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Mật khẩu truy cập", txtMatKhau));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Mã nhân viên sở hữu", txtNhanVien));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Chức vụ hệ thống", txtChucVu));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Trạng thái hoạt động", txtTrangThai));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Mã số tài khoản", txtNgayTao));

        // Đẩy toàn bộ cấu trúc lên trên cùng để form luôn giữ kích thước nhỏ, vừa vặn
        formContainer.add(Box.createVerticalGlue());

        rightPanel.add(formContainer, BorderLayout.CENTER);
        mainContentGrid.add(rightPanel);

        // ===== LOAD DATA =====
        loadData();

        // =================================================
        // INTERACTION & LOGIC CONTROL (GIỮ NGUYÊN)
        // =================================================
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) return;

                String username = model.getValueAt(row, 0).toString();
                TaiKhoan tk = new TaiKhoanDao().getByUsername(username);

                if (tk != null) {
                    txtUsername.setText(tk.getTenDangNhap());
                    txtMatKhau.setText(tk.getMatKhau());
                    txtNhanVien.setText(tk.getMaNV());
                    txtChucVu.setText(tk.getMaChucVu());
                    txtTrangThai.setText(tk.getTrangThai());
                    txtNgayTao.setText(tk.getMaTK()); // Tạm dùng MaTK hiển thị ở ô mã tài khoản
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            model.setRowCount(0);
            List<TaiKhoan> list;

            if (keyword.isEmpty()) {
                list = new TaiKhoanDao().getAll();
            } else {
                list = new TaiKhoanDao().searchTaiKhoan(keyword);
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
                clearForm();
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản tương thích!");
            } else {
                // Tự động chọn dòng đầu tiên sau khi tìm kiếm để lấp đầy form chi tiết
                table.setRowSelectionInterval(0, 0);
                String userDau = model.getValueAt(0, 0).toString();
                TaiKhoan tkDau = new TaiKhoanDao().getByUsername(userDau);
                if (tkDau != null) {
                    txtUsername.setText(tkDau.getTenDangNhap());
                    txtMatKhau.setText(tkDau.getMatKhau());
                    txtNhanVien.setText(tkDau.getMaNV());
                    txtChucVu.setText(tkDau.getMaChucVu());
                    txtTrangThai.setText(tkDau.getTrangThai());
                    txtNgayTao.setText(tkDau.getMaTK());
                }
            }
        });
    }

    // ===== LOAD DATA TỪ DAO =====
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
        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void clearForm() {
        txtUsername.setText(""); txtMatKhau.setText(""); txtNhanVien.setText("");
        txtChucVu.setText(""); txtTrangThai.setText(""); txtNgayTao.setText("");
    }

    // Định dạng cấu trúc phẳng mịn cho TextField hiển thị thông tin chi tiết
    private void applyFlatTextField(TextField tf) {
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(COLOR_DARK);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
    }

    // Hàm bao bọc cặp Nhãn xếp dọc trên Trường dữ liệu (Ngăn kéo dẹt méo ô chữ)
    private JPanel createFieldGroup(String labelText, TextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(COLOR_BG_CARD);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);

        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 58));
        return p;
    }

    // ===== KHỞI TẠO COMPONENT =====
    private TextField createField() {
        TextField tf = new TextField();
        tf.setEditable(false);
        return tf;
    }
}