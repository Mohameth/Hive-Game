package Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class VueSettings extends Vue {

    private static int NB_LIGNE = 3;
    private static int NB_COL = 7;
    private Stage primaryStage;
    private boolean inGame;
    private Group root;
    private GridPane g;
    private final ToggleGroup group = new ToggleGroup();
    private TextField nomJ1;
    private TextField nomJ2;
    private ComboBox<Point> cb;
    private ComboBox<String> cb1;
    private CheckBox chb;

    VueSettings(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.inGame = false;

        boolean fs = primaryStage.isFullScreen();
        Scene s = new Scene(getSetting(), primaryStage.getWidth(), primaryStage.getHeight());
        getConfig();
        s.getStylesheets().add("Vue/button1.css");
        primaryStage.setScene(s);
        primaryStage.setFullScreen(fs);
        primaryStage.show();
    }

    VueSettings(Stage primaryStage, boolean inGame, Group root){
        this.inGame = inGame;
        this.root = root;
        this.primaryStage = primaryStage;
    }

    public GridPane getSetting(){
        g = new GridPane();

        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(primaryStage.getWidth() / NB_COL, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(primaryStage.getHeight() / NB_LIGNE, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        g.getRowConstraints().get(1).setMinHeight((primaryStage.getHeight() / NB_LIGNE) / 2);
        VBox solo = getSolo();
        VBox multi = getMulti();
        GridPane all = getAll();
        g.add(solo, 2, 0, 3, 1);
        g.add(all, 2, 2, 3, 1);
        g.add(multi, 2, 1, 3, 1);

        g.prefHeightProperty().bind(primaryStage.heightProperty());
        g.prefWidthProperty().bind(primaryStage.widthProperty());
        g.setStyle("-fx-background-image: url(background.jpg);");
        g.getStylesheets().add("Vue/button1.css");

        return g;
    }

    private GridPane getAll() {
        GridPane gAll = new GridPane();
        for (int column = 0; column < 2; column++) {
            gAll.getColumnConstraints().add(new ColumnConstraints((primaryStage.getWidth() / NB_COL), Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true));
        }

        for (int row = 0; row < 4; row++) {
            gAll.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true));
        }

        gAll.getColumnConstraints().get(1).setHalignment(HPos.CENTER);

        Label t5 = new Label(getLangStr("all"));
        t5.setFont(Font.font(36));
        t5.setAlignment(Pos.TOP_LEFT);

        Text t6 = new Text(getLangStr("wSize"));
        this.cb = new ComboBox();
        cb.getItems().addAll(new Point(1920, 1080), new Point(1280, 720));
        cb.getSelectionModel().select(cb.getItems().get(0));
        cb.setConverter(new StringConverter<Point>() {
            @Override
            public String toString(Point object) {
                if (object == null) {
                    return null;
                }
                return object.x + "x" + object.y;
            }

            @Override
            public Point fromString(String string) {
                return null;
            }
        });

        Text t7 = new Text(getLangStr("language"));
        cb1 = new ComboBox();
        cb1.getItems().addAll(getLangStr("fr"), getLangStr("en"));
        cb1.getSelectionModel().select(getLangStr("fr"));

        HBox hb2 = new HBox();
        hb2.getChildren().addAll(t6, cb);
        hb2.setSpacing(10);

        chb = new CheckBox(getLangStr("fullscreen"));
        chb.setAlignment(Pos.TOP_CENTER);

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(t7, cb1);
        hb3.setSpacing(40);

        Button bSaveDef = new Button(getLangStr("save"));
        Button bCancel = new Button(getLangStr("cancel"));
        Button bSave = new Button(getLangStr("saveGame"));

        bCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(!inGame)
                SceneMain(primaryStage);
            else {
                root.getChildren().remove(g);
            }
        });

        bSaveDef.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            setConfig();
        });

        bSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (chb.isSelected()) {
                primaryStage.setFullScreen(true);
            } else {
                primaryStage.setFullScreen(false);
                primaryStage.setWidth(cb.getValue().x);
                primaryStage.setHeight(cb.getValue().y);
            }
            if (cb1.getValue().equals(getLangStr("fr"))) {
                language = "fr";
                country = "FR";
            } else if (cb1.getValue().equals(getLangStr("en"))) {
                language = "en";
                country = "US";
            }
            this.currentLocale = new Locale(this.language, this.country);
            this.messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
            SceneSettings(primaryStage);
        });

        HBox hb5 = new HBox();
        if (this.inGame) {
            hb5.getChildren().add(bSave);
        }
        hb5.getChildren().addAll(bCancel, bSaveDef);
        hb5.setSpacing(20);

        gAll.add(t5, 0, 0, 1, 1);
        gAll.add(hb2, 0, 1, 1, 1);
        gAll.add(hb3, 0, 2, 1, 1);
        gAll.add(chb, 1, 1, 1, 1);
        gAll.add(hb5, 0, 3, 2, 1);

        gAll.setPadding(new Insets(0, 20.0, 0, 20.0));
        gAll.setStyle("-fx-background-color : rgba(255, 255, 255, .7);-fx-border-color: black; -fx-border-width: 0 3 0 3;");

        return gAll;
    }

    private VBox getMulti() {
        Label t1 = new Label(getLangStr("multi"));
        t1.setFont(Font.font(36));

        Label t2 = new Label(getLangStr("pname"));

        Text t3 = new Text(getLangStr("white"));
        nomJ1 = new TextField();
        Text t4 = new Text(getLangStr("black"));
        nomJ2 = new TextField();

        HBox hb = new HBox();
        hb.getChildren().addAll(t3, nomJ1);
        hb.setSpacing(10);

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(t4, nomJ2);
        hb1.setSpacing(15);
        hb1.setPadding(new Insets(0, 0, 30, 0));
        hb1.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0;");

        VBox v = new VBox();
        v.getChildren().addAll(t1, t2, hb, hb1);
        v.setPadding(new Insets(0, 20.0, 0, 20.0));
        v.setStyle("-fx-background-color : rgba(255, 255, 255, .7);-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        v.setSpacing(10.0);

        return v;
    }

    private VBox getSolo() {
        Label t = new Label(getLangStr("solo"));
        t.setFont(Font.font(36));

        Text td = new Text(getLangStr("difficulte"));
        RadioButton rEasy = new RadioButton(getLangStr("easy"));
        rEasy.setUserData(getLangStr("easy"));
        RadioButton rMedium = new RadioButton(getLangStr("medi"));
        rMedium.setUserData(getLangStr("medi"));
        RadioButton rHard = new RadioButton(getLangStr("hard"));
        rHard.setUserData(getLangStr("hard"));
        rEasy.setToggleGroup(group);
        rMedium.setToggleGroup(group);
        rHard.setToggleGroup(group);
        rMedium.setSelected(true);

        VBox vb = new VBox();
        vb.getChildren().addAll(td, rEasy, rMedium, rHard);
        vb.setStyle("-fx-border-color: black; -fx-border-width: 0 0 3 0;");
        vb.setPadding(new Insets(0, 0, 30.0, 0));
        VBox.setMargin(vb, new Insets(0, 20, 0, 0));
        vb.setSpacing(5);

        VBox v = new VBox();
        v.getChildren().addAll(t, vb);
        v.setAlignment(Pos.CENTER_LEFT);
        v.setStyle("-fx-background-color : rgba(255, 255, 255, .7);-fx-border-color: black; -fx-border-width: 0 3 0 3;");
        v.setSpacing(20);
        v.setPadding(new Insets(0, 0, 0, 20));

        return v;
    }

    private void setConfig() {
        Properties prop = new Properties();
        String propFileName = System.getProperty("user.dir").concat("/Hive/rsc/config.properties");
        prop.setProperty("difficulteIA",group.getSelectedToggle().getUserData().toString());
        prop.setProperty("joueurBlanc",nomJ1.getText());
        prop.setProperty("joueurNoir",nomJ2.getText());
        prop.setProperty("langue",cb1.getValue());
        prop.setProperty("tailleFenetre",cb.getValue().x + "x" + cb.getValue().y);
        if(chb.isSelected())
            prop.setProperty("fullscreen","true");
        else
            prop.setProperty("fullscreen","false");
        try {
            prop.store(new FileWriter(propFileName),"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getConfig() {
        Properties prop = new Properties();
        String propFileName = System.getProperty("user.dir").concat("/Hive/rsc/config.properties");
        InputStream input = null;
        try {
            input = new FileInputStream(propFileName);
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }

        for(Toggle t : group.getToggles()) {
            if (t.getUserData().equals(prop.getProperty("difficulteIA")))
                group.selectToggle(t);
        }
        if(prop.getProperty("fullscreen").equals("true"))
            chb.setSelected(true);
        else
            chb.setSelected(false);
        nomJ1.setText(prop.getProperty("joueurBlanc",nomJ1.getText()));
        nomJ2.setText(prop.getProperty("joueurNoir",nomJ2.getText()));
        for (String s : cb1.getItems()){
            if(s.equals(prop.getProperty("langue")))
                cb1.getSelectionModel().select(s);
        }

        for(Point p : cb.getItems()){
            String s = p.x + "x" + p.y;
            if(s.equals(prop.getProperty("tailleFenetre")))
                cb.getSelectionModel().select(p);
        }

        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
