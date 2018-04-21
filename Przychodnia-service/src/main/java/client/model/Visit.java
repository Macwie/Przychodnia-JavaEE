package client.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Maciek on 2017-05-20.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Visit.findAll", query = "select b from Visit b"),
        @NamedQuery(name ="Visit.findRelatedTo", query = "select b from Visit b where b.admission = :admission"),
        @NamedQuery(name = "Visit.findSoon", query="select b from Visit b where b.VDate between :Vstart and :Vend")
})

public class Visit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    private Patient patient;
    private LocalDateTime VDate;
    @ManyToOne()
    private Admission admission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getVDate() {
        return VDate;
    }

    public void setVDate(LocalDateTime VDate) {
        this.VDate = VDate;
    }

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
    }
}
