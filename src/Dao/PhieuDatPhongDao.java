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

        List<Object[]> list =
                new ArrayList<>();

        String sql = """
        SELECT DISTINCT

            p.MaPhong,
            lp.TenLP,
            kh.HoTen,
            kh.SDT

        FROM ChiTietPhieuDatPhong ctp

        JOIN PhieuDatPhong pdp
            ON ctp.MaPhieuDatPhong =
               pdp.MaPhieuDatPhong

        JOIN KhachHang kh
            ON pdp.MaKH = kh.MaKH

        JOIN Phong p
            ON ctp.MaPhong = p.MaPhong

        JOIN LoaiPhong lp
            ON p.MaLP = lp.MaLP

        WHERE p.TrangThai = N'Đã đặt'

        AND NOT EXISTS(

            SELECT 1
            FROM ChiTietHoaDonPhong cthd

            WHERE cthd.MaPhong = p.MaPhong
            AND cthd.TrangThaiSuDung =
                N'Đang dùng'
        )
    """;

        try(

                Connection con =
                        Database.getInstance()
                                .getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()

        ){

            while(rs.next()){

                list.add(new Object[]{

                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                });
            }

        }catch(Exception e){

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

        String sql = """
        SELECT TOP 1
            ct.ThoiGianNhan,
            ct.ThoiGianTra
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
                return new Object[]{
                        rs.getTimestamp("ThoiGianNhan"),
                        rs.getTimestamp("ThoiGianTra")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String taoPhieuDatPhongTuDong(
            String maKH,
            String maNV
    ) {

        String maPDP = getNextMaPhieu();

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
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maPDP);

            ps.setTimestamp(
                    2,
                    new Timestamp(
                            System.currentTimeMillis()
                    )
            );

            ps.setString(3, maKH);
            ps.setString(4, maNV);

            ps.executeUpdate();

            return maPDP;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}