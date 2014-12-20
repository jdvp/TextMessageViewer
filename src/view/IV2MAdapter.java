package view;

import model.IM2VAdapter;

import java.io.File;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public interface IV2MAdapter {

    IV2MAdapter NULL_ADAPTER = new IV2MAdapter() {
        public void setMessageFile(File file) {}
    };

    public void setMessageFile(File file);

}
