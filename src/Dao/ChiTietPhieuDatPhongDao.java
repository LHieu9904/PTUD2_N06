package Dao;

import ConnectDB.Database;
import java.sql.*;
import java.time.LocalDateTime;

public class ChiTietPhieuDatPhongDao {

    public boolean insert(
            String maPhieu,
            String maPhong,
            LocalDateTime tgNhan,
            LocalDateTime tgTra,
            int soNguoi
    ) {

        // 🌟 ĐÃ SỬA: Bổ sung thêm cột SoLuong và dấu chấm hỏi (?) thứ 5 vào câu lệnh SQL
        String sql = """
            INSERT INTO ChiTietPhieuDatPhong
            (
                MaPhieuDatPhong,
                MaPhong,
                ThoiGianNhan,
                ThoiGianTra,
                SoLuong
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhieu);
            ps.setString(2, maPhong);

            ps.setTimestamp(
                    3,
                    Timestamp.valueOf(tgNhan)
            );

            ps.setTimestamp(
                    4,
                    Timestamp.valueOf(tgTra)
            );

            // 🌟 ĐÃ SỬA: Nạp tham số số người vào dấu chấm hỏi thứ 5
            ps.setInt(5, soNguoi);

            int rs = ps.executeUpdate();

            System.out.println(
                    "INSERT CT PDP = " + rs
            );

            return rs > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateThoiGianNhanTra(
            String maPhong,
            java.time.LocalDateTime thoiGianNhan,
            java.time.LocalDateTime thoiGianTra
    ) {

        String sql = """
        UPDATE ChiTietPhieuDatPhong
        SET ThoiGianNhan = ?, ThoiGianTra = ?
        WHERE MaPhieuDatPhong = (
            SELECT TOP 1 MaPhieuDatPhong
            FROM ChiTietPhieuDatPhong
            WHERE MaPhong = ?
            ORDER BY ThoiGianNhan DESC
        )
        AND MaPhong = ?
    """;

        try (
                java.sql.Connection con = ConnectDB.Database.getInstance().getConnection();
                java.sql.PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(thoiGianNhan));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(thoiGianTra));
            ps.setString(3, maPhong);
            ps.setString(4, maPhong);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}