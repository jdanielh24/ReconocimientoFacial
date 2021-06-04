/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author jdanielh24
 */
public class Image {
    
    private int id;
    private String nameImg;
    
    public Image(){
        
    }

    public Image(int id, String nameImg) {
        this.id = id;
        this.nameImg = nameImg;
    }

    public String getNameImg() {
        return nameImg;
    }

    public void setNameImg(String nameImg) {
        this.nameImg = nameImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
