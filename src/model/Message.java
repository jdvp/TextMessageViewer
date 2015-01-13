package model;

import java.util.Date;

/**
 * A message object.
 * The pertinent information gleaned from individual
 * text messages is stored here.
 *
 * @author JD Porterfield
 **/

public class Message implements Comparable<Message>{

    /**
     * The actual body of the message
     */
    private final String text;
    /**
     * The date that the message was received (which includes the time)
     */
    private final Date date;
    /**
     * Indicates whether a message is in or out. Is compared to INBOUND_ and OUTBOUND_MESSAGE
     */
    private final int mode; // 0 if in 1 if out
    /**
     * The contact of the user that sent or received the message (the owner of the message store is nameless)
     */
    private final Contact user;
    /**
     * The previous message sent to this message's user
     */
    private Message previousMessage = null;
    /**
     * The position of this message relative to all of the messages in a contact
     */
    private int messageNumber = 0;

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
    public Message(String textIn, Date dateIn, int modeIn, Contact userIn) {
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
    public String getUser(){return user.getName(); }

    /**
     * @return the Contact associated with this message
     */
    public Contact getAssociatedContact(){return user;}

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

    /**
     * Sets the message number of this message.
     * Use a setter instead of setting in the constructor because the model is not guaranteed to
     * recognize the correct position relative to the correct contact and therefore the contact
     * must figure this out and set accordingly.
     */
    public void setMessageNumber(int messageNumberIn) {messageNumber = messageNumberIn; }

    /**
     * @return The number of this message in relation to the contact that this message belongs to
     */
    public int getMessageNumber() {return messageNumber; }

    /**
     * Will allow the messages to be sorted by their date
     *
     * @param otherMessage The message to compare to
     * @return the value returned by comparing the message's Date objects
     */
    @Override
    public int compareTo(Message otherMessage){
        return this.date.compareTo(otherMessage.getDate());
    }
}
