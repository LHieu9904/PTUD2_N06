// FILE 1: Entity/TaiKhoan.java
package Entity;

import java.util.Date;
import java.util.Objects;

public class TaiKhoan {
    private String maTK;
    private String tenDangNhap;
    private String matKhau;
    private String trangThai;

    private String maNV;
    private String hoTen;
    private int gioiTinh;
    private String cccd;
    private Date ngaySinh;
    private String trangThaiLamViec;
    private String maChucVu;
    private double luong;
    private String email;
    private String sdt;

    public TaiKhoan() {}

    public String getMaTK() { return maTK; }
    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

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
    public void setTrangThaiLamViec(String trangThaiLamViec) { this.trangThaiLamViec = trangThaiLamViec; }

    public String getMaChucVu() { return maChucVu; }
    public void setMaChucVu(String maChucVu) { this.maChucVu = maChucVu; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    @Override
    public int hashCode() {
        return Objects.hash(maTK);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TaiKhoan)) return false;
        TaiKhoan other = (TaiKhoan) obj;
        return Objects.equals(maTK, other.maTK);
    }
}
