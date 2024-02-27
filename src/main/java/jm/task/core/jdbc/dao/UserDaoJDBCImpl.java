package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(255), lastname VARCHAR(255), age SMALLINT)";
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(SQL);
            System.out.println("Таблица создана");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = connection.getMetaData().getTables(null, null, "users", null);
            if (resultSet.next()) {
                statement.executeUpdate(SQL);
            }
            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String SQL = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("user с именем " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении user " + name + " - " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        String SQL = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setLong(1, id);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        String SQL = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                byte age = resultSet.getByte("age");
                User user = new User(name, lastname, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при возвращении списка users" + e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String SQL = "DELETE FROM users";
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очищении таблицы " + e.getMessage());
        }
    }
}
