package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ClientController {
    int My_id = 1;
    @FXML
    Pane P;
    @FXML
    private Pane P1;
    @FXML
    private Pane P2;
    @FXML
    private Pane P3;
    Pane[] panes;

    @FXML
    private Label Name;
    @FXML
    private Label Score;
    @FXML
    private Label Shoots;
    @FXML
    private Label Name1;
    @FXML
    private Label Score1;
    @FXML
    private Label Shoots1;
    @FXML
    private Label Name2;
    @FXML
    private Label Score2;
    @FXML
    private Label Shoots2;
    @FXML
    private Label Name3;
    @FXML
    private Label Score3;
    @FXML
    private Label Shoots3;
    Label[] names;
    Label[] scores;
    Label[] shootss;

    @FXML
    Pane plate;

    @FXML
    Polygon tri;
    @FXML
    Polygon tri1;
    @FXML
    Polygon tri2;
    @FXML
    Polygon tri3;
    Polygon[] tris;

    @FXML
    Line line1;
    @FXML
    Line line2;

    @FXML
    Circle target1;
    @FXML
    Circle target2;

    Circle S_blue;

    MyPair[] Sc_sh = new MyPair[4];
    //int score_val = 0;
    //int shoots_val = 0;

    int tg1p = 2;
    int tg2p = -1;

    boolean flag = false;

    Circle Move_and_target(Circle cir, int id) {
        if (cir != null) {
            cir.setLayoutX(cir.getLayoutX() + 7);

            if ((cir.getLayoutX() > (target1.getLayoutX() - target1.getRadius())) &
                    (cir.getLayoutX() < (target1.getLayoutX() + target1.getRadius())) &
                    (cir.getLayoutY() > (target1.getLayoutY() - target1.getRadius())) &
                    (cir.getLayoutY() < (target1.getLayoutY() + target1.getRadius()))) {
                plate.getChildren().remove(cir);
                Sc_sh[id].sc += 1;
                scores[id].setText(Integer.toString(Sc_sh[id].sc));
                cir = null;
            } else if ((cir.getLayoutX() > (target2.getLayoutX() - target2.getRadius())) &
                    (cir.getLayoutX() < (target2.getLayoutX() + target2.getRadius())) &
                    (cir.getLayoutY() > (target2.getLayoutY() - target2.getRadius())) &
                    (cir.getLayoutY() < (target2.getLayoutY() + target2.getRadius()))) {
                plate.getChildren().remove(cir);
                Sc_sh[id].sc += 2;
                scores[id].setText(Integer.toString(Sc_sh[id].sc));
                cir = null;
            } else if ((cir.getLayoutX() >= (400 - 0.7 * target2.getRadius()))) {
                plate.getChildren().remove(cir);
                cir = null;
            }
        } else if (Sc_sh[id].sc == 3) {
            Stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(names[id].getText() + " WIN!!!");
            alert.showAndWait();
        } else if (Sc_sh[id].sh == 7) {
            Stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(names[id].getText() + " LOSE!!!");
            alert.showAndWait();
        }

        return cir;
    }

    Thread st = new Thread(
            () -> {
                try {
                    while (true) {
                        //System.out.println("Hello world!");
                        if(!flag) {
                            synchronized (this) {
                                this.wait();
                            }
                        }
                        Platform.runLater(() -> {
                            if (target1.getLayoutY() <= (0 + target1.getRadius())) {
                                tg1p = 1;
                            } else if (target1.getLayoutY() >= (330 - target1.getRadius())) {
                                tg1p = -1;
                            }
                            if (target2.getLayoutY() <= (0 + 1.1 * target2.getRadius())) {
                                tg2p = 1;
                            } else if (target2.getLayoutY() >= (330 - 1.1 * target2.getRadius())) {
                                tg2p = -1;
                            }

                            S_blue = Move_and_target(S_blue, My_id);

                            target1.setLayoutY(target1.getLayoutY() + 2 * tg1p);
                            target2.setLayoutY(target2.getLayoutY() + 4 * tg2p);

                        });
                        Thread.sleep(25);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    );

    @FXML
    protected void Start() {
        if (!flag) {
            synchronized (this) {
                flag = true;
                S_blue = null;
                if (tg1p == 2) {
                    panes = new Pane[] {P, P1, P2, P3};
                    names = new Label[] {Name, Name1, Name2, Name3};
                    scores = new Label[] {Score, Score1, Score2, Score3};
                    shootss = new Label[] {Shoots, Shoots1, Shoots2, Shoots3};
                    tris = new Polygon[] {tri, tri1, tri2, tri3};

                    for (int id = 0; id <= My_id; id++) {
                        panes[id].setVisible(true);
                        tris[id].setVisible(true);
                    }
                    tg1p = 1;
                    for (int id = 0; id < 4; id++) {
                        Sc_sh[id] = new MyPair();
                    }
                    TextInputDialog td = new TextInputDialog("****");
                    td.setHeaderText("Enter your name");
                    td.showAndWait();
                    String name_str = td.getEditor().getText();
                    names[My_id].setText(name_str.length() < 4 ? name_str : name_str.substring(0, 4));
                    st.start();
                } else {
                    notifyAll();
                }
            }
        }
    }

    @FXML
    protected void Stop() {
        if(flag) {
            flag = false;
            if (S_blue != null) {
                plate.getChildren().remove(S_blue);
                S_blue = null;
            }
            for (int id_i = 0; id_i < Sc_sh.length; id_i++) {
                Sc_sh[id_i].sc = 0;
                Sc_sh[id_i].sh = 0;
            }
            tg1p = 1;
            tg2p = -1;
            scores[My_id].setText(Integer.toString(Sc_sh[My_id].sc));
            shootss[My_id].setText(Integer.toString(Sc_sh[My_id].sh));
            target1.setLayoutX(line1.getLayoutX() + line1.getStartX());
            target1.setLayoutY(tris[My_id].getLayoutY());
            target2.setLayoutX(line2.getLayoutX() + line2.getStartX());
            target2.setLayoutY(tris[My_id].getLayoutY());
        }
    }

    @FXML
    protected synchronized void Pause() {
        if(flag) {
            flag = false;
            if (S_blue != null) {
                plate.getChildren().remove(S_blue);
                S_blue = null;
            }
        }
    }

    @FXML
    protected void Shoot() {
        //System.out.println(S_blue);
        if (flag & S_blue == null) {
            S_blue = new Circle();
            S_blue.setStroke(tris[My_id].getFill());
            S_blue.setLayoutX(tris[My_id].getLayoutX());
            S_blue.setLayoutY(tris[My_id].getLayoutY());
            S_blue.setRadius(5);
            plate.getChildren().add(S_blue);
            Sc_sh[My_id].sh += 1;
            shootss[My_id].setText(Integer.toString(Sc_sh[My_id].sh));
        }
    }
}