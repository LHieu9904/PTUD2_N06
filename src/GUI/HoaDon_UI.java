package GUI;

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
                     List<Object[]> dsDV,   // 🔥 đổi sang dịch vụ
                     BigDecimal tienPhong,
                     BigDecimal tienDV,
                     BigDecimal giamGia) {

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

        JLabel lblTen = new JLabel("TRƯỜNG ĐẠI HỌC CÔNG NGHIỆP TP. HCM");
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

        // 🔥 thêm dòng tiền phòng
        model.addRow(new Object[]{
                "",
                "Tiền phòng",
                "",
                "",
                df.format(tienPhong)
        });

        // 🔥 dịch vụ
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
        BigDecimal sauGiam = tong.subtract(giamGia);
        BigDecimal vat = sauGiam.multiply(new BigDecimal("0.1"));
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
}