package view;

import model.Message;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by JD Porterfield on 12/21/2014.
 */
public class TextMessagePanel extends JPanel{

    private Message myMessage;
    private String myName;

    public TextMessagePanel(Message message, String name) {

        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        myMessage = message;
        myName = name;
        addPerson();
        addText();
    }

    private void addPerson() {
        JLabel nameLabel = null;
        if(myMessage.getMode() == 0){

            nameLabel = new JLabel(myName);
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        }
        else {
            nameLabel = new JLabel("You");
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        }
        add(nameLabel);
    }

    private void addText() {
        JTextArea area = new JTextArea();
        //area.setSize(50, 100);
        area.setText(myMessage.getText());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(new Color(240,240, 240));

        area.setBorder(new BevelBorder(BevelBorder.LOWERED));

        if(myMessage.getMode() == 0)
            area.setAlignmentX(RIGHT_ALIGNMENT);
        else
            area.setAlignmentX(LEFT_ALIGNMENT);
        add(area);
    }

}
