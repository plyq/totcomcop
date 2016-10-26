package com.filesys.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.io.File;

/**
 * Created by plyq on 26.10.16.
 */
public class Controller {
    public Controller() {
        initComponents();
    }

    public void showMainWindow(){
        mainWindow.setVisible(true);
    }

    private void initComponents(){
        mainWindow = new MainWindow();
        leftFolder = new File(System.getProperty("user.home"));
        rightFolder = new File(System.getProperty("user.home"));
        modelLeftTable = new FolderTableModel(leftFolder);
        modelRighttTable = new FolderTableModel(rightFolder);
        mainWindow.getLeftTable().setModel(modelLeftTable);
        mainWindow.getRightTable().setModel(modelRighttTable);
    }

    public FolderTableModel getModelLeftTable() {
        return modelLeftTable;
    }

    private MainWindow mainWindow;
    private FolderTableModel modelLeftTable;
    private FolderTableModel modelRighttTable;
    private File leftFolder;
    private File rightFolder;
}
