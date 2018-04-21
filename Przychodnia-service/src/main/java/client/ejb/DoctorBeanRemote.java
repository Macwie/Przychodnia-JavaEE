package client.ejb;

import client.model.Doctor;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Maciek on 2017-05-29.
 */
@Remote
public interface DoctorBeanRemote {
    void save(Doctor doctor);
    List<Doctor> findAll();
    Doctor findDoctor(String name, String surname);
    void update(Doctor doctor);
    void remove(Long id);
}
