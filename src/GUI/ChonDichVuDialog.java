package GUI;

import Dao.DichVuDao;
import Entity.DichVu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChonDichVuDialog extends JDialog {

    private JTable table;
    private DefaultTableModel model;

    private String maPhong;
    private String maHDDV;

    private DichVuDao dvDao = new DichVuDao();

    public ChonDichVuDialog(Frame parent, String maPhong, String maHDDV) {

        super(parent, "Chọn dịch vụ", true);

        this.maPhong = maPhong;
        this.maHDDV = maHDDV;

        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        // ===== TITLE =====
        JLabel title = new JLabel("DANH SÁCH DỊCH VỤ", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(30);

        loadDichVu();

        // DOUBLE CLICK → ADD
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    themDichVu();
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAdd = new JButton("➕ Thêm dịch vụ");
        JButton btnClose = new JButton("❌ Đóng");

        btnAdd.addActionListener(e -> themDichVu());
        btnClose.addActionListener(e -> dispose());

        bottom.add(btnAdd);
        bottom.add(btnClose);

        add(bottom, BorderLayout.SOUTH);
    }

    // ================= LOAD DV =================
    private void loadDichVu(){

        model.setRowCount(0);

        List<DichVu> list = dvDao.getDichVuCon();

        for(DichVu dv : list){
            model.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    dv.getDonGia()
            });
        }
    }

    // ================= ADD DV =================
    private void themDichVu(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
            return;
        }

        String maDV = model.getValueAt(row, 0).toString();
        String tenDV = model.getValueAt(row, 1).toString();
        double gia = Double.parseDouble(model.getValueAt(row, 2).toString());

        boolean ok = dvDao.addOrUpdateDichVu(
                maHDDV,
                maDV,
                gia
        );

        if(ok){
            JOptionPane.showMessageDialog(this,
                    "Đã thêm: " + tenDV);
        }else{
            JOptionPane.showMessageDialog(this,
                    "Thêm thất bại!");
        }
    }
}