package Entity;

import java.sql.Date;
import java.util.Objects;

public class NhanVien {

    private String maNV;
    private String hoTen;
    private int gioiTinh;
    private String cccd;
    private Date ngaySinh;
    private String trangThaiLamViec;
    private ChucVu chucVu;
    private double luong;
    private String anhNhanVien;
    private String email;
    private String sdt;

    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, int gioiTinh, String cccd,
                    Date ngaySinh, String trangThaiLamViec, ChucVu chucVu,
                    double luong, String anhNhanVien, String email, String sdt) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.cccd = cccd;
        this.ngaySinh = ngaySinh;
        this.trangThaiLamViec = trangThaiLamViec;
        this.chucVu = chucVu;
        this.luong = luong;
        this.anhNhanVien = anhNhanVien;
        this.email = email;
        this.sdt = sdt;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public int getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(int gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getTrangThaiLamViec() { return trangThaiLamViec; }
    public void setTrangThaiLamViec(String t) { this.trangThaiLamViec = t; }

    public ChucVu getChucVu() { return chucVu; }
    public void setChucVu(ChucVu chucVu) { this.chucVu = chucVu; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getAnhNhanVien() { return anhNhanVien; }
    public void setAnhNhanVien(String a) { this.anhNhanVien = a; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    @Override
    public int hashCode() { return Objects.hash(maNV); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NhanVien)) return false;
        return Objects.equals(maNV, ((NhanVien)obj).maNV);
    }

    @Override
    public String toString() {
        return hoTen;
    }

}