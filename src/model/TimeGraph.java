package model;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JD on 5/16/2015.
 */
public class TimeGraph extends JFrame {
    private int[][] messageTimes = new int[14][720];
    private Map<Integer, Color> colors = new HashMap<Integer, Color>();

    public TimeGraph(Contact contact){
        colors.put(-10, new Color(255,0,0));
        colors.put(-9, new Color(255,73,73));
        colors.put(-8, new Color(255,119,119));
        colors.put(-7, new Color(255,130,130));
        colors.put(-6, new Color(255,140,140));
        colors.put(-5, new Color(255,160,155));
        colors.put(-4, new Color(255,190,170));
        colors.put(-3, new Color(255,210,190));
        colors.put(-2, new Color(255,230,210));
        colors.put(-1, new Color(255,250,230));
        colors.put(0, new Color(64,64,64));
        colors.put(1, new Color(255,255,240));
        colors.put(2, new Color(245,255,230));
        colors.put(3, new Color(235,255,220));
        colors.put(4, new Color(225,255,210));
        colors.put(5, new Color(215,255,200));
        colors.put(6, new Color(195,255,170));
        colors.put(7, new Color(175,255,150));
        colors.put(8, new Color(145,255,135));
        colors.put(9, new Color(115,255,155));
        colors.put(10, new Color(0,255,0));

        for(int x = 0; x < 14; x++)
            for(int y = 0; y < 720; y++)
                messageTimes[x][y] = 0;

        for(Message m : contact.getMessages()){
            Date d = m.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(d.getTime());

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            int mode = m.getMode() + 1;

            minute /= 2;

            messageTimes[(day * mode) - 1][(hour * 30) + minute] ++;
        }

        System.out.println(messageTimes);
        setSize(994,720);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g){
        for(int x = 0; x < 14; x++){
            for(int y = 0; y < 720; y++){
                if(messageTimes[x][y] > 10)
                    messageTimes[x][y] = 10;
                else if(messageTimes[x][y] < -10)
                    messageTimes[x][y] = -10;

                g.setColor(colors.get(messageTimes[x][y]));
                for(int z = 0; z < 70; z++){
                    g.drawLine((x*70)+z, y, (x*70)+z, y);
                }
            }
        }
    }
}
