package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Иван", "Семенов", (byte) 18);
        userService.saveUser("Василий", "Афанасьев", (byte) 32);
        userService.saveUser("Степан", "Зосимов", (byte) 22);
        userService.saveUser("Антон", "Петренко", (byte) 21);

        List<User> users = userService.getAllUsers();

        for (User user : users) {
            System.out.println(user);
        }
        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
