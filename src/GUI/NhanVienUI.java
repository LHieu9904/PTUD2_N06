package GUI;

import Dao.NhanVienDao;
import Entity.ChucVu;
import Entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class NhanVienUI extends JPanel {

    // ================= COLOR =================

    private final Color BACKGROUND =
            new Color(241,245,249);

    private final Color CARD =
            Color.WHITE;

    private final Color PRIMARY =
            new Color(37,99,235);

    private final Color SUCCESS =
            new Color(16,185,129);

    private final Color WARNING =
            new Color(245,158,11);

    private final Color DANGER =
            new Color(239,68,68);

    private final Color BORDER =
            new Color(226,232,240);

    private final Color TEXT =
            new Color(30,41,59);

    // ================= COMPONENT =================

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtCCCD, txtSDT;
    private JSpinner spNgaySinh;
    private JTextField txtLuong, txtEmail, txtAnh;

    private JComboBox<String> cbGioiTinh;
    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbChucVu;

    private JLabel lblAnh;

    private NhanVienDao dao = new NhanVienDao();

    // Biến cờ ngăn chặn sự kiện tự sinh mã khi đổ dữ liệu từ bảng lên form
    private boolean isFilling = false;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NhanVienUI() {

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        // ================= TITLE =================

        JLabel title = new JLabel(
                "QUẢN LÝ NHÂN VIÊN",
                JLabel.CENTER
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(TEXT);

        title.setBorder(
                new EmptyBorder(20,0,10,0)
        );

        // ================= FORM =================

        JPanel form = new JPanel(
                new GridLayout(1,3,20,0)
        );

        form.setBorder(
                new EmptyBorder(20,40,10,40)
        );

        form.setBackground(BACKGROUND);

        // ================= INPUT =================

        txtMa = createTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(
                new Color(226,232,240)
        );

        txtTen = createTextField();
        txtCCCD = createTextField();
        txtSDT = createTextField();
        txtLuong = createTextField();
        txtEmail = createTextField();
        txtAnh = createTextField();

        SpinnerDateModel modelDate =
                new SpinnerDateModel();

        spNgaySinh = new JSpinner(modelDate);

        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(
                        spNgaySinh,
                        "dd-MM-yyyy"
                );

        spNgaySinh.setEditor(editor);

        spNgaySinh.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cbGioiTinh = new JComboBox<>(
                new String[]{"Nam","Nữ"}
        );

        cbTrangThai = new JComboBox<>(
                new String[]{"Đang làm","Nghỉ"}
        );

        cbChucVu = new JComboBox<>(
                new String[]{"Quản Lý","Lễ Tân"}
        );

        styleCombo(cbGioiTinh);
        styleCombo(cbTrangThai);
        styleCombo(cbChucVu);

        // ================= AUTO MÃ NV =================

        cbChucVu.addActionListener(e -> {
            // Nếu đang đổ dữ liệu từ table lên form thì KHÔNG tự sinh mã mới
            if (isFilling) return;

            txtMa.setText(
                    dao.getNextMaNV(
                            cbChucVu.getSelectedItem()
                                    .toString()
                    )
            );
        });

        // ================= PANEL ẢNH =================

        JPanel pnAnh = createCardPanel();

        pnAnh.setLayout(
                new BorderLayout(10,10)
        );

        lblAnh = new JLabel(
                "NO IMAGE",
                JLabel.CENTER
        );

        lblAnh.setPreferredSize(
                new Dimension(180,180)
        );

        lblAnh.setOpaque(true);

        lblAnh.setBackground(Color.WHITE);

        lblAnh.setForeground(
                new Color(100,116,139)
        );

        lblAnh.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblAnh.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                10,10,10,10
                        )
                )
        );

        JButton btnChonAnh =
                createButton(
                        "CHỌN ẢNH",
                        PRIMARY
                );

        pnAnh.add(lblAnh, BorderLayout.CENTER);
        pnAnh.add(btnChonAnh, BorderLayout.SOUTH);

        // ================= PANEL MID =================

        JPanel pnMid = createCardPanel();

        pnMid.setLayout(
                new GridLayout(6,2,10,14)
        );

        pnMid.add(label("Mã NV"));
        pnMid.add(txtMa);

        pnMid.add(label("Họ tên"));
        pnMid.add(txtTen);

        pnMid.add(label("Giới tính"));
        pnMid.add(cbGioiTinh);

        pnMid.add(label("Ngày sinh"));
        pnMid.add(spNgaySinh);

        pnMid.add(label("CCCD"));
        pnMid.add(txtCCCD);

        pnMid.add(label("SĐT"));
        pnMid.add(txtSDT);

        // ================= PANEL RIGHT =================

        JPanel pnRight = createCardPanel();

        pnRight.setLayout(
                new GridLayout(6,2,10,14)
        );

        pnRight.add(label("Chức vụ"));
        pnRight.add(cbChucVu);

        pnRight.add(label("Trạng thái"));
        pnRight.add(cbTrangThai);

        pnRight.add(label("Lương"));
        pnRight.add(txtLuong);

        pnRight.add(label("Email"));
        pnRight.add(txtEmail);

        pnRight.add(label("Ảnh"));
        pnRight.add(txtAnh);

        // ================= ADD FORM =================

        form.add(pnAnh);
        form.add(pnMid);
        form.add(pnRight);

        // ================= BUTTON PANEL =================

        JPanel btnPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        18,
                        10
                )
        );

        btnPanel.setBackground(BACKGROUND);

        JButton btnAdd =
                createButton(
                        "THÊM",
                        SUCCESS
                );

        JButton btnUpdate =
                createButton(
                        "SỬA",
                        PRIMARY
                );

        JButton btnReload =
                createButton(
                        "LÀM MỚI",
                        WARNING
                );

        JButton btnDelete =
                createButton(
                        "XÓA",
                        DANGER
                );

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnReload);
        btnPanel.add(btnDelete);

        // ================= TABLE =================

        model = new DefaultTableModel(
                new String[]{
                        "Mã NV",
                        "Tên",
                        "Giới tính",
                        "Ngày sinh",
                        "CCCD",
                        "SĐT",
                        "Chức vụ",
                        "Trạng thái",
                        "Lương",
                        "Email"
                },0
        );

        table = new JTable(model);

        table.setRowHeight(36);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        table.setGridColor(BORDER);

        table.setSelectionBackground(
                new Color(191,219,254)
        );

        table.setSelectionForeground(Color.BLACK);

        JTableHeader header =
                table.getTableHeader();

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        header.setBackground(TEXT);

        header.setForeground(Color.WHITE);

        header.setPreferredSize(
                new Dimension(0,40)
        );

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        JPanel tableWrapper =
                new JPanel(new BorderLayout());

        tableWrapper.setBackground(BACKGROUND);

        tableWrapper.setBorder(
                new EmptyBorder(20,40,20,40)
        );

        tableWrapper.add(scroll);

        // ================= MAIN =================

        JPanel main = new JPanel();

        main.setLayout(
                new BoxLayout(main, BoxLayout.Y_AXIS)
        );

        main.setBackground(BACKGROUND);

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

            JFileChooser chooser =
                    new JFileChooser();

            int result =
                    chooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION){

                try {

                    File file =
                            chooser.getSelectedFile();

                    String tenAnh =
                            file.getName();

                    Path source =
                            file.toPath();

                    Path target =
                            Paths.get(
                                    "photo/" + tenAnh
                            );

                    Files.copy(
                            source,
                            target,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    txtAnh.setText(tenAnh);

                    loadImage(
                            "photo/" + tenAnh
                    );

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        // ================= RELOAD =================

        btnReload.addActionListener(e -> {

            loadData();

            clearForm();

            txtMa.setText(
                    dao.getNextMaNV(
                            cbChucVu.getSelectedItem()
                                    .toString()
                    )
            );
        });

        // ================= DELETE =================

        btnDelete.addActionListener(e -> {

            int row =
                    table.getSelectedRow();

            if(row == -1){

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn nhân viên cần xóa"
                );

                return;
            }

            String maNV =
                    model.getValueAt(
                            row,
                            0
                    ).toString();

            if(new NhanVienDao()
                    .deleteNhanVien(maNV)){

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thành công"
                );

                loadData();

                clearForm();

            }else{

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thất bại"
                );
            }
        });

        // ================= UPDATE =================

        btnUpdate.addActionListener(e -> {

            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên từ bảng để thực hiện sửa!");
                return;
            }

            NhanVien nv =
                    getFormData();

            if(nv == null) return;

            if(dao.updateNhanVien(nv)){

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thông tin nhân viên thành công"
                );

                loadData();

                clearForm();

                txtMa.setText(
                        dao.getNextMaNV(
                                cbChucVu.getSelectedItem()
                                        .toString()
                        )
                );

            }else{

                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật thất bại"
                );
            }
        });

        // ================= ADD =================

        btnAdd.addActionListener(e -> {

            NhanVien nv = getFormData();
            if (nv == null) return;

            try {
                if (dao.insertNhanVien(nv)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Thêm thành công!\n" +
                                    "Tài khoản đã được tạo tự động\n" +
                                    "Tên đăng nhập: " + nv.getMaNV() + "\n" +
                                    "Mật khẩu: " + nv.getMaNV()
                    );

                    loadData();
                    clearForm();
                    txtMa.setText(
                            dao.getNextMaNV(cbChucVu.getSelectedItem().toString())
                    );
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại: Không rõ nguyên nhân.");
                }

            } catch (java.sql.SQLException ex) {
                String errorMsg = ex.getMessage();
                if (errorMsg.contains("PRIMARY KEY") || ex.getErrorCode() == 2627) {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại: Mã nhân viên [" + nv.getMaNV() + "] đã tồn tại trong hệ thống!", "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
                } else if (errorMsg.contains("UQ_") || errorMsg.contains("UNIQUE") || errorMsg.contains("CCCD")) {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại: Số CCCD hoặc Email này đã được đăng ký cho nhân viên khác!", "Lỗi Trùng Dữ Liệu", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi Cơ Sở Dữ Liệu: " + errorMsg, "SQL Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Hệ thống gặp lỗi: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // ================= TABLE EVENT =================

        table.getSelectionModel()
                .addListSelectionListener(
                        e -> fillForm()
                );
    }

    // =====================================================
    // UI
    // =====================================================

    private JPanel createCardPanel(){

        JPanel p = new JPanel();

        p.setBackground(CARD);

        p.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(20,20,20,20)
                )
        );

        return p;
    }

    private JTextField createTextField(){

        JTextField txt = new JTextField();

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        txt.setPreferredSize(
                new Dimension(200,38)
        );

        txt.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                5,10,5,10
                        )
                )
        );

        return txt;
    }

    private void styleCombo(
            JComboBox<String> cb
    ){

        cb.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cb.setBackground(Color.WHITE);

        cb.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );
    }

    private JButton createButton(
            String text,
            Color bg
    ){

        JButton btn = new JButton(text);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        btn.setForeground(Color.WHITE);

        btn.setBackground(bg);

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setBorder(
                new EmptyBorder(
                        12,22,12,22
                )
        );

        return btn;
    }

    private JLabel label(String t){

        JLabel lb = new JLabel(t);

        lb.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lb.setForeground(TEXT);

        return lb;
    }

    // =====================================================
    // FUNCTION
    // =====================================================

    private void clearForm(){

        txtTen.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");

        spNgaySinh.setValue(
                new java.util.Date()
        );

        txtLuong.setText("");
        txtEmail.setText("");
        txtAnh.setText("");

        cbGioiTinh.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        cbChucVu.setSelectedIndex(0);

        lblAnh.setIcon(null);
        lblAnh.setText("NO IMAGE");

        txtMa.setText(
                dao.getNextMaNV(
                        cbChucVu.getSelectedItem()
                                .toString()
                )
        );
    }

    private void loadData(){

        model.setRowCount(0);

        txtMa.setText(
                dao.getNextMaNV(
                        cbChucVu.getSelectedItem()
                                .toString()
                )
        );

        List<NhanVien> list =
                new NhanVienDao()
                        .getAll();

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "dd-MM-yyyy"
                );

        for(NhanVien nv : list){

            model.addRow(new Object[]{

                    nv.getMaNV(),

                    nv.getHoTen(),

                    nv.getGioiTinh() == 1
                            ? "Nam"
                            : "Nữ",

                    nv.getNgaySinh() != null
                            ? sdf.format(
                            nv.getNgaySinh()
                    )
                            : "",

                    nv.getCccd(),

                    nv.getSdt(),

                    nv.getChucVu()
                            .getTenChucVu(),

                    nv.getTrangThaiLamViec(),

                    nv.getLuong(),

                    nv.getEmail()
            });
        }
    }

    private void loadImage(String path){

        try {

            if(path == null
                    || path.trim().isEmpty()){

                lblAnh.setIcon(null);

                lblAnh.setText("NO IMAGE");

                return;
            }

            File file = new File(path);

            if(!file.exists()){

                lblAnh.setIcon(null);

                lblAnh.setText("KHÔNG TÌM THẤY");

                return;
            }

            ImageIcon icon =
                    new ImageIcon(path);

            Image img =
                    icon.getImage()
                            .getScaledInstance(
                                    180,
                                    180,
                                    Image.SCALE_SMOOTH
                            );

            lblAnh.setIcon(
                    new ImageIcon(img)
            );

            lblAnh.setText("");

        }catch (Exception e){

            lblAnh.setIcon(null);

            lblAnh.setText("LỖI ẢNH");
        }
    }

    private void fillForm(){

        int row =
                table.getSelectedRow();

        if(row == -1) return;

        // Bật cờ thông báo hệ thống bắt đầu đổ dữ liệu lên form (khóa hành động đổi mã tự động)
        isFilling = true;

        String maNV =
                model.getValueAt(
                        row,
                        0
                ).toString();

        NhanVien nv =
                new NhanVienDao()
                        .getById(maNV);

        txtMa.setText(nv.getMaNV());

        txtTen.setText(nv.getHoTen());

        cbGioiTinh.setSelectedIndex(
                nv.getGioiTinh() == 1 ? 0 : 1
        );

        if(nv.getNgaySinh() != null){

            spNgaySinh.setValue(
                    nv.getNgaySinh()
            );
        }

        txtCCCD.setText(nv.getCccd());

        txtSDT.setText(nv.getSdt());

        cbTrangThai.setSelectedItem(
                nv.getTrangThaiLamViec()
        );

        String tenCV =
                nv.getChucVu()
                        .getTenChucVu();

        if(tenCV.equalsIgnoreCase(
                "Quản Lý"
        )){

            cbChucVu.setSelectedItem(
                    "Quản Lý"
            );

        }else{

            cbChucVu.setSelectedItem(
                    "Lễ Tân"
            );
        }

        txtLuong.setText(
                String.valueOf(
                        nv.getLuong()
                )
        );

        txtEmail.setText(
                nv.getEmail()
        );

        txtAnh.setText(
                nv.getAnhNhanVien()
        );

        loadImage(
                "photo/" + nv.getAnhNhanVien()
        );

        // Tắt cờ sau khi hoàn thành đổ dữ liệu
        isFilling = false;
    }

    // =====================================================
    // VALIDATE
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

        if (!hoTen.matches(
                "^[\\p{L}]+(\\s[\\p{L}]+)*$"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Họ tên chỉ được chứa chữ cái!"
            );

            return false;
        }

        String[] arr =
                hoTen.split("\\s+");

        for (String s : arr) {

            if (!Character.isUpperCase(
                    s.charAt(0)
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Chữ cái đầu mỗi từ phải viết HOA!"
                );

                return false;
            }
        }

        return true;
    }

    private boolean validateNgaySinh(
            java.util.Date ngaySinh
    ) {

        java.util.Calendar cal =
                java.util.Calendar.getInstance();

        cal.add(
                java.util.Calendar.YEAR,
                -18
        );

        java.util.Date minDate =
                cal.getTime();

        if (ngaySinh.after(minDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nhân viên phải đủ 18 tuổi!"
            );

            return false;
        }

        return true;
    }

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

    private boolean validateCCCD(
            String cccd,
            java.util.Date ngaySinh,
            boolean isNam
    ) {

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
                cal.get(
                        java.util.Calendar.YEAR
                );

        int yy = year % 100;

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

        int genderCode =
                Integer.parseInt(
                        cccd.substring(3,4)
                );

        boolean hopLe = false;

        if (year >= 1900 && year <= 1999) {

            if (isNam && genderCode == 0) {
                hopLe = true;
            }

            if (!isNam && genderCode == 1) {
                hopLe = true;
            }
        }

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

    private NhanVien getFormData(){

        NhanVien nv = new NhanVien();

        nv.setMaNV(txtMa.getText().trim());

        nv.setGioiTinh(
                cbGioiTinh.getSelectedItem()
                        .equals("Nam") ? 1 : 0
        );

        String hoTen =
                txtTen.getText().trim();

        if (!validateHoTen(hoTen)) {
            return null;
        }

        nv.setHoTen(hoTen);

        java.util.Date d;

        try {

            d = (java.util.Date)
                    spNgaySinh.getValue();

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
        String txtChucVu = cbChucVu.getSelectedItem().toString();

        // ĐÃ KHẮC PHỤC: Đồng bộ toàn diện cả Mã lẫn Tên chức vụ truyền xuống DB để không lỗi update
        if (txtChucVu.equals("Quản Lý")) {
            cv.setMaChucVu("CV01");
            cv.setTenChucVu("Quản Lý");
        } else {
            cv.setMaChucVu("CV02");
            cv.setTenChucVu("Lễ Tân");
        }

        nv.setChucVu(cv);

        nv.setTrangThaiLamViec(
                cbTrangThai.getSelectedItem()
                        .toString()
        );

        double luong = 0;

        String luongText =
                txtLuong.getText().trim();

        if (!luongText.isEmpty()) {

            luong = Double.parseDouble(
                    luongText
            );
        }

        nv.setLuong(luong);

        nv.setEmail(txtEmail.getText());

        nv.setAnhNhanVien(
                txtAnh.getText()
        );

        return nv;
    }
}