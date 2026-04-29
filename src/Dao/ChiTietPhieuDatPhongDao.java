package Dao;

import ConnectDB.Database;
import java.sql.*;
import java.time.LocalDateTime;

public class ChiTietPhieuDatPhongDao {

    public boolean insert(String maPhieu, String maPhong,
                          LocalDateTime tu, LocalDateTime den,
                          int soNguoi){

        String sql = """
            INSERT INTO ChiTietPhieuDatPhong
            (MaPhieuDatPhong, MaPhong, ThoiGianNhan, ThoiGianTra, SoLuong)
            VALUES (?, ?, ?, ?, ?)
        """;

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, maPhieu);
            ps.setString(2, maPhong);
            ps.setTimestamp(3, Timestamp.valueOf(tu));
            ps.setTimestamp(4, Timestamp.valueOf(den));
            ps.setInt(5, soNguoi);

            return ps.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}