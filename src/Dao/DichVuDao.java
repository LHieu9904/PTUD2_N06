package Dao;

import ConnectDB.Database;
import Entity.DichVu;

import java.sql.*;
import java.util.*;

public class DichVuDao {

    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();

        String sql = "SELECT * FROM DichVu";

        try (Connection con = Database.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new DichVu(
                        rs.getString("MaDichVu"),
                        rs.getString("TenDichVu"),
                        rs.getDouble("DonGia"),
                        rs.getString("TrangThai")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(DichVu dv) {
        String sql = "INSERT INTO DichVu VALUES (?,?,?,?)";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dv.getMaDichVu());
            ps.setString(2, dv.getTenDichVu());
            ps.setDouble(3, dv.getDonGia());
            ps.setString(4, dv.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(DichVu dv) {
        String sql = """
            UPDATE DichVu
            SET TenDichVu=?, DonGia=?, TrangThai=?
            WHERE MaDichVu=?
        """;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dv.getTenDichVu());
            ps.setDouble(2, dv.getDonGia());
            ps.setString(3, dv.getTrangThai());
            ps.setString(4, dv.getMaDichVu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String maDV) {
        String sql = "DELETE FROM DichVu WHERE MaDichVu=?";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDV);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
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
                return rs.getString("MaHoaDonDichVu");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



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
    public List<DichVu> getDichVuCon() {

        List<DichVu> list = new ArrayList<>();

        String sql = """
        SELECT *
        FROM DichVu
        WHERE TrangThai = N'Còn'
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                list.add(new DichVu(
                        rs.getString("MaDichVu"),
                        rs.getString("TenDichVu"),
                        rs.getDouble("DonGia"),
                        rs.getString("TrangThai")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    // TÍNH TỔNG TIỀN DỊCH VỤ THEO PHÒNG


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
}