package openSite;

import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class MainGUI extends JFrame {
    static JFrame f = new JFrame("  ZSchedule ver 0.0.3");
    static ImageIcon nowClassBasic = new ImageIcon("C:\\Temp\\ZSchedule\\images\\classes.png");
    static ImageIcon nowClassEnter = new ImageIcon("C:\\Temp\\ZSchedule\\images\\classesEnter.png");
    static File file = new File("C:\\Temp\\ZSchedule\\files\\clearDay.txt");
    static File saveConfig = new File("C:\\Temp\\ZSchedule\\files\\saveConfig.xml");
    static File autoLinkingFile = new File("C:\\Temp\\ZSchedule\\files\\autoLinkConfig.txt");
    static File shortSchoolFile = new File("C:\\Temp\\ZSchedule\\files\\shortSchool.xml");
    static File ringingFile = new File("C:\\Temp\\ZSchedule\\files\\ringing.wav");
    static File connectTimeFile = new File("C:\\Temp\\ZSchedule\\files\\setting.xml");
    static File timetableFile = new File("C:\\Temp\\ZSchedule\\files\\timeTable.xml");
    static JLabel lb1 = new JLabel("", SwingConstants.CENTER);
    static JButton classes = new JButton(nowClassBasic);
    static JToolBar bar = new JToolBar();
    static JCheckBox autoLinking = new JCheckBox("자동 연결", Boolean.parseBoolean(fileRead(autoLinkingFile)));
    static JMenuBar mb = new JMenuBar();
    static JMenu editMenu = new JMenu("Edit");
    static JMenu settingMenu = new JMenu("Setting");
    static JMenuItem edit = new JMenuItem("링크");
    static JMenu shortDay = new JMenu("단축수업");
    static JMenu filesChoose = new JMenu("파일");
    static JMenuItem alarmTime = new JMenuItem("알림");
    static JMenuItem timetableMenu = new JMenuItem("시간표");
    static JMenuItem shortDayBreakTime = new JMenuItem("쉬는시간");
    static JMenuItem shortDayStudyTime = new JMenuItem("수업시간");
    static JMenuItem linkEditFile = new JMenuItem("링크 파일");
    static JMenuItem timetableEditFile = new JMenuItem("시간표 파일");
    static String[] URLs = new String[16];
    static boolean[] changeAble = new boolean[URLs.length];
    static String[] subjectNames = {"MeetEnd", "Kor", "Math", "Eng", "ScienceB", "History", "PE", "Chin", "Music", "Moral", "Home", "Techno", "CEA", "ScienceA", "Sports", "Music2"};
    static String[] korSubjectNames = {"조종례", "국어", "수학", "영어", "과학B", "역사", "체육", "한문", "음악", "도덕", "가정", "기술", "창체", "과학A", "스포츠", "음악2"};
    static String[] timetable = new String[35];
    static String[][] timetable2D = new String[5][7];
    static String[] timetableNode = new String[35];
    static String[] times = new String[2]; // break,study
    static int connectTime;
    static MenuItem openItem = new MenuItem("Open");
    static MenuItem hideItem = new MenuItem("Hide");
    static MenuItem exitItem = new MenuItem("Exit");
    static PopupMenu popup = new PopupMenu();
    static Thread thread = new Thread(new ThreadWithRunnable());
    static HashMap<String, String> nameURL = new HashMap<>();
    static HashMap<String, String> korToEng = new HashMap<>();
    static HashMap<String, String> engToKor = new HashMap<>();
    static Insets margin = new Insets(2, -25, 2, 10);

    public MainGUI() throws ParserConfigurationException, IOException, SAXException, AWTException {
        for (int i = 0; i < subjectNames.length; i++) {
            korToEng.put(korSubjectNames[i], subjectNames[i]);
            engToKor.put(subjectNames[i], korSubjectNames[i]);
        }
        for (int i = 0; i < timetable.length; i++) {
            timetableNode[i] = "t"+ i;
            timetable[i] = engToKor.get(XMLManager.XMLReader(timetableFile.getPath(), i));
            if(timetable[i] == null) {
                timetable[i] = "";
            }
        }
        timetable2D = mk2DArr(timetable, timetable2D.length, timetable2D[0].length);
        connectTime = Integer.parseInt(XMLManager.XMLReader(connectTimeFile.getPath(), 0));
        for (int i = 0; i < times.length; i++) {
            XMLManager.XMLReader(shortSchoolFile.getPath(), times, i);
        }
        for (int i = 0; i < URLs.length; i++) {
            changeAble[i] = Boolean.parseBoolean(XMLManager.XMLReader(saveConfig.getPath(), URLs, i));
            XMLManager.XMLReader(saveConfig.getPath(), URLs, i);
            nameURL.put(korSubjectNames[i], URLs[i]);
        }
        if(!fileRead(file).equals(Integer.toString(Main.date))) {
            times[0] = "15";
            times[1] = "40";
            shortSchoolXmlWrite();
            for (int i = 0; i < changeAble.length; i++) {
                manyIF.manyIFToday(changeAble[i], i, URLs);
            }
            BufferWriteTry(Integer.toString(Main.date), file);
        }
        setFrameOptions(f);
        Tray.TrayDemoDefault();
        new manyIF(connectTime, timetable, nameURL);
        if(autoLinking.isSelected()) {
            thread.start();
        }
    }
    public static void BufferWriteTry(String index, File target) {
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(target));
            if(target.isFile() && target.canWrite()){
                try {
                    bufferedWriter.write(index);
                } catch (NullPointerException e) {
                    System.exit(0);
                }
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void mkJOptionPane(String showMsg, String[] target, int i) {
        String index = JOptionPane.showInputDialog(showMsg);
        if (index == null) {
            manyIF.desktopView("https://rang.edunet.net/main.do");
            mkJOptionPane(showMsg, target, i);
        } else {
            if(index.contains(" ")) {
                index = index.replace(" ", "");
            }
            target[i] = index;
            try {
                XMLManager.XMLWriter(saveConfig, "saveConfig", subjectNames, URLs, true, changeAble);
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
    }
    public static void mkJOptionPane(String showMsg, String title, int option) {
        JOptionPane.showMessageDialog(null, showMsg, title, option);
    }
    public static int mkJOptionPane(String showMsg) {
        String result = JOptionPane.showInputDialog(showMsg);
        if(result == null) {
            mkJOptionPane(showMsg);
        } else {
            if (result.matches("-?\\d+")) {
                return Integer.parseInt(result);
            } else {
                mkJOptionPane("숫자를 입력해 주세요", "Notification", JOptionPane.ERROR_MESSAGE);
                mkJOptionPane(showMsg);
            }
        }
        return 0;
    }
    public static void ButtonDefaultSet(int x, int y, int width, int height, JButton btn, String toolTipText) {
        btn.setBounds(x, y ,width, height);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setToolTipText(toolTipText);
        btn.addMouseListener(new MouseEvents());
        bar.add(btn);
    }
    public static String fileRead(File path) {
        String str = "";
        try{
            FileReader filereader = new FileReader(path);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line;
            while((line = bufReader.readLine()) != null){
                str = line;
            }
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static void setFrameOptions(JFrame f) {
        shortDay.setMargin(margin);
        edit.setMargin(margin);
        shortDayBreakTime.setMargin(margin);
        shortDayStudyTime.setMargin(margin);
        alarmTime.setMargin(margin);
        timetableMenu.setMargin(margin);
        filesChoose.setMargin(margin);
        linkEditFile.setMargin(margin);
        timetableEditFile.setMargin(margin);
        filesChoose.add(linkEditFile);
        filesChoose.add(timetableEditFile);
        shortDay.add(shortDayBreakTime);
        shortDay.add(shortDayStudyTime);
        editMenu.add(edit);
        editMenu.add(shortDay);
        editMenu.add(alarmTime);
        editMenu.add(timetableMenu);
        settingMenu.add(filesChoose);
        mb.add(editMenu);
        mb.add(settingMenu);
        edit.addMouseListener(new MouseEvents());
        shortDayBreakTime.addMouseListener(new MouseEvents());
        shortDayStudyTime.addMouseListener(new MouseEvents());
        alarmTime.addMouseListener(new MouseEvents());
        timetableMenu.addMouseListener(new MouseEvents());
        linkEditFile.addMouseListener(new MouseEvents());
        timetableEditFile.addMouseListener(new MouseEvents());
        ButtonDefaultSet(140, 160, 350, 40, classes, "linking");
        lb1.setFont(new Font("", Font.PLAIN, 50));
        manyIF.nowClass(lb1);
        autoLinking.setBounds(540, 0, 100, 15);
        autoLinking.addItemListener(new ItemEvents());
        if(autoLinking.isSelected()) {
            lb1.setBounds(0, 130, 640, 50);
            classes.setVisible(false);
        } else {
            lb1.setBounds(0, 80, 640, 50);
            classes.setVisible(true);
        }
        f.getContentPane().add(autoLinking);
        f.getContentPane().add(lb1);
        f.getContentPane().add(classes);
        f.setSize(Main.SCREEN_W, Main.SCREEN_H);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setLayout(null);
        f.getContentPane().setBackground(new Color(245, 245, 245, 255));
        f.setJMenuBar(mb);
        f.setVisible(true);
        f.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                boolean b = true;
                for (String[] strings : timetable2D) {
                    for (int j = 0; j < timetable2D[0].length; j++) {
                        if (strings[j].equals("") && !(j == 6)) {
                            b = false;
                            if (JOptionPane.showConfirmDialog(null,
                                    " 현재 시간표 설정값이 불완전 합니다.\n 계속 진행하시겠습니까? (오류발생 가능)",
                                    "Notification", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION) {
                                System.exit(0);
                            }
                        }
                    }
                }
                if(b) {
                    System.exit(0);
                }
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }
    public static String[][] mk2DArr(String[] Arr, int i, int j) {
        int sum = 0;
        String[][] Arr2 = new String[i][j];
        for (int k = 0; k < i; k++) {
            for (int l = 0; l < j; l++) {
                Arr2[k][l] = Arr[sum];
                sum++;
            }
        }
        return Arr2;
    }
    public static void shortSchoolXmlWrite() {
        try {
            XMLManager.XMLWriter(shortSchoolFile,
                    "shortSchool",
                    new String[]{"breakTime", "studyTime"},
                    times,
                    false, null);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }
    public static void restart() {
        try {
            Runtime.getRuntime().exec("C:\\Temp\\ZSchedule\\ZSchedule.exe");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.exit(0);
    }
    public static class MouseEvents implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() == classes) {
                classes.setIcon(nowClassEnter);
            }
        }
        @Override
        public void mouseExited(MouseEvent e) {
            if (!autoLinking.isSelected() && e.getSource() == classes) {
                classes.setIcon(nowClassBasic);
            }
        }
        @Override
        public void mousePressed(MouseEvent e){
            if(e.getSource() == classes) {
                manyIF.manyIFNowClass();
                System.exit(0);
            } else if(e.getSource() == edit) {
                if(!(LinkEditGUI.frame.isActive())) {
                    try {
                        LinkEditGUI.mkEditor(URLs, saveConfig, "saveConfig", subjectNames, korSubjectNames, true);
                    } catch (ParserConfigurationException | SAXException | IOException e1) {
                        e1.printStackTrace();
                    }
                }
//            } else if(e.getSource() == shortDayBreakTime) {
//                times[0] = String.valueOf(mkJOptionPane("쉬는시간이 몇 분인지 입력해 주세요"));
//                shortSchoolXmlWrite();
//                restart();
//            } else if(e.getSource() == shortDayStudyTime) {
//                times[1] = String.valueOf(mkJOptionPane("수업시간이 몇 분인지 입력해 주세요"));
//                shortSchoolXmlWrite();
//                restart();
            }else if(e.getSource() == shortDay) {
                new shortSchoolGUI();
            } else if(e.getSource() == alarmTime) {
                if(!(ConnectTimeGUI.frame.isActive())) {
                    ConnectTimeGUI.mkSetting(connectTime, connectTimeFile);
                }
            } else if(e.getSource() == timetableMenu) {
                if(!(TimeTableGUI.f.isVisible())) {
                    new TimeTableGUI(timetableFile, korSubjectNames, "timetable", timetableNode, timetable, korToEng);
                }
            } else if(e.getSource() == linkEditFile) {
                String s = new FileChooser().fileChooser(saveConfig).getPath();
                for (int i = 0; i < URLs.length; i++) {
                    try {
                        XMLManager.XMLReader(s, URLs, i);
                    } catch (ParserConfigurationException | SAXException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    XMLManager.XMLWriter(saveConfig, "saveConfig", subjectNames, URLs, true, changeAble);
                } catch (ParserConfigurationException | TransformerException e1) {
                    e1.printStackTrace();
                }
            } else if(e.getSource() == timetableEditFile) {
                String s = new FileChooser().fileChooser(timetableFile).getPath();
                for (int i = 0; i < timetable.length; i++) {
                    try {
                        XMLManager.XMLReader(s, timetable, i);
                    } catch (ParserConfigurationException | SAXException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
                try {
                    XMLManager.XMLWriter(timetableFile, "timetable", timetableNode, timetable, false, null);
                } catch (ParserConfigurationException | TransformerException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public static class ItemEvents implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource() == autoLinking) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    BufferWriteTry("true", autoLinkingFile);
                } else {
                    BufferWriteTry("false", autoLinkingFile);
                }
                Tray.tray.remove(Tray.trayIcon);
                restart();
            }
        }
    }
    public static class ThreadWithRunnable implements Runnable {
        @Override
        public void run() {
            while (true){
                if(manyIF.autoLinkingIF()) {
                    break;
                }
            }
            new Sound(ringingFile);
            Thread.currentThread().interrupt();
        }
    }
    protected static PopupMenu createPopupMenu() {
        openItem.addActionListener(new ActionListeners());
        hideItem.addActionListener(new ActionListeners());
        exitItem.addActionListener(new ActionListeners());
        popup.add(openItem);
        popup.add(hideItem);
        popup.addSeparator();
        popup.add(exitItem);
        return popup;
    }
    public static class ActionListeners implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == openItem) {
                f.setVisible(true);
            }
            if(e.getSource() == hideItem) {
                f.setVisible(false);
            }
            if(e.getSource() == exitItem) {
                System.exit(0);
            }
        }
    }
}
