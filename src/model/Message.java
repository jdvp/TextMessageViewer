package model;

import java.util.Date;

/**
 * A message object.
 * The pertinent information gleaned from individual
 * text messages is stored here.
 *
 * @author JD Porterfield
 * @date 12/20/2014
 **/

public class Message {

    private String text;
    private Date date;
    private int mode; // 0 if in 1 if out

    public static final int OUTBOUND_MESSAGE = 1;
    public static final int INBOUND_MESSAGE = 0;

    /**
     * Constructs a message object
     *
     * @param textIn The text of the message
     * @param dateIn The date the message was sent
     * @param modeIn 0 if in, 1 if out
     */
    public Message(String textIn, Date dateIn, int modeIn) {
        text = textIn;
        date = dateIn;
        mode = modeIn;
    }

    /**
     * Returns the body of the message
     * @return
     */
    public String getText(){
        return text;
    }

    /**
     * Returns the date the message was sent
     * @return
     */
    public Date getDate () {
        return date;
    }

    /**
     * Returns the mode of the message
     * @return
     */
    public int getMode() {
        return mode;
    }
}
