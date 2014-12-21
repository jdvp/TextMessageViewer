package model;

import java.util.ArrayList;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public interface IM2VAdapter {

    IM2VAdapter NULL_ADAPTER = new IM2VAdapter(){
        public void addPeople(ArrayList<Contact> contacts){}
    };

    public void addPeople(ArrayList<Contact> contacts);

}
