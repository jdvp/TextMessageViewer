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
    private String myName;

    /**
     * Constructs the TextMessagePanel and calls
     * on subroutines to add specific componets
     * (i.e. Contact name, message, date sent, etc.)
     *
     * @param message The message to be displayed
     * @param name The name of the contact that this message belongs to
     */
    public TextMessagePanel(Message message, String name) {

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        myMessage = message;
        myName = name;
        addPerson();
        addText();
    }

    /**
     * Adds the sender of the message to the TextMessagePanel
     */
    private void addPerson() {
        JLabel nameLabel = null;
        if(myMessage.getMode() == Message.OUTBOUND_MESSAGE){

            nameLabel = new JLabel(myName);
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        }
        else {
            nameLabel = new JLabel("You");
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        }
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
        area.setBackground(new Color(240,240, 240));

        area.setBorder(new BevelBorder(BevelBorder.LOWERED));

        if(myMessage.getMode() == Message.OUTBOUND_MESSAGE)
            area.setAlignmentX(RIGHT_ALIGNMENT);
        else
            area.setAlignmentX(LEFT_ALIGNMENT);
        add(area);
    }

}
