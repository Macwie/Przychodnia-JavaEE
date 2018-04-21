package client.ejb;

import client.model.SystemUser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Maciek on 2017-05-21.
 */
@Stateless
public class SystemUserBean implements SystemUserBeanRemote {

    @EJB
    private SystemUserDao system_user;

    @Override
    public void save(SystemUser user) {
        system_user.save(user);
    }

    @Override
    public List<SystemUser> findAll() {
        return system_user.findAll();
    }

    @Override
    public SystemUser findUser(String username, String password) {
        return system_user.findUser(username, password);
    }

    @Override
    public void remove(Long id) {
        system_user.remove(id);
    }
}
