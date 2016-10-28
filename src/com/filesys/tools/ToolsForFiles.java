package com.filesys.tools;

import com.sun.nio.file.ExtendedCopyOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


/**
 * Created by Mashkovsky on 28.10.2016.
 */
public class ToolsForFiles {
    public static class CopyThread extends Thread{
        public CopyThread(File from, File dest) {
            this.from = from;
            this.dest = dest;
        }

        @Override
        public void run() {
            try {
                Files.copy(from.toPath(),
                        dest.toPath().resolve(from.toPath().getFileName()),
                        ExtendedCopyOption.INTERRUPTIBLE,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private File from;
        private File dest;
    }

}
