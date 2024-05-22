package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.pojo.Monitor;

import javax.swing.*;
import java.awt.*;

/**
 * @author YahocenMiniPC
 */
public class MonitorControlPanel extends JPanel {

    public MonitorControlPanel(Monitor monitor) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //显示器名称
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.ipady = 10;
        this.add(new MonitorNameLabel(monitor.getName()), constraints);

        //亮度调节滑块
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(new MonitorBrightnessPanel(monitor), constraints);
    }

}
