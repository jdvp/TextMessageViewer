package view;

import javax.swing.*;

/**
 * Created by JD Porterfield on 12/18/2014.
 */
public class View extends JFrame {

    IV2MAdapter model = null;

    public View(IV2MAdapter adptIn) {
        model = adptIn;
        initGUI();
    }

    public void start(){
        setVisible(true);
    }

    public void initGUI() {
        setSize(600,450);
    }
}
