package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.source.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewController {
    private IWriter sqlWriter = new SQLWritable();
    private IWriter xmlWriter = new XMLWritable();

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

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        columnColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        tableUsers.setItems(usersData);

        removeUserButton.setOnAction(event -> {
            sqlWriter.removeUser(tableUsers.getSelectionModel().getSelectedItem());
            xmlWriter.removeUser(tableUsers.getSelectionModel().getSelectedItem());
            tableUsers.getItems().remove(tableUsers.getSelectionModel().getSelectedIndex());
        });
    }

    private void initData() {

        String query = "SELECT * FROM " + Const.USER_TABLE;

        try {
            Statement statement = DBConnection.getInstance().getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
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