package Dao;

import ConnectDB.Database;
import Entity.LoaiPhong;
import Entity.Phong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongDao {

    public List<Phong> getAllPhong() {

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT
            p.MaPhong,
            p.Tang,
            p.TrangThai,

            lp.MaLP,
            lp.TenLP,
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay,

            kh.HoTen,
            kh.SDT,
                              
            pd.MaPhieuDatPhong,
                              
            hd.TrangThai AS TrangThaiHD,
            ctHD.TrangThaiSuDung,
            ctHD.TrangThaiThanhToan

        FROM Phong p

        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP

       LEFT JOIN ChiTietPhieuDatPhong ct
                                    ON p.MaPhong = ct.MaPhong
                                
                                LEFT JOIN PhieuDatPhong pd
                                    ON ct.MaPhieuDatPhong = pd.MaPhieuDatPhong
                                
                                LEFT JOIN KhachHang kh
                                    ON pd.MaKH = kh.MaKH
                                
                                LEFT JOIN ChiTietHoaDonPhong ctHD
                                    ON p.MaPhong = ctHD.MaPhong
                                
                                LEFT JOIN HoaDonPhong hd
                                    ON ctHD.MaHoaDonPhong = hd.MaHoaDonPhong
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                LoaiPhong lp = new LoaiPhong();
                lp.setMaLP(rs.getString("MaLP"));
                lp.setTenLP(rs.getString("TenLP"));
                lp.setGiaGioDau(rs.getBigDecimal("GiaGioDau"));
                lp.setGiaGioTiepTheo(rs.getBigDecimal("GiaGioTiepTheo"));
                lp.setGiaCaNgay(rs.getBigDecimal("GiaCaNgay"));

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                // lấy thông tin khách
                p.setTenKhach(rs.getString("HoTen"));
                p.setSdt(rs.getString("SDT"));

                // nếu có phiếu đặt phòng -> phòng đã đặt
                // 1. ĐẶT PHÒNG -> MÀU ĐỎ
                // 1. NHẬN PHÒNG -> MÀU XANH (ưu tiên trước)
                if (
                        "Chưa thanh toán".equals(rs.getString("TrangThaiHD"))
                                || "Đang dùng".equals(rs.getString("TrangThaiSuDung"))
                ) {
                    p.setTrangThai("Đang thuê");
                }

// 2. CHỈ ĐẶT PHÒNG -> MÀU ĐỎ
                else if (rs.getString("MaPhieuDatPhong") != null) {
                    p.setTrangThai("Đã đặt");
                }

// 3. THANH TOÁN XONG -> MÀU VÀNG
                else if (
                        "Đã thanh toán".equals(rs.getString("TrangThaiHD"))
                                || "Đã TT".equals(rs.getString("TrangThaiThanhToan"))
                ) {
                    p.setTrangThai("Đang dọn dẹp");
                }

// 4. CÒN LẠI -> TRỐNG
                else {
                    p.setTrangThai("Trống");
                }

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(Phong p){

        String sql = "INSERT INTO Phong VALUES (?,?,?,?)";

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTrangThai());
            ps.setInt(3, p.getTang());
            ps.setString(4, p.getLoaiPhong().getMaLP());

            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(Phong p){

        String sql = """
            UPDATE Phong
            SET TrangThai=?, Tang=?, MaLP=?
            WHERE MaPhong=?
        """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, p.getTrangThai());
            ps.setInt(2, p.getTang());
            ps.setString(3, p.getLoaiPhong().getMaLP());
            ps.setString(4, p.getMaPhong());

            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(String maPhong) {

        Connection con = null;

        try {
            con = Database.getInstance().getConnection();
            con.setAutoCommit(false);

            // 1. xóa chi tiết hóa đơn phòng
            String sql1 = """
            DELETE FROM ChiTietHoaDonPhong
            WHERE MaPhong = ?
        """;

            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setString(1, maPhong);
            ps1.executeUpdate();

            // 2. xóa chi tiết phiếu đặt phòng
            String sql2 = """
            DELETE FROM ChiTietPhieuDatPhong
            WHERE MaPhong = ?
        """;

            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, maPhong);
            ps2.executeUpdate();

            // 3. xóa phòng
            String sql3 = """
            DELETE FROM Phong
            WHERE MaPhong = ?
        """;

            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setString(1, maPhong);
            ps3.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        }

        return false;
    }
    public int countPhongByTang(int tang){

        String sql = "SELECT COUNT(*) FROM Phong WHERE Tang=?";

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, tang);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(1);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    public int getMaxSoPhongTheoTang(int tang){

        String sql = """
        SELECT MAX(CAST(SUBSTRING(MaPhong, 3, 2) AS INT))
        FROM Phong
        WHERE Tang = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, tang);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt(1); // có thể = 0 nếu null
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }
    public List<Phong> getPhongTrong(){

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT p.MaPhong, p.Tang, p.TrangThai,
               lp.MaLP, lp.TenLP
        FROM Phong p
        JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
        WHERE p.TrangThai = N'Trống'
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                LoaiPhong lp = new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        null,null,null
                );

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                list.add(p);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public boolean updateTrangThai(String maPhong, String trangThai) {

        String sql = """
        UPDATE Phong
        SET TrangThai = ?
        WHERE MaPhong = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, trangThai);
            ps.setString(2, maPhong);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public java.util.List<String> getAllLoaiPhong(){

        java.util.List<String> list = new java.util.ArrayList<>();

        String sql = "SELECT TenLP FROM LoaiPhong";

        try(java.sql.Connection con = ConnectDB.Database.getInstance().getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                list.add(rs.getString("TenLP"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public boolean isPhongDaDat(String maPhong){

        String sql = """
        SELECT COUNT(*)
        FROM ChiTietPhieuDatPhong ct
        JOIN PhieuDatPhong pd 
            ON ct.MaPhieuDatPhong = pd.MaPhieuDatPhong
        WHERE ct.MaPhong = ?
        AND ct.ThoiGianNhan > GETDATE()
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
    public boolean isPhongDangThue(String maPhong){

        String sql = """
        SELECT COUNT(*)
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd 
            ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        WHERE ct.MaPhong = ?
        AND hd.TrangThai = N'Chưa thanh toán'
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
    public Object[] getThongTinPhongDangThue(String maPhong) {

        String sql = """
        SELECT TOP 1
            kh.HoTen,
            kh.SDT,
            kh.CCCD,

            p.MaPhong,
            lp.TenLP,

            cthd.ThoiGianNhan,
            cthd.ThoiGianTra

        FROM Phong p

        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP

        JOIN ChiTietHoaDonPhong cthd
            ON p.MaPhong = cthd.MaPhong

        JOIN HoaDonPhong hd
            ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong

        JOIN KhachHang kh
            ON hd.MaKH = kh.MaKH

        WHERE p.MaPhong = ?
          AND p.TrangThai = N'Đang thuê'

        ORDER BY cthd.ThoiGianNhan DESC
    """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Object[]{

                        rs.getString("HoTen"),       // tên khách
                        rs.getString("SDT"),         // sdt
                        rs.getString("CCCD"),        // cccd

                        rs.getString("MaPhong"),     // mã phòng
                        rs.getString("TenLP"),       // loại phòng

                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // ===== THÊM vào PhongDao.java =====
// load phòng trống theo loại phòng (Đơn / Đôi / VIP) :contentReference[oaicite:0]{index=0}

    // =====================================================
// SỬA HÀM getPhongTrongTheoLoai()
// =====================================================

    public List<Phong> getPhongTrongTheoLoai(String tenLoaiPhong) {

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT
            p.MaPhong,
            p.Tang,
            p.TrangThai,

            lp.MaLP,
            lp.TenLP,
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay

        FROM Phong p
        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP

        WHERE
            p.TrangThai = N'Trống'
            AND lp.TenLP = ?

        ORDER BY p.MaPhong
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, tenLoaiPhong);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LoaiPhong lp = new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        rs.getBigDecimal("GiaGioDau"),
                        rs.getBigDecimal("GiaGioTiepTheo"),
                        rs.getBigDecimal("GiaCaNgay")
                );

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Phong> searchPhongByMa(String maPhong) {

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT
            p.MaPhong,
            p.Tang,
            p.TrangThai,

            lp.MaLP,
            lp.TenLP,
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay

        FROM Phong p
        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP

        WHERE p.MaPhong LIKE ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, "%" + maPhong + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LoaiPhong lp = new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        rs.getBigDecimal("GiaGioDau"),
                        rs.getBigDecimal("GiaGioTiepTheo"),
                        rs.getBigDecimal("GiaCaNgay")
                );

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean updateTrangThaiPhong(
            String maPhong,
            String trangThai
    ) {
        return updateTrangThai(
                maPhong,
                trangThai
        );
    }
    public List<Phong> getPhongTrongChoThue() {

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT
            p.MaPhong,
            p.Tang,
            p.TrangThai,
            lp.MaLP,
            lp.TenLP,
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay
        FROM Phong p
        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP
        WHERE p.TrangThai = N'Trống'
        ORDER BY p.MaPhong
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                LoaiPhong lp = new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        rs.getBigDecimal("GiaGioDau"),
                        rs.getBigDecimal("GiaGioTiepTheo"),
                        rs.getBigDecimal("GiaCaNgay")
                );

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Phong> getPhongTrongTheoLoaiChoThue(String tenLoaiPhong) {

        List<Phong> list = new ArrayList<>();

        String sql = """
        SELECT
            p.MaPhong,
            p.Tang,
            p.TrangThai,
            lp.MaLP,
            lp.TenLP,
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay
        FROM Phong p
        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP
        WHERE p.TrangThai = N'Trống'
          AND lp.TenLP = ?
        ORDER BY p.MaPhong
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, tenLoaiPhong);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LoaiPhong lp = new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        rs.getBigDecimal("GiaGioDau"),
                        rs.getBigDecimal("GiaGioTiepTheo"),
                        rs.getBigDecimal("GiaCaNgay")
                );

                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }




}