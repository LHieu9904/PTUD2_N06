/*package GUI;

import Dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NhanPhongUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtTen, txtSDT, txtMa, txtSoNguoi;
    private JTextField txtNhan, txtTra;

    private JButton btnTra;

    private KhachHangDao khDao = new KhachHangDao();
    private HoaDonPhongDao hdDao = new HoaDonPhongDao();
    private ChiTietHoaDonPhongDao ctDao = new ChiTietHoaDonPhongDao();

    private String maNV;

    public NhanPhongUI(String maNV) {

        this.maNV = maNV;
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(245,245,245));

        // ===== TITLE =====
        JLabel title = new JLabel("NHẬN PHÒNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(title, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Mã phòng","Loại","Khách","SĐT"},0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách phòng đã đặt"));

        // ===== FORM =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nhận phòng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,10,8,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTen = new JTextField();
        txtSDT = new JTextField();
        txtMa = new JTextField();
        txtSoNguoi = new JTextField();
        txtNhan = new JTextField();
        txtTra = new JTextField();

        txtMa.setEditable(false);
        txtNhan.setEditable(false);

        btnTra = new JButton("⏱");

        int y = 0;

        addRow(form, gbc, y++, "Họ tên", txtTen);
        addRow(form, gbc, y++, "SĐT", txtSDT);
        addRow(form, gbc, y++, "Mã phòng", txtMa);
        addRow(form, gbc, y++, "Số người", txtSoNguoi);
        addRow(form, gbc, y++, "Giờ nhận", txtNhan);

        // ===== giờ trả =====
        gbc.gridx = 0; gbc.gridy = y;
        form.add(new JLabel("Giờ trả"), gbc);

        JPanel pTra = new JPanel(new BorderLayout(5,0));
        pTra.setBackground(Color.WHITE);
        pTra.add(txtTra, BorderLayout.CENTER);
        pTra.add(btnTra, BorderLayout.EAST);

        gbc.gridx = 1;
        form.add(pTra, gbc);

        // ===== BUTTON =====
        JButton btnOK = new JButton("Xác nhận");
        JButton btnCancel = new JButton("Hủy");

        btnOK.setBackground(new Color(0,153,255));
        btnOK.setForeground(Color.WHITE);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);
        right.add(form, BorderLayout.CENTER);
        right.add(btnPanel, BorderLayout.SOUTH);

        // ===== SPLIT =====
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                scroll,
                right
        );
        split.setDividerLocation(450);

        add(split, BorderLayout.CENTER);

        // ===== LOAD =====
        loadPhongDat();

        // ===== CLICK TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();
                if(row == -1) return;

                String maPhong = model.getValueAt(row,0).toString();

                txtMa.setText(maPhong);
                txtTen.setText(model.getValueAt(row,2).toString());
                txtSDT.setText(model.getValueAt(row,3).toString());

                Object[] info = new PhieuDatPhongDao().getThongTinNhanPhong(maPhong);

                if(info == null){
                    info = hdDao.getThongTinThanhToan(maPhong);
                }

                txtNhan.setText(info != null && info[0] != null
                        ? format(info[0]) : formatNow());

                if(info != null && info[1] != null){
                    txtTra.setText(format(info[1]));
                    txtTra.setEditable(false);
                    btnTra.setEnabled(false);
                }else{
                    txtTra.setText("");
                    txtTra.setEditable(true);
                    btnTra.setEnabled(true);
                }
            }
        });

        btnTra.addActionListener(e -> txtTra.setText(pickDate()));
        btnOK.addActionListener(e -> handleNhanPhong());
        btnCancel.addActionListener(e -> clearForm());
    }
    private void addRow(JPanel form, GridBagConstraints gbc,
                        int y, String label, JComponent field){

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        form.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(field, gbc);
    }

    // ================= LOAD =================
    private void loadPhongDat(){

        model.setRowCount(0);

        // 🔥 CHỈ LẤY PHÒNG CHƯA NHẬN
        List<Object[]> list = new PhieuDatPhongDao().getAllDatPhong();

        for(Object[] r : list){
            model.addRow(r);
        }
    }

    // ================= HANDLE =================
    private void handleNhanPhong(){

        try{

            String maPhong = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();

            if(maPhong.isEmpty()){
                JOptionPane.showMessageDialog(this, "Chưa chọn phòng!");
                return;
            }

            LocalDateTime nhan = parse(txtNhan.getText());
            LocalDateTime tra = txtTra.getText().isEmpty() ? null : parse(txtTra.getText());

            String maKH = khDao.getMaKHBySDT(sdt);
            if(maKH == null){
                maKH = khDao.insertKhach(ten, sdt);
            }

            String maPhieu = new PhieuDatPhongDao().getMaPhieuDatPhongByMaPhong(maPhong);

            String maHD = hdDao.getMaHDByMaPhieu(maPhieu);

            if(maHD == null){
                maHD = hdDao.getNextMaHD();
                hdDao.insertCoPhieuDatPhong(maHD, maNV, maKH, maPhieu);
            }

            if(!ctDao.exists(maHD, maPhong)){
                ctDao.insert(maHD, maPhong, java.sql.Timestamp.valueOf(nhan));
            }

            ctDao.updateThoiGianNhanTraHoaDon(maHD, maPhong, nhan, tra);
            ctDao.updateTrangThai(maHD, maPhong, "Đang dùng");
            new PhongDao().updateTrangThai(
                    maPhong,
                    "Đang thuê"
            );

            JOptionPane.showMessageDialog(this, "Nhận phòng thành công!");

            loadPhongDat(); // 🔥 giờ sẽ biến mất
            clearForm();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // ================= UTIL =================
    private String pickDate(){
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        sp.setEditor(new JSpinner.DateEditor(sp,"dd/MM/yyyy HH:mm"));

        if(JOptionPane.showConfirmDialog(this, sp,"Chọn giờ",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
            return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format((java.util.Date) sp.getValue());
        }
        return "";
    }

    private String format(Object o){
        return ((java.sql.Timestamp)o)
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private String formatNow(){
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private LocalDateTime parse(String s){
        return LocalDateTime.parse(s,
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private void clearForm(){
        txtTen.setText("");
        txtSDT.setText("");
        txtMa.setText("");
        txtNhan.setText("");
        txtTra.setText("");
    }
}*/
package GUI;

import Dao.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NhanPhongUI extends JPanel {

    // =====================================================
    // COLOR
    // =====================================================

    private final Color BG =
            new Color(245,247,250);

    private final Color CARD =
            Color.WHITE;

    private final Color PRIMARY =
            new Color(79,70,229);

    private final Color PRIMARY_HOVER =
            new Color(67,56,202);

    private final Color SUCCESS =
            new Color(16,185,129);

    private final Color SUCCESS_HOVER =
            new Color(5,150,105);

    private final Color DANGER =
            new Color(239,68,68);

    private final Color DANGER_HOVER =
            new Color(220,38,38);

    private final Color TEXT =
            new Color(15,23,42);

    private final Color SUBTEXT =
            new Color(100,116,139);

    private final Color BORDER =
            new Color(226,232,240);

    // =====================================================
    // COMPONENT
    // =====================================================

    private JTable table;

    private DefaultTableModel model;

    private JTextField txtTen;

    private JTextField txtSDT;

    private JTextField txtMa;

    private JTextField txtSoNguoi;

    private JTextField txtNhan;

    private JTextField txtTra;

    private JButton btnTra;

    private String maNV;

    // =====================================================
    // DAO
    // =====================================================

    private KhachHangDao khDao =
            new KhachHangDao();

    private HoaDonPhongDao hdDao =
            new HoaDonPhongDao();

    private ChiTietHoaDonPhongDao ctDao =
            new ChiTietHoaDonPhongDao();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NhanPhongUI(String maNV) {

        this.maNV = maNV;

        try {

            UIManager.setLookAndFeel(
                    new FlatLightLaf()
            );

        } catch (Exception ignored) {
        }

        setLayout(new BorderLayout(24,24));

        setBackground(BG);

        setBorder(
                new EmptyBorder(
                        24,24,24,24
                )
        );

        // =====================================================
        // TOP
        // =====================================================

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JLabel title =
                new JLabel("NHẬN PHÒNG");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        title.setForeground(TEXT);

        JLabel sub =
                new JLabel(
                        "Quản lý thông tin nhận phòng khách hàng"
                );

        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        17
                )
        );

        sub.setForeground(SUBTEXT);

        JPanel titleWrap =
                new JPanel();

        titleWrap.setLayout(
                new BoxLayout(
                        titleWrap,
                        BoxLayout.Y_AXIS
                )
        );

        titleWrap.setOpaque(false);

        titleWrap.add(title);

        titleWrap.add(Box.createVerticalStrut(4));

        titleWrap.add(sub);

        top.add(titleWrap, BorderLayout.WEST);

        add(top, BorderLayout.NORTH);

        // =====================================================
        // TABLE
        // =====================================================

        model = new DefaultTableModel(
                new String[]{
                        "Mã phòng",
                        "Loại phòng",
                        "Khách hàng",
                        "SĐT"
                },
                0
        );

        table = new JTable(model){

            public boolean isCellEditable(
                    int row,
                    int column
            ){
                return false;
            }
        };

        table.setRowHeight(60);

        table.setShowVerticalLines(false);

        table.setShowHorizontalLines(false);

        table.setIntercellSpacing(
                new Dimension(0,12)
        );

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        table.setSelectionBackground(
                new Color(224,231,255)
        );

        table.setSelectionForeground(TEXT);

        table.setGridColor(BG);

        table.setBackground(CARD);

        table.setFocusable(false);

        table.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "rowHeight:60;"
                        + "showHorizontalLines:false;"
                        + "showVerticalLines:false;"
                        + "intercellSpacing:0,12"
        );

        JTableHeader header =
                table.getTableHeader();

        header.setPreferredSize(
                new Dimension(0,58)
        );

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        header.setBackground(
                new Color(30,41,59)
        );

        header.setForeground(Color.WHITE);

        header.setBorder(null);

        header.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "height:58;"
                        + "separatorColor:#1E293B"
        );

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                JLabel.CENTER
        );

        for(int i = 0;
            i < table.getColumnCount();
            i++){

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBorder(null);

        scroll.getViewport()
                .setBackground(CARD);

        scroll.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:24;"
                        + "border:0,0,0,0"
        );

        // =====================================================
        // LEFT CARD
        // =====================================================

        JPanel left =
                createCard();

        left.setLayout(
                new BorderLayout(0,20)
        );

        left.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:30;"
        );

        JLabel lbDS =
                new JLabel(
                        "DANH SÁCH PHÒNG ĐÃ ĐẶT"
                );

        lbDS.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lbDS.setForeground(TEXT);

        left.add(lbDS, BorderLayout.NORTH);

        left.add(scroll, BorderLayout.CENTER);

        // =====================================================
        // RIGHT CARD
        // =====================================================

        JPanel right =
                createCard();

        right.setLayout(
                new BorderLayout(0,24)
        );

        right.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:30;"
        );

        JLabel lbInfo =
                new JLabel(
                        "THÔNG TIN NHẬN PHÒNG"
                );

        lbInfo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        lbInfo.setForeground(TEXT);

        right.add(lbInfo, BorderLayout.NORTH);

        // =====================================================
        // FORM
        // =====================================================

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(12,10,12,10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        txtTen = modernField();

        txtSDT = modernField();

        txtMa = modernField();

        txtSoNguoi = modernField();

        txtNhan = modernField();

        txtTra = modernField();

        txtMa.setEditable(false);

        txtNhan.setEditable(false);

        txtMa.setBackground(
                new Color(248,250,252)
        );

        txtNhan.setBackground(
                new Color(248,250,252)
        );

        int y = 0;

        addField(
                form,
                gbc,
                y++,
                "Họ tên khách",
                txtTen
        );

        addField(
                form,
                gbc,
                y++,
                "Số điện thoại",
                txtSDT
        );

        addField(
                form,
                gbc,
                y++,
                "Mã phòng",
                txtMa
        );

        addField(
                form,
                gbc,
                y++,
                "Số người",
                txtSoNguoi
        );

        addField(
                form,
                gbc,
                y++,
                "Giờ nhận",
                txtNhan
        );

        // =====================================================
        // GIỜ TRẢ
        // =====================================================

        gbc.gridx = 0;

        gbc.gridy = y;

        JLabel lbTra =
                createLabel("Giờ trả");

        form.add(lbTra, gbc);

        JPanel pTra =
                new JPanel(
                        new BorderLayout(10,0)
                );

        pTra.setOpaque(false);

        btnTra =
                new JButton("🗓");

        btnTra.setPreferredSize(
                new Dimension(55,48)
        );

        btnTra.setBackground(PRIMARY);

        btnTra.setForeground(Color.WHITE);

        btnTra.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        18
                )
        );

        btnTra.setFocusPainted(false);

        btnTra.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btnTra.setBorder(null);

        btnTra.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:20;"
                        + "focusWidth:0;"
                        + "borderWidth:0"
        );

        pTra.add(txtTra, BorderLayout.CENTER);

        pTra.add(btnTra, BorderLayout.EAST);

        gbc.gridx = 1;

        gbc.weightx = 1;

        form.add(pTra, gbc);

        right.add(form, BorderLayout.CENTER);

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel btnPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                16,
                                0
                        )
                );

        btnPanel.setOpaque(false);

        JButton btnOK =
                modernButton(
                        "XÁC NHẬN",
                        SUCCESS
                );

        JButton btnCancel =
                modernButton(
                        "HỦY",
                        DANGER
                );

        addHover(
                btnOK,
                SUCCESS,
                SUCCESS_HOVER
        );

        addHover(
                btnCancel,
                DANGER,
                DANGER_HOVER
        );

        addHover(
                btnTra,
                PRIMARY,
                PRIMARY_HOVER
        );

        btnPanel.add(btnOK);

        btnPanel.add(btnCancel);

        right.add(btnPanel, BorderLayout.SOUTH);

        // =====================================================
        // SPLIT
        // =====================================================

        JSplitPane split =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        left,
                        right
                );

        split.setDividerLocation(650);

        split.setDividerSize(10);

        split.setContinuousLayout(true);

        split.setBorder(null);

        split.setOpaque(false);

        split.setBackground(BG);

        split.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "dividerSize:10"
        );

        add(split, BorderLayout.CENTER);

        // =====================================================
        // LOAD
        // =====================================================

        loadPhongDat();

        // =====================================================
        // EVENT TABLE
        // =====================================================

        table.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    public void mouseClicked(
                            java.awt.event.MouseEvent evt
                    ) {

                        int row =
                                table.getSelectedRow();

                        if(row == -1) return;

                        String maPhong =
                                model.getValueAt(
                                        row,
                                        0
                                ).toString();

                        txtMa.setText(maPhong);

                        txtTen.setText(
                                model.getValueAt(
                                        row,
                                        2
                                ).toString()
                        );

                        txtSDT.setText(
                                model.getValueAt(
                                        row,
                                        3
                                ).toString()
                        );

                        Object[] info =
                                new PhieuDatPhongDao()
                                        .getThongTinNhanPhong(
                                                maPhong
                                        );

                        if(info == null){

                            info =
                                    hdDao.getThongTinThanhToan(
                                            maPhong
                                    );
                        }

                        txtNhan.setText(
                                info != null
                                        && info[0] != null
                                        ? format(info[0])
                                        : formatNow()
                        );

                        if(info != null
                                && info[1] != null){

                            txtTra.setText(
                                    format(info[1])
                            );

                            txtTra.setEditable(false);

                            btnTra.setEnabled(false);

                        }else{

                            txtTra.setText("");

                            txtTra.setEditable(true);

                            btnTra.setEnabled(true);
                        }
                    }
                });

        // =====================================================
        // EVENT
        // =====================================================

        btnTra.addActionListener(
                e -> txtTra.setText(
                        pickDate()
                )
        );

        btnOK.addActionListener(
                e -> handleNhanPhong()
        );

        btnCancel.addActionListener(
                e -> clearForm()
        );
    }

    // =====================================================
    // UI
    // =====================================================

    private JPanel createCard(){

        JPanel p =
                new JPanel();

        p.setBackground(CARD);

        p.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:28;"
                        + "background:#FFFFFF;"
                        + "border:1,1,1,1,#E2E8F0;"
                        + "shadowColor:rgba(0,0,0,0.08);"
                        + "shadowSize:12"
        );

        p.setBorder(
                new EmptyBorder(
                        28,28,28,28
                )
        );

        return p;
    }

    private JTextField modernField(){

        JTextField txt =
                new JTextField();

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        txt.setPreferredSize(
                new Dimension(320,52)
        );

        txt.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:20;"
                        + "borderWidth:1;"
                        + "focusWidth:1;"
                        + "innerFocusWidth:0;"
                        + "borderColor:#CBD5E1;"
                        + "focusedBorderColor:#4F46E5;"
                        + "background:#FFFFFF;"
                        + "margin:8,14,8,14"
        );

        return txt;
    }

    private JButton modernButton(
            String text,
            Color bg
    ){

        JButton btn =
                new JButton(text);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        btn.setForeground(Color.WHITE);

        btn.setBackground(bg);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setFocusPainted(false);

        btn.setBorder(null);

        btn.setPreferredSize(
                new Dimension(190,54)
        );

        btn.putClientProperty(
                FlatClientProperties.STYLE,
                ""
                        + "arc:22;"
                        + "focusWidth:0;"
                        + "borderWidth:0"
        );

        return btn;
    }

    private JLabel createLabel(String t){

        JLabel lb =
                new JLabel(t);

        lb.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lb.setForeground(
                new Color(51,65,85)
        );

        return lb;
    }

    private void addField(
            JPanel form,
            GridBagConstraints gbc,
            int y,
            String label,
            JComponent field
    ){

        gbc.gridx = 0;

        gbc.gridy = y;

        gbc.weightx = 0;

        JLabel lb =
                createLabel(label);

        form.add(lb, gbc);

        gbc.gridx = 1;

        gbc.weightx = 1;

        form.add(field, gbc);
    }

    private void addHover(
            JButton btn,
            Color normal,
            Color hover
    ){

        btn.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    public void mouseEntered(
                            java.awt.event.MouseEvent evt
                    ){

                        btn.setBackground(hover);
                    }

                    public void mouseExited(
                            java.awt.event.MouseEvent evt
                    ){

                        btn.setBackground(normal);
                    }
                });
    }

    // =====================================================
    // LOAD
    // =====================================================

    private void loadPhongDat(){

        model.setRowCount(0);

        List<Object[]> list =
                new PhieuDatPhongDao()
                        .getAllDatPhong();

        for(Object[] r : list){

            model.addRow(r);
        }
    }

    // =====================================================
    // HANDLE
    // =====================================================

    private void handleNhanPhong(){

        try{

            String maPhong =
                    txtMa.getText().trim();

            String ten =
                    txtTen.getText().trim();

            String sdt =
                    txtSDT.getText().trim();

            if(maPhong.isEmpty()){

                JOptionPane.showMessageDialog(
                        this,
                        "Chưa chọn phòng!"
                );

                return;
            }

            LocalDateTime nhan =
                    parse(txtNhan.getText());

            LocalDateTime tra =
                    txtTra.getText().isEmpty()
                            ? null
                            : parse(txtTra.getText());

            String maKH =
                    khDao.getMaKHBySDT(sdt);

            if(maKH == null){

                maKH =
                        khDao.insertKhach(
                                ten,
                                sdt
                        );
            }

            String maPhieu =
                    new PhieuDatPhongDao()
                            .getMaPhieuDatPhongByMaPhong(
                                    maPhong
                            );

            String maHD =
                    hdDao.getMaHDByMaPhieu(maPhieu);

            if(maHD == null){

                maHD =
                        hdDao.getNextMaHD();

                hdDao.insertCoPhieuDatPhong(
                        maHD,
                        maNV,
                        maKH,
                        maPhieu
                );
            }

            if(!ctDao.exists(maHD, maPhong)){

                ctDao.insert(
                        maHD,
                        maPhong,
                        java.sql.Timestamp.valueOf(
                                nhan
                        )
                );
            }

            ctDao.updateThoiGianNhanTraHoaDon(
                    maHD,
                    maPhong,
                    nhan,
                    tra
            );

            ctDao.updateTrangThai(
                    maHD,
                    maPhong,
                    "Đang dùng"
            );

            new PhongDao().updateTrangThai(
                    maPhong,
                    "Đang thuê"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Nhận phòng thành công!"
            );

            loadPhongDat();

            clearForm();

        }catch(Exception e){

            e.printStackTrace();
        }
    }

    // =====================================================
    // UTIL
    // =====================================================

    private String pickDate(){

        JSpinner sp =
                new JSpinner(
                        new SpinnerDateModel()
                );

        sp.setEditor(
                new JSpinner.DateEditor(
                        sp,
                        "dd/MM/yyyy HH:mm"
                )
        );

        if(JOptionPane.showConfirmDialog(
                this,
                sp,
                "Chọn giờ",
                JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION){

            return new java.text.SimpleDateFormat(
                    "dd/MM/yyyy HH:mm"
            ).format(
                    (java.util.Date)
                            sp.getValue()
            );
        }

        return "";
    }

    private String format(Object o){

        return ((java.sql.Timestamp)o)
                .toLocalDateTime()
                .format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        )
                );
    }

    private String formatNow(){

        return LocalDateTime.now()
                .format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        )
                );
    }

    private LocalDateTime parse(String s){

        return LocalDateTime.parse(
                s,
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                )
        );
    }

    private void clearForm(){

        txtTen.setText("");

        txtSDT.setText("");

        txtMa.setText("");

        txtNhan.setText("");

        txtTra.setText("");

        txtSoNguoi.setText("");
    }
}