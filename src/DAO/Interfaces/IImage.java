/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO.Interfaces;

import Modelos.Image;

/**
 *
 * @author jdanielh24
 */
public interface IImage {
    public boolean addImages(Image u);
    public boolean deleteImages(int id);
}
