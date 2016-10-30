package com.filesys.gui;

import javax.swing.*;

/**
 * Created by plyq on 26.10.16.
 */
public class MainWindow extends JFrame {

    public MainWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    public JTable getLeftTable() {
        return leftTable;
    }

    public JTable getRightTable() {
        return rightTable;
    }

    public JButton getBackRightBtn() {
        return backRightBtn;
    }

    public JButton getBackLeftBtn() {
        return backLeftBtn;
    }

    public JTextArea getLeftFullDestTextArea() {
        return leftFullDestTextArea;
    }

    public JTextArea getRightFullDestTextArea() {
        return rightFullDestTextArea;
    }

    public JButton getCopyLeftBtn() {
        return copyLeftBtn;
    }

    public JButton getCopyRightBtn() {
        return copyRightBtn;
    }

    public JButton getLeftAddBtn() {
        return leftAddBtn;
    }

    public JButton getRightAddBtn() {
        return rightAddBtn;
    }

    public JButton getDelLeftBtn() {
        return delLeftBtn;
    }

    public JButton getDelRightBtn() {
        return delRightBtn;
    }

    public JButton getMovLeftBtn() {
        return movLeftBtn;
    }

    public JButton getMovRightBtn() {
        return movRightBtn;
    }

    private JTable leftTable;
    private JTable rightTable;
    private JPanel mainPanel;
    private JScrollPane leftTableScroll;
    private JScrollPane rightTableScroll;
    private JButton backLeftBtn;
    private JButton backRightBtn;
    private JTextArea leftFullDestTextArea;
    private JTextArea rightFullDestTextArea;
    private JButton copyLeftBtn;
    private JButton copyRightBtn;
    private JButton leftAddBtn;
    private JButton rightAddBtn;
    private JButton delLeftBtn;
    private JButton delRightBtn;
    private JButton movLeftBtn;
    private JButton movRightBtn;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

}

class AddNewFolderDialog extends JDialog {
    public JButton getConfirmBtn() {
        return confirmBtn;
    }

    public AddNewFolderDialog(JFrame frame, String title) {
        super(frame, title, false);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Choose folder name"));
        panel.add(textfield);
        panel.add(confirmBtn);

        add(panel);
        pack();
        setLocationRelativeTo(frame);
    }

    public String getTextFieldText() {
        return textfield.getText();
    }

    private JTextField textfield = new JTextField(10);
    private JButton confirmBtn = new JButton("OK");
}