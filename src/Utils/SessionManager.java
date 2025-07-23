/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

public class SessionManager {
    private static boolean isLoggedIn = false;

    // Đánh dấu người dùng đã đăng nhập
    public static void setLoggedIn(boolean status) {
        isLoggedIn = status;
    }

    // Kiểm tra trạng thái đăng nhập
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Đăng xuất
    public static void logout() {
        isLoggedIn = false;
    }
}
