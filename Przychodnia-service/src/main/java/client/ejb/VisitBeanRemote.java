package client.ejb;

import client.model.Admission;
import client.model.Visit;

import javax.ejb.Remote;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Maciek on 2017-06-04.
 */
@Remote
public interface VisitBeanRemote {
    void save(Visit visit);
    List<Visit> findAll();
    List<Visit> findRelatedTo(Admission admission);
    List<Visit> findSoon(LocalDateTime date);
    void remove(Long id);
    void sendEmail();
}
