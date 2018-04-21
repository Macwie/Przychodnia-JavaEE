package client.ejb;

import client.model.Admission;
import client.model.Patient;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Maciek on 2017-05-20.
 */
@Stateless
public class PatientDao {
    @PersistenceContext(unitName = "Przychodnia-ejb_ejb_1.0PU")
    private EntityManager em;

    public void save(Patient patient) {
        em.persist(patient);
    }

    public List<Patient> findAll() {
        TypedQuery<Patient> q = em.createNamedQuery("Patient.findAll", Patient.class);
        return q.getResultList();
    }

    public void remove(Long id) {
        em.remove(em.getReference(Patient.class, id));
    }
}