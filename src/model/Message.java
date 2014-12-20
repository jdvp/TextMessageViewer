package model;

import java.sql.Time;
import java.util.Date;

/**
 * Created by JD Porterfield on 12/20/2014.
 */
public class Message {

    private String text;
    private Date date;
    private Time time;
    private int mode;

    public Message(String textIn, Date dateIn, Time timeIn, int modeIn) {
        text = textIn;
        date = dateIn;
        time = timeIn;
        mode = modeIn;
    }

    public String getText(){
        return text;
    }

    public Date getDate () {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getMode() {
        return mode;
    }
}
