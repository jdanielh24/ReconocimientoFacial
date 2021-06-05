/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.IUser;
import Modelos.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jdanielh24
 */
public class IUserImp implements IUser{

    @Override
    public boolean addUser(User u) {
        Conexion conn = new Conexion();
        String sql = "INSERT INTO user (name, jobTitle) VALUES (?,?)";
        try {
            PreparedStatement stmt = conn.conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getJobTitle());
            stmt.executeUpdate();
            
            sql = "SELECT LAST_INSERT_ID()";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                u.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(conn.conn != null){
                try {
                    conn.conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        Conexion conn = new Conexion();
        String sql = "DELETE FROM user WHERE id=?";
        try {
            PreparedStatement stmt = conn.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(conn.conn != null){
                try {
                    conn.conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateUser(User u) {
        Conexion conn = new Conexion();
        String sql = "UPDATE user SET name=?, jobTitle=? WHERE id=?";
        try {
            PreparedStatement stmt = conn.conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getJobTitle());
            stmt.setInt(3, u.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(conn.conn != null){
                try {
                    conn.conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    @Override
    public User getUser(int id) {
        Conexion conn = new Conexion();
        String sql = "SELECT * FROM user WHERE id=?";
        try {
            PreparedStatement stmt = conn.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                User u = new User(rs.getInt(1), rs.getString(2), rs.getString(3));
                return u;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(conn.conn != null){
                try {
                    conn.conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
        
    }

    @Override
    public List<User> listUsers() {
        List users = new ArrayList<User>();
        Conexion conn = new Conexion();
        String sql = "SELECT * FROM user";
        PreparedStatement stmt;
        try {
            stmt = conn.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            return users;
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(conn.conn != null)
                try {
                    conn.conn.close();
                    return users;
            } catch (SQLException ex) {
                Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;
    }
    
}
