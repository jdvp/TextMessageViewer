package view;

import java.io.File;

/**
 * The adapter used by the view to communicate
 * with the model
 *
 * @author JD Porterfield
 * @date 12/18/2014
 */
public interface IV2MAdapter {

    /**
     * Null adapter
     */
    IV2MAdapter NULL_ADAPTER = new IV2MAdapter() {
        public void setMessageFile(File file) {}
    };

    /**
     * Tells the model to process the input File
     *
     * @param file The file to process
     */
    public void setMessageFile(File file);

}
