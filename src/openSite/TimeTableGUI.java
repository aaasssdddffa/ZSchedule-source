package openSite;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static openSite.MainGUI.mk2DArr;

public class TimeTableGUI extends JFrame {
    static JFrame f = new JFrame();
    static JList<String> sourceList = new JList<>(new DefaultListModel<>());
    static JLabel[] dayList = new JLabel[]{new JLabel("Mon"), new JLabel("Tus"), new JLabel("Wed"), new JLabel("Tur"), new JLabel("Fri")};
    static JTextField[][] newTextField = new JTextField[5][7];
    static JButton OKButton = new JButton("OK");
    static JButton CancelButton = new JButton("Cancel");
    static Box HBox = Box.createHorizontalBox();
    static Box[] MainVBox = new Box[5];
    static int[] dayGaps = new int[]{30, 50, 55, 55, 55, 35};
    static int[] buttonGaps = new int[]{100, 50, 100};
    static Box dayBox = Box.createHorizontalBox();
    static Box sourceBox = Box.createVerticalBox();
    static Box listBox = Box.createHorizontalBox();
    static Box buttonBox = Box.createHorizontalBox();
    static Box allBox = Box.createVerticalBox();
    public TimeTableGUI(File file, String[] names, String eleName, String[] nodeName, String[] timetable, HashMap<String, String> korToEng) {
        OKButton.setFont(new Font("", Font.BOLD, 15));
        CancelButton.setFont(new Font("", Font.BOLD, 15));
        for (int i = 0; i < MainGUI.URLs.length; i++) {
            ((DefaultListModel<String>) sourceList.getModel()).add(i, names[i]);
        }
        for (int i = 0; i < 5; i++) {
            MainVBox[i] = Box.createVerticalBox();
            for (int j = 0; j < 7; j++) {
                newTextField[i][j] = new JTextField(5);
                newTextField[i][j].setText(mk2DArr(timetable, 5, 7)[i][j]);
                newTextField[i][j].setFont(new Font("맑은 고딕", Font.PLAIN, 17));
                MainVBox[i].add(newTextField[i][j]);
            }
            HBox.add(MainVBox[i]);
        }
        for (int i = 0; i < dayList.length; i++) {
            dayList[i].setFont(new Font("휴먼모음T", Font.PLAIN, 20));
            dayBox.add(Box.createRigidArea(new Dimension(dayGaps[i], 0)));
            dayBox.add(dayList[i]);
        }
        dayBox.add(Box.createRigidArea(new Dimension(dayGaps[5], 0)));
        buttonBox.add(Box.createRigidArea(new Dimension(buttonGaps[0], 0)));
        buttonBox.add(OKButton);
        buttonBox.add(Box.createRigidArea(new Dimension(buttonGaps[1], 0)));
        buttonBox.add(CancelButton);
        buttonBox.add(Box.createRigidArea(new Dimension(buttonGaps[2], 0)));
        sourceBox.add(new JLabel("Class"));
        sourceBox.add(new JScrollPane(sourceList));
        listBox.add(sourceBox);
        allBox.add(dayBox);
        allBox.add(HBox);
        allBox.add(listBox);
        allBox.add(Box.createRigidArea(new Dimension(0, 10)));
        allBox.add(buttonBox);
        allBox.add(Box.createRigidArea(new Dimension(0, 10)));
        f.getContentPane().add(allBox, BorderLayout.CENTER);
        sourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sourceList.setFont(new Font("HY헤드라인M", Font.PLAIN,15));
        sourceList.setDragEnabled(true);
        sourceList.setDropMode(DropMode.INSERT);
        sourceList.setTransferHandler(new ListTransferHandler());

        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        OKButton.addActionListener(e -> {
            boolean b = true;
            String[][] gt = new String[newTextField.length][newTextField[0].length];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if(!(j==6) && newTextField[i][j].getText().equals("")) {
                        b = false;
                    }
                    gt[i][j] = newTextField[i][j].getText();
                }
            }
            for (int i = 0; i < gt.length; i++) {
                System.arraycopy(gt[i], 0, timetable, gt[i].length * i, gt[i].length);
            }
            for (int i = 0; i < timetable.length; i++) {
                timetable[i] = korToEng.get(timetable[i]);
            }
            try {
                XMLManager.XMLWriter(file, eleName, nodeName, timetable, false, null);
            } catch (ParserConfigurationException | TransformerException e1) {
                e1.printStackTrace();
            }
            if (b) {
                MainGUI.restart();
            } else {
                MainGUI.mkJOptionPane("7교시 이외의 값들은 채워져 있어야 합니다.", "Notification", JOptionPane.ERROR_MESSAGE);
            }
        });
        CancelButton.addActionListener(e -> MainGUI.restart());
    }
    static class ListTransferHandler extends TransferHandler {
        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }
        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            @SuppressWarnings("unchecked")
            JList<String> sourceList = (JList<String>) source;
            String movedItem = sourceList.getSelectedValue();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    String gt = newTextField[i][j].getText();
                    if(!gt.equals(movedItem) && gt.length() > movedItem.length() && gt.length() > 3) {
                        newTextField[i][j].setText(movedItem);
                    }
                }
            }
        }
        @Override
        protected Transferable createTransferable(JComponent source) {
            @SuppressWarnings("unchecked")
            JList<String> sourceList = (JList<String>) source;
            String data = sourceList.getSelectedValue();
            return new StringSelection(data);
        }
        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            }
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            return this.canImport(support);
        }
    }
}