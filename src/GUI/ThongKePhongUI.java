package GUI;

import Dao.HoaDonPhongDao;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThongKePhongUI extends JPanel {

    // ===== PALETTE MÀU DASHBOARD SANG TRỌNG =====
    private final Color COLOR_PRIMARY = new Color(37, 99, 235);     // Xanh Cobalt
    private final Color COLOR_SUCCESS = new Color(22, 163, 74);     // Xanh lá Emerald
    private final Color COLOR_BG_MAIN = new Color(248, 250, 252);   // Nền tổng Slate nhạt
    private final Color COLOR_TEXT_MAIN = new Color(15, 23, 42);     // Đen sẫm nội dung
    private final Color COLOR_TEXT_MUTED = new Color(100, 116, 139); // Xám phụ
    private final Color COLOR_BORDER = new Color(226, 232, 240);    // Viền mảnh

    private JLabel lblPhongNhieuNhat, lblDoanhThuPhongCaoNhat;
    private JLabel lblLoaiPhongNhieuNhat, lblTienLoaiPhongCao;

    private JSpinner spinnerTuNgay, spinnerDenNgay;

    // Quản lý hoán đổi hiển thị Đồ thị / Bảng biểu chi tiết
    private JPanel dynamicViewPanel;
    private CardLayout cardLayout;
    private BarChartPanel chartPanel;
    private JTable tableChiTietPhong;
    private DefaultTableModel modelChiTietPhong;

    private JToggleButton btnToggleView;
    private final DecimalFormat df = new DecimalFormat("###,### VNĐ");
    private final HoaDonPhongDao hdDao = new HoaDonPhongDao();

    public ThongKePhongUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout(20, 20));
        setBackground(COLOR_BG_MAIN);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // 1. HEADER & CONTROL TOOLBAR (NORTH)
        add(createHeaderAndFilter(), BorderLayout.NORTH);

        // 2. CENTER PANEL (KPI CARDS & DYNAMIC VIEW)
        JPanel centerContainer = new JPanel(new BorderLayout(0, 20));
        centerContainer.setOpaque(false);

        centerContainer.add(createKPICardsPanel(), BorderLayout.NORTH);
        centerContainer.add(createDynamicMainContainer(), BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        initDefaultFilterDates();
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
        JLabel title = new JLabel("Báo Cáo Thống Kê Hiệu Suất & Doanh Thu Phòng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_MAIN);
        JLabel subtitle = new JLabel("Phân tích chi tiết doanh thu dòng tiền thực tế, chi phí dịch vụ đi kèm của từng phòng");
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
        styleButton(btnLoc, COLOR_PRIMARY, Color.WHITE);
        btnLoc.addActionListener(e -> loadThongKeData());

        JButton btnExport = new JButton("📥 Xuất Báo Cáo");
        styleButton(btnExport, new Color(100, 116, 139), Color.WHITE);
        btnExport.addActionListener(e -> exportThongKePhong());

        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(spinnerTuNgay);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(spinnerDenNgay);
        filterPanel.add(btnLoc);
        filterPanel.add(btnExport);

        toolbar.add(filterPanel, BorderLayout.EAST);
        return toolbar;
    }

    private JPanel createKPICardsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 20, 0));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(0, 115));

        // Thẻ KPI Phòng sinh lời cao nhất
        JPanel cardPhong = createBaseCard();
        cardPhong.setLayout(new BorderLayout(20, 0));
        JLabel iconPhong = new JLabel("💰", JLabel.CENTER);
        iconPhong.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconPhong.setPreferredSize(new Dimension(60, 0));

        JPanel infoPhong = new JPanel(new GridLayout(3, 1, 0, 2));
        infoPhong.setOpaque(false);
        JLabel lblTitleP = new JLabel("PHÒNG MANG LẠI DOANH THU CAO NHẤT");
        lblTitleP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleP.setForeground(COLOR_TEXT_MUTED);
        lblPhongNhieuNhat = new JLabel("N/A");
        lblPhongNhieuNhat.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPhongNhieuNhat.setForeground(COLOR_PRIMARY);
        lblDoanhThuPhongCaoNhat = new JLabel("Doanh thu: 0 VNĐ");
        lblDoanhThuPhongCaoNhat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDoanhThuPhongCaoNhat.setForeground(COLOR_TEXT_MAIN);
        infoPhong.add(lblTitleP); infoPhong.add(lblPhongNhieuNhat); infoPhong.add(lblDoanhThuPhongCaoNhat);
        cardPhong.add(iconPhong, BorderLayout.WEST);
        cardPhong.add(infoPhong, BorderLayout.CENTER);

        // Thẻ KPI Hạng phòng doanh thu dẫn đầu
        JPanel cardLoai = createBaseCard();
        cardLoai.setLayout(new BorderLayout(20, 0));
        JLabel iconLoai = new JLabel("🏢", JLabel.CENTER);
        iconLoai.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLoai.setPreferredSize(new Dimension(60, 0));

        JPanel infoLoai = new JPanel(new GridLayout(3, 1, 0, 2));
        infoLoai.setOpaque(false);
        JLabel lblTitleL = new JLabel("HẠNG PHÒNG ĐẶT DOANH THU ĐỈNH");
        lblTitleL.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitleL.setForeground(COLOR_TEXT_MUTED);
        lblLoaiPhongNhieuNhat = new JLabel("N/A");
        lblLoaiPhongNhieuNhat.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLoaiPhongNhieuNhat.setForeground(COLOR_SUCCESS);
        lblTienLoaiPhongCao = new JLabel("Tổng thu: 0 VNĐ");
        lblTienLoaiPhongCao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTienLoaiPhongCao.setForeground(COLOR_TEXT_MAIN);
        infoLoai.add(lblTitleL); infoLoai.add(lblLoaiPhongNhieuNhat); infoLoai.add(lblTienLoaiPhongCao);
        cardLoai.add(iconLoai, BorderLayout.WEST);
        cardLoai.add(infoLoai, BorderLayout.CENTER);

        wrapper.add(cardPhong);
        wrapper.add(cardLoai);
        return wrapper;
    }

    private JPanel createDynamicMainContainer() {
        JPanel container = createBaseCard();
        container.setLayout(new BorderLayout(0, 15));

        // Thanh điều khiển chuyển chế độ xem đồ thị/bảng
        JPanel viewHeader = new JPanel(new BorderLayout());
        viewHeader.setOpaque(false);

        JLabel chartTitle = new JLabel("Chi tiết phân bổ dòng tiền khai thác tài nguyên");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(COLOR_TEXT_MAIN);
        viewHeader.add(chartTitle, BorderLayout.WEST);

        btnToggleView = new JToggleButton("📋 Xem Dạng Bảng");
        btnToggleView.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnToggleView.setPreferredSize(new Dimension(150, 32));
        btnToggleView.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleView.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnToggleView.addActionListener(e -> {
            if (btnToggleView.isSelected()) {
                btnToggleView.setText("📊 Xem Biểu Đồ");
                cardLayout.show(dynamicViewPanel, "TABLE_VIEW");
            } else {
                btnToggleView.setText("📋 Xem Dạng Bảng");
                cardLayout.show(dynamicViewPanel, "CHART_VIEW");
            }
        });
        viewHeader.add(btnToggleView, BorderLayout.EAST);
        container.add(viewHeader, BorderLayout.NORTH);

        // Vùng CardLayout hoán đổi linh hoạt
        cardLayout = new CardLayout();
        dynamicViewPanel = new JPanel(cardLayout);
        dynamicViewPanel.setOpaque(false);

        // Chế độ 1: Biểu đồ cột
        chartPanel = new BarChartPanel();
        dynamicViewPanel.add(chartPanel, "CHART_VIEW");

        // Chế độ 2: Bảng dữ liệu chi tiết phòng
        modelChiTietPhong = new DefaultTableModel(
                new String[]{"Mã Phòng", "Lượt Cư Trú",  "Tổng Doanh Thu"}, 0
        );
        tableChiTietPhong = new JTable(modelChiTietPhong);
        styleTable();
        JScrollPane scrollTable = new JScrollPane(tableChiTietPhong);
        scrollTable.setBorder(null);
        scrollTable.getViewport().setBackground(Color.WHITE);
        dynamicViewPanel.add(scrollTable, "TABLE_VIEW");

        container.add(dynamicViewPanel, BorderLayout.CENTER);
        return container;
    }

    private void loadThongKeData() {
        java.util.Date tuNgay = (java.util.Date) spinnerTuNgay.getValue();
        java.util.Date denNgay = (java.util.Date) spinnerDenNgay.getValue();

        // 1. Thống kê Thẻ KPI Phòng mang lại doanh thu cao nhất
        Object[] thongTinPhong = hdDao.getPhongDoanhThuCaoNhat(tuNgay, denNgay);
        if (thongTinPhong != null) {
            lblPhongNhieuNhat.setText("Phòng " + thongTinPhong[0].toString());
            lblDoanhThuPhongCaoNhat.setText("Doanh thu thực tế: " + df.format(Double.parseDouble(thongTinPhong[1].toString())));
        }

        // 2. Thống kê Thẻ KPI Hạng phòng mang lại doanh thu đỉnh nhất
        Object[] thongTinLoai = hdDao.getLoaiPhongDoanhThuCaoNhat(tuNgay, denNgay);
        if (thongTinLoai != null) {
            lblLoaiPhongNhieuNhat.setText(thongTinLoai[0].toString());
            lblTienLoaiPhongCao.setText("Tổng tiền tích lũy: " + df.format(Double.parseDouble(thongTinLoai[1].toString())));
        }

        // 3. Đổ dữ liệu lên Biểu đồ cột phân tích dòng tiền theo Loại phòng
        Map<String, Integer> dataset = hdDao.getDatasetDoanhThuLoaiPhong(tuNgay, denNgay);
        chartPanel.setChartData(dataset);

        // 4. Đổ dữ liệu lên Bảng danh sách chi tiết doanh thu từng phòng đơn lẻ
        modelChiTietPhong.setRowCount(0);
        List<Object[]> listPhongChiTiet = hdDao.getChiTietTienTungPhong(tuNgay, denNgay);
        if (listPhongChiTiet != null) {
            for (Object[] row : listPhongChiTiet) {
                modelChiTietPhong.addRow(new Object[]{
                        row[0],
                        row[1],
                        df.format(Double.parseDouble(row[2].toString()))
                });
            }
        }
    }

    private void exportThongKePhong() {
        java.util.Date tuNgay = (java.util.Date) spinnerTuNgay.getValue();
        java.util.Date denNgay = (java.util.Date) spinnerDenNgay.getValue();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn vị trí lưu file thống kê dòng tiền phòng");
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            FileWriter writer = new FileWriter(chooser.getSelectedFile() + "_ThongKePhong.csv");

            writer.write("BAO CAO THONG KE DOANH THU TINH TRANG PHONG CHI TIET\n");
            writer.write("Giai doan bao cao:," + sdf.format(tuNgay) + " den " + sdf.format(denNgay) + "\n\n");

            writer.write("KHOI KPI TONG HOP\n");
            writer.write("Phong doanh thu cao nhat," + lblPhongNhieuNhat.getText() + "," + lblDoanhThuPhongCaoNhat.getText() + "\n");
            writer.write("Hang phong doanh thu cao nhat," + lblLoaiPhongNhieuNhat.getText() + "," + lblTienLoaiPhongCao.getText() + "\n\n");

            writer.write("DANH SACH DOANH THU DONG TIEN CHI TIET TUNG PHONG\n");
            writer.write("Ma Phong,Luot Cu Tru,Tien Thue Phong,Tien Dich Vu,Tong Doanh Thu\n");

            for (int i = 0; i < modelChiTietPhong.getRowCount(); i++) {
                String maPhong = modelChiTietPhong.getValueAt(i, 0).toString();
                String luotO = modelChiTietPhong.getValueAt(i, 1).toString();

                String tTong = modelChiTietPhong.getValueAt(i, 2).toString().replaceAll("[^0-9]", "");

                writer.write(maPhong + "," + luotO + "," + tTong + "\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(this, "Xuất báo cáo dữ liệu phòng thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gặp lỗi trong quá trình xuất tệp Excel!");
            e.printStackTrace();
        }
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
    }

    private void styleTable() {
        tableChiTietPhong.setRowHeight(40);
        tableChiTietPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableChiTietPhong.setGridColor(new Color(241, 245, 249));
        tableChiTietPhong.setShowVerticalLines(false);
        tableChiTietPhong.setSelectionBackground(new Color(219, 234, 254));

        JTableHeader header = tableChiTietPhong.getTableHeader();
        header.setPreferredSize(new Dimension(0, 42));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(COLOR_PRIMARY);
        header.setForeground(Color.WHITE);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
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
    // INNER CLASS: PANEL BIỂU ĐỒ CỘT TỰ VẼ RESPONSIVE
    // =========================================================================
    private class BarChartPanel extends JPanel {
        private Map<String, Integer> data = new LinkedHashMap<>();
        private final Color COLOR_BAR = new Color(79, 70, 229);

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

            long maxVal = 0;
            for (long val : data.values()) {
                if (val > maxVal) maxVal = val;
            }
            if (maxVal == 0) maxVal = 1;

            int chartWidth = width - (2 * padding);
            int chartHeight = height - (2 * padding) - labelOffset;
            int barCount = data.size();
            int barGap = 30;
            int totalGapWidth = barGap * (barCount - 1);
            int barWidth = (chartWidth - totalGapWidth) / barCount;

            g2.setColor(new Color(203, 213, 225));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(padding, height - padding - labelOffset, width - padding, height - padding - labelOffset);

            int x = padding;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String label = entry.getKey();
                long value = entry.getValue();

                int barHeight = (int) (((double) value / maxVal) * chartHeight);
                int barX = x;
                int barY = height - padding - labelOffset - barHeight;

                g2.setColor(COLOR_BAR);
                g2.fillRoundRect(barX, barY, barWidth, barHeight, 8, 8);
                if (barHeight > 8) {
                    g2.fillRect(barX, height - padding - labelOffset - 6, barWidth, 6);
                }

                g2.setColor(COLOR_TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String valStr = df.format(value);
                int valStrWidth = g2.getFontMetrics().stringWidth(valStr);
                g2.drawString(valStr, barX + (barWidth - valStrWidth) / 2, barY - 8);

                g2.setColor(COLOR_TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, barX + (barWidth - labelWidth) / 2, height - padding - 8);

                x += barWidth + barGap;
            }
            g2.dispose();
        }
    }
}