package GUI;

import Dao.NhanVienDao;
import Entity.ChucVu;
import Entity.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class NhanVienUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtCCCD, txtSDT;
    private JSpinner spNgaySinh;
    private JTextField txtLuong, txtEmail, txtAnh;
    private NhanVienDao dao = new NhanVienDao();

    private JComboBox<String> cbGioiTinh, cbTrangThai, cbChucVu;

    private JLabel lblAnh;

    public NhanVienUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // ================= FORM =================
        JPanel form = new JPanel(new GridLayout(1,3,20,0));
        form.setBorder(BorderFactory.createEmptyBorder(20,40,10,40));

        // ===== INPUT =====
        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(Color.LIGHT_GRAY);
        txtTen = new JTextField();
        txtCCCD = new JTextField();
        txtSDT = new JTextField();
        SpinnerDateModel modelDate = new SpinnerDateModel();
        spNgaySinh = new JSpinner(modelDate);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spNgaySinh, "dd-MM-yyyy");
        spNgaySinh.setEditor(editor);

        txtLuong = new JTextField();
        txtEmail = new JTextField();
        txtAnh = new JTextField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        cbTrangThai = new JComboBox<>(new String[]{"Đang làm","Nghỉ"});
        cbChucVu = new JComboBox<>(new String[]{"Quản Lý","Lễ Tân"});

        Font f = new Font("Segoe UI", Font.PLAIN, 14);

        for(Component c : new Component[]{
                txtMa, txtTen, txtCCCD, txtSDT, spNgaySinh,
                txtLuong, txtEmail, txtAnh,
                cbGioiTinh, cbTrangThai, cbChucVu
        }) c.setFont(f);

        // ===== PANEL ẢNH =====
        JPanel pnAnh = new JPanel(new BorderLayout());
        pnAnh.setPreferredSize(new Dimension(200,200));

        lblAnh = new JLabel("No Image", JLabel.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnChonAnh = new JButton("Chọn ảnh");

        pnAnh.add(lblAnh, BorderLayout.CENTER);
        pnAnh.add(btnChonAnh, BorderLayout.SOUTH);

        // ===== PANEL GIỮA =====
        JPanel pnMid = new JPanel(new GridLayout(6,2,10,10));

        pnMid.add(label("Mã NV")); pnMid.add(txtMa);
        pnMid.add(label("Họ tên")); pnMid.add(txtTen);

        pnMid.add(label("Giới tính")); pnMid.add(cbGioiTinh);
        pnMid.add(label("Ngày sinh")); pnMid.add(spNgaySinh);

        pnMid.add(label("CCCD")); pnMid.add(txtCCCD);
        pnMid.add(label("SĐT")); pnMid.add(txtSDT);

        // ===== PANEL PHẢI =====
        JPanel pnRight = new JPanel(new GridLayout(6,2,10,10));

        pnRight.add(label("Chức vụ")); pnRight.add(cbChucVu);
        pnRight.add(label("Trạng thái")); pnRight.add(cbTrangThai);

        pnRight.add(label("Lương")); pnRight.add(txtLuong);
        pnRight.add(label("Email")); pnRight.add(txtEmail);

        pnRight.add(label("Ảnh")); pnRight.add(txtAnh);

        // ===== GHÉP =====
        form.add(pnAnh);
        form.add(pnMid);
        form.add(pnRight);

        // ================= BUTTON =================
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245,245,245));

        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnReload = new JButton("Làm mới");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnReload);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new String[]{
                        "Mã NV","Tên","Giới tính","Ngày sinh",
                        "CCCD","SĐT","Chức vụ","Trạng thái",
                        "Lương","Email"
                },0
        );

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));
        tableWrapper.add(scroll);

        // ================= MAIN =================
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

        // ================= LOAD =================
        loadData();

        // ================= EVENTS =================

        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("images");

            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                String path = chooser.getSelectedFile().getPath();
                txtAnh.setText(path);
                loadImage(path);
            }
        });
        // ================= NhanVienUI.java =================
// EVENT XÓA
        btnReload.addActionListener(e -> {

            loadData();
            clearForm();

            txtMa.setText(
                    new NhanVienDao().getNextMaNV()
            );
        });

        JButton btnDelete = new JButton("Xóa");
        btnPanel.add(btnDelete);

        btnDelete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn nhân viên cần xóa");
                return;
            }

            String maNV = model.getValueAt(row, 0).toString();

            if (new NhanVienDao().deleteNhanVien(maNV)) {

                JOptionPane.showMessageDialog(this,
                        "Xóa thành công");

                loadData();
                clearForm();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa thất bại");
            }
        });


        btnUpdate.addActionListener(e -> {

            NhanVien nv = getFormData();
            if (nv == null) return;

            if (dao.updateNhanVien(nv)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thông tin nhân viên thành công"
                );

                loadData();
                clearForm();

                txtMa.setText(
                        dao.getNextMaNV()
                );

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thất bại"
                );
            }
        });
        // ================= NhanVienUI.java =================
// EVENT THÊM

        btnAdd.addActionListener(e -> {

            NhanVien nv = getFormData();
            if (nv == null) return;

            if (new NhanVienDao().insertNhanVien(nv)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm thành công!\n" +
                                "Tài khoản đã được tạo tự động\n" +
                                "Tên đăng nhập: " + nv.getMaNV() + "\n" +
                                "Mật khẩu: " + nv.getMaNV()
                );

                loadData();
                clearForm();

                // PHÁT SINH LẠI MÃ NHÂN VIÊN MỚI
                txtMa.setText(
                        new NhanVienDao().getNextMaNV()
                );

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Thêm thất bại"
                );
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> fillForm());
    }


    // ================= FUNCTION =================

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lb;
    }
    private void clearForm(){

        txtTen.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");
        spNgaySinh.setValue(new java.util.Date());
        txtLuong.setText("");
        txtEmail.setText("");
        txtAnh.setText("");

        cbGioiTinh.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        cbChucVu.setSelectedIndex(0);


        lblAnh.setIcon(null);
        lblAnh.setText("No Image");


        txtMa.setText(new NhanVienDao().getNextMaNV());
    }

    private void loadData() {
        model.setRowCount(0);
        txtMa.setText(new NhanVienDao().getNextMaNV());

        List<NhanVien> list = new NhanVienDao().getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.getHoTen(),
                    nv.getGioiTinh() == 1 ? "Nam" : "Nữ",
                    nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "",
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getChucVu().getTenChucVu(),
                    nv.getTrangThaiLamViec(),
                    nv.getLuong(),
                    nv.getEmail()
            });
        }
    }

    private void loadImage(String path){
        try {
            if(path == null || path.isEmpty()){
                lblAnh.setIcon(null);
                lblAnh.setText("No Image");
                return;
            }

            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(180,180,Image.SCALE_SMOOTH);

            lblAnh.setIcon(new ImageIcon(img));
            lblAnh.setText("");

        } catch (Exception e){
            lblAnh.setText("Lỗi ảnh");
        }
    }

    private NhanVien getFormData(){
        NhanVien nv = new NhanVien();

        nv.setMaNV(txtMa.getText().trim());
        nv.setGioiTinh(cbGioiTinh.getSelectedItem().equals("Nam") ? 1 : 0);
        String hoTen = txtTen.getText().trim();

        if (!validateHoTen(hoTen)) {
            return null;
        }

        nv.setHoTen(hoTen);

        java.util.Date d;

        try {

            d = (java.util.Date) spNgaySinh.getValue();

            if (!validateNgaySinh(d)) {
                return null;
            }

            nv.setNgaySinh(
                    new Date(d.getTime())
            );

        } catch (Exception e){

            JOptionPane.showMessageDialog(
                    this,
                    "Sai ngày"
            );

            return null;
        }

        nv.setCccd(txtCCCD.getText());
        nv.setSdt(txtSDT.getText());
        if (!validateSDT(nv.getSdt())) {
            return null;
        }

        boolean isNam =
                cbGioiTinh.getSelectedItem()
                        .equals("Nam");

        if (!validateCCCD(
                nv.getCccd(),
                d,
                isNam
        )) {
            return null;
        }

        ChucVu cv = new ChucVu();
        cv.setMaChucVu(cbChucVu.getSelectedItem().equals("Quản Lý") ? "CV01" : "CV02");

        nv.setChucVu(cv);
        nv.setTrangThaiLamViec(cbTrangThai.getSelectedItem().toString());

        double luong = 0;

        String luongText = txtLuong.getText().trim();

        if (!luongText.isEmpty()) {
            luong = Double.parseDouble(luongText);
        }

        nv.setLuong(luong);
        nv.setEmail(txtEmail.getText());
        nv.setAnhNhanVien(txtAnh.getText());

        return nv;
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String maNV = model.getValueAt(row,0).toString();

        NhanVien nv = new NhanVienDao().getById(maNV);

        txtMa.setText(nv.getMaNV());
        txtTen.setText(nv.getHoTen());
        cbGioiTinh.setSelectedIndex(nv.getGioiTinh() == 1 ? 0 : 1);
        if(nv.getNgaySinh() != null){
            spNgaySinh.setValue(nv.getNgaySinh());
        }
        txtCCCD.setText(nv.getCccd());
        txtSDT.setText(nv.getSdt());
        cbTrangThai.setSelectedItem(nv.getTrangThaiLamViec());
        String tenCV = nv.getChucVu().getTenChucVu();

        if(tenCV.equalsIgnoreCase("Quản Lý")){
            cbChucVu.setSelectedItem("Quản Lý");
        }else{
            cbChucVu.setSelectedItem("Lễ Tân");
        }

        txtLuong.setText(String.valueOf(nv.getLuong()));
        txtEmail.setText(nv.getEmail());

        txtAnh.setText(nv.getAnhNhanVien());
        loadImage(nv.getAnhNhanVien());
    }
    // =====================================================
// VALIDATE HỌ TÊN
// =====================================================

    private boolean validateHoTen(String hoTen) {

        hoTen = hoTen.trim();

        if (hoTen.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Họ tên không được rỗng!"
            );

            return false;
        }

        // Chỉ chữ và khoảng trắng
        if (!hoTen.matches(
                "^[\\p{L}]+(\\s[\\p{L}]+)*$"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Họ tên chỉ được chứa chữ cái!"
            );

            return false;
        }

        // Kiểm tra viết hoa đầu từ
        String[] arr = hoTen.split("\\s+");

        for (String s : arr) {

            if (!Character.isUpperCase(s.charAt(0))) {

                JOptionPane.showMessageDialog(
                        this,
                        "Chữ cái đầu mỗi từ phải viết HOA!"
                );

                return false;
            }
        }

        return true;
    }

// =====================================================
// VALIDATE TUỔI >= 18
// =====================================================

    private boolean validateNgaySinh(
            java.util.Date ngaySinh
    ) {

        java.util.Calendar cal =
                java.util.Calendar.getInstance();

        cal.add(java.util.Calendar.YEAR, -18);

        java.util.Date minDate = cal.getTime();

        if (ngaySinh.after(minDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nhân viên phải đủ 18 tuổi!"
            );

            return false;
        }

        return true;
    }

// =====================================================
// VALIDATE SĐT
// =====================================================

    private boolean validateSDT(String sdt) {

        if (!sdt.matches("^0\\d{9}$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "SĐT phải gồm 10 số và bắt đầu bằng 0!"
            );

            return false;
        }

        return true;
    }

// =====================================================
// VALIDATE CCCD
// =====================================================

    private boolean validateCCCD(
            String cccd,
            java.util.Date ngaySinh,
            boolean isNam
    ) {

        // 12 số
        if (!cccd.matches("^\\d{12}$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "CCCD phải gồm đúng 12 số!"
            );

            return false;
        }

        java.util.Calendar cal =
                java.util.Calendar.getInstance();

        cal.setTime(ngaySinh);

        int year =
                cal.get(java.util.Calendar.YEAR);

        int yy = year % 100;

        // YY trong CCCD
        int yyCCCD =
                Integer.parseInt(
                        cccd.substring(4, 6)
                );

        if (yy != yyCCCD) {

            JOptionPane.showMessageDialog(
                    this,
                    "CCCD không đúng năm sinh!"
            );

            return false;
        }

        // mã giới tính + thế kỷ
        int genderCode =
                Integer.parseInt(
                        cccd.substring(3,4)
                );

        boolean hopLe = false;

        // thế kỷ 20
        if (year >= 1900 && year <= 1999) {

            if (isNam && genderCode == 0) {
                hopLe = true;
            }

            if (!isNam && genderCode == 1) {
                hopLe = true;
            }
        }

        // thế kỷ 21
        else {

            if (isNam && genderCode == 2) {
                hopLe = true;
            }

            if (!isNam && genderCode == 3) {
                hopLe = true;
            }
        }

        if (!hopLe) {

            JOptionPane.showMessageDialog(
                    this,
                    "CCCD không đúng giới tính/thế kỷ!"
            );

            return false;
        }

        return true;
    }
}