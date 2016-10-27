package com.filesys.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by plyq on 26.10.16.
 */
public class Controller {
    public Controller() {
        initComponents();
        initListeners();
    }

    public void showMainWindow(){
        mainWindow.setVisible(true);
    }
    public FolderTableModel getModelLeftTable() {
        return modelLeftTable;
    }

    public File getRightFolder() {
        return rightFolder;
    }

    public void setRightFolder(File rightFolder) {
        this.rightFolder = rightFolder;
    }

    public File getLeftFolder() {
        return leftFolder;
    }

    public void setLeftFolder(File leftFolder) {
        this.leftFolder = leftFolder;
    }

    private void initComponents(){
        mainWindow = new MainWindow();
        leftFolder = new File(System.getProperty("user.home"));
        rightFolder = new File(System.getProperty("user.home"));
        modelLeftTable = new FolderTableModel(leftFolder);
        modelRightTable = new FolderTableModel(rightFolder);
        mainWindow.getLeftTable().setModel(modelLeftTable);
        mainWindow.getRightTable().setModel(modelRightTable);
        backLeftBtn = mainWindow.getBackLeftBtn();
        backRightBtn = mainWindow.getBackRightBtn();
    }

    private void initListeners(){
        listeners = new Listeners();
        backLeftBtn.addActionListener(listeners.getBackLeftBtnListener());
        backRightBtn.addActionListener(listeners.getBackRightBtnListener());
    }

    private MainWindow mainWindow;
    private FolderTableModel modelLeftTable;
    private FolderTableModel modelRightTable;
    private File leftFolder;
    private File rightFolder;
    private JButton backLeftBtn;
    private JButton backRightBtn;
    private Listeners listeners;

    private class Listeners {

        private BackLeftBtnListener getBackLeftBtnListener(){
            return new BackLeftBtnListener();
        }
        private BackRightBtnListener getBackRightBtnListener(){
            return new BackRightBtnListener();
        }

        private class BackLeftBtnListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                File parent = leftFolder.getParentFile();
                if (parent != null) {
                    leftFolder = parent;
                    modelLeftTable.setFolder(parent);
                    modelLeftTable.fireTableDataChanged();
                }
            }
        }
        private class BackRightBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                File parent = rightFolder.getParentFile();
                if (parent != null) {
                    rightFolder = parent;
                    modelRightTable.setFolder(parent);
                    modelRightTable.fireTableDataChanged();
                }
            }
        }
    }
}
