package GUI;

import Dao.KhuyenMaiDao;
import Entity.KhuyenMai;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KhuyenMaiUI extends JPanel {

    // ================= COLOR =================
    private final Color BACKGROUND = new Color(241,245,249);
    private final Color CARD = Color.WHITE;
    private final Color PRIMARY = new Color(37,99,235);
    private final Color SUCCESS = new Color(16,185,129);
    private final Color WARNING = new Color(245,158,11);
    private final Color DANGER = new Color(239,68,68);
    private final Color BORDER = new Color(226,232,240);
    private final Color TEXT = new Color(30,41,59);

    // ================= COMPONENT =================
    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaKM;
    private JTextField txtTenKM;
    private JTextField txtPhanTram;
    private JComboBox<String> cbTrangThai;
    private JDateChooser dateBD;
    private JDateChooser dateKT;

    private KhuyenMaiDao dao = new KhuyenMaiDao();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public KhuyenMaiUI() {

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        // ================= TITLE =================
        JLabel title = new JLabel("QUẢN LÝ KHUYẾN MÃI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT);
        title.setBorder(new EmptyBorder(20,0,10,0));

        // ================= FORM =================
        JPanel form = new JPanel(new GridLayout(3,4,30,20));
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(25,25,25,25)
        ));

        // ================= INPUT =================
        txtMaKM = createTextField();
        txtMaKM.setEditable(false); // KHÓA: Mã tự động phát sinh, người dùng không tự sửa
        txtMaKM.setBackground(new Color(241, 245, 249)); // Đổi nền nhận biết ô bị khóa

        txtTenKM = createTextField();
        txtPhanTram = createTextField();

        cbTrangThai = new JComboBox<>(new String[]{"Áp dụng", "Ngưng"});
        styleCombo(cbTrangThai);
        cbTrangThai.setEnabled(true); // Cho phép người dùng chủ động chọn ngưng khi sửa

        dateBD = new JDateChooser();
        dateKT = new JDateChooser();
        styleDateChooser(dateBD);
        styleDateChooser(dateKT);

        dateBD.setDateFormatString("dd/MM/yyyy");
        dateKT.setDateFormatString("dd/MM/yyyy");

        ((JTextField) dateBD.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateKT.getDateEditor().getUiComponent()).setEditable(false);

        // ================= ADD FORM =================
        form.add(label("Mã KM (Tự sinh)")); form.add(wrap(txtMaKM));
        form.add(label("Tên KM")); form.add(wrap(txtTenKM));
        form.add(label("% Giảm")); form.add(wrap(txtPhanTram));
        form.add(label("Trạng thái")); form.add(wrap(cbTrangThai));
        form.add(label("Ngày BD")); form.add(wrap(dateBD));
        form.add(label("Ngày KT")); form.add(wrap(dateKT));

        // ================= BUTTON =================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        btnPanel.setBackground(BACKGROUND);

        JButton btnThem = createButton("THÊM", SUCCESS);
        JButton btnSua = createButton("SỬA", PRIMARY);
        JButton btnXoa = createButton("XÓA", DANGER);
        JButton btnReload = createButton("LÀM MỚI", WARNING);

        btnPanel.add(btnThem); btnPanel.add(btnSua);
        btnPanel.add(btnXoa); btnPanel.add(btnReload);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Mã KM", "Tên KM", "%", "BD", "KT", "Trạng thái"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(191,219,254));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(TEXT);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0,42));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BACKGROUND);
        tableWrapper.setBorder(new EmptyBorder(20,40,20,40));
        tableWrapper.add(scroll);

        // ================= MAIN =================
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(BACKGROUND);

        main.add(form);
        main.add(Box.createVerticalStrut(10));
        main.add(btnPanel);
        main.add(Box.createVerticalStrut(20));
        main.add(tableWrapper);

        add(title, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        // =====================================================
        // EVENT TABLE
        // =====================================================
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = table.getSelectedRow();
                txtMaKM.setText(val(r,0));
                txtTenKM.setText(val(r,1));
                txtPhanTram.setText(val(r,2));
                cbTrangThai.setSelectedItem(val(r,5));

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    java.util.Date bd = java.util.Date.from(LocalDate.parse(val(r,3), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.util.Date kt = java.util.Date.from(LocalDate.parse(val(r,4), formatter).atStartOfDay(ZoneId.systemDefault()).toInstant());

                    dateBD.setDate(bd);
                    dateKT.setDate(kt);
                } catch (Exception e){
                    dateBD.setDate(null);
                    dateKT.setDate(null);
                }
            }
        });

        // =====================================================
        // EVENT ADD
        // =====================================================
        btnThem.addActionListener(e -> {
            if(!validateInput()){
                return;
            }

            // Tự động đồng bộ hiển thị trạng thái lên ComboBox theo mốc thời gian lúc lưu dữ liệu
            LocalDate homNay = LocalDate.now();
            LocalDate localBD = new Date(dateBD.getDate().getTime()).toLocalDate();
            LocalDate localKT = new Date(dateKT.getDate().getTime()).toLocalDate();
            if (homNay.isBefore(localBD) || homNay.isAfter(localKT)) {
                cbTrangThai.setSelectedItem("Ngưng");
            } else {
                cbTrangThai.setSelectedItem("Áp dụng");
            }

            KhuyenMai km = getForm();
            if(km != null && dao.insert(km)){
                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                loadData();
                clearForm(); // Gọi để tự động cập nhật và sinh mã tiếp theo
            }
        });

        // =====================================================
        // EVENT UPDATE
        // =====================================================
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình khuyến mãi trên danh sách!");
                return;
            }

            if(!validateInput()){
                return;
            }

            KhuyenMai km = getForm();
            if(km != null && dao.update(km)){
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            }
        });

        // =====================================================
        // EVENT DELETE
        // =====================================================
        btnXoa.addActionListener(e -> {
            String ma = txtMaKM.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Xóa mã khuyến mãi " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if(dao.delete(ma)){
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadData();
                    clearForm();
                }
            }
        });

        // =====================================================
        // EVENT RELOAD
        // =====================================================
        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });

        // Tải dữ liệu và đồng bộ sinh mã cho lượt nhập đầu tiên ngay khi chạy ứng dụng
        loadData();
        clearForm();
    }

    // =====================================================
    // UI HELPER METHODS
    // =====================================================
    private JTextField createTextField(){
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(200,40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        return txt;
    }

    private void styleCombo(JComboBox<String> cb){
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
    }

    private void styleDateChooser(JDateChooser dc){
        dc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dc.setBorder(BorderFactory.createLineBorder(BORDER));
        JTextField txt = (JTextField) dc.getDateEditor().getUiComponent();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
    }

    private JButton createButton(String text, Color bg){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12,22,12,22));
        return btn;
    }

    private JPanel wrap(JComponent c){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.add(c);
        return p;
    }

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lb.setForeground(TEXT);
        return lb;
    }

    private String val(int r,int c){
        Object v = model.getValueAt(r,c);
        return v == null ? "" : v.toString();
    }

    // =====================================================
    // LOGIC TỰ ĐỘNG PHÁT SINH MÃ TĂNG DẦN THEO DANH SÁCH BẢNG
    // =====================================================
    private String phatSinhMaKM() {
        int max = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String maHienTai = model.getValueAt(i, 0).toString().trim();
            try {
                int so = Integer.parseInt(maHienTai.substring(2));
                if (so > max) {
                    max = so;
                }
            } catch (Exception e) {
                // Bỏ qua lỗi format chuỗi số trống
            }
        }
        return String.format("KM%03d", max + 1);
    }

    // =====================================================
    // GET FORM
    // =====================================================
    private KhuyenMai getForm(){
        try{
            double pt = Double.parseDouble(txtPhanTram.getText().trim());

            if(pt < 0 || pt > 100){
                JOptionPane.showMessageDialog(this,"% 0-100");
                return null;
            }

            Date bd = new Date(dateBD.getDate().getTime());
            Date kt = new Date(dateKT.getDate().getTime());

            if(kt.before(bd)){
                JOptionPane.showMessageDialog(this,"Sai ngày!");
                return null;
            }

            String trangThaiFinal = "Áp dụng";
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                // KHI THÊM: Tự động gán trạng thái thông minh theo ngày hệ thống
                LocalDate homNay = LocalDate.now();
                LocalDate localBD = bd.toLocalDate();
                LocalDate localKT = kt.toLocalDate();

                if (homNay.isBefore(localBD) || homNay.isAfter(localKT)) {
                    trangThaiFinal = "Ngưng";
                }
            } else {
                // KHI SỬA: Lấy trực tiếp từ ComboBox để người dùng thoải mái ép "Ngưng"
                trangThaiFinal = cbTrangThai.getSelectedItem().toString();
            }

            return new KhuyenMai(
                    txtMaKM.getText().trim(),
                    txtTenKM.getText().trim(),
                    pt,
                    bd.toLocalDate(),
                    kt.toLocalDate(),
                    trangThaiFinal
            );

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Lỗi dữ liệu!");
            return null;
        }
    }

    // =====================================================
    // LOAD DATA
    // =====================================================
    private void loadData(){
        model.setRowCount(0);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<KhuyenMai> list = dao.getAll();

        for(KhuyenMai km : list){
            model.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getPhanTramGiam(),
                    km.getNgayBatDau().format(f),
                    km.getNgayKetThuc().format(f),
                    km.getTrangThai()
            });
        }
    }

    // =====================================================
    // CLEAR FORM
    // =====================================================
    private void clearForm(){
        txtMaKM.setText(phatSinhMaKM()); // TỰ ĐỘNG PHÁT SINH MÃ VÀ ĐIỀN SẴN VÀO FORM KHÔNG LO BỊ TRỐNG MÃ

        txtTenKM.setText("");
        txtPhanTram.setText("");
        cbTrangThai.setSelectedIndex(0);
        dateBD.setDate(null);
        dateKT.setDate(null);
        table.clearSelection();
    }

    // =====================================================
    // VALIDATE INPUT (GIỮ NGUYÊN GỐC 100%)
    // =====================================================
    private boolean validateInput() {
        String ma = txtMaKM.getText().trim();
        String ten = txtTenKM.getText().trim();
        String pt = txtPhanTram.getText().trim();
        java.util.Date ngayBD = dateBD.getDate();
        java.util.Date ngayKT = dateKT.getDate();

        // ===== MÃ =====
        if(!ma.matches("^KM\\d{3}$")){
            JOptionPane.showMessageDialog(this, "Mã KM phải dạng KM001!");
            return false;
        }

        // ===== TÊN =====
        if(ten.isEmpty()){
            JOptionPane.showMessageDialog(this, "Tên khuyến mãi không được rỗng!");
            return false;
        }

        // ===== PHẦN TRĂM =====
        try{
            double giam = Double.parseDouble(pt);
            if(giam < 0 || giam > 100){
                JOptionPane.showMessageDialog(this, "% giảm phải từ 0 - 100!");
                return false;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Phần trăm giảm không hợp lệ!");
            return false;
        }

        // ===== DATE NULL =====
        if(ngayBD == null || ngayKT == null){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
            return false;
        }

        // ===== NGÀY =====
        if(ngayBD.after(ngayKT)){
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải <= ngày kết thúc!");
            return false;
        }
        if(ngayKT.before(new java.util.Date())){
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải >= ngày hiện tại!");
            return false;
        }

        return true;
    }
}