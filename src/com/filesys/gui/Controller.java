package com.filesys.gui;

import com.filesys.tools.ToolsForFiles;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by plyq on 26.10.16.
 */
public class Controller {

    //Simple constructor
    public Controller() {
        initComponents();
        initListeners();
    }

    public void showMainWindow(){
        mainWindow.setVisible(true);
    } //show main frame

    public FolderTableModel getModelLeftTable() {
        return modelLeftTable;
    }

    public FolderTableModel getModelRightTable() { return modelRightTable; }

    public File getRightFolder() { return rightFolder; }

    public File getLeftFolder() {
        return leftFolder;
    }

    private void initComponents(){
        //init main frame
        mainWindow = new MainWindow();

        //left/right shown folder File var. Set up default value
        leftFolder = new File(System.getProperty("user.home"));
        rightFolder = new File(System.getProperty("user.home"));

        //text, that show current folder
        mainWindow.getLeftFullDestTextArea().setText(leftFolder.getAbsolutePath());
        mainWindow.getRightFullDestTextArea().setText(rightFolder.getAbsolutePath());

        //init left/right tables with its models
        modelLeftTable = new FolderTableModel(leftFolder);
        modelRightTable = new FolderTableModel(rightFolder);
        mainWindow.getLeftTable().setModel(modelLeftTable);
        mainWindow.getRightTable().setModel(modelRightTable);
        //some beauty: fix "size" column width.
        mainWindow.getLeftTable().getColumnModel().getColumn(1).setMaxWidth(100);
        mainWindow.getLeftTable().getColumnModel().getColumn(1).setMinWidth(100);
        mainWindow.getLeftTable().getColumnModel().getColumn(1).setPreferredWidth(100);
        mainWindow.getRightTable().getColumnModel().getColumn(1).setMaxWidth(100);
        mainWindow.getRightTable().getColumnModel().getColumn(1).setMinWidth(100);
        mainWindow.getRightTable().getColumnModel().getColumn(1).setPreferredWidth(100);
        //some beauty: split folders and files by color
        mainWindow.getLeftTable().getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        mainWindow.getRightTable().getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        //add buttons:
        //go to parent folder
        backLeftBtn = mainWindow.getBackLeftBtn();
        backRightBtn = mainWindow.getBackRightBtn();
        //copy one side selection to other side
        copyLeftBtn = mainWindow.getCopyLeftBtn();
        copyRightBtn = mainWindow.getCopyRightBtn();
        //delete selection
        delLeftBtn = mainWindow.getDelLeftBtn();
        delRightBtn = mainWindow.getDelRightBtn();
        //move selection from one side to other
        movLeftBtn = mainWindow.getMovLeftBtn();
        movRightBtn = mainWindow.getMovRightBtn();
        //add new folder: button
        leftAddBtn = mainWindow.getLeftAddBtn();
        rightAddBtn = mainWindow.getRightAddBtn();
        //add new folder: dialog: name of new  folder
        addNewFolderLeftDialog = new AddNewFolderDialog(mainWindow, "Add folder");
        addNewFolderRightDialog = new AddNewFolderDialog(mainWindow, "Add folder");
    }

    private void initListeners(){ //apply listeners to buttons and tables
        listeners = new Listeners();
        backLeftBtn.addActionListener(listeners.getBackLeftBtnListener());
        backRightBtn.addActionListener(listeners.getBackRightBtnListener());
        mainWindow.getLeftTable().addMouseListener(listeners.getTableListener());
        mainWindow.getRightTable().addMouseListener(listeners.getTableListener());
        mainWindow.getLeftTable().getTableHeader().addMouseListener(listeners.getHeaderListener());
        mainWindow.getRightTable().getTableHeader().addMouseListener(listeners.getHeaderListener());
        copyLeftBtn.addActionListener(listeners.getCopyLeftBtnListener());
        copyRightBtn.addActionListener(listeners.getCopyRightBtnListener());
        delLeftBtn.addActionListener(listeners.getDeleteLeftBtnListener());
        delRightBtn.addActionListener(listeners.getDeleteRightBtnListener());
        movLeftBtn.addActionListener(listeners.getMoveLeftBtnListener());
        movRightBtn.addActionListener(listeners.getMoveRightBtnListener());
        leftAddBtn.addActionListener(listeners.getAddLeftBtnListener());
        rightAddBtn.addActionListener(listeners.getAddRightBtnListener());
        addNewFolderLeftDialog.getConfirmBtn().addActionListener(listeners.getAddLeftDialogListener());
        addNewFolderRightDialog.getConfirmBtn().addActionListener(listeners.getAddRightDialogListener());
    }

    private MainWindow mainWindow;
    private FolderTableModel modelLeftTable;
    private FolderTableModel modelRightTable;
    private File leftFolder;
    private File rightFolder;
    private JButton backLeftBtn;
    private JButton backRightBtn;
    private Listeners listeners;
    private RenderFolderFile cellRenderer = new RenderFolderFile();
    private JButton copyLeftBtn;
    private JButton copyRightBtn;
    private JButton delLeftBtn;
    private JButton delRightBtn;
    private JButton movLeftBtn;
    private JButton movRightBtn;
    private JButton leftAddBtn;
    private JButton rightAddBtn;
    private AddNewFolderDialog addNewFolderLeftDialog;
    private AddNewFolderDialog addNewFolderRightDialog;

    private class Listeners {

        //getters for all listeners
        private BackLeftBtnListener getBackLeftBtnListener() { return new BackLeftBtnListener(); }
        private BackRightBtnListener getBackRightBtnListener() { return new BackRightBtnListener(); }
        private CopyLeftBtnListener getCopyLeftBtnListener() { return new CopyLeftBtnListener(); }
        private CopyRightBtnListener getCopyRightBtnListener() { return new CopyRightBtnListener(); }
        private DeleteLeftBtnListener getDeleteLeftBtnListener() { return new DeleteLeftBtnListener(); }
        private DeleteRightBtnListener getDeleteRightBtnListener() { return new DeleteRightBtnListener(); }
        private MoveLeftBtnListener getMoveLeftBtnListener() { return new MoveLeftBtnListener(); }
        private MoveRightBtnListener getMoveRightBtnListener() { return new MoveRightBtnListener(); }
        private AddLeftBtnListener getAddLeftBtnListener() { return new AddLeftBtnListener(); }
        private AddRightBtnListener getAddRightBtnListener() { return new AddRightBtnListener(); }
        private AddLeftDialogListener getAddLeftDialogListener() { return new AddLeftDialogListener(); }
        private AddRightDialogListener getAddRightDialogListener() { return new AddRightDialogListener(); }


        //sorting listener
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

        //table listener. 2click  open
        private MouseAdapter getTableListener() {
            return new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    JTable table =(JTable) me.getSource();
                    Point p = me.getPoint();
                    int iRow = table.rowAtPoint(p);
                    int iCol = table.columnAtPoint(p);
                    File currentFolder = ((FolderTableModel) table.getModel()).getFolder();
                    File destinationFolder = new File(currentFolder, (String) table.getModel().getValueAt(iRow, iCol));
                    if (me.getClickCount() == 2 && iCol == 0 && destinationFolder.isDirectory()) {
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

        //above listeners for left and right button panels
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

        private class CopyLeftBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getLeftTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelLeftTable.getFileContent().get(i));
                }
                ToolsForFiles.CopyThread copyThread = new ToolsForFiles.CopyThread(fileContent, rightFolder);
                copyThread.start();
            }
        }

        private class CopyRightBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getRightTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelRightTable.getFileContent().get(i));
                }
                ToolsForFiles.CopyThread copyThread = new ToolsForFiles.CopyThread(fileContent, leftFolder);
                copyThread.start();
            }
        }

        private class DeleteLeftBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getLeftTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelLeftTable.getFileContent().get(i));
                }
                ToolsForFiles.DeleteThread delThread = new ToolsForFiles.DeleteThread(fileContent);
                delThread.start();
            }
        }

        private class DeleteRightBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getRightTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelRightTable.getFileContent().get(i));
                }
                ToolsForFiles.DeleteThread delThread = new ToolsForFiles.DeleteThread(fileContent);
                delThread.start();
            }
        }

        private class MoveLeftBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getLeftTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelLeftTable.getFileContent().get(i));
                }
                ToolsForFiles.MoveThread moveThread = new ToolsForFiles.MoveThread(fileContent, rightFolder);
                moveThread.start();
            }
        }

        private class MoveRightBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rowsToCopy = mainWindow.getRightTable().getSelectedRows();
                ArrayList<File> fileContent = new ArrayList<>();
                for (int i :
                        rowsToCopy) {
                    fileContent.add(modelRightTable.getFileContent().get(i));
                }
                ToolsForFiles.MoveThread moveThread = new ToolsForFiles.MoveThread(fileContent, leftFolder);
                moveThread.start();
            }
        }

        private class AddLeftBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!addNewFolderLeftDialog.isVisible()) {
                    addNewFolderLeftDialog.setVisible(true);
                }
            }
        }

        private class AddRightBtnListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!addNewFolderRightDialog.isVisible()) {
                    addNewFolderRightDialog.setVisible(true);
                }
            }
        }

        private class AddLeftDialogListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = addNewFolderLeftDialog.getTextFieldText();
                File folderToAdd = new File(leftFolder.getAbsolutePath(), text);
                try{
                    folderToAdd.mkdir();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                modelLeftTable.update();
                modelLeftTable.fireTableDataChanged();
                addNewFolderLeftDialog.setVisible(false);
            }
        }

        private class AddRightDialogListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = addNewFolderRightDialog.getTextFieldText();
                File folderToAdd = new File(rightFolder.getAbsolutePath(), text);
                try{
                    folderToAdd.mkdir();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                modelRightTable.update();
                modelRightTable.fireTableDataChanged();
                addNewFolderRightDialog.setVisible(false);
            }
        }
    }

    private final class RenderFolderFile extends DefaultTableCellRenderer {

        @Override public Component getTableCellRendererComponent(
                JTable aTable, Object aFile, boolean aIsSelected,
                boolean aHasFocus, int aRow, int aColumn
        ) {
            if (aFile == null) return this;
            Component renderer = super.getTableCellRendererComponent(
                    aTable, aFile, aIsSelected, aHasFocus, aRow, aColumn
            );
            String fileName = (String) aFile;
            File file = new File(((FolderTableModel) aTable.getModel()).getFolder(), fileName);
            if (file.isDirectory()) {
                renderer.setForeground(brownColor);
            }
            else if (file.isFile()) {
                renderer.setForeground(darkblueColor);
            }
            else {
                renderer.setForeground(Color.BLACK);
            }
            return this;
        }

        private Color brownColor = new Color(80, 50, 20);
        private Color darkblueColor = new Color(15, 40, 70);
    }
}
