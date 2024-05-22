package red.yhc.lightsourceevecare.ui;

import cn.hutool.core.util.NumberUtil;
import red.yhc.lightsourceevecare.pojo.Monitor;

import javax.swing.*;
import java.awt.*;

/**
 * @author YahocenMiniPC
 */
public class MonitorBrightnessPanel extends JPanel {

    public MonitorBrightnessPanel(Monitor monitor) {
        //布局信息
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // 创建滑块
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.98;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        BrightnessSlider brightnessSlider = new BrightnessSlider(monitor);
        this.add(brightnessSlider, constraints);

        // 当前亮度数值
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.02;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        BrightnessLabel brightnessLabel = new BrightnessLabel(monitor);
        this.add(brightnessLabel, constraints);

        //监控显示器亮度数值变化
        monitor.addBrightnessChangeListener(evt -> {
            int brightness = ((Float) evt.getNewValue()).intValue();
            brightnessSlider.setValue(brightness);
            brightnessLabel.setText(NumberUtil.toStr(brightness));
        });
    }

}
