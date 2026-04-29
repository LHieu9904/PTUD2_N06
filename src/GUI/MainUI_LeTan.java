package GUI;

import Entity.TaiKhoan;

import javax.swing.*;

public class MainUI_LeTan extends JFrame {

    public MainUI_LeTan(TaiKhoan tk) {

        setTitle("Giao diện Lễ Tân");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lb = new JLabel("ĐÂY LÀ MÀN HÌNH LỄ TÂN", JLabel.CENTER);
        add(lb);
    }
}