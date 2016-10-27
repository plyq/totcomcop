package com.filesys.gui;

import javax.swing.table.AbstractTableModel;
import java.io.File;

/**
 * Created by plyq on 26.10.16.
 */
public class FolderTableModel extends AbstractTableModel{

    public FolderTableModel(File folder) {
        this.folder = folder;
        this.content = folder.list();
        this.columns = new String[]{"FileName", "Size"};
        this.columnClasses = new Class[]{String.class, BitString.class};
    }

    public String getColumnName(int col) { return columns[col]; }
    public Class getColumnClass(int col) { return columnClasses[col]; }

    @Override
    public int getRowCount() {
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
        this.columns = new String[]{"FileName", "Size"};
        this.columnClasses = new Class[]{String.class, BitString.class};
    }

    private File folder;
    private String[] content;
    private String[] columns;
    private Class[] columnClasses;
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
