package Dao;

import ConnectDB.Database;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PhieuDatPhongDao {



    public String getNextMaPhieu() {

        String sql = """
        SELECT MAX(
            CAST(SUBSTRING(MaPhieuDatPhong, 3, LEN(MaPhieuDatPhong)) AS INT)
        )
        FROM PhieuDatPhong
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                int so = rs.getInt(1) + 1;

                return String.format("DP%03d", so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "DP001";
    }

    public String insert(String maKH, String maNV) {

        String ma = getNextMaPhieu();

        String sql = """
        INSERT INTO PhieuDatPhong
        (
            MaPhieuDatPhong,
            ThoiGianDat,
            MaKH,
            MaNV
        )
        VALUES (?, ?, ?, ?)
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, ma);

            ps.setTimestamp(
                    2,
                    new java.sql.Timestamp(
                            System.currentTimeMillis()
                    )
            );

            ps.setString(3, maKH);

            // phải là NV01 / NV02 / NV03 thật
            ps.setString(4, maNV);

            ps.executeUpdate();

            return ma;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Object[]> getAllDatPhong(){

        List<Object[]> list = new ArrayList<>();

        // SQL TỐI ƯU CAO CẤP: Sử dụng ROW_NUMBER() để bóc tách chính xác 100% dòng đặt phòng mới nhất, lấy đúng số lượng người tương ứng
        String sql = """
            WITH PhieuMoiNhat AS (
                SELECT 
                    MaPhong,
                    MaPhieuDatPhong,
                    SoLuong,
                    ROW_NUMBER() OVER (PARTITION BY MaPhong ORDER BY MaPhieuDatPhong DESC) AS RowNum
                FROM ChiTietPhieuDatPhong
            )
            SELECT 
                p.MaPhong,
                lp.TenLP,
                kh.HoTen,
                kh.SDT,
                pmn.SoLuong -- Lấy chính xác số lượng người đi kèm của lượt đặt mới nhất

            FROM Phong p
            JOIN PhieuMoiNhat pmn 
                ON p.MaPhong = pmn.MaPhong AND pmn.RowNum = 1 -- Chỉ lấy lượt đặt mới nhất của phòng đó
            JOIN PhieuDatPhong pdp 
                ON pmn.MaPhieuDatPhong = pdp.MaPhieuDatPhong
            JOIN KhachHang kh 
                ON pdp.MaKH = kh.MaKH
            JOIN LoaiPhong lp 
                ON p.MaLP = lp.MaLP

            WHERE p.TrangThai = N'Đã đặt'
              AND NOT EXISTS (
                    SELECT 1
                    FROM ChiTietHoaDonPhong cthd
                    WHERE cthd.MaPhong = p.MaPhong
                      AND cthd.TrangThaiSuDung = N'Đang dùng'
              )
        """;

        try(
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){

            while(rs.next()){
                list.add(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        // Lấy chính xác số lượng từ DB, nếu có lỗi null thì mặc định là "1"
                        rs.getObject(5) != null ? rs.getObject(5).toString() : "1"
                });
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public String getMaPhieuDatPhongByMaPhong(String maPhong) {

        String sql = """
        SELECT TOP 1 MaPhieuDatPhong
        FROM ChiTietPhieuDatPhong
        WHERE MaPhong = ?
        ORDER BY ThoiGianNhan DESC
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("MaPhieuDatPhong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public Object[] getThongTinNhanPhong(String maPhong) {

        // 🌟 CẬP NHẬT SQL: Lấy thêm cột SoLuong từ bảng chi tiết để đồng bộ sang giao diện
        String sql = """
        SELECT TOP 1
            ct.ThoiGianNhan,
            ct.ThoiGianTra,
            ct.SoLuong -- Bổ sung thêm cột này để không bị thiếu dữ liệu số lượng
        FROM ChiTietPhieuDatPhong ct
        WHERE ct.MaPhong = ?
        ORDER BY ct.ThoiGianNhan DESC
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maPhong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Trả về mảng Object gồm 3 phần tử để giao diện nhận diện được info[2]
                return new Object[]{
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra"),
                        rs.getObject("SoLuong") // Nạp số lượng người vào index số 2
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}