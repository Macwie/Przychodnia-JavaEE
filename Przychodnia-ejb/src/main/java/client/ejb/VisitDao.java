package client.ejb;

import client.model.Admission;
import client.model.Visit;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Maciek on 2017-05-20.
 */
@Stateless
public class VisitDao {
    @PersistenceContext(unitName = "Przychodnia-ejb_ejb_1.0PU")
    private EntityManager em;

    public void save(Visit visit) {
        em.persist(visit);
    }

    public List<Visit> findAll() {
        TypedQuery<Visit> q = em.createNamedQuery("Visit.findAll", Visit.class);
        return q.getResultList();
    }

    public List<Visit> findSoon(LocalDateTime date)
    {
        TypedQuery<Visit> q = em.createNamedQuery("Visit.findSoon", Visit.class);
        q.setParameter("Vstart", date);
        q.setParameter("Vend", date.plusDays(1));
        return q.getResultList();
    }

    public void remove(Long id) {
        em.remove(em.getReference(Visit.class, id));
    }

    public List<Visit> findRelatedTo(Admission admission) {
        TypedQuery<Visit> q = em.createNamedQuery("Visit.findRelatedTo", Visit.class);
        q.setParameter("admission", admission);
        return q.getResultList();
    }
}

