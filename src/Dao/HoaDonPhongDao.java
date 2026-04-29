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
    public Object[] getChiTietHoaDon(String maHD) {

        String sql = """
        SELECT
            hd.MaHoaDonPhong,
            hd.NgayLapHoaDon,
            nv.HoTen,
            kh.HoTen,
            hd.TongTien,
            hd.TienThue,
            hd.TongTien
        FROM HoaDonPhong hd
        JOIN NhanVien nv
            ON hd.MaNV = nv.MaNV
        JOIN KhachHang kh
            ON hd.MaKH = kh.MaKH
        WHERE hd.MaHoaDonPhong = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maHD);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Object[]{
                        rs.getString(1),
                        rs.getTimestamp(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7)
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean insert(
            String maHoaDon,
            String maNV,
            String maKH
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
        VALUES (?, GETDATE(), NULL, ?, ?, ?, 0, 0)
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, maHoaDon);
            ps.setString(2, maNV);
            ps.setString(3, maKH);
            ps.setString(4, "Chưa thanh toán");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    public String taoMaHoaDonTuDong() {
        return getNextMaHD();
    }

}
