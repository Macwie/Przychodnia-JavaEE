package client.ejb;

import client.model.Admission;
import client.model.Patient;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Maciek on 2017-06-04.
 */
@Stateless
public class PatientBean implements PatientBeanRemote {

    @EJB
    PatientDao patient;

    @Override
    public void save(Patient patient) {
        this.patient.save(patient);
    }

    @Override
    public List<Patient> findAll() {
        return this.patient.findAll();
    }

    @Override
    public void remove(Long id) {
        this.patient.remove(id);
    }
}
