package Vue;

import Controleur.Hive;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Vue extends Application {
    protected double width = 1280.0;
    protected double heigth = 720.0;
    protected static Hive controleur;
    protected String language;
    protected String country;
    Locale currentLocale;
    public static ResourceBundle messages;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.language = "fr";
        this.country = "FR";
        this.currentLocale = new Locale(this.language, this.country);
        this.messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        primaryStage.setTitle("Hive");
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(heigth);
        primaryStage.setWidth(width);
        primaryStage.setHeight(heigth);
        SceneMain(primaryStage);
    }

    protected String getLangStr(String name) {
        String result = messages.getString(name);
        if(result != null){
            return result;
        }
        return name;
    }

    protected void SceneMain(Stage primaryStage){
        new VueMenuPrincipal(primaryStage);
    }

    protected void SceneSoloParam(Stage primaryStage) {
        new VueSolo(primaryStage);
    }

    protected void SceneTerrain(Stage primaryStage, int casJoueur){
        new VueTerrain(primaryStage, controleur, 1);
    }

    protected void SceneSettings(Stage primaryStage){ new VueSettings(primaryStage);}

    protected void SceneMulti(Stage primaryStage){ new VueMulti(primaryStage);}

    public static void initFenetre(String[] args, Hive c) {
        controleur = c;
        launch(args);
    }
}
