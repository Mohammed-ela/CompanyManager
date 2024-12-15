import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Créez une instance de la page d'accueil
        HomePage homePage = new HomePage();

        // Appelez la méthode pour afficher la page
        homePage.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
