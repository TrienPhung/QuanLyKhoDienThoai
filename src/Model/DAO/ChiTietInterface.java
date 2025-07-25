/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Model.DAO;

import java.util.ArrayList;


public interface ChiTietInterface<T> {
    public int insert(ArrayList<T> t);
    public int delete(String t);
    public int update(ArrayList<T> t, String pk);
    public ArrayList<T> selectAll(String t);
}
