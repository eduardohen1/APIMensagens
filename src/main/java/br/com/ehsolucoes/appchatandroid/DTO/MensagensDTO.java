package br.com.ehsolucoes.appchatandroid.DTO;

import java.io.Serializable;
import java.util.Date;

public class MensagensDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String message;
    private Date sentat;
    private Integer users_id;
    private String name;

    public MensagensDTO(){}

    public MensagensDTO(Integer id, String message, Date sentat, Integer users_id, String name) {
        this.id = id;
        this.message = message;
        this.sentat = sentat;
        this.users_id = users_id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentat() {
        return sentat;
    }

    public void setSentat(Date sentat) {
        this.sentat = sentat;
    }

    public Integer getUsers_id() {
        return users_id;
    }

    public void setUsers_id(Integer users_id) {
        this.users_id = users_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
