package Vue;

import Controleur.Hive;
import hive.HiveMain;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        Properties prop = new Properties();
        String path;
        path = "rsc";
        File rep = new File(path);
        if (!rep.exists()) {
            rep.mkdir();
            System.out.println("generation rsc");
        }
        path = "rsc/config.properties";
        InputStream in = new FileInputStream(path);
        File config = new File(path);
        if (!config.exists()) {
            try {
                Files.copy(in , config.toPath() , StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(HiveMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("generation rsc/config.properties");
        }

        prop.load(in);
        String dim = prop.getProperty("tailleFenetre");
        String s[] = dim.split("x");
        width = Integer.parseInt(s[0]);
        heigth = Integer.parseInt(s[1]);
        if(prop.get("langue").equals("Français") || prop.get("langue").equals("French")) {
            this.language = "fr";
            this.country = "FR";
        } else {
            language = "en";
            country = "US";
        }
        this.currentLocale = new Locale(this.language, this.country);
        this.messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        primaryStage.setTitle("Hive");
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(heigth);
        primaryStage.setWidth(width);
        primaryStage.setHeight(heigth);
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ENTER,KeyCombination.ALT_DOWN));
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(Boolean.valueOf(prop.getProperty("fullscreen")));
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

    protected void SceneTerrain(Stage primaryStage, int casJoueur, boolean solo, boolean load){
        if (!load) {
            this.controleur.resetPartie();
        }
        new VueTerrain(primaryStage, controleur, 8, true, false);
    }

    protected void SceneRegle(Stage primaryStage){ new  VueRegle(primaryStage);}

    protected void SceneSettings(Stage primaryStage){
        new VueSettings(primaryStage);
    }

    protected void SceneLoad(Stage primaryStage){ new VueLoad(primaryStage);}

    protected void SceneMulti(Stage primaryStage){ new VueMulti(primaryStage);}

    public static void initFenetre(String[] args, Hive c) {
        controleur = c;
        launch(args);
    }
}
