package red.yhc.lightsourceevecare.ui;

import cn.hutool.core.util.NumberUtil;
import red.yhc.lightsourceevecare.pojo.Monitor;

import javax.swing.*;
import java.awt.*;

/**
 * @author YahocenMiniPC
 */
public class BrightnessLabel extends JLabel {

    public BrightnessLabel(Monitor monitor) {
        super(NumberUtil.toStr(monitor.getBrightnessValue()));
        this.setFont(new Font("Arial", Font.PLAIN, 20));
        this.setHorizontalAlignment(JLabel.CENTER);
    }

}
