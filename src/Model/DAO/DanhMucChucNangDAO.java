/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.DTO.DanhMucChucNangDTO;
import Utils.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DanhMucChucNangDAO {

    public static DanhMucChucNangDAO getInstance() {
        return new DanhMucChucNangDAO();
    }

    public ArrayList<DanhMucChucNangDTO> selectAll() {
        ArrayList<DanhMucChucNangDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM danhmucchucnang";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                String machucnang = rs.getString("machucnang");
                String tenchucnang = rs.getString("tenchucnang");
                DanhMucChucNangDTO dvt = new DanhMucChucNangDTO(machucnang, tenchucnang);
                result.add(dvt);
            }
            DbConnection.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
}
