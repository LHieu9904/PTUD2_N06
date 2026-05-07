package Dao;

import ConnectDB.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThongKeDao {

    // =====================================================
    // TỔNG DOANH THU
    // =====================================================

    public double getTongDoanhThu(
            int thang,
            int nam
    ) {

        return getDoanhThuPhong(thang, nam)
                + getDoanhThuDichVu(thang, nam);
    }

    // =====================================================
    // DOANH THU PHÒNG
    // =====================================================

    public double getDoanhThuPhong(
            int thang,
            int nam
    ) {

        String sql = """
            SELECT ISNULL(SUM(TongTien),0) AS Tong

            FROM HoaDonPhong

            WHERE MONTH(NgayLapHoaDon) = ?
              AND YEAR(NgayLapHoaDon) = ?
              AND TrangThai = N'Đã thanh toán'
        """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getDouble("Tong");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // DOANH THU DỊCH VỤ
    // =====================================================

    public double getDoanhThuDichVu(
            int thang,
            int nam
    ) {

        String sql = """
            SELECT ISNULL(SUM(ct.ThanhTien),0) AS Tong

            FROM ChiTietHoaDonDichVu ct

            JOIN HoaDonDichVu hdv
                ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu

            JOIN HoaDonPhong hdp
                ON hdv.MaHoaDonPhong = hdp.MaHoaDonPhong

            WHERE MONTH(hdp.NgayLapHoaDon) = ?
              AND YEAR(hdp.NgayLapHoaDon) = ?
        """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getDouble("Tong");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // DOANH THU THEO NGÀY
    // =====================================================

    public Map<Integer, Double> getDoanhThuTheoNgay(
            int thang,
            int nam
    ) {

        Map<Integer, Double> map =
                new LinkedHashMap<>();

        String sql = """
        SELECT

            DAY(hd.NgayLapHoaDon) AS Ngay,

            ISNULL(
                SUM(
                    ISNULL(hd.TongTien,0)
                ),0
            )

            +

            ISNULL(
                SUM(
                    ISNULL(ctdv.ThanhTien,0)
                ),0
            ) AS Tong

        FROM HoaDonPhong hd

        LEFT JOIN HoaDonDichVu hddv
            ON hd.MaHoaDonPhong = hddv.MaHoaDonPhong

        LEFT JOIN ChiTietHoaDonDichVu ctdv
            ON hddv.MaHoaDonDichVu = ctdv.MaHoaDonDichVu

        WHERE MONTH(hd.NgayLapHoaDon) = ?
          AND YEAR(hd.NgayLapHoaDon) = ?
          AND hd.TrangThai = N'Đã thanh toán'

        GROUP BY DAY(hd.NgayLapHoaDon)

        ORDER BY DAY(hd.NgayLapHoaDon)
    """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                map.put(

                        rs.getInt("Ngay"),

                        rs.getDouble("Tong")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return map;
    }

    // =====================================================
    // SỐ PHÒNG ĐANG THUÊ
    // =====================================================

    public int getSoPhongDangThue() {

        String sql = """
            SELECT COUNT(*) AS Tong
            FROM Phong
            WHERE TrangThai = N'Đang thuê'
        """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("Tong");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // SỐ PHÒNG TRỐNG
    // =====================================================

    public int getSoPhongTrong() {

        String sql = """
            SELECT COUNT(*) AS Tong
            FROM Phong
            WHERE TrangThai = N'Trống'
        """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("Tong");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // SỐ PHÒNG ĐÃ ĐẶT
    // =====================================================

    public int getSoPhongDaDat() {

        String sql = """
            SELECT COUNT(*) AS Tong
            FROM Phong
            WHERE TrangThai = N'Đã đặt'
        """;

        try (

                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("Tong");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }
}