package Model.DAO;

import Model.DTO.ChiTietQuyenDTO;
import Utils.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChiTietQuyenDAO implements ChiTietInterface<ChiTietQuyenDTO> {

    public static ChiTietQuyenDAO getInstance() {
        return new ChiTietQuyenDAO();
    }
    
    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "DELETE FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            DbConnection.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int insert(ArrayList<ChiTietQuyenDTO> t) {
        int result = 0;
        for (int i = 0; i < t.size(); i++) {
            try {
                Connection con = (Connection) DbConnection.getConnection();
                String sql = "INSERT INTO `ctquyen`(`manhomquyen`,`machucnang`,`hanhdong`) VALUES (?,?,?)";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setInt(1, t.get(i).getManhomquyen());
                pst.setString(2, t.get(i).getMachucnang());
                pst.setString(3, t.get(i).getHanhdong());
                result = pst.executeUpdate();
                DbConnection.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietQuyenDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietQuyenDTO> selectAll(String t) {
        ArrayList<ChiTietQuyenDTO> result = new ArrayList<ChiTietQuyenDTO>();
        try {
            Connection con = (Connection) DbConnection.getConnection();
            String sql = "SELECT * FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int manhomquyen = rs.getInt("manhomquyen");
                String machucnang = rs.getString("machucnang");
                String hanhdong = rs.getString("hanhdong");
                ChiTietQuyenDTO dvt = new ChiTietQuyenDTO(manhomquyen, machucnang, hanhdong);
                result.add(dvt);
            }
            DbConnection.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietQuyenDTO> t, String pk) {
        int result = this.delete(pk);
        if(result != 0) result = this.insert(t);
        return result;
    }
}
