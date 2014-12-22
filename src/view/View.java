package view;

import model.Contact;
import model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public class View extends JFrame {

    private IV2MAdapter model = null;
    private JPanel contactPanel;
    private JPanel messagePanel;
    private JButton fileChooser;
    private JLabel lblContacts;

    public View(IV2MAdapter adptIn) {
        model = adptIn;
        initGUI();
    }

    public void start(){
        setVisible(true);
    }

    public void initGUI() {
        setSize(600,500);
        setResizable(false);
        setTitle("Text Message Viewer");
        JScrollPane scroll = new JScrollPane();
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        getContentPane().add(scroll);

        contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        scroll.setViewportView(contactPanel);

        fileChooser = new JButton("Choose SMS file");
        fileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(contactPanel);
                File retFile = fc.getSelectedFile();

                model.setMessageFile(retFile);
            }
        });
        contactPanel.add(fileChooser);

        lblContacts = new JLabel("Contacts:");
        contactPanel.add(lblContacts);

        JScrollPane scroll1 = new JScrollPane();
        scroll1.setWheelScrollingEnabled(true);
        scroll.setWheelScrollingEnabled(true);
        scroll1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.Y_AXIS));
        messagePanel.setBorder(new EmptyBorder(5,20,5,20));

        scroll1.setViewportView(messagePanel);
        getContentPane().add(scroll1);
    }

    public void addPeople(ArrayList<Contact> contacts) {
        contactPanel.removeAll();
        contactPanel.add(fileChooser);
        contactPanel.add(lblContacts);
        for(final Contact c: contacts){
            JButton contactButton = new JButton(c.getName());
            contactButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    messagePanel.removeAll();
                    for(Message m: c.getMessages()){
                        TextMessagePanel textMessage = new TextMessagePanel(m, c.getName());
                        Dimension dim = messagePanel.getSize();
                        dim.setSize(dim.getWidth()/2,dim.getHeight());
                        textMessage.setMaximumSize(dim);
                        if(m.getMode() == 0)
                            textMessage.setAlignmentX(LEFT_ALIGNMENT);
                        else
                            textMessage.setAlignmentX(RIGHT_ALIGNMENT);
                        messagePanel.add(textMessage);
                        contactPanel.setPreferredSize(contactPanel.getPreferredSize());
                        getContentPane().validate();
                    }
                    messagePanel.validate();
                }
            });
            contactPanel.add(contactButton);
            contactPanel.setMinimumSize(contactPanel.getPreferredSize());
            getContentPane().validate();
        }
    }
}
