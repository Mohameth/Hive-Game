package Vue;

import Controleur.Hive;
import javafx.application.Application;
import javafx.stage.Stage;


public class Vue extends Application {
    protected double width = 1280.0;
    protected double heigth = 720.0;
    protected static Hive controleur;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hive");
        SceneMain(primaryStage);
    }

    protected void SceneMain(Stage primaryStage){
        new VueMenuPrincipal(primaryStage);
    }

    protected void SceneSoloParam(Stage primaryStage) {
        new VueSolo(primaryStage);
    }

    protected void SceneTerrain(Stage primaryStage){
        new VueTerrain(primaryStage);
    }

    public static void initFenetre(String[] args, Hive c) {
        controleur = c;
        launch(args);
    }
}
