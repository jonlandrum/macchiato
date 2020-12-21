package com.jonlandrum.macchiato;

import dorkbox.systemTray.Checkbox;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.*;
import dorkbox.util.CacheUtil;

import org.slf4j.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class Macchiato {
    private static final Logger      LOGGER      = LoggerFactory.getLogger(Macchiato.class);
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(Macchiato.class);
    
    private static ServerSocket serverSocket;
    private static SystemTray   systemTray;
    
    public static void main(String[] args) {
        // Debugging
        if(Arrays.asList(args).contains("debug")) {
            SystemTray.DEBUG = true;
            CacheUtil.clear();
        }
        
        // Ensure only one instance is running at a time
        try {
            serverSocket = new ServerSocket(65535);
        } catch(IOException ex) {
            String msg = "Another instance of Macchiato is likely already running";
            LOGGER.error(msg);
            shutdown();
            throw new RuntimeException(msg);
        }
        
        systemTray = SystemTray.get();
        if(null == systemTray) {
            throw new RuntimeException("Unable to load SystemTray");
        }
        
        URL icon = Macchiato.class.getResource("/hot_beverage.png");
        systemTray.setImage(icon);
        systemTray.setStatus("Macchiato");
        
        // Autostart Option
        Checkbox autoStart = new Checkbox("Autostart", event -> {
            if(((Checkbox) event.getSource()).getChecked()) {
                PREFERENCES.put(Constants.AUTO_START, String.valueOf(true));
            } else {
                PREFERENCES.put(Constants.AUTO_START, String.valueOf(false));
            }
        });
        autoStart.setShortcut('a');
        systemTray.getMenu().add(autoStart);
        
        // Key Stroke Option
        Checkbox keyStroke = new Checkbox("Key Stroke (F15)", event -> {
            if(((Checkbox) event.getSource()).getChecked()) {
                ((Checkbox) systemTray.getMenu().get(2)).setChecked(false);
                PREFERENCES.put(Constants.METHOD, Constants.KEY_STROKE);
            } else {
                ((Checkbox) systemTray.getMenu().get(2)).setChecked(true);
                PREFERENCES.put(Constants.METHOD, Constants.MOUSE_MOVE);
            }
        });
        keyStroke.setShortcut('k');
        keyStroke.setChecked(PREFERENCES.get(Constants.METHOD, Constants.KEY_STROKE).equals(Constants.KEY_STROKE));
        systemTray.getMenu().add(keyStroke);
        
        // Mouse Move Option
        Checkbox mouseMove = new Checkbox("Mouse Move", event -> {
            if(((Checkbox) event.getSource()).getChecked()) {
                ((Checkbox) systemTray.getMenu().get(1)).setChecked(false);
                PREFERENCES.put(Constants.METHOD, Constants.MOUSE_MOVE);
            } else {
                ((Checkbox) systemTray.getMenu().get(1)).setChecked(true);
                PREFERENCES.put(Constants.METHOD, Constants.KEY_STROKE);
            }
        });
        mouseMove.setShortcut('m');
        mouseMove.setChecked(PREFERENCES.get(Constants.METHOD, Constants.KEY_STROKE).equals(Constants.MOUSE_MOVE));
        systemTray.getMenu().add(mouseMove);
        
        // Separator
        systemTray.getMenu().add(new Separator());
        
        // Quit Option
        systemTray.getMenu().add(new MenuItem("Quit", e -> {
            shutdown();
        })).setShortcut('q');
        
        // Call the selected method
        call(PREFERENCES.get(Constants.METHOD, Constants.KEY_STROKE));
    }
    
    private static void call(final String method) {
        try {
            TimeUnit.MINUTES.sleep(1);
            execute(method);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
            shutdown();
        }
    }
    
    private static void execute(final String method) {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(10);
            if(method.equals(Constants.KEY_STROKE)) {
                robot.keyPress(KeyEvent.VK_F15);
                robot.keyRelease(KeyEvent.VK_F15);
            } else if(method.equals(Constants.MOUSE_MOVE)) {
                Point location = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(location.x + 1, location.y + 1);
                robot.mouseMove(location.x - 1, location.y - 1);
            }
        } catch(AWTException ex) {
            LOGGER.error(ex.toString());
        }
        LOGGER.info(method);
        call(method);
    }
    
    private static void shutdown() {
        LOGGER.info("Shutting down Macchiato");
        try {
            serverSocket.close();
        } catch(IOException ex) {
            LOGGER.error("Could not close server socket");
        }
        systemTray.shutdown();
    }
    
    private interface Constants {
        String AUTO_START = "auto_start";
        String KEY_STROKE = "key_stroke";
        String MOUSE_MOVE = "mouse_move";
        String METHOD     = "method";
    }
}
