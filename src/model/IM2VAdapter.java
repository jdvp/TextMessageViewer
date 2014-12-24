package model;

import java.util.ArrayList;

/**
 * This is the adapter that the model uses to interact with the view
 *
 * @author JD Porterfield
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
     * @param contacts The list of contacts to add to the view
     */
    public void addPeople(ArrayList<Contact> contacts);

}
