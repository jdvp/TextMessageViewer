package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * This is the Model of the system.
 * It is responsible for parsing the input file into
 * the appropriate message and contact objects,
 * and storing these objects.
 *
 * @author JD Porterfield
 */
public class Model {

    /**
     * The adapter used to interact with the view
     */
    IM2VAdapter view = null;
    /**
     * The list of contacts held by the system and filled by the setMessageFile method
     */
    ArrayList<Contact> contacts = new ArrayList<Contact>();

    /**
     * The constructor for the model
     * @param adptIn The adapter used to interact with the view
     */
    public Model(IM2VAdapter adptIn) {
        view = adptIn;
    }

    /**
     * This is a no-op.
     * We can't start the model without input from
     * the view, since we don't yet have any SMS stores.
     */
    public void start(){}

    /**
     * Parses the input XML file and turns each appropriate
     * line in the file into a Message object according to
     * what the line specifies. Further, these messages are
     * stored to the proper Contact object.
     * Lastly, the view is called upon to display the processed
     * result.
     *
     * @param file The XML file to process
     */
    public void setMessageFile(File file) {
        contacts = new ArrayList<Contact>();

        //Try to open the file
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: FILE NOT FOUND");
            e.printStackTrace();
        }

        //Parse the header and search for the count
        //which details the number of messages stored
        int SMSCount = 0;
        while(fileReader.hasNext()){
            String headerLine = fileReader.nextLine();
            if(headerLine.contains("count")){
                int start = headerLine.indexOf("\"")+1;
                int end = headerLine.indexOf("\"",start+1);
                SMSCount = Integer.parseInt(headerLine.substring(start,end));
                break;
            }
        }

        //Parse the messages
        for(int x = 0; x < SMSCount; x++){

            if(fileReader.next().equals("<mms"))
                break;

            String line = fileReader.nextLine();
            while(!line.contains("/>"))
                line += fileReader.nextLine();
            ParsedLine parsedSMSLine = new ParsedLine(line);

            String number = parsedSMSLine.findMatch("address");
            Date date = new Date(Long.parseLong(parsedSMSLine.findMatch("date")));
            //-1 since in text stores inbound and outbound are stored as 1 and 2 not 0 and 1
            int mode = Integer.parseInt(parsedSMSLine.findMatch("type")) -1;
            String text = parsedSMSLine.findMatch("body");
            text = parseBody(text);
            String name = parsedSMSLine.findMatch("name");
            if(name.equals(""))
                name = "(unknown)";

            Contact newContact = new Contact(name, number);
            Message message = new Message(text, date, mode, name);

            boolean addMe = true;
            for(Contact contact : contacts) {
                if(contact.equals(newContact)){
                    addMe = false;
                    contact.addMessage(message);
                }
            }

            if(addMe){
                newContact.addMessage(message);
                contacts.add(newContact);
            }
        }

        view.addPeople(contacts);
    }

    /**
     * Parses the body of the message and takes out html entities and transforms them
     * into the correct characters
     *
     * @param body The message to parse for html entities
     * @return The body of the message without html entities
     */
    private String parseBody(String body) {
        body = body.replaceAll("&apos;","'");
        body = body.replaceAll("&lt;","<");
        body = body.replaceAll("&gt;",">");
        body = body.replaceAll("&#10;","\n");
        return body;
    }
}
