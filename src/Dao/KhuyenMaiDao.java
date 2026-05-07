package Dao;

import ConnectDB.Database;
import Entity.KhuyenMai;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDao {

    // =====================================================
    // LẤY TẤT CẢ KHUYẾN MÃI
    // =====================================================

    public List<KhuyenMai> getAll() {

        List<KhuyenMai> list = new ArrayList<>();

        String sql = "SELECT * FROM KhuyenMai";

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                list.add(
                        new KhuyenMai(
                                rs.getString("MaKhuyenMai"),
                                rs.getString("TenKhuyenMai"),
                                rs.getDouble("PhanTramGiam"),
                                rs.getDate("NgayBatDau").toLocalDate(),
                                rs.getDate("NgayKetThuc").toLocalDate(),
                                rs.getString("TrangThai")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // THÊM KHUYẾN MÃI
    // =====================================================

    public boolean insert(KhuyenMai km) {

        String sql = """
            INSERT INTO KhuyenMai
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

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

    // =====================================================
    // CẬP NHẬT KHUYẾN MÃI
    // =====================================================

    public boolean update(KhuyenMai km) {

        String sql = """
            UPDATE KhuyenMai
            SET
                TenKhuyenMai = ?,
                PhanTramGiam = ?,
                NgayBatDau = ?,
                NgayKetThuc = ?,
                TrangThai = ?
            WHERE MaKhuyenMai = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

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

    // =====================================================
    // XÓA KHUYẾN MÃI
    // =====================================================

    public boolean delete(String ma) {

        String sql = """
            DELETE FROM KhuyenMai
            WHERE MaKhuyenMai = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, ma);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // LẤY DANH SÁCH TÊN KHUYẾN MÃI
    // =====================================================

    public List<String> getAllTenKhuyenMai(){

        List<String> list = new ArrayList<>();

        String sql = """
        SELECT TenKhuyenMai 
        FROM KhuyenMai
        WHERE TrangThai = N'Áp dụng'
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                list.add(rs.getString("TenKhuyenMai"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // LẤY PHẦN TRĂM GIẢM GIÁ
    // =====================================================

    public double getPhanTramGiamGia(String tenKhuyenMai) {

        if (tenKhuyenMai == null
                || tenKhuyenMai.trim().isEmpty()
                || tenKhuyenMai.equals("Không áp dụng")) {
            return 0;
        }

        String sql = """
            SELECT PhanTramGiam
            FROM KhuyenMai
            WHERE TenKhuyenMai = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, tenKhuyenMai);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("PhanTramGiam");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // LẤY MÃ KHUYẾN MÃI THEO TÊN
    // =====================================================

    public String getMaKhuyenMaiByTen(String tenKhuyenMai) {

        String sql = """
            SELECT MaKhuyenMai
            FROM KhuyenMai
            WHERE TenKhuyenMai = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, tenKhuyenMai);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("MaKhuyenMai");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // KIỂM TRA TỒN TẠI
    // =====================================================

    public boolean exists(String tenKhuyenMai) {

        String sql = """
            SELECT 1
            FROM KhuyenMai
            WHERE TenKhuyenMai = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, tenKhuyenMai);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}