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
                    CONTROLLER = new Controller();
                    CONTROLLER.showMainWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Controller getCONTROLLER() {
        return CONTROLLER;
    }
    private static Controller CONTROLLER;
}
