package client.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 2017-05-20.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Admission.findAll", query = "select b from Admission b"),
        @NamedQuery(name="Admission.findRelatedTo", query ="select b from Admission b where b.doctor = :doctor"),
        @NamedQuery(name="Admission.findAdmission", query = "select b from Admission b where b.id = :id")
})

public class Admission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String place;
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
    @ManyToOne()
    private Doctor doctor;
    @OneToMany(mappedBy = "admission", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Visit> visits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit)
    {
        if(visits == null)
            visits = new ArrayList<>();
        visits.add(visit);
    }
}
