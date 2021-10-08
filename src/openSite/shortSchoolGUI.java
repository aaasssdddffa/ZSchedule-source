package openSite;

import javax.swing.*;
import java.awt.*;

public class shortSchoolGUI {
    static JFrame frame = new JFrame();
    static JPanel nameGroup = new JPanel(new GridBagLayout());
    static JPanel interactionGroup = new JPanel();
    static String[] nameString = new String[]{"링크", "수업시간", "쉬는시간", "연결 시간"};
    static JLabel[] nameLabel = new JLabel[nameString.length];
    static GridBagConstraints c = new GridBagConstraints();
    static Container pane = frame.getContentPane();
    shortSchoolGUI() {
        c.insets = new Insets(10, 10, 10, 10);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx=0;
        c.gridy=0;
        interactionGroup.setPreferredSize(new Dimension(300, 150));
        nameGroup.setPreferredSize(new Dimension(100, 150));
        for (int i = 0; i < nameLabel.length; i++) {
            nameLabel[i] = new JLabel();
            nameLabel[i].setText(nameString[i]);
            nameGroup.add(nameLabel[i], c);
            c.gridy=i+1;
        }
        pane.add(interactionGroup, BorderLayout.CENTER);
        pane.add(nameGroup, BorderLayout.LINE_START);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
