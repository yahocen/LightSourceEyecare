package red.yhc.lightsourceevecare;

import cn.hutool.core.util.StrUtil;
import red.yhc.lightsourceevecare.service.*;
import red.yhc.lightsourceevecare.ui.MenuBar;
import red.yhc.lightsourceevecare.ui.MonitorsControlPanel;
import red.yhc.lightsourceevecare.ui.SensorBrightnessPanel;
import red.yhc.lightsourceevecare.ui.SystemTrayIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author YahocenMiniPC
 */
public class LightSourceEyecare extends JFrame {

    public static LightSourceEyecare APP;

    public static void main(String[] args) {
        if(isNativeImage()) {
            System.setProperty("java.home", ".");
        }
        SwingUtilities.invokeLater(() -> APP = new LightSourceEyecare());
    }

    private LightSourceEyecare() {
        //控制屏幕业务类
        MonitorService monitorService = new MonitorService();
        //快捷键监听
        ShortcutKeyService.monitoring(monitorService);
        //UDP远程监听光线亮度
        LightUdpService.monitoring(monitorService).addLightValueChangeListener(evt -> {
            Float lightValue = (Float) evt.getNewValue();
            this.setTitle(StrUtil.format("光源护眼（感光值 {}）", lightValue));
            //调整屏幕亮度
            monitorService.getMonitors().values().parallelStream().forEach(monitor -> {
                monitor.changeBrightness(monitor.getBrightness().calculateBrightness(lightValue));
            });
        });
        //显示任务栏图标
        SystemTrayIcon.show(this);
        //显示控制面板
        this.add(new MonitorsControlPanel(monitorService.getMonitors().values()));
        //菜单栏
        this.setJMenuBar(new MenuBar());
        //设置布局
        this.setTitle("光源护眼（传感离线）");
        this.setSize(550, (85 * monitorService.getMonitors().size()) + 50);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置图标
        ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon128.png")));
        this.setIconImage(imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        // 计算屏幕中间位置
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - this.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - this.getHeight()) / 2);
        // 设置JFrame位置为屏幕中间
        this.setLocation(x, y);
        //显示UI
        this.setVisible(true);
    }

    private static boolean isNativeImage() {
        String classPath = System.getProperty("java.class.path");
        return classPath == null || classPath.isEmpty();
    }

}
