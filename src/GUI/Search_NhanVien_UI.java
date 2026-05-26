package GUI;

import Dao.NhanVienDao;
import Entity.NhanVien;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Search_NhanVien_UI extends JPanel {

    // ===== BIẾN TOÀN CỤC =====
    private DefaultTableModel model;
    private JTable table;
    private TextField tfSearch;
    private JComboBox<String> cbChucVu; // Đưa ComboBox ra toàn cục để xử lý lọc nâng cao

    private TextField txtMaNV;
    private TextField txtHoTen;
    private TextField txtSDT;
    private TextField txtCCCD;
    private TextField txtGioiTinh;
    private TextField txtChucVu;
    private TextField txtTrangThai;
    private TextField txtDiaChi;

    // Hệ màu Material Design Modern UX đồng bộ toàn bộ hệ thống
    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);   // Xanh thương hiệu
    private final Color COLOR_DARK        = new Color(30, 41, 59);     // Chữ chính (Charcoal)
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);    // Chữ phụ / Nhãn
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);  // Nền card nhẹ tinh tế
    private final Color COLOR_BORDER      = new Color(226, 232, 240);  // Đường viền mảnh phẳng

    public Search_NhanVien_UI() {

        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Đệm biên rộng giúp giao diện thoáng đãng

        // =================================================
        // 1. TOP SECTION: HEADER & SEARCH BAR
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRA CỨU NHÂN VIÊN");
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
        tfSearch.setPreferredSize(new Dimension(300, 35));
        tfSearch.setHint("Nhập tên / SĐT / CCCD...");
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchBar.add(tfSearch);

        // Cách tân JComboBox mặc định sang kiểu phẳng hiện đại
        cbChucVu = new JComboBox<>(new String[]{
                "Tất cả chức vụ", "Quản lý", "Lễ tân"
        });
        cbChucVu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cbChucVu.setBackground(Color.WHITE);
        cbChucVu.setPreferredSize(new Dimension(150, 35));
        searchBar.add(cbChucVu);

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

        JLabel lblLeft = new JLabel("DANH SÁCH NHÂN VIÊN HỆ THỐNG");
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(COLOR_TEXT_MAIN);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã NV",
                        "Họ tên",
                        "SĐT",
                        "Chức vụ",
                        "Trạng thái"
                }, 0
        );

        // 🛠️ SỬA LỖI LAG: Không cho click sửa ô chữ trực tiếp, chỉ bôi đen hàng
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chống nháy đúp chuột sửa text trong bảng gây lag
            }
        };
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 255)); // Đổ nền xanh dịu khi chọn
        table.setSelectionForeground(COLOR_DARK);
        table.setShowGrid(false); // Xóa đường lưới ô caro thô kệch
        table.setIntercellSpacing(new Dimension(0, 0));

        // 🛠️ CHẾ ĐỘ CHỌN: Click là ăn nguyên dòng mượt mà
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

        JLabel lblRight = new JLabel("THÔNG TIN CHI TIẾT NHÂN VIÊN");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(COLOR_TEXT_MAIN);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(COLOR_BG_CARD);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 24, 20, 24)
        ));

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 20, 14));
        formGrid.setBackground(COLOR_BG_CARD);

        txtMaNV = createField();
        txtHoTen = createField();
        txtSDT = createField();
        txtCCCD = createField();
        txtGioiTinh = createField();
        txtChucVu = createField();
        txtTrangThai = createField();
        txtDiaChi = createField();

        applyFlatTextField(txtMaNV);
        applyFlatTextField(txtHoTen);
        applyFlatTextField(txtSDT);
        applyFlatTextField(txtCCCD);
        applyFlatTextField(txtGioiTinh);
        applyFlatTextField(txtChucVu);
        applyFlatTextField(txtTrangThai);
        applyFlatTextField(txtDiaChi);

        formGrid.add(createFieldGroup("Mã nhân viên", txtMaNV));
        formGrid.add(createFieldGroup("Họ và tên", txtHoTen));
        formGrid.add(createFieldGroup("Số điện thoại", txtSDT));
        formGrid.add(createFieldGroup("Căn cước công dân (CCCD)", txtCCCD));
        formGrid.add(createFieldGroup("Giới tính", txtGioiTinh));
        formGrid.add(createFieldGroup("Chức vụ đảm nhiệm", txtChucVu));
        formGrid.add(createFieldGroup("Trạng thái làm việc", txtTrangThai));
        formGrid.add(createFieldGroup("Địa chỉ Email", txtDiaChi));

        formWrapper.add(formGrid, BorderLayout.NORTH);
        rightPanel.add(formWrapper, BorderLayout.CENTER);
        mainContentGrid.add(rightPanel);

        // ===== LOAD DATA =====
        loadData();

        // =================================================
        // INTERACTION & LOGIC CONTROL
        // =================================================
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
                    txtGioiTinh.setText(nv.getGioiTinh() == 1 ? "Nam" : "Nữ");
                    txtChucVu.setText(nv.getChucVu().getTenChucVu());
                    txtTrangThai.setText(nv.getTrangThaiLamViec());
                    txtDiaChi.setText(nv.getEmail());
                }
            }
        });

        // 🛠️ LÀM ĐẦY ĐỦ HÀM TÌM KIẾM: Kết hợp đồng thời ô từ khóa và bộ lọc ComboBox Chức Vụ
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            String chucVuDuocChon = cbChucVu.getSelectedItem().toString();

            model.setRowCount(0);
            List<NhanVien> danhSachGoc = new NhanVienDao().getAll();
            List<NhanVien> ketQuaLoc = new ArrayList<>();

            for (NhanVien nv : danhSachGoc) {
                // Kiểm tra điều kiện 1: Khớp chức vụ (Nếu chọn "Tất cả chức vụ" thì bỏ qua kiểm tra này)
                boolean khớpChứcVụ = chucVuDuocChon.equals("Tất cả chức vụ")
                        || nv.getChucVu().getTenChucVu().equalsIgnoreCase(chucVuDuocChon);

                // Kiểm tra điều kiện 2: Khớp từ khóa tìm kiếm (Mã, Tên, SĐT, CCCD)
                boolean khớpTừKhóa = keyword.isEmpty()
                        || nv.getMaNV().toLowerCase().contains(keyword)
                        || nv.getHoTen().toLowerCase().contains(keyword)
                        || nv.getSdt().contains(keyword)
                        || nv.getCccd().contains(keyword);

                // Thỏa mãn cả 2 tiêu chí lọc thì mới đưa vào kết quả bảng
                if (khớpChứcVụ && khớpTừKhóa) {
                    ketQuaLoc.add(nv);
                }
            }

            // Đẩy toàn bộ danh sách đã lọc lên Table
            for (NhanVien nv : ketQuaLoc) {
                model.addRow(new Object[]{
                        nv.getMaNV(),
                        nv.getHoTen(),
                        nv.getSdt(),
                        nv.getChucVu().getTenChucVu(),
                        nv.getTrangThaiLamViec()
                });
            }

            if (model.getRowCount() == 0) {
                clearForm();
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu nhân viên phù hợp!");
            } else {
                // Tự động bôi đen hàng đầu tiên sau khi tìm kiếm để tránh trống form
                table.setRowSelectionInterval(0, 0);
                String maDau = model.getValueAt(0, 0).toString();
                NhanVien nvDau = new NhanVienDao().getById(maDau);
                if (nvDau != null) {
                    txtMaNV.setText(nvDau.getMaNV());
                    txtHoTen.setText(nvDau.getHoTen());
                    txtSDT.setText(nvDau.getSdt());
                    txtCCCD.setText(nvDau.getCccd());
                    txtGioiTinh.setText(nvDau.getGioiTinh() == 1 ? "Nam" : "Nữ");
                    txtChucVu.setText(nvDau.getChucVu().getTenChucVu());
                    txtTrangThai.setText(nvDau.getTrangThaiLamViec());
                    txtDiaChi.setText(nvDau.getEmail());
                }
            }
        });
    }

    // ===== LOAD DATA TỪ CSDL =====
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
        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private void clearForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtSDT.setText(""); txtCCCD.setText("");
        txtGioiTinh.setText(""); txtChucVu.setText(""); txtTrangThai.setText(""); txtDiaChi.setText("");
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