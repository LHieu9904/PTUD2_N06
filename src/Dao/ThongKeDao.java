    package Dao;

    import ConnectDB.Database;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.util.ArrayList;
    import java.util.LinkedHashMap;
    import java.util.List;
    import java.util.Map;

    public class ThongKeDao {

        // =====================================================
        // TỔNG DOANH THU
        // =====================================================

        public double getTongDoanhThu(int thang, int nam) {
            String sql = """
            SELECT ISNULL(SUM(Tong), 0) FROM (
                SELECT ISNULL(SUM(TongTien), 0) AS Tong 
                FROM HoaDonPhong 
                WHERE MONTH(NgayLapHoaDon) = ? AND YEAR(NgayLapHoaDon) = ? AND TrangThai = N'Đã thanh toán'
                UNION ALL
                SELECT ISNULL(SUM(ct.ThanhTien), 0) AS Tong
                FROM ChiTietHoaDonDichVu ct
                JOIN HoaDonDichVu hdv ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu
                JOIN HoaDonPhong hdp ON hdv.MaHoaDonPhong = hdp.MaHoaDonPhong
                WHERE MONTH(hdp.NgayLapHoaDon) = ? AND YEAR(hdp.NgayLapHoaDon) = ? AND hdp.TrangThai = N'Đã thanh toán'
            ) AS DoanhThuTong
        """;

            try (Connection con = Database.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, thang); ps.setInt(2, nam);
                ps.setInt(3, thang); ps.setInt(4, nam);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getDouble(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
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
            SELECT ISNULL(SUM(ct.ThanhTien), 0) AS Tong
            FROM ChiTietHoaDonDichVu ct
            JOIN HoaDonDichVu hdv ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu
            JOIN HoaDonPhong hdp ON hdv.MaHoaDonPhong = hdp.MaHoaDonPhong
            WHERE MONTH(hdp.NgayLapHoaDon) = ?
              AND YEAR(hdp.NgayLapHoaDon) = ?
              AND hdp.TrangThai = N'Đã thanh toán' -- THÊM DÒNG NÀY ĐỂ ĐỒNG BỘ
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
        public List<Object[]> getDoanhThuNhanVienTheoThoiGian(java.util.Date tuNgay, java.util.Date denNgay) {
            List<Object[]> list = new ArrayList<>();

            // Sử dụng LEFT JOIN để lấy tất cả nhân viên (kể cả người chưa có hóa đơn)
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

            try (Connection con = Database.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                // Sử dụng setTimestamp để khớp hoàn toàn với kiểu DATETIME trong SQL Server
                ps.setTimestamp(1, new java.sql.Timestamp(tuNgay.getTime()));
                ps.setTimestamp(2, new java.sql.Timestamp(denNgay.getTime()));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Object[] {
                                rs.getString("MaNV"),
                                rs.getString("HoTen"),
                                rs.getInt("SoHoaDon"),
                                rs.getBigDecimal("TongDoanhThu")
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        // =====================================================
        // DOANH THU THEO NGÀY
        // =====================================================

        // =====================================================
        // DOANH THU THEO NGÀY (ĐÃ SỬA LỖI NHÂN ĐÔI/NHÂN BA DOANH THU DO JOIN)
        // =====================================================
        public Map<Integer, Double> getDoanhThuTheoNgay(int thang, int nam) {
            Map<Integer, Double> map = new java.util.TreeMap<>(); // Dùng TreeMap để tự sắp xếp ngày

            String sql = """
        SELECT Ngay, SUM(TongTien) as TongNgay FROM (
            SELECT DAY(NgayLapHoaDon) AS Ngay, SUM(TongTien) AS TongTien
            FROM HoaDonPhong 
            WHERE MONTH(NgayLapHoaDon) = ? AND YEAR(NgayLapHoaDon) = ? AND TrangThai = N'Đã thanh toán'
            GROUP BY DAY(NgayLapHoaDon)
            UNION ALL
            SELECT DAY(hdp.NgayLapHoaDon) AS Ngay, SUM(ct.ThanhTien) AS TongTien
            FROM ChiTietHoaDonDichVu ct
            JOIN HoaDonDichVu hdv ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu
            JOIN HoaDonPhong hdp ON hdv.MaHoaDonPhong = hdp.MaHoaDonPhong
            WHERE MONTH(hdp.NgayLapHoaDon) = ? AND YEAR(hdp.NgayLapHoaDon) = ? AND hdp.TrangThai = N'Đã thanh toán'
            GROUP BY DAY(hdp.NgayLapHoaDon)
        ) AS T GROUP BY Ngay ORDER BY Ngay
    """;

            try (Connection con = Database.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, thang); ps.setInt(2, nam);
                ps.setInt(3, thang); ps.setInt(4, nam);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    map.put(rs.getInt("Ngay"), rs.getDouble("TongNgay"));
                }
            } catch (Exception e) { e.printStackTrace(); }
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
        // =========================================================================
        // CẬP NHẬT: ĐỒNG BỘ 100% VỚI DATABASE THỰC TẾ CỦA BẠN (SẠCH LỖI LỆCH SỐ)
        // =========================================================================

        public double getDoanhThuPhongTheoNgay(int ngay, int thang, int nam) {
            String sql = """
                SELECT ISNULL(SUM(TongTien), 0) AS Tong
                FROM HoaDonPhong
                WHERE DAY(NgayLapHoaDon) = ?
                  AND MONTH(NgayLapHoaDon) = ?
                  AND YEAR(NgayLapHoaDon) = ?
                  AND TrangThai = N'Đã thanh toán'
            """;

            try (
                    Connection con = Database.getInstance().getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
            ) {
                ps.setInt(1, ngay);
                ps.setInt(2, thang);
                ps.setInt(3, nam);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("Tong");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public double getDoanhThuDichVuTheoNgay(int ngay, int thang, int nam) {
            String sql = """
                SELECT ISNULL(SUM(ct.ThanhTien), 0) AS Tong
                FROM ChiTietHoaDonDichVu ct
                JOIN HoaDonDichVu hdv 
                    ON ct.MaHoaDonDichVu = hdv.MaHoaDonDichVu
                JOIN HoaDonPhong hdp 
                    ON hdv.MaHoaDonPhong = hdp.MaHoaDonPhong
                WHERE DAY(hdp.NgayLapHoaDon) = ?
                  AND MONTH(hdp.NgayLapHoaDon) = ?
                  AND YEAR(hdp.NgayLapHoaDon) = ?
                  AND hdp.TrangThai = N'Đã thanh toán'
            """;

            try (
                    Connection con = Database.getInstance().getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
            ) {
                ps.setInt(1, ngay);
                ps.setInt(2, thang);
                ps.setInt(3, nam);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("Tong");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public double getTongDoanhThuTheoNgay(int ngay, int thang, int nam) {
            // Tổng doanh thu của một ngày bằng Tiền phòng thực tế + Tiền dịch vụ thực tế của ngày đó
            return getDoanhThuPhongTheoNgay(ngay, thang, nam)
                    + getDoanhThuDichVuTheoNgay(ngay, thang, nam);
        }
    }