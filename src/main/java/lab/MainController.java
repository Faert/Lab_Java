package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


public class MainController {
    int score_val = 0;
    int shoots_val = 0;

    int tg1p = 2;
    int tg2p = -1;

    boolean flag = false;

    @FXML
    private Label Score;

    @FXML
    private Label Shoots;

    @FXML
    Pane plate;

    @FXML
    Polygon tri;

    @FXML
    Line line1;
    @FXML
    Line line2;

    @FXML
    Circle target1;
    @FXML
    Circle target2;

    Circle S_blue;

    Thread st = new Thread(
            () -> {
                while (true) {
                    if(flag) {
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

                            if (S_blue != null) {
                                S_blue.setLayoutX(S_blue.getLayoutX() + 7);

                                if ((S_blue.getLayoutX() > (target1.getLayoutX() - target1.getRadius())) &
                                        (S_blue.getLayoutX() < (target1.getLayoutX() + target1.getRadius())) &
                                        (S_blue.getLayoutY() > (target1.getLayoutY() - target1.getRadius())) &
                                        (S_blue.getLayoutY() < (target1.getLayoutY() + target1.getRadius()))) {
                                    plate.getChildren().remove(S_blue);
                                    S_blue = null;
                                    score_val += 1;
                                    Score.setText(Integer.toString(score_val));
                                } else if ((S_blue.getLayoutX() > (target2.getLayoutX() - target2.getRadius())) &
                                        (S_blue.getLayoutX() < (target2.getLayoutX() + target2.getRadius())) &
                                        (S_blue.getLayoutY() > (target2.getLayoutY() - target2.getRadius())) &
                                        (S_blue.getLayoutY() < (target2.getLayoutY() + target2.getRadius()))) {
                                    plate.getChildren().remove(S_blue);
                                    S_blue = null;
                                    score_val += 2;
                                    Score.setText(Integer.toString(score_val));
                                } else if ((S_blue.getLayoutX() >= (400 - 0.7 * target2.getRadius()))) {
                                    plate.getChildren().remove(S_blue);
                                    S_blue = null;
                                }
                            }

                            target1.setLayoutY(target1.getLayoutY() + 2 * tg1p);
                            target2.setLayoutY(target2.getLayoutY() + 4 * tg2p);

                        });
                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    @FXML
    protected void Start() {
        if (tg1p == 2) {
            tg1p = 1;
            st.start();
        }

        if (!flag) {
            flag = true;
            S_blue = null;
            Score.setText(Integer.toString(score_val));
            Shoots.setText(Integer.toString(shoots_val));
        }
    }

    @FXML
    protected void Stop() {
        if(flag) {
            if (S_blue != null) {
                plate.getChildren().remove(S_blue);
                S_blue = null;
            }
            flag = false;
            score_val = 0;
            shoots_val = 0;
            tg1p = 1;
            tg2p = -1;
            Score.setText(Integer.toString(score_val));
            Shoots.setText(Integer.toString(shoots_val));
            target1.setLayoutX(line1.getLayoutX()+line1.getStartX());
            target1.setLayoutY(tri.getLayoutY());
            target2.setLayoutX(line2.getLayoutX()+line2.getStartX());
            target2.setLayoutY(tri.getLayoutY());
        }
    }

    @FXML
    protected void Pause() {
        if(flag) {
            if (S_blue != null) {
                plate.getChildren().remove(S_blue);
                S_blue = null;
            }
            flag = false;
        }
    }

    @FXML
    protected void Shoot() {
        if (flag & S_blue == null) {
            S_blue = new Circle();
            S_blue.setStroke(Color.BLUE);
            S_blue.setLayoutX(tri.getLayoutX());
            S_blue.setLayoutY(tri.getLayoutY());
            S_blue.setRadius(5);
            plate.getChildren().add(S_blue);
            shoots_val += 1;
            Shoots.setText(Integer.toString(shoots_val));
        }
    }
}