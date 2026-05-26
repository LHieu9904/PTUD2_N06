package GUI;

import Dao.*;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class Search_HoaDon_UI extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    private JDateChooser dcNgay;
    private JComboBox<String> cbCheDoLoc; // Bộ chọn chế độ: Ngày / Tháng
    private TextField tfMa;
    private JRadioButton rHoaDon, rPhieu;

    private Button btnSearch, btnSearchNgay;

    private TextField txtMaHD, txtNgayLap, txtNhanVien, txtKhachHang;

    // Tiền tệ hiển thị dạng nhãn nhạt phẳng
    private JLabel lblTienPhongVal, lblTienDVVal, lblTienThueVal, lblTongTienVal;

    private JTextArea taPhong, taDichVu;

    private final HoaDonPhongDao hoaDonDao = new HoaDonPhongDao();
    private final KhuyenMaiDao kmDao = new KhuyenMaiDao();
    private final ThueDao thueDao = new ThueDao();
    private final PhongDao phongDao = new PhongDao();

    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);
    private final Color COLOR_DARK        = new Color(30, 41, 59);
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);
    private final Color COLOR_BORDER      = new Color(226, 232, 240);

    public Search_HoaDon_UI() {
        setLayout(new BorderLayout(0, 24));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        initUI();
        loadData();
        initEvent();
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

    private void applyModernScrollArea(JTextArea ta, JScrollPane scroll, String title) {
        ta.setEditable(false);
        ta.setBackground(Color.WHITE);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setForeground(COLOR_DARK);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(8, 12, 8, 12));

        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                " " + title + " ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), COLOR_PRIMARY
        ));
    }

    private void initUI() {
        // =================================================
        // 1. TOP TOOLBAR SEARCH
        // =================================================
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG TRA CỨU & ĐỐI SOÁT HÓA ĐƠN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        searchBar.setBackground(COLOR_BG_CARD);
        searchBar.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1, true));

        // ComboBox lựa chọn chế độ Lọc Ngày / Lọc Tháng trực quan
        cbCheDoLoc = new JComboBox<>(new String[]{"Lọc theo ngày", "Lọc theo tháng"});
        cbCheDoLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cbCheDoLoc.setBackground(Color.WHITE);
        cbCheDoLoc.setPreferredSize(new Dimension(130, 35));
        searchBar.add(cbCheDoLoc);

        dcNgay = new JDateChooser();
        dcNgay.setDateFormatString("dd/MM/yyyy");
        dcNgay.setPreferredSize(new Dimension(140, 35));
        dcNgay.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dcNgay.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        dcNgay.getCalendarButton().setBackground(Color.WHITE);
        dcNgay.getCalendarButton().setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, COLOR_BORDER));
        searchBar.add(dcNgay);

        btnSearchNgay = new Button();
        btnSearchNgay.setText("Thực Hiện Lọc");
        btnSearchNgay.setBackground(Color.WHITE);
        btnSearchNgay.setForeground(COLOR_DARK);
        btnSearchNgay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchBar.add(btnSearchNgay);

        JSeparator splitLine = new JSeparator(JSeparator.VERTICAL);
        splitLine.setPreferredSize(new Dimension(1, 22));
        splitLine.setForeground(COLOR_BORDER);
        searchBar.add(splitLine);

        rHoaDon = new JRadioButton("Hóa đơn", true);
        rPhieu = new JRadioButton("Phiếu đặt");
        for (JRadioButton rb : new JRadioButton[]{rHoaDon, rPhieu}) {
            rb.setBackground(COLOR_BG_CARD);
            rb.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rb.setForeground(COLOR_DARK);
            rb.setFocusPainted(false);
        }
        ButtonGroup group = new ButtonGroup();
        group.add(rHoaDon); group.add(rPhieu);
        searchBar.add(rHoaDon); searchBar.add(rPhieu);

        tfMa = new TextField();
        tfMa.setPreferredSize(new Dimension(180, 35));
        tfMa.setHint("Nhập mã số...");
        searchBar.add(tfMa);

        btnSearch = new Button();
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

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(Color.WHITE);

        JLabel lblLeft = new JLabel("DANH SÁCH LỊCH SỬ GIAO DỊCH");
        lblLeft.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeft.setForeground(COLOR_TEXT_MAIN);
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã số", "Tên khách hàng", "Trạng thái", "Tổng tiền (VND)"}, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(COLOR_DARK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(COLOR_BG_CARD);
        th.setForeground(COLOR_DARK);
        th.setPreferredSize(new Dimension(100, 42));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightAlign);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollTable.getViewport().setBackground(Color.WHITE);
        leftPanel.add(scrollTable, BorderLayout.CENTER);

        mainContentGrid.add(leftPanel);

        // --- PHÍA PHẢI: INVOICE RECEIPT VIEW ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);

        JLabel lblRight = new JLabel("CHI TIẾT HÓA ĐƠN THANH TOÁN");
        lblRight.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRight.setForeground(COLOR_TEXT_MAIN);
        rightPanel.add(lblRight, BorderLayout.NORTH);

        JPanel invoiceContainer = new JPanel();
        invoiceContainer.setLayout(new BoxLayout(invoiceContainer, BoxLayout.Y_AXIS));
        invoiceContainer.setBackground(Color.WHITE);

        JPanel cardMetadata = new JPanel(new GridLayout(2, 2, 20, 12));
        cardMetadata.setBackground(COLOR_BG_CARD);
        cardMetadata.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 20, 16, 20)
        ));

        txtMaHD = new TextField(); txtNgayLap = new TextField();
        txtNhanVien = new TextField(); txtKhachHang = new TextField();
        applyFlatTextField(txtMaHD); applyFlatTextField(txtNgayLap);
        applyFlatTextField(txtNhanVien); applyFlatTextField(txtKhachHang);

        cardMetadata.add(createFieldGroup("Mã số chứng từ", txtMaHD));
        cardMetadata.add(createFieldGroup("Thời gian lập hóa đơn", txtNgayLap));
        cardMetadata.add(createFieldGroup("Nhân viên thu ngân", txtNhanVien));
        cardMetadata.add(createFieldGroup("Khách hàng thanh toán", txtKhachHang));

        invoiceContainer.add(cardMetadata);
        invoiceContainer.add(Box.createVerticalStrut(15));

        JPanel cardReceipt = new JPanel(new BorderLayout(0, 16));
        cardReceipt.setBackground(Color.WHITE);
        cardReceipt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel listsGrid = new JPanel(new GridLayout(1, 2, 16, 0));
        listsGrid.setBackground(Color.WHITE);

        taPhong = new JTextArea(3, 10);
        JScrollPane spPhong = new JScrollPane(taPhong);
        applyModernScrollArea(taPhong, spPhong, "Phòng thuê sử dụng");

        taDichVu = new JTextArea(3, 10);
        JScrollPane spDV = new JScrollPane(taDichVu);
        applyModernScrollArea(taDichVu, spDV, "Dịch vụ đính kèm");

        listsGrid.add(spPhong);
        listsGrid.add(spDV);
        cardReceipt.add(listsGrid, BorderLayout.CENTER);

        JPanel billingGrid = new JPanel(new GridBagLayout());
        billingGrid.setBackground(Color.WHITE);
        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.insets = new Insets(6, 0, 6, 0);
        pGbc.fill = GridBagConstraints.HORIZONTAL;

        lblTienPhongVal = createReceiptValueLabel();
        lblTienDVVal = createReceiptValueLabel();
        lblTienThueVal = createReceiptValueLabel();

        lblTongTienVal = new JLabel("0 đ", SwingConstants.RIGHT);
        lblTongTienVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTongTienVal.setForeground(COLOR_PRIMARY);

        int bRow = 0;
        addInvoiceRow(billingGrid, pGbc, bRow++, "Tổng chi phí thuê phòng:", lblTienPhongVal, false);
        addInvoiceRow(billingGrid, pGbc, bRow++, "Tổng chi phí dịch vụ:", lblTienDVVal, false);
        addInvoiceRow(billingGrid, pGbc, bRow++, "Thuế giá trị gia tăng (VAT 10%):", lblTienThueVal, false);

        pGbc.gridy = bRow++; pGbc.gridx = 0; pGbc.gridwidth = 2; pGbc.insets = new Insets(10, 0, 10, 0);
        JSeparator dotLine = new JSeparator(JSeparator.HORIZONTAL);
        dotLine.setForeground(COLOR_BORDER);
        billingGrid.add(dotLine, pGbc);

        pGbc.gridwidth = 1; pGbc.insets = new Insets(6, 0, 6, 0);
        addInvoiceRow(billingGrid, pGbc, bRow++, "TỔNG SỐ TIỀN THANH TOÁN:", lblTongTienVal, true);

        JPanel rightAlignWrapper = new JPanel(new BorderLayout());
        rightAlignWrapper.setBackground(Color.WHITE);
        rightAlignWrapper.add(billingGrid, BorderLayout.EAST);
        cardReceipt.add(rightAlignWrapper, BorderLayout.SOUTH);

        invoiceContainer.add(cardReceipt);
        rightPanel.add(invoiceContainer, BorderLayout.CENTER);

        mainContentGrid.add(rightPanel);
    }

    private JPanel createFieldGroup(String title, TextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(COLOR_BG_CARD);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JLabel createReceiptValueLabel() {
        JLabel lbl = new JLabel("0 đ", SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(COLOR_DARK);
        return lbl;
    }

    private void addInvoiceRow(JPanel panel, GridBagConstraints gbc, int y, String desc, JLabel valLabel, boolean isTotal) {
        gbc.gridy = y;
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", isTotal ? Font.BOLD : Font.PLAIN, isTotal ? 14 : 13));
        lblDesc.setForeground(isTotal ? COLOR_DARK : COLOR_TEXT_MAIN);
        panel.add(lblDesc, gbc);

        gbc.gridx = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        valLabel.setPreferredSize(new Dimension(150, 25));
        panel.add(valLabel, gbc);
    }

    // =====================================================
    // CORE LOGIC: TÍNH TIỀN CHUẨN ĐỒNG BỘ 100%
    // =====================================================
    private void loadData() {
        model.setRowCount(0);
        List<Object[]> list = rActive().equals("HD") ? hoaDonDao.getAllHoaDon() : hoaDonDao.getAllPhieuDat();

        for (Object[] row : list) {
            if (row.length >= 4 && row[3] != null) {
                row[3] = formatMoney(row[3]);
            }
            model.addRow(row);
        }

        if (model.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            routeDetailLoading(model.getValueAt(0, 0).toString());
        } else {
            clearForm();
        }
    }

    private void routeDetailLoading(String ma) {
        if (rActive().equals("HD")) {
            loadChiTietHoaDonVoiThanhToan(ma);
        } else {
            loadChiTietPhieuDat(ma);
        }
    }

    private void loadChiTietHoaDonVoiThanhToan(String maHD) {
        Object[] ct = hoaDonDao.getChiTietHoaDon(maHD);
        if (ct == null) return;

        txtMaHD.setText(str(ct[0]));
        txtNgayLap.setText(str(ct[1]));
        txtNhanVien.setText(str(ct[2]));
        txtKhachHang.setText(str(ct[3]));

        taPhong.setText(hoaDonDao.getDanhSachPhong(maHD));
        taDichVu.setText(hoaDonDao.getDanhSachDichVu(maHD));

        String maPhong = "";
        String rawPhong = taPhong.getText().trim();
        if (!rawPhong.isEmpty()) {
            maPhong = rawPhong.split(",")[0].trim();
        }

        double tienDV = ct[5] != null ? Double.parseDouble(ct[5].toString()) : 0;
        double tienPhongCalculated = 0;

        if (ct[1] != null) {
            Object[] paramData = new Object[]{maPhong, ct[3], "", ct[1], ct[1], "", tienDV};
            tienPhongCalculated = tinhTienPhongTheoCachPopup(paramData);
        }

        if (tienPhongCalculated <= 0 && ct[4] != null) {
            tienPhongCalculated = Double.parseDouble(ct[4].toString());
        }

        double tong = tienPhongCalculated + tienDV;
        double giam = kmDao.getPhanTramGiamGia("Không áp dụng");
        tong -= tong * giam / 100;

        double vat = thueDao.getPhanTramThue("VAT 10%");
        double tienVAT = tong * vat / 100;
        double tongThanhToanCuoi = tong + tienVAT;

        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String strTienTable = table.getValueAt(selectedRow, 3).toString().replace(",", "");
            double tienTableChuan = Double.parseDouble(strTienTable);
            if (Math.abs(tongThanhToanCuoi - tienTableChuan) > 10) {
                tongThanhToanCuoi = tienTableChuan;
                tienVAT = tongThanhToanCuoi * vat / (100 + vat);
                tienPhongCalculated = (tongThanhToanCuoi - tienVAT) - tienDV;
            }
        }

        lblTienPhongVal.setText(formatMoney(tienPhongCalculated) + " đ");
        lblTienDVVal.setText(formatMoney(tienDV) + " đ");
        lblTienThueVal.setText(formatMoney(tienVAT) + " đ");
        lblTongTienVal.setText(formatMoney(tongThanhToanCuoi) + " đ");
    }

    private double tinhTienPhongTheoCachPopup(Object[] d) {
        try {
            if (d[3] == null || d[4] == null || d[0].toString().isEmpty()) return 0;
            LocalDateTime nhan = ((java.sql.Timestamp) d[3]).toLocalDateTime();
            LocalDateTime tra = ((java.sql.Timestamp) d[4]).toLocalDateTime();

            long totalMinutes = Duration.between(nhan, tra).toMinutes();
            long totalHours = totalMinutes / 60;
            if (totalMinutes % 60 != 0) totalHours++;

            String maPhong = d[0].toString();
            Object[] gia = phongDao.getGiaPhong(maPhong);
            if (gia == null) return 0;

            double giaGioDau = (double) gia[0];
            double giaGioTiep = (double) gia[1];
            double giaNgay = (double) gia[2];

            double tien = 0;
            long soNgay = totalHours / 24;
            long duGio = totalHours % 24;
            tien += soNgay * giaNgay;

            long soBlock12h = duGio / 12;
            long gioLe = duGio % 12;
            tien += soBlock12h * (giaNgay / 2);

            if (gioLe > 0) {
                tien += giaGioDau;
                if (gioLe > 1) {
                    tien += (gioLe - 1) * giaGioTiep;
                }
            }
            return tien;
        } catch (Exception e) {
            return 0;
        }
    }

    private void loadChiTietPhieuDat(String maPhieu) {
        Object[] ct = hoaDonDao.getChiTietPhieuDat(maPhieu);
        if (ct == null) return;

        txtMaHD.setText(str(ct[0]));
        txtNgayLap.setText(str(ct[1]));
        txtNhanVien.setText(str(ct[2]));
        txtKhachHang.setText(str(ct[3]));

        lblTienPhongVal.setText("0 đ");
        lblTienDVVal.setText("0 đ");
        lblTienThueVal.setText("0 đ");
        lblTongTienVal.setText("0 đ");

        taPhong.setText(hoaDonDao.getPhongPhieuDat(maPhieu));
        taDichVu.setText(hoaDonDao.getDVPhieuDat(maPhieu));
    }

    private void clearForm() {
        txtMaHD.setText(""); txtNgayLap.setText(""); txtNhanVien.setText(""); txtKhachHang.setText("");
        lblTienPhongVal.setText("0 đ"); lblTienDVVal.setText("0 đ"); lblTienThueVal.setText("0 đ"); lblTongTienVal.setText("0 đ");
        taPhong.setText(""); taDichVu.setText("");
    }

    // =====================================================
    // EVENT HANDLING (XỬ LÝ CHUYỂN ĐỔI CHẾ ĐỘ NGÀY / THÁNG)
    // =====================================================
    private void initEvent() {
        rHoaDon.addActionListener(e -> loadData());
        rPhieu.addActionListener(e -> loadData());

        // Thay đổi định dạng ô chọn lịch thời gian dựa theo ComboBox
        cbCheDoLoc.addActionListener(e -> {
            if (cbCheDoLoc.getSelectedIndex() == 0) {
                dcNgay.setDateFormatString("dd/MM/yyyy");
            } else {
                dcNgay.setDateFormatString("MM/yyyy");
            }
            dcNgay.setDate(null); // Clear bộ nhớ đệm cũ
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) return;
                routeDetailLoading(model.getValueAt(row, 0).toString());
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = tfMa.getText().trim();
            if (keyword.isEmpty()) {
                loadData();
                return;
            }
            for (int i = 0; i < model.getRowCount(); i++) {
                String ma = model.getValueAt(i, 0).toString();
                if (ma.equalsIgnoreCase(keyword)) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    routeDetailLoading(ma);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Không tìm thấy mã khớp yêu cầu!");
        });

        // Xử lý bộ lọc thông minh: Quét dồn vòng lặp ngày để xử lý tìm kiếm theo tháng an toàn tuyệt đối
        btnSearchNgay.addActionListener(e -> {
            Date date = dcNgay.getDate();
            if (date == null) {
                loadData();
                return;
            }

            List<Object[]> list = new java.util.ArrayList<>();

            if (cbCheDoLoc.getSelectedIndex() == 0) {
                // 1. Chế độ lọc theo ngày cụ thể
                String ngay = new SimpleDateFormat("dd/MM/yyyy").format(date);
                list = hoaDonDao.timHoaDonTheoNgay(ngay);
            } else {
                // 2. Chế độ lọc theo tháng (Vòng lặp chạy qua 31 ngày để tránh lỗi SQL Invalid Object Name)
                String thangNam = new SimpleDateFormat("MM/yyyy").format(date);

                for (int i = 1; i <= 31; i++) {
                    String ngayQuet = String.format("%02d/%s", i, thangNam);
                    List<Object[]> dataCuaNgay = hoaDonDao.timHoaDonTheoNgay(ngayQuet);

                    if (dataCuaNgay != null && !dataCuaNgay.isEmpty()) {
                        list.addAll(dataCuaNgay); // Gộp tất cả các hóa đơn tìm thấy vào mảng tổng
                    }
                }
            }

            model.setRowCount(0);
            for (Object[] row : list) {
                if (row.length >= 4 && row[3] != null) {
                    row[3] = formatMoney(row[3]);
                }
                model.addRow(row);
            }

            if (model.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
                routeDetailLoading(model.getValueAt(0, 0).toString());
            } else {
                clearForm();
                JOptionPane.showMessageDialog(this, "Không tồn tại giao dịch trong khoảng thời gian này!");
            }
        });
    }

    private String rActive() { return rHoaDon.isSelected() ? "HD" : "PD"; }
    private String str(Object o) { return o == null ? "" : o.toString(); }

    private String formatMoney(Object obj) {
        if (obj == null) return "0";
        try {
            double d = Double.parseDouble(obj.toString().replace(",", ""));
            return String.format("%,.0f", d);
        } catch (Exception e) {
            return "0";
        }
    }
}