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
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(IUserImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateUser(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User showUser(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
