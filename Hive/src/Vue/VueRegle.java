package Vue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VueRegle extends Vue {
    private int numeroPageTuto = 0;

    public VueRegle(Stage primaryStage){
        boolean fs = primaryStage.isFullScreen();
        Label l = new Label(getLangStr("rule"));
        String[] urlImg = new String[20];
        l.setStyle("-fx-font-weight: bold;\n-fx-font-size: 100px;\n-fx-text-fill: white;");

        for (int x = 1; x < 14; x++) {
        urlImg[x - 1] = "rules/rule" + x + ".png";
        }

        ImageView img = new ImageView(new Image(urlImg[numeroPageTuto]));
        Button back = new Button(getLangStr("previous"));
        back.setPrefWidth(150);
        Label nbPage = new Label(" " + (numeroPageTuto + 1) + "/13");
        nbPage.setStyle("-fx-font-weight: bold;\n-fx-font-size: 1.1em;\n-fx-text-fill: white;");
        Button next = new Button(getLangStr("next"));
        next.setPrefWidth(150);
        Button retour = new Button(getLangStr("back"));
        back.setDisable(true);

        HBox h = new HBox(back, nbPage, next);
        h.setAlignment(Pos.CENTER);
        h.setSpacing(20);
        VBox v = new VBox(l, img, h, retour);
        v.prefHeightProperty().bind(primaryStage.heightProperty());
        v.prefWidthProperty().bind(primaryStage.widthProperty());
        v.setStyle("-fx-background-image: url(background.jpg); -fx-background-size: cover;");
        v.getStylesheets().add("Vue/button1.css");
        v.setAlignment(Pos.CENTER);
        v.setSpacing(15);

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            next.setDisable(false);
            img.setImage(changeImg(urlImg, false, nbPage));
            if (numeroPageTuto == 0) {
                back.setDisable(true);
            }
        });

        next.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            back.setDisable(false);
            img.setImage(changeImg(urlImg, true, nbPage));
            if (numeroPageTuto == 12) {
                next.setDisable(true);
            }
        });

        retour.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            SceneMain(primaryStage);
        });

        Scene s = new Scene(v,primaryStage.getWidth(),primaryStage.getHeight());

        primaryStage.setScene(s);
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ENTER,KeyCombination.ALT_DOWN));
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(fs);
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();


}

    private Image changeImg(String[] url, boolean next, Label l) {
        if (next && numeroPageTuto < 12) {
            numeroPageTuto++;
        } else if (!next && numeroPageTuto > 0) {
            numeroPageTuto--;
        }
        if (numeroPageTuto + 1 < 10) {
            l.setText(" " + (numeroPageTuto + 1) + "/13");
        } else {
            l.setText((numeroPageTuto + 1) + "/13");
        }
        return new Image(url[numeroPageTuto]);

    }
}
