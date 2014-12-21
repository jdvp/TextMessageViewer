package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public class Model {

    IM2VAdapter view = null;
    ArrayList<Contact> contacts = new ArrayList<Contact>();

    public Model(IM2VAdapter adptIn) {
        view = adptIn;
    }

    public void start(){

    }

    public void setMessageFile(File file) {
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.err.println("ERROR: FILE NOT FOUND");
            e.printStackTrace();
        }

        //Skip header
        fileReader.nextLine();
        fileReader.nextLine();
        fileReader.nextLine();

        //Parse number of messages
        String smsCount = fileReader.nextLine();
        smsCount = smsCount.replaceAll("<smses count=\"","");
        smsCount = smsCount.replaceAll("\">","");

        for(int x = 0; x < Integer.parseInt(smsCount); x++){

            if(fileReader.next().equals("<mms"))
                break;

            String line = fileReader.nextLine();

            String number = line.substring(line.indexOf("address=")+9, line.indexOf("date=")-2);
            Date date = new Date(Long.parseLong(line.substring(line.indexOf("date=")+6, line.indexOf("type=")-2)));
            int mode = Integer.parseInt(line.substring(line.indexOf("type=")+6,line.indexOf("type=")+7)) - 1;
            String text = line.substring(line.indexOf("body=")+6, line.indexOf("toa=")-2);
            String name = line.substring(line.indexOf("contact_name=")+14, line.length()-4);

            Contact newContact = new Contact(name, number);
            Message message = new Message(text, date, mode);

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
        for(Contact c : contacts){
            c.printMessagesToConsole();
        }
    }
}
