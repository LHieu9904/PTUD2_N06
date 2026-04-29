package Entity;

import java.sql.Timestamp;
import java.util.Objects;

public class HoaDonPhong {

    private String maHoaDonPhong;
    private Timestamp ngayLap;
    private PhieuDatPhong phieuDatPhong;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private double tongTien;
    private double tienThue;
    private String trangThai;

    public HoaDonPhong() {}

    public String getMaHoaDonPhong() { return maHoaDonPhong; }
    public void setMaHoaDonPhong(String m) { this.maHoaDonPhong = m; }

    public Timestamp getNgayLap() { return ngayLap; }
    public void setNgayLap(Timestamp n) { this.ngayLap = n; }

    public PhieuDatPhong getPhieuDatPhong() { return phieuDatPhong; }
    public void setPhieuDatPhong(PhieuDatPhong p) { this.phieuDatPhong = p; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien n) { this.nhanVien = n; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang k) { this.khachHang = k; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double t) { this.tongTien = t; }

    public double getTienThue() { return tienThue; }
    public void setTienThue(double t) { this.tienThue = t; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String t) { this.trangThai = t; }

    @Override
    public int hashCode() { return Objects.hash(maHoaDonPhong); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HoaDonPhong)) return false;
        return Objects.equals(maHoaDonPhong, ((HoaDonPhong)obj).maHoaDonPhong);
    }
}