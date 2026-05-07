
package Dao;

import ConnectDB.Database;
import Entity.Thue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThueDao {

    // =====================================================
    // LẤY TOÀN BỘ THUẾ
    // =====================================================

    public List<Thue> getAll() {

        List<Thue> list = new ArrayList<>();

        String sql = """
            SELECT *
            FROM Thue
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

                list.add(
                        new Thue(
                                rs.getString("MaThue"),
                                rs.getString("TenThue"),
                                rs.getDouble("PhanTramThue")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // LẤY DANH SÁCH TÊN THUẾ
    // =====================================================

    public List<String> getAllTenThue() {

        List<String> list = new ArrayList<>();

        String sql = """
            SELECT TenThue
            FROM Thue
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

                list.add(
                        rs.getString("TenThue")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // LẤY PHẦN TRĂM THUẾ THEO TÊN
    // =====================================================

    public double getPhanTramThue(String tenThue) {

        if (tenThue == null || tenThue.trim().isEmpty()) {
            return 0;
        }

        String sql = """
            SELECT PhanTramThue
            FROM Thue
            WHERE TenThue = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, tenThue);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getDouble(
                        "PhanTramThue"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // =====================================================
    // INSERT
    // =====================================================

    public boolean insert(Thue thue) {

        String sql = """
            INSERT INTO Thue
            VALUES (?, ?, ?)
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, thue.getMaThue());
            ps.setString(2, thue.getTenThue());
            ps.setDouble(3, thue.getPhanTramThue());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public boolean update(Thue thue) {

        String sql = """
            UPDATE Thue
            SET
                TenThue = ?,
                PhanTramThue = ?
            WHERE MaThue = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, thue.getTenThue());
            ps.setDouble(2, thue.getPhanTramThue());
            ps.setString(3, thue.getMaThue());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // DELETE
    // =====================================================

    public boolean delete(String maThue) {

        String sql = """
            DELETE FROM Thue
            WHERE MaThue = ?
        """;

        try (
                Connection con =
                        Database.getInstance().getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, maThue);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String getMaThueByTen(String tenThue) {

        if (tenThue == null || tenThue.trim().isEmpty()) {
            return null;
        }

        String sql = """
        SELECT MaThue
        FROM Thue
        WHERE TenThue = ?
    """;

        try (
                Connection con =
                        Database.getInstance()
                                .getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(1, tenThue);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return rs.getString("MaThue");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}