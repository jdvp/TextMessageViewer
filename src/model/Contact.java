package model;

import java.util.ArrayList;

/**
 * Created by JD Porterfield on 12/20/2014.
 */
public class Contact {
    private String name;
    private String number;
    private ArrayList<Message> messages;

    public Contact(String nameIn, String numberIn) {
        name = nameIn;
        number = numberIn;
        messages = new ArrayList<Message>();
    }
}
