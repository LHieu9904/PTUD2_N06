package Entity;

import java.math.BigDecimal;
import java.util.Objects;

public class LoaiPhong {

    private String maLP;
    private String tenLP;

    // 🔥 dùng BigDecimal cho tiền (chuẩn DB DECIMAL)
    private BigDecimal giaGioDau;
    private BigDecimal giaGioTiepTheo;
    private BigDecimal giaCaNgay;

    // ===== CONSTRUCTOR =====
    public LoaiPhong() {}

    public LoaiPhong(String maLP, String tenLP,
                     BigDecimal giaGioDau,
                     BigDecimal giaGioTiepTheo,
                     BigDecimal giaCaNgay) {
        this.maLP = maLP;
        this.tenLP = tenLP;
        this.giaGioDau = giaGioDau;
        this.giaGioTiepTheo = giaGioTiepTheo;
        this.giaCaNgay = giaCaNgay;
    }

    // ===== GETTER / SETTER =====
    public String getMaLP() { return maLP; }
    public void setMaLP(String maLP) { this.maLP = maLP; }

    public String getTenLP() { return tenLP; }
    public void setTenLP(String tenLP) { this.tenLP = tenLP; }

    public BigDecimal getGiaGioDau() { return giaGioDau; }
    public void setGiaGioDau(BigDecimal giaGioDau) { this.giaGioDau = giaGioDau; }

    public BigDecimal getGiaGioTiepTheo() { return giaGioTiepTheo; }
    public void setGiaGioTiepTheo(BigDecimal giaGioTiepTheo) { this.giaGioTiepTheo = giaGioTiepTheo; }

    public BigDecimal getGiaCaNgay() { return giaCaNgay; }
    public void setGiaCaNgay(BigDecimal giaCaNgay) { this.giaCaNgay = giaCaNgay; }

    // ===== equals / hashCode =====
    @Override
    public int hashCode() {
        return Objects.hash(maLP);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LoaiPhong)) return false;
        LoaiPhong other = (LoaiPhong) obj;
        return Objects.equals(maLP, other.maLP);
    }

    // ===== toString (dùng cho JComboBox) =====
    @Override
    public String toString() {
        return tenLP;
    }
}