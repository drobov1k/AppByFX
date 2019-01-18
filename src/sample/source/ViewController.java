package sample.source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewController {
    DatabaseHandler handler = null;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    @FXML
    private TableView<User> tableUsers;

    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> columnColumn;

    @FXML
    private Button removeUserButton;

    @FXML
    private void initialize() {
        initData();

        id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<User, String>("location"));

        tableUsers.setItems(usersData);

        removeUserButton.setOnAction(event ->{
            try {
                handler.removeUser(tableUsers.getSelectionModel().getSelectedItem());
                tableUsers.getItems().remove(tableUsers.getSelectionModel().getSelectedIndex());
            }  catch (ParserConfigurationException | IOException | SAXException | SQLException | TransformerException e) {
                e.printStackTrace();
            }
        });
    }

    private void initData() {

        String query = "SELECT * FROM " + Const.USER_TABLE;

        try {
            Statement statement = DBConnection.getInstance().getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                usersData.add(new User.UserBuilder(resultSet.getString("username"), resultSet.getString("password"))
                        .firstName(resultSet.getString("firstname"))
                        .lastName(resultSet.getString("lastname"))
                        .location(resultSet.getString("location"))
                        .gender(resultSet.getString("gender"))
                        .id(resultSet.getInt(1))
                        .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}