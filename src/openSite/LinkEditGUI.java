package openSite;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LinkEditGUI {
    static JFrame frame= new JFrame();
    static boolean[] changeAble = new boolean[MainGUI.URLs.length];
    static JPanel panel = new JPanel(new GridBagLayout());
    static JButton OKButton = new JButton("OK");
    static JButton CancelButton = new JButton("Cancel");
    static GridBagConstraints construe = new GridBagConstraints();
    static JLabel[] subjectLabels = new JLabel[MainGUI.URLs.length];
    static JTextField[] subjects = new JTextField[MainGUI.URLs.length];
    static JCheckBox[] toChangeAtt = new JCheckBox[MainGUI.URLs.length];
    static File file = new File("C:\\Temp\\ZSchedule\\files\\saveConfig.xml");

    public static void mkEditor(String[] URLs, File saveConfig, String txt, String[] subjectNames, String[] korSubjectNames, boolean AttAble) throws ParserConfigurationException, IOException, SAXException {
        construe.insets = new Insets(5, 5, 5, 5);
        construe.anchor = GridBagConstraints.CENTER;
        construe.gridx=0;
        construe.gridy=0;
        for (int i = 0; i < subjects.length; i++) {
            changeAble[i] = Boolean.parseBoolean(XMLManager.XMLReader(file.getPath(), URLs, i));
            toChangeAtt[i] = new JCheckBox("", changeAble[i]);
            subjects[i] = new JTextField(20);
            subjects[i].setText(URLs[i]);
            subjectLabels[i] = new JLabel(korSubjectNames[i]);
            panel.add(subjectLabels[i], construe);
            construe.gridx=1;
            panel.add(subjects[i], construe);
            construe.gridx=2;
            panel.add(toChangeAtt[i], construe);
            construe.gridx=0; construe.gridy=i+1;
        }
        OKButton.addActionListener(e -> {
            for (int i = 0; i < subjects.length; i++) {
                changeAble[i] = toChangeAtt[i].isSelected();
                URLs[i] = subjects[i].getText();
            }
            try {
                XMLManager.XMLWriter(saveConfig, txt, subjectNames, URLs, AttAble, changeAble);
            } catch (ParserConfigurationException | TransformerException e1) {
                e1.printStackTrace();
            }
            MainGUI.restart();
        });
        CancelButton.addActionListener(e -> frame.dispose());
        construe.gridx=1;
        panel.add(OKButton, construe);
        construe.gridx=2;
        panel.add(CancelButton, construe);

        frame.add(panel);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
