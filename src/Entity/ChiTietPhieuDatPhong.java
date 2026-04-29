package Entity;

import java.time.LocalDateTime;

public class ChiTietPhieuDatPhong {

    private String maPhieuDatPhong;
    private String maPhong;

    private LocalDateTime thoiGianNhan;
    private LocalDateTime thoiGianTra;

    private int soLuong;

    public ChiTietPhieuDatPhong(){}

    public ChiTietPhieuDatPhong(String maPhieuDatPhong, String maPhong,
                                LocalDateTime thoiGianNhan,
                                LocalDateTime thoiGianTra,
                                int soLuong) {
        this.maPhieuDatPhong = maPhieuDatPhong;
        this.maPhong = maPhong;
        this.thoiGianNhan = thoiGianNhan;
        this.thoiGianTra = thoiGianTra;
        this.soLuong = soLuong;
    }

    public String getMaPhieuDatPhong() { return maPhieuDatPhong; }
    public String getMaPhong() { return maPhong; }
    public LocalDateTime getThoiGianNhan() { return thoiGianNhan; }
    public LocalDateTime getThoiGianTra() { return thoiGianTra; }
    public int getSoLuong() { return soLuong; }
}