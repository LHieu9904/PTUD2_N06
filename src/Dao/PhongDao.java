package Dao;

import ConnectDB.Database;
import Entity.LoaiPhong;
import Entity.Phong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongDao {

   /* public List<Phong> getAllPhong() {

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
        ctHD.TrangThaiThanhToan,

        ctHD.ThoiGianNhan,
        ctHD.ThoiGianTra

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

                // ===== LOẠI PHÒNG =====
                LoaiPhong lp = new LoaiPhong();
                lp.setMaLP(rs.getString("MaLP"));
                lp.setTenLP(rs.getString("TenLP"));
                lp.setGiaGioDau(rs.getBigDecimal("GiaGioDau"));
                lp.setGiaGioTiepTheo(rs.getBigDecimal("GiaGioTiepTheo"));
                lp.setGiaCaNgay(rs.getBigDecimal("GiaCaNgay"));

                // ===== PHÒNG =====
                Phong p = new Phong(
                        rs.getString("MaPhong"),
                        rs.getInt("Tang"),
                        rs.getString("TrangThai"),
                        lp
                );

                p.setTenKhach(rs.getString("HoTen"));
                p.setSdt(rs.getString("SDT"));

                // ===== LẤY DỮ LIỆU =====
                String maPhieu = rs.getString("MaPhieuDatPhong");
                String trangThaiSuDung = rs.getString("TrangThaiSuDung");
                String trangThaiHD = rs.getString("TrangThaiHD");
                String trangThaiTT = rs.getString("TrangThaiThanhToan");

                Timestamp tgNhan = rs.getTimestamp("ThoiGianNhan");

                // =====================================================
                // LOGIC TRẠNG THÁI CHUẨN
                // =====================================================

                // 1. ĐANG THUÊ → chỉ khi thực sự đã nhận phòng
                // 1. ĐANG THUÊ → chỉ khi có hóa đơn + chưa thanh toán
                if ("Đang dùng".equals(trangThaiSuDung)
                        && "Chưa thanh toán".equals(trangThaiHD)) {

                    p.setTrangThai("Đang thuê");
                }

// 2. ĐÃ ĐẶT → có phiếu nhưng chưa có sử dụng
                else if (maPhieu != null && trangThaiSuDung == null) {

                    p.setTrangThai("Đã đặt");
                }

// 3. ĐANG DỌN
                else if ("Đã thanh toán".equals(trangThaiHD)
                        || "Đã TT".equals(trangThaiTT)) {

                    p.setTrangThai("Đang dọn dẹp");
                }

// 4. TRỐNG
                else {
                    p.setTrangThai("Trống");
                }

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }*/
   public List<Phong> getAllPhong() {

       List<Phong> list = new ArrayList<>();

       String sql = """
    SELECT DISTINCT

        p.MaPhong,
        p.Tang,

        p.TrangThai AS TrangThaiPhong,

        lp.MaLP,
        lp.TenLP,
        lp.GiaGioDau,
        lp.GiaGioTiepTheo,
        lp.GiaCaNgay,

        -- KHÁCH HÀNG
        COALESCE(kh1.HoTen, kh2.HoTen) AS HoTen,
        COALESCE(kh1.SDT, kh2.SDT) AS SDT,

        hd.TrangThai AS TrangThaiHD

    FROM Phong p

    JOIN LoaiPhong lp
        ON p.MaLP = lp.MaLP

    -- =================================================
    -- PHÒNG ĐANG THUÊ
    -- =================================================

    LEFT JOIN ChiTietHoaDonPhong cthd
        ON p.MaPhong = cthd.MaPhong

    LEFT JOIN HoaDonPhong hd
        ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong
        AND hd.TrangThai = N'Chưa thanh toán'

    LEFT JOIN KhachHang kh1
        ON hd.MaKH = kh1.MaKH

    -- =================================================
    -- PHÒNG ĐÃ ĐẶT
    -- =================================================

    LEFT JOIN ChiTietPhieuDatPhong ctpdp
        ON p.MaPhong = ctpdp.MaPhong

    LEFT JOIN PhieuDatPhong pdp
        ON ctpdp.MaPhieuDatPhong = pdp.MaPhieuDatPhong

    LEFT JOIN KhachHang kh2
        ON pdp.MaKH = kh2.MaKH

    ORDER BY p.MaPhong
""";

       try (

               Connection con =
                       Database.getInstance().getConnection();

               PreparedStatement ps =
                       con.prepareStatement(sql);

               ResultSet rs =
                       ps.executeQuery()

       ) {

           while (rs.next()) {

               // =====================================================
               // LOẠI PHÒNG
               // =====================================================

               LoaiPhong lp = new LoaiPhong();

               lp.setMaLP(
                       rs.getString("MaLP")
               );

               lp.setTenLP(
                       rs.getString("TenLP")
               );

               lp.setGiaGioDau(
                       rs.getBigDecimal("GiaGioDau")
               );

               lp.setGiaGioTiepTheo(
                       rs.getBigDecimal("GiaGioTiepTheo")
               );

               lp.setGiaCaNgay(
                       rs.getBigDecimal("GiaCaNgay")
               );

               // =====================================================
               // PHÒNG
               // =====================================================

               Phong p = new Phong();

               p.setMaPhong(
                       rs.getString("MaPhong")
               );

               p.setTang(
                       rs.getInt("Tang")
               );

               p.setLoaiPhong(lp);

               // =====================================================
               // KHÁCH HÀNG
               // =====================================================

               p.setTenKhach(
                       rs.getString("HoTen")
               );

               p.setSdt(
                       rs.getString("SDT")
               );

               // =====================================================
               // TRẠNG THÁI
               // =====================================================

               String trangThaiHD =
                       rs.getString("TrangThaiHD");

               String trangThaiPhong =
                       rs.getString("TrangThaiPhong");

               // ĐANG THUÊ
               if ("Chưa thanh toán".equals(trangThaiHD)) {

                   p.setTrangThai("Đang thuê");
               }

               // ĐANG DỌN DẸP
               else if ("Đang dọn dẹp".equals(trangThaiPhong)) {

                   p.setTrangThai("Đang dọn dẹp");
               }

               // ĐÃ ĐẶT
               else if ("Đã đặt".equals(trangThaiPhong)) {

                   p.setTrangThai("Đã đặt");
               }

               // TRỐNG
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

    LEFT JOIN PhieuDatPhong pd
        ON hd.MaPhieuDatPhong = pd.MaPhieuDatPhong

    JOIN KhachHang kh
        ON kh.MaKH =
            COALESCE(pd.MaKH, hd.MaKH)

    WHERE p.MaPhong = ?
      AND hd.TrangThai = N'Chưa thanh toán'

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

                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getString("CCCD"),

                        rs.getString("MaPhong"),
                        rs.getString("TenLP"),

                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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
    public Object[] getThongTinGiaHanPhong(
            String keyword
    ) {

        String sql = """
    SELECT TOP 1

        p.MaPhong,
        kh.HoTen,
        kh.SDT,

        cthd.ThoiGianNhan,
        cthd.ThoiGianTra

    FROM ChiTietHoaDonPhong cthd

    JOIN HoaDonPhong hd
        ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong

    JOIN KhachHang kh
        ON hd.MaKH = kh.MaKH

    JOIN Phong p
        ON cthd.MaPhong = p.MaPhong

    WHERE hd.TrangThai = N'Chưa thanh toán'

      AND (
            p.MaPhong = ?
            OR kh.SDT = ?
      )

    ORDER BY cthd.ThoiGianNhan DESC
""";

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, keyword);
            ps.setString(2, keyword);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                return new Object[]{

                        rs.getString("MaPhong"),

                        rs.getString("HoTen"),

                        rs.getString("SDT"),

                        rs.getTimestamp("ThoiGianNhan"),

                        rs.getTimestamp("ThoiGianTra")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

// =====================================================
// 1. LOAD DANH SÁCH PHÒNG TRẢ PHÒNG
// =====================================================

    public List<Object[]> getDanhSachTraPhong() {

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT TOP 100
            p.MaPhong,
            kh.HoTen,
            kh.SDT,
            ct.ThoiGianNhan,
            ct.ThoiGianTra,
            hd.TrangThai
        FROM Phong p
        JOIN ChiTietHoaDonPhong ct
            ON p.MaPhong = ct.MaPhong
        JOIN HoaDonPhong hd
            ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        JOIN KhachHang kh
            ON hd.MaKH = kh.MaKH
        WHERE
            p.TrangThai IN (
                N'Đang thuê',
                N'Đang dọn dẹp'
            )
        ORDER BY ct.ThoiGianNhan DESC
    """;

        try (
                Connection con =
                        Database.getInstance()
                                .getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                list.add(new Object[]{
                        rs.getString("MaPhong"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra"),
                        rs.getString("TrangThai")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


// =====================================================
// 2. CHI TIẾT TRẢ PHÒNG
// QUAN TRỌNG:
// KHÔNG dùng hd.TienThue làm tiền phòng
// =====================================================

    public Object[] getChiTietTraPhong(String maPhong){

        String sql = """
    SELECT TOP 1
        p.MaPhong,
        kh.HoTen,
        kh.SDT,
        ct.ThoiGianNhan,
        ct.ThoiGianTra,
        ISNULL(
            DATEDIFF(HOUR, ct.ThoiGianNhan, ISNULL(ct.ThoiGianTra, GETDATE()))
            * lp.GiaGioDau
        ,0) AS TienPhong,
        ISNULL((
            SELECT SUM(ctdv.ThanhTien)
            FROM HoaDonDichVu hddv
            JOIN ChiTietHoaDonDichVu ctdv
                ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
            WHERE hddv.MaHoaDonPhong = hd.MaHoaDonPhong
        ),0) AS TienDichVu,
        ISNULL(
            (
                DATEDIFF(HOUR, ct.ThoiGianNhan, ISNULL(ct.ThoiGianTra, GETDATE()))
                * lp.GiaGioDau
            )
            +
            ISNULL((
                SELECT SUM(ctdv.ThanhTien)
                FROM HoaDonDichVu hddv
                JOIN ChiTietHoaDonDichVu ctdv
                    ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
                WHERE hddv.MaHoaDonPhong = hd.MaHoaDonPhong
            ),0)
        ,0) AS TongTien

    FROM ChiTietHoaDonPhong ct
    JOIN HoaDonPhong hd
        ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
    JOIN KhachHang kh
        ON hd.MaKH = kh.MaKH
    JOIN Phong p
        ON ct.MaPhong = p.MaPhong
    JOIN LoaiPhong lp
        ON p.MaLP = lp.MaLP

    WHERE ct.MaPhong = ?
    ORDER BY ct.ThoiGianNhan DESC
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Object[]{
                        rs.getString("MaPhong"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra"),
                        rs.getDouble("TienPhong"),
                        rs.getDouble("TienDichVu"),
                        rs.getDouble("TongTien")
                };
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


// =====================================================
// 3. LOAD DỊCH VỤ ĐÃ SỬ DỤNG
// =====================================================

    public List<Object[]> getDichVuDaSuDung(String maPhong){

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT 
            dv.TenDichVu,
            ctdv.SoLuong,
            ctdv.DonGia,
            ctdv.ThanhTien
        FROM HoaDonDichVu hddv
        JOIN ChiTietHoaDonDichVu ctdv
            ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
        JOIN DichVu dv
            ON ctdv.MaDichVu = dv.MaDichVu
        JOIN ChiTietHoaDonPhong ct
            ON hddv.MaHoaDonPhong = ct.MaHoaDonPhong
        WHERE ct.MaPhong = ?
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                list.add(new Object[]{
                        rs.getString("TenDichVu"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("DonGia"),
                        rs.getDouble("ThanhTien")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public Object[] getGiaPhong(String maPhong){

        String sql = """
        SELECT 
            lp.GiaGioDau,
            lp.GiaGioTiepTheo,
            lp.GiaCaNgay
        FROM Phong p
        JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
        WHERE p.MaPhong = ?
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Object[]{
                        rs.getDouble("GiaGioDau"),
                        rs.getDouble("GiaGioTiepTheo"),
                        rs.getDouble("GiaCaNgay")
                };
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return new Object[]{0.0,0.0,0.0};
    }
    public String getDVPhieuDat(String maPhieu){
        return "Chưa phát sinh dịch vụ";
    }

}