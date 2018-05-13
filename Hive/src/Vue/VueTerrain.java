package Vue;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class VueTerrain extends Vue {
    private Group root;
    private Stage primaryStage;

    VueTerrain(Stage primaryStage){
        this.primaryStage = primaryStage;
        root = new Group();
        BorderPane b = new BorderPane();
        b.setTop(getHudHaut());
        b.setBottom(getHudBas());
        b.setRight(getHudDroite());
        b.setLeft(getHudGauche());

        root.getChildren().add(b);
        Scene s = new Scene(root,primaryStage.getWidth(),primaryStage.getHeight());
        b.prefWidthProperty().bind(s.widthProperty());
        b.prefHeightProperty().bind(s.heightProperty());
        primaryStage.setScene(s);
        primaryStage.show();
    }

    private BorderPane getHudBas(){
        Button bEdit = new Button();
        bEdit.setGraphic(new ImageView(new Image("Vue/pencil.png")));
        bEdit.setStyle("-fx-background-color: Transparent;\n");
        Text txt1 = new Text("Point du joueur 1");

        HBox hName = new HBox();
        hName.setAlignment(Pos.CENTER_LEFT);
        hName.getChildren().addAll(bEdit,txt1);
        hName.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");

        HBox pointJ1 = new HBox();
        Piece p2 = new Piece("Vue/piontr_black_abeille.png",pointJ1,width,heigth);
        pointJ1.getChildren().addAll(hName);
        pointJ1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");

        bEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            TextInputDialog ti = new TextInputDialog(txt1.getText());
            ti.setHeaderText("Enter your name");
            Optional<String> result = ti.showAndWait();
            if(result.isPresent()){
                txt1.setText(result.get());
            }
        });

        BorderPane b = new BorderPane();
        b.setLeft(hName);
        b.setCenter(pointJ1);
        return b;
    }

    private BorderPane getHudHaut(){
        Button bEdit = new Button();
        bEdit.setGraphic(new ImageView(new Image("Vue/pencil.png")));
        bEdit.setStyle("-fx-background-color: Transparent;\n");
        Text txt = new Text("joueur 2");

        HBox hName = new HBox();
        hName.setAlignment(Pos.CENTER_LEFT);
        hName.getChildren().addAll(bEdit,txt);
        hName.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 0 0 3 0;\n");

        HBox pointJ2 = new HBox();
        //Piece p = new Piece("Vue/piontr_black_abeille.png",pointJ2,width,heigth);
        pointJ2.getChildren().addAll(hName);
        pointJ2.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 0 0 3 0;\n");

        bEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            TextInputDialog ti = new TextInputDialog(txt.getText());
            ti.setHeaderText("Enter your name");
            Optional<String> result = ti.showAndWait();
            if(result.isPresent()){
                txt.setText(result.get());
            }
        });

        BorderPane b = new BorderPane();
        b.setLeft(hName);
        b.setCenter(pointJ2);
        return b;
    }

    private BorderPane getHudDroite(){

        Button bPause = new Button();

        bPause.setGraphic(new ImageView(new Image("Vue/pause.png")));
        bPause.setMinSize(100,100);
        bPause.setStyle("-fx-background-color: Transparent;\n");

        Button bSave = new Button();
        Button bLoad = new Button();

        bSave.setGraphic(new ImageView(new Image("Vue/diskette.png")));
        bSave.setMinSize(32,32);
        bSave.setStyle("-fx-background-color: Transparent;\n");
        bLoad.setGraphic(new ImageView(new Image("Vue/upload.png")));
        bLoad.setMinSize(32,32);
        bLoad.setStyle("-fx-background-color: Transparent;\n");

        HBox hb = new HBox();
        hb.getChildren().addAll(bSave,bLoad);

        Button bUndo = new Button();
        Button bRedo = new Button();
        Button bSug = new Button();

        bUndo.setGraphic(new ImageView(new Image("Vue/icon.png")));
        bUndo.setMinSize(32,32);
        bUndo.setStyle("-fx-background-color: Transparent;\n");
        bRedo.setGraphic(new ImageView(new Image("Vue/redo-arrow.png")));
        bRedo.setMinSize(32,32);
        bRedo.setStyle("-fx-background-color: Transparent;\n");
        bSug.setGraphic(new ImageView(new Image("Vue/small-light-bulb.png")));
        bSug.setMinSize(32,32);
        bSug.setStyle("-fx-background-color: Transparent;\n");

        HBox hb1 = new HBox();
        hb1.getChildren().addAll(bUndo,bRedo);

        VBox vb1 = new VBox();
        vb1.getChildren().addAll(hb1,bSug);
        vb1.setSpacing(10);
        vb1.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 3 0 0 0;\n");
        vb1.setAlignment(Pos.BOTTOM_CENTER);

        VBox vb = new VBox();
        vb.getChildren().addAll(bPause,hb);
        vb.setSpacing(50);

        BorderPane pDroite = new BorderPane();
        pDroite.setTop(vb);
        pDroite.setBottom(vb1);
        pDroite.setStyle("-fx-border-color:black;\n" + "-fx-border-width: 0 0 0 3;\n");

        bPause.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            getPause();;
        });

        return pDroite;
    }

    private BorderPane getHudGauche(){

        Button bZoom = new Button();
        Button bDeZoom = new Button();

        bZoom.setGraphic(new ImageView(new Image("Vue/zoom-in.png")));
        bZoom.setStyle("-fx-background-color: Transparent;\n");
        bZoom.setMinSize(32,32);
        bDeZoom.setGraphic(new ImageView(new Image("Vue/zoom-out.png")));
        bDeZoom.setMinSize(32,32);
        bDeZoom.setStyle("-fx-background-color: Transparent;\n");

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(bZoom,bDeZoom);

        Button bUp = new Button();
        Button bLeft = new Button();
        Button bDown = new Button();
        Button bRight = new Button();

        bUp.setGraphic(new ImageView(new Image("Vue/up-arrowhead-in-a-circle.png")));
        bUp.setStyle("-fx-background-color: Transparent;\n");
        bUp.setMinSize(24,24);
        bRight.setGraphic(new ImageView(new Image("Vue/right-arrow-in-circular-button.png")));
        bRight.setMaxSize(24,24);
        bRight.setStyle("-fx-background-color: Transparent;\n");
        bDown.setGraphic(new ImageView(new Image("Vue/down-arrowhead-in-a-circle.png")));
        bDown.setMaxSize(24,24);
        bDown.setStyle("-fx-background-color: Transparent;\n");
        bLeft.setGraphic(new ImageView(new Image("Vue/left-arrow-in-circular-button.png")));
        bLeft.setMaxSize(24,24);
        bLeft.setStyle("-fx-background-color: Transparent;\n");
        ImageView img = new ImageView(new Image("Vue/target.png"));

        HBox hb = new HBox();
        hb.getChildren().addAll(bLeft,img,bRight);

        VBox vb = new VBox();
        vb.getChildren().addAll(bUp,hb,bDown);
        vb.setAlignment(Pos.CENTER);

        BorderPane bgauche = new BorderPane();
        bgauche.setTop(hb3);
        bgauche.setBottom(vb);

        return bgauche;
    }

    public void getPause(){
        Rectangle r = new Rectangle(width,heigth,Color.BLACK);
        r.setOpacity(0.5);

        Text t = new Text("PAUSE");
        t.setFont(Font.font(60));
        t.setStyle("-fx-fill: white;\n");
        Button bResume = new Button("Resume");
        bResume.setMaxWidth(200);
        Button bRules = new Button("Rules");
        bRules.setMaxWidth(200);
        Button bRestart = new Button("Restart");
        bRestart.setMaxWidth(200);
        Button bSettings = new Button("Settings");
        bSettings.setMaxWidth(200);
        Button bMain = new Button("Back main menu");
        bMain.setMaxWidth(200);
        Button bQuit = new Button("Quit");
        bQuit.setMaxWidth(200);

        VBox menu = new VBox();
        menu.getChildren().addAll(t,bResume,bRules,bRestart,bSettings,bMain,bQuit);
        menu.setMinSize(width,heigth);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);
        menu.getStylesheets().add("Vue/button.css");

        bResume.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            root.getChildren().removeAll(menu,r);
        });

        bMain.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want back to main menu ?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes){
                new VueMenuPrincipal(this.primaryStage);
            }
        });

        bQuit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want quit the game ?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes){
                System.exit(0);
            }
        });

        root.getChildren().addAll(r,menu);
    }
}
