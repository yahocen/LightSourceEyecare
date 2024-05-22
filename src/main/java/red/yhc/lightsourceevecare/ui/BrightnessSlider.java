package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.LightSourceEyecare;
import red.yhc.lightsourceevecare.pojo.Monitor;
import red.yhc.lightsourceevecare.service.LightUdpService;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author YahocenMiniPC
 */
public class BrightnessSlider extends JSlider {

    private final Monitor monitor;

    public BrightnessSlider(Monitor monitor) {
        super(JSlider.HORIZONTAL, 0, 100, monitor.getBrightnessValue().intValue());
        this.monitor = monitor;
        this.setMajorTickSpacing(5);
        this.setMinorTickSpacing(1);
        this.setPaintTicks(false);
        this.setPaintLabels(false);
        this.setPaintTrack(true);
        this.setSnapToTicks(true);
        //监听滑块数值变化
        this.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            this.monitor.changeBrightness((float) source.getValue());
        });
        //监控鼠标事件用于更新亮度算法数值
        this.addMouseListener(new MouseEvent());
    }

    class MouseEvent implements MouseListener {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {

        }
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {

        }
        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            //如果传感器在线每次手动操作滑块就等于训练亮度计算模型
            if(LightUdpService.ONLINE) {
                monitor.getBrightness().learnFromUser(LightUdpService.LIGHT_VALUE, monitor.getBrightnessValue());
            }
        }
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {

        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {

        }
    }

}
