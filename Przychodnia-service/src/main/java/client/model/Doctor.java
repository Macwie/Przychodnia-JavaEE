package client.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 2017-05-19.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Doctor.findAll", query = "select b from Doctor b"),
        @NamedQuery(name = "Doctor.findDoctor", query = "select b from Doctor b where b.name = :name and b.surname = :surname")
})

public class Doctor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String specialization;
    @OneToMany(mappedBy = "doctor", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Admission> admissions;
    @OneToOne(mappedBy = "doctor", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private SystemUser user;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

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

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public List<Admission> getAdmissions() {
        return admissions;
    }

    public void setAdmissions(List<Admission> admission_places) {
        this.admissions = admission_places;
    }

    public void addAdmission(Admission a)
    {
        if(admissions == null)
            admissions = new ArrayList<>();
        admissions.add(a);
    }

    public void removeAdmission(int i)
    {
        admissions.remove(i);
    }
}