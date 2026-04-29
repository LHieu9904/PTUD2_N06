package Entity;

import java.sql.Timestamp;
import java.util.Objects;

public class ChiTietHoaDonDichVu {

    private HoaDonDichVu hoaDonDichVu;
    private DichVu dichVu;
    private Timestamp thoiDiemSuDung;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    public ChiTietHoaDonDichVu() {}

    public HoaDonDichVu getHoaDonDichVu() { return hoaDonDichVu; }
    public void setHoaDonDichVu(HoaDonDichVu h) { this.hoaDonDichVu = h; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu d) { this.dichVu = d; }

    public Timestamp getThoiDiemSuDung() { return thoiDiemSuDung; }
    public void setThoiDiemSuDung(Timestamp t) { this.thoiDiemSuDung = t; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int s) { this.soLuong = s; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double d) { this.donGia = d; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double t) { this.thanhTien = t; }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDonDichVu, dichVu, thoiDiemSuDung);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChiTietHoaDonDichVu)) return false;
        ChiTietHoaDonDichVu other = (ChiTietHoaDonDichVu) obj;
        return Objects.equals(hoaDonDichVu, other.hoaDonDichVu)
                && Objects.equals(dichVu, other.dichVu)
                && Objects.equals(thoiDiemSuDung, other.thoiDiemSuDung);
    }
}