package com.filesys.gui;

import java.awt.*;

/**
 * Created by plyq on 26.10.16.
 */
public class Runner {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Controller controller = new Controller();
                    controller.showMainWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
