package com.filesys.tools;

import com.filesys.gui.Runner;
import com.sun.nio.file.ExtendedCopyOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


/**
 * Created by Mashkovsky on 28.10.2016.
 */
public class ToolsForFiles {

    public static class CopyThread extends Thread{
        public CopyThread(ArrayList<File> source, File target) {
            this.from = source;
            this.dest = target;
        }

        @Override
        public void run() {
            try {
                Copy.doCopy(from, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<File> from;
        private File dest;
    }

    public static class DeleteThread extends Thread{
        public DeleteThread(ArrayList<File> source) {
            this.from = source;
        }

        @Override
        public void run() {
            try {
                Delete.doDelete(from);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<File> from;
    }

    public static class MoveThread extends Thread{
        public MoveThread(ArrayList<File> source, File target) {
            this.from = source;
            this.dest = target;
        }

        @Override
        public void run() {
            try {
                Move.doMove(from, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<File> from;
        private File dest;
    }

    public static void  infoContentChanged(File target){
        if (Runner.getCONTROLLER().getLeftFolder().equals(target)){
            Runner.getCONTROLLER().getModelLeftTable().update();
            Runner.getCONTROLLER().getModelLeftTable().fireTableDataChanged();
        }
        if (Runner.getCONTROLLER().getRightFolder().equals(target)){
            Runner.getCONTROLLER().getModelRightTable().update();
            Runner.getCONTROLLER().getModelRightTable().fireTableDataChanged();
        }
    }

}
