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
 * Created by Mashkovsky on 28.10.2016.
 */
//Thanks oracle documentation. This class contains walking on tree and copying files one by one.
public class Copy {

    //simply copy file
    static void copyFile(Path source, Path target) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException x) {
            System.err.format("Unable to copy: %s: %s%n", source, x);
        }
    }

    //tree class
    static class TreeCopier implements FileVisitor<Path> {
        private final Path source;
        private final Path target;

        TreeCopier(Path source, Path target) {
            this.source = source;
            this.target = target;
        }

        //Copy folder, when visit it
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

        //Copy file when visit it
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            copyFile(file, target.resolve(source.relativize(file)));
            return CONTINUE;
        }

        //Do nothing when working with folder and folder content ended
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return CONTINUE;
        }

        //Check some bad cases
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            if (exc instanceof FileSystemLoopException) {
                System.err.println("cycle detected: " + file);
            } else {
                System.err.format("Unable to copy: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }


    //main method for using external
    public static void doCopy(ArrayList<File> source, File target) throws IOException {
        // check if target is a directory
        boolean isDir = target.isDirectory();

        // copy each source file/directory to target
        for (File file : source) {
            Path dest = (isDir) ? target.toPath().resolve(file.toPath().getFileName()) : target.toPath();

            EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            TreeCopier tc = new TreeCopier(file.toPath(), dest);
            Files.walkFileTree(file.toPath(), opts, Integer.MAX_VALUE, tc);
            ToolsForFiles.infoContentChanged(target);
        }

    }
}


