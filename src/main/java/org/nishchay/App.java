package org.nishchay;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

public class App 
{

//    private SessionFactory sessionFactory;
    private EntityManagerFactory sessionFactory;

    public App() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static void main(String[] args) {
        User user = new User("Nishchay", LocalDate.now());
        App app = new App();
        app.create(user);
        app.read(3L);
        app.update(2L, new User("NishchayUpdated", LocalDate.now()));
        app.delete(3L);
        }

    // CRUD

    // CREATE
    public void create(User user) {
//        try(Session session = sessionFactory.openSession();) {
        try(EntityManager session = sessionFactory.createEntityManager();) {
//            session.beginTransaction();
            session.getTransaction().begin();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ
    public User read(Long id) {
        User user = null;
//        try(Session session = sessionFactory.openSession();) {
        try(EntityManager session = sessionFactory.createEntityManager();) {
//            List<User> users = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).toList();
            List<User> users = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getResultList();
            user = users.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // UPDATE
    public void update(Long id, User userUpdated) {
        User user = null;
//        try(Session session = sessionFactory.openSession();) {
        try(EntityManager session = sessionFactory.createEntityManager();) {
//            List<User> existingEntry = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).list();
            List<User> existingEntry = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getResultList();
            if(existingEntry.isEmpty()) {
                System.out.println("No user found with id : " + id);
                return;
            }
            userUpdated.setId(id);
//            session.beginTransaction();
            session.getTransaction().begin();
            session.merge(userUpdated); // merge() will update if present, otherwise insert,
            // persist() is still preferred for new entries as it does not have to check for existing entries
            // and hence will perform faster
            // **can also directly use hql / jpql to update
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Updated user : " + userUpdated);
    }


    // DELETE
    public void delete(Long id) {
        User user = null;
//        try(Session session = sessionFactory.openSession();) {
        try(EntityManager session = sessionFactory.createEntityManager();) {
//            List<User> existingEntry = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).list();
            List<User> existingEntry = session.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class).setParameter("id", id).getResultList();
            if(existingEntry.isEmpty()) {
                System.out.println("No user found with id : " + id);
                return;
            }
            user = existingEntry.get(0);
//            session.beginTransaction();
            session.getTransaction().begin();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Deleted user : " + user);
    }

}
