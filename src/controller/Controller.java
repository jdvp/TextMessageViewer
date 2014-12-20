package controller;

import model.IM2VAdapter;
import model.Model;
import view.IV2MAdapter;
import view.View;

import java.awt.*;
import java.io.File;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public class Controller {

    Model model = new Model(IM2VAdapter.NULL_ADAPTER);
    View view = new View(IV2MAdapter.NULL_ADAPTER);

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Controller controller = new Controller();
                    controller.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Controller() {
        model = new Model(new IM2VAdapter() {

        });

        view = new View(new IV2MAdapter() {
            public void setMessageFile(File file) {
                model.setMessageFile(file);
            }
        });
    }

    public void start() {
        model.start();
        view.start();
    }
}
