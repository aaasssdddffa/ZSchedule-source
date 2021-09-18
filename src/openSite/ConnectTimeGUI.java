package openSite;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;

public class ConnectTimeGUI {
    static JFrame frame = new JFrame();
    static JPanel panel = new JPanel(new GridBagLayout());
    static JLabel[] text = new JLabel[]{new JLabel("오픈시간"), new JLabel("                |"), new JLabel("분 전에 오픈")};
    static JTextField tf = new JTextField(5);
    static JButton resetButton = new JButton("Reset");
    static JButton OKButton = new JButton("OK");
    static JButton CancelButton = new JButton("Cancel");
    static GridBagConstraints construe = new GridBagConstraints();

    public static void mkSetting(int connectTime, File file) {
        final String[] s = new String[]{String.valueOf(connectTime)};
        resetButton.setFont(new Font("", Font.BOLD, 10));
        text[1].setSize(10, 10);
        tf.setText(String.valueOf(connectTime));
        construe.insets = new Insets(5, 5, 5, 5);
        construe.anchor = GridBagConstraints.CENTER;
        construe.gridx=0;
        construe.gridy=0;
        panel.add(text[0], construe);
        panel.add(text[1], construe);
        construe.gridx=1;
        panel.add(tf, construe);
        construe.gridx=2;
        panel.add(text[2], construe);
        construe.gridx=3;
        construe.insets = new Insets(5, 40, 5, 5);
        panel.add(resetButton, construe);
        construe.gridy=1;
        construe.gridx=0;
        panel.add(OKButton, construe);
        construe.gridx=1;
        panel.add(CancelButton, construe);
        frame.add(panel);
        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        resetButton.addActionListener(e -> {
            MainGUI.mkJOptionPane("정말로 초기화 하시겠습니까?", "Notification", JOptionPane.OK_CANCEL_OPTION);
            tf.setText(String.valueOf(connectTime));
        });
        OKButton.addActionListener(e -> {
            s[0] = tf.getText();
            try {
                XMLManager.XMLWriter(file, "setting", new String[]{"contentTime"}, s, false, null);
            } catch (ParserConfigurationException | TransformerException ex) {
                ex.printStackTrace();
            }
            MainGUI.restart();
        });
        CancelButton.addActionListener(e -> frame.dispose());
    }
}
