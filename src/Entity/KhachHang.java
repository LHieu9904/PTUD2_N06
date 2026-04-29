package Entity;

import java.util.Objects;

public class KhachHang {

    private String maKH;
    private String hoTen;
    private String sdt;
    private String cccd;
    private int gioiTinh;

    public KhachHang() {}

    public KhachHang(String maKH, String hoTen, String sdt, String cccd, int gioiTinh) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.cccd = cccd;
        this.gioiTinh = gioiTinh;
    }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public int getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(int gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Override
    public int hashCode() { return Objects.hash(maKH); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KhachHang)) return false;
        return Objects.equals(maKH, ((KhachHang)obj).maKH);
    }
}