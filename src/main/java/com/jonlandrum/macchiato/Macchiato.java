package com.jonlandrum.macchiato;

import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.*;
import dorkbox.util.CacheUtil;

import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Macchiato {
    public static void main(String[] args) {
        if(Arrays.asList(args).contains("debug")) {
            SystemTray.DEBUG = true;
            CacheUtil.clear();
        }
        
        SystemTray systemTray = SystemTray.get();
        if(null == systemTray) {
            throw new RuntimeException("Unable to load SystemTray");
        }
        
        URL icon = Macchiato.class.getResource("/hot_beverage.png");
        systemTray.setImage(icon);
        systemTray.setStatus("Macchiato");
        
        systemTray.getMenu().add(new MenuItem("Autostart", e -> {
        
        })).setShortcut('a');
        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(new MenuItem("Quit", e -> {
            systemTray.shutdown();
        })).setShortcut('q');
        
        while(true) {
            try {
                doTheThing();
                TimeUnit.MINUTES.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void doTheThing() {
        try {
            Robot robot = new Robot();
            Point location = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove(location.x + 1, location.y + 1);
            robot.mouseMove(location.x - 1, location.y - 1);
        } catch(AWTException e) {
            e.printStackTrace();
        }
    }
}
