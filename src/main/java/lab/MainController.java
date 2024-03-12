package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainController {
    int port = 3124;
    InetAddress ip = null;
    int My_id = 0;

    int P_n = 1;

    ServerSocket ss;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;

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

    //Circle S_blue;
    Circle[] cirs = new Circle[4];

    MyPair[] Sc_sh = new MyPair[4];
    //int score_val = 0;
    //int shoots_val = 0;

    int tg1p = 2;
    int tg2p = -1;

    boolean flag = false;

    Circle Move_and_target(Circle cir, int id) {
        if (cir != null) {
            cir.setLayoutX(cir.getLayoutX() + 7);
            try {
                if ((cir.getLayoutX() > (target1.getLayoutX() - target1.getRadius())) &
                        (cir.getLayoutX() < (target1.getLayoutX() + target1.getRadius())) &
                        (cir.getLayoutY() > (target1.getLayoutY() - target1.getRadius())) &
                        (cir.getLayoutY() < (target1.getLayoutY() + target1.getRadius()))) {
                    plate.getChildren().remove(cir);
                    Sc_sh[id].sc += 1;
                    scores[id].setText(Integer.toString(Sc_sh[id].sc));
                    cir = null;
                    //System.out.println(Integer.toString(id) + Integer.toString(1));
                    dos.writeUTF(Integer.toString(id) + Integer.toString(1));
                } else if ((cir.getLayoutX() > (target2.getLayoutX() - target2.getRadius())) &
                        (cir.getLayoutX() < (target2.getLayoutX() + target2.getRadius())) &
                        (cir.getLayoutY() > (target2.getLayoutY() - target2.getRadius())) &
                        (cir.getLayoutY() < (target2.getLayoutY() + target2.getRadius()))) {
                    plate.getChildren().remove(cir);
                    Sc_sh[id].sc += 2;
                    scores[id].setText(Integer.toString(Sc_sh[id].sc));
                    cir = null;
                    dos.writeUTF(Integer.toString(id) + Integer.toString(2));
                } else if ((cir.getLayoutX() >= (400 - 0.7 * target2.getRadius()))) {
                    plate.getChildren().remove(cir);
                    cir = null;
                    dos.writeUTF(Integer.toString(id) + Integer.toString(0));
                }
            }  catch (IOException ex) {
                System.out.println("Error");
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

    void cirs_create(int id) {
        cirs[id] = new Circle();
        cirs[id].setStroke(tris[id].getFill());
        cirs[id].setLayoutX(tris[id].getLayoutX());
        cirs[id].setLayoutY(tris[id].getLayoutY());
        cirs[id].setRadius(5);
        plate.getChildren().add(cirs[id]);
    }

    Thread read_sh = new Thread(
            () -> {
                try {
                    while(true) {
                        int sh_id = Integer.parseInt(dis.readUTF());
                        Sc_sh[sh_id].sh += 1;
                        Platform.runLater(() -> {
                            shootss[sh_id].setText(Integer.toString(Sc_sh[sh_id].sh));
                            cirs_create(sh_id);
                        });
                    }
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
    );

    /*Thread write_sc = new Thread(
            () -> {
                try {
                    while(true) {
                        int sh_id = Integer.parseInt(dis.readUTF());
                        Sc_sh[sh_id].sh += 1;
                        Platform.runLater(() -> {
                            shootss[sh_id].setText(Integer.toString(Sc_sh[sh_id].sh));
                        });
                    }
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
    );*/
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

                            for (int id = 0; id < P_n; id++) {
                                cirs[id] = Move_and_target(cirs[id], id);
                            }

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
                if (tg1p == 2) {
                    panes = new Pane[] {P, P1, P2, P3};
                    names = new Label[] {Name, Name1, Name2, Name3};
                    scores = new Label[] {Score, Score1, Score2, Score3};
                    shootss = new Label[] {Shoots, Shoots1, Shoots2, Shoots3};
                    tris = new Polygon[] {tri, tri1, tri2, tri3};

                    TextInputDialog td = new TextInputDialog("****");
                    td.setHeaderText("Enter your name");
                    td.showAndWait();
                    String name_str = td.getEditor().getText();
                    names[My_id].setText(name_str.length() < 4 ? name_str : name_str.substring(0, 4));

                    try {
                        ip = InetAddress.getLocalHost();
                        ss = new ServerSocket(port, 0, ip);
                        System.out.append("Server start\n");

                        cs = ss.accept();//!!!4 and while flag
                        System.out.println("Client connect (" + cs.getPort() + ")");
                        P_n +=1;

                        dis = new DataInputStream(cs.getInputStream());
                        dos = new DataOutputStream(cs.getOutputStream());

                        //String s = dis.readUTF();
                        //System.out.println("Connect: " + s);

                        dos.writeUTF(Integer.toString(P_n));
                        dos.writeUTF(names[My_id].getText());//!!!
                        names[1].setText(dis.readUTF());

                        for (int id = 0; id < P_n; id++) {
                            cirs[id] = null;
                        }

                        read_sh.start();

                    } catch (IOException ex) {
                        System.out.println("Error");
                    }

                    for (int id = 0; id < P_n; id++) {
                        panes[id].setVisible(true);
                        tris[id].setVisible(true);
                    }
                    tg1p = 1;
                    for (int id = 0; id < 4; id++) {
                        Sc_sh[id] = new MyPair();
                    }

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
            for (int id_i = 0; id_i < P_n; id_i++) {
                if (cirs[id_i] != null) {
                    plate.getChildren().remove(cirs[id_i]);
                    cirs[id_i] = null;
                }
                Sc_sh[id_i].sc = 0;
                Sc_sh[id_i].sh = 0;
                scores[id_i].setText(Integer.toString(Sc_sh[id_i].sc));
                shootss[id_i].setText(Integer.toString(Sc_sh[id_i].sh));
            }
            tg1p = 1;
            tg2p = -1;
            target1.setLayoutX(line1.getLayoutX() + line1.getStartX());
            target1.setLayoutY(tris[0].getLayoutY());
            target2.setLayoutX(line2.getLayoutX() + line2.getStartX());
            target2.setLayoutY(tris[0].getLayoutY());
        }
    }

    @FXML
    protected synchronized void Pause() {
        if(flag) {
            flag = false;
            for (int id_i = 0; id_i < P_n; id_i++) {
                if (cirs[id_i] != null) {
                    plate.getChildren().remove(cirs[id_i]);
                    cirs[id_i] = null;
                }
            }
        }
    }
    @FXML
    protected void Shoot() {
        //System.out.println(S_blue);
        if (flag & cirs[My_id] == null) {
            Sc_sh[My_id].sh += 1;
            shootss[My_id].setText(Integer.toString(Sc_sh[My_id].sh));
            cirs_create(My_id);
            try {//!!
                dos.writeUTF(Integer.toString(My_id) + Integer.toString(3));
            } catch (IOException ex) {
                System.out.println("Error");
            }
        }
    }
}