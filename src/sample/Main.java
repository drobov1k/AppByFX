/*

 *@author Eugeniy Dubovik
 *@version 1.8
 *
 *
*/

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../AppLayers/sample.fxml"));
        primaryStage.setTitle("Course");
        primaryStage.getIcons().add(new Image("sample/accets/icon.png"));
        primaryStage.setScene(new Scene(root, 700, 400));
        primaryStage.show();
    }
    
    public static void main(String... args) {
        launch(args);
    }
}
