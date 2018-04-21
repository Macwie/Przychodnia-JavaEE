package client.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 2017-05-20.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Patient.findAll", query = "select b from Patient b")
})

public class Patient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Size(min = 11, max= 11)
    private String pesel;
    private String mail;
    @OneToMany(mappedBy = "patient", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Visit> visit_list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void addVisit(Visit visit)
    {
        if(visit_list == null)
            visit_list = new ArrayList<>();
        visit_list.add(visit);
    }

    public void removeVisit(Visit visit)
    {
        if(visit_list != null)
            visit_list.remove(visit);

    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<Visit> getVisit_list() {
        return visit_list;
    }

    public void setVisit_list(List<Visit> visit_list) {
        this.visit_list = visit_list;
    }
}
