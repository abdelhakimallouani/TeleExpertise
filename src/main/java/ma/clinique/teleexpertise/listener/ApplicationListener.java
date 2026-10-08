// package ma.clinique.teleexpertise.listener;

// import jakarta.servlet.ServletContextEvent;
// import jakarta.servlet.ServletContextListener;
// import jakarta.servlet.annotation.WebListener;

// import ma.clinique.teleexpertise.dao.*;
// import ma.clinique.teleexpertise;.service.UserService;

// @WebListener
// public class ApplicationListener implements ServletContextListener {

//     @Override
//     public void contextInitialized(ServletContextEvent event) {

//         UserDAO userDAO = new JdbcUserDAO();

//         UserService userService = new UserService(userDAO);

//         userService.initializeDefaultUsers();

//         System.out.println("user creer par defaut");
//     }
// }