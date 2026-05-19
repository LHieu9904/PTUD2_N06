package GUI;

import Dao.KhachHangDao;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeKhachHangUI extends JPanel {

    // ===== PALETTE MÀU HIỆN ĐẠI ĐỒNG BỘ =====
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);     // Xanh Cobalt
    private final Color COLOR_BG_MAIN = new Color(243, 244, 246);   // Nền tổng xám nhạt
    private final Color COLOR_TEXT_MAIN = new Color(17, 24, 39);     // Đen sẫm nội dung
    private final Color COLOR_TEXT_MUTED = new Color(107, 114, 128);  // Xám phụ
    private final Color COLOR_BORDER = new Color(229, 231, 235);      // Viền mảnh hiện đại

    private JSpinner spinnerTuNgay, spinnerDenNgay;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblFooterReport;

    private final KhachHangDao khDao = new KhachHangDao();

    public ThongKeKhachHangUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // 1. NORTH: HEADER VÀ THANH BỘ LỌC THỜI GIAN
        add(createHeaderAndFilterBar(), BorderLayout.NORTH);

        // 2. CENTER: BẢNG SỐ LIỆU THỐNG KÊ TỔNG HỢP
        add(createTableReportCard(), BorderLayout.CENTER);

        // Thiết lập khoảng thời gian mặc định (Từ đầu tháng hiện tại đến hôm nay)
        initDefaultDates();

        // Nạp dữ liệu lên bảng
        loadDataThongKe();
    }

    private void initDefaultDates() {
        Calendar cal = Calendar.getInstance();
        spinnerDenNgay.setValue(cal.getTime()); // Hôm nay

        cal.set(Calendar.DAY_OF_MONTH, 1); // Đầu tháng
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

        JLabel title = new JLabel("Thống Kê Lượt Sử Dụng Khách Sạn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel subtitle = new JLabel("Xếp hạng tổng lượt tương tác toàn diện của khách hàng (Bao gồm cả đặt trước và thuê trực tiếp)");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Thanh công cụ bộ lọc ngày phẳng bo góc tròn
        JPanel filterToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        filterToolbar.setBackground(Color.WHITE);
        filterToolbar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        filterToolbar.setBorder(new EmptyBorder(4, 10, 4, 10));

        spinnerTuNgay = new JSpinner(new SpinnerDateModel());
        spinnerTuNgay.setEditor(new JSpinner.DateEditor(spinnerTuNgay, "dd/MM/yyyy"));
        spinnerTuNgay.setPreferredSize(new Dimension(130, 36));
        spinnerTuNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        spinnerDenNgay = new JSpinner(new SpinnerDateModel());
        spinnerDenNgay.setEditor(new JSpinner.DateEditor(spinnerDenNgay, "dd/MM/yyyy"));
        spinnerDenNgay.setPreferredSize(new Dimension(130, 36));
        spinnerDenNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        JButton btnLoc = new JButton("📊 Lọc Số Liệu");
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setBackground(COLOR_PRIMARY);
        btnLoc.setPreferredSize(new Dimension(130, 36));
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoc.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnLoc.addActionListener(e -> loadDataThongKe());

        filterToolbar.add(new JLabel("Từ ngày:"));
        filterToolbar.add(spinnerTuNgay);
        filterToolbar.add(new JLabel("Đến ngày:"));
        filterToolbar.add(spinnerDenNgay);
        filterToolbar.add(Box.createHorizontalStrut(10));
        filterToolbar.add(btnLoc);

        container.add(titlePanel);
        container.add(filterToolbar);
        return container;
    }

    private JPanel createTableReportCard() {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] columns = {"Thứ Hạng", "Mã Khách Hàng", "Họ và Tên Khách Hàng", "Số Điện Thoại", "Số CCCD", "Giới Tính", "Tổng Lượt Đặt & Thuê"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(38);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30, 41, 59)); // Màu tối sang trọng
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setGridColor(COLOR_BORDER);

        // Định dạng căn giữa văn bản
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        card.add(scroll, BorderLayout.CENTER);

        // Chân bảng hiển thị tổng số
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setOpaque(false);
        lblFooterReport = new JLabel("Đang tải dữ liệu...");
        lblFooterReport.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblFooterReport.setForeground(COLOR_TEXT_MUTED);
        footer.add(lblFooterReport);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private void loadDataThongKe() {
        Date tuNgay = (Date) spinnerTuNgay.getValue();
        Date denNgay = (Date) spinnerDenNgay.getValue();

        // Chuẩn hóa mốc giờ cuối ngày cho ô "Đến ngày" để quét trọn vẹn dữ liệu
        Calendar cal = Calendar.getInstance();
        cal.setTime(denNgay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        denNgay = cal.getTime();

        // Gọi hàm gộp luồng kép mới cập nhật
        List<Object[]> list = khDao.getThongKeTongLuotDenKhachHang(tuNgay, denNgay);
        tableModel.setRowCount(0);

        int rank = 1;
        for (Object[] row : list) {
            int gioiTinhCode = (int) row[4];
            String gioiTinhStr = (gioiTinhCode == 1) ? "Nam" : "Nữ";

            tableModel.addRow(new Object[]{
                    "🥇 Top " + rank,
                    row[0], // MaKH
                    row[1], // HoTen
                    row[2], // SDT
                    row[3], // CCCD
                    gioiTinhStr,
                    row[5] + " lượt ghé thăm"
            });
            rank++;
        }

        lblFooterReport.setText("Hệ thống ghi nhận: Có " + list.size() + " đối tượng khách hàng đã phát sinh hoạt động lưu trú tại khách sạn trong kỳ.");
    }
}