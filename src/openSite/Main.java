package openSite;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main extends JFrame{ //todo 쉬는시간 수업시간 편집 gui

    public static final int SCREEN_W = 640;
    public static final int SCREEN_H = 360;

    public static int date = LocalDate.now().getDayOfWeek().getValue();
    public static int min = LocalTime.now().getMinute();
    public static int hour = LocalTime.now().getHour();
    public static int now =  Integer.parseInt((hour == 0 ? "12" : hour) + (min < 10 ? "0" + min : String.valueOf(min)));
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, AWTException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainGUI();
    }
}
