package GUI;

import Dao.LoaiPhongDao;
import Dao.PhongDao;
import Entity.LoaiPhong;
import Entity.Phong;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PhongUI extends JPanel {

    private JPanel roomPanel;

    private JTextField txtMaPhong, txtTang;
    private JComboBox<String> cbTrangThai, cbLoai;

    private PhongDao phongDao = new PhongDao();
    private LoaiPhongDao loaiDao = new LoaiPhongDao();
    private List<LoaiPhong> dsLoai;

    public PhongUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        // ===== TITLE =====
        JLabel title = new JLabel("QUẢN LÝ PHÒNG", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(10,0,10,0));

        // ===== FORM (GIỮ NGUYÊN) =====
        JPanel form = new JPanel(new GridLayout(2,4,20,10));
        form.setBorder(new EmptyBorder(10,40,10,40));
        form.setPreferredSize(new Dimension(0, 100));

        txtMaPhong = new JTextField();
        txtMaPhong.setEditable(false);
        txtTang = new JTextField();
        txtTang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {

                try{
                    int tang = Integer.parseInt(txtTang.getText());
                    txtMaPhong.setText(generateMaPhong(tang));
                }catch(Exception e){
                    txtMaPhong.setText("");
                }
            }
        });

        cbTrangThai = new JComboBox<>(new String[]{
                "Trống",
                "Đã đặt",
                "Đang thuê",
                "Đang dọn dẹp"
        });
        cbLoai = new JComboBox<>();


        loadLoaiPhong();

        form.add(label("Mã phòng")); form.add(txtMaPhong);
        form.add(label("Tầng")); form.add(txtTang);
        form.add(label("Trạng thái")); form.add(cbTrangThai);
        form.add(label("Loại phòng")); form.add(cbLoai);

        // ===== BUTTON =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(245,245,245));

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnReload = new JButton("Reload");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnReload);

        // ===== LEFT FILTER (GIỮ NGUYÊN) =====
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(180,0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(245,245,245));
        left.setBorder(new EmptyBorder(10,10,10,10));

        // ===== FILTER =====
        JComboBox<String> cbTrangThaiFilter =
                new JComboBox<>(new String[]{
                        "Tất cả",
                        "Trống",
                        "Đã đặt",
                        "Đang thuê",
                        "Đang dọn dẹp"
                });


        JComboBox<Integer> cbTangFilter = new JComboBox<>();
        cbTangFilter.addItem(0); // 0 = tất cả
        cbTangFilter.removeAllItems();
        cbTangFilter.addItem(0); // tất cả

        List<Phong> dsPhong = phongDao.getAllPhong();

        java.util.Set<Integer> tangSet = new java.util.TreeSet<>();

        for(Phong p : dsPhong){
            tangSet.add(p.getTang());
        }

        for(Integer t : tangSet){
            cbTangFilter.addItem(t);
        }
        JComboBox<LoaiPhong> cbLoaiFilter = new JComboBox<>();
        cbLoaiFilter.addItem(null); // null = tất cả
        for(LoaiPhong lp : loaiDao.getAllLoaiPhong()){
            cbLoaiFilter.addItem(lp);
        }

// add UI
        left.add(createFilter("Trạng thái", cbTrangThaiFilter));
        left.add(createFilter("Tầng", cbTangFilter));
        left.add(createFilter("Loại phòng", cbLoaiFilter));
        // 🔥 EVENT FILTER (DÁN NGAY DƯỚI left.add(...))

        cbTrangThaiFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter.getSelectedItem().toString(),
                        (Integer) cbTangFilter.getSelectedItem(),
                        (LoaiPhong) cbLoaiFilter.getSelectedItem()
                )
        );

        cbTangFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter.getSelectedItem().toString(),
                        (Integer) cbTangFilter.getSelectedItem(),
                        (LoaiPhong) cbLoaiFilter.getSelectedItem()
                )
        );

        cbLoaiFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter.getSelectedItem().toString(),
                        (Integer) cbTangFilter.getSelectedItem(),
                        (LoaiPhong) cbLoaiFilter.getSelectedItem()
                )
        );

        // ===== ROOM PANEL (GIỮ NGUYÊN) =====
        roomPanel = new JPanel();
        roomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        roomPanel.setBackground(new Color(245,245,245));
        Dimension size = new Dimension(140, 25);

        cbTrangThaiFilter.setPreferredSize(size);
        cbTrangThaiFilter.setMaximumSize(size);

        cbTangFilter.setPreferredSize(size);
        cbTangFilter.setMaximumSize(size);

        cbLoaiFilter.setPreferredSize(size);
        cbLoaiFilter.setMaximumSize(size);

// 🔥 QUAN TRỌNG: cho panel cao hơn
        roomPanel.setPreferredSize(new Dimension(800, 1000));

        JScrollPane scroll = new JScrollPane(roomPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ===== MAIN (GIỮ NGUYÊN) =====
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245,245,245));

        main.add(left, BorderLayout.WEST);
        main.add(scroll, BorderLayout.CENTER);

        // ===== TOP WRAPPER (GIỮ NGUYÊN) =====
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(new Color(245,245,245));

        top.add(form);
        top.add(btnPanel);

        // ===== CENTER WRAPPER (QUAN TRỌNG - GIỮ NGUYÊN) =====
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(new Color(245,245,245));

        centerWrapper.add(top, BorderLayout.NORTH);
        centerWrapper.add(main, BorderLayout.CENTER);

        add(title, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        // ===== EVENT =====
        btnReload.addActionListener(e ->
                loadData("Tất cả", 0, null)
        );



        btnThem.addActionListener(e -> insertPhong());
        btnSua.addActionListener(e -> updatePhong());
        btnXoa.addActionListener(e -> deletePhong());

        loadData("Tất cả", 0, null);
    }

    // ================= LOAD =================

    private void loadLoaiPhong(){
        dsLoai = loaiDao.getAllLoaiPhong();
        cbLoai.removeAllItems();
        for(LoaiPhong lp : dsLoai){
            cbLoai.addItem(lp.getTenLP()); // giữ giống UI cũ
        }
    }

    private void loadData(String trangThaiFilter, Integer tangFilter, LoaiPhong loaiFilter){

        roomPanel.removeAll();

        List<Phong> list = phongDao.getAllPhong();

        for (Phong p : list) {

            // lọc trạng thái
            if (!"Tất cả".equals(trangThaiFilter)
                    && !p.getTrangThai().equals(trangThaiFilter)) {
                continue;
            }

            // lọc tầng
            if (tangFilter != 0 && p.getTang() != tangFilter) {
                continue;
            }

            // lọc loại phòng
            if (loaiFilter != null &&
                    !p.getLoaiPhong().equals(loaiFilter)) {
                continue;
            }

            // ===== LOGIC TRẠNG THÁI =====
            if ("Đã đặt".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDaDat(p)); // 🔴 phòng đã đặt
            }
            else if ("Đang thuê".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDangThue(p)); // 🟢 đã nhận phòng
            }
            else if ("Đang dọn dẹp".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDangDon(p)); // 🟡
            }
            else {
                roomPanel.add(createRoomTrong(p)); // ⚪
            }
        }

        roomPanel.setPreferredSize(
                new Dimension(800, (roomPanel.getComponentCount()/3 + 1) * 120)
        );

        roomPanel.revalidate();
        roomPanel.repaint();
    }

    private void loadData(String filter){

        roomPanel.removeAll();

        List<Phong> list = phongDao.getAllPhong();

        for (Phong p : list) {

            if (!filter.equals("Tất cả") && !p.getTrangThai().equals(filter)) {
                continue;
            }

            // 🔥 FIX CHUẨN: dựa vào có khách hay không
            if (p.getTenKhach() != null) {
                roomPanel.add(createRoomBusy(p));
            } else {
                roomPanel.add(createRoom(p));
            }
        }

        // 🔥 FIX SCROLL (QUAN TRỌNG)
        roomPanel.setPreferredSize(
                new Dimension(800, (roomPanel.getComponentCount()/3 + 1) * 120)
        );

        roomPanel.revalidate();
        roomPanel.repaint();
    }

    // ================= CRUD =================
    private JPanel createRoomTrong(Phong p){

        JPanel card = baseCard(p);

        JLabel center = new JLabel(
                p.getLoaiPhong() != null ? p.getLoaiPhong().getTenLP() : "",
                JLabel.CENTER
        );

        JLabel status = new JLabel("Trống", JLabel.RIGHT);
        status.setForeground(new Color(0,150,0));

        card.setBackground(Color.WHITE);

        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        addClickEvent(card, p);

        return card;
    }
    private JPanel createRoomDangThue(Phong p){

        JPanel card = baseCard(p);

        String ten = p.getTenKhach() != null ? p.getTenKhach() : "Khách";
        String sdt = p.getSdt() != null ? p.getSdt() : "";

        JLabel center = new JLabel("<html>" + ten + "<br>" + sdt + "</html>");

        JLabel status = new JLabel("Đang thuê", JLabel.RIGHT);
        status.setForeground(new Color(0,150,0));

        card.setBackground(new Color(200,255,200));

        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        addClickEvent(card, p);

        return card;
    }
    private JPanel createRoomDaDat(Phong p){

        JPanel card = baseCard(p);

        JLabel center = new JLabel("Đã đặt", JLabel.CENTER);

        JLabel status = new JLabel("Đã đặt", JLabel.RIGHT);
        status.setForeground(Color.RED);

        card.setBackground(new Color(255,200,200));

        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        addClickEvent(card, p);

        return card;
    }
    private JPanel createRoomDangDon(Phong p){

        JPanel card = baseCard(p);

        JLabel center = new JLabel("Đang dọn", JLabel.CENTER);

        JLabel status = new JLabel("Dọn dẹp", JLabel.RIGHT);
        status.setForeground(Color.ORANGE);

        card.setBackground(new Color(255,255,200));

        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        addClickEvent(card, p);

        return card;
    }
    private JPanel baseCard(Phong p){

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 110));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(10,10,10,10)
        ));

        JLabel top = new JLabel("Tầng " + p.getTang() + " - " + p.getMaPhong());
        top.setFont(new Font("Segoe UI", Font.BOLD, 13));

        card.add(top, BorderLayout.NORTH);

        return card;
    }
    private void addClickEvent(JPanel card, Phong p){

        Color original = card.getBackground();

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                txtMaPhong.setText(p.getMaPhong());
                txtTang.setText(String.valueOf(p.getTang()));

                if(p.getTrangThai() != null){
                    cbTrangThai.setSelectedItem(p.getTrangThai());
                }

                if(p.getLoaiPhong() != null){
                    cbLoai.setSelectedItem(p.getLoaiPhong().getTenLP());
                }

                // CHỈ popup khi phòng đang thuê
                if ("Đang thuê".equals(p.getTrangThai())) {

                    Object[] info = phongDao.getThongTinPhongDangThue(
                            p.getMaPhong()
                    );

                    if (info != null) {

                        String message =
                                "===== THÔNG TIN KHÁCH HÀNG =====\n" +
                                        "Họ tên: " + info[0] + "\n" +
                                        "SĐT: " + info[1] + "\n" +
                                        "CCCD: " + info[2] + "\n\n" +

                                        "===== THÔNG TIN PHÒNG =====\n" +
                                        "Mã phòng: " + info[3] + "\n" +
                                        "Loại phòng: " + info[4] + "\n" +
                                        "Thời gian nhận: " + info[5] + "\n" +
                                        "Thời gian trả: " + info[6];


                        JOptionPane.showMessageDialog(
                                null,
                                message,
                                "Chi tiết phòng đang thuê",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(original.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(original);
            }
        });
    }

    private Phong getForm(){

        try{
            String ma = txtMaPhong.getText();
            int tang = Integer.parseInt(txtTang.getText());
            String tt = cbTrangThai.getSelectedItem().toString();

            LoaiPhong lp = dsLoai.get(cbLoai.getSelectedIndex());

            return new Phong(ma, tang, tt, lp);

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Dữ liệu sai!");
            return null;
        }
    }

    private void insertPhong() {

        try {
            if (txtTang.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập tầng");
                return;
            }

            int tang = Integer.parseInt(txtTang.getText().trim());

            // tự phát sinh mã phòng
            txtMaPhong.setText(generateMaPhong(tang));

            Phong p = getForm();
            if (p == null) return;

            if (phongDao.insert(p)) {

                JOptionPane.showMessageDialog(this,
                        "Thêm phòng thành công");

                loadData("Tất cả", 0, null);
                clearForm();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm phòng thất bại");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Tầng không hợp lệ");
        }
    }

    private void updatePhong() {

        if (txtMaPhong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phòng cần sửa");
            return;
        }

        Phong p = getForm();
        if (p == null) return;

        if (phongDao.update(p)) {

            JOptionPane.showMessageDialog(this,
                    "Cập nhật phòng thành công");

            loadData("Tất cả", 0, null);
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thất bại");
        }
    }

    private void deletePhong() {

        String maPhong = txtMaPhong.getText().trim();

        if (maPhong.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn phòng cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa phòng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        if (phongDao.delete(maPhong)) {

            JOptionPane.showMessageDialog(this,
                    "Xóa phòng thành công");

            loadData("Tất cả", 0, null);
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa phòng thất bại");
        }
    }

    // ================= UI CARD (GIỮ NGUYÊN 100%) =================

    private JPanel createRoom(Phong p){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(180, 110));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                new EmptyBorder(10,10,10,10)
        ));

        JLabel top = new JLabel("Tầng " + p.getTang() + " - " + p.getMaPhong());
        top.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel center = new JLabel(
                p.getLoaiPhong() != null ? p.getLoaiPhong().getTenLP() : "",
                JLabel.CENTER
        );

        JLabel status = new JLabel(p.getTrangThai(), JLabel.RIGHT);

        if("Trống".equals(p.getTrangThai())){
            status.setForeground(new Color(0,150,0));
        }else if("Đang dọn dẹp".equals(p.getTrangThai())){
            status.setForeground(Color.ORANGE);
        }else{
            status.setForeground(Color.RED);
        }
        status.setForeground(new Color(0,150,0));

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                txtMaPhong.setText(p.getMaPhong());
                txtTang.setText(String.valueOf(p.getTang()));

                // tránh null crash
                if(p.getTrangThai() != null){
                    cbTrangThai.setSelectedItem(p.getTrangThai());
                }

                if(p.getLoaiPhong() != null){
                    cbLoai.setSelectedItem(p.getLoaiPhong().getTenLP());
                }
            }


            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240,248,255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }
    private void clearForm() {

        txtMaPhong.setText("");
        txtTang.setText("");

        cbTrangThai.setSelectedIndex(0);

        if (cbLoai.getItemCount() > 0) {
            cbLoai.setSelectedIndex(0);
        }
    }



    private JPanel createRoomBusy(Phong p){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255,230,230));
        card.setPreferredSize(new Dimension(180, 110));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,120,120)),
                new EmptyBorder(10,10,10,10)
        ));

        JLabel top = new JLabel("Tầng " + p.getTang() + " - " + p.getMaPhong());
        top.setFont(new Font("Segoe UI", Font.BOLD, 13));

        String ten = p.getTenKhach() != null ? p.getTenKhach() : "Chưa có";
        String sdt = p.getSdt() != null ? p.getSdt() : "";

        JLabel center = new JLabel(
                "<html>" + ten + "<br>" + sdt + "</html>"
        );

        JLabel status = new JLabel(p.getTrangThai(), JLabel.RIGHT);

        if("Đang dọn dẹp".equals(p.getTrangThai())){
            status.setForeground(Color.ORANGE);
        }else{
            status.setForeground(Color.RED);
        }
        status.setText("Đang thuê");
        card.setBackground(new Color(200,255,200));

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(status, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                txtMaPhong.setText(p.getMaPhong());
                txtTang.setText(String.valueOf(p.getTang()));

                // tránh lỗi null
                if(p.getTrangThai() != null){
                    cbTrangThai.setSelectedItem(p.getTrangThai());
                }

                if(p.getLoaiPhong() != null){
                    cbLoai.setSelectedItem(p.getLoaiPhong().getTenLP());
                }
            }
        });

        return card;
    }
    private String generateMaPhong(int tang){

        int max = phongDao.getMaxSoPhongTheoTang(tang);

        int next = max + 1;

        return "P" + tang + String.format("%02d", next);
    }

    // ================= FILTER =================

    private JPanel createFilter(String title, JComboBox<?> cb) {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        p.setMaximumSize(new Dimension(160, 60));
        p.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JLabel lb = new JLabel(title);
        lb.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        box.setBackground(Color.WHITE);
        box.add(cb);

        p.add(lb, BorderLayout.NORTH);
        p.add(box, BorderLayout.CENTER);

        return p;
    }

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lb;
    }
}