/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

import Model.DTO.ThuocTinhSanPham.HeDieuHanhDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Utils.DbConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;


public class HeDieuHanhDAO implements DAOinterface<HeDieuHanhDTO>{

    public static HeDieuHanhDAO getInstance() {
        return new HeDieuHanhDAO();
    }
    @Override
    public int insert(HeDieuHanhDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "INSERT INTO `hedieuhanh`(`mahedieuhanh`, `tenhedieuhanh`,`trangthai`) VALUES (?,?,1)";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t.getMahdh());
            pst.setString(2, t.getTenhdh());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(HeDieuHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(HeDieuHanhDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `hedieuhanh` SET `tenhedieuhanh`=? WHERE `mahedieuhanh`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t.getTenhdh());
            pst.setInt(2, t.getMahdh());
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(HeDieuHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "UPDATE `hedieuhanh` SET `trangthai` = 0 WHERE mahedieuhanh = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1,t);
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(HeDieuHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<HeDieuHanhDTO> selectAll() {
        ArrayList<HeDieuHanhDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM hedieuhanh WHERE trangthai = 1";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mahdh = rs.getInt("mahedieuhanh");
                String tenhdh = rs.getString("tenhedieuhanh");
                HeDieuHanhDTO ms = new HeDieuHanhDTO(mahdh, tenhdh);
                result.add(ms);
            }
            DbConnection.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public HeDieuHanhDTO selectById(String t) {
        HeDieuHanhDTO result = null;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM hedieuhanh WHERE mahedieuhanh=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int mahdh = rs.getInt("mahedieuhanh");
                String tenhdh = rs.getString("tenhedieuhanh");
                result = new HeDieuHanhDTO(mahdh, tenhdh);
            }
            DbConnection.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(HeDieuHanhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlykhohangdienthoai' AND   TABLE_NAME   = 'hedieuhanh'";
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
            Logger.getLogger(HeDieuHanhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    }

