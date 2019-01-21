package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.animations.Shake;
import sample.source.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController {
    private IWriter sqlWriter = new SQLWritable();
    private IWriter xmlWriter = new XMLWritable();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpName;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private TextField signUpLocation;

    @FXML
    private CheckBox signUpCheckBoxMale;

    @FXML
    private CheckBox signUpCheckBoxFemale;

    @FXML
    void initialize() {
        signUpButton.setOnAction(event -> signUpNewUser());
    }

    private void signUpNewUser() {
        String firstName = signUpName.getText();
        String lastName = signUpLastName.getText();
        String userName = login_field.getText();
        String password = pass_field.getText();
        String location = signUpLocation.getText();
        String gender;

        if (signUpCheckBoxMale.isSelected())
            gender = "Male";
        else gender = "Female";

        if (checkCorrectInput(signUpName, signUpLastName, login_field, pass_field, signUpLocation) == true) {
            User user = new User.UserBuilder(userName, password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .location(location)
                    .gender(gender)
                    .build();

            sqlWriter.signUpUser(user);
            xmlWriter.signUpUser(user);
            Controller controller = new Controller();
            controller.openNewScene("/AppLayers/sample.fxml", signUpButton);
        } else {
            signUpButton.setOnAction(event -> AlertWindow.display("Warning", "Вы ввели не все поля.\nПожалуйста, заполните все поля."));
        }
    }

    private boolean checkCorrectInput(TextField... str) {
        boolean flag = true;
        for (int i = 0; i < str.length; i++) {
            if (str[i].getText().equals("")) {
                Shake fieldShake = new Shake(str[i]);
                fieldShake.playAnim();
                flag = false;
            }
        }
        return flag;
    }
}