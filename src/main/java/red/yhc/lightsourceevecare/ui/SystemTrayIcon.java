package red.yhc.lightsourceevecare.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author YahocenMiniPC
 */
public class SystemTrayIcon {

    private final JFrame application;

    public static void show(JFrame application) {
        new SystemTrayIcon(application);
    }

    private SystemTrayIcon(JFrame application) {
        this.application = application;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon64.png"));

            ActionListener exitListener = e -> System.exit(0);

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Close");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            TrayIcon trayIcon = setupTrayBehaviour(image, popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            application.setExtendedState(JFrame.ICONIFIED);
        }
    }

    private TrayIcon setupTrayBehaviour(Image image, PopupMenu popup) {
        TrayIcon trayIcon = new TrayIcon(image, "光源护眼", popup);
        trayIcon.setImageAutoSize(true);

        // Add a mouse listener to handle double-click open events
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                application.setVisible(true);
            }
            }
        });
        return trayIcon;
    }

}
