package client.ejb;

import client.model.SystemUser;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Maciek on 2017-05-21.
 */
@Stateless
public class SystemUserDao {
    @PersistenceContext(unitName = "Przychodnia-ejb_ejb_1.0PU")
    private EntityManager em;

    public void save(SystemUser user) {
        em.persist(user);
    }

    public List<SystemUser> findAll() {
        TypedQuery<SystemUser> q = em.createNamedQuery("SystemUser.findAll", SystemUser.class);
        return q.getResultList();
    }

    public SystemUser findUser(String username, String password)
    {
        TypedQuery<SystemUser> query = em.createNamedQuery("SystemUser.findUser", SystemUser.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getSingleResult();
    }

    public void remove(Long id) {
        em.remove(em.getReference(SystemUser.class, id));
    }
}
