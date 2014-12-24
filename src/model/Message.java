package model;

import java.util.Date;

/**
 * A message object.
 * The pertinent information gleaned from individual
 * text messages is stored here.
 *
 * @author JD Porterfield
 **/

public class Message {

    /**
     * The actual body of the message
     */
    private String text;
    /**
     * The date that the message was received (which includes the time)
     */
    private Date date;
    /**
     * Indicates whether a message is in or out. Is compared to INBOUND_ and OUTBOUND_MESSAGE
     */
    private int mode; // 0 if in 1 if out
    /**
     * The name of the user that sent or received the message (the owner of the message store is nameless)
     */
    private String user;
    /**
     * The previous message sent to this message's user
     */
    private Message previousMessage = null;

    /**
     * Indicates a message is outbound and was sent TO the user specified by the user String
     */
    public static final int OUTBOUND_MESSAGE = 1;
    /**
     * Indicates a message is inbound and was sent FROM the user specified by the user String
     */
    public static final int INBOUND_MESSAGE = 0;

    /**
     * Constructs a message object
     *
     * @param textIn The text of the message
     * @param dateIn The date the message was sent
     * @param modeIn 0 if in, 1 if out
     * @param userIn The user's name that is associated with this text
     */
    public Message(String textIn, Date dateIn, int modeIn, String userIn) {
        text = textIn;
        date = dateIn;
        mode = modeIn;
        user = userIn;
    }

    /**
     * @return the body of the message
     */
    public String getText(){
        return text;
    }

    /**
     * @return the date the message was sent
     */
    public Date getDate () {
        return date;
    }

    /**
     * @return the mode of the message
     */
    public int getMode() {
        return mode;
    }

    /**
     * @return the name of the user associated with this message
     */
    public String getUser(){return user; }

    /**
     * Sets the predecessor message to this one
     *
     * @param message This message's predecessor
     */
    public void setPreviousMessage(Message message) {
        previousMessage = message;
    }

    /**
     * @return this message's predecessor
     */
    public Message getPreviousMessage() {
        return previousMessage;
    }
}
