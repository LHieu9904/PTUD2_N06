/*package GUI;

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
}*/
package GUI;

import Dao.ThongKeDao;
import Raven.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

public class Manager_Stastics_UI extends JPanel {

    private JComboBox<String> chonThang;
    private JComboBox<String> chonNam;

    private JLabel tongTien;
    private JLabel doanhThuPhong;
    private JLabel doanhThuDichVu;

    private JPanel pieChartPanel;
    private JPanel lineChartPanel;

    private final ThongKeDao thongKeDao =
            new ThongKeDao();

    public Manager_Stastics_UI(){

        initUI();

        loadThongKe();
    }

    // =====================================================
    // INIT UI
    // =====================================================

    private void initUI(){

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);

        add(createBody(), BorderLayout.CENTER);
    }

    // =====================================================
    // HEADER
    // =====================================================

    private JPanel createHeader(){

        JPanel header = new JPanel();

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBackground(Color.WHITE);

        header.add(createFilterPanel());

        header.add(createStatsPanel());

        return header;
    }

    // =====================================================
    // FILTER PANEL
    // =====================================================

    private JPanel createFilterPanel(){

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        panel.setBackground(Color.WHITE);

        chonThang = new JComboBox<>();

        for(int i = 1; i <= 12; i++){

            chonThang.addItem(
                    "Tháng " + i
            );
        }

        chonThang.setSelectedIndex(
                LocalDate.now().getMonthValue() - 1
        );

        chonNam = new JComboBox<>();

        int year = LocalDate.now().getYear();

        for(int i = 0; i < 5; i++){

            chonNam.addItem(
                    String.valueOf(year - i)
            );
        }

        Button btnSearch = new Button();
        btnSearch.setText("Tìm");

        Button btnExport = new Button();
        btnExport.setText("Xuất file");

        btnSearch.addActionListener(e -> loadThongKe());

        btnExport.addActionListener(e -> exportThongKe());

        panel.add(chonThang);
        panel.add(chonNam);
        panel.add(btnSearch);
        panel.add(btnExport);

        return panel;
    }

    // =====================================================
    // STATS PANEL
    // =====================================================

    private JPanel createStatsPanel(){

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        panel.setBackground(Color.WHITE);

        panel.add(createCard(
                "Tổng tiền hóa đơn",
                tongTien = new JLabel("0 VNĐ"),
                new Color(255,153,0)
        ));

        panel.add(createCard(
                "Tiền phòng",
                doanhThuPhong = new JLabel("0 VNĐ"),
                new Color(255,80,80)
        ));

        panel.add(createCard(
                "Tiền dịch vụ",
                doanhThuDichVu = new JLabel("0 VNĐ"),
                new Color(0,153,255)
        ));

        return panel;
    }

    // =====================================================
    // BODY
    // =====================================================

    private JPanel createBody(){

        JPanel body =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        body.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        body.setBackground(Color.WHITE);

        body.add(createPieChartPanel());

        body.add(createLineChartPanel());

        return body;
    }

    // =====================================================
    // PIE CHART
    // =====================================================

    private JPanel createPieChartPanel(){

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBorder(
                new LineBorder(
                        new Color(200,200,200)
                )
        );

        JLabel title =
                new JLabel(
                        "Tỷ lệ doanh thu",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        14
                )
        );

        pieChartPanel = new JPanel(){

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                drawPieChart(g);
            }
        };

        pieChartPanel.setBackground(Color.WHITE);

        panel.add(title, BorderLayout.NORTH);

        panel.add(pieChartPanel, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // LINE CHART
    // =====================================================

    private JPanel createLineChartPanel(){

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBorder(
                new LineBorder(
                        new Color(200,200,200)
                )
        );

        JLabel title =
                new JLabel(
                        "Doanh thu theo ngày",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        14
                )
        );

        lineChartPanel = new JPanel(){

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                drawLineChart(g);
            }
        };

        lineChartPanel.setBackground(Color.WHITE);

        panel.add(title, BorderLayout.NORTH);

        panel.add(lineChartPanel, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // LOAD THỐNG KÊ
    // =====================================================

    private void loadThongKe(){

        int thang =
                chonThang.getSelectedIndex() + 1;

        int nam =
                Integer.parseInt(
                        chonNam.getSelectedItem().toString()
                );

        double tong =
                thongKeDao.getTongDoanhThu(thang, nam);

        double phong =
                thongKeDao.getDoanhThuPhong(thang, nam);

        double dv =
                thongKeDao.getDoanhThuDichVu(thang, nam);

        DecimalFormat df =
                new DecimalFormat("###,### VNĐ");

        tongTien.setText(df.format(tong));

        doanhThuPhong.setText(df.format(phong));

        doanhThuDichVu.setText(df.format(dv));

        pieChartPanel.repaint();

        lineChartPanel.repaint();
    }

    // =====================================================
    // PIE CHART DRAW
    // =====================================================

    private void drawPieChart(Graphics g){

        int thang =
                chonThang.getSelectedIndex() + 1;

        int nam =
                Integer.parseInt(
                        chonNam.getSelectedItem().toString()
                );

        double phong =
                thongKeDao.getDoanhThuPhong(thang, nam);

        double dv =
                thongKeDao.getDoanhThuDichVu(thang, nam);

        double tong = phong + dv;

        if(tong <= 0){

            g.drawString(
                    "Không có dữ liệu",
                    120,
                    120
            );

            return;
        }

        int phongAngle =
                (int)((phong / tong) * 360);

        g.setColor(new Color(255,80,80));
        g.fillArc(70,40,180,180,0,phongAngle);

        g.setColor(new Color(0,153,255));
        g.fillArc(70,40,180,180,phongAngle,360-phongAngle);
    }

    // =====================================================
    // LINE CHART DRAW
    // =====================================================

    private void drawLineChart(Graphics g){

        int thang =
                chonThang.getSelectedIndex() + 1;

        int nam =
                Integer.parseInt(
                        chonNam.getSelectedItem().toString()
                );

        Map<Integer, Double> map =
                thongKeDao.getDoanhThuTheoNgay(thang, nam);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawLine(50,250,550,250);

        g2.drawLine(50,250,50,50);

        if(map.isEmpty()){

            g2.drawString(
                    "Không có dữ liệu",
                    250,
                    150
            );

            return;
        }

        double max = 0;

        for(double d : map.values()){

            if(d > max){

                max = d;
            }
        }

        int prevX = -1;
        int prevY = -1;

        for(int day : map.keySet()){

            double value = map.get(day);

            int x = 50 + day * 15;

            int y =
                    250 - (int)(
                            (value / max) * 180
                    );

            g2.fillOval(x-3,y-3,6,6);

            if(prevX != -1){

                g2.drawLine(
                        prevX,
                        prevY,
                        x,
                        y
                );
            }

            prevX = x;
            prevY = y;
        }
    }

    // =====================================================
    // EXPORT FILE
    // =====================================================

    private void exportThongKe(){

        try{

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showSaveDialog(this);

            if(result != JFileChooser.APPROVE_OPTION){

                return;
            }

            FileWriter writer =
                    new FileWriter(
                            chooser.getSelectedFile()
                    );

            writer.write(
                    "THỐNG KÊ DOANH THU\n"
            );

            writer.write(
                    tongTien.getText() + "\n"
            );

            writer.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Xuất file thành công!"
            );

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    // =====================================================
    // CARD
    // =====================================================

    private JPanel createCard(
            String title,
            JLabel value,
            Color color
    ){

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setBackground(color);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(200,200,200)
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setForeground(Color.WHITE);

        lblTitle.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        16
                )
        );

        value.setForeground(Color.WHITE);

        value.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        22
                )
        );

        panel.add(lblTitle, BorderLayout.NORTH);

        panel.add(value, BorderLayout.SOUTH);

        return panel;
    }
}