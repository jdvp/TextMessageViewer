package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

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
        setTitle("Text Message Viewer");
        JScrollPane scroll = new JScrollPane();
        getContentPane().add(scroll, BorderLayout.WEST);

        contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel,BoxLayout.Y_AXIS));
        scroll.setViewportView(contactPanel);

        JButton fileChooser = new JButton("Choose SMS file");
        fileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(contactPanel);
                File retFile = fc.getSelectedFile();

                model.setMessageFile(retFile);
            }
        });
        contactPanel.add(fileChooser);

        JLabel lblContacts = new JLabel("Contacts:");
        contactPanel.add(lblContacts);

        JScrollPane scroll1 = new JScrollPane();
        getContentPane().add(scroll1, BorderLayout.CENTER);

        messagePanel = new JPanel();
        scroll1.setViewportView(messagePanel);
    }
}
