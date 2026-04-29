package Entity;

import java.time.LocalDate;

public class KhuyenMai {

    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String trangThai;

    public KhuyenMai(){}

    public KhuyenMai(String ma, String ten, double pt,
                     LocalDate bd, LocalDate kt, String tt) {
        this.maKhuyenMai = ma;
        this.tenKhuyenMai = ten;
        this.phanTramGiam = pt;
        this.ngayBatDau = bd;
        this.ngayKetThuc = kt;
        this.trangThai = tt;
    }

    public String getMaKhuyenMai() { return maKhuyenMai; }
    public void setMaKhuyenMai(String ma) { this.maKhuyenMai = ma; }

    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public void setTenKhuyenMai(String ten) { this.tenKhuyenMai = ten; }

    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double pt) { this.phanTramGiam = pt; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate bd) { this.ngayBatDau = bd; }

    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate kt) { this.ngayKetThuc = kt; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String tt) { this.trangThai = tt; }
}