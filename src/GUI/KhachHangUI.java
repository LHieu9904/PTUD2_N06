/*
package GUI;

import Dao.KhachHangDao;
import Entity.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhachHangUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtSDT, txtCCCD;
    private JComboBox<String> cbGioiTinh;

    private JButton btnThem, btnSua, btnXoa, btnReload;

    public KhachHangUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(3,4,30,20));
        form.setBorder(BorderFactory.createEmptyBorder(20,40,10,40));

        txtMa = new JTextField();
        txtMa.setEditable(false); //

        txtTen = new JTextField();
        txtSDT = new JTextField();
        txtCCCD = new JTextField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});

        form.add(label("Mã KH")); form.add(txtMa);
        form.add(label("Tên KH")); form.add(txtTen);

        form.add(label("Giới tính")); form.add(cbGioiTinh);
        form.add(label("SĐT")); form.add(txtSDT);

        form.add(label("CCCD")); form.add(txtCCCD);

        // ===== BUTTON =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245,245,245));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnReload = new JButton("Làm mới");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnReload);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Mã KH","Tên KH","Giới tính","SĐT","CCCD"},0
        );

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));
        tableWrapper.add(scroll);

        // ===== MAIN =====
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245,245,245));

        main.add(form);
        main.add(Box.createVerticalStrut(10));
        main.add(btnPanel);
        main.add(Box.createVerticalStrut(20));
        main.add(tableWrapper);

        add(title, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        // ===== EVENT TABLE =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();

                txtMa.setText(model.getValueAt(row, 0).toString());
                txtTen.setText(model.getValueAt(row, 1).toString());

                cbGioiTinh.setSelectedItem(model.getValueAt(row, 2).toString());


                txtSDT.setText(model.getValueAt(row, 3).toString());
                txtCCCD.setText(model.getValueAt(row, 4).toString());
            }
        });

        // ===== EVENT BUTTON =====
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });

        loadData();
    }

    // ================= FUNCTION =================

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lb;
    }

    private void loadData() {
        model.setRowCount(0);

        KhachHangDao dao = new KhachHangDao();
        List<KhachHang> list = dao.getAll();

        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getGioiTinh() == 1 ? "Nam" : "Nữ",
                    kh.getSdt(),
                    kh.getCccd()
            });
        }
    }

    private void clearForm(){
        txtMa.setText("");
        txtTen.setText("");
        txtSDT.setText("");
        txtCCCD.setText("");
        cbGioiTinh.setSelectedIndex(0);
    }

    // ===== VALIDATE =====
    private boolean validateInput(){

        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String cccd = txtCCCD.getText().trim();

        if(ten.isEmpty()){
            JOptionPane.showMessageDialog(this,"Tên không được rỗng");
            return false;
        }

        if(!sdt.matches("^(03|05|07|09)\\d{8}$")){
            JOptionPane.showMessageDialog(this,"SĐT không hợp lệ");
            return false;
        }
        cccd = cccd.trim();

        if (!cccd.matches("^(\\d{3})([0-3])(\\d{2})(\\d{6})$")) {
            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ");
            return false;
        }

        return true;
    }

    // ===== THÊM =====
    private void them(){
        if(!validateInput()) return;

        KhachHangDao dao = new KhachHangDao();

        if(dao.isDuplicate(txtSDT.getText(), txtCCCD.getText())){
            JOptionPane.showMessageDialog(this,"Trùng SĐT hoặc CCCD");
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setMaKH(dao.getNextMaKH());
        kh.setHoTen(txtTen.getText());
        kh.setGioiTinh(Integer.parseInt(String.valueOf(cbGioiTinh.getSelectedIndex())));
        kh.setSdt(txtSDT.getText());
        kh.setCccd(txtCCCD.getText());

        if(dao.create(kh)){
            JOptionPane.showMessageDialog(this,"Thêm thành công");
            loadData();
            clearForm();
        }
    }

    // ===== SỬA =====
    private void sua(){
        if(!validateInput()) return;

        KhachHangDao dao = new KhachHangDao();

        KhachHang kh = new KhachHang();
        kh.setMaKH(txtMa.getText());
        kh.setHoTen(txtTen.getText());
        kh.setGioiTinh(Integer.parseInt(String.valueOf(cbGioiTinh.getSelectedIndex() == 0 ? 1 : 0)));
        kh.setSdt(txtSDT.getText());
        kh.setCccd(txtCCCD.getText());

        if(dao.update(kh)){
            JOptionPane.showMessageDialog(this,"Sửa thành công");
            loadData();
        }
    }

    // ===== XÓA =====
    private void xoa(){
        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Chọn khách hàng để xóa");
            return;
        }

        String ma = model.getValueAt(row,0).toString();

        KhachHangDao dao = new KhachHangDao();

        if(dao.delete(ma)){
            JOptionPane.showMessageDialog(this,"Xóa thành công");
            loadData();
            clearForm();
        }
    }
}
*/
package GUI;

import Dao.KhachHangDao;
import Entity.KhachHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class KhachHangUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtSDT, txtCCCD;
    private JComboBox<String> cbGioiTinh;

    private JButton btnThem, btnSua, btnXoa, btnReload;

    public KhachHangUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ================= TITLE =================
        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(30, 41, 59));
        title.setBorder(new EmptyBorder(20, 30, 10, 0));

        // ================= FORM PANEL =================
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(15, 25, 15, 25),
                new LineBorder(new Color(230, 230, 230), 1, true)
        ));

        JLabel formTitle = new JLabel("Thông tin khách hàng");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(new Color(51, 65, 85));
        formTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel form = new JPanel(new GridLayout(3, 4, 25, 18));
        form.setBackground(Color.WHITE);

        txtMa = createTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(248, 250, 252));

        txtTen = createTextField();
        txtSDT = createTextField();
        txtCCCD = createTextField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbGioiTinh.setPreferredSize(new Dimension(100, 40));

        form.add(label("Mã khách hàng"));
        form.add(txtMa);

        form.add(label("Tên khách hàng"));
        form.add(txtTen);

        form.add(label("Giới tính"));
        form.add(cbGioiTinh);

        form.add(label("Số điện thoại"));
        form.add(txtSDT);

        form.add(label("CCCD"));
        form.add(txtCCCD);

        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(form, BorderLayout.CENTER);

        // ================= BUTTON =================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        btnPanel.setBackground(new Color(245, 247, 250));

        btnThem = createButton("THÊM", new Color(37, 99, 235));
        btnSua = createButton("SỬA", new Color(234, 179, 8));
        btnXoa = createButton("XÓA", new Color(220, 38, 38));
        btnReload = createButton("LÀM MỚI", new Color(100, 116, 139));

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnReload);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{"Mã KH", "Tên KH", "Giới tính", "SĐT", "CCCD"}, 0
        );

        table = new JTable(model);

        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(235, 235, 235));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(30, 41, 59));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 42));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 25, 25, 25),
                new EmptyBorder(0, 0, 0, 0)
        ));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("Danh sách khách hàng");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableTitle.setForeground(new Color(51, 65, 85));
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        tableWrapper.add(tableTitle, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);

        tableCard.add(tableWrapper);

        // ================= MAIN =================
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(new EmptyBorder(0, 20, 20, 20));

        main.add(formCard);
        main.add(Box.createVerticalStrut(15));
        main.add(btnPanel);
        main.add(Box.createVerticalStrut(15));
        main.add(tableCard);

        add(title, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);

        // ================= EVENT TABLE =================
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();

                txtMa.setText(model.getValueAt(row, 0).toString());
                txtTen.setText(model.getValueAt(row, 1).toString());

                cbGioiTinh.setSelectedItem(model.getValueAt(row, 2).toString());

                txtSDT.setText(model.getValueAt(row, 3).toString());
                txtCCCD.setText(model.getValueAt(row, 4).toString());
            }
        });

        // ================= EVENT BUTTON =================
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());

        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });

        loadData();
    }

    // ================= UI COMPONENT =================

    private JLabel label(String t) {

        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lb.setForeground(new Color(71, 85, 105));

        return lb;
    }

    private JTextField createTextField() {

        JTextField txt = new JTextField();

        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setPreferredSize(new Dimension(100, 40));

        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        return txt;
    }

    private JButton createButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);

        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(130, 42));

        return btn;
    }

    // ================= FUNCTION =================

    private void loadData() {

        model.setRowCount(0);

        KhachHangDao dao = new KhachHangDao();
        List<KhachHang> list = dao.getAll();

        for (KhachHang kh : list) {

            model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoTen(),
                    kh.getGioiTinh() == 1 ? "Nam" : "Nữ",
                    kh.getSdt(),
                    kh.getCccd()
            });
        }
    }

    private void clearForm() {

        txtMa.setText("");
        txtTen.setText("");
        txtSDT.setText("");
        txtCCCD.setText("");

        cbGioiTinh.setSelectedIndex(0);
    }

    // ================= VALIDATE =================

    private boolean validateInput() {

        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String cccd = txtCCCD.getText().trim();

        if (ten.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Tên không được rỗng");
            return false;
        }

        if (!sdt.matches("^(03|05|07|09)\\\\d{8}$")) {

            JOptionPane.showMessageDialog(this, "SĐT không hợp lệ");
            return false;
        }

        cccd = cccd.trim();

        if (!cccd.matches("^(\\\\d{3})([0-3])(\\\\d{2})(\\\\d{6})$")) {

            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ");
            return false;
        }

        return true;
    }

    // ================= THÊM =================

    private void them() {

        if (!validateInput()) return;

        KhachHangDao dao = new KhachHangDao();

        if (dao.isDuplicate(txtSDT.getText(), txtCCCD.getText())) {

            JOptionPane.showMessageDialog(this, "Trùng SĐT hoặc CCCD");
            return;
        }

        KhachHang kh = new KhachHang();

        kh.setMaKH(dao.getNextMaKH());
        kh.setHoTen(txtTen.getText());

        kh.setGioiTinh(Integer.parseInt(String.valueOf(cbGioiTinh.getSelectedIndex())));

        kh.setSdt(txtSDT.getText());
        kh.setCccd(txtCCCD.getText());

        if (dao.create(kh)) {

            JOptionPane.showMessageDialog(this, "Thêm thành công");

            loadData();
            clearForm();
        }
    }

    // ================= SỬA =================

    private void sua() {

        if (!validateInput()) return;

        KhachHangDao dao = new KhachHangDao();

        KhachHang kh = new KhachHang();

        kh.setMaKH(txtMa.getText());
        kh.setHoTen(txtTen.getText());

        kh.setGioiTinh(Integer.parseInt(String.valueOf(
                cbGioiTinh.getSelectedIndex() == 0 ? 1 : 0
        )));

        kh.setSdt(txtSDT.getText());
        kh.setCccd(txtCCCD.getText());

        if (dao.update(kh)) {

            JOptionPane.showMessageDialog(this, "Sửa thành công");

            loadData();
        }
    }

    // ================= XÓA =================

    private void xoa() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(this, "Chọn khách hàng để xóa");
            return;
        }

        String ma = model.getValueAt(row, 0).toString();

        KhachHangDao dao = new KhachHangDao();

        if (dao.delete(ma)) {

            JOptionPane.showMessageDialog(this, "Xóa thành công");

            loadData();
            clearForm();
        }
    }
}
