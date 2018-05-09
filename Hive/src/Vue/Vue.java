package Vue;

import Controleur.Controleur;
import javafx.application.Application;
import javafx.stage.Stage;


public class Vue extends Application {
    protected double width = 1280.0;
    protected double heigth = 720.0;
    protected static Controleur controleur;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hive");
        primaryStage.setMinHeight(heigth);
        primaryStage.setMinWidth(width);
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

    protected void SceneSettings(Stage primaryStage){ new VueSettings(primaryStage);}

    public static void initFenetre(String[] args, Controleur c) {
        controleur = c;
        launch(args);
    }
}
