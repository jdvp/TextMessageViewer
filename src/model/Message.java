package model;

import java.util.Date;

/**
 * Created by JD Porterfield on 12/20/2014.
 */
public class Message {

    private String text;
    private Date date;
    private int mode; // 0 if in 1 if out

    public Message(String textIn, Date dateIn, int modeIn) {
        text = textIn;
        date = dateIn;
        mode = modeIn;
    }

    public String getText(){
        return text;
    }

    public Date getDate () {
        return date;
    }

    public int getMode() {
        return mode;
    }
}
