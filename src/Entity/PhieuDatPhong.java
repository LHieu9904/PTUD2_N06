package Entity;

import java.time.LocalDateTime;

public class PhieuDatPhong {

    private String maPhieuDatPhong;
    private LocalDateTime thoiGianDat;
    private String maKH;
    private String maNV;

    public PhieuDatPhong(){}

    public PhieuDatPhong(String maPhieuDatPhong, String maKH, String maNV) {
        this.maPhieuDatPhong = maPhieuDatPhong;
        this.maKH = maKH;
        this.maNV = maNV;
    }

    public String getMaPhieuDatPhong() { return maPhieuDatPhong; }
    public String getMaKH() { return maKH; }
    public String getMaNV() { return maNV; }
}