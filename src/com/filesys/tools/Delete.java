package com.filesys.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

/**
 * Created by plyq on 30.10.16.
 */

//This class contains walking on tree and deleting files one by one.
public class Delete {

    //simple delete file
    static void deleteFile(Path source) {
        try {
            Files.delete(source);
        } catch (IOException e) {
            System.err.format("Unable to delete: %s: %s%n", source, e);
        }
    }


    //tree class for deleting files and folders
    static class TreeDeleter implements FileVisitor<Path> {
        private final Path source;

        TreeDeleter(Path source) {
            this.source = source;
        }

        //when visit a folder do nothing, cause we previously needed to delete all content of folder
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return CONTINUE;
        }

        //delete file
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            deleteFile(file);
            return CONTINUE;
        }

        //delete folder, when we delete all content
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            try {
                Files.delete(dir);
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such" + " file or directory%n", dir);
            } catch (DirectoryNotEmptyException x) {
                System.err.format("%s not empty%n", dir);
            } catch (IOException x) {
                // File permission problems are caught here.
                System.err.println(x);
                return SKIP_SUBTREE;
            }
            return CONTINUE;
        }


        //Check some bad cases
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            if (exc instanceof FileSystemLoopException) {
                System.err.println("cycle detected: " + file);
            } else {
                System.err.format("Unable to delete: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }

    //main method for using external
    public static void doDelete(ArrayList<File> source) throws IOException {
        for (File file : source) {
            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Delete.TreeDeleter td = new Delete.TreeDeleter(file.toPath());
            Files.walkFileTree(file.toPath(), opts, Integer.MAX_VALUE, td);
            ToolsForFiles.infoContentChanged(file.getParentFile());
        }
    }
}
