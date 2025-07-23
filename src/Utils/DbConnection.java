/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author ACER
 */
public class DbConnection {
     public static Connection getConnection() {
        Connection result = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String url = "jdbc:mySQL://localhost:3306/quanlykhohangdienthoai";
            String userName = "root";
            String password = "";     
            result = DriverManager.getConnection(url, userName, password);
        //    JOptionPane.showMessageDialog(null, "Kết nối thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu !", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Connection conn = getConnection();
        if(conn != null){
            System.out.println("Kết nối thành công!");
        }else{
            System.out.println("Kết nối thất bại!");
        }
    }
}
