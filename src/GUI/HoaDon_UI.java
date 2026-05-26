/*package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDon_UI extends JFrame {

    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

    public HoaDon_UI(String maHD,
                     String khachHang,
                     String nhanVien,
                     String tenPhong,
                     List<Object[]> dsDV,
                     BigDecimal tienPhong,
                     BigDecimal tienDV,
                     BigDecimal giamGia,
                     BigDecimal vatPercent) {

        setTitle("HÓA ĐƠN");
        setSize(700, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== HEADER =====
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblLogo = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/logocinema.png"));
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO");
        }

        JPanel pnlTitle = new JPanel();
        pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.Y_AXIS));

        JLabel lblTen = new JLabel("Khách sạn Luxury");
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblDiaChi = new JLabel("12 Nguyễn Văn Bảo, Gò Vấp, TP.HCM");
        lblDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblHoaDon = new JLabel("                               HÓA ĐƠN");
        lblHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 22));

        pnlTitle.add(lblTen);
        pnlTitle.add(lblDiaChi);
        pnlTitle.add(Box.createVerticalStrut(5));
        pnlTitle.add(lblHoaDon);

        pnlHeader.add(lblLogo, BorderLayout.WEST);
        pnlHeader.add(pnlTitle, BorderLayout.CENTER);

        add(pnlHeader, BorderLayout.NORTH);

        // ===== CENTER =====
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 10, 5));
        pnlInfo.add(new JLabel("Mã hóa đơn: " + maHD));
        pnlInfo.add(new JLabel("Phòng: " + tenPhong));
        pnlInfo.add(new JLabel("Khách hàng: " + khachHang));
        pnlInfo.add(new JLabel("Nhân viên: " + nhanVien));
        pnlInfo.add(new JLabel("Thời gian: " + LocalDateTime.now().format(dtf)));

        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = {"STT", "Tên DV", "Đơn giá", "SL", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        int stt = 1;

        model.addRow(new Object[]{
                "",
                "Tiền phòng",
                "",
                "",
                df.format(tienPhong)
        });

        for (Object[] dv : dsDV) {
            model.addRow(new Object[]{
                    stt++,
                    dv[0], // tên DV
                    df.format(new BigDecimal(dv[2].toString())),
                    dv[1], // số lượng
                    df.format(new BigDecimal(dv[3].toString()))
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);

        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // ===== FOOTER =====
        JPanel pnlBottom = new JPanel(new GridLayout(5, 1, 5, 5));
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));

        BigDecimal tong = tienPhong.add(tienDV);
        BigDecimal sauGiam =
                tong.subtract(
                        tong.multiply(giamGia)
                                .divide(
                                        new BigDecimal("100")
                                )
                );
        BigDecimal vat = sauGiam.multiply(vatPercent).divide(new BigDecimal("100"));
        BigDecimal tongThanhToan = sauGiam.add(vat);

        pnlBottom.add(new JLabel("Tổng cộng: " + df.format(tong), SwingConstants.RIGHT));
        pnlBottom.add(new JLabel("Giảm giá: - " + df.format(giamGia), SwingConstants.RIGHT));
        pnlBottom.add(new JLabel("VAT (10%): " + df.format(vat), SwingConstants.RIGHT));

        JLabel lblTong = new JLabel("TỔNG THANH TOÁN: " + df.format(tongThanhToan), SwingConstants.RIGHT);
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlBottom.add(lblTong);

        pnlBottom.add(new JLabel("Cảm ơn quý khách!", SwingConstants.CENTER));

        add(pnlBottom, BorderLayout.SOUTH);
    }
}*/
package GUI;

import Dao.*;
import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDon_UI extends JFrame {

    private DecimalFormat df = new DecimalFormat("#,### VNĐ");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

    // Hệ màu Material Design Modern UX cao cấp chuyên nghiệp cho biểu mẫu
    private final Color COLOR_PRIMARY     = new Color(0, 153, 255);   // Xanh thương hiệu
    private final Color COLOR_DARK        = new Color(30, 41, 59);     // Chữ chính (Charcoal)
    private final Color COLOR_TEXT_MAIN   = new Color(71, 85, 105);    // Chữ phụ / Nhãn
    private final Color COLOR_BG_CARD     = new Color(248, 250, 252);  // Nền khối nhạt tinh tế
    private final Color COLOR_BORDER      = new Color(226, 232, 240);  // Đường viền mảnh phẳng

    public HoaDon_UI(String maHD,
                     String khachHang,
                     String nhanVien,
                     String tenPhong,
                     List<Object[]> dsDV,
                     BigDecimal tienPhong,
                     BigDecimal tienDV,
                     BigDecimal giamGia,
                     BigDecimal vatPercent) {

        setTitle("Hóa Đơn Điện Tử - Khách Sạn Luxury");
        setSize(650, 780); // Tinh chỉnh kích cỡ vàng cân đối cho form hóa đơn giấy
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 15));
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // =====================================================
        // 1. HEADER SECTION: BRANDING & INVOICE TITLE
        // =====================================================
        JPanel pnlHeader = new JPanel(new BorderLayout(20, 0));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel lblLogo = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/logocinema.png"));
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("Luxury Hotel");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblLogo.setForeground(COLOR_PRIMARY);
        }

        JPanel pnlTitle = new JPanel();
        pnlTitle.setLayout(new BoxLayout(pnlTitle, BoxLayout.Y_AXIS));
        pnlTitle.setBackground(Color.WHITE);

        JLabel lblTen = new JLabel("KHÁCH SẠN LUXURY");
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTen.setForeground(COLOR_DARK);

        JLabel lblDiaChi = new JLabel("12 Nguyễn Văn Bảo, Gò Vấp, TP.HCM");
        lblDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDiaChi.setForeground(COLOR_TEXT_MAIN);

        JLabel lblHoaDon = new JLabel("HÓA ĐƠN THANH TOÁN");
        lblHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHoaDon.setForeground(COLOR_PRIMARY);
        lblHoaDon.setBorder(new EmptyBorder(10, 0, 0, 0));

        pnlTitle.add(lblTen);
        pnlTitle.add(Box.createVerticalStrut(2));
        pnlTitle.add(lblDiaChi);
        pnlTitle.add(lblHoaDon);

        pnlHeader.add(lblLogo, BorderLayout.WEST);
        pnlHeader.add(pnlTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // =====================================================
        // 2. CENTER SECTION: CUSTOMER INFO & BILLING TABLE
        // =====================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(0, 25, 0, 25));

        // 🌟 KHỐI CARD THÔNG TIN HÀNH CHÍNH
        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 15, 8));
        pnlInfo.setBackground(COLOR_BG_CARD);
        pnlInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(14, 16, 14, 16)
        ));

        pnlInfo.add(createMetadataLabel("Mã hóa đơn: ", maHD));
        pnlInfo.add(createMetadataLabel("Phòng thuê: ", tenPhong));
        pnlInfo.add(createMetadataLabel("Khách hàng: ", khachHang));
        pnlInfo.add(createMetadataLabel("Thu ngân: ", nhanVien));
        pnlInfo.add(createMetadataLabel("Thời gian lập: ", LocalDateTime.now().format(dtf)));
        // Ô trống bổ sung giữ GridLayout cân xứng
        pnlInfo.add(new JPanel() {{ setBackground(COLOR_BG_CARD); }});

        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // 🌟 KHỐI BẢNG KÊ CHI PHÍ FLAT UI KHÔNG LAG
        String[] cols = {"STT", "Tên dịch vụ / phòng", "Đơn giá", "Số lượng", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khóa sửa ô chữ trực tiếp chống lag giật
            }
        };

        int stt = 1;
        model.addRow(new Object[]{
                "-",
                "Tiền thuê phòng nghỉ",
                "",
                "",
                df.format(tienPhong)
        });

        for (Object[] dv : dsDV) {
            model.addRow(new Object[]{
                    stt++,
                    dv[0], // tên DV
                    df.format(new BigDecimal(dv[2].toString())),
                    dv[1], // số lượng
                    df.format(new BigDecimal(dv[3].toString()))
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(36); // Hàng cao thoáng ráo chuyên nghiệp
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false); // Triệt tiêu đường chia ô caro lỗi thời
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(240, 246, 255));
        table.setSelectionForeground(COLOR_DARK);

        // Thiết kế Header Table thanh lịch phẳng
        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 13));
        th.setBackground(COLOR_BG_CARD);
        th.setForeground(COLOR_DARK);
        th.setPreferredSize(new Dimension(100, 38));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        // Căn phải cho các cột số liệu đơn giá và thành tiền theo chuẩn kế toán
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        table.getColumnModel().getColumn(4).setCellRenderer(rightAlign);

        // Căn giữa cột số lượng và STT
        DefaultTableCellRenderer centerAlign = new DefaultTableCellRenderer();
        centerAlign.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerAlign);
        table.getColumnModel().getColumn(3).setCellRenderer(centerAlign);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // =====================================================
        // 3. FOOTER SECTION: INVOICE BILLING SUMMATION
        // =====================================================
        JPanel pnlFooterContainer = new JPanel(new BorderLayout());
        pnlFooterContainer.setBackground(Color.WHITE);
        pnlFooterContainer.setBorder(new EmptyBorder(10, 25, 20, 25));

        // Khối căn phải bảng kê cộng tiền (Invoice Billing Grid)
        JPanel pnlBottom = new JPanel(new GridBagLayout());
        pnlBottom.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logic tính toán cốt lõi nguyên bản bảo lưu 100%
        BigDecimal tong = tienPhong.add(tienDV);
        BigDecimal sauGiam = tong.subtract(tong.multiply(giamGia).divide(new BigDecimal("100")));
        BigDecimal vat = sauGiam.multiply(vatPercent).divide(new BigDecimal("100"));
        BigDecimal tongThanhToan = sauGiam.add(vat);

        int bRow = 0;
        addInvoiceRow(pnlBottom, gbc, bRow++, "Tổng chi phí dịch vụ thô:", df.format(tong), false);
        addInvoiceRow(pnlBottom, gbc, bRow++, "Chiết khấu giảm giá:", "- " + df.format(giamGia), false);
        addInvoiceRow(pnlBottom, gbc, bRow++, "Thuế giá trị gia tăng (VAT 10%):", df.format(vat), false);

        // Đường gạch mảnh ngang cắt tổng tiền thanh toán thanh lịch
        gbc.gridy = bRow++; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(8, 0, 8, 0);
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setForeground(COLOR_BORDER);
        pnlBottom.add(sep, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(4, 0, 4, 0); // Reset viền đệm
        addInvoiceRow(pnlBottom, gbc, bRow++, "TỔNG SỐ TIỀN THANH TOÁN:", df.format(tongThanhToan), true);

        // Lời cảm ơn chân thành đáy hóa đơn đặt chính giữa
        JLabel lblThanks = new JLabel("Cảm ơn quý khách đã lựa chọn Luxury Hotel!", SwingConstants.CENTER);
        lblThanks.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblThanks.setForeground(COLOR_TEXT_MAIN);
        lblThanks.setBorder(new EmptyBorder(20, 0, 0, 0));

        pnlFooterContainer.add(pnlBottom, BorderLayout.EAST);
        pnlFooterContainer.add(lblThanks, BorderLayout.SOUTH);

        add(pnlFooterContainer, BorderLayout.SOUTH);
    }

    // Hàm tạo chuỗi nhãn chữ tinh gọn cho khối thông tin hành chính
    private JLabel createMetadataLabel(String label, String value) {
        JLabel lbl = new JLabel("<html><font color='#8C96A0'><b>" + label + "</b></font> <font color='#1E293B'>" + value + "</font></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    // Hàm căn dòng hóa đơn lệch phải chuẩn UX E-Invoice
    private void addInvoiceRow(JPanel panel, GridBagConstraints gbc, int y, String description, String value, boolean isTotal) {
        gbc.gridy = y;
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Segoe UI", isTotal ? Font.BOLD : Font.PLAIN, isTotal ? 14 : 13));
        lblDesc.setForeground(isTotal ? COLOR_DARK : COLOR_TEXT_MAIN);
        panel.add(lblDesc, gbc);

        gbc.gridx = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel lblVal = new JLabel(value, SwingConstants.RIGHT);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, isTotal ? 17 : 14));
        lblVal.setForeground(isTotal ? COLOR_PRIMARY : COLOR_DARK);
        lblVal.setPreferredSize(new Dimension(160, 24));
        panel.add(lblVal, gbc);
    }
}