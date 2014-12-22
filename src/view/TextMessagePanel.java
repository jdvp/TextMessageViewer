package view;

import model.Message;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * This is a specialized JPanel used specifically to
 * display text messages
 *
 * @author JD Porterfield
 * @date 12/22/2014
 */
public class TextMessagePanel extends JPanel{

    private Message myMessage;
    private JTextArea area;

    /**
     * Constructs the TextMessagePanel and calls
     * on subroutines to add specific componets
     * (i.e. Contact name, message, date sent, etc.)
     *
     * @param message The message to be displayed
     */
    public TextMessagePanel(Message message) {

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        myMessage = message;
        addPerson();
        addText();
        addDate();
    }

    /**
     * Adds the sender of the message to the TextMessagePanel
     */
    private void addPerson() {
        JLabel nameLabel;
        if(myMessage.getMode() == Message.INBOUND_MESSAGE) {
            nameLabel = new JLabel(myMessage.getUser());
            nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        }
        else {
            nameLabel = new JLabel("You");
            nameLabel.setAlignmentX(RIGHT_ALIGNMENT);
        }
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
        area.setBackground(new Color(240,240, 240));

        area.setBorder(new BevelBorder(BevelBorder.LOWERED));

        if(myMessage.getMode() == Message.OUTBOUND_MESSAGE)
            area.setAlignmentX(RIGHT_ALIGNMENT);
        else
            area.setAlignmentX(LEFT_ALIGNMENT);
        add(area);
    }

    /**
     * Adds the date to the current TextMessagePanel
     */
    private void addDate(){
        JLabel dateLabel = new JLabel(myMessage.getDate().toString());
        dateLabel.setAlignmentX(area.getAlignmentX());
        dateLabel.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        add(dateLabel);
    }

}
