package client.ejb;

import client.model.Doctor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Maciek on 2017-05-29.
 */
@Stateless
public class DoctorBean implements DoctorBeanRemote {

    @EJB
    private DoctorDao doctor;

    @Override
    public void save(Doctor d) {
        doctor.save(d);
    }

    @Override
    public List<Doctor> findAll() {
        return doctor.findAll();
    }

    @Override
    public Doctor findDoctor(String name, String surname) {
        return doctor.findDoctor(name, surname);
    }

    @Override
    public void update(Doctor d) {
        doctor.update(d);
    }

    @Override
    public void remove(Long id) {
        doctor.remove(id);
    }
}
