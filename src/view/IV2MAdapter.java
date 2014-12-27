package view;

import model.Message;

import java.io.File;
import java.util.ArrayList;

/**
 * The adapter used by the view to communicate
 * with the model
 *
 * @author JD Porterfield
 */
public interface IV2MAdapter {

    /**
     * Null adapter
     */
    IV2MAdapter NULL_ADAPTER = new IV2MAdapter() {
        public void setMessageFile(File file) {}
        public ArrayList<Message> search(String query){return new ArrayList<Message>();}
    };

    /**
     * Tells the model to process the input File
     *
     * @param file The file to process
     */
    public void setMessageFile(File file);

    /**
     * Asks the model to find messages that contain a query String
     *
     * @param query The string to search for
     * @return A list of messages that contain the query
     */
    public ArrayList<Message> search(String query);

}
