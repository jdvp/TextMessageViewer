package view;

import model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * This is a specialized JPanel used specifically to
 * display text messages
 *
 * @author JD Porterfield
 */
public class TextMessagePanel extends JPanel{

    /**
     * The TextMessagePanel serialVersionUID
     */
    private static final long serialVersionUID = 4508730492790941948L;
    /**
     * The Message object that this TextMessagePanel will display
     */
	private final Message myMessage;
    /**
     * The alignment of objects in this panel, as determined by the mode of the input Message
     */
    private float myAlignment;
    /**
     * The query that was searched to yield the display of this panel
     */
    private String mySearchQuery = null;

    /**
     * Constructs the TextMessagePanel and calls
     * on subroutines to add specific componets
     * (i.e. Contact name, message, date sent, etc.)
     * and decides what the orientation for the objects in the panel is
     * based on whether it is inbound or outbound.
     *
     * @param message The message to be displayed
     */
    public TextMessagePanel(Message message) {
        myMessage = message;
        initialize();
    }

    /**
     * Constructs the TextMessagePanel and sets the searchQuery which
     * indicates that this panel was created for a search. The difference
     * between a TextMessagePanel that has been searched for and one that
     * has not is that the searched one always has a name attached whereas
     * the non-searched one does not.
     *
     * @param message The message to be displayed
     * @param searchQuery The string that was queried
     */
    public TextMessagePanel(Message message, String searchQuery){
        myMessage = message;
        mySearchQuery = searchQuery;
        initialize();
    }

    /**
     * Initializes the GUI components of this panel
     */
    private void initialize(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        if(myMessage.getMode() == Message.INBOUND_MESSAGE)
            myAlignment = LEFT_ALIGNMENT;
        else
            myAlignment = RIGHT_ALIGNMENT;
        addPerson();
        addText();
        addDate();
    }

    /**
     * Adds the sender of the message to the TextMessagePanel
     */
    private void addPerson() {
        JLabel nameLabel;

        if(myMessage.getMode() == Message.INBOUND_MESSAGE)
            nameLabel = new JLabel(myMessage.getUser());
        else
            nameLabel = new JLabel("You");

        nameLabel.setAlignmentX(myAlignment);
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
        nameLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
        if((!(myMessage.getPreviousMessage().getMode() == myMessage.getMode()))|| mySearchQuery != null)
            add(nameLabel);
    }

    /**
     * Adds the message body to the TextMessagePanel
     */
    private void addText() {
        JTextArea area = new JTextArea();
        //area.setSize(50, 100);
        area.setText(myMessage.getText());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        area.setSize(area.getPreferredSize());

        area.setBackground(new Color(255, 255, 255));
        area.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        area.setFont(new Font("Helvetica", Font.PLAIN, 12));
        area.setAlignmentX(myAlignment);
        add(area);
    }

    /**
     * Adds the date to the current TextMessagePanel
     */
    private void addDate(){
        JLabel dateLabel = new JLabel(myMessage.getDate().toString());
        dateLabel.setAlignmentX(myAlignment);
        dateLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
        add(dateLabel);
    }

}
