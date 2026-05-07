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

        String sql = """
        SELECT
            hd.MaHoaDonPhong,
            kh.HoTen,
            hd.TrangThai,
            hd.TongTien
        FROM HoaDonPhong hd
        JOIN KhachHang kh
            ON hd.MaKH = kh.MaKH
        ORDER BY hd.MaHoaDonPhong DESC
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

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

    public Object[] getChiTietHoaDon(String maHD){

        String sql = """
        SELECT TOP 1
                                      hd.MaHoaDonPhong,
                                      hd.NgayLapHoaDon,
                                      nv.HoTen,
                                      kh.HoTen,
                                      ISNULL(
                                          hd.TongTien
                                          - hd.TienThue
                                          - ISNULL((
                                              SELECT SUM(ctdv.ThanhTien)
                                              FROM HoaDonDichVu hddv
                                              JOIN ChiTietHoaDonDichVu ctdv
                                                  ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
                                              WHERE hddv.MaHoaDonPhong = hd.MaHoaDonPhong
                                          ),0)
                                      ,0) AS TienPhong,
                                      ISNULL((
                                          SELECT SUM(ctdv.ThanhTien)
                                          FROM HoaDonDichVu hddv
                                          JOIN ChiTietHoaDonDichVu ctdv
                                              ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
                                          WHERE hddv.MaHoaDonPhong = hd.MaHoaDonPhong
                                      ),0) AS TienDichVu,
                                      ISNULL(hd.TienThue,0),
                                      ISNULL(hd.TongTien,0)
                                  
                                  FROM HoaDonPhong hd
                                  JOIN NhanVien nv ON hd.MaNV = nv.MaNV
                                  JOIN KhachHang kh ON hd.MaKH = kh.MaKH
                                  
                                  WHERE hd.MaHoaDonPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Object[]{
                        rs.getString(1),
                        rs.getTimestamp(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8)
                };
            }

        }catch(Exception e){
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
    // ===============================
// FILE: HoaDonPhongDao.java
// ===============================

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
            String maKhuyenMai,
            String maThue,
            double tienThue,
            double tongTien
    ) {

        Connection con = null;

        try {

            con = Database.getInstance().getConnection();
            con.setAutoCommit(false);

            // 1. LẤY HÓA ĐƠN
            String maHoaDon = null;

            String sqlGetHD = """
            SELECT TOP 1 MaHoaDonPhong
            FROM ChiTietHoaDonPhong
            WHERE MaPhong = ?
            ORDER BY ThoiGianNhan DESC
        """;

            try (PreparedStatement ps = con.prepareStatement(sqlGetHD)) {
                ps.setString(1, maPhong);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    maHoaDon = rs.getString(1);
                }
            }

            if (maHoaDon == null) {
                con.rollback();
                return false;
            }

            // 2. UPDATE HÓA ĐƠN
            String sqlUpdateHD = """
            UPDATE HoaDonPhong
            SET
                MaKhuyenMai = ?,
                MaThue = ?,
                TienThue = ?,
                TongTien = ?,
                TrangThai = N'Đã thanh toán'
            WHERE MaHoaDonPhong = ?
        """;

            try (PreparedStatement ps = con.prepareStatement(sqlUpdateHD)) {

                ps.setString(1, maKhuyenMai);
                ps.setString(2, maThue);
                ps.setDouble(3, tienThue);
                ps.setDouble(4, tongTien);
                ps.setString(5, maHoaDon);

                if (ps.executeUpdate() <= 0) {
                    con.rollback();
                    return false;
                }
            }

            // 3. UPDATE CHI TIẾT
            String sqlUpdateCT = """
            UPDATE ChiTietHoaDonPhong
            SET
                TrangThaiThanhToan = N'Đã TT',
                TrangThaiSuDung = N'Đã trả',
                ThoiGianTra = GETDATE()
            WHERE MaHoaDonPhong = ?
              AND MaPhong = ?
        """;

            try (PreparedStatement ps = con.prepareStatement(sqlUpdateCT)) {
                ps.setString(1, maHoaDon);
                ps.setString(2, maPhong);
                ps.executeUpdate();
            }

            // 4. UPDATE PHÒNG
            String sqlPhong = """
            UPDATE Phong
            SET TrangThai = N'Đang dọn dẹp'
            WHERE MaPhong = ?
        """;

            try (PreparedStatement ps = con.prepareStatement(sqlPhong)) {
                ps.setString(1, maPhong);
                ps.executeUpdate();
            }

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
    public String getDanhSachDichVu(String maHD){

        StringBuilder sb = new StringBuilder();

        String sql = """
        SELECT dv.TenDichVu, ctdv.SoLuong
        FROM HoaDonDichVu hddv
        JOIN ChiTietHoaDonDichVu ctdv
            ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu
        JOIN DichVu dv
            ON ctdv.MaDichVu = dv.MaDichVu
        WHERE hddv.MaHoaDonPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                sb.append(rs.getString(1))
                        .append(" x")
                        .append(rs.getInt(2))
                        .append("\n");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String getDanhSachPhong(String maHD){

        StringBuilder sb = new StringBuilder();

        String sql = """
        SELECT MaPhong
        FROM ChiTietHoaDonPhong
        WHERE MaHoaDonPhong = ?
    """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                sb.append(rs.getString(1)).append("\n");
            }

        }catch(Exception e){
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
            p.MaPhong,
            lp.TenLP,
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
        JOIN LoaiPhong lp 
            ON p.MaLP = lp.MaLP
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



    public String taoMaHoaDonTuDong() {
        return getNextMaHD();
    }
}

