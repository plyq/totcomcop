package com.filesys.gui;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by plyq on 26.10.16.
 */
public class FolderTableModel extends AbstractTableModel{

    public FolderTableModel(File folder) {
        this.folder = folder;
        this.content = folder.list();
        this.columns = new String[]{"Name", "Size"};
        this.columnClasses = new Class[]{String.class, BitString.class};
        initFileContent();
        sortOrder = new int [2];
    }

    public void update(){
        this.content = folder.list();
        this.columns = new String[]{"Name", "Size"};
        this.columnClasses = new Class[]{String.class, BitString.class};
        initFileContent();
    }

    public ArrayList<File> getFileContent() {
        return fileContent;
    }

    public String getColumnName(int col) { return columns[col]; }
    public Class getColumnClass(int col) { return columnClasses[col]; }

    @Override
    public int getRowCount() {
        if (content == null) return 0;
        return content.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getRowAttributeAt(content[rowIndex], columnIndex);
    }

    public void setFolder(File folder) {
        this.folder = folder;
        this.content = folder.list();
        this.columnClasses = new Class[]{String.class, BitString.class};
        initFileContent();
    }

    public File getFolder() {
        return folder;
    }

    public void sortDataByCol (int iRow){
        Comparator comparator;
        if (sortOrder[iRow] == 0) {
            sortOrder[iRow] = 1;
        } else {
            sortOrder[iRow] = -1 * sortOrder[iRow];
        }
        switch (iRow){
            case 0:
                comparator = new NameComparator();
                break;
            case 1:
                comparator = new SizeComparator();
                break;
            default:
                comparator = new NameComparator();
                break;
        }
        Collections.sort(fileContent, comparator);
        for (int i = 0; i < content.length; i++) {
            content[i] = fileContent.get(i).getName();
        }
    }


    private int[] sortOrder;

    private ArrayList<File> fileContent;
    private File folder;
    private String[] content;
    private String[] columns;
    private Class[] columnClasses;

    private void initFileContent(){
        fileContent = new ArrayList<>();
        if (content != null) {
            for (String filename :
                    content) {
                fileContent.add(new File(folder, filename));
            }
        }
    }

    private class NameComparator implements Comparator<File>{
        @Override
        public int compare(File o1, File o2) {
            boolean isO1Folder = o1.isDirectory();
            boolean isO2Folder = o2.isDirectory();
            if (isO1Folder && !isO2Folder) return -1;
            if (!isO1Folder && isO2Folder) return 1;
            return sortOrder[0] * o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
    }
    private class SizeComparator implements Comparator<File>{
        @Override
        public int compare(File o1, File o2) {
            boolean isO1Folder = o1.isDirectory();
            boolean isO2Folder = o2.isDirectory();
            if (isO1Folder && !isO2Folder) {
                return -1;
            }
            if (!isO1Folder && isO2Folder) {
                return 1;
            }
            if (isO1Folder && isO2Folder) {
                return sortOrder[0] * o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
            BitString bitStringO1 = new BitString(o1.length());
            BitString bitStringO2 = new BitString(o2.length());
            return sortOrder[1] * bitStringO1.compareTo(bitStringO2);
        }
    }

    private Object getRowAttributeAt(String filename, int columnIndex){
        switch (columnIndex){
            case 0:
                return filename;
            case 1:
                File tempFileObj = new File(folder, filename);
                if (tempFileObj.isFile()) {
                    return new BitString(tempFileObj.length());
                }
                return new BitString(null);
            default:
                return null;
        }
    }
    private class BitString extends Number implements Comparable<BitString>{
        public BitString(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }

        @Override
        public String toString() {
            if (value == null) {
                return "";
            }
            return value + "B";
        }

        @Override
        public int compareTo(BitString o) {
            return this.nullToZero().compareTo(o.nullToZero());
        }

        @Override
        public int intValue() {
            return value.intValue();
        }

        @Override
        public long longValue() {
            return value.longValue();
        }

        @Override
        public float floatValue() {
            return value.longValue();
        }

        @Override
        public double doubleValue() {
            return value.doubleValue();
        }

        private Long value;
        private Long nullToZero(){
            if (value == null) {
                return -1L;
            } else {
                return value;
            }
        }
    }
}
