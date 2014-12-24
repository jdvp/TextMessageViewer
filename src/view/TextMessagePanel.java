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

    private static final long serialVersionUID = 4508730492790941948L;
	private Message myMessage;
    private JTextArea area;
    private float myAlignment;

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

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        myMessage = message;
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
        nameLabel.setBorder(new EmptyBorder(3,0,3,0));
        if(!(myMessage.getPreviousMessage().getMode() == myMessage.getMode()))
            add(nameLabel);
    }

    /**
     * Adds the message body to the TextMessagePanel
     */
    private void addText() {
        area = new JTextArea();
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
