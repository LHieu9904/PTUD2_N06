package GUI;

import Dao.HoaDonPhongDao;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

// import gói io và util cơ bản (GIỮ NGUYÊN)
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// ===== CHỈ ĐỊNH RÕ ĐƯỜNG DẪN ORG.APACHE.POI KHI KHỞI TẠO ĐỂ TRÁNH XUNG ĐỘT VỚI AWT =====
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        tableChiTietPhong.revalidate();
        tableChiTietPhong.repaint();
    }

    // =========================================================================
    // HÀM XUẤT BÁO CÁO PHÒNG RA FILE EXCEL (.XLSX) ĐỒNG BỘ CAO CẤP SẠCH LỖI
    // =========================================================================
    private void exportThongKePhong() {
        if (modelChiTietPhong.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu thống kê trên bảng để kết xuất báo cáo!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file thống kê dòng tiền phòng");

        java.text.SimpleDateFormat fileDateFormatter = new java.text.SimpleDateFormat("ddMMyyyy");
        String txtTuNgay = fileDateFormatter.format((java.util.Date) spinnerTuNgay.getValue());
        String txtDenNgay = fileDateFormatter.format((java.util.Date) spinnerDenNgay.getValue());

        fileChooser.setSelectedFile(new java.io.File("BaoCaoDoanhThuPhong_" + txtTuNgay + "_" + txtDenNgay + ".xlsx"));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Doanh Thu Phòng");

            // ===== SỬ DỤNG ĐƯỜNG DẪN TUYỆT ĐỐI ĐỂ KHỞI TẠO FONT EXCEL TRÁNH XUNG ĐỘT =====
            org.apache.poi.ss.usermodel.Font titleExcelFont = workbook.createFont();
            titleExcelFont.setFontName("Segoe UI");
            titleExcelFont.setFontHeightInPoints((short) 16);
            titleExcelFont.setBold(true);
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleExcelFont);

            org.apache.poi.ss.usermodel.Font sectionExcelFont = workbook.createFont();
            sectionExcelFont.setFontName("Segoe UI");
            sectionExcelFont.setFontHeightInPoints((short) 12);
            sectionExcelFont.setBold(true);
            CellStyle sectionStyle = workbook.createCellStyle();
            sectionStyle.setFont(sectionExcelFont);

            org.apache.poi.ss.usermodel.Font headerExcelFont = workbook.createFont();
            headerExcelFont.setFontName("Segoe UI");
            headerExcelFont.setFontHeightInPoints((short) 11);
            headerExcelFont.setBold(true);
            headerExcelFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerExcelFont);

            // ĐA KHÁC PHỤC LỖI METHOD: Sử dụng chuẩn phương thức setFillForegroundColor để đổi màu nền
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            org.apache.poi.ss.usermodel.Font normalExcelFont = workbook.createFont();
            normalExcelFont.setFontName("Segoe UI");
            normalExcelFont.setFontHeightInPoints((short) 11);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setFont(normalExcelFont);

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setFont(normalExcelFont);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle rightStyle = workbook.createCellStyle();
            rightStyle.setFont(normalExcelFont);
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);

            // Ghi tiêu đề văn bản
            Row rowTitle = sheet.createRow(0);
            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue("BÁO CÁO THỐNG KÊ HIỆU SUẤT & DOANH THU PHÒNG");
            cellTitle.setCellStyle(titleStyle);

            Row rowTime = sheet.createRow(1);
            Cell cellTime = rowTime.createCell(0);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            cellTime.setCellValue("Giai đoạn báo cáo: Từ ngày " + sdf.format((java.util.Date) spinnerTuNgay.getValue()) + " đến ngày " + sdf.format((java.util.Date) spinnerDenNgay.getValue()));
            cellTime.setCellStyle(normalStyle);

            // Ghi Khối KPI tổng hợp tích lũy
            int rowIdx = 3;
            Row rowSectionKPI = sheet.createRow(rowIdx++);
            Cell cellKPI = rowSectionKPI.createCell(0);
            cellKPI.setCellValue("I. KHỐI KPI TỔNG HỢP TÍCH LŨY");
            cellKPI.setCellStyle(sectionStyle);

            Row rowKPI1 = sheet.createRow(rowIdx++);
            rowKPI1.createCell(0).setCellValue("Phòng mang lại doanh thu cao nhất:");
            rowKPI1.createCell(1).setCellValue(lblPhongNhieuNhat.getText());
            rowKPI1.createCell(2).setCellValue(lblDoanhThuPhongCaoNhat.getText());
            rowKPI1.getCell(0).setCellStyle(normalStyle); rowKPI1.getCell(1).setCellStyle(sectionStyle); rowKPI1.getCell(2).setCellStyle(normalStyle);

            Row rowKPI2 = sheet.createRow(rowIdx++);
            rowKPI2.createCell(0).setCellValue("Hạng phòng đạt doanh thu đỉnh nhất:");
            rowKPI2.createCell(1).setCellValue(lblLoaiPhongNhieuNhat.getText());
            rowKPI2.createCell(2).setCellValue(lblTienLoaiPhongCao.getText());
            rowKPI2.getCell(0).setCellStyle(normalStyle); rowKPI2.getCell(1).setCellStyle(sectionStyle); rowKPI2.getCell(2).setCellStyle(normalStyle);

            rowIdx++; // Dòng cách trống

            // Ghi tiêu đề Danh sách chi tiết
            Row rowSectionList = sheet.createRow(rowIdx++);
            Cell cellListTitle = rowSectionList.createCell(0);
            cellListTitle.setCellValue("II. DANH SÁCH DOANH THU DÒNG TIỀN CHI TIẾT TUNG PHÒNG");
            cellListTitle.setCellStyle(sectionStyle);

            // Ghi tiêu đề cột
            Row rowHeader = sheet.createRow(rowIdx++);
            for (int c = 0; c < tableChiTietPhong.getColumnCount(); c++) {
                Cell cell = rowHeader.createCell(c);
                cell.setCellValue(tableChiTietPhong.getColumnName(c));
                cell.setCellStyle(headerStyle);
            }

            // Đổ dữ liệu vòng lặp JTable
            for (int r = 0; r < modelChiTietPhong.getRowCount(); r++) {
                Row rowData = sheet.createRow(rowIdx++);
                for (int c = 0; c < tableChiTietPhong.getColumnCount(); c++) {
                    Cell cell = rowData.createCell(c);
                    Object value = modelChiTietPhong.getValueAt(r, c);
                    cell.setCellValue(value != null ? value.toString() : "");

                    if (c == 0 || c == 1) {
                        cell.setCellStyle(centerStyle);
                    } else if (c == 2) {
                        cell.setCellStyle(rightStyle);
                    } else {
                        cell.setCellStyle(normalStyle);
                    }
                }
            }

            // Auto fit độ rộng khít với nội dung chữ
            for (int c = 0; c < tableChiTietPhong.getColumnCount(); c++) {
                sheet.autoSizeColumn(c);
            }

            // Ghi xuất luồng dữ liệu vật lý ra máy tính
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất báo cáo hiệu suất phòng ra tệp Excel thành công!\nĐường dẫn tệp: " + filePath, "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gặp lỗi trong quá trình kết xuất báo cáo Excel: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
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