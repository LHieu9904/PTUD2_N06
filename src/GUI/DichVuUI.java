package GUI;

import Dao.DichVuDao;
import Entity.DichVu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DichVuUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtGia;
    private JComboBox<String> cbTrangThai;

    private DichVuDao dao = new DichVuDao();

    public DichVuUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JLabel title = new JLabel("QUẢN LÝ DỊCH VỤ", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(2,4,30,20));
        form.setBorder(BorderFactory.createEmptyBorder(20,40,10,40));

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtGia = new JTextField();
        cbTrangThai = new JComboBox<>(new String[]{
                "Còn",
                "Hết"
        });

        form.add(label("Mã DV")); form.add(txtMa);
        form.add(label("Tên DV")); form.add(txtTen);

        form.add(label("Đơn giá")); form.add(txtGia);
        form.add(label("Trạng thái")); form.add(cbTrangThai);

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

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Mã DV","Tên DV","Đơn giá","Trạng thái"},0
        );

        table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

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

        // ================= EVENT =================

        // CLICK TABLE (FIX NULL)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = table.getSelectedRow();

                txtMa.setText(getValue(row,0));
                txtTen.setText(getValue(row,1));
                txtGia.setText(getValue(row,2));

                cbTrangThai.setSelectedItem(getValue(row,3));
            }
        });

        // THÊM
        btnThem.addActionListener(e -> {
            if(!validateInput()) return;

            DichVu dv = getForm();

            if(dao.insert(dv)){
                JOptionPane.showMessageDialog(this,"Thêm thành công");
                loadData();
                clearForm();
            }else{
                JOptionPane.showMessageDialog(this,"Thêm thất bại");
            }
        });

        // SỬA
        btnSua.addActionListener(e -> {
            if(!validateInput()) return;

            DichVu dv = getForm();

            if(dao.update(dv)){
                JOptionPane.showMessageDialog(this,"Cập nhật thành công");
                loadData();
            }else{
                JOptionPane.showMessageDialog(this,"Cập nhật thất bại");
            }
        });

        // XÓA
        btnXoa.addActionListener(e -> {

            String ma = txtMa.getText();

            if(ma.isEmpty()){
                JOptionPane.showMessageDialog(this,"Chọn dịch vụ cần xóa");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xóa dịch vụ này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION){
                if(dao.delete(ma)){
                    JOptionPane.showMessageDialog(this,"Xóa thành công");
                    loadData();
                    clearForm();
                }else{
                    JOptionPane.showMessageDialog(this,"Xóa thất bại");
                }
            }
        });

        // RELOAD
        btnReload.addActionListener(e -> {
            loadData();
            clearForm();
        });

        loadData();
    }

    // ================= SUPPORT =================

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lb;
    }

    private void loadData() {
        model.setRowCount(0);

        List<DichVu> list = dao.getAll();

        for (DichVu dv : list) {
            model.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getDonGia(),
                    dv.getTrangThai()
            });
        }
    }

    private String getValue(int row, int col){
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

    private DichVu getForm(){
        DichVu dv = new DichVu();
        dv.setMaDichVu(txtMa.getText());
        dv.setTenDichVu(txtTen.getText());
        dv.setDonGia(Double.parseDouble(txtGia.getText()));
        dv.setTrangThai(cbTrangThai.getSelectedItem().toString());
        return dv;
    }

    private void clearForm(){
        txtMa.setText("");
        txtTen.setText("");
        txtGia.setText("");
        cbTrangThai.setSelectedIndex(0);
    }

    private boolean validateInput(){

        if(txtMa.getText().isEmpty() ||
                txtTen.getText().isEmpty() ||
                txtGia.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Không được để trống");
            return false;
        }

        try{
            double gia = Double.parseDouble(txtGia.getText());
            if(gia < 0){
                JOptionPane.showMessageDialog(this,"Giá phải >= 0");
                return false;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Giá không hợp lệ");
            return false;
        }

        return true;
    }
}