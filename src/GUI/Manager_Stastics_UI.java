package GUI;

import Raven.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;

public class Manager_Stastics_UI extends JPanel {

    private JComboBox<String> chonThang;
    private JComboBox<String> chonNam;

    private JLabel tongTien;
    private JLabel doanhThuPhong;
    private JLabel doanhThuDichVu;

    public Manager_Stastics_UI() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // ===== FILTER =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        filterPanel.setBackground(Color.WHITE);

        chonThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            chonThang.addItem("Tháng " + i);
        }
        chonThang.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        chonThang.setPreferredSize(new Dimension(120,35));

        chonNam = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            chonNam.addItem(String.valueOf(year - i));
        }
        chonNam.setPreferredSize(new Dimension(100,35));

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");
        btnSearch.setPreferredSize(new Dimension(80,35));

        Button btnExport = new Button();
        btnExport.setText("Xuất file");
        btnExport.setPreferredSize(new Dimension(100,35));

        filterPanel.add(chonThang);
        filterPanel.add(chonNam);
        filterPanel.add(btnSearch);
        filterPanel.add(btnExport);

        header.add(filterPanel);

        // ===== CARD THỐNG KÊ =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(new EmptyBorder(20,20,20,20));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createCard(
                "Tổng tiền hóa đơn",
                tongTien = new JLabel("0 VNĐ"),
                new Color(255,153,0)
        ));

        statsPanel.add(createCard(
                "Tiền phòng",
                doanhThuPhong = new JLabel("0 VNĐ"),
                new Color(255,80,80)
        ));

        statsPanel.add(createCard(
                "Tiền dịch vụ",
                doanhThuDichVu = new JLabel("0 VNĐ"),
                new Color(0,153,255)
        ));

        header.add(statsPanel);

        // ===== BODY =====
        JPanel body = new JPanel(new GridLayout(1,2,20,0));
        body.setBorder(new EmptyBorder(20,20,20,20));
        body.setBackground(Color.WHITE);
        add(body, BorderLayout.CENTER);

        // ===== PIE CHART (UI giả) =====
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(new LineBorder(new Color(200,200,200)));
        left.setBackground(Color.WHITE);

        JLabel pieTitle = new JLabel("Tỷ lệ doanh thu (Phòng / Dịch vụ)", SwingConstants.CENTER);
        pieTitle.setFont(new Font("Tahoma", Font.BOLD, 14));

        left.add(pieTitle, BorderLayout.NORTH);
        left.add(new JLabel("PieChart", SwingConstants.CENTER), BorderLayout.CENTER);

        // ===== LINE CHART (UI giả) =====
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new LineBorder(new Color(200,200,200)));
        right.setBackground(Color.WHITE);

        JLabel lineTitle = new JLabel("Doanh thu theo ngày trong tháng", SwingConstants.CENTER);
        lineTitle.setFont(new Font("Tahoma", Font.BOLD, 14));

        right.add(lineTitle, BorderLayout.NORTH);
        right.add(new JLabel("LineChart", SwingConstants.CENTER), BorderLayout.CENTER);

        body.add(left);
        body.add(right);
    }

    // ===== CARD =====
    private JPanel createCard(String title, JLabel value, Color color){

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);

        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200,200,200)),
                new EmptyBorder(20,20,20,20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));

        value.setForeground(Color.WHITE);
        value.setFont(new Font("Tahoma", Font.BOLD, 22));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(value, BorderLayout.SOUTH);

        return panel;
    }

    // ===== TEST =====
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1200,600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Manager_Stastics_UI());
        f.setVisible(true);
    }
}