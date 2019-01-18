/*
Класс => singletone.
Приложение не является многопоточным,
поэтому использован самый простой способ создания одиночки.
В данном классе создается экземпляр подключения к БД:
метод getDbConnection
*/
package sample.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection extends Configs {
    private Connection connection;

    private static DBConnection instance;

    private DBConnection (){
        try {
            connection = DriverManager.getConnection(URL, log, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance(){
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getDbConnection(){
        return connection;
    }
}
