package client.ejb;

import client.model.Doctor;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Maciek on 2017-05-19.
 */
@DataSourceDefinition(
        name="java:global/PrzychodniaDS",
        className="org.apache.derby.jdbc.ClientDataSource",
        minPoolSize = 1,
        initialPoolSize = 1,
        portNumber = 1527,
        serverName = "localhost",
        user = "app",
        password = "app",
        databaseName = "PrzychodniaDB"
)
@Stateless
public class DoctorDao {
    @PersistenceContext(unitName = "Przychodnia-ejb_ejb_1.0PU")
    private EntityManager em;

    public void save(Doctor doctor) {
        em.persist(doctor);
    }

    public List<Doctor> findAll() {
        TypedQuery<Doctor> q = em.createNamedQuery("Doctor.findAll", Doctor.class);
        return q.getResultList();
    }

    public Doctor findDoctor(String name, String surname)
    {
        TypedQuery<Doctor> query = em.createNamedQuery("Doctor.findDoctor", Doctor.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        return query.getSingleResult();
    }

    public void remove(Long id) {
        em.remove(em.getReference(Doctor.class, id));
    }

    public void update(Doctor d) {
        em.merge(d);
    }
}
