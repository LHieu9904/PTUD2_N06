package Dao;

import ConnectDB.Database;
import Entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDao {

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();

        String sql = "SELECT * FROM KhachHang";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                KhachHang kh = new KhachHang();

                kh.setMaKH(rs.getString("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setGioiTinh(Integer.parseInt(String.valueOf(rs.getInt("GioiTinh")))); // INT đúng DB
                kh.setSdt(rs.getString("SDT"));
                kh.setCccd(rs.getString("CCCD"));

                list.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===== Thêm =====
    public boolean create(KhachHang kh){
        String sql = "INSERT INTO KhachHang(MaKH, HoTen, GioiTinh, SDT, CCCD) VALUES(?,?,?,?,?)";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoTen());
            ps.setInt(3, kh.getGioiTinh()); // INT
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getCccd());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Trùng SDT hoặc CCCD!");
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // ===== SỬA =====
    public boolean update(KhachHang kh){
        String sql = "UPDATE KhachHang SET HoTen=?, GioiTinh=?, SDT=?, CCCD=? WHERE MaKH=?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, kh.getHoTen());
            ps.setInt(2, kh.getGioiTinh());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getCccd());
            ps.setString(5, kh.getMaKH());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Trùng SDT hoặc CCCD!");
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // ===== XÓA =====
    public boolean delete(String maKH){
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // ===== SINH MÃ KH =====
    public String getNextMaKH() {

        String maKH;

        do {

            maKH = String.format(
                    "KH%04d",
                    (int)(Math.random() * 10000)
            );

        } while (existsMaKH(maKH));

        return maKH;
    }

    private boolean existsMaKH(String maKH) {

        String sql =
                "SELECT * FROM KhachHang WHERE MaKH = ?";

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setString(1, maKH);

            ResultSet rs =
                    ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    // =========================================================================
    // 1. LẤY DANH SÁCH SĐT VÀ TÊN ĐỂ GỢI Ý KHI GÕ CHỮ (ĐÃ FIX LỖI SQL SERVER)
    // =========================================================================
    public List<String> getDanhSachSDTGoiY(String input) {
        List<String> list = new ArrayList<>();

        // Sử dụng TOP 5 chuẩn cú pháp SQL Server (T-SQL)
        String sql = "SELECT TOP 5 SDT, HoTen FROM KhachHang WHERE SDT LIKE ?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            // Khớp các SĐT bắt đầu bằng chuỗi nhập vào (Ví dụ: "09" -> "09%")
            ps.setString(1, input.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String sdt = rs.getString("SDT");
                    String hoTen = rs.getString("HoTen");

                    // Ghép chuỗi hiển thị lên giao diện Popup: "0912345678 - Nguyễn Văn A"
                    list.add(sdt + " - " + hoTen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // 2. LẤY CHI TIẾT MẢNG THÔNG TIN KHÁCH HÀNG THEO SĐT (ĐỒNG BỘ CHUẨN ĐỔ DATA)
    // =========================================================================
    public Object[] getKhachHangBySDT(String sdt) {
        String sql = "SELECT MaKH, HoTen, GioiTinh, CCCD FROM KhachHang WHERE SDT = ?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, sdt.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] khData = new Object[4];
                    khData[0] = rs.getString("MaKH");
                    khData[1] = rs.getString("HoTen");

                    int gioiTinhInt = rs.getInt("GioiTinh");
                    khData[2] = (gioiTinhInt == 1) ? "Nam" : "Nữ";

                    // SỬA TẠI ĐÂY: Bảo toàn đọc dữ liệu dạng chuỗi văn bản thuần túy
                    khData[3] = rs.getString("CCCD");

                    return khData;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== CHECK TRÙNG =====
    public boolean isDuplicate(String sdt, String cccd){
        String sql = "SELECT 1 FROM KhachHang WHERE SDT=? OR CCCD=?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, sdt);
            ps.setString(2, cccd);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
    // =========================================================================
    // THỐNG KÊ KHÁCH HÀNG CÓ LƯỢT ĐẶT/THUÊ PHÒNG NHIỀU NHẤT
    // =========================================================================
    // =========================================================================
    // THỐNG KÊ CHÍNH XÁC TỔNG LƯỢT ĐẶT + THUÊ PHÒNG TRỰC TIẾP CỦA KHÁCH HÀNG
    // =========================================================================
    public List<Object[]> getThongKeTongLuotDenKhachHang(java.util.Date tuNgay, java.util.Date denNgay) {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            WITH TongHopLuotDen AS (
                -- Luồng 1: Khách hàng đặt phòng trước thông qua phiếu đặt
                SELECT MaKH, ThoiGianDat AS NgayGiaoDich 
                FROM PhieuDatPhong
                
                UNION ALL
                
                -- Luồng 2: Khách hàng vãng lai đến thuê phòng trực tiếp tại quầy
                SELECT MaKH, NgayLapHoaDon AS NgayGiaoDich 
                FROM HoaDonPhong
                WHERE MaPhieuDatPhong IS NULL -- Tránh đếm trùng những hóa đơn sinh ra từ phiếu đặt
            )
            SELECT 
                kh.MaKH,
                kh.HoTen,
                kh.SDT,
                kh.CCCD,
                kh.GioiTinh,
                COUNT(th.MaKH) AS TongSoLuotDen
            FROM KhachHang kh
            JOIN TongHopLuotDen th ON kh.MaKH = th.MaKH
            WHERE th.NgayGiaoDich BETWEEN ? AND ?
            GROUP BY kh.MaKH, kh.HoTen, kh.SDT, kh.CCCD, kh.GioiTinh
            ORDER BY TongSoLuotDen DESC
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                            rs.getString("MaKH"),
                            rs.getString("HoTen"),
                            rs.getString("SDT"),
                            rs.getString("CCCD"),
                            rs.getInt("GioiTinh"),
                            rs.getInt("TongSoLuotDen")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== SEARCH =====
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();

        String sql = """
            SELECT * FROM KhachHang
            WHERE MaKH LIKE ?
               OR HoTen LIKE ?
               OR SDT LIKE ?
               OR CCCD LIKE ?
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String key = "%" + keyword.trim() + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang();

                kh.setMaKH(rs.getString("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setGioiTinh(Integer.parseInt(String.valueOf(rs.getInt("GioiTinh"))));
                kh.setSdt(rs.getString("SDT"));
                kh.setCccd(rs.getString("CCCD"));

                list.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // Sửa thành: nhận thêm tham số cccd
    public String insertKhach(String hoTen, String sdt, String cccd) {
        String maKH = "KH" + (System.currentTimeMillis() % 100000);

        String sql = """
    INSERT INTO KhachHang (MaKH, HoTen, SDT, CCCD, GioiTinh)
    VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);
            ps.setString(2, hoTen);
            ps.setString(3, sdt);
            ps.setString(4, cccd); // Sử dụng cccd thật từ người dùng nhập
            ps.setInt(5, 1);       // Lưu ý: nên thay bằng tham số giới tính sau này

            ps.executeUpdate();
            return maKH;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public KhachHang getById(String maKH) {

        String sql = """
        SELECT *
        FROM KhachHang
        WHERE MaKH = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maKH);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                KhachHang kh = new KhachHang();

                kh.setMaKH(
                        rs.getString("MaKH")
                );

                kh.setHoTen(
                        rs.getString("HoTen")
                );

                kh.setSdt(
                        rs.getString("SDT")
                );

                kh.setCccd(
                        rs.getString("CCCD")
                );

                kh.setGioiTinh(
                        rs.getInt("GioiTinh")
                );


                return kh;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



// KIỂM TRA KHÁCH HÀNG ĐÃ TỒN TẠI CHƯA
// theo SDT hoặc CCCD

    public KhachHang timKhachHangTonTai(String sdt, String cccd) {

        String sql = """
        SELECT *
        FROM KhachHang
        WHERE SDT = ?
           OR CCCD = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, sdt);
            ps.setString(2, cccd);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                KhachHang kh = new KhachHang();

                kh.setMaKH(
                        rs.getString("MaKH")
                );

                kh.setHoTen(
                        rs.getString("HoTen")
                );

                kh.setSdt(
                        rs.getString("SDT")
                );

                kh.setCccd(
                        rs.getString("CCCD")
                );

                kh.setGioiTinh(
                        rs.getInt("GioiTinh")
                );

                return kh;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getMaKHBySDT(String sdt) {
        String sql = "SELECT MaKH FROM KhachHang WHERE SDT = ?";
        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("MaKH");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }public void updateCCCD(String maKH, String cccd) {
        String sql = "UPDATE KhachHang SET CCCD = ? WHERE MaKH = ?";
        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cccd);
            ps.setString(2, maKH);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}