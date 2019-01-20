package sample.source;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLWritable implements IWriter {
    private PreparedStatement prSt;
    private Statement st;

    @Override
    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USERS_FIRSTNAME + "," + Const.USERS_LASTNAME + "," +
                Const.USERS_USERNAME + "," + Const.USERS_PASSWORD + "," +
                Const.USERS_LOCATION + "," + Const.USERS_GENDER + ")" +
                "VALUES(?,?,?,?,?,?)";
        try {
            prSt = DBConnection.getInstance().getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getFirstName());
            prSt.setString(2, user.getLastName());
            prSt.setString(3, user.getUserName());
            prSt.setString(4, user.getPassword());
            prSt.setString(5, user.getLocation());
            prSt.setString(6, user.getGender());

            prSt.executeUpdate();

            String getid = "SELECT * FROM " + Const.USER_TABLE +
                    " ORDER BY id DESC LIMIT 1";

            st = DBConnection.getInstance().getDbConnection().createStatement();
            ResultSet res = st.executeQuery(getid);
            String id;
            if (res.next()) {
                id = String.valueOf(res.getInt("id"));
                user.setId(Integer.valueOf(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUser(User user) {
        String deleteQuery = "DELETE FROM " + Const.USER_TABLE
                + " WHERE " + Const.USERS_ID + " = " + user.getId();
        try {
            st = DBConnection.getInstance().getDbConnection().createStatement();
            st.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getDbConnection().prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            resSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }
}
