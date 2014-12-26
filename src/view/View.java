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
 */
public class View extends JFrame {


    private static final long serialVersionUID = 4737920320973004483L;
    private IV2MAdapter model = null;
    private JPanel contactPanel;
    private JPanel messagePanel;
    private JButton fileChooser;
    private JLabel lblContacts;
    private Dimension defaultMessagePaneSize;
    private Dimension defaultContactPaneSize;
    private JScrollPane contactScrollPane;
    private JScrollPane messageScrollPane;

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
        contactScrollPane = new JScrollPane();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(contactScrollPane);

        contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.PAGE_AXIS));
        contactScrollPane.setViewportView(contactPanel);

        fileChooser = new JButton("Choose SMS file");
        fileChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(contactPanel);
                File retFile = fc.getSelectedFile();

                model.setMessageFile(retFile);
            }
        });
        fileChooser.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) fileChooser.getPreferredSize().getHeight()));
        fileChooser.setHorizontalAlignment(SwingConstants.LEFT);
        contactPanel.add(fileChooser);

        lblContacts = new JLabel("      Contacts:");
        contactPanel.add(lblContacts);

        messageScrollPane = new JScrollPane();
        messageScrollPane.setWheelScrollingEnabled(true);
        contactScrollPane.setWheelScrollingEnabled(true);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBorder(new EmptyBorder(5,20,5,20));

        messageScrollPane.setViewportView(messagePanel);
        getContentPane().add(messageScrollPane);
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
        messagePanel.repaint();
        contactPanel.repaint();
        contactPanel.add(fileChooser);
        contactPanel.add(lblContacts);
        int maxButtonWidth = 0;
        for(final Contact c: contacts){
            JButton contactButton = new JButton(c.getName());
            contactButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    messagePanel.removeAll();
                    messagePanel.repaint();
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

                    setMessageScrollPaneSize();

                }
            });

            if(contactButton.getPreferredSize().getWidth() > maxButtonWidth)
                maxButtonWidth = (int) contactButton.getPreferredSize().getWidth();
            contactButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) contactButton.getPreferredSize().getHeight()));
            contactButton.setHorizontalAlignment(SwingConstants.LEFT);
            contactPanel.add(contactButton);
        }
        //+50 for the padding on the buttons
        contactPanel.setMinimumSize(new Dimension(maxButtonWidth + 50, 100));
        contactScrollPane.setMinimumSize(new Dimension(maxButtonWidth + 50, 100));
        getContentPane().validate();
    }

    /**
     * Used to set the size of the message scroll pane so that it is always the correct size
     */
    private void setMessageScrollPaneSize(){
        int messageWidth = messageScrollPane.getViewport().getWidth()- 40;
        for(int i = 0; i < messagePanel.getComponentCount(); i++) {
            messagePanel.getComponent(i).setMaximumSize(new Dimension(messageWidth / 2,Integer.MAX_VALUE));
            messagePanel.setVisible(true);
        }
        messagePanel.validate();
        getContentPane().validate();
    }
}
