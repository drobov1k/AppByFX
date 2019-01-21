package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.animations.Shake;
import sample.source.SQLWritable;
import sample.source.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private Button authSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private Button showAllButton;

    @SuppressWarnings("ThrowableNotThrown")
    @FXML
    void initialize() {
        authSignInButton.setOnAction(event -> {
            String loginText = login_field.getText().trim();
            String loginPassword = pass_field.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals("")) {
                try {
                    loginUser(loginText, loginPassword);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else new OutOfMemoryError();
        });

        loginSignUpButton.setOnAction(event -> openNewScene("/AppLayers/signUp.fxml", loginSignUpButton));

        showAllButton.setOnAction(event -> {
            openNewScene("/AppLayers/view.fxml", showAllButton);
                });
     }

    private void loginUser(String loginText, String loginPassword) throws SQLException {
        SQLWritable writer = new SQLWritable();
        //DatabaseHandler  databaseHandler = new DatabaseHandler();
        User user = new User
                .UserBuilder(loginText,loginPassword)
                .build();

        ResultSet result = writer.getUser(user);

        int cout = 0;

        while (result.next()) {
            cout++;
        }

        if (cout>=1) {
            openNewScene("/AppLayers/app.fxml", authSignInButton);
        }
            else {
                Shake userLoginAnim = new Shake(login_field);
                Shake userPassAnim = new Shake(pass_field);
                userLoginAnim.playAnim();
                userPassAnim.playAnim();
        }
    }

    public void openNewScene (String window, Button button){
        button.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
