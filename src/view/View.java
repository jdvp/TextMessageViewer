package view;

import model.Contact;
import model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the view of the system.
 * It is responsible for taking user input and
 * displaying the appropriate content
 *
 * @author JD Porterfield
 */
public class View extends JFrame {

    /**
     * The View's serialVersionUID
     */
    private static final long serialVersionUID = 4737920320973004483L;
    /**
     * The adapter used to communicate with the model
     */
    private IV2MAdapter model = null;
    /**
     * The panel used to display the contacts
     */
    private JPanel contactPanel;
    /**
     * The panel used to display messages
     */
    private JPanel messagePanel;
    /**
     * The button that allows a user to pick an SMS store
     */
    private JButton fileChooser;
    /**
     * "Contacts:"
     */
    private JLabel lblContacts;
    /**
     * The dimension of the messagePanel when it is first created
     */
    private Dimension defaultMessagePaneSize;
    /**
     * The dimension of the contactPanel when it is first created
     */
    private Dimension defaultContactPaneSize;
    /**
     * The Scroll Pane that contains the contactPanel
     */
    private JScrollPane contactScrollPane;
    /**
     * The Scroll Pane that contains the messagePanel
     */
    private JScrollPane messageScrollPane;
    /**
     * A text field that allows users to search for specific strings in the text messages
     */
    private JTextField searchQuery;
    /**
     * The set of contacts that was acquired by the model
     */
    private ArrayList<Contact> myContacts = null;

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
                if (returnVal == JFileChooser.APPROVE_OPTION)
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
        JMenuBar myMenu = new JMenuBar();
        searchQuery = new JTextField(40);
        JButton search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Message> searchResults = model.search(searchQuery.getText());
                Collections.sort(searchResults);
                messagePanel.removeAll();
                messagePanel.repaint();
                JLabel resultMessage = new JLabel();
                resultMessage.setAlignmentX(LEFT_ALIGNMENT);
                resultMessage.setHorizontalAlignment(SwingConstants.LEFT);
                String query = searchQuery.getText();
                if(!query.equals("")) {
                    if (searchResults.size() == 0) {
                        resultMessage.setText("No results for : '" + query +"'");
                        messagePanel.add(resultMessage);
                    } else {
                        resultMessage.setText("Search results for : '" + query + "'");
                        messagePanel.add(resultMessage);
                        for (final Message m : searchResults) {
                            TextMessagePanel textMessage = new TextMessagePanel(m, query);
                            textMessage.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    Contact contactToDisplay = new Contact("null","null");
                                    for(Contact c : myContacts)
                                        if(c.equals(m.getAssociatedContact()))
                                            contactToDisplay = c;

                                    displayTexts(contactToDisplay);
                                    final int num = m.getMessageNumber();

                                    System.out.println("MESSAGE NUMBER "+ num);
                                    final Rectangle textLocation = messagePanel.getComponent(num).getBounds();
                                    System.out.println("MESSAGE LOCATION " + textLocation.toString());

                                    //Scroll to the spot in the message list that contains the searched message
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageScrollPane.getVerticalScrollBar().setValue(messagePanel.getComponent(num).getLocation().y);
                                            messageScrollPane.validate();
                                        }
                                    });
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }
                            });
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
                    }

                    searchQuery.setText("");
                    setMessageScrollPaneSize(1);
                }
            }
        });
        getRootPane().setDefaultButton(search);
        myMenu.add(searchQuery);
        myMenu.add(search);
        myMenu.setAlignmentX(RIGHT_ALIGNMENT);
        setJMenuBar(myMenu);
    }

    /**
     * Displays a new button for each new contact in the list of
     * contacts. If a Contact's button is later clicked, the list of messages
     * held by the contact are then displayed.
     *
     * @param contacts The list of contacts processed by the model
     */
    public void addPeople(ArrayList<Contact> contacts) {
        myContacts = contacts;
        messagePanel.setSize(defaultMessagePaneSize);
        contactPanel.setSize(defaultContactPaneSize);
        messagePanel.removeAll();
        contactPanel.removeAll();
        messagePanel.repaint();
        contactPanel.repaint();
        contactPanel.add(fileChooser);
        contactPanel.add(lblContacts);
        int maxButtonWidth = 0;
        for(final Contact c: contacts) {
            JButton contactButton = new JButton("<HTML>"+c.getName()+"<br>"+c.getNumber()+"</HTML>");
            contactButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        displayTexts(c);
                    }
            });

            if (contactButton.getPreferredSize().getWidth() > maxButtonWidth)
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
     *
     * @param offset The number of components to skip resizing for (starting at the top)
     */
    private void setMessageScrollPaneSize(int offset){
        int messageWidth = messageScrollPane.getViewport().getWidth()- 40;
        for(int i = offset; i < messagePanel.getComponentCount(); i++) {
            messagePanel.getComponent(i).setMaximumSize(new Dimension(messageWidth / 2,Integer.MAX_VALUE));
            messagePanel.setVisible(true);
        }
        messagePanel.validate();
        getContentPane().validate();
    }

    /**
     * Displays the text messages owned by the specified contact on the message window.
     * @param contact the contact which owns the messages that we want to view
     */
    private void displayTexts(Contact contact) {
        messagePanel.removeAll();
        messagePanel.repaint();
        for (Message m : contact.getMessages()) {
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
        setMessageScrollPaneSize(0);
    }
}
