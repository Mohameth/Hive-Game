package Vue;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VueMenuPrincipal extends Vue {

    private static int NB_LIGNE = 5;
    private static int NB_COL = 5;

    VueMenuPrincipal(Stage primaryStage) {
        boolean fs = primaryStage.isFullScreen();

        Group root = new Group();
        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        scene.getStylesheets().add("Vue/button.css");

        Text t = new Text("Hive");
        t.setFont(Font.font(100.0));

        BorderPane bp = new BorderPane();
        bp.setCenter(t);

        Button solo = new Button();
        solo.setText(getLangStr("solo"));
        solo.setMaxWidth(200.0);

        solo.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneSoloParam(primaryStage);
        });

        Button jvj = new Button();
        jvj.setText(getLangStr("deuxjoueur"));
        jvj.setMaxWidth(200.0);

        jvj.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMulti(primaryStage);
        });

        Button loadGame = new Button(getLangStr("ldgame"));
        loadGame.setMaxWidth(200.0);

        Button tuto = new Button();
        tuto.setText(getLangStr("tuto"));
        tuto.setMaxWidth(200.0);

        Button setting = new Button();
        setting.setText(getLangStr("setting"));
        setting.setMaxWidth(200.0);

        setting.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneSettings(primaryStage);
        });

        Button quit = new Button();
        quit.setText(getLangStr("quitter"));
        quit.setMaxWidth(200.0);

        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            System.exit(0);
        });

        VBox vb = new VBox();
        vb.setSpacing(20.0);
        vb.getChildren().add(solo);
        vb.getChildren().add(jvj);
        vb.getChildren().add(loadGame);
        vb.getChildren().add(tuto);
        vb.getChildren().addAll(setting, quit);

        GridPane g = new GridPane();
        for (int column = 0; column < NB_COL; column++) {
            g.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
        }

        for (int row = 0; row < NB_LIGNE; row++) {
            g.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }

        g.prefHeightProperty().bind(scene.heightProperty());
        g.prefWidthProperty().bind(scene.widthProperty());
        g.add(bp,0,0,5,1);
        g.add(vb,4,2,1,5);
        g.setStyle("-fx-background-image: url(background.jpg);");

        root.getChildren().addAll(g);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(fs);
        primaryStage.show();
    }
}
