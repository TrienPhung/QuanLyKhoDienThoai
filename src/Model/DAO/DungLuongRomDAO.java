/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.DTO.ThuocTinhSanPham.DungLuongRomDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Utils.DbConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class DungLuongRomDAO implements DAOinterface<DungLuongRomDTO> {

    public static DungLuongRomDAO getInstance() {
        return new DungLuongRomDAO();
    }

    @Override
    public int insert(DungLuongRomDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "INSERT INTO `dungluongrom`(`madlrom`, `kichthuocrom`,`trangthai`) VALUES (?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getMadungluongrom());
            pst.setInt(2, t.getDungluongrom());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DungLuongRomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(DungLuongRomDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `dungluongrom` SET `kichthuocrom`=? WHERE `madlrom`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getDungluongrom());
            pst.setInt(2, t.getMadungluongrom());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DungLuongRomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `dungluongrom` SET trangthai = 0 WHERE madlrom = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DungLuongRomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<DungLuongRomDTO> selectAll() {
        ArrayList<DungLuongRomDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM dungluongrom WHERE trangthai = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int marom = rs.getInt("madlrom");
                int kichthuocrom = rs.getInt("kichthuocrom");
                DungLuongRomDTO dlr = new DungLuongRomDTO(marom, kichthuocrom);
                result.add(dlr);
            }
            DbConnection.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    public DungLuongRomDTO selectById(String t) {
        DungLuongRomDTO result = null;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM dungluongrom WHERE madlrom=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int madlrom = rs.getInt("madlrom");
                int kichthuorom = rs.getInt("kichthuocrom");
                result = new DungLuongRomDTO(madlrom, kichthuorom);
            }
            DbConnection.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlykhohangdienthoai' AND   TABLE_NAME   = 'dungluongrom'";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery(sql);
            if (!rs2.isBeforeFirst()) {
                System.out.println("No data");
            } else {
                while (rs2.next()) {
                    result = rs2.getInt("AUTO_INCREMENT");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DungLuongRomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
