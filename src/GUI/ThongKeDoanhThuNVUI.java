package GUI;

import Dao.HoaDonPhongDao;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class ThongKeDoanhThuNVUI extends JPanel {

    // ===== PALETTE MÀU HIỆN ĐẠI =====
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);
    private final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);
    private final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    private JSpinner spinnerTuNgay, spinnerDenNgay;
    private JComboBox<String> cbThangQuick, cbNamQuick;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTongDoanhThuHeThong;

    public ThongKeDoanhThuNVUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // 1. NORTH: HEADER VÀ THANH CÔNG CỤ LỌC
        add(createHeaderAndFilterBar(), BorderLayout.NORTH);

        // 2. CENTER: BẢNG SỐ LIỆU DOANH THU
        add(createTableReportPanel(), BorderLayout.CENTER);

        // Cài đặt thời gian mặc định và nạp dữ liệu ban đầu
        initDefaultFilters();
        loadDataDoanhThu();
    }

    private void initDefaultFilters() {
        Calendar cal = Calendar.getInstance();
        cbNamQuick.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
        cbThangQuick.setSelectedIndex(cal.get(Calendar.MONTH)); // Tháng hiện tại

        // Đồng bộ ô chọn ngày tự do
        spinnerDenNgay.setValue(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        spinnerTuNgay.setValue(cal.getTime());
    }

    private JPanel createHeaderAndFilterBar() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Tiêu đề phân hệ
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 12, 0));
        JLabel title = new JLabel("Hiệu Suất & Doanh Thu Nhân Viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);
        JLabel subtitle = new JLabel("Thống kê tổng số lượng hóa đơn và doanh thu chi tiết do từng nhân viên lập");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_MUTED);
        titlePanel.add(title); titlePanel.add(subtitle);

        // Thanh công cụ bộ lọc (Gồm 2 chế độ: Lọc theo tháng nhanh hoặc Lọc khoảng ngày tùy biến)
        JPanel filterToolbar = new JPanel(new GridBagLayout());
        filterToolbar.setBackground(Color.WHITE);
        filterToolbar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        filterToolbar.setBorder(new EmptyBorder(12, 16, 12, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 4, 0, 12);

        // --- Chế độ 1: Thống kê nhanh theo Tháng/Nam ---
        cbThangQuick = new JComboBox<>(new String[]{"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"});
        cbNamQuick = new JComboBox<>(new String[]{"2024", "2025", "2026", "2027"});
        styleComponent(cbThangQuick, 110);
        styleComponent(cbNamQuick, 85);

        cbThangQuick.addActionListener(e -> syncQuickDateFilter());
        cbNamQuick.addActionListener(e -> syncQuickDateFilter());

        // --- Chế độ 2: Ô Chọn khoảng ngày tự do ---
        spinnerTuNgay = new JSpinner(new SpinnerDateModel());
        spinnerTuNgay.setEditor(new JSpinner.DateEditor(spinnerTuNgay, "dd/MM/yyyy"));
        spinnerDenNgay = new JSpinner(new SpinnerDateModel());
        spinnerDenNgay.setEditor(new JSpinner.DateEditor(spinnerDenNgay, "dd/MM/yyyy"));
        styleComponent(spinnerTuNgay, 120);
        styleComponent(spinnerDenNgay, 120);

        JButton btnLoc = new JButton("📊 Lọc Số Liệu");
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setBackground(COLOR_PRIMARY);
        btnLoc.setPreferredSize(new Dimension(130, 36));
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoc.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnLoc.addActionListener(e -> loadDataDoanhThu());

        // Đẩy các component vào layout hàng ngang
        c.gridx = 0; filterToolbar.add(new JLabel("Xem nhanh:"), c);
        c.gridx = 1; filterToolbar.add(cbThangQuick, c);
        c.gridx = 2; filterToolbar.add(cbNamQuick, c);
        c.gridx = 3; filterToolbar.add(new JSeparator(JSeparator.VERTICAL), c);
        c.gridx = 4; filterToolbar.add(new JLabel("Từ ngày:"), c);
        c.gridx = 5; filterToolbar.add(spinnerTuNgay, c);
        c.gridx = 6; filterToolbar.add(new JLabel("Đến ngày:"), c);
        c.gridx = 7; filterToolbar.add(spinnerDenNgay, c);
        c.gridx = 8; c.weightx = 1.0; filterToolbar.add(Box.createHorizontalGlue(), c); // Đẩy nút về bên phải
        c.gridx = 9; c.weightx = 0.0; filterToolbar.add(btnLoc, c);

        container.add(titlePanel);
        container.add(filterToolbar);
        return container;
    }

    // Tự động tính toán ngày đầu tháng và cuối tháng khi thay đổi JComboBox
    private void syncQuickDateFilter() {
        int thangIdx = cbThangQuick.getSelectedIndex();
        int nam = Integer.parseInt(cbNamQuick.getSelectedItem().toString());

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, nam);
        cal.set(Calendar.MONTH, thangIdx);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        spinnerTuNgay.setValue(cal.getTime()); // Ngày đầu tháng

        int ngayCuoiThang = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, ngayCuoiThang);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        spinnerDenNgay.setValue(cal.getTime()); // Ngày cuối tháng

        loadDataDoanhThu();
    }

    private JPanel createTableReportPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Thiết lập cấu trúc bảng
        String[] columns = {"Mã Nhân Viên", "Họ và Tên Nhân Viên", "Số Lượng Hóa Đơn Đã Lập", "Tổng Doanh Thu Mang Về (VND)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; } // Khóa không cho sửa bảng
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.setShowVerticalLines(false); // Ẩn viền dọc đúng chuẩn UI phẳng hiện đại
        table.setGridColor(COLOR_BORDER);

        // Canh phải và định dạng VND cho cột doanh thu
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // Canh giữa cho cột số lượng hóa đơn và mã NV
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        card.add(scroll, BorderLayout.CENTER);

        // Thanh hiển thị tổng cộng dưới chân bảng
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        summaryPanel.setOpaque(false);
        JLabel lblLabel = new JLabel("TỔNG DOANH THU HỆ THỐNG:");
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongDoanhThuHeThong = new JLabel("0 VND");
        lblTongDoanhThuHeThong.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongDoanhThuHeThong.setForeground(Color.RED);
        summaryPanel.add(lblLabel);
        summaryPanel.add(lblTongDoanhThuHeThong);
        card.add(summaryPanel, BorderLayout.SOUTH);

        return card;
    }

    private void loadDataDoanhThu() {
        java.util.Date tuNgay = (java.util.Date) spinnerTuNgay.getValue();
        java.util.Date denNgay = (java.util.Date) spinnerDenNgay.getValue();

        // Ép giờ của denNgay lên cuối ngày để không bị sót hóa đơn lập lúc chiều/tối
        Calendar cDen = Calendar.getInstance();
        cDen.setTime(denNgay);
        if (cDen.get(Calendar.HOUR_OF_DAY) == 0) {
            cDen.set(Calendar.HOUR_OF_DAY, 23);
            cDen.set(Calendar.MINUTE, 59);
            cDen.set(Calendar.SECOND, 59);
            denNgay = cDen.getTime();
        }

        // Gọi DAO lấy dữ liệu thực từ SQL Server
        HoaDonPhongDao hdDao = new HoaDonPhongDao();
        List<Object[]> dataList = hdDao.getDoanhThuNhanVienTheoThoiGian(tuNgay, denNgay);

        // Xóa trắng bảng cũ trước khi nạp data mới
        tableModel.setRowCount(0);
        double tongDoanhThuHeThong = 0;
        DecimalFormat formatter = new DecimalFormat("#,###");

        for (Object[] row : dataList) {
            double doanhThu = ((java.math.BigDecimal) row[3]).doubleValue();
            tongDoanhThuHeThong += doanhThu;

            tableModel.addRow(new Object[]{
                    row[0], // MaNV
                    row[1], // HoTen
                    row[2], // SoHoaDon
                    formatter.format(doanhThu) + " đ" // Định dạng tiền tệ
            });
        }

        // Cập nhật nhãn tổng doanh thu
        lblTongDoanhThuHeThong.setText(formatter.format(tongDoanhThuHeThong) + " VND");
    }

    private void styleComponent(JComponent comp, int width) {
        comp.setPreferredSize(new Dimension(width, 36));
        comp.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
    }
}