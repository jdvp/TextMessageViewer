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
 * This is the view of the system.
 * It is responsible for taking user input and
 * displaying the appropriate content
 *
 * @author JD Porterfield
 * @date 12/18/2014
 */
public class View extends JFrame {

    private IV2MAdapter model = null;
    private JPanel contactPanel;
    private JPanel messagePanel;
    private JButton fileChooser;
    private JLabel lblContacts;
    private Dimension defaultMessagePaneSize;
    private Dimension defaultContactPaneSize;

    /**
     * The constructor for a View object.
     * Takes in the an adpter and starts the
     * process to creating the GUI
     *
     * @param adptIn The adapter to use
     */
    public View(IV2MAdapter adptIn) {
        model = adptIn;
        initGUI();
    }

    /**
     * Sets the view to be visible so that
     * the user can interact with the system
     */
    public void start(){
        setVisible(true);
    }

    /**
     * Initializes the GUI for the system.
     */
    public void initGUI() {
        setSize(600,500);
        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("img/TextMessageViewerIcon.png")));
        setResizable(false);
        setTitle("Text Message Viewer");
        JScrollPane scroll = new JScrollPane();
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
        defaultContactPaneSize = contactPanel.getSize();
        defaultMessagePaneSize = messagePanel.getSize();
    }

    /**
     * Displays a new button for each new contact in the list of
     * contacts. If a Contact's button is later clicked, the list of messages
     * held by the contact are then displayed.
     *
     * @param contacts The list of contacts processed by the model
     */
    public void addPeople(ArrayList<Contact> contacts) {
        messagePanel.setSize(defaultMessagePaneSize);
        contactPanel.setSize(defaultContactPaneSize);
        messagePanel.removeAll();
        contactPanel.removeAll();
        contactPanel.repaint();
        contactPanel.add(fileChooser);
        contactPanel.add(lblContacts);
        for(final Contact c: contacts){
            JButton contactButton = new JButton(c.getName());
            contactButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    messagePanel.removeAll();
                    for (Message m : c.getMessages()) {
                        TextMessagePanel textMessage = new TextMessagePanel(m);
                        Dimension dim = messagePanel.getSize();
                        dim.setSize(dim.getWidth() / 2, dim.getHeight());
                        textMessage.setMaximumSize(dim);
                        if (m.getMode() == Message.OUTBOUND_MESSAGE)
                            textMessage.setAlignmentX(LEFT_ALIGNMENT);
                        else
                            textMessage.setAlignmentX(RIGHT_ALIGNMENT);
                        messagePanel.add(textMessage);
                        getContentPane().validate();
                    }
                    messagePanel.validate();
                }
            });
            contactPanel.add(contactButton);
        }
        getContentPane().validate();
    }
}
