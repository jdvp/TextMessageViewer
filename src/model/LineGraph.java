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
public class LineGraph extends JFrame{

    private int factor = 10;
    private int p[][] = new int[1440/factor][100];
    private int u[] = new int[1440/factor];
    private int c[] = new int[1440/factor];
    private Map<Integer, Color> colors = new HashMap<Integer, Color>();

    public LineGraph (Contact contact){
        colors.put(0, new Color(255,255,255));
        colors.put(-1, new Color(255,0,0));
        colors.put(1, new Color(0,255,0));
        for(int x = 0; x < (1440/factor); x++)
            for(int y = 0; y < (100); y++)
                p[x][y] = 0;

        int maxMessages = 0;

        for(Message m : contact.getMessages()){
            Date d = m.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(d.getTime());

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            int mode = m.getMode();

            if(mode == 0)
                c[(hour * (60/factor)) + (minute/factor)] ++;
            else
                u[(hour * (60/factor))+ (minute/factor)] ++;
        }

        for(int i : c)
            if(i > maxMessages)
                maxMessages = i;

        for(int i : u)
            if(i > maxMessages)
                maxMessages = i;

        System.out.println("maxMessages = " + maxMessages);
        for(int x = 0; x < (1440/factor); x++){
            System.out.println("x = " + x);
            Double a = (double)u[x]/(double) maxMessages * 99.0;
            p[x][a.intValue()] = 1;
            System.out.println("u[x]/maxMessages * 99 = " + u[x] / maxMessages * 99);
            Double b = (double) c[x] / (double) maxMessages * 99.0;
            p[x][b.intValue()] = -1;
            System.out.println("c[x]/maxMessages * 99 = " + c[x] / maxMessages * 99);
        }
        setSize((1440/factor), 100);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g){
        for(int x = 0; x < (1440/factor); x++){
            for(int y = 0; y < 100; y++){
                g.setColor(colors.get(p[x][y]));
                g.drawLine(x,y,x,y);
            }
        }
    }
}
