package client.ejb;

import client.model.Admission;
import client.model.Doctor;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Maciek on 2017-06-04.
 */
@Stateless
public class AdmissionBean implements AdmissionBeanRemote {

    @EJB
    AdmissionDao admission;

    @Override
    public void save(Admission admission) {
        this.admission.save(admission);
    }

    @Override
    public List<Admission> findAll() {
        return admission.findAll();
    }

    @Override
    public List<Admission> findRelatedTo(Doctor doctor) {
        return admission.findRelatedTo(doctor);
    }

    @Override
    public Admission findAdmission(Long id) {
        return admission.findAdmission(id);
    }

    @Override
    public void update(Admission admission) {
        this.admission.update(admission);
    }

    @Override
    public void remove(Long id) {
        admission.remove(id);
    }
}
