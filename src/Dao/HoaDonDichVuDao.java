package Dao;

import ConnectDB.Database;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class HoaDonDichVuDao {

    // lấy mã hóa đơn dịch vụ theo mã phòng
    public String getMaHoaDonDVByMaPhong(String maPhong) {

        String sql = """
            SELECT TOP 1 hddv.MaHoaDonDichVu
            FROM HoaDonDichVu hddv
            JOIN HoaDonPhong hdp
                ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
            JOIN ChiTietHoaDonPhong cthdp
                ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
            WHERE cthdp.MaPhong = ?
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // thêm dịch vụ vào phòng
    public boolean addDichVuPhong(
            String maHoaDonDV,
            String maDichVu,
            int soLuong,
            double donGia
    ) {

        String sql = """
            INSERT INTO ChiTietHoaDonDichVu
            VALUES (?, ?, GETDATE(), ?, ?, ?)
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            double thanhTien = soLuong * donGia;

            ps.setString(1, maHoaDonDV);
            ps.setString(2, maDichVu);
            ps.setInt(3, soLuong);
            ps.setDouble(4, donGia);
            ps.setDouble(5, thanhTien);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public double getTongTienDV(String maPhong) {

        String sql = """
        SELECT ISNULL(SUM(ct.ThanhTien), 0)
        FROM ChiTietHoaDonDichVu ct
        JOIN HoaDonDichVu hddv
            ON ct.MaHoaDonDichVu = hddv.MaHoaDonDichVu
        JOIN HoaDonPhong hdp
            ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
        JOIN ChiTietHoaDonPhong cthdp
            ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
        WHERE cthdp.MaPhong = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    public String getDichVuDangSuDung(String maPhong) {

        StringBuilder sb = new StringBuilder();

        String sql = """
        SELECT dv.TenDichVu, ct.SoLuong
        FROM ChiTietHoaDonDichVu ct
        JOIN DichVu dv
            ON ct.MaDichVu = dv.MaDichVu
        JOIN HoaDonDichVu hddv
            ON ct.MaHoaDonDichVu = hddv.MaHoaDonDichVu
        JOIN HoaDonPhong hdp
            ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
        JOIN ChiTietHoaDonPhong cthdp
            ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
        WHERE cthdp.MaPhong = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sb.append("- ")
                        .append(rs.getString(1))
                        .append(" x ")
                        .append(rs.getInt(2))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public List<Object[]> getDanhSachDichVuDangDung(String maPhong) {

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT 
            dv.TenDichVu,
            ct.SoLuong,
            ct.ThanhTien
        FROM ChiTietHoaDonDichVu ct
        JOIN DichVu dv
            ON ct.MaDichVu = dv.MaDichVu
        JOIN HoaDonDichVu hddv
            ON ct.MaHoaDonDichVu = hddv.MaHoaDonDichVu
        JOIN HoaDonPhong hdp
            ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
        JOIN ChiTietHoaDonPhong cthdp
            ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
        WHERE cthdp.MaPhong = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("TenDichVu"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("ThanhTien")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean updateSoLuongDichVu(String maPhong, String tenDV, int soLuongMoi) {

        String sql = """
        UPDATE ct
        SET 
            ct.SoLuong = ?,
            ct.ThanhTien = ? * ct.DonGia
        FROM ChiTietHoaDonDichVu ct
        JOIN DichVu dv
            ON ct.MaDichVu = dv.MaDichVu
        JOIN HoaDonDichVu hddv
            ON ct.MaHoaDonDichVu = hddv.MaHoaDonDichVu
        JOIN HoaDonPhong hdp
            ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
        JOIN ChiTietHoaDonPhong cthdp
            ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
        WHERE cthdp.MaPhong = ?
          AND dv.TenDichVu = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, soLuongMoi);
            ps.setInt(2, soLuongMoi);
            ps.setString(3, maPhong);
            ps.setString(4, tenDV);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

// Hàm xóa dịch vụ đang dùng


    public boolean deleteDichVuDangDung(String maPhong, String tenDV) {

        String sql = """
        DELETE ct
        FROM ChiTietHoaDonDichVu ct
        JOIN DichVu dv
            ON ct.MaDichVu = dv.MaDichVu
        JOIN HoaDonDichVu hddv
            ON ct.MaHoaDonDichVu = hddv.MaHoaDonDichVu
        JOIN HoaDonPhong hdp
            ON hddv.MaHoaDonPhong = hdp.MaHoaDonPhong
        JOIN ChiTietHoaDonPhong cthdp
            ON hdp.MaHoaDonPhong = cthdp.MaHoaDonPhong
        WHERE cthdp.MaPhong = ?
          AND dv.TenDichVu = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);
            ps.setString(2, tenDV);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}