package web.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.models.Role;
import web.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Repository
public class UserHibernateDao {

    @PersistenceContext private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    public boolean addUser(
            String firstName, String lastName, int age, String email, Set<Role> roles, String password) {
        if (!checkLogin(email)) {
            EntityManager em = getEntityManager();
            em.persist(new User(firstName, lastName, age, email, roles, password));
            return true;
        } else {
            return false;
        }
    }


    @Transactional(readOnly = true)
    public User getUserById(long id) {
        User result;
        EntityManager em = getEntityManager();
        List<?> tempResult =
                em.createQuery("SELECT user from User user where user.id = ?1")
                        .setParameter(1, id)
                        .getResultList();
        if (tempResult.size() != 0) {
            result = ((User) tempResult.get(0));
        } else {
            result = null;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public long getUserIdByLogin(String email) {
        long result;
        EntityManager em = getEntityManager();
        List<?> tempResult =
                em.createQuery("SELECT user from User user where user.email = ?1")
                        .setParameter(1, email)
                        .getResultList();
        if (tempResult.size() != 0) {
            result = ((User) tempResult.get(0)).getId();
        } else {
            result = -1;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        EntityManager em = getEntityManager();
        @SuppressWarnings("unchecked")
        List<User> result = em.createQuery("SELECT user from User user").getResultList();
        return result;
    }

    @Transactional
    public void updateUser(String firstName, String lastName, int age,
                           String email,
                           Set<Role> roles,
                           String password,
                           long id) {

        if (checkLogin(getUserById(id).getEmail())) {

            EntityManager em = getEntityManager();
            User changedUser = getUserById(id);

            em.detach(changedUser);
            changedUser.setFirstName(firstName);
            changedUser.setLastName(lastName);
            changedUser.setAge(age);
            changedUser.setRoles(roles);
            changedUser.setEmail(email);
            changedUser.setPassword(password);
            changedUser.setEmail(email);
            em.merge(changedUser);
        }
    }

    @Transactional
    public boolean deleteUser(long id) {
        if (checkLogin(getUserById(id).getEmail())) {
            EntityManager em = getEntityManager();
            User deletedUser = getUserById(id);
            em.remove(deletedUser);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public boolean checkLogin(String email) {

        EntityManager em = getEntityManager();
        List<?> tempResult =
                em.createQuery("SELECT user from User user where user.email = ?1")
                        .setParameter(1, email)
                        .getResultList();
        return tempResult.size() != 0;
    }
}
