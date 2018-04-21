package client.ejb;

import client.model.Admission;
import client.model.Doctor;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Maciek on 2017-06-04.
 */
@Remote
public interface AdmissionBeanRemote {
    void save(Admission admission);
    List<Admission> findAll();
    List<Admission> findRelatedTo(Doctor doctor);
    Admission findAdmission(Long id);
    void update(Admission admission);
    void remove(Long id);
}
