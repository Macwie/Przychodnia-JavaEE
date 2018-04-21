package client.ejb;

import client.model.Admission;
import client.model.Patient;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Maciek on 2017-06-04.
 */
@Remote
public interface PatientBeanRemote {

    void save(Patient patient);
    List<Patient> findAll();
    void remove(Long id);
}
