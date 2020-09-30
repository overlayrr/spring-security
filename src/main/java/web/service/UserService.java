package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.DAO.UserHibernateDao;
import web.models.User;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserHibernateDao dao;

    public void addUser(User user) {
        dao.addUser(
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getRoles(),
                user.getPassword());
    }

    public User getUserById(long id) {
        return dao.getUserById(id);
    }

    public long getUserIdByLogin(String email) {
        return dao.getUserIdByLogin(email);
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public void updateUser(User user) {
        dao.updateUser(
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getEmail(),
                user.getRoles(),
                user.getPassword(),
                user.getId());
    }

    public boolean deleteUser(long id) {
        return dao.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dao.getUserById(dao.getUserIdByLogin(username));
    }
}
