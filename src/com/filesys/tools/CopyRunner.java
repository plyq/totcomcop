package com.filesys.tools;

import java.io.IOException;

/**
 * Created by Mashkovsky on 28.10.2016.
 */
public class CopyRunner {
    public static void main(String[] args) {
        Copy copy = new Copy();
        String[] args1 = {
           "C:\\Users\\mashkovsky\\Documents\\New Folder (2)\\1.txt",
                "C:\\Users\\mashkovsky\\Documents\\New Folder (2)\\2\\"
        };
        try {
            copy.main(args1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
