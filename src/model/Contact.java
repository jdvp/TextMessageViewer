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

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public void addMessage(Message messageIn) {
        messages.add(messageIn);
    }

    public boolean equals(Contact otherContact) {
        if(this.name.equals(otherContact.getName())
                && this.number.equals(otherContact.getNumber())){
            return true;
        }
        return false;
    }

    public String toString(){
        return name+" : "+number;
    }

    public void printMessagesToConsole(){
        for(Message m: messages){
            String person = m.getMode()==0?name:"You";
            System.out.println(person+" : "+m.getText()+" "+m.getDate());
        }
    }
}
