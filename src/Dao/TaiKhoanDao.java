package Dao;

import ConnectDB.Database;
import Entity.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDao {

    // ================= GET ALL =================
    // Tên đăng nhập (MaNV)
    // Mật khẩu
    // Trạng thái
    // Chức vụ

    public List<TaiKhoan> getAll() {

        List<TaiKhoan> list = new ArrayList<>();

        String sql = """
        SELECT 
            tk.MaTK,
            tk.TenDangNhap,
            tk.MatKhau,
            tk.TrangThai,
            tk.MaNV,
            nv.MaChucVu
        FROM TaiKhoan tk
        INNER JOIN NhanVien nv
            ON tk.MaNV = nv.MaNV
        ORDER BY tk.MaTK DESC
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                TaiKhoan tk = new TaiKhoan();


                tk.setMaTK(rs.getString("MaTK"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setMaChucVu(rs.getString("MaChucVu"));

                list.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= UPDATE =================
    // chỉ sửa:
    // 1. mật khẩu
    // 2. trạng thái
    // 3. chức vụ

    // ================= UPDATE (ĐỒNG BỘ TRẠNG THÁI SANG BẢNG NHÂN VIÊN) =================
    public boolean updateOnlyAccount(TaiKhoan tk) {

        String sqlTK = """
            UPDATE TaiKhoan
            SET MatKhau = ?,
                TrangThai = ?
            WHERE TenDangNhap = ?
        """;

        // Thêm lệnh cập nhật trạng thái làm việc bên Nhân viên tương ứng
        String sqlNV = """
            UPDATE NhanVien
            SET MaChucVu = ?,
                TrangThaiLamViec = ?
            WHERE MaNV = ?
        """;

        Connection con = null;

        try {
            con = Database.getInstance().getConnection();
            con.setAutoCommit(false); // Kích hoạt Transaction an toàn dữ liệu

            // 1. Cập nhật bảng tài khoản
            PreparedStatement psTK = con.prepareStatement(sqlTK);
            psTK.setString(1, tk.getMatKhau());
            psTK.setString(2, tk.getTrangThai());
            psTK.setString(3, tk.getTenDangNhap());
            psTK.executeUpdate();

            // 2. Cập nhật chức vụ và tự động đồng bộ trạng thái làm việc sang nhân viên
            PreparedStatement psNV = con.prepareStatement(sqlNV);
            psNV.setString(1, tk.getMaChucVu());

            // Nếu tài khoản chọn trạng thái "Khóa" -> Nhân viên chuyển thành "Nghỉ"
            // Ngược lại nếu chọn "Hoạt động" -> Nhân viên chuyển thành "Đang làm"
            if (tk.getTrangThai() != null && tk.getTrangThai().equalsIgnoreCase("Khóa")) {
                psNV.setString(2, "Nghỉ");
            } else {
                psNV.setString(2, "Đang làm");
            }

            psNV.setString(3, tk.getMaNV());
            psNV.executeUpdate();

            con.commit(); // Xác nhận lưu lên cả 2 bảng dữ liệu thành công
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // ================= DELETE =================
    // xóa tài khoản -> xóa luôn nhân viên

    // Dùng CHUNG cho cả TaiKhoanUI và NhanVienUI
    // Xóa ở đâu cũng mất cả 2 bên

    public boolean delete(String maNV) {

        String sqlDeleteTK = """
        DELETE FROM TaiKhoan
        WHERE MaNV = ?
    """;

        String sqlDeleteNV = """
        DELETE FROM NhanVien
        WHERE MaNV = ?
    """;

        Connection con = null;
        PreparedStatement psTK = null;
        PreparedStatement psNV = null;

        try {
            con = Database.getInstance().getConnection();
            con.setAutoCommit(false);

        /*
         Dù bấm xóa ở:
         - Nhân viên UI
         - Tài khoản UI

         thì đều truyền MaNV vào đây
         và hệ thống sẽ xóa cả:
         - TaiKhoan
         - NhanVien
        */

            // luôn xóa tài khoản trước
            psTK = con.prepareStatement(sqlDeleteTK);
            psTK.setString(1, maNV);
            psTK.executeUpdate();

            // sau đó xóa nhân viên
            psNV = con.prepareStatement(sqlDeleteNV);
            psNV.setString(1, maNV);

            int rows = psNV.executeUpdate();

            if (rows > 0) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (Exception e) {

            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        } finally {

            try {
                if (psTK != null) psTK.close();
                if (psNV != null) psNV.close();
                if (con != null && !con.isClosed()) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // ================= TẠO MÃ TK TỰ ĐỘNG =================

    public String taoMaTKTuDong() {

        String sql = """
            SELECT TOP 1 MaTK
            FROM TaiKhoan
            ORDER BY MaTK DESC
        """;

        try (
                Connection con = Database.getInstance().getConnection();
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

    // ================= TẠO TÀI KHOẢN TỪ NHÂN VIÊN =================
    // Khi thêm nhân viên:
    // username = MaNV
    // password mặc định = MaNV

    public boolean createFromNhanVien(TaiKhoan tk) {

        String sql = """
            INSERT INTO TaiKhoan
            (MaTK, TenDangNhap, MatKhau, MaNV, TrangThai)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String maTK = taoMaTKTuDong();

            ps.setString(1, maTK);
            ps.setString(2, tk.getMaNV()); // username = MaNV
            ps.setString(3, tk.getMaNV()); // password mặc định = MaNV
            ps.setString(4, tk.getMaNV());
            ps.setString(5, "Hoạt động");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // ================= RESET PASSWORD =================
// Quên mật khẩu:
// nhập username + CCCD + mật khẩu mới

    public boolean resetPassword(String username, String cccd, String newPass) {

        String sql = """
        UPDATE TaiKhoan
        SET MatKhau = ?
        WHERE TenDangNhap = ?
          AND MaNV IN (
              SELECT MaNV
              FROM NhanVien
              WHERE CCCD = ?
          )
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, newPass);
            ps.setString(2, username);
            ps.setString(3, cccd);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // ================= LOGIN (CHẶN TUYỆT ĐỐI TÀI KHOẢN KHÓA/NHÂN VIÊN NGHỈ) =================
    public TaiKhoan login(String user, String pass) {

        // Bỏ điều kiện lọc TrangThai ở đây để câu lệnh SQL luôn tìm ra tài khoản nếu gõ đúng mật khẩu
        String sql = """
        SELECT
            tk.*,
            nv.HoTen,
            nv.MaChucVu
        FROM TaiKhoan tk
        JOIN NhanVien nv
            ON tk.MaNV = nv.MaNV
        WHERE tk.TenDangNhap = ?
          AND tk.MatKhau = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaNV(rs.getString("MaNV"));
                tk.setHoTen(rs.getString("HoTen"));
                tk.setMaChucVu(rs.getString("MaChucVu"));

                // Bắt buộc phải lấy cột này để đẩy ra ngoài giao diện kiểm tra
                tk.setTrangThai(rs.getString("TrangThai"));

                return tk;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
// ======================================
// SEARCH TÀI KHOẢN
// tìm theo username hoặc mã nhân viên
// ======================================

    public List<TaiKhoan> searchTaiKhoan(String keyword) {

        List<TaiKhoan> list = new ArrayList<>();

        String sql = """
        SELECT
            tk.MaTK,
            tk.TenDangNhap,
            tk.MatKhau,
            tk.TrangThai,
            tk.MaNV,
            nv.MaChucVu
        FROM TaiKhoan tk
        INNER JOIN NhanVien nv
            ON tk.MaNV = nv.MaNV
        WHERE
            tk.TenDangNhap LIKE ?
            OR tk.MaNV LIKE ?
        ORDER BY tk.MaTK DESC
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                TaiKhoan tk = new TaiKhoan();

                tk.setMaTK(rs.getString("MaTK"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setMaChucVu(rs.getString("MaChucVu"));

                list.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


// ======================================
// GET BY USERNAME
// ======================================

    public TaiKhoan getByUsername(String username) {

        String sql = """
        SELECT
            tk.MaTK,
            tk.TenDangNhap,
            tk.MatKhau,
            tk.TrangThai,
            tk.MaNV,
            nv.MaChucVu
        FROM TaiKhoan tk
        INNER JOIN NhanVien nv
            ON tk.MaNV = nv.MaNV
        WHERE tk.TenDangNhap = ?
    """;

        try (
                Connection con = Database.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                TaiKhoan tk = new TaiKhoan();

                tk.setMaTK(rs.getString("MaTK"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getString("TrangThai"));
                tk.setMaNV(rs.getString("MaNV"));
                tk.setMaChucVu(rs.getString("MaChucVu"));

                return tk;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}