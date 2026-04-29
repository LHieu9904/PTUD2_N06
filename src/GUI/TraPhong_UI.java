package GUI;

import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraPhong_UI extends JPanel {

    private JPanel tableCanTT;
    private JPanel tableDaTT;
    private JPanel tableDaChon;

    public TraPhong_UI() {

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5,5,5,5));

        // ===== HEADER =====
        JPanel header = new JPanel();
        JLabel title = new JLabel("TRẢ PHÒNG - THANH TOÁN");
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setForeground(new Color(0,153,255));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== MAIN =====
        JPanel main = new JPanel(new BorderLayout());
        add(main, BorderLayout.CENTER);

        JPanel center = new JPanel(new GridLayout(1,3,10,0));
        main.add(center, BorderLayout.CENTER);

        // ===== CỘT 1 =====
        JPanel col1 = new JPanel(new GridLayout(2,1,5,5));

        col1.add(createList("Phòng chưa thanh toán", tableCanTT = new JPanel()));
        col1.add(createList("Phòng đã thanh toán", tableDaTT = new JPanel()));

        center.add(col1);

        // ===== CỘT 2 =====
        JPanel col2 = new JPanel(new BorderLayout());
        col2.add(createList("Phòng đang chọn", tableDaChon = new JPanel()));
        center.add(col2);

        // ===== CỘT 3 =====
        JPanel col3 = new JPanel(new BorderLayout());

        // KHÁCH HÀNG
        JPanel panelKH = new JPanel(new GridLayout(2,2,5,5));
        panelKH.setBorder(createBorder("Khách hàng"));

        panelKH.add(new JLabel("Họ tên"));
        panelKH.add(createField());

        panelKH.add(new JLabel("SĐT"));
        panelKH.add(createField());

        col3.add(panelKH, BorderLayout.NORTH);

        // PHÒNG
        JPanel panelPhong = new JPanel(new BorderLayout());
        panelPhong.setBorder(createBorder("Thông tin phòng"));

        JPanel info = new JPanel(new GridLayout(3,2,5,5));

        info.add(new JLabel("Mã phòng"));
        info.add(createField());

        info.add(new JLabel("Ngày nhận"));
        info.add(createField());

        info.add(new JLabel("Ngày trả"));
        info.add(createField());

        panelPhong.add(info, BorderLayout.NORTH);

        // ===== TABLE DỊCH VỤ =====
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Dịch vụ", "Số lượng", "Thành tiền"}, 0
        );

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);

        panelPhong.add(sp, BorderLayout.CENTER);

        col3.add(panelPhong, BorderLayout.CENTER);

        center.add(col3);

        // ===== FOOTER =====
        JPanel footer = new JPanel(new GridLayout(1,3,10,0));

        // tiền còn lại
        JPanel p1 = new JPanel();
        p1.add(new JLabel("Còn lại"));

        TextField tfConLai = createField();
        tfConLai.setForeground(Color.RED);
        p1.add(tfConLai);

        // tổng tiền
        JPanel p2 = new JPanel();
        p2.add(new JLabel("Tổng tiền"));

        TextField tfTong = createField();
        tfTong.setForeground(Color.RED);
        p2.add(tfTong);

        // button
        JPanel p3 = new JPanel();

        Button btnTra = new Button();
        btnTra.setText("Trả phòng");

        Button btnTT = new Button();
        btnTT.setText("Thanh toán");

        p3.add(btnTra);
        p3.add(btnTT);

        footer.add(p1);
        footer.add(p2);
        footer.add(p3);

        add(footer, BorderLayout.SOUTH);
    }

    // ===== COMPONENT =====

    private JPanel createList(String title, JPanel content){
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(content), BorderLayout.CENTER);

        panel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        return panel;
    }

    private TitledBorder createBorder(String title){
        return BorderFactory.createTitledBorder(
                new LineBorder(Color.GRAY),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Tahoma", Font.BOLD, 13)
        );
    }

    private TextField createField(){
        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(150,30));
        tf.setEditable(false);
        tf.setBackground(new Color(245,245,245));
        return tf;
    }
}