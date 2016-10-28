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

    public void setLeftTable(JTable leftTable) {
        this.leftTable = leftTable;
    }

    public JTable getRightTable() {
        return rightTable;
    }

    public void setRightTable(JTable rightTable) {
        this.rightTable = rightTable;
    }

    public JScrollPane getLeftTableScroll() {
        return leftTableScroll;
    }

    public void setLeftTableScroll(JScrollPane leftTableScroll) {
        this.leftTableScroll = leftTableScroll;
    }

    public JScrollPane getRightTableScroll() {
        return rightTableScroll;
    }

    public void setRightTableScroll(JScrollPane rightTableScroll) {
        this.rightTableScroll = rightTableScroll;
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

    private JTable leftTable;
    private JTable rightTable;
    private JPanel mainPanel;
    private JScrollPane leftTableScroll;
    private JScrollPane rightTableScroll;
    private JButton backLeftBtn;
    private JButton backRightBtn;
    private JTextArea leftFullDestTextArea;
    private JTextArea rightFullDestTextArea;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

}
