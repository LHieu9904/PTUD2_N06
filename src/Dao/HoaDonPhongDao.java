package Dao;

import ConnectDB.Database;
import Entity.HoaDonPhong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.List;
import java.util.ArrayList;

public class HoaDonPhongDao {

    // TẠO HÓA ĐƠN PHÒNG

    public String createHoaDon(
            String maKH,
            String maPhieuDatPhong,
            String maNV
    ) {

        String maHD = getNextMaHD();

        String sql = """
            INSERT INTO HoaDonPhong
            (
                MaHoaDonPhong,
                NgayLapHoaDon,
                MaPhieuDatPhong,
                MaNV,
                MaKH,
                TrangThai
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            // Mã hóa đơn tự sinh
            ps.setString(1, maHD);

            // Ngày lập hóa đơn
            ps.setTimestamp(
                    2,
                    new java.sql.Timestamp(
                            System.currentTimeMillis()
                    )
            );

            // BẮT BUỘC vì UNIQUE
            ps.setString(
                    3,
                    maPhieuDatPhong
            );

            // Mã nhân viên đang đăng nhập
            ps.setString(
                    4,
                    maNV
            );

            // Mã khách hàng
            ps.setString(
                    5,
                    maKH
            );

            // Trạng thái
            ps.setString(
                    6,
                    "Chưa thanh toán"
            );

            ps.executeUpdate();

            return maHD;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // TỰ PHÁT SINH MÃ HÓA ĐƠN

    public String getNextMaHD() {

        String sql = """
            SELECT MAX(
                CAST(
                    SUBSTRING(
                        MaHoaDonPhong,
                        3,
                        LEN(MaHoaDonPhong)
                    ) AS INT
                )
            )
            FROM HoaDonPhong
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                int so = rs.getInt(1) + 1;

                return String.format(
                        "HD%03d",
                        so
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "HD001";
    }

    public List<Object[]> getAllHoaDon() {

        List<Object[]> list = new ArrayList<>();

        try {

            Connection con = Database.getInstance().getConnection();

            String sql =
                    "SELECT hd.MaHoaDonPhong, " +
                            "kh.HoTen, " +
                            "hd.TrangThai, " +
                            "hd.TongTien " +
                            "FROM HoaDonPhong hd " +
                            "LEFT JOIN KhachHang kh " +
                            "ON hd.MaKH = kh.MaKH " +
                            "ORDER BY hd.NgayLapHoaDon DESC";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Object[]{

                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Object[] getChiTietHoaDon(String maHD) {

        String sql = """
    SELECT

        hd.MaHoaDonPhong,
        hd.NgayLapHoaDon,

        nv.HoTen AS NhanVien,
        kh.HoTen AS KhachHang,
        ISNULL((
            SELECT SUM(

                CASE

                    WHEN cthd.CachThue = N'Ngày'
                    THEN lp.GiaCaNgay

                    ELSE

                        lp.GiaGioDau +

                        CASE
                            WHEN DATEDIFF(
                                    HOUR,
                                    cthd.ThoiGianNhan,
                                    cthd.ThoiGianTra
                                 ) <= 1
                            THEN 0

                            ELSE
                                (
                                    DATEDIFF(
                                        HOUR,
                                        cthd.ThoiGianNhan,
                                        cthd.ThoiGianTra
                                    ) - 1
                                ) * lp.GiaGioTiepTheo
                        END
                END

            )

            FROM ChiTietHoaDonPhong cthd

            JOIN Phong p
                ON cthd.MaPhong = p.MaPhong

            JOIN LoaiPhong lp
                ON p.MaLP = lp.MaLP

            WHERE cthd.MaHoaDonPhong =
                  hd.MaHoaDonPhong

        ),0) AS TienPhong,

        -- TIỀN DỊCH VỤ
        ISNULL((
            SELECT SUM(ct.ThanhTien)

            FROM ChiTietHoaDonDichVu ct

            JOIN HoaDonDichVu dv
                ON ct.MaHoaDonDichVu =
                   dv.MaHoaDonDichVu

            WHERE dv.MaHoaDonPhong =
                  hd.MaHoaDonPhong
        ),0) AS TienDV,

        -- THUẾ
        ISNULL(hd.TienThue,0) AS TienThue,

        -- TỔNG TIỀN
        ISNULL(
            (
                SELECT SUM(

                    CASE

                        WHEN cthd.CachThue = N'Ngày'
                        THEN lp.GiaCaNgay

                        ELSE

                            lp.GiaGioDau +

                            CASE
                                WHEN DATEDIFF(
                                        HOUR,
                                        cthd.ThoiGianNhan,
                                        cthd.ThoiGianTra
                                     ) <= 1
                                THEN 0

                                ELSE
                                    (
                                        DATEDIFF(
                                            HOUR,
                                            cthd.ThoiGianNhan,
                                            cthd.ThoiGianTra
                                        ) - 1
                                    ) * lp.GiaGioTiepTheo
                            END
                    END

                )

                FROM ChiTietHoaDonPhong cthd

                JOIN Phong p
                    ON cthd.MaPhong = p.MaPhong

                JOIN LoaiPhong lp
                    ON p.MaLP = lp.MaLP

                WHERE cthd.MaHoaDonPhong =
                      hd.MaHoaDonPhong
            )

            +

            ISNULL((
                SELECT SUM(ct.ThanhTien)

                FROM ChiTietHoaDonDichVu ct

                JOIN HoaDonDichVu dv
                    ON ct.MaHoaDonDichVu =
                       dv.MaHoaDonDichVu

                WHERE dv.MaHoaDonPhong =
                      hd.MaHoaDonPhong
            ),0)

            +

            ISNULL(hd.TienThue,0)

        ,0) AS TongTien

    FROM HoaDonPhong hd

    JOIN NhanVien nv
        ON hd.MaNV = nv.MaNV

    JOIN KhachHang kh
        ON hd.MaKH = kh.MaKH

    WHERE hd.MaHoaDonPhong = ?
""";

        try (

                Connection con =
                        Database.getInstance()
                                .getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setString(1, maHD);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                return new Object[]{

                        rs.getString(1),
                        rs.getTimestamp(2),

                        rs.getString(3),
                        rs.getString(4),

                        rs.getBigDecimal(5), // tiền phòng
                        rs.getBigDecimal(6), // DV
                        rs.getBigDecimal(7), // thuế
                        rs.getBigDecimal(8)  // tổng
                };
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean insertCoPhieuDatPhong(
            String maHoaDon,
            String maNV,
            String maKH,
            String maPhieuDatPhong
    ) {

        String sql = """
        INSERT INTO HoaDonPhong
        (
            MaHoaDonPhong,
            NgayLapHoaDon,
            MaPhieuDatPhong,
            MaNV,
            MaKH,
            TrangThai,
            TongTien,
            TienThue
        )
        VALUES
        (
            ?,
            GETDATE(),
            ?,
            ?,
            ?,
            ?,
            0,
            0
        )
    """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maHoaDon);
            ps.setString(2, maPhieuDatPhong);
            ps.setString(3, maNV);
            ps.setString(4, maKH);
            ps.setString(5, "Chưa thanh toán");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Object[] getThongTinThanhToan(String maPhong) {

        String sql = """
        SELECT TOP 1
            ct.ThoiGianNhan,
            ct.ThoiGianTra,
            hd.TienThue,
            ISNULL(hd.TienDichVu,0) AS TienDV
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd
            ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        WHERE ct.MaPhong = ?
        ORDER BY ct.ThoiGianNhan DESC
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
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra"),
                        rs.getDouble("TienThue"),
                        rs.getDouble("TienDV")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean capNhatThanhToan(String maPhong) {

        String sql = """
        UPDATE HoaDonPhong
        SET TrangThai = N'Đã thanh toán'
        WHERE MaHoaDonPhong = (
            SELECT TOP 1 MaHoaDonPhong
            FROM ChiTietHoaDonPhong
            WHERE MaPhong = ?
            ORDER BY ThoiGianNhan DESC
        )
    """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String getMaHDByPhong(String maPhong) {

        String sql = """
        SELECT TOP 1 hd.MaHoaDonPhong
        FROM HoaDonPhong hd
        JOIN ChiTietHoaDonPhong ct
            ON hd.MaHoaDonPhong = ct.MaHoaDonPhong
        WHERE ct.MaPhong = ?
        ORDER BY ct.ThoiGianNhan DESC
    """;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("MaHoaDonPhong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean thanhToanHoaDon(
            String maPhong,
            String maNV, // <--- THÊM THAM SỐ NÀY
            String maKhuyenMai,
            String maThue,
            double tienThue,
            double tongTien
    ) {
        Connection con = null;
        try {
            con = Database.getInstance().getConnection();
            con.setAutoCommit(false);

            // 1. LẤY HÓA ĐƠN (Giữ nguyên)
            String maHoaDon = null;
            String sqlGetHD = "SELECT TOP 1 MaHoaDonPhong FROM ChiTietHoaDonPhong WHERE MaPhong = ? ORDER BY ThoiGianNhan DESC";
            try (PreparedStatement ps = con.prepareStatement(sqlGetHD)) {
                ps.setString(1, maPhong);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) maHoaDon = rs.getString(1);
            }

            if (maHoaDon == null) { con.rollback(); return false; }

            // 2. UPDATE HÓA ĐƠN (CẬP NHẬT THÊM MA_NV)
            String sqlUpdateHD = """
            UPDATE HoaDonPhong
            SET
                MaKhuyenMai = ?,
                MaThue = ?,
                TienThue = ?,
                TongTien = ?,
                MaNV = ?,             -- <--- GÁN NHÂN VIÊN ĐANG ĐĂNG NHẬP
                TrangThai = N'Đã thanh toán'
            WHERE MaHoaDonPhong = ?
        """;

            try (PreparedStatement ps = con.prepareStatement(sqlUpdateHD)) {
                ps.setString(1, maKhuyenMai);
                ps.setString(2, maThue);
                ps.setDouble(3, tienThue);
                ps.setDouble(4, tongTien);
                ps.setString(5, maNV);       // <--- GÁN MA_NV VÀO ĐÂY
                ps.setString(6, maHoaDon);   // <--- MA_HD Ở VỊ TRÍ THỨ 6

                if (ps.executeUpdate() <= 0) { con.rollback(); return false; }
            }

            // 3 & 4. UPDATE CHI TIẾT & PHÒNG (Giữ nguyên)
            // ... (phần code update phòng và chi tiết giữ nguyên như cũ)

            con.commit();
            return true;
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
        return false;
    }
    public String getDanhSachDichVu(String maHD) {

        StringBuilder sb = new StringBuilder();

        try {
            Connection con = Database.getInstance().getConnection();

            String sql =
                    "SELECT dv.TenDichVu, ct.SoLuong " +
                            "FROM ChiTietHoaDonDichVu ct " +
                            "JOIN HoaDonDichVu hdv " +
                            "ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu " +
                            "JOIN DichVu dv " +
                            "ON ct.MaDichVu = dv.MaDichVu " +
                            "WHERE hdv.MaHoaDonPhong = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, maHD);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                sb.append(rs.getString(1))
                        .append(" x")
                        .append(rs.getInt(2))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String getDanhSachPhong(String maHD) {

        StringBuilder sb = new StringBuilder();

        try {

            Connection con = Database.getInstance().getConnection();

            String sql =
                    "SELECT MaPhong " +
                            "FROM ChiTietHoaDonPhong " +
                            "WHERE MaHoaDonPhong = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, maHD);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                sb.append(rs.getString(1))
                        .append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
    public boolean updateTienPhong(String maPhong){

        String sql = """
        UPDATE hd
        SET TongTien =
            CASE 
                WHEN DATEDIFF(HOUR, ct.ThoiGianNhan, ct.ThoiGianTra) < 24
                THEN lp.GiaGioDau +
                     (DATEDIFF(HOUR, ct.ThoiGianNhan, ct.ThoiGianTra)-1)
                     * lp.GiaGioTiepTheo
                ELSE lp.GiaCaNgay
            END
        FROM HoaDonPhong hd
        JOIN ChiTietHoaDonPhong ct 
            ON hd.MaHoaDonPhong = ct.MaHoaDonPhong
        JOIN Phong p 
            ON ct.MaPhong = p.MaPhong
        JOIN LoaiPhong lp 
            ON p.MaLP = lp.MaLP
        WHERE ct.MaPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public boolean applyKhuyenMai(String maPhong){

        String sql = """
        UPDATE hd
        SET TongTien = TongTien - 
            (TongTien * km.PhanTramGiam / 100)
        FROM HoaDonPhong hd
        JOIN ChiTietHoaDonPhong ct
            ON hd.MaHoaDonPhong = ct.MaHoaDonPhong
        JOIN KhuyenMai km
            ON hd.MaKhuyenMai = km.MaKhuyenMai
        WHERE ct.MaPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    // ================= DANH SÁCH PHIẾU =================
    public List<Object[]> getAllPhieuDat(){

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT p.MaPhieuDatPhong,
               kh.HoTen,
               CONVERT(VARCHAR, p.ThoiGianDat, 103) AS NgayDat,
               0 AS TongTien
        FROM PhieuDatPhong p
        JOIN KhachHang kh ON p.MaKH = kh.MaKH
        ORDER BY p.ThoiGianDat DESC
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){

            while(rs.next()){
                list.add(new Object[]{
                        rs.getString("MaPhieuDatPhong"),
                        rs.getString("HoTen"),
                        rs.getString("NgayDat"),
                        rs.getDouble("TongTien")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public Object[] getChiTietPhieuDat(String maPhieu){

        String sql = """
        SELECT p.MaPhieuDatPhong,
               CONVERT(VARCHAR, p.ThoiGianDat, 120),
               nv.HoTen,
               kh.HoTen
        FROM PhieuDatPhong p
        JOIN NhanVien nv ON p.MaNV = nv.MaNV
        JOIN KhachHang kh ON p.MaKH = kh.MaKH
        WHERE p.MaPhieuDatPhong = ?
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, maPhieu);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3), // NV
                        rs.getString(4)  // KH
                };
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public String getPhongPhieuDat(String maPhieu){

        StringBuilder sb = new StringBuilder();

        String sql = """
        SELECT MaPhong, ThoiGianNhan, ThoiGianTra
        FROM ChiTietPhieuDatPhong
        WHERE MaPhieuDatPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhieu);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                sb.append("- ")
                        .append(rs.getString(1))
                        .append(" (")
                        .append(rs.getTimestamp(2))
                        .append(" → ")
                        .append(rs.getTimestamp(3))
                        .append(")\n");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }
    public String getDVPhieuDat(String maPhieu){
        return "Chưa phát sinh dịch vụ";
    }
    public String getMaHDByMaPhieu(String maPhieu){

        String sql = """
        SELECT MaHoaDonPhong
        FROM HoaDonPhong
        WHERE MaPhieuDatPhong = ?
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ){

            ps.setString(1, maPhieu);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return rs.getString("MaHoaDonPhong");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public List<Object[]> getPhongDangThue(){

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT 
            p.MaPhong,
            kh.HoTen,
            kh.SDT,
            cthd.ThoiGianNhan,
            cthd.ThoiGianTra
        FROM ChiTietHoaDonPhong cthd
        JOIN HoaDonPhong hd ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong
        JOIN KhachHang kh ON hd.MaKH = kh.MaKH
        JOIN Phong p ON cthd.MaPhong = p.MaPhong
        WHERE cthd.TrangThaiSuDung = N'Đang dùng'
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){

            while(rs.next()){
                list.add(new Object[]{
                        rs.getString("MaPhong"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public List<Object[]> getAllDangThue(){

        List<Object[]> list = new ArrayList<>();

        String sql = """
        SELECT 
            p.MaPhong, lp.TenLP, kh.HoTen, kh.SDT, cthd.ThoiGianNhan, cthd.ThoiGianTra
        FROM ChiTietHoaDonPhong cthd
        JOIN HoaDonPhong hd ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong
        JOIN KhachHang kh ON hd.MaKH = kh.MaKH
        JOIN Phong p ON cthd.MaPhong = p.MaPhong
        JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
        WHERE cthd.TrangThaiSuDung = N'Đang dùng'
          AND hd.TrangThai = N'Chưa thanh toán' -- THÊM DÒNG NÀY VÀO
    """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){

            while(rs.next()){
                list.add(new Object[]{
                        rs.getString("MaPhong"),
                        rs.getString("TenLP"),
                        rs.getString("HoTen"),
                        rs.getString("SDT"),
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public boolean insert(
            String maHD,
    String maNV,
    String maKH
    ) {

        String sql = """
        INSERT INTO HoaDonPhong
        (
            MaHoaDonPhong,
            MaNV,
            MaKH,
            NgayLapHoaDon,
            TrangThai,
            TongTien,
            TienThue
        )
        VALUES (?, ?, ?, GETDATE(),
                N'Chưa thanh toán', 0, 0)
    """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maHD);
            ps.setString(2, maNV);
            ps.setString(3, maKH);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean exists(String maHD){

        String sql = """
        SELECT 1
        FROM HoaDonPhong
        WHERE MaHoaDonPhong = ?
    """;

        try(
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ){

            ps.setString(1, maHD);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    public List<Object[]> timHoaDonTheoNgay(String ngay) {

        List<Object[]> list = new ArrayList<>();

        try {
            Connection con = Database.getInstance().getConnection();

            String sql =
                    "SELECT hd.MaHoaDonPhong, " +
                            "kh.HoTen, " +
                            "hd.TrangThai, " +
                            "hd.TongTien " +
                            "FROM HoaDonPhong hd " +
                            "LEFT JOIN KhachHang kh " +
                            "ON hd.MaKH = kh.MaKH " +
                            "WHERE CONVERT(VARCHAR, hd.NgayLapHoaDon, 103) = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, ngay);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Object[]{

                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // =========================================================================
    // 1. TÌM PHÒNG ĐƯỢC SỬ DỤNG NHIỀU NHẤT TRONG KHOẢNG THỜI GIAN
    // =========================================================================
    public Object[] getPhongSuDungNhieuNhat(java.util.Date tuNgay, java.util.Date denNgay) {
        String sql = """
            SELECT TOP 1 cthd.MaPhong, COUNT(*) AS SoLuot
            FROM ChiTietHoaDonPhong cthd
            JOIN HoaDonPhong hd ON cthd.MaHoaDonPhong = hd.MaHoaDonPhong
            WHERE hd.NgayLapHoaDon BETWEEN ? AND ?
            GROUP BY cthd.MaPhong
            ORDER BY SoLuot DESC
        """;
        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{ rs.getString("MaPhong"), rs.getInt("SoLuot") };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{ "Không có", 0 };
    }

    // =========================================================================
    // 2. TÌM LOẠI PHÒNG ĐƯỢC ĐẶT NHIỀU NHẤT TRONG KHOẢNG THỜI GIAN
    // =========================================================================
    public Object[] getLoaiPhongDatNhieuNhat(java.util.Date tuNgay, java.util.Date denNgay) {
        String sql = """
            SELECT TOP 1 lp.TenLP, COUNT(*) AS SoLuot
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN PhieuDatPhong pdp ON ctpdp.MaPhieuDatPhong = pdp.MaPhieuDatPhong
            JOIN Phong p ON ctpdp.MaPhong = p.MaPhong
            JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
            WHERE pdp.ThoiGianDat BETWEEN ? AND ?
            GROUP BY lp.TenLP
            ORDER BY SoLuot DESC
        """;
        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{ rs.getString("TenLP"), rs.getInt("SoLuot") };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{ "Không có", 0 };
    }

    // =========================================================================
    // 3. LẤY DATASET SỐ LƯỢT ĐẶT CỦA TẤT CẢ LOẠI PHÒNG ĐỂ ĐỔ LÊN BIỂU ĐỒ CỘT
    // =========================================================================
    public java.util.Map<String, Integer> getDatasetLoaiPhong(java.util.Date tuNgay, java.util.Date denNgay) {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = """
            SELECT lp.TenLP, COUNT(ctpdp.MaPhong) AS SoLuot
            FROM LoaiPhong lp
            LEFT JOIN Phong p ON lp.MaLP = p.MaLP
            LEFT JOIN ChiTietPhieuDatPhong ctpdp ON p.MaPhong = ctpdp.MaPhong
            LEFT JOIN PhieuDatPhong pdp ON ctpdp.MaPhieuDatPhong = pdp.MaPhieuDatPhong 
                 AND pdp.ThoiGianDat BETWEEN ? AND ?
            GROUP BY lp.TenLP
            ORDER BY lp.TenLP
        """;
        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("TenLP"), rs.getInt("SoLuot"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    // =========================================================================
    // THỐNG KÊ DOANH THU CỦA TỪNG NHÂN VIÊN TRONG KHOẢNG THỜI GIAN
    // =========================================================================
    public List<Object[]> getDoanhThuNhanVienTheoThoiGian(java.util.Date tuNgay, java.util.Date denNgay) {
        List<Object[]> list = new ArrayList<>();

        // Truy vấn lấy Mã NV, Họ Tên, Số hóa đơn đã lập và Tổng doanh thu mang về
        String sql = """
            SELECT 
                nv.MaNV,
                nv.HoTen,
                COUNT(hd.MaHoaDonPhong) AS SoHoaDon,
                ISNULL(SUM(hd.TongTien), 0) AS TongDoanhThu
            FROM NhanVien nv
            LEFT JOIN HoaDonPhong hd ON nv.MaNV = hd.MaNV 
                 AND hd.TrangThai = N'Đã thanh toán'
                 AND hd.NgayLapHoaDon BETWEEN ? AND ?
            GROUP BY nv.MaNV, nv.HoTen
            ORDER BY TongDoanhThu DESC
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            // Chuyển đổi sang java.sql.Timestamp để giữ độ chính xác giờ phút giây trong DB
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[] {
                            rs.getString("MaNV"),
                            rs.getString("HoTen"),
                            rs.getInt("SoHoaDon"),
                            rs.getBigDecimal("TongDoanhThu") // Trả về dạng tiền tệ chính xác
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // =========================================================================
// ĐÃ ĐỒNG BỘ HOÀN TOÀN THEO CẤU TRÚC SCRIPT DATABASE THỰC TẾ CỦA BẠN
// =========================================================================

    public Object[] getPhongDoanhThuCaoNhat(java.util.Date tuNgay, java.util.Date denNgay) {
        String sql = """
        SELECT TOP 1 ct.MaPhong, SUM(hd.TongTien) AS DoanhThu
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        -- Sử dụng cột chuẩn NgayLapHoaDon từ cấu trúc bảng của bạn
        WHERE hd.TrangThai = N'Đã thanh toán' 
          AND hd.NgayLapHoaDon BETWEEN ? AND ?
        GROUP BY ct.MaPhong 
        ORDER BY DoanhThu DESC""";
        try (java.sql.Connection con = Database.getInstance().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{rs.getString("MaPhong"), rs.getDouble("DoanhThu")};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{"Chưa có", 0.0};
    }

    public Object[] getLoaiPhongDoanhThuCaoNhat(java.util.Date tuNgay, java.util.Date denNgay) {
        String sql = """
        SELECT TOP 1 lp.TenLP, SUM(hd.TongTien) AS DoanhThu
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        JOIN Phong p ON ct.MaPhong = p.MaPhong
        JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
        -- Sử dụng cột chuẩn NgayLapHoaDon từ cấu trúc bảng của bạn
        WHERE hd.TrangThai = N'Đã thanh toán' 
          AND hd.NgayLapHoaDon BETWEEN ? AND ?
        GROUP BY lp.TenLP 
        ORDER BY DoanhThu DESC""";
        try (java.sql.Connection con = Database.getInstance().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Object[]{rs.getString("TenLP"), rs.getDouble("DoanhThu")};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{"Chưa có", 0.0};
    }

    public java.util.Map<String, Integer> getDatasetDoanhThuLoaiPhong(java.util.Date tuNgay, java.util.Date denNgay) {
        java.util.Map<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = """
        SELECT lp.TenLP, CAST(SUM(hd.TongTien) AS INT) AS DoanhThu
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        JOIN Phong p ON ct.MaPhong = p.MaPhong
        JOIN LoaiPhong lp ON p.MaLP = lp.MaLP
        -- Sử dụng cột chuẩn NgayLapHoaDon từ cấu trúc bảng của bạn
        WHERE hd.TrangThai = N'Đã thanh toán' 
          AND hd.NgayLapHoaDon BETWEEN ? AND ?
        GROUP BY lp.TenLP""";
        try (java.sql.Connection con = Database.getInstance().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("TenLP"), rs.getInt("DoanhThu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public java.util.List<Object[]> getChiTietTienTungPhong(java.util.Date tuNgay, java.util.Date denNgay) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();

        // Câu lệnh SQL tinh gọn: Chỉ lấy Mã phòng, Số lượt ở, và Tổng tiền
        String sql = """
        SELECT 
            ct.MaPhong,
            COUNT(ct.MaHoaDonPhong) AS LuotO,
            SUM(hd.TongTien) AS TongDoanhThu
        FROM ChiTietHoaDonPhong ct
        JOIN HoaDonPhong hd ON ct.MaHoaDonPhong = hd.MaHoaDonPhong
        WHERE hd.TrangThai = N'Đã thanh toán' 
          AND hd.NgayLapHoaDon BETWEEN ? AND ?
        GROUP BY ct.MaPhong
        """;

        try (java.sql.Connection con = Database.getInstance().getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("MaPhong"),
                        rs.getInt("LuotO"),
                        rs.getDouble("TongDoanhThu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Object[]> timHoaDonTheoThang(String thangNam) {
        List<Object[]> list = new java.util.ArrayList<>();
        // SQL Server sử dụng FORMAT(NgayLap, 'MM/yyyy'), thay NgayLap bằng tên cột thực tế của bạn
        String sql = "SELECT maHD, tenKhachHang, trangThai, tongTien " +
                "FROM HoaDon WHERE FORMAT(NgayLap, 'MM/yyyy') = ?";

        try (java.sql.Connection con = Database.getInstance().getConnection(); // Thay bằng cách lấy Connection của bạn
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, thangNam);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDouble(4)
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String taoMaHoaDonTuDong() {
        return getNextMaHD();
    }
}

