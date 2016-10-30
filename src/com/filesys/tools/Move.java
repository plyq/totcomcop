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
public class Move {

    static void moveFile(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException x) {
            System.err.format("Unable to move: %s: %s%n", source, x);
        }
    }

    static class TreeMover implements FileVisitor<Path> {
        private final Path source;
        private final Path target;

        TreeMover(Path source, Path target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            Path newdir = target.resolve(source.relativize(dir));
            try {
                Files.move(dir, newdir);
            } catch (FileAlreadyExistsException x) {
                // ignore
            } catch (IOException x) {
                System.err.format("Unable to create: %s: %s%n", newdir, x);
                return SKIP_SUBTREE;
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            moveFile(file, target.resolve(source.relativize(file)));
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return CONTINUE;
        }

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


    public static void doMove(ArrayList<File> source, File target) throws IOException {
        // check if target is a directory
        boolean isDir = target.isDirectory();

        // copy each source file/directory to target
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
