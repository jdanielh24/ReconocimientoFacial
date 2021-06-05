/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.Interfaces.IImage;
import Modelos.Image;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author jdanielh24
 */
public class IImageImp implements IImage {

    @Override
    public boolean addImages(Image u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteImages(int id) {
        String rutaBase = System.getProperty("user.dir");
        File dirUsuario = new File(rutaBase + "/Datos/" + id + "/");
        if(dirUsuario.exists()){
            deleteDir(dirUsuario);
            return true;
        }else return false;
    }
    
    private void deleteDir(File file){
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

}
