package Dao;

import ConnectDB.Database;
import Entity.*;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class NhanVienDao {

    // ================= GET ALL =================
    public List<NhanVien> getAll() {

        List<NhanVien> list = new ArrayList<>();

        String sql = """
    SELECT nv.MaNV,
           nv.HoTen,
           nv.GioiTinh,
           nv.CCCD,
           nv.NgaySinh,
           nv.TrangThaiLamViec,
           nv.MaChucVu,
           cv.TenChucVu,
           nv.Luong,
           nv.Email,
           nv.SDT
    FROM NhanVien nv
    JOIN ChucVu cv
        ON nv.MaChucVu = cv.MaChucVu
""";
        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getInt("GioiTinh"));
                nv.setCccd(rs.getString("CCCD"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setTrangThaiLamViec(rs.getString("TrangThaiLamViec"));

                ChucVu cv = new ChucVu();

                cv.setMaChucVu(rs.getString("MaChucVu"));
                cv.setTenChucVu(rs.getString("TenChucVu"));

                nv.setChucVu(cv);

                nv.setLuong(rs.getDouble("Luong"));
                nv.setEmail(rs.getString("Email"));
                nv.setSdt(rs.getString("SDT"));

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= INSERT =================
// Khi thêm nhân viên -> tự tạo luôn tài khoản
// Tên đăng nhập = MaNV
// Mật khẩu = MaNV

    // ================= INSERT (CẬP NHẬT ĐỂ BÁO LỖI CHI TIẾT) =================
    public boolean insertNhanVien(NhanVien nv) throws Exception {

        String sqlNV = """
        INSERT INTO NhanVien
        (MaNV, HoTen, GioiTinh, CCCD, NgaySinh,
         TrangThaiLamViec, MaChucVu, Luong,
         AnhNhanVien, Email, SDT)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        String sqlTK = """
        INSERT INTO TaiKhoan
        (MaTK, TenDangNhap, MatKhau, MaNV, TrangThai)
        VALUES (?, ?, ?, ?, ?)
    """;

        Connection con = null;
        PreparedStatement psNV = null;
        PreparedStatement psTK = null;

        try {
            con = Database.getInstance().getConnection();
            con.setAutoCommit(false);

            // ================= INSERT NHÂN VIÊN =================
            psNV = con.prepareStatement(sqlNV);

            psNV.setString(1, nv.getMaNV());
            psNV.setString(2, nv.getHoTen());
            psNV.setInt(3, nv.getGioiTinh());
            psNV.setString(4, nv.getCccd());
            psNV.setDate(5, nv.getNgaySinh());
            psNV.setString(6, nv.getTrangThaiLamViec());
            psNV.setString(7, nv.getChucVu().getMaChucVu());
            psNV.setDouble(8, nv.getLuong());
            psNV.setString(9, nv.getAnhNhanVien());
            psNV.setString(10, nv.getEmail());
            psNV.setString(11, nv.getSdt());

            psNV.executeUpdate();

            // ================= TỰ TẠO TÀI KHOẢN =================
            String maTK = taoMaTKTuDong(con);

            psTK = con.prepareStatement(sqlTK);

            psTK.setString(1, maTK);
            psTK.setString(2, nv.getMaNV());
            psTK.setString(3, nv.getMaNV());
            psTK.setString(4, nv.getMaNV());
            psTK.setString(5, "Hoạt động");

            psTK.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            if (con != null && !con.isClosed()) {
                try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            // NÉM NGOẠI LỆ RA NGOÀI ĐỂ GUI BẮT ĐƯỢC THÔNG BÁO CHI TIẾT
            throw e;
        } finally {
            try {
                if (psNV != null) psNV.close();
                if (psTK != null) psTK.close();
                if (con != null && !con.isClosed()) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ================= UPDATE =================
// SỬA updateNhanVien() để khi sửa nhân viên
// thì bảng TaiKhoan cũng cập nhật Họ tên tương ứng
// CHỨC NĂNG SỬA THÔNG TIN NHÂN VIÊN
// cho phép sửa:
// - Họ tên
// - Giới tính
// - CCCD
// - Ngày sinh
// - Trạng thái làm việc
// - Chức vụ
// - Lương
// - Email
// - SĐT
// - Ảnh nhân viên

    // ================= UPDATE (ĐÃ SỬA LỖI KHÔNG ĐỔI CHỨC VỤ) =================
    public boolean updateNhanVien(NhanVien nv) {

        // Chuỗi SQL viết liền mạch để loại bỏ hoàn toàn lỗi ngắt dòng của Text Block cũ
        String sql = "UPDATE NhanVien SET HoTen = ?, GioiTinh = ?, CCCD = ?, NgaySinh = ?, " +
                "TrangThaiLamViec = ?, MaChucVu = ?, Luong = ?, AnhNhanVien = ?, Email = ?, SDT = ? " +
                "WHERE MaNV = ?";

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, nv.getHoTen());
            ps.setInt(2, nv.getGioiTinh());
            ps.setString(3, nv.getCccd());
            ps.setDate(4, nv.getNgaySinh());
            ps.setString(5, nv.getTrangThaiLamViec());

            // Lấy chính xác MaChucVu truyền vào vị trí tham số thứ 6 (?)
            if (nv.getChucVu() != null) {
                ps.setString(6, nv.getChucVu().getMaChucVu());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            ps.setDouble(7, nv.getLuong());
            ps.setString(8, nv.getAnhNhanVien());
            ps.setString(9, nv.getEmail());
            ps.setString(10, nv.getSdt());
            ps.setString(11, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public NhanVien getById(String maNV){

        String sql = """
    SELECT nv.*, cv.TenChucVu, cv.MoTa
    FROM NhanVien nv
    JOIN ChucVu cv ON nv.MaChucVu = cv.MaChucVu
    WHERE nv.MaNV=?
""";

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                ChucVu cv = new ChucVu(
                        rs.getString("MaChucVu"),
                        rs.getString("TenChucVu"),
                        rs.getString("MoTa")
                );

                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getInt("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));

                // SỬA TẠI ĐÂY: Lấy chuẩn chuỗi chữ hoa "CCCD" và "SDT" trực tiếp từ DB
                nv.setCccd(rs.getString("CCCD"));
                nv.setSdt(rs.getString("SDT"));

                nv.setTrangThaiLamViec(rs.getString("TrangThaiLamViec"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setEmail(rs.getString("Email"));
                nv.setAnhNhanVien(rs.getString("AnhNhanVien"));
                nv.setChucVu(cv);

                return nv;
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // ================= DELETE =================
// Xóa nhân viên -> xóa luôn tài khoản

    public boolean deleteNhanVien(String maNV) {

        String sqlTK = "DELETE FROM TaiKhoan WHERE MaNV = ?";
        String sqlNV = "DELETE FROM NhanVien WHERE MaNV = ?";

        try (
                Connection con = Database.getInstance().getConnection()
        ) {
            con.setAutoCommit(false);

            PreparedStatement psTK = con.prepareStatement(sqlTK);
            psTK.setString(1, maNV);
            psTK.executeUpdate();

            PreparedStatement psNV = con.prepareStatement(sqlNV);
            psNV.setString(1, maNV);

            int rows = psNV.executeUpdate();

            if (rows > 0) {
                con.commit();
                return true;
            }

            con.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    private String taoMaTKTuDong(Connection con) {

        String sql = """
        SELECT TOP 1 MaTK
        FROM TaiKhoan
        ORDER BY MaTK DESC
    """;

        try (
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                String lastMa = rs.getString("MaTK");
                int so = Integer.parseInt(lastMa.substring(2)) + 1;
                return String.format("TK%02d", so);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "TK01";
    }
    public NhanVien getNhanVienTheoMa(String maNV) {

        NhanVien nv = null;

        try {

            Connection con = Database.getInstance().getConnection();

            String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, maNV);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                nv = new NhanVien();

                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getInt("GioiTinh"));

                // SỬA TẠI ĐÂY: Đồng bộ viết HOA tên cột trong Database
                nv.setCccd(rs.getString("CCCD"));
                nv.setSdt(rs.getString("SDT"));

                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setAnhNhanVien(rs.getString("AnhNhanVien"));
                nv.setEmail(rs.getString("Email"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nv;
    }

    // ================= AUTO ID =================
    public String getNextMaNV(String chucVu) {

        // ================= X =================
        // X = 1 -> Quản lý
        // X = 0 -> Bán vé / Lễ tân

        String roleCode;

        if (chucVu.equalsIgnoreCase("Quản Lý")) {

            roleCode = "1";

        } else {

            roleCode = "0";
        }

        // ================= YY =================
        // 2 số cuối năm hiện tại

        int year =
                java.time.LocalDate.now()
                        .getYear() % 100;

        String yy =
                String.format("%02d", year);

        // ================= PREFIX =================
        // VD:
        // NV125
        // NV025

        String prefix =
                "NV" +
                        roleCode +
                        yy;

        // ================= SQL =================

        String sql = """
        SELECT TOP 1 MaNV
        FROM NhanVien
        WHERE MaNV LIKE ?
        ORDER BY
        CAST(SUBSTRING(MaNV, 6, LEN(MaNV)) AS INT) DESC
    """;

        try (

                Connection con =
                        Database.getInstance()
                                .getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)

        ) {

            ps.setString(1, prefix + "%");

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                String lastMa =
                        rs.getString("MaNV");

                // VD:
                // NV1250001
                // lấy: 0001

                int number =
                        Integer.parseInt(
                                lastMa.substring(5)
                        );

                number++;

                // giới hạn 9999

                if(number > 9999){

                    JOptionPane.showMessageDialog(
                            null,
                            "Đã vượt giới hạn mã nhân viên!"
                    );

                    return null;
                }

                return prefix +
                        String.format(
                                "%04d",
                                number
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // mặc định đầu tiên

        return prefix + "0001";
    }
    public List<NhanVien> searchNhanVien(String keyword) {

        List<NhanVien> list = new ArrayList<>();

        String sql = """
        SELECT
            nv.MaNV,
            nv.HoTen,
            nv.GioiTinh,
            nv.CCCD,
            nv.NgaySinh,
            nv.TrangThaiLamViec,
            nv.MaChucVu,
            cv.TenChucVu,
            nv.Luong,
            nv.Email,
            nv.SDT
        FROM NhanVien nv
        JOIN ChucVu cv
            ON nv.MaChucVu = cv.MaChucVu
        WHERE
            nv.MaNV LIKE ?
            OR nv.HoTen LIKE ?
            OR nv.SDT LIKE ?
            OR nv.CCCD LIKE ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                NhanVien nv = new NhanVien();

                nv.setMaNV(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getInt("GioiTinh"));
                nv.setCccd(rs.getString("CCCD"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setTrangThaiLamViec(
                        rs.getString("TrangThaiLamViec")
                );

                ChucVu cv = new ChucVu();
                cv.setMaChucVu(
                        rs.getString("MaChucVu")
                );
                cv.setTenChucVu(
                        rs.getString("TenChucVu")
                );

                nv.setChucVu(cv);
                nv.setLuong(rs.getDouble("Luong"));
                nv.setEmail(rs.getString("Email"));
                nv.setSdt(rs.getString("SDT"));

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}