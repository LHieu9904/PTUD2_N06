package GUI;

import Dao.HoaDonPhongDao;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKePhongUI extends JPanel {

    // ===== PALETTE MÀU DASHBOARD SANG TRỌNG =====
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);     // Xanh Cobalt
    private final Color COLOR_SUCCESS = new Color(22, 163, 74);     // Xanh lá Emerald
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);   // Nền tổng Slate nhạt
    private final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);     // Đen sẫm nội dung
    private final Color COLOR_TEXT_MUTED = new Color(100, 116, 139); // Xám phụ
    private final Color COLOR_BORDER = new Color(226, 232, 240);    // Viền mảnh

    private JLabel lblPhongNhieuNhat, lblLuotDungPhong;
    private JLabel lblLoaiPhongNhieuNhat, lblLuotDatLoai;
    private JSpinner spinnerTuNgay, spinnerDenNgay;
    private BarChartPanel chartPanel;

    public ThongKePhongUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // 1. HEADER & CONTROL TOOLBAR (NORTH)
        add(createHeaderAndFilter(), BorderLayout.NORTH);

        // 2. CENTER PANEL (KPI CARDS & BIỂU ĐỒ)
        JPanel centerContainer = new JPanel(new BorderLayout(0, 20));
        centerContainer.setOpaque(false);

        centerContainer.add(createKPICardsPanel(), BorderLayout.NORTH);
        centerContainer.add(createChartContainer(), BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // Đặt khoảng thời gian mặc định (Từ đầu tháng hiện tại đến ngày hôm nay)
        initDefaultFilterDates();

        // Kích hoạt nạp dữ liệu thực tế
        loadThongKeData();
    }

    private void initDefaultFilterDates() {
        Calendar cal = Calendar.getInstance();
        spinnerDenNgay.setValue(cal.getTime()); // Ngày hôm nay

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        spinnerTuNgay.setValue(cal.getTime()); // Ngày đầu tháng
    }

    private JPanel createHeaderAndFilter() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Báo Cáo Thống Kê Phòng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);
        JLabel subtitle = new JLabel("Phân tích mật độ đặt hạng phòng và hiệu suất khai thác tài nguyên phòng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(COLOR_TEXT_MUTED);
        titlePanel.add(title);
        titlePanel.add(subtitle);
        toolbar.add(titlePanel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        filterPanel.setOpaque(false);

        spinnerTuNgay = new JSpinner(new SpinnerDateModel());
        spinnerTuNgay.setEditor(new JSpinner.DateEditor(spinnerTuNgay, "dd/MM/yyyy"));
        spinnerTuNgay.setPreferredSize(new Dimension(125, 36));
        spinnerTuNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        spinnerDenNgay = new JSpinner(new SpinnerDateModel());
        spinnerDenNgay.setEditor(new JSpinner.DateEditor(spinnerDenNgay, "dd/MM/yyyy"));
        spinnerDenNgay.setPreferredSize(new Dimension(125, 36));
        spinnerDenNgay.putClientProperty(FlatClientProperties.STYLE, "arc: 8");

        JButton btnLoc = new JButton("📊 Lọc Báo Cáo");
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setBackground(COLOR_PRIMARY);
        btnLoc.setPreferredSize(new Dimension(135, 36));
        btnLoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoc.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnLoc.addActionListener(e -> loadThongKeData());

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(spinnerTuNgay);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(spinnerDenNgay);
        filterPanel.add(btnLoc);

        toolbar.add(filterPanel, BorderLayout.EAST);
        return toolbar;
    }

    private JPanel createKPICardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 20, 0));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(0, 115));

        // Thẻ KPI Phòng sử dụng nhiều nhất
        JPanel cardPhong = createBaseCard();
        cardPhong.setLayout(new BorderLayout(20, 0));
        JLabel iconPhong = new JLabel("🚪", JLabel.CENTER);
        iconPhong.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconPhong.setPreferredSize(new Dimension(60, 0));

        JPanel infoPhong = new JPanel(new GridLayout(3, 1, 0, 2));
        infoPhong.setOpaque(false);
        JLabel lblTitleP = new JLabel("PHÒNG ĐƯỢC KHAI THÁC NHIỀU NHẤT");
        lblTitleP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleP.setForeground(COLOR_TEXT_MUTED);
        lblPhongNhieuNhat = new JLabel("N/A");
        lblPhongNhieuNhat.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPhongNhieuNhat.setForeground(COLOR_PRIMARY);
        lblLuotDungPhong = new JLabel("Tổng cộng: 0 lượt");
        lblLuotDungPhong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLuotDungPhong.setForeground(COLOR_TEXT_MAIN);
        infoPhong.add(lblTitleP); infoPhong.add(lblPhongNhieuNhat); infoPhong.add(lblLuotDungPhong);
        cardPhong.add(iconPhong, BorderLayout.WEST);
        cardPhong.add(infoPhong, BorderLayout.CENTER);

        // Thẻ KPI Loại phòng được đặt nhiều nhất
        JPanel cardLoai = createBaseCard();
        cardLoai.setLayout(new BorderLayout(20, 0));
        JLabel iconLoai = new JLabel("🏆", JLabel.CENTER);
        iconLoai.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLoai.setPreferredSize(new Dimension(60, 0));

        JPanel infoLoai = new JPanel(new GridLayout(3, 1, 0, 2));
        infoLoai.setOpaque(false);
        JLabel lblTitleL = new JLabel("HẠNG PHÒNG ĐẶT CAO NHẤT");
        lblTitleL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleL.setForeground(COLOR_TEXT_MUTED);
        lblLoaiPhongNhieuNhat = new JLabel("N/A");
        lblLoaiPhongNhieuNhat.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLoaiPhongNhieuNhat.setForeground(COLOR_SUCCESS);
        lblLuotDatLoai = new JLabel("Tổng cộng: 0 lượt");
        lblLuotDatLoai.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLuotDatLoai.setForeground(COLOR_TEXT_MAIN);
        infoLoai.add(lblTitleL); infoLoai.add(lblLoaiPhongNhieuNhat); infoLoai.add(lblLuotDatLoai);
        cardLoai.add(iconLoai, BorderLayout.WEST);
        cardLoai.add(infoLoai, BorderLayout.CENTER);

        wrapper.add(cardPhong);
        wrapper.add(cardLoai);
        return wrapper;
    }

    private JPanel createChartContainer() {
        JPanel container = createBaseCard();
        container.setLayout(new BorderLayout(0, 15));

        JLabel chartTitle = new JLabel("Biểu đồ phân bổ lượng đặt phòng theo Hạng Phòng");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(COLOR_TEXT_MAIN);
        container.add(chartTitle, BorderLayout.NORTH);

        chartPanel = new BarChartPanel();
        container.add(chartPanel, BorderLayout.CENTER);

        return container;
    }

    private void loadThongKeData() {
        java.util.Date tuNgay = (java.util.Date) spinnerTuNgay.getValue();
        java.util.Date denNgay = (java.util.Date) spinnerDenNgay.getValue();

        HoaDonPhongDao hdDao = new HoaDonPhongDao();

        // 1. Đổ dữ liệu lên thẻ Phòng sử dụng nhiều nhất
        Object[] thongTinPhong = hdDao.getPhongSuDungNhieuNhat(tuNgay, denNgay);
        lblPhongNhieuNhat.setText(thongTinPhong[0].toString());
        lblLuotDungPhong.setText("Tổng cộng: " + thongTinPhong[1].toString() + " lượt cư trú");

        // 2. Đổ dữ liệu lên thẻ Loại phòng được đặt nhiều nhất
        Object[] thongTinLoai = hdDao.getLoaiPhongDatNhieuNhat(tuNgay, denNgay);
        lblLoaiPhongNhieuNhat.setText(thongTinLoai[0].toString());
        lblLuotDatLoai.setText("Tổng cộng: " + thongTinLoai[1].toString() + " lượt đăng ký");

        // 3. Đổ dữ liệu lên biểu đồ cột vẽ phẳng
        Map<String, Integer> dataset = hdDao.getDatasetLoaiPhong(tuNgay, denNgay);
        chartPanel.setChartData(dataset);
    }

    private JPanel createBaseCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    // =========================================================================
    // INNER CLASS: PANEL TỰ VẼ BIỂU ĐỒ CỘT PHẲNG HIỆN ĐẠI (RESPONSIVE)
    // =========================================================================
    private class BarChartPanel extends JPanel {
        private Map<String, Integer> data = new LinkedHashMap<>();
        private final Color COLOR_BAR = new Color(79, 70, 229); // Màu Indigo hiện đại

        public void setChartData(Map<String, Integer> data) {
            this.data = data;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            int labelOffset = 30;

            int maxVal = 0;
            for (int val : data.values()) {
                if (val > maxVal) maxVal = val;
            }
            if (maxVal == 0) maxVal = 1;

            int chartWidth = width - (2 * padding);
            int chartHeight = height - (2 * padding) - labelOffset;
            int barCount = data.size();
            int barGap = 24;
            int totalGapWidth = barGap * (barCount - 1);
            int barWidth = (chartWidth - totalGapWidth) / barCount;

            // Vẽ đường trục hoành cơ sở
            g2.setColor(new Color(203, 213, 225));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(padding, height - padding - labelOffset, width - padding, height - padding - labelOffset);

            int x = padding;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String label = entry.getKey();
                int value = entry.getValue();

                int barHeight = (int) (((double) value / maxVal) * chartHeight);
                int barX = x;
                int barY = height - padding - labelOffset - barHeight;

                // Vẽ thanh cột (Bo góc đỉnh trên)
                g2.setColor(COLOR_BAR);
                g2.fillRoundRect(barX, barY, barWidth, barHeight, 8, 8);
                if (barHeight > 8) { // Bù đắp phần chân không bị bo góc dưới
                    g2.fillRect(barX, height - padding - labelOffset - 6, barWidth, 6);
                }

                // Vẽ số lượng lượt trên đầu cột
                g2.setColor(COLOR_TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String valStr = String.valueOf(value);
                int valStrWidth = g2.getFontMetrics().stringWidth(valStr);
                g2.drawString(valStr, barX + (barWidth - valStrWidth) / 2, barY - 8);

                // Vẽ nhãn chữ tên Loại phòng dưới chân cột
                g2.setColor(COLOR_TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                int labelWidth = g2.getFontMetrics().stringWidth(label);

                String displayLabel = label;
                if (labelWidth > barWidth + 15) {
                    displayLabel = label.substring(0, Math.min(label.length(), 10)) + "..";
                    labelWidth = g2.getFontMetrics().stringWidth(displayLabel);
                }
                g2.drawString(displayLabel, barX + (barWidth - labelWidth) / 2, height - padding - 8);

                x += barWidth + barGap;
            }
            g2.dispose();
        }
    }
}