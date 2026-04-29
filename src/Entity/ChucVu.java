package Entity;

import java.util.Objects;

public class ChucVu {

    private String maChucVu;
    private String tenChucVu;
    private String moTa;

    public ChucVu() {}

    public ChucVu(String maChucVu, String tenChucVu, String moTa) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.moTa = moTa;
    }

    public String getMaChucVu() { return maChucVu; }
    public void setMaChucVu(String maChucVu) { this.maChucVu = maChucVu; }

    public String getTenChucVu() { return tenChucVu; }
    public void setTenChucVu(String tenChucVu) { this.tenChucVu = tenChucVu; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public int hashCode() { return Objects.hash(maChucVu); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChucVu)) return false;
        return Objects.equals(maChucVu, ((ChucVu)obj).maChucVu);
    }

    @Override
    public String toString() {
        return tenChucVu;
    }
}