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
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    int port = 3124;
    InetAddress ip = null;
    int My_id = 0;

    int P_n = 1;

    int P_act = 1;

    int ready = 0;

    ServerSocket ss;


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
    Button DBout;

    //Circle S_blue;
    Circle[] cirs = new Circle[4];

    //MyPair[] Sc_sh = new MyPair[4];

    int tg1p = 2;
    int tg2p = -1;

    boolean flag = false;
    boolean flag_r = false;

    Server_Client[] S_c = new Server_Client[4];
    Socket soc = null;

    ScoreDAO dao;


    class Server_Client extends Thread {
        Socket cs;
        DataInputStream dis;
        DataOutputStream dos;

        Score data;

        int id = -1;

        public Server_Client(Socket socket, int id_) throws IOException {
            if(id_ != 0) {
                cs = socket;
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                id = id_;
                data = new Score();
                start();
            } else {
                cs = null;
                dis = null;
                dos = null;
                id = 0;
                data = new Score();
            }
        }

        @Override
        public void run() {
            try {
                while(true) {
                    String msg = dis.readUTF();
                    System.out.println(msg);
                    if (!names[id].getText().equals("None")) {
                        switch (msg) {
                            case "sh": {
                                //Sc_sh[id].sh += 1;
                                data.shoots += 1;
                                Platform.runLater(() -> {
                                    //shootss[id].setText(Integer.toString(Sc_sh[id].sh));
                                    shootss[id].setText(Integer.toString(data.shoots));
                                    cirs_create(id);
                                });
                                for (int id_i = 1; id_i<P_n; id_i++) {
                                    if(id_i != id) {
                                        S_c[id_i].send(id + Integer.toString(3));
                                    }
                                }
                                break;
                            }
                            case "re": {
                                ready += 1;
                                Platform.runLater(() -> {
                                    names[id].setUnderline(true);
                                });
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
                        }
                    } else {
                        Platform.runLater(() -> {
                            names[id].setText(msg);
                            data.Name = msg;
                        });
                    }
                }
            } catch (IOException ex) {
                System.out.println("Lost connect with " + Integer.toString(id));
                    Platform.runLater(() -> {
                        if (id != -1) {
                            panes[id].setVisible(false);
                            tris[id].setVisible(false);
                            if (cirs[id] != null) {
                                plate.getChildren().remove(cirs[id]);
                                cirs[id] = null;
                            }
                            //Sc_sh[id].sc = 0;
                            //Sc_sh[id].sh = 0;
                            //scores[id].setText(Integer.toString(Sc_sh[id].sc));
                            //shootss[id].setText(Integer.toString(Sc_sh[id].sh));
                            data.score = 0;
                            data.shoots = 0;
                            scores[id].setText(Integer.toString(data.score));
                            shootss[id].setText(Integer.toString(data.shoots));
                        }
                    });
                id = -1;
                P_act -= 1;
                //cs.close();
            }
        }

        private void send(String st) {
            if(id != -1) {
                try {
                    dos.writeUTF(st);
                } catch (IOException ex) {
                    System.out.println("Lost connect with " + Integer.toString(id));
                }
            }
        }

    }

    Circle Move_and_target(Circle cir, int id) {
        if (cir != null) {
            cir.setLayoutX(cir.getLayoutX() + 7);
            if (id != 0 && (S_c[id] == null || S_c[id].id == -1)) {
                plate.getChildren().remove(cir);
                cir = null;
            } else if ((cir.getLayoutX() > (target1.getLayoutX() - target1.getRadius())) &&
                    (cir.getLayoutX() < (target1.getLayoutX() + target1.getRadius())) &&
                    (cir.getLayoutY() > (target1.getLayoutY() - target1.getRadius())) &&
                    (cir.getLayoutY() < (target1.getLayoutY() + target1.getRadius()))) {
                plate.getChildren().remove(cir);
                S_c[id].data.score += 1;
                scores[id].setText(Integer.toString(S_c[id].data.score));
                cir = null;
                for (int id_i = 1; id_i < P_n; id_i++) {
                    S_c[id_i].send(Integer.toString(id) + Integer.toString(1));
                }
            } else if ((cir.getLayoutX() > (target2.getLayoutX() - target2.getRadius())) &&
                    (cir.getLayoutX() < (target2.getLayoutX() + target2.getRadius())) &&
                    (cir.getLayoutY() > (target2.getLayoutY() - target2.getRadius())) &&
                    (cir.getLayoutY() < (target2.getLayoutY() + target2.getRadius()))) {
                plate.getChildren().remove(cir);
                S_c[id].data.score += 2;
                scores[id].setText(Integer.toString(S_c[id].data.score));
                cir = null;
                for (int id_i = 1; id_i < P_n; id_i++) {
                    S_c[id_i].send(Integer.toString(id) + Integer.toString(2));
                }
            } else if ((cir.getLayoutX() >= (400 - 0.7 * target2.getRadius()))) {
                plate.getChildren().remove(cir);
                cir = null;
                for (int id_i = 1; id_i < P_n; id_i++) {
                    S_c[id_i].send(Integer.toString(id) + Integer.toString(0));
                }
            }
        } else if (S_c[id].data.score >= 3) {
            Stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(names[id].getText() + " WIN!!!");
            alert.showAndWait();
        } else if (S_c[id].data.shoots == 7) {
            S_c[id].data.shoots = 10;
            Pause();
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
    Thread st = new Thread(
            () -> {
                System.out.append("Server back start\n");
                try {
                    while (true) {
                        if(!flag) {// || ready < P_n
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

    Thread connect_s = new Thread(()->{
        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 3, ip);
            S_c[0] = new Server_Client(null, 0);
            S_c[0].data.Name = names[0].getText();
            System.out.append("Server start\n");

            while (ready < P_act & P_n < 4) {
                try {
                    soc = ss.accept();
                    S_c[P_n] = new Server_Client(soc, P_n);
                    System.out.println("Client connect (" + S_c[P_n].cs.getPort() + ")");
                    S_c[P_n].send(Integer.toString(P_n));
                    for (int id_i = 0; id_i < P_n; id_i++) {
                        S_c[P_n].send(names[id_i].getText());
                    }
                    cirs[P_n] = null;
                    panes[P_n].setVisible(true);
                    tris[P_n].setVisible(true);
                    P_n += 1;
                    P_act += 1;
                } catch (IOException ex) {
                    System.out.println("End client connect");
                    //soc.close();
                }
            }

        } catch (Exception ex) {
            System.out.println("End client connect");
            //System.exit(0);
        }

        boolean temp_flag = true;
        while(temp_flag) {
            temp_flag = false;
            for(int id_i = 1; id_i < P_n; id_i++) {
                if(names[id_i].getText().equals("None")) {
                    temp_flag = true;
                    break;
                }
            }
        }

        for (int id = 1; id < P_n; id++) {
            S_c[id].send(Integer.toString(P_n));
            System.out.println(P_n);
            for (int id_i = id+1; id_i < P_n; id_i++) {
                S_c[id].send(names[id_i].getText());
            }
        }

        //st.start();
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

                    TextInputDialog td = new TextInputDialog("****");
                    td.setHeaderText("Enter your name");
                    td.showAndWait();
                    String name_str = td.getEditor().getText();
                    names[My_id].setText(name_str.length() <= 4 ? name_str : name_str.substring(0, 4));

                    dao = new ScoreDAO();
/*
                    for (int id = 0; id < 4; id++) {
                        Sc_sh[id] = new MyPair();
                    }
 */
                    connect_s.start();

                    tg1p = 1;
                } else if (ready == P_act){
                    try {
                        ss.close();
                        Thread.sleep(10);
                    }  catch (Exception ex) {
                        System.out.println("Start game");
                        //System.exit(0);
                    }

                    flag = true;
                    if(!st.isAlive()) {
                        st.start();
                    }
                    System.out.println(st.getState());
                    notifyAll();
                    System.out.println(st.getState());
                    for (int id_i = 1; id_i < P_n; id_i++) {
                        S_c[id_i].send("sta");
                    }
                }
            }
        }
    }

    @FXML
    protected void Ready() {
        if(!flag && !flag_r && S_c[0] != null) {
            ready += 1;
            flag_r = true;
            Platform.runLater(() -> {
                names[My_id].setUnderline(true);
            });
        }
    }

    @FXML
    protected void Stop() {
        if(flag) {
            flag = false;
            flag_r = false;
            ready = 0;
            /*
            Thread tmp_t = new Thread(
                () -> {
                    for (int id_i = 0; id_i < P_n; id_i++) {
                        dao.Sc_addOrUpdate(S_c[id_i].data);
                    }
            }).start();*/
            Platform.runLater(() -> {
                for (int id_i = 0; id_i < P_n; id_i++) {
                    if (id_i > 0) {
                        S_c[id_i].send("st");
                    }
                    if (cirs[id_i] != null) {
                        plate.getChildren().remove(cirs[id_i]);
                        cirs[id_i] = null;
                    }
                    dao.Sc_addOrUpdate(S_c[id_i].data);
                    names[id_i].setUnderline(false);
                    S_c[id_i].data.score = 0;
                    S_c[id_i].data.shoots = 0;
                    scores[id_i].setText(Integer.toString(S_c[id_i].data.score));
                    shootss[id_i].setText(Integer.toString(S_c[id_i].data.shoots));
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
            ready = 0;

            for (int id_i = 0; id_i < P_n; id_i++) {
                names[id_i].setUnderline(false);
                if (id_i > 0) {
                    S_c[id_i].send("pa");
                }
            }
        }
    }
    @FXML
    protected void Shoot() {
        if (flag && cirs[My_id] == null && S_c[My_id].data.shoots < 7) {
            S_c[My_id].data.shoots += 1;
            shootss[My_id].setText(Integer.toString(S_c[My_id].data.shoots));
            cirs_create(My_id);
            for(int id = 1; id < P_n; id++)
            {
                S_c[id].send(Integer.toString(My_id) + Integer.toString(3));
            }
        }
    }

    @FXML
    protected void DBout() {
        Thread thread = new Thread(() -> {
            dao.printAll();
        });
        thread.start();
    }
}