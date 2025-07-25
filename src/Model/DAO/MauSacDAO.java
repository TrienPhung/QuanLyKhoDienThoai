package Model.DAO;

import Model.DTO.ThuocTinhSanPham.MauSacDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Utils.DbConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;


public class MauSacDAO implements DAOinterface<MauSacDTO> {

    public static MauSacDAO getInstance() {
        return new MauSacDAO();
    }

    @Override
    public int insert(MauSacDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "INSERT INTO `mausac`(`mamau`, `tenmau`,`trangthai`) VALUES (?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getMamau());
            pst.setString(2, t.getTenmau());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(MauSacDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `mausac` SET `tenmau`=? WHERE `mamau`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTenmau());
            pst.setInt(2, t.getMamau());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `mausac` SET `trangthai` = 0 WHERE mamau = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1,t);
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<MauSacDTO> selectAll() {
        ArrayList<MauSacDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM mausac WHERE trangthai = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                MauSacDTO ms = new MauSacDTO(mamau, tenmau);
                result.add(ms);
            }
            DbConnection.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
    
    public ArrayList<MauSacDTO> getAll() {
        ArrayList<MauSacDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM mausac";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                MauSacDTO ms = new MauSacDTO(mamau, tenmau);
                result.add(ms);
            }
            DbConnection.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public MauSacDTO selectById(String t) {
        MauSacDTO result = null;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM mausac WHERE mamau=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                result = new MauSacDTO(mamau, tenmau);
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
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlykhohangdienthoai' AND   TABLE_NAME   = 'mausac'";
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
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
