package Entity;

import java.util.Objects;

public class HoaDonDichVu {

    private String maHoaDonDichVu;
    private HoaDonPhong hoaDonPhong;

    public HoaDonDichVu() {}

    public String getMaHoaDonDichVu() { return maHoaDonDichVu; }
    public void setMaHoaDonDichVu(String m) { this.maHoaDonDichVu = m; }

    public HoaDonPhong getHoaDonPhong() { return hoaDonPhong; }
    public void setHoaDonPhong(HoaDonPhong h) { this.hoaDonPhong = h; }

    @Override
    public int hashCode() { return Objects.hash(maHoaDonDichVu); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HoaDonDichVu)) return false;
        return Objects.equals(maHoaDonDichVu, ((HoaDonDichVu)obj).maHoaDonDichVu);
    }
}