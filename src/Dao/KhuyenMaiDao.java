package Dao;

import ConnectDB.Database;
import Entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDao {

    public List<KhuyenMai> getAll() {

        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new KhuyenMai(
                        rs.getString("MaKhuyenMai"),
                        rs.getString("TenKhuyenMai"),
                        rs.getDouble("PhanTramGiam"),
                        rs.getDate("NgayBatDau").toLocalDate(),
                        rs.getDate("NgayKetThuc").toLocalDate(),
                        rs.getString("TrangThai")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(KhuyenMai km) {

        String sql = "INSERT INTO KhuyenMai VALUES (?,?,?,?,?,?)";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(6, km.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(KhuyenMai km) {

        String sql = """
            UPDATE KhuyenMai
            SET TenKhuyenMai=?,
                PhanTramGiam=?,
                NgayBatDau=?,
                NgayKetThuc=?,
                TrangThai=?
            WHERE MaKhuyenMai=?
        """;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getTenKhuyenMai());
            ps.setDouble(2, km.getPhanTramGiam());
            ps.setDate(3, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(4, Date.valueOf(km.getNgayKetThuc()));
            ps.setString(5, km.getTrangThai());
            ps.setString(6, km.getMaKhuyenMai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String ma) {

        String sql = "DELETE FROM KhuyenMai WHERE MaKhuyenMai=?";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ma);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}