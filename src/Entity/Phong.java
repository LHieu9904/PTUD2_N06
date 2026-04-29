package Entity;

public class Phong {

    private String maPhong;
    private int tang;
    private String trangThai;
    private LoaiPhong loaiPhong;

    private String tenKhach;
    private String sdt;

    public Phong(){}

    public Phong(String maPhong, int tang, String trangThai, LoaiPhong loaiPhong){
        this.maPhong = maPhong;
        this.tang = tang;
        this.trangThai = trangThai;
        this.loaiPhong = loaiPhong;
    }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public int getTang() { return tang; }
    public void setTang(int tang) { this.tang = tang; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LoaiPhong getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(LoaiPhong loaiPhong) { this.loaiPhong = loaiPhong; }

    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}