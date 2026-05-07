package GUI;

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
}