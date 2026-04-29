package Dao;

import ConnectDB.Database;
import Entity.LoaiPhong;

import java.sql.*;
import java.util.*;

public class LoaiPhongDao {

    public List<LoaiPhong> getAllLoaiPhong(){

        List<LoaiPhong> list = new ArrayList<>();

        String sql = "SELECT * FROM LoaiPhong";

        try(Connection con = Database.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                list.add(new LoaiPhong(
                        rs.getString("MaLP"),
                        rs.getString("TenLP"),
                        rs.getBigDecimal("GiaGioDau"),
                        rs.getBigDecimal("GiaGioTiepTheo"),
                        rs.getBigDecimal("GiaCaNgay")
                ));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
}