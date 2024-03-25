package lab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientController {
    int port = 3124;
    InetAddress ip = null;
    int My_id = 1;
    int P_n = 1;

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

    @FXML
    Button Start;

    //Circle S_blue;
    Circle[] cirs = new Circle[4];

    MyPair[] Sc_sh = new MyPair[4];

    int tg1p = 2;
    int tg2p = -1;

    boolean flag = false;
    boolean flag_r = false;

    Circle Move(Circle cir) {
        if (cir != null) {
            cir.setLayoutX(cir.getLayoutX() + 7);
        }

        return cir;
    }

    Circle upd_remove(Circle cir, int id) {
        plate.getChildren().remove(cir);
        cir = null;
        if (Sc_sh[id].sc >= 3) {
            //Stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(names[id].getText() + " WIN!!!");
            alert.showAndWait();
        } else if (Sc_sh[id].sh == 7) {
            Sc_sh[id].sh = 10;
            //Pause();
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

    Thread read_sc = new Thread(//!!sc
            () -> {
                try {
                    while(true) {
                        String msg = dis.readUTF();
                        System.out.println(msg);
                        switch (msg) {
                            case "sta": {
                                Start();
                                break;
                            }
                            case  "st": {
                                Stop();
                                break;
                            }
                            case "pa": {
                                Pause();
                                break;
                            }
                            default: {
                                int sc_id = Integer.parseInt(msg.substring(0, 1));
                                int ch_sc = Integer.parseInt(msg.substring(1, 2));
                                if(ch_sc == 3) {
                                    Sc_sh[sc_id].sh += 1;
                                    Platform.runLater(() -> {
                                        shootss[sc_id].setText(Integer.toString(Sc_sh[sc_id].sh));
                                        cirs_create(sc_id);//!!cir
                                    });
                                } else {
                                    Sc_sh[sc_id].sc += ch_sc;
                                    Platform.runLater(() -> {
                                        scores[sc_id].setText(Integer.toString(Sc_sh[sc_id].sc));
                                        cirs[sc_id] = upd_remove(cirs[sc_id], sc_id);//!!cir
                                    });
                                }
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
    );

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
                                cirs[id] = Move(cirs[id]);
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
                if (tg1p == 2) {
                    panes = new Pane[] {P, P1, P2, P3};
                    names = new Label[] {Name, Name1, Name2, Name3};
                    scores = new Label[] {Score, Score1, Score2, Score3};
                    shootss = new Label[] {Shoots, Shoots1, Shoots2, Shoots3};
                    tris = new Polygon[] {tri, tri1, tri2, tri3};

                    try {
                        ip = InetAddress.getLocalHost();
                        cs = new Socket(ip, port);
                        System.out.append("Client start \n");

                        dis = new DataInputStream(cs.getInputStream());
                        dos = new DataOutputStream(cs.getOutputStream());
                        P_n = Integer.parseInt(dis.readUTF());
                        My_id = P_n;
                        //names[0].setText(dis.readUTF());
                        Platform.runLater(() -> {
                            for (int id_i = 0; id_i < P_n; id_i++) {
                                try {
                                    names[id_i].setText(dis.readUTF());
                                } catch (IOException ex) {
                                    System.out.println("Error");
                                    System.exit(0);
                                }
                            }
                        });

                        //System.out.println("Host name: " + s);

                        TextInputDialog td = new TextInputDialog("****");
                        td.setHeaderText("Enter your name");
                        td.showAndWait();
                        String name_str = td.getEditor().getText();
                        boolean name_flag = true;
                        for(int id_i = 0; id_i < My_id; id_i++){
                            if (names[id_i].getText().equals(name_str)){
                                name_flag = false;
                            }
                        }
                        names[My_id].setText((name_str.length() <= 4 && name_flag) ? name_str : "P "+Integer.toString(My_id));
                        //System.out.println(names[My_id].getText());
                        dos.writeUTF(names[My_id].getText());

                        new Thread(()->{
                            try {
                                P_n = Integer.parseInt(dis.readUTF());
                                Platform.runLater(() -> {
                                    for (int id_i = My_id + 1; id_i < P_n; id_i++) {
                                        try {
                                            names[id_i].setText(dis.readUTF());
                                        } catch (IOException ex) {
                                            System.out.println("Error");
                                            System.exit(0);
                                        }
                                    }
                                });

                                for (int id = 0; id < P_n; id++) {
                                    cirs[id] = null;
                                }
                            }  catch (IOException ex) {
                                System.out.println("Server connect error");
                                System.exit(0);
                            }

                            for (int id = 0; id < P_n; id++) {
                                panes[id].setVisible(true);
                                tris[id].setVisible(true);
                            }


                            for (int id = 0; id < 4; id++) {
                                Sc_sh[id] = new MyPair();
                            }

                            read_sc.start();
                            st.start();
                        }).start();

                        tg1p = 1;
                        Start.setVisible(false);

                    } catch (IOException ex) {
                        System.out.println("Server connect error");
                        System.exit(0);
                    }
                } else {
                    flag = true;
                    notifyAll();
                }
            }
        }
    }

    @FXML
    protected void Ready() {
        if(!flag && !flag_r) {
            flag_r = true;
            try {
                dos.writeUTF("re");
                //System.out.println("re");
            } catch (IOException ex) {
                System.out.println("Server connect error");
                System.exit(0);
            }
        }
    }

    @FXML
    protected void Stop() {
        if(flag) {
            flag = false;
            flag_r = false;
            try {
                dos.writeUTF("st");
            } catch (IOException ex) {
                System.out.println("Server connect error");
                System.exit(0);
            }
            Platform.runLater(() -> {
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
            });
        }
    }

    @FXML
    protected synchronized void Pause() {
        if(flag) {
            flag = false;
            flag_r = false;
            try {
                dos.writeUTF("pa");
            } catch (IOException ex) {
                System.out.println("Server connect error");
                System.exit(0);
            }
        }
    }

    @FXML
    protected void Shoot() {
        //System.out.println(S_blue);
        if (flag && cirs[My_id] == null && Sc_sh[My_id].sh < 7) {
            Sc_sh[My_id].sh += 1;
            shootss[My_id].setText(Integer.toString(Sc_sh[My_id].sh));
            cirs_create(My_id);
            try {//!!
                dos.writeUTF("sh");
            } catch (IOException ex) {
                System.out.println("Server connect error");
                System.exit(0);
            }

        }
    }
}