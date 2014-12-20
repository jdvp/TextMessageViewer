package view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public class View extends JFrame {

    private IV2MAdapter model = null;
    private JPanel contactPanel;
    private JPanel messagePanel;

    public View(IV2MAdapter adptIn) {
        model = adptIn;
        initGUI();
    }

    public void start(){
        setVisible(true);
    }

    public void initGUI() {
        setSize(600,450);
        JScrollPane scroll = new JScrollPane();
        getContentPane().add(scroll, BorderLayout.WEST);

        contactPanel = new JPanel();
        scroll.setViewportView(contactPanel);

        JLabel lblContacts = new JLabel("Contacts:");
        contactPanel.add(lblContacts);

        JScrollPane scroll1 = new JScrollPane();
        getContentPane().add(scroll1, BorderLayout.CENTER);

        messagePanel = new JPanel();
        scroll1.setViewportView(messagePanel);
    }
}
