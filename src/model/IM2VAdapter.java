package model;

import java.util.ArrayList;

/**
 * This is the adapter that the model uses to interact with the view
 *
 * @author JD Porterfield
 * @date 12/18/2014
 */
public interface IM2VAdapter {

    /**
     * Null adapter
     */
    IM2VAdapter NULL_ADAPTER = new IM2VAdapter(){
        public void addPeople(ArrayList<Contact> contacts){}
    };

    /**
     * Allows the model to tell the view to add the processed
     * contact data however the view sees fit.
     *
     * @param contacts
     */
    public void addPeople(ArrayList<Contact> contacts);

}
