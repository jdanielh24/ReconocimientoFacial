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
public class User {
    
    private int id;
    private String name;
    private String jobTitle;

    public User() {
    }
    
    public User(int id, String name, String jobTitle) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
    }

    public User(String name, String jobTitle) {
        this.name = name;
        this.jobTitle = jobTitle;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
