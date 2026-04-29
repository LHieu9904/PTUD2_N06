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

        String sql = """
        SELECT MAX(
            CAST(
                SUBSTRING(MaKH, 3, LEN(MaKH)) AS INT
            )
        )
        FROM KhachHang
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                int so = rs.getInt(1) + 1;

                return String.format("KH%03d", so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "KH001";
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
    public String insertKhach(String hoTen, String sdt) {

        String maKH = "KH" + (System.currentTimeMillis() % 100000);

        // tránh UNIQUE bị NULL
        String cccd = String.valueOf(
                System.currentTimeMillis() % 1000000000
        );

        String sql = """
        INSERT INTO KhachHang
        (
            MaKH,
            HoTen,
            SDT,
            CCCD,
            GioiTinh
        )
        VALUES (?, ?, ?, ?, ?)
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maKH);
            ps.setString(2, hoTen);
            ps.setString(3, sdt);

            // tuyệt đối không null
            ps.setString(4, cccd);

            ps.setInt(5, 1);

            ps.executeUpdate();

            return maKH;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getMaKHBySDT(String sdt) {

        String sql = """
        SELECT MaKH
        FROM KhachHang
        WHERE SDT = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, sdt);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("MaKH");
            }

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

}