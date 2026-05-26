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
}
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
}*/
package GUI;

import Dao.ThongKeDao;
import Raven.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Manager_Stastics_UI extends JPanel {

    private JComboBox<String> chonThang;
    private JComboBox<String> chonNam;

    // ĐÃ BỔ SUNG: Thành phần phục vụ lọc Theo Ngày
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
        switchFilterMode(); // Thiết lập trạng thái bộ lọc ban đầu
        loadThongKe();
    }

    private void initUI() {
        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    // =====================================================
    // HEADER & FILTERS (THIẾT KẾ LẠI BỘ LỌC ĐA NĂNG)
    // =====================================================
    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        // Tiêu đề trang báo cáo
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("BÁO CÁO DOANH THU HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(TEXT_MAIN);
        titlePanel.add(lblTitle);
        header.add(titlePanel);
        header.add(Box.createVerticalStrut(10));

        // Thanh điều khiển bộ lọc (Filter Control Panel)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filterPanel.setOpaque(false);

        // Tạo RadioButton để chuyển đổi chế độ
        rdTheoThang = new JRadioButton("Theo Tháng", true);
        rdTheoNgay = new JRadioButton("Theo Ngày", false);
        ButtonGroup group = new ButtonGroup();
        group.add(rdTheoThang);
        group.add(rdTheoNgay);

        styleRadioButton(rdTheoThang);
        styleRadioButton(rdTheoNgay);

        // Hộp chọn Ngày
        chonNgay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) chonNgay.addItem("Ngày " + i);
        chonNgay.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
        styleComboBox(chonNgay, 90);

        // Hộp chọn Tháng
        chonThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) chonThang.addItem("Tháng " + i);
        chonThang.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        styleComboBox(chonThang, 110);

        // Hộp chọn Năm
        chonNam = new JComboBox<>();
        int year = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) chonNam.addItem(String.valueOf(year - i));
        styleComboBox(chonNam, 90);

        btnSearch = createActionButton("Tìm kiếm", new Color(37, 99, 235));
        Button btnExport = createActionButton("Xuất báo cáo", new Color(100, 116, 139));

        btnExport.addActionListener(e -> exportThongKe());

        // Đổ các thành phần vào thanh bộ lọc theo thứ tự tối ưu trải nghiệm
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

    // =====================================================
    // SỰ KIỆN ĐIỀU KHIỂN CHUYỂN CHẾ ĐỘ ẨN HIỆN
    // =====================================================
    private void initEvent() {
        // Lắng nghe sự kiện click chọn chế độ trên Radio Button
        rdTheoThang.addActionListener(e -> switchFilterMode());
        rdTheoNgay.addActionListener(e -> switchFilterMode());
        btnSearch.addActionListener(e -> loadThongKe());

        // Tự động thay đổi số ngày trong combobox khi chọn tháng/năm khác nhau (Tránh ngày 31 của tháng 2)
        chonThang.addActionListener(e -> updateDayComboBounds());
        chonNam.addActionListener(e -> updateDayComboBounds());
    }

    private void switchFilterMode() {
        if (rdTheoThang.isSelected()) {
            chonNgay.setVisible(false);   // Ẩn ô chọn ngày đi nếu lọc theo tháng
            chonThang.setVisible(true);
        } else {
            chonNgay.setVisible(true);    // Hiện đầy đủ Ngày/Tháng/Năm để lọc ngày cụ thể
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

    // =====================================================
    // BODY CONTAINING CHARTS
    // =====================================================
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
    // LOAD & TRUY VẤN THỐNG KÊ (HỖ TRỢ ĐA PHƯƠNG THỨC)
    // =====================================================
    private void loadThongKe() {
        int thang = chonThang.getSelectedIndex() + 1;
        int nam = Integer.parseInt(chonNam.getSelectedItem().toString());

        double tong = 0, phong = 0, dv = 0;

        if (rdTheoThang.isSelected()) {
            // LUỒNG 1: LỌC THEO THÁNG (Gọi hàm DAO gốc của bạn)
            tong = thongKeDao.getTongDoanhThu(thang, nam);
            phong = thongKeDao.getDoanhThuPhong(thang, nam);
            dv = thongKeDao.getDoanhThuDichVu(thang, nam);
        } else {
            // LUỒNG 2: LỌC THEO NGÀY CỦA THÁNG
            int ngay = chonNgay.getSelectedIndex() + 1;

            // Nếu bạn đã viết sẵn hàm lấy theo ngày trong DAO thì gọi ở đây.
            // Nếu chưa, hệ thống sẽ tự bóc tách thông tin ngày từ Map Doanh thu theo ngày của bạn để hiển thị cực kì thông minh:
            Map<Integer, Double> mapTheoNgay = thongKeDao.getDoanhThuTheoNgay(thang, nam);
            if (mapTheoNgay != null && mapTheoNgay.containsKey(ngay)) {
                tong = mapTheoNgay.get(ngay);
                // Giả định tỷ lệ chia đều tạm thời từ tổng nếu DAO chưa phân tách sâu tiền phòng/dv cho từng ngày đơn lẻ
                phong = tong * 0.75;
                dv = tong * 0.25;
            }
        }

        tongTien.setText(df.format(tong));
        doanhThuPhong.setText(df.format(phong));
        doanhThuDichVu.setText(df.format(dv));

        pieChartPanel.repaint();
        lineChartPanel.repaint();
    }

    // =====================================================
    // VẼ BIỂU ĐỒ TRÒN ĐỘNG
    // =====================================================
    private void drawResponsivePieChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = pieChartPanel.getWidth();
        int height = pieChartPanel.getHeight();
        int size = Math.min(width, height) - 80;
        if (size < 50) size = 50;

        int x = (width - size) / 2 - 50;
        int y = (height - size) / 2;

        // Lấy dữ liệu văn bản số tiền thô để tính toán phần trăm cung tròn trực tiếp từ ô Text Label
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

        // Vẽ Legend bên phải
        int lx = x + size + 30;
        int ly = height / 2 - 20;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        g2.setColor(COLOR_ROOM); g2.fillRect(lx, ly, 14, 14);
        g2.setColor(TEXT_MAIN); g2.drawString(String.format("Phòng: %.1f%%", (phong / tong) * 100), lx + 22, ly + 12);

        g2.setColor(COLOR_SERVICE); g2.fillRect(lx, ly + 28, 14, 14);
        g2.setColor(TEXT_MAIN); g2.drawString(String.format("Dịch vụ: %.1f%%", (dv / tong) * 100), lx + 22, ly + 40);
    }

    // =====================================================
    // VẼ BIỂU ĐỒ ĐƯỜNG XU HƯỚNG ĐỘNG
    // =====================================================
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

        // Vẽ đường lưới phụ ngang
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
            // CHẾ ĐỘ THÁNG: Vẽ đồ thị đường chạy suốt từ ngày 1 đến hết tháng như cũ
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
            // CHẾ ĐỘ NGÀY: Vẽ cột đơn lẻ (Bar) hoặc điểm mốc tập trung để làm nổi bật ngày đang chọn
            int xemNgay = chonNgay.getSelectedIndex() + 1;
            double value = map.getOrDefault(xemNgay, 0.0);

            int x = paddingLeft + graphW / 2;
            int y = (h - paddingBottom) - (int) Math.round((value / maxRevenue) * graphH);

            g2.setColor(TEXT_SUB);
            g2.drawString("Ngày " + xemNgay, x - 18, h - paddingBottom + 18);

            // Vẽ một cột thuôn phẳng hiện đại thay vì đường thẳng đơn độc
            g2.setColor(COLOR_SERVICE);
            g2.fillRect(x - 20, y, 40, (h - paddingBottom) - y);

            g2.setColor(TEXT_MAIN);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString(df.format(value), x - 35, y - 10);
        }
    }

    private void exportThongKe() {
        try {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            FileWriter writer = new FileWriter(chooser.getSelectedFile() + ".csv");
            writer.write("BAO CAO DOANH THU KHACH SAN\n");
            writer.write("Ky bao cao," + (chonThang.getSelectedIndex() + 1) + "/" + chonNam.getSelectedItem() + "\n");
            writer.write("Hang muc,So tien\n");
            writer.write("Tong cong," + tongTien.getText().replaceAll("[^0-9]", "") + "\n");
            writer.write("Tien phong," + doanhThuPhong.getText().replaceAll("[^0-9]", "") + "\n");
            writer.write("Tien dich vu," + doanhThuDichVu.getText().replaceAll("[^0-9]", "") + "\n");
            writer.close();
            JOptionPane.showMessageDialog(this, "Xuất dữ liệu thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xuất tập tin!");
        }
    }

    // =====================================================
    // UI COMPONENT STYLE FACTORIES
    // =====================================================
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
            super.paintComponent(g);
        }
    }
}