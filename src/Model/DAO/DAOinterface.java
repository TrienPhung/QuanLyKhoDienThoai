package Model.DAO;

import java.util.ArrayList;


public interface DAOinterface<T> {
    public int insert(T t);
    
    public int update(T t);
    
    public int delete(String t);
    
    public ArrayList<T> selectAll();
    
    public T selectById(String t);
    
    
    int getAutoIncrement();
}
