/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO.Interfaces;

import Modelos.User;

/**
 *
 * @author jdanielh24
 */
public interface IUser {
    public boolean addUser(User u);
    public boolean deleteUser(int id);
    public boolean updateUser(int id);
    public User showUser(int id);
}
