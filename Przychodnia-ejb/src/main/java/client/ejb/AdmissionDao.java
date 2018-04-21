package client.ejb;

import client.model.Admission;
import client.model.Doctor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Maciek on 2017-05-20.
 */
@Stateless
public class AdmissionDao {
    @PersistenceContext(unitName = "Przychodnia-ejb_ejb_1.0PU")
    private EntityManager em;

    public void save(Admission admission_place) {
        em.persist(admission_place);
    }

    public List<Admission> findAll() {
        TypedQuery<Admission> q = em.createNamedQuery("Admission.findAll", Admission.class);
        return q.getResultList();
    }

    public List<Admission> findRelatedTo(Doctor doctor)
    {
        TypedQuery<Admission> query = em.createNamedQuery("Admission.findRelatedTo", Admission.class);
        query.setParameter("doctor", doctor);
        return query.getResultList();
    }

    public Admission findAdmission(Long id)
    {
        TypedQuery<Admission> query = em.createNamedQuery("Admission.findAdmission", Admission.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public void remove(Long id) {
        em.remove(em.getReference(Admission.class, id));
    }

    public void update(Admission admission) {
        em.merge(admission);
    }
}
