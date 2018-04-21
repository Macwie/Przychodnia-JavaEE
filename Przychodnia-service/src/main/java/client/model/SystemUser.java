package client.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Maciek on 2017-05-21.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "SystemUser.findAll", query = "select b from SystemUser b"),
        @NamedQuery(name = "SystemUser.findUser", query = "select b from SystemUser b where b.login = :username and b.password = :password")
})

public class SystemUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    @OneToOne()
    private Doctor doctor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}

