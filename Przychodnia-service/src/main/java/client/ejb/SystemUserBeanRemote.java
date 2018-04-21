package client.ejb;

import client.model.SystemUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by Maciek on 2017-05-21.
 */
@Remote
public interface SystemUserBeanRemote {
    void save(SystemUser user);
    List<SystemUser> findAll();
    SystemUser findUser(String username, String password);
    void remove(Long id);
}
