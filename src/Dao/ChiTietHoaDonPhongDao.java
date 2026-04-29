package Dao;

import ConnectDB.Database;

import java.sql.*;
import java.time.LocalDateTime;

public class ChiTietHoaDonPhongDao {

    // ================= THÊM CHI TIẾT =================
    public boolean addChiTiet(String maHD, String maPhong) {

        String sql = """
            INSERT INTO ChiTietHoaDonPhong
            (
                MaHoaDonPhong,
                MaPhong,
                ThoiGianNhan,
                ThoiGianTra,
                TrangThaiSuDung,
                TrangThaiThanhToan
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            LocalDateTime now = LocalDateTime.now();

            ps.setString(1, maHD);
            ps.setString(2, maPhong);

            // bắt buộc NOT NULL
            ps.setTimestamp(
                    3,
                    Timestamp.valueOf(now)
            );

            // chưa trả phòng nên để null
            ps.setTimestamp(
                    4,
                    null
            );

            ps.setString(
                    5,
                    "Đang dùng"
            );

            ps.setString(
                    6,
                    "Chưa TT"
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= CẬP NHẬT TRẠNG THÁI =================
    public boolean updateTrangThai(String maHD, String maPhong, String tt){

        String sql = """
            UPDATE ChiTietHoaDonPhong
            SET TrangThaiSuDung = ?
            WHERE MaHoaDonPhong = ? AND MaPhong = ?
        """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, tt);
            ps.setString(2, maHD);
            ps.setString(3, maPhong);

            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // ================= CHECK PHÒNG ĐANG DÙNG =================
    public boolean isPhongDangSuDung(String maPhong){

        String sql = """
            SELECT COUNT(*)
            FROM ChiTietHoaDonPhong ct
            JOIN HoaDonPhong hd ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
            WHERE ct.MaPhong = ?
              AND hd.NgayTra IS NULL
        """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(1) > 0;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public boolean insert(
            String maHD,
            String maPhong,
            Timestamp thoiGianNhan
    ) {

        String sql = """
        INSERT INTO ChiTietHoaDonPhong
        (
            MaHoaDonPhong,
            MaPhong,
            ThoiGianNhan,
            ThoiGianTra,
            TrangThaiSuDung,
            TrangThaiThanhToan
        )
        VALUES (?, ?, ?, NULL, ?, ?)
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maHD);
            ps.setString(2, maPhong);
            ps.setTimestamp(3, thoiGianNhan);
            ps.setString(4, "Đang dùng");
            ps.setString(5, "Chưa TT");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}