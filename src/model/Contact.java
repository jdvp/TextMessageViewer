package model;

import java.util.ArrayList;

/**
 * This is a contact. A contact has a name and number
 * that represent it and a series of messages between which
 * the Contact and the user have messaged each other.
 *
 * @author JD Porterfield
 * @date 12/20/2014
 */
public class Contact {
    private String name;
    private String number;
    private ArrayList<Message> messages;

    /**
     * The Constructor for a Contact object
     *
     * @param nameIn The name of the new contact
     * @param numberIn The number of the new contact
     */
    public Contact(String nameIn, String numberIn) {
        name = nameIn;
        number = numberIn;
        messages = new ArrayList<Message>();
    }

    /**
     * Returns the name of this contact
     *
     * @return this Contact's name
     */
    public String getName() {
        return name;
    }

    /**
     * The number belonging to this contact
     *
     * @return this Contact's number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Returns the list of messages for this contact
     *
     * @return this Contact's ArrayList of Messages
     */
    public ArrayList<Message> getMessages(){
        return messages;
    }

    /**
     * Appends a new message to the end of the list of
     * messages that this Contact currently has
     *
     * @param messageIn the new message to append
     */
    public void addMessage(Message messageIn) {
        messages.add(messageIn);
    }

    /**
     * Checks for equality between this and another Contact object.
     * For this application, it suffices to say that two contacts are
     * the same if both their names and numbers are equivalent.
     *
     * @param otherContact The Contact to compare to this one
     * @return true if the other Contact is equal to this one
     */
    public boolean equals(Contact otherContact) {
        if(this.name.equals(otherContact.getName())
                && this.number.equals(otherContact.getNumber())){
            return true;
        }
        return false;
    }

    /**
     * Allows for a Contact object to be printed in a
     * recognizable manner
     *
     * @return The String approximation of this Contact
     */
    public String toString(){
        return name+" : "+number;
    }
}
