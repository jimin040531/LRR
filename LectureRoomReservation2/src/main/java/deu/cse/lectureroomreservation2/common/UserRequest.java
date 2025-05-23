/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.lectureroomreservation2.common;

import java.io.Serializable;

/**
 *
 * @author Jimin
 */
public class UserRequest implements Serializable {

    private String command; // ADD, DELETE, SEARCH
    private String role;
    private String name;
    private String id;
    private String password;
    private String nameFilter;

    public UserRequest(String command, String role, String name, String id, String password, String nameFilter) {
        this.command = command;
        this.role = role;
        this.name = name;
        this.id = id;
        this.password = password;
        this.nameFilter = nameFilter;
    }

    public String getCommand() {
        return command;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNameFilter() {
        return nameFilter;
    }
}
