package Entity;

import java.util.Objects;

public class DichVu {

    private String maDichVu;
    private String tenDichVu;
    private double donGia;
    private String trangThai;

    public DichVu() {}

    public DichVu(String maDichVu, String tenDichVu, double donGia, String trangThai) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.donGia = donGia;
        this.trangThai = trangThai;
    }

    public String getMaDichVu() { return maDichVu; }
    public void setMaDichVu(String maDichVu) { this.maDichVu = maDichVu; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public int hashCode() { return Objects.hash(maDichVu); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DichVu)) return false;
        return Objects.equals(maDichVu, ((DichVu)obj).maDichVu);
    }
}