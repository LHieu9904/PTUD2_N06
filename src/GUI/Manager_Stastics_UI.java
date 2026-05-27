package GUI;

import Dao.ThongKeDao;
import Raven.button.Button;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

// import gói io và util cơ bản (GIỮ NGUYÊN)
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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

public class Manager_Stastics_UI extends JPanel {

    private JComboBox<String> chonThang;
    private JComboBox<String> chonNam;

    private JComboBox<String> chonNgay;
    private JRadioButton rdTheoThang;
    private JRadioButton rdTheoNgay;
    private Button btnSearch;

    private JLabel tongTien;
    private JLabel doanhThuPhong;
    private JLabel doanhThuDichVu;

    private JPanel pieChartPanel;
    private JPanel lineChartPanel;

    private final ThongKeDao thongKeDao = new ThongKeDao();
    private final DecimalFormat df = new DecimalFormat("###,### VNĐ");

    // =====================================================
    // FLAT DESIGN SYSTEM COLORS
    // =====================================================
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD_BG = Color.WHITE;
    private final Color TEXT_MAIN = new Color(15, 23, 42);
    private final Color TEXT_SUB = new Color(100, 116, 139);
    private final Color BORDER_COLOR = new Color(226, 232, 240);

    private final Color COLOR_ROOM = new Color(239, 68, 68);
    private final Color COLOR_SERVICE = new Color(37, 99, 235);
    private final Color COLOR_TOTAL = new Color(245, 158, 11);

    public Manager_Stastics_UI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        initUI();
        initEvent();
        switchFilterMode();
        loadThongKe();
    }

    private void initUI() {
        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(TEXT_MAIN);
        titlePanel.add(lblTitle);
        header.add(titlePanel);
        header.add(Box.createVerticalStrut(10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterPanel.setOpaque(false);

        rdTheoThang = new JRadioButton("Theo Tháng", true);
        rdTheoNgay = new JRadioButton("Theo Ngày", false);
        ButtonGroup group = new ButtonGroup();
        group.add(rdTheoThang);
        group.add(rdTheoNgay);

        styleRadioButton(rdTheoThang);
        styleRadioButton(rdTheoNgay);

        chonNgay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) chonNgay.addItem("Ngày " + i);
        chonNgay.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
        styleComboBox(chonNgay, 90);

        chonThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) chonThang.addItem("Tháng " + i);
        chonThang.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        styleComboBox(chonThang, 110);

        chonNam = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) chonNam.addItem(String.valueOf(year - i));
        styleComboBox(chonNam, 90);

        btnSearch = createActionButton("Tìm kiếm", new Color(37, 99, 235));
        Button btnExport = createActionButton("Xuất báo cáo", new Color(100, 116, 139));

        btnExport.addActionListener(e -> exportThongKe());

        filterPanel.add(new JLabel("Chế độ lọc:"));
        filterPanel.add(rdTheoThang);
        filterPanel.add(rdTheoNgay);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(chonNgay);
        filterPanel.add(chonThang);
        filterPanel.add(chonNam);
        filterPanel.add(btnSearch);
        filterPanel.add(btnExport);

        header.add(filterPanel);
        header.add(Box.createVerticalStrut(15));
        header.add(createStatsPanel());

        return header;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);

        panel.add(createCard("Tổng doanh thu kỳ báo cáo", tongTien = new JLabel("0 VNĐ"), COLOR_TOTAL));
        panel.add(createCard("Doanh thu thuê phòng", doanhThuPhong = new JLabel("0 VNĐ"), COLOR_ROOM));
        panel.add(createCard("Doanh thu dịch vụ phát sinh", doanhThuDichVu = new JLabel("0 VNĐ"), COLOR_SERVICE));

        return panel;
    }

    private void initEvent() {
        rdTheoThang.addActionListener(e -> switchFilterMode());
        rdTheoNgay.addActionListener(e -> switchFilterMode());
        btnSearch.addActionListener(e -> loadThongKe());
        chonThang.addActionListener(e -> updateDayComboBounds());
        chonNam.addActionListener(e -> updateDayComboBounds());
    }

    private void switchFilterMode() {
        if (rdTheoThang.isSelected()) {
            chonNgay.setVisible(false);
            chonThang.setVisible(true);
        } else {
            chonNgay.setVisible(true);
            chonThang.setVisible(true);
            updateDayComboBounds();
        }
        filterPanelRepaint();
    }

    private void updateDayComboBounds() {
        if (chonThang.getSelectedIndex() < 0 || chonNam.getSelectedIndex() < 0) return;

        int thang = chonThang.getSelectedIndex() + 1;
        int nam = Integer.parseInt(chonNam.getSelectedItem().toString());
        int daysInMonth = java.time.YearMonth.of(nam, thang).lengthOfMonth();

        int currentSelection = chonNgay.getSelectedIndex();
        chonNgay.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            chonNgay.addItem("Ngày " + i);
        }
        if (currentSelection >= daysInMonth) {
            chonNgay.setSelectedIndex(daysInMonth - 1);
        } else if (currentSelection >= 0) {
            chonNgay.setSelectedIndex(currentSelection);
        }
    }

    private void filterPanelRepaint() {
        if (chonNgay.getParent() != null) {
            chonNgay.getParent().revalidate();
            chonNgay.getParent().repaint();
        }
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setOpaque(false);

        body.add(createChartWrapper("TỶ LỆ PHÂN BỔ DOANH THU", pieChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawResponsivePieChart(g);
            }
        }));

        body.add(createChartWrapper("XU HƯỚNG DOANH THU", lineChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawResponsiveLineChart(g);
            }
        }));

        return body;
    }

    private JPanel createChartWrapper(String title, JPanel chartComponent) {
        JPanel wrapper = new RoundedPanel(24);
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BorderLayout(0, 14));
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(TEXT_SUB);
        wrapper.add(lblTitle, BorderLayout.NORTH);

        chartComponent.setBackground(CARD_BG);
        wrapper.add(chartComponent, BorderLayout.CENTER);

        return wrapper;
    }

    // =====================================================
    // LOAD & TRUY VẤN THỐNG KÊ (ĐÃ CẬP NHẬT LẤY DỮ LIỆU THẬT 100%)
    // =====================================================
    private void loadThongKe() {
        int thang = chonThang.getSelectedIndex() + 1;
        int nam = Integer.parseInt(chonNam.getSelectedItem().toString());

        double tong = 0, phong = 0, dv = 0;

        if (rdTheoThang.isSelected()) {
            // LUỒNG 1: LỌC THEO THÁNG (Giữ nguyên dữ liệu thật cũ của bạn)
            tong = thongKeDao.getTongDoanhThu(thang, nam);
            phong = thongKeDao.getDoanhThuPhong(thang, nam);
            dv = thongKeDao.getDoanhThuDichVu(thang, nam);
        } else {
            // LUỒNG 2: LỌC THEO NGÀY CỦA THÁNG
            int ngay = chonNgay.getSelectedIndex() + 1;

            // ĐÃ SỬA: Xóa bỏ đoạn map cũ chia tỷ lệ % toán học đi.
            // Gọi trực tiếp dữ liệu chuẩn từ câu lệnh SQL Server ra.
            phong = thongKeDao.getDoanhThuPhongTheoNgay(ngay, thang, nam);
            dv = thongKeDao.getDoanhThuDichVuTheoNgay(ngay, thang, nam);
            tong = thongKeDao.getTongDoanhThuTheoNgay(ngay, thang, nam);
        }

        // Đổ dữ liệu thật lên các thẻ hiển thị trên giao diện
        tongTien.setText(df.format(tong));
        doanhThuPhong.setText(df.format(phong)); // Đang chứa "1,200,000 VNĐ"
        doanhThuDichVu.setText(df.format(dv));

        // Vẽ lại 2 biểu đồ khớp theo mốc số liệu thật mới
        pieChartPanel.revalidate();
        pieChartPanel.repaint();
        lineChartPanel.revalidate();
        lineChartPanel.repaint();
    }

    private void doActiveText(JLabel label, String text) {
        label.setText(text);
    }

    private void drawResponsivePieChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = pieChartPanel.getWidth();
        int height = pieChartPanel.getHeight();
        int size = Math.min(width, height) - 80;
        if (size < 50) size = 50;

        int x = (width - size) / 2 - 50;
        int y = (height - size) / 2;

        double phong = Double.parseDouble(doanhThuPhong.getText().replaceAll("[^0-9]", ""));
        double dv = Double.parseDouble(doanhThuDichVu.getText().replaceAll("[^0-9]", ""));
        double tong = phong + dv;

        if (tong <= 0) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.setColor(TEXT_SUB);
            g2.drawString("Không có dữ liệu kinh doanh trong kỳ này", width / 2 - 130, height / 2);
            return;
        }

        int phongAngle = (int) Math.round((phong / tong) * 360);

        g2.setColor(COLOR_ROOM);
        g2.fillArc(x, y, size, size, 0, phongAngle);

        g2.setColor(COLOR_SERVICE);
        g2.fillArc(x, y, size, size, phongAngle, 360 - phongAngle);

        int lx = x + size + 30;
        int ly = height / 2 - 20;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        g2.setColor(COLOR_ROOM); g2.fillRect(lx, ly, 14, 14);
        g2.setColor(TEXT_MAIN); g2.drawString(String.format("Phòng: %.1f%%", (phong / tong) * 100), lx + 22, ly + 12);

        g2.setColor(COLOR_SERVICE); g2.fillRect(lx, ly + 28, 14, 14);
        g2.setColor(TEXT_MAIN); g2.drawString(String.format("Dịch vụ: %.1f%%", (dv / tong) * 100), lx + 22, ly + 40);
    }

    private void drawResponsiveLineChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = lineChartPanel.getWidth();
        int h = lineChartPanel.getHeight();

        int paddingLeft = 65; int paddingRight = 30;
        int paddingTop = 30; int paddingBottom = 40;

        int graphW = w - paddingLeft - paddingRight;
        int graphH = h - paddingTop - paddingBottom;

        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(paddingLeft, h - paddingBottom, w - paddingRight, h - paddingBottom);
        g2.drawLine(paddingLeft, paddingTop, paddingLeft, h - paddingBottom);

        int thang = chonThang.getSelectedIndex() + 1;
        int nam = Integer.parseInt(chonNam.getSelectedItem().toString());

        Map<Integer, Double> rawMap = thongKeDao.getDoanhThuTheoNgay(thang, nam);
        if (rawMap == null) rawMap = new java.util.HashMap<>();
        Map<Integer, Double> map = new TreeMap<>(rawMap);

        if (map.isEmpty()) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.setColor(TEXT_SUB);
            g2.drawString("Không có dữ liệu biểu đồ xu hướng", w / 2 - 110, h / 2);
            return;
        }

        double maxRevenue = 0;
        for (double d : map.values()) if (d > maxRevenue) maxRevenue = d;
        if (maxRevenue == 0) maxRevenue = 1.0;

        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f}, 0.0f));
        for (int i = 1; i <= 4; i++) {
            int gridY = (h - paddingBottom) - (graphH * i / 4);
            g2.setColor(new Color(235, 240, 245));
            g2.drawLine(paddingLeft, gridY, w - paddingRight, gridY);
            g2.setColor(TEXT_SUB);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString(String.format("%,.0f", maxRevenue * i / 4), 8, gridY + 4);
        }

        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (rdTheoThang.isSelected()) {
            int maxDays = java.time.YearMonth.of(nam, thang).lengthOfMonth();
            int prevX = -1, prevY = -1;

            for (int day = 1; day <= maxDays; day++) {
                double value = map.getOrDefault(day, 0.0);
                int x = paddingLeft + (int) Math.round((double) (day - 1) / (maxDays - 1) * graphW);
                int y = (h - paddingBottom) - (int) Math.round((value / maxRevenue) * graphH);

                if (day == 1 || day % 5 == 0 || day == maxDays) {
                    g2.setColor(TEXT_SUB);
                    g2.drawString(String.valueOf(day), x - 4, h - paddingBottom + 18);
                }

                if (value > 0) { g2.setColor(COLOR_SERVICE); g2.fillOval(x - 3, y - 3, 6, 6); }
                if (prevX != -1) { g2.setColor(COLOR_SERVICE); g2.drawLine(prevX, prevY, x, y); }
                prevX = x; prevY = y;
            }
        } else {
            int xemNgay = chonNgay.getSelectedIndex() + 1;
            double value = map.getOrDefault(xemNgay, 0.0);

            int x = paddingLeft + graphW / 2;
            int y = (h - paddingBottom) - (int) Math.round((value / maxRevenue) * graphH);

            g2.setColor(TEXT_SUB);
            g2.drawString("Ngày " + xemNgay, x - 18, h - paddingBottom + 18);

            g2.setColor(COLOR_SERVICE);
            g2.fillRect(x - 20, y, 40, (h - paddingBottom) - y);

            g2.setColor(TEXT_MAIN);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString(df.format(value), x - 35, y - 10);
        }
    }

    public void refreshData() {
        loadThongKe();
        pieChartPanel.repaint();
        lineChartPanel.repaint();
    }

    // =========================================================================
    // HÀM XUẤT BÁO CÁO DOANH THU TỔNG HỢP RA EXCEL (.XLSX) CHUẨN ĐỒNG BỘ
    // =========================================================================
    // =========================================================================
    // HÀM XUẤT BÁO CÁO DOANH THU TỔNG HỢP RA EXCEL (.XLSX) ĐÃ TRỊ DỨT ĐIỂM LỖI
    // =========================================================================
    private void exportThongKe() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file báo cáo tổng hợp doanh thu");

        SimpleDateFormat fileDateFormatter = new SimpleDateFormat("ddMMyyyy");
        String txtThang = String.format("%02d", chonThang.getSelectedIndex() + 1);
        String txtNam = chonNam.getSelectedItem().toString();

        if (rdTheoThang.isSelected()) {
            fileChooser.setSelectedFile(new File("BaoCaoDoanhThu_Thang" + txtThang + "_" + txtNam + ".xlsx"));
        } else {
            String txtNgay = String.format("%02d", chonNgay.getSelectedIndex() + 1);
            fileChooser.setSelectedFile(new File("BaoCaoDoanhThu_Ngay" + txtNgay + txtThang + txtNam + ".xlsx"));
        }

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Doanh Thu Tổng Hợp");

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

            // ĐÃ SỬA LỖI: Thay thế setBackgroundColor bằng cặp lệnh màu nền chuẩn của POI
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

            // 1. Ghi tiêu đề văn bản chính
            Row rowTitle = sheet.createRow(0);
            Cell cellTitle = rowTitle.createCell(0);
            cellTitle.setCellValue("BÁO CÁO THỐNG KÊ DOANH THU TỔNG HỢP HỆ THỐNG");
            cellTitle.setCellStyle(titleStyle);

            Row rowTime = sheet.createRow(1);
            Cell cellTime = rowTime.createCell(0);
            String thongTinKyKyBaoCao = rdTheoThang.isSelected()
                    ? "Kỳ báo cáo: " + chonThang.getSelectedItem().toString() + " / Năm " + txtNam
                    : "Kỳ báo cáo: " + chonNgay.getSelectedItem().toString() + " - " + chonThang.getSelectedItem().toString() + " / Năm " + txtNam;
            cellTime.setCellValue(thongTinKyKyBaoCao);
            cellTime.setCellStyle(normalStyle);

            // 2. Đổ khối KPI tổng hợp số liệu
            int rowIdx = 3;
            Row rowSectionKPI = sheet.createRow(rowIdx++);
            Cell cellKPI = rowSectionKPI.createCell(0);
            cellKPI.setCellValue("I. SỐ LIỆU TỔNG HỢP KINH DOANH TRONG KỲ");
            cellKPI.setCellStyle(sectionStyle);

            Row rowKPI1 = sheet.createRow(rowIdx++);
            rowKPI1.createCell(0).setCellValue("Hạng mục tích lũy");
            rowKPI1.createCell(2).setCellValue("Giá trị doanh thu mang về");
            rowKPI1.getCell(0).setCellStyle(headerStyle); rowKPI1.getCell(2).setCellStyle(headerStyle);

            Row rowKPI2 = sheet.createRow(rowIdx++);
            rowKPI2.createCell(0).setCellValue("Tổng tiền hóa đơn ghi nhận:");
            rowKPI2.createCell(2).setCellValue(tongTien.getText());
            rowKPI2.getCell(0).setCellStyle(normalStyle); rowKPI2.getCell(2).setCellStyle(sectionStyle);

            Row rowKPI3 = sheet.createRow(rowIdx++);
            rowKPI3.createCell(0).setCellValue("Doanh thu khai thác phòng:");
            rowKPI3.createCell(2).setCellValue(doanhThuPhong.getText());
            rowKPI3.getCell(0).setCellStyle(normalStyle); rowKPI3.getCell(2).setCellStyle(normalStyle);

            Row rowKPI4 = sheet.createRow(rowIdx++);
            rowKPI4.createCell(0).setCellValue("Doanh thu khai thác dịch vụ đi kèm:");
            rowKPI4.createCell(2).setCellValue(doanhThuDichVu.getText());
            rowKPI4.getCell(0).setCellStyle(normalStyle); rowKPI4.getCell(2).setCellStyle(normalStyle);

            rowIdx++; // Dòng cách trống

            // 3. Nếu là chế độ xem theo tháng, đổ thêm chi tiết bảng phân rã doanh thu theo từng ngày
            if (rdTheoThang.isSelected()) {
                Row rowSectionList = sheet.createRow(rowIdx++);
                Cell cellListTitle = rowSectionList.createCell(0);
                cellListTitle.setCellValue("II. BẢNG PHÂN RÃ CHI TIẾT DOANH THU THEO TỪNG NGÀY TRONG THÁNG");
                cellListTitle.setCellStyle(sectionStyle);

                Row rowHeader = sheet.createRow(rowIdx++);
                rowHeader.createCell(0).setCellValue("Mốc thời gian");
                rowHeader.createCell(2).setCellValue("Doanh thu thực tế thu về");
                rowHeader.getCell(0).setCellStyle(headerStyle); rowHeader.getCell(2).setCellStyle(headerStyle);

                int maxDays = java.time.YearMonth.of(Integer.parseInt(txtNam), chonThang.getSelectedIndex() + 1).lengthOfMonth();
                Map<Integer, Double> rawMap = thongKeDao.getDoanhThuTheoNgay(chonThang.getSelectedIndex() + 1, Integer.parseInt(txtNam));
                if (rawMap == null) rawMap = new java.util.HashMap<>();

                for (int day = 1; day <= maxDays; day++) {
                    double value = rawMap.getOrDefault(day, 0.0);
                    Row rowData = sheet.createRow(rowIdx++);
                    rowData.createCell(0).setCellValue("Ngày " + day + "/" + txtThang + "/" + txtNam);
                    rowData.createCell(2).setCellValue(df.format(value));
                    rowData.getCell(0).setCellStyle(centerStyle);
                    rowData.getCell(2).setCellStyle(rightStyle);
                }
            }

            // Tự động căn chỉnh dãn rộng cột Excel khít nội dung
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
                JOptionPane.showMessageDialog(this, "Xuất báo cáo tổng hợp doanh thu hệ thống ra tệp Excel thành công!\nĐường dẫn tệp: " + filePath, "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gặp lỗi trong quá trình kết xuất báo cáo Excel: " + e.getMessage(), "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JPanel createCard(String title, JLabel value, Color indicatorColor) {
        JPanel panel = new RoundedPanel(20);
        panel.setBackground(CARD_BG);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(TEXT_SUB);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

        value.setForeground(TEXT_MAIN);
        value.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel line = new JPanel();
        line.setBackground(indicatorColor);
        line.setPreferredSize(new Dimension(0, 4));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(value, BorderLayout.CENTER);
        panel.add(line, BorderLayout.SOUTH);

        return panel;
    }

    private void styleComboBox(JComboBox<String> cb, int width) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setPreferredSize(new Dimension(width, 36));
        cb.setBackground(Color.WHITE);
    }

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rb.setForeground(TEXT_MAIN);
        rb.setOpaque(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private Button createActionButton(String text, Color bg) {
        Button btn = new Button();
        btn.setText(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    class RoundedPanel extends JPanel {
        private final int radius;
        public RoundedPanel(int radius) { this.radius = radius; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(15, 23, 42, 10));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, radius, radius);
            g2.dispose();
            super.paintComponent(g2);
        }
    }

}