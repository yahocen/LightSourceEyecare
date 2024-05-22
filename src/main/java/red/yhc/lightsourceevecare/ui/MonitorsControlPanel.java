package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.pojo.Monitor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Collection;

/**
 * @author YahocenMiniPC
 */
public class MonitorsControlPanel extends JPanel {

    public MonitorsControlPanel(Collection<Monitor> monitors) {
        //设置布局
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //设置边距
        Border padding = BorderFactory.createEmptyBorder(0, 8, 10, 5);
        this.setBorder(padding);
        // 添加按钮到框架
        monitors.forEach(monitor -> {
            this.add(new MonitorControlPanel(monitor));
        });
    }

}
