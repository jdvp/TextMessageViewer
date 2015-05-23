package model;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JD on 5/17/2015.
 */
public class TimeBucketGraph extends JFrame{

    private Map<String, Integer> times = new HashMap<String, Integer>();

    public TimeBucketGraph (Contact contact){

        for(int x = 1; x <= 12; x++){
            String t1 = "" + x + ":00 AM - " + x +":30 AM";
            String t2 = "" + x + ":00 PM - "+ x + ":30 PM";
            String am = "AM";
            String pm = "PM";
            if(x == 12){
                am = "PM";
                pm = "AM";
            }
            String t3 = "" + x + ":30 AM - "+ ((x+1)%12) + ":00 " + am;
            String t4 = "" + x + ":30 AM - "+ ((x+1)%12) + ":00 " + pm;

            times.put(t1, 0);
            times.put(t2, 0);
            times.put(t3, 0);
            times.put(t4, 0);
        }

        for(Message m : contact.getMessages()){
            addTime(m.getDate().getTime());
        }
    }


    private void addTime(long timeIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeIn);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String slotStart = "";
        String slotEnd = " - ";
        int h = hour % 12;
        if (h == 0)
            h = 12;
        slotStart += h;
        boolean sameHour = false;
        if (minute < 30) {
            slotStart += ":00";
            slotEnd += h + ":30";
            sameHour = true;
        } else {
            slotStart += ":30";
            int temp = ((h + 1) % 12);
            if(temp == 0)
                temp = 12;
            slotEnd += temp + ":00";
        }


        String amPM = "";
        String opp = "";
        if (hour < 12) {
            amPM = " AM";
            opp = " PM";
        } else{
            amPM = " PM";
            opp = " AM";
        }

        slotStart += amPM;
        if(sameHour)
            slotEnd += amPM;
        else if(h == 11)
            slotEnd += opp;
        else
            slotEnd += amPM;

        String key = slotStart+slotEnd;
        if(times.containsKey(key))
            times.put(key, times.get(key)+1);
        else
            times.put(key, 1);
    }

    @Override
    public void paint(Graphics g){
        int[] p = new int[24];

        int count = 0;
        for(int i = 0; i <= 12; i++){
            String t1 = "" + i + ":00 AM - " + i + ":30 AM";
            String t2 = "" + i + ":00 PM - " + i + ":30 PM";

            p[count] = times.get(t1);
            count++;
            p[count] = times.get(t2);
            count++;
        }

        for(int i = 0; i <= 12; i++){
            String am = "AM";
            String pm = "PM";
            if(i == 12){
                am = "PM";
                pm = "AM";
            }
            String t1 = "" + i + ":30 AM - "+ ((i+1)%12) + ":00 " + am;
            String t2 = "" + i + ":30 AM - "+ ((i+1)%12) + ":00 " + pm;

            p[count] = times.get(t1);
            count++;
            p[count] = times.get(t2);
            count++;
        }
    }
}
