package GUI;

import Dao.PhongDao;
import Entity.Phong;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class Search_Phong_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;

    // Ô số liệu chi tiết
    private TextField txtMaPhong;
    private TextField txtTang;
    private TextField txtLoaiPhong;
    private TextField txtTrangThai;
    private TextField txtTenKhach;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtNgayNhan;
    private TextField txtNgayTra;

    // Hệ màu Material Design Modern UX đồng bộ toàn hệ thống
    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);   // Xanh thương hiệu
    private final Color COLOR_DARK        = new Color(30, 41, 59);     // Chữ chính (Charcoal)
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);    // Chữ phụ / Nhãn
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);  // Nền card nhẹ tinh tế
    private final Color COLOR_BORDER      = new Color(226, 232, 240);  // Đường viền mảnh phẳng

    public Search_Phong_UI() {

        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Đệm biên rộng giúp giao diện thoáng đãng

        // =================================================
        // 1. TOP SECTION: HEADER & SEARCH BAR
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRA CỨU PHÒNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topPanel.add(title, BorderLayout.NORTH);

        // Thanh công cụ tìm kiếm thiết kế phẳng (Premium Search Toolbar)
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchBar.setBackground(COLOR_BG_CARD);
        searchBar.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        JLabel lblSearch = new JLabel("Mã phòng:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(COLOR_TEXT_MAIN);
        searchBar.add(lblSearch);

        tfSearch = new TextField();
        tfSearch.setPreferredSize(new Dimension(300, 35));
        tfSearch.setHint("Nhập mã phòng cần tra cứu...");
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

        JLabel lblLeft = new JLabel("DANH SÁCH PHÒNG KHÁCH SẠN");
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(COLOR_TEXT_MAIN);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã phòng",
                        "Tầng",
                        "Loại",
                        "Trạng thái"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống nhấp đúp chỉnh sửa ô trực tiếp gây lag
            }
        };

        table = new JTable(model);
        table.setRowHeight(42); // Hàng cao thoáng ráo chuẩn hiện đại
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 255)); // Đổ nền màu xanh pastel dịu nhẹ khi chọn dòng
        table.setSelectionForeground(COLOR_DARK);
        table.setShowGrid(false); // Triệt tiêu đường lưới chia ô ô caro thô kệch
        table.setIntercellSpacing(new Dimension(0, 0));

        // Bật chế độ chỉ bôi đen nguyên hàng mượt mà
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        leftPanel.add(scrollPane, BorderLayout.CENTER);
        mainContentGrid.add(leftPanel);

        // --- BÊN PHẢI: KHỐI CARD CHI TIẾT SANG TRỌNG ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);

        JLabel lblRight = new JLabel("THÔNG TIN CHI TIẾT PHÒNG TÌM KIẾM");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(COLOR_TEXT_MAIN);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        // Khối Card bao bọc toàn bộ form dọc gọn gàng chống co giãn méo lệch ô
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(COLOR_BG_CARD);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JPanel formGrid = new JPanel(new GridLayout(5, 2, 20, 14)); // Phân bổ 2 cột cân xứng song song
        formGrid.setBackground(COLOR_BG_CARD);

        txtMaPhong = createField();
        txtTang = createField();
        txtLoaiPhong = createField();
        txtTrangThai = createField();
        txtTenKhach = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtNgayNhan = createField();
        txtNgayTra = createField();

        applyFlatTextField(txtMaPhong);
        applyFlatTextField(txtTang);
        applyFlatTextField(txtLoaiPhong);
        applyFlatTextField(txtTrangThai);
        applyFlatTextField(txtTenKhach);
        applyFlatTextField(txtSDT);
        applyFlatTextField(txtCCCD);
        applyFlatTextField(txtNgayNhan);
        applyFlatTextField(txtNgayTra);

        formGrid.add(createFieldGroup("Mã số phòng", txtMaPhong));
        formGrid.add(createFieldGroup("Vị trí tầng", txtTang));
        formGrid.add(createFieldGroup("Loại hình phòng", txtLoaiPhong));
        formGrid.add(createFieldGroup("Trạng thái hiện tại", txtTrangThai));
        formGrid.add(createFieldGroup("Tên khách đang thuê", txtTenKhach));
        formGrid.add(createFieldGroup("Số điện thoại khách", txtSDT));
        formGrid.add(createFieldGroup("Căn cước công dân (CCCD)", txtCCCD));
        formGrid.add(createFieldGroup("Thời gian nhận phòng", txtNgayNhan));
        formGrid.add(createFieldGroup("Thời gian trả phòng dự kiến", txtNgayTra));

        // Ô trống bổ sung để giữ lưới GridLayout vuông vắn
        formGrid.add(new JPanel() {{ setBackground(COLOR_BG_CARD); }});

        formWrapper.add(formGrid, BorderLayout.NORTH);
        rightPanel.add(formWrapper, BorderLayout.CENTER);
        mainContentGrid.add(rightPanel);

        // =====================================================
        // INTERACTION & LOGIC CONTROL (GIỮ NGUYÊN HOÀN TOÀN)
        // =====================================================
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
                clearForm();
                JOptionPane.showMessageDialog(this, "Không tìm thấy phòng!");
            } else {
                table.setRowSelectionInterval(0, 0);
                hienThiChiTietDongSelected(0);
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                hienThiChiTietDongSelected(row);
            }
        });

        // ===== LOAD DANH SÁCH =====
        loadAllPhong();
    }

    private void hienThiChiTietDongSelected(int row) {
        txtMaPhong.setText(model.getValueAt(row, 0).toString());
        txtTang.setText(model.getValueAt(row, 1).toString());
        txtLoaiPhong.setText(model.getValueAt(row, 2).toString());
        txtTrangThai.setText(model.getValueAt(row, 3).toString());

        String maPhong = model.getValueAt(row, 0).toString();
        Object[] info = new PhongDao().getThongTinPhongDangThue(maPhong);

        if (info != null) {
            txtTenKhach.setText(info[0] != null ? info[0].toString() : "");
            txtSDT.setText(info[1] != null ? info[1].toString() : "");
            txtCCCD.setText(info[2] != null ? info[2].toString() : "");
            txtNgayNhan.setText(info[5] != null ? info[5].toString() : "");
            txtNgayTra.setText(info[6] != null ? info[6].toString() : "");
        } else {
            txtTenKhach.setText("");
            txtSDT.setText("");
            txtCCCD.setText("");
            txtNgayNhan.setText("");
            txtNgayTra.setText("");
        }
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
        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            hienThiChiTietDongSelected(0);
        }
    }

    private void clearForm() {
        txtMaPhong.setText(""); txtTang.setText(""); txtLoaiPhong.setText(""); txtTrangThai.setText("");
        txtTenKhach.setText(""); txtSDT.setText(""); txtCCCD.setText(""); txtNgayNhan.setText(""); txtNgayTra.setText("");
    }

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

    private JPanel createFieldGroup(String labelText, TextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(COLOR_BG_CARD);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private TextField createField() {
        TextField tf = new TextField();
        tf.setEditable(false);
        return tf;
    }
}