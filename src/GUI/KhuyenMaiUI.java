package GUI;

import Dao.KhuyenMaiDao;
import Entity.KhuyenMai;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KhuyenMaiUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaKM, txtTenKM, txtPhanTram;
    private JComboBox<String> cbTrangThai;
    private JDateChooser dateBD, dateKT;

    private KhuyenMaiDao dao = new KhuyenMaiDao();

    public KhuyenMaiUI() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JLabel title = new JLabel("QUẢN LÝ KHUYẾN MÃI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(3,4,30,20));
        form.setBorder(BorderFactory.createEmptyBorder(20,40,10,40));

        txtMaKM = new JTextField();
        txtTenKM = new JTextField();
        txtPhanTram = new JTextField();

        cbTrangThai = new JComboBox<>(new String[]{"Áp dụng","Ngưng"});

        dateBD = new JDateChooser();
        dateKT = new JDateChooser();

        dateBD.setDateFormatString("dd/MM/yyyy");
        dateKT.setDateFormatString("dd/MM/yyyy");

        ((JTextField) dateBD.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateKT.getDateEditor().getUiComponent()).setEditable(false);

        form.add(label("Mã KM")); form.add(wrap(txtMaKM));
        form.add(label("Tên KM")); form.add(wrap(txtTenKM));

        form.add(label("% Giảm")); form.add(wrap(txtPhanTram));
        form.add(label("Trạng thái")); form.add(wrap(cbTrangThai));

        form.add(label("Ngày BD")); form.add(wrap(dateBD));
        form.add(label("Ngày KT")); form.add(wrap(dateKT));

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
                new String[]{"Mã KM","Tên KM","%","BD","KT","Trạng thái"},0
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

        // ===== EVENT =====

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int r = table.getSelectedRow();

                txtMaKM.setText(val(r,0));
                txtTenKM.setText(val(r,1));
                txtPhanTram.setText(val(r,2));
                cbTrangThai.setSelectedItem(val(r,5));

                try {
                    Date bd = Date.valueOf(val(r,3));
                    Date kt = Date.valueOf(val(r,4));

                    dateBD.setDate(new java.util.Date(bd.getTime()));
                    dateKT.setDate(new java.util.Date(kt.getTime()));
                } catch (Exception e){
                    dateBD.setDate(null);
                    dateKT.setDate(null);
                }
            }
        });

        btnThem.addActionListener(e -> {
            KhuyenMai km = getForm();
            if(km != null && dao.insert(km)){
                loadData();
            }
        });

        btnSua.addActionListener(e -> {
            KhuyenMai km = getForm();
            if(km != null && dao.update(km)){
                loadData();
            }
        });

        btnXoa.addActionListener(e -> {
            if(dao.delete(txtMaKM.getText())){
                loadData();
            }
        });

        btnReload.addActionListener(e -> loadData());

        loadData();
    }

    // ===== SUPPORT =====

    private JPanel wrap(JComponent c){
        JPanel p = new JPanel(new BorderLayout());
        p.add(c);
        return p;
    }

    private JLabel label(String t){
        JLabel lb = new JLabel(t);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lb;
    }

    private String val(int r,int c){
        Object v = model.getValueAt(r,c);
        return v==null?"":v.toString();
    }

    private KhuyenMai getForm(){
        try{
            double pt = Double.parseDouble(txtPhanTram.getText());

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

            return new KhuyenMai(
                    txtMaKM.getText(),
                    txtTenKM.getText(),
                    pt,
                    bd.toLocalDate(),
                    kt.toLocalDate(),
                    cbTrangThai.getSelectedItem().toString()
            );

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Lỗi dữ liệu!");
            return null;
        }
    }

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
}