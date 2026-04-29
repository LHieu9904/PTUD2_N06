package GUI;

import Raven.button.Button;
import Raven.textfield.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GiaHanPhong_UI extends JPanel {

    public GiaHanPhong_UI() {

        setLayout(new BorderLayout());
        setBackground(new Color(220,240,250));

        // ===== HEADER =====
        JPanel header = new JPanel();
        header.setBackground(new Color(0,102,204));
        header.setPreferredSize(new Dimension(0,50));

        JLabel title = new JLabel("GIA HẠN PHÒNG");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 18));

        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== BODY =====
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(15,20,15,20));
        body.setBackground(new Color(200,230,250));
        add(body, BorderLayout.CENTER);

        // ===== SEARCH =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        searchPanel.setBackground(new Color(200,230,250));

        TextField txtSearch = new TextField();
        txtSearch.setPreferredSize(new Dimension(300,40));
        txtSearch.setHint("Nhập mã phòng / SĐT");

        Button btnSearch = new Button();
        btnSearch.setBackground(Color.RED);
        btnSearch.setPreferredSize(new Dimension(40,40));

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        body.add(searchPanel);

        // ===== INFO =====
        JPanel infoPanel = new JPanel(new GridLayout(3,2,10,10));
        infoPanel.setBackground(new Color(240,250,255));
        infoPanel.setBorder(new EmptyBorder(10,10,10,10));

        infoPanel.add(createField("Mã phòng"));
        infoPanel.add(createField("Tên khách"));

        infoPanel.add(createField("SĐT"));
        infoPanel.add(createField("Ngày lập hóa đơn"));

        infoPanel.add(createField("Trạng thái"));
        infoPanel.add(createField("Tiền thuê"));

        body.add(infoPanel);

        // ===== EXTEND TIME =====
        JPanel timePanel = new JPanel(new GridLayout(1,4,10,0));
        timePanel.setBorder(new EmptyBorder(10,0,10,0));
        timePanel.setBackground(new Color(200,230,250));

        timePanel.add(createBtn("+1H"));
        timePanel.add(createBtn("+2H"));
        timePanel.add(createBtn("+3H"));

        JComboBox<String> cbTime = new JComboBox<>(new String[]{
                "Chọn giờ thêm", "30 phút", "1 giờ", "2 giờ"
        });
        cbTime.setPreferredSize(new Dimension(100,40));

        timePanel.add(cbTime);
        body.add(timePanel);

        // ===== PAYMENT =====
        JPanel payPanel = new JPanel(new GridLayout(2,2,10,10));
        payPanel.setBackground(new Color(0,102,204));
        payPanel.setBorder(new EmptyBorder(15,15,15,15));

        payPanel.add(createFieldWhite("Thời gian trả mới"));
        payPanel.add(createCombo("Phương thức thanh toán"));

        payPanel.add(createFieldWhite("Phí gia hạn"));
        payPanel.add(new JLabel());

        body.add(payPanel);

        // ===== FOOTER =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        footer.setBackground(new Color(200,230,250));

        Button btnConfirm = new Button();
        btnConfirm.setText("Xác nhận");

        Button btnCancel = new Button();
        btnCancel.setText("Hủy");

        footer.add(btnConfirm);
        footer.add(btnCancel);

        body.add(footer);
    }

    // ===== COMPONENT =====

    private JPanel createField(String label){
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBackground(new Color(240,250,255));

        JLabel lbl = new JLabel(label);

        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(150,35));
        tf.setEditable(false);
        tf.setBackground(new Color(245,245,245));

        p.add(lbl, BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);

        return p;
    }

    private JPanel createFieldWhite(String label){
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBackground(new Color(0,102,204));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);

        TextField tf = new TextField();
        tf.setPreferredSize(new Dimension(150,35));

        p.add(lbl, BorderLayout.NORTH);
        p.add(tf, BorderLayout.CENTER);

        return p;
    }

    private JPanel createCombo(String label){
        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBackground(new Color(0,102,204));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);

        JComboBox<String> cb = new JComboBox<>(new String[]{
                "Tiền mặt","Chuyển khoản","Momo"
        });

        p.add(lbl, BorderLayout.NORTH);
        p.add(cb, BorderLayout.CENTER);

        return p;
    }

    private Button createBtn(String text){
        Button b = new Button();
        b.setText(text);
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(80,40));
        return b;
    }

    // TEST
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(900,650);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GiaHanPhong_UI());
        f.setVisible(true);
    }
}