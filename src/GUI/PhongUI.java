/*package GUI;

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
    private JComboBox<Integer> cbTangFilter;

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
        JButton btnReload = new JButton("Làm mới");

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


        cbTangFilter = new JComboBox<>();
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
        left.add(createFilter("Trạng thái", cbTrangThaiFilter));
        left.add(createFilter("Tầng", cbTangFilter));
        left.add(createFilter("Loại phòng", cbLoaiFilter));

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
            if (tangFilter != null
                    && tangFilter != 0
                    && p.getTang() != tangFilter.intValue()) {

                continue;
            }

            // lọc loại phòng
            if (loaiFilter != null
                    && p.getLoaiPhong() != null
                    && !p.getLoaiPhong()
                    .getMaLP()
                    .equals(loaiFilter.getMaLP())) {

                continue;
            }
            // ===== LOGIC TRẠNG THÁI =====
            if ("Đã đặt".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDaDat(p));
            }
            else if ("Đang thuê".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDangThue(p));
            }
            else if ("Đang dọn dẹp".equals(p.getTrangThai())) {
                roomPanel.add(createRoomDangDon(p));
            }
            else {
                roomPanel.add(createRoomTrong(p));
            }
        }

        roomPanel.setPreferredSize(
                new Dimension(800, (roomPanel.getComponentCount()/3 + 1) * 120)
        );

        roomPanel.revalidate();
        roomPanel.repaint();
    }
    private void reloadTangFilter(){

        cbTangFilter.removeAllItems();

        cbTangFilter.addItem(0);

        List<Phong> dsPhong =
                phongDao.getAllPhong();

        java.util.Set<Integer> tangSet =
                new java.util.TreeSet<>();

        for(Phong p : dsPhong){
            tangSet.add(p.getTang());
        }

        for(Integer t : tangSet){
            cbTangFilter.addItem(t);
        }
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
                // ===== CLICK PHÒNG ĐANG DỌN =====

                if ("Đang dọn dẹp".equals(p.getTrangThai())) {

                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Đã dọn phòng xong chưa?",
                            "Xác nhận dọn phòng",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {

                        boolean updated =
                                phongDao.updateTrangThai(
                                        p.getMaPhong(),
                                        "Trống"
                                );

                        if (updated) {

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Phòng đã chuyển sang trạng thái trống"
                            );

                            loadData("Tất cả", 0, null);

                        } else {

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Cập nhật thất bại"
                            );
                        }
                    }

                    return;
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

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm phòng thành công"
                );

                clearForm();
                reloadTangFilter();

                loadData("Tất cả", 0, null);

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
}*/

        package GUI;

import Dao.LoaiPhongDao;
import Dao.PhongDao;
import Entity.LoaiPhong;
import Entity.Phong;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PhongUI extends JPanel {

    // ================= COLOR =================

    private final Color BG = new Color(245, 247, 250);

    private final Color PRIMARY = new Color(41, 98, 255);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color WARNING = new Color(255, 170, 0);
    private final Color DANGER = new Color(255, 82, 82);

    private final Color TEXT = new Color(35, 35, 35);
    private final Color SUBTEXT = new Color(120, 120, 120);

    // ================= COMPONENT =================

    private JPanel roomPanel;

    private JTextField txtMaPhong;
    private JTextField txtTang;

    private JComboBox<String> cbTrangThai;
    private JComboBox<String> cbLoai;

    private JComboBox<Integer> cbTangFilter;

    // ================= DAO =================

    private PhongDao phongDao = new PhongDao();
    private LoaiPhongDao loaiDao = new LoaiPhongDao();

    private List<LoaiPhong> dsLoai;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PhongUI() {

        setLayout(new BorderLayout());
        setBackground(BG);

        // ================= HEADER =================

        add(createHeader(), BorderLayout.NORTH);

        // ================= FORM =================

        JPanel formCard = createFormCard();

        // ================= BUTTON =================

        JPanel buttonPanel = createButtonPanel();

        // ================= LEFT FILTER =================

        JPanel leftFilter = createFilterPanel();

        // ================= ROOM PANEL =================


        roomPanel = new JPanel();

        roomPanel.setLayout(
                new GridLayout(0, 5, 14, 14)
        );

        roomPanel.setBackground(new Color(248, 250, 252));

        roomPanel.setBorder(
                new EmptyBorder(16, 16, 16, 16)
        );


        JScrollPane scroll = new JScrollPane(roomPanel);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(BG);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );

        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(7, 0)
        );

        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(
                new Color(245, 245, 245)
        );

        scroll.getVerticalScrollBar().setUI(
                new javax.swing.plaf.basic.BasicScrollBarUI() {

                    @Override
                    protected void configureScrollBarColors() {

                        thumbColor = new Color(180, 180, 180);
                        trackColor = new Color(245, 245, 245);
                    }

                    @Override
                    protected JButton createDecreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(int orientation) {
                        return createZeroButton();
                    }

                    private JButton createZeroButton() {

                        JButton btn = new JButton();

                        btn.setPreferredSize(new Dimension(0, 0));

                        return btn;
                    }
                }
        );


        // ================= CENTER =================

        JPanel center = new JPanel(
                new BorderLayout(16, 16)
        );

        center.setBackground(BG);

        center.setBorder(
                new EmptyBorder(0, 20, 20, 20)
        );

        center.add(leftFilter, BorderLayout.WEST);
        center.add(scroll, BorderLayout.CENTER);

        // ================= TOP =================

        JPanel top = new JPanel();

        top.setLayout(
                new BoxLayout(top, BoxLayout.Y_AXIS)
        );

        top.setBackground(BG);

        top.add(formCard);
        top.add(Box.createVerticalStrut(10));
        top.add(buttonPanel);

        // ================= WRAPPER =================

        JPanel wrapper = new JPanel(
                new BorderLayout()
        );

        wrapper.setBackground(BG);

        wrapper.add(top, BorderLayout.NORTH);
        wrapper.add(center, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);

        loadData("Tất cả", 0, null);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel wrapper = new JPanel(
                new BorderLayout()
        );

        wrapper.setBackground(
                new Color(248, 250, 252)
        );

        wrapper.setBorder(
                new EmptyBorder(14, 20, 10, 20)
        );

        RoundedPanel card =
                new RoundedPanel(26);

        card.setBackground(Color.WHITE);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBorder(
                new EmptyBorder(18, 20, 18, 20)
        );

        JLabel title = new JLabel(
                "QUẢN LÝ PHÒNG"
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        title.setForeground(
                new Color(15, 23, 42)
        );




        card.add(title);
        card.add(Box.createVerticalStrut(6));

        wrapper.add(card);

        return wrapper;
    }

    // =========================================================
    // FORM
    // =========================================================

    private JPanel createFormCard() {

        RoundedPanel card = new RoundedPanel(24);

        card.setBackground(Color.WHITE);

        card.setLayout(new BorderLayout());

        card.setBorder(
                new EmptyBorder(14, 20, 14, 20)
        );

        JPanel form = new JPanel(
                new GridLayout(2, 4, 18, 10)
        );

        form.setOpaque(false);

        txtMaPhong = createTextField();
        txtMaPhong.setEditable(false);

        txtTang = createTextField();

        txtTang.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                try {

                    int tang = Integer.parseInt(
                            txtTang.getText()
                    );

                    txtMaPhong.setText(
                            generateMaPhong(tang)
                    );

                } catch (Exception ex) {

                    txtMaPhong.setText("");
                }
            }
        });

        cbTrangThai = createComboBox(
                new String[]{
                        "Trống",
                        "Đã đặt",
                        "Đang thuê",
                        "Đang dọn dẹp"
                }
        );

        cbLoai = createComboBox(new String[]{});

        loadLoaiPhong();

        form.add(createField("Mã phòng", txtMaPhong));
        form.add(createField("Tầng", txtTang));
        form.add(createField("Trạng thái", cbTrangThai));
        form.add(createField("Loại phòng", cbLoai));

        card.add(form, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // BUTTON PANEL
    // =========================================================

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        14,
                        0
                )
        );

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(12, 0, 18, 0)
        );

        JButton btnThem =
                createButton("THÊM",
                        new Color(37, 99, 235));

        ImageIcon iconThem = new ImageIcon(
                "photo/plus.png"
        );

        Image imgThem = iconThem.getImage().getScaledInstance(
                14,
                14,
                Image.SCALE_SMOOTH
        );

        btnThem.setIcon(
                new ImageIcon(imgThem)
        );

        JButton btnSua =
                createButton("SỬA",
                        new Color(245, 158, 11));
        ImageIcon iconSua = new ImageIcon(
                "photo/settings.png"
        );

        Image imgSua = iconSua.getImage().getScaledInstance(
                14,
                14,
                Image.SCALE_SMOOTH
        );

        btnSua.setIcon(
                new ImageIcon(imgSua)
        );

        JButton btnXoa =
                createButton("XÓA",
                        new Color(239, 68, 68));
        ImageIcon iconXoa = new ImageIcon(
                "src/photo/delete (1).png"
        );

        Image imgXoa = iconXoa.getImage().getScaledInstance(
                14,
                14,
                Image.SCALE_SMOOTH
        );

        btnXoa.setIcon(
                new ImageIcon(imgXoa)
        );

        JButton btnReload =
                createButton("LÀM MỚI",
                        new Color(100, 116, 139));
        ImageIcon iconReload = new ImageIcon(
                "src/photo/refresh-348.png"
        );

        Image imgReload = iconReload.getImage().getScaledInstance(
                14,
                14,
                Image.SCALE_SMOOTH
        );

        btnReload.setIcon(
                new ImageIcon(imgReload)
        );

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnReload);

        btnReload.addActionListener(e ->
                loadData("Tất cả", 0, null)
        );

        btnThem.addActionListener(e ->
                insertPhong()
        );

        btnSua.addActionListener(e ->
                updatePhong()
        );

        btnXoa.addActionListener(e ->
                deletePhong()
        );

        return panel;
    }

    // =========================================================
    // FILTER
    // =========================================================

    private JPanel createFilterPanel() {

        RoundedPanel left =
                new RoundedPanel(24);

        left.setBackground(Color.WHITE);

        left.setPreferredSize(
                new Dimension(220, 0)
        );

        left.setLayout(
                new BoxLayout(left, BoxLayout.Y_AXIS)
        );

        left.setBorder(
                new EmptyBorder(24, 20, 24, 20)
        );

        ImageIcon iconFilter = new ImageIcon(
                "src/photo/Bộ lọc .png"
        );

        Image imgFilter =
                iconFilter.getImage().getScaledInstance(
                        20,
                        20,
                        Image.SCALE_SMOOTH
                );

        JLabel title = new JLabel(
                "BỘ LỌC",
                new ImageIcon(imgFilter),
                JLabel.LEFT
        );

        title.setIconTextGap(8);

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        title.setForeground(
                new Color(37, 99, 235)
        );

        left.add(title);

        left.add(Box.createVerticalStrut(28));

        JComboBox<String> cbTrangThaiFilter =
                createComboBox(
                        new String[]{
                                "Tất cả",
                                "Trống",
                                "Đã đặt",
                                "Đang thuê",
                                "Đang dọn dẹp"
                        }
                );

        cbTangFilter = createComboBoxInt();

        cbTangFilter.addItem(0);

        reloadTangFilter();

        JComboBox<LoaiPhong> cbLoaiFilter =
                new JComboBox<>();

        styleComboBox(cbLoaiFilter);

        cbLoaiFilter.addItem(null);

        for (LoaiPhong lp :
                loaiDao.getAllLoaiPhong()) {

            cbLoaiFilter.addItem(lp);
        }

        left.add(createFilterItem(
                "Trạng thái",
                "src/photo/images.png",
                cbTrangThaiFilter
        ));

        left.add(Box.createVerticalStrut(26));

        left.add(createFilterItem(
                "Tầng",
                "src/photo/Tầng.png",
                cbTangFilter
        ));

        left.add(Box.createVerticalStrut(26));

        left.add(createFilterItem(
                "Loại phòng",
                "src/photo/loaiPhong.png",
                cbLoaiFilter
        ));

        cbTrangThaiFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter
                                .getSelectedItem()
                                .toString(),

                        (Integer)
                                cbTangFilter
                                        .getSelectedItem(),

                        (LoaiPhong)
                                cbLoaiFilter
                                        .getSelectedItem()
                )
        );

        cbTangFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter
                                .getSelectedItem()
                                .toString(),

                        (Integer)
                                cbTangFilter
                                        .getSelectedItem(),

                        (LoaiPhong)
                                cbLoaiFilter
                                        .getSelectedItem()
                )
        );

        cbLoaiFilter.addActionListener(e ->
                loadData(
                        cbTrangThaiFilter
                                .getSelectedItem()
                                .toString(),

                        (Integer)
                                cbTangFilter
                                        .getSelectedItem(),

                        (LoaiPhong)
                                cbLoaiFilter
                                        .getSelectedItem()
                )
        );

        return left;
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void loadLoaiPhong() {

        dsLoai = loaiDao.getAllLoaiPhong();

        cbLoai.removeAllItems();

        for (LoaiPhong lp : dsLoai) {

            cbLoai.addItem(lp.getTenLP());
        }
    }

    private void loadData(
            String trangThaiFilter,
            Integer tangFilter,
            LoaiPhong loaiFilter
    ) {

        roomPanel.removeAll();

        List<Phong> list =
                phongDao.getAllPhong();

        for (Phong p : list) {

            // trạng thái
            if (!"Tất cả".equals(trangThaiFilter)
                    &&
                    !p.getTrangThai()
                            .equals(trangThaiFilter)) {

                continue;
            }

            // tầng
            if (tangFilter != null
                    &&
                    tangFilter != 0
                    &&
                    p.getTang() != tangFilter) {

                continue;
            }

            // loại phòng
            if (loaiFilter != null
                    &&
                    p.getLoaiPhong() != null
                    &&
                    !p.getLoaiPhong()
                            .getMaLP()
                            .equals(
                                    loaiFilter.getMaLP()
                            )) {

                continue;
            }

            roomPanel.add(
                    createRoomCard(p)
            );
        }

        roomPanel.revalidate();
        roomPanel.repaint();
    }

    // =========================================================
    // ROOM CARD
    // =========================================================

    private JPanel createRoomCard(Phong p) {

        // =====================================================
        // CARD
        // =====================================================

        RoundedPanel card = new RoundedPanel(24);

        card.setLayout(new BorderLayout());

        card.setBackground(Color.WHITE);

        card.setPreferredSize(
                new Dimension(205, 245)
        );

        card.setMinimumSize(
                new Dimension(205, 245)
        );

        card.setMaximumSize(
                new Dimension(205, 245)
        );

        // =====================================================
        // COLOR + ICON
        // =====================================================

        Color accent = SUCCESS;

        String icon = "🛏";

        if ("Đã đặt".equals(p.getTrangThai())) {

            accent = new Color(239,68,68);
            icon = "📅";

        } else if ("Đang thuê".equals(p.getTrangThai())) {

            accent = new Color(37,99,235);
            icon = "👤";

        } else if ("Đang dọn dẹp".equals(p.getTrangThai())) {

            accent = new Color(249,115,22);
            icon = "🧹";
        }

        final Color finalAccent = accent;

        // =====================================================
        // BORDER
        // =====================================================

        card.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                new Color(230,230,230),
                                1,
                                true
                        ),
                        new EmptyBorder(1,1,1,1)
                )
        );

        // =====================================================
        // TOP LINE
        // =====================================================

        JPanel topWrap = new JPanel(
                new BorderLayout()
        );

        topWrap.setOpaque(false);

        topWrap.setBorder(
                new EmptyBorder(8,10,0,10)
        );

        JPanel topLine = new JPanel();

        topLine.setBackground(accent);

        topLine.setPreferredSize(
                new Dimension(0,3)
        );

        topWrap.add(topLine);

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setBorder(
                new EmptyBorder(10,12,10,12)
        );

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // ICON
        // =====================================================

        JPanel iconWrap = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        0
                )
        );

        iconWrap.setOpaque(false);

        JPanel iconBg = new JPanel(
                new GridBagLayout()
        );

        iconBg.setPreferredSize(
                new Dimension(48,48)
        );

        iconBg.setMaximumSize(
                new Dimension(48,48)
        );

        iconBg.setBackground(
                new Color(
                        accent.getRed(),
                        accent.getGreen(),
                        accent.getBlue(),
                        18
                )
        );

        JLabel iconLabel = new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );

        iconBg.add(iconLabel);

        iconWrap.add(iconBg);

        // =====================================================
        // ROOM NAME
        // =====================================================

        JLabel room = new JLabel(
                p.getMaPhong(),
                JLabel.CENTER
        );

        room.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        room.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        room.setForeground(accent);

        // =====================================================
        // INFO ROW
        // =====================================================

        JPanel infoRow = new JPanel(
                new GridLayout(1,2)
        );

        infoRow.setOpaque(false);

        JLabel floor = new JLabel(
                "Tầng " + p.getTang(),
                JLabel.CENTER
        );

        floor.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        floor.setForeground(SUBTEXT);

        JPanel typePanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        4,
                        0
                )
        );

        typePanel.setOpaque(false);

        JLabel typeIcon =
                new JLabel("🏷");

        typeIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        10
                )
        );

        JLabel typeText =
                new JLabel(
                        p.getLoaiPhong() != null
                                ? p.getLoaiPhong().getTenLP()
                                : "Phòng"
                );

        typeText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        typePanel.add(typeIcon);
        typePanel.add(typeText);

        infoRow.add(floor);
        infoRow.add(typePanel);

        // =====================================================
        // SEPARATOR
        // =====================================================

        JSeparator separator =
                new JSeparator();

        separator.setForeground(
                new Color(235,235,235)
        );

        // =====================================================
        // CUSTOMER PANEL
        // =====================================================

        JPanel customerPanel = new JPanel();

        customerPanel.setOpaque(false);

        customerPanel.setLayout(
                new BoxLayout(
                        customerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        customerPanel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        customerPanel.setMaximumSize(
                new Dimension(180,45)
        );

        if ("Đang thuê".equals(p.getTrangThai())) {

            JPanel row1 = new JPanel(
                    new FlowLayout(
                            FlowLayout.CENTER,
                            4,
                            0
                    )
            );

            row1.setOpaque(false);

            JLabel userIcon =
                    new JLabel("👤");

            userIcon.setFont(
                    new Font(
                            "Segoe UI Emoji",
                            Font.PLAIN,
                            10
                    )
            );

            JLabel name =
                    new JLabel(
                            p.getTenKhach() != null
                                    ? p.getTenKhach()
                                    : "Khách hàng"
                    );

            name.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            row1.add(userIcon);
            row1.add(name);

            JPanel row2 = new JPanel(
                    new FlowLayout(
                            FlowLayout.CENTER,
                            4,
                            0
                    )
            );

            row2.setOpaque(false);

            JLabel phoneIcon =
                    new JLabel("📞");

            phoneIcon.setFont(
                    new Font(
                            "Segoe UI Emoji",
                            Font.PLAIN,
                            10
                    )
            );

            JLabel phone =
                    new JLabel(
                            p.getSdt() != null
                                    ? p.getSdt()
                                    : ""
                    );

            phone.setForeground(SUBTEXT);

            phone.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            row2.add(phoneIcon);
            row2.add(phone);

            customerPanel.add(row1);
            customerPanel.add(row2);

        } else {

            customerPanel.setPreferredSize(
                    new Dimension(0,10)
            );
        }

        // =====================================================
        // STATUS
        // =====================================================

        JPanel statusWrap = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        0,
                        0
                )
        );

        statusWrap.setOpaque(false);

        JPanel status = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        6,
                        3
                )
        );

        status.setBackground(
                new Color(
                        accent.getRed(),
                        accent.getGreen(),
                        accent.getBlue(),
                        20
                )
        );

        status.setBorder(
                new EmptyBorder(
                        3,
                        12,
                        3,
                        12
                )
        );

        JLabel dot = new JLabel("●");

        dot.setForeground(accent);

        JLabel txt = new JLabel(
                p.getTrangThai()
        );

        txt.setForeground(accent);

        txt.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        status.add(dot);
        status.add(txt);

        statusWrap.add(status);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        content.add(iconWrap);

        content.add(Box.createVerticalStrut(8));

        content.add(room);

        content.add(Box.createVerticalStrut(6));

        content.add(infoRow);

        content.add(Box.createVerticalStrut(8));

        content.add(separator);

        content.add(Box.createVerticalStrut(10));

        content.add(customerPanel);

        content.add(Box.createVerticalGlue());

        content.add(statusWrap);

        // =====================================================
        // ADD TO CARD
        // =====================================================

        card.add(topWrap, BorderLayout.NORTH);

        card.add(content, BorderLayout.CENTER);

        // =====================================================
        // HOVER
        // =====================================================

        card.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBorder(
                        new CompoundBorder(
                                new LineBorder(
                                        new Color(
                                                finalAccent.getRed(),
                                                finalAccent.getGreen(),
                                                finalAccent.getBlue(),
                                                120
                                        ),
                                        1,
                                        true
                                ),
                                new EmptyBorder(1,1,1,1)
                        )
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBorder(
                        new CompoundBorder(
                                new LineBorder(
                                        new Color(230,230,230),
                                        1,
                                        true
                                ),
                                new EmptyBorder(1,1,1,1)
                        )
                );
            }
        });

        addClickEvent(card, p);

        return card;
    }


    // =========================================================
    // CLICK EVENT
    // =========================================================

    private void addClickEvent(
            JPanel card,
            Phong p
    ) {

        card.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        card.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        card.setBorder(
                                new CompoundBorder(
                                        new LineBorder(
                                                PRIMARY,
                                                2,
                                                true
                                        ),
                                        new EmptyBorder(
                                                16,
                                                16,
                                                16,
                                                16
                                        )
                                )
                        );
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        card.setBorder(
                                new CompoundBorder(
                                        new EmptyBorder(3, 3, 3, 3),
                                        new EmptyBorder(10, 20, 10, 20)
                                )
                        );
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        txtMaPhong.setText(
                                p.getMaPhong()
                        );

                        txtTang.setText(
                                String.valueOf(
                                        p.getTang()
                                )
                        );

                        if (p.getTrangThai() != null) {

                            cbTrangThai.setSelectedItem(
                                    p.getTrangThai()
                            );
                        }

                        if (p.getLoaiPhong() != null) {

                            cbLoai.setSelectedItem(
                                    p.getLoaiPhong()
                                            .getTenLP()
                            );
                        }

                        // ================= ĐANG THUÊ =================

                        if ("Đang thuê".equals(
                                p.getTrangThai()
                        )) {

                            Object[] info =
                                    phongDao
                                            .getThongTinPhongDangThue(
                                                    p.getMaPhong()
                                            );

                            if (info != null) {

                                String message =
                                        "===== THÔNG TIN KHÁCH HÀNG =====\n\n"
                                                +
                                                "Họ tên: "
                                                + info[0]
                                                + "\n"

                                                +
                                                "SĐT: "
                                                + info[1]
                                                + "\n"

                                                +
                                                "CCCD: "
                                                + info[2]
                                                + "\n\n"

                                                +
                                                "===== THÔNG TIN PHÒNG =====\n\n"

                                                +
                                                "Mã phòng: "
                                                + info[3]
                                                + "\n"

                                                +
                                                "Loại phòng: "
                                                + info[4]
                                                + "\n"

                                                +
                                                "Thời gian nhận: "
                                                + info[5]
                                                + "\n"

                                                +
                                                "Thời gian trả: "
                                                + info[6];

                                JOptionPane.showMessageDialog(
                                        null,
                                        message,
                                        "Chi tiết phòng",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                        }

                        // ================= DỌN DẸP =================

                        if ("Đang dọn dẹp".equals(
                                p.getTrangThai()
                        )) {

                            int confirm =
                                    JOptionPane.showConfirmDialog(
                                            null,
                                            "Đã dọn phòng xong chưa?",
                                            "Xác nhận",
                                            JOptionPane.YES_NO_OPTION
                                    );

                            if (confirm ==
                                    JOptionPane.YES_OPTION) {

                                boolean updated =
                                        phongDao.updateTrangThai(
                                                p.getMaPhong(),
                                                "Trống"
                                        );

                                if (updated) {

                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Đã cập nhật trạng thái"
                                    );

                                    loadData(
                                            "Tất cả",
                                            0,
                                            null
                                    );

                                } else {

                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Cập nhật thất bại"
                                    );
                                }
                            }
                        }
                    }
                }
        );
    }

    // =========================================================
    // CRUD
    // =========================================================

    private Phong getForm() {

        try {

            String ma =
                    txtMaPhong.getText();

            int tang =
                    Integer.parseInt(
                            txtTang.getText()
                    );

            String tt =
                    cbTrangThai
                            .getSelectedItem()
                            .toString();

            LoaiPhong lp =
                    dsLoai.get(
                            cbLoai.getSelectedIndex()
                    );

            return new Phong(
                    ma,
                    tang,
                    tt,
                    lp
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Dữ liệu không hợp lệ"
            );

            return null;
        }
    }

    private void insertPhong() {

        try {

            if (txtTang.getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập tầng"
                );

                return;
            }

            int tang =
                    Integer.parseInt(
                            txtTang.getText().trim()
                    );

            txtMaPhong.setText(
                    generateMaPhong(tang)
            );

            Phong p = getForm();

            if (p == null) return;

            if (phongDao.insert(p)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm phòng thành công"
                );

                clearForm();

                reloadTangFilter();

                loadData(
                        "Tất cả",
                        0,
                        null
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Thêm thất bại"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tầng không hợp lệ"
            );
        }
    }

    private void updatePhong() {

        if (txtMaPhong.getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng"
            );

            return;
        }

        Phong p = getForm();

        if (p == null) return;

        if (phongDao.update(p)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thành công"
            );

            clearForm();

            loadData(
                    "Tất cả",
                    0,
                    null
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Cập nhật thất bại"
            );
        }
    }

    private void deletePhong() {

        String maPhong =
                txtMaPhong.getText().trim();

        if (maPhong.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn phòng"
            );

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc muốn xóa?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION)
            return;

        if (phongDao.delete(maPhong)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thành công"
            );

            clearForm();

            loadData(
                    "Tất cả",
                    0,
                    null
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thất bại"
            );
        }
    }

    // =========================================================
    // UTIL
    // =========================================================

    private void clearForm() {

        txtMaPhong.setText("");

        txtTang.setText("");

        cbTrangThai.setSelectedIndex(0);

        if (cbLoai.getItemCount() > 0) {

            cbLoai.setSelectedIndex(0);
        }
    }

    private void reloadTangFilter() {

        cbTangFilter.removeAllItems();

        cbTangFilter.addItem(0);

        List<Phong> dsPhong =
                phongDao.getAllPhong();

        Set<Integer> tangSet =
                new TreeSet<>();

        for (Phong p : dsPhong) {

            tangSet.add(p.getTang());
        }

        for (Integer t : tangSet) {

            cbTangFilter.addItem(t);
        }
    }

    private String generateMaPhong(int tang) {

        int max =
                phongDao.getMaxSoPhongTheoTang(tang);

        int next = max + 1;

        return "P"
                + tang
                + String.format("%02d", next);
    }

    // =========================================================
    // STYLE
    // =========================================================

    private JPanel createCardPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                new Color(230, 230, 230),
                                1,
                                true
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        return panel;
    }

    private JTextField createTextField() {

        JTextField txt = new JTextField();

        txt.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        txt.setPreferredSize(
                new Dimension(0, 36)
        );

        txt.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                new Color(220, 220, 220),
                                1,
                                true
                        ),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        return txt;
    }

    private JComboBox<String> createComboBox(
            String[] items
    ) {

        JComboBox<String> cb =
                new JComboBox<>(items);

        styleComboBox(cb);

        return cb;
    }

    private JComboBox<Integer> createComboBoxInt() {

        JComboBox<Integer> cb =
                new JComboBox<>();

        styleComboBox(cb);

        return cb;
    }

    private void styleComboBox(JComboBox<?> cb) {

        cb.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        cb.setPreferredSize(
                new Dimension(0, 36)
        );

        cb.setBackground(Color.WHITE);

        cb.setBorder(
                new LineBorder(
                        new Color(220, 220, 220),
                        1,
                        true
                )
        );
    }

    private JButton createButton(
            String text,
            Color color
    ) {

        JButton btn = new JButton(text);

        btn.setForeground(Color.WHITE);

        btn.setBackground(color);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        btn.setPreferredSize(
                new Dimension(150, 40)
        );

        btn.setBorder(
                new EmptyBorder(10, 22, 10, 22)
        );

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                btn.setBackground(
                        color.brighter()
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                btn.setBackground(color);
            }
        });

        return btn;
    }

    private JPanel createField(
            String title,
            JComponent comp
    ) {

        JPanel panel = new JPanel(
                new BorderLayout(0, 8)
        );

        panel.setOpaque(false);

        JLabel lb = new JLabel(title);

        lb.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        lb.setForeground(
                new Color(30, 41, 59)
        );

        panel.add(lb, BorderLayout.NORTH);
        panel.add(comp, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFilterItem(
            String title,
            String iconPath,
            JComponent comp
    ) {

        JPanel panel = new JPanel(
                new BorderLayout(0,10)
        );

        panel.setOpaque(false);

        JPanel titlePanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        6,
                        0
                )
        );

        titlePanel.setOpaque(false);

        // ================= ICON =================

        ImageIcon icon = new ImageIcon(iconPath);

        Image img = icon.getImage().getScaledInstance(
                14,
                14,
                Image.SCALE_SMOOTH
        );

        JLabel lbIcon = new JLabel(
                new ImageIcon(img)
        );

        // ================= TITLE =================

        JLabel lb = new JLabel(title);

        lb.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        lb.setForeground(
                new Color(51,65,85)
        );

        titlePanel.add(lbIcon);
        titlePanel.add(lb);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(comp, BorderLayout.CENTER);

        return panel;
    }
}
class RoundedPanel extends JPanel {

    private int radius;

    public RoundedPanel(int radius) {

        this.radius = radius;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // shadow

        g2.setColor(new Color(15,23,42,18));

        g2.fillRoundRect(
                4,
                4,
                getWidth()-8,
                getHeight()-8,
                radius,
                radius
        );

        // background

        g2.setColor(getBackground());

        g2.fillRoundRect(
                0,
                0,
                getWidth()-8,
                getHeight()-8,
                radius,
                radius
        );

        super.paintComponent(g2);

        g2.dispose();
    }

}

