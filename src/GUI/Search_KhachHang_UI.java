package GUI;

import Dao.KhachHangDao;
import Entity.KhachHang;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

    // Hệ màu Material Design Modern UX đồng bộ hệ thống
    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);   // Xanh chủ đạo
    private final Color COLOR_DARK        = new Color(30, 41, 59);     // Chữ chính (Charcoal)
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);    // Chữ phụ / Nhãn
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);  // Nền card nhẹ tinh tế
    private final Color COLOR_BORDER      = new Color(226, 232, 240);  // Đường viền mảnh phẳng

    public Search_KhachHang_UI() {

        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Đệm biên rộng giúp giao diện thoáng đãng

        // =================================================
        // 1. TOP SECTION: HEADER & SEARCH BAR
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRA CỨU KHÁCH HÀNG");
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
        tfSearch.setHint("Nhập tên / SĐT / CCCD / Mã KH...");
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

        // --- BÊN TRÁI: DỮ LIỆU BẢNG PHẲNG ---
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblLeft = new JLabel("DANH SÁCH KẾT QUẢ TÌM KIẾM");
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(COLOR_TEXT_MAIN);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã KH",
                        "Họ tên",
                        "SĐT",
                        "CCCD"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(42); // Hàng cao rộng rãi thoáng đãng chuẩn hiện đại
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 255)); // Đổ nền màu xanh pastel dịu nhẹ khi chọn dòng
        table.setSelectionForeground(COLOR_DARK);
        table.setShowGrid(false); // Triệt tiêu đường chia ô caro xấu xí kiểu cũ
        table.setIntercellSpacing(new Dimension(0, 0));

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

        JLabel lblRight = new JLabel("THÔNG TIN CHI TIẾT KHÁCH HÀNG");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(COLOR_TEXT_MAIN);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        // Khối Card bao bọc toàn bộ form, loại bỏ GridLayout co giãn mất kiểm soát
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(COLOR_BG_CARD);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        // Khởi tạo các ô Textfield
        txtMaKH = createField();
        txtHoTen = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtGioiTinh = createField();

        // Gọi hàm định dạng phẳng mượt mà cho các TextField
        applyFlatTextField(txtMaKH);
        applyFlatTextField(txtHoTen);
        applyFlatTextField(txtSDT);
        applyFlatTextField(txtCCCD);
        applyFlatTextField(txtGioiTinh);

        // Sắp xếp các cụm Nhãn - Trường nhập liệu theo chiều dọc vuông vức
        formContainer.add(createFieldGroup("Mã thành viên khách hàng", txtMaKH));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Họ và tên", txtHoTen));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Số điện thoại liên lạc", txtSDT));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Căn cước công dân (CCCD)", txtCCCD));
        formContainer.add(Box.createVerticalStrut(12));
        formContainer.add(createFieldGroup("Giới tính", txtGioiTinh));

        // Đẩy toàn bộ form lên trên cùng, giữ kích cỡ cố định hoàn hảo
        formContainer.add(Box.createVerticalGlue());

        rightPanel.add(formContainer, BorderLayout.CENTER);
        mainContentGrid.add(rightPanel);

        // ===== LOAD DATA =====
        loadData();

        // =================================================
        // INTERACTION & LOGIC CONTROL (GIỮ NGUYÊN LOGIC CŨ)
        // =================================================
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
                    txtGioiTinh.setText(kh.getGioiTinh() == 1 ? "Nam" : "Nữ");
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            model.setRowCount(0);
            List<KhachHang> list;

            if (keyword.isEmpty()) {
                list = new KhachHangDao().getAll();
            } else {
                list = new KhachHangDao().searchKhachHang(keyword);
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
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
            }
        });
    }

    // ===== LOAD DATA TỪ DAO =====
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

    // 🌟 ĐÃ THÊM: Định nghĩa hàm applyFlatTextField bị thiếu khiến chương trình báo lỗi đỏ
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

    // Cấu trúc nhóm trường xếp dọc thanh lịch
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