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

//This class contains walking on tree and moving files one by one.
public class Move {

    //simple move file
    static void moveFile(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException x) {
            System.err.format("Unable to move: %s: %s%n", source, x);
        }
    }

    //tree class
    static class TreeMover implements FileVisitor<Path> {
        private final Path source;
        private final Path target;

        TreeMover(Path source, Path target) {
            this.source = source;
            this.target = target;
        }


        //lets just copy a folder when visit it firstly
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            Path newdir = target.resolve(source.relativize(dir));
            try {
                Files.copy(dir, newdir);
            } catch (FileAlreadyExistsException x) {
                // ignore
            } catch (IOException x) {
                System.err.format("Unable to create: %s: %s%n", newdir, x);
                return SKIP_SUBTREE;
            }
            return CONTINUE;
        }

        //when we visit file - just move it
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            moveFile(file, target.resolve(source.relativize(file)));
            return CONTINUE;
        }

        //when we visited all content of folder we can delete it
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
                System.err.format("Unable to move: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }


    public static void doMove(ArrayList<File> source, File target) throws IOException {
        // check if target is a directory
        boolean isDir = target.isDirectory();

        // move each source file/directory to target
        for (File file : source) {
            Path dest = (isDir) ? target.toPath().resolve(file.toPath().getFileName()) : target.toPath();

            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Move.TreeMover tm = new Move.TreeMover(file.toPath(), dest);
            Files.walkFileTree(file.toPath(), opts, Integer.MAX_VALUE, tm);
            ToolsForFiles.infoContentChanged(target);
            ToolsForFiles.infoContentChanged(file.getParentFile());
        }

    }
}
