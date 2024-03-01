package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    static final SessionFactory sessionFactory = Util.getSessionFactory();
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(255), lastname VARCHAR(255), age SMALLINT)";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(SQL).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица создана");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании таблицы с Hibernate: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS users";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(SQL).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица удалена");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении таблицы c Hibernate: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            System.out.println("user с именем " + name + " добавлен в базу данных.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при добавлении " + name + " c Hibernate: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.getReference(User.class, id);
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении c Hibernate: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("SELECT u from User u", User.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при возвращении списка users c Hibernate: " + e.getMessage());
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("DELETE FROM User");
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при очищении таблицы с Hibernate: " + e.getMessage());
        }
    }
}
