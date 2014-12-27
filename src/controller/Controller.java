package controller;

import model.Contact;
import model.IM2VAdapter;
import model.Message;
import model.Model;
import view.IV2MAdapter;
import view.View;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * This is the controller of the system.
 * It is responsible for creating the model and view
 * and dictating the behavior of the adapters between
 * them.
 *
 * @author JD Porterfield
 */
public class Controller {

    /**
     * The model of the system
     */
    Model model = new Model(IM2VAdapter.NULL_ADAPTER);
    /**
     * The view of the system
     */
    View view = new View(IV2MAdapter.NULL_ADAPTER);

    /**
     * Creates and starts a Controller object
     *
     * @param args
     */
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

    /**
     * The constructor of a Controller object.
     * Creates the model and view and their associated adapters.
     */
    public Controller() {
        model = new Model(new IM2VAdapter() {
            public void addPeople(ArrayList<Contact> contacts) {
                view.addPeople(contacts);
            }
        });

        view = new View(new IV2MAdapter() {
            public void setMessageFile(File file) {
                model.setMessageFile(file);
            }

            @Override
            public ArrayList<Message> search(String query) {
                return model.search(query);
            }
        });
    }

    /**
     * Starts the model and the view
     */
    public void start() {
        model.start();
        view.start();
    }
}
