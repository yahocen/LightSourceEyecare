package red.yhc.lightsourceevecare.ui;

import cn.hutool.core.util.NumberUtil;
import red.yhc.lightsourceevecare.service.LightUdpService;

import javax.swing.*;
import java.awt.*;

/**
 * @author YahocenMiniPC
 */
public class SensorBrightnessPanel extends JPanel {

    public SensorBrightnessPanel(LightUdpService udpService) {
        JLabel num = new JLabel(NumberUtil.toStr(LightUdpService.LIGHT_VALUE));

        udpService.addLightValueChangeListener(evt -> {
            num.setText(NumberUtil.toStr((Float)evt.getNewValue()));
        });

        this.add(num);
    }

}
