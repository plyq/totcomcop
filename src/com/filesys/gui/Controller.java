package com.filesys.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        mainWindow.getLeftFullDestTextArea().setText(leftFolder.getAbsolutePath());
        mainWindow.getRightFullDestTextArea().setText(rightFolder.getAbsolutePath());
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
        mainWindow.getLeftTable().addMouseListener(listeners.getTableListener());
        mainWindow.getRightTable().addMouseListener(listeners.getTableListener());
        mainWindow.getLeftTable().getTableHeader().addMouseListener(listeners.getHeaderListener());
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

        private MouseAdapter getHeaderListener() {
            return new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTableHeader tableHeader =(JTableHeader) me.getSource();
                    JTable table = tableHeader.getTable();
                    Point p = me.getPoint();
                    int iCol = tableHeader.columnAtPoint(p);
                    if (me.getClickCount() == 2) {
                        ((FolderTableModel) table.getModel()).sortDataByCol(iCol);
                        ((FolderTableModel) table.getModel()).fireTableDataChanged();
                    }
                }
            };
        }

        private MouseAdapter getTableListener() {
            return new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table =(JTable) me.getSource();
                    Point p = me.getPoint();
                    int iRow = table.rowAtPoint(p);
                    int iCol = table.columnAtPoint(p);
                    if (me.getClickCount() == 2 && iCol == 0) {
                        File currentFolder = ((FolderTableModel) table.getModel()).getFolder();
                        File destinationFolder = new File(currentFolder, (String) table.getModel().getValueAt(iRow, iCol));
                        ((FolderTableModel) table.getModel()).setFolder(destinationFolder);
                        leftFolder = modelLeftTable.getFolder();
                        rightFolder = modelRightTable.getFolder();
                        ((FolderTableModel) table.getModel()).fireTableDataChanged();
                        mainWindow.getLeftFullDestTextArea().setText(String.valueOf(leftFolder));
                        mainWindow.getRightFullDestTextArea().setText(String.valueOf(rightFolder));
                    }
                }
            };
        }

        private class BackLeftBtnListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                File parent = leftFolder.getParentFile();
                if (parent != null) {
                    leftFolder = parent;
                    modelLeftTable.setFolder(parent);
                    modelLeftTable.fireTableDataChanged();
                    mainWindow.getLeftFullDestTextArea().setText(String.valueOf(leftFolder));
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
                    mainWindow.getRightFullDestTextArea().setText(String.valueOf(rightFolder));
                }
            }
        }
    }
}
