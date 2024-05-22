package red.yhc.lightsourceevecare.service;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import red.yhc.lightsourceevecare.config.Constant;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author YahocenMiniPC
 */
public class LightUdpService implements Runnable {

    public static boolean ONLINE = false;

    public static Float LIGHT_VALUE = 0F;

    private final PropertyChangeSupport lightValueChangeSupport = new PropertyChangeSupport(this);

    private LightUdpService(MonitorService monitorService) {
    }

    public static LightUdpService monitoring(MonitorService monitorService) {
        LightUdpService lightUdpService = new LightUdpService(monitorService);
        ThreadUtil.execute(lightUdpService);
        return lightUdpService;
    }

    public void addLightValueChangeListener(PropertyChangeListener listener) {
        lightValueChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(23245)){
            byte[] buffer = new byte[256];
            Console.log("UDP 服务器正在侦听端口 23245");

            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                ONLINE = true;
                //回调当前亮度值
                float lightValue = Float.parseFloat(new String(packet.getData(), 0, packet.getLength()));
                //同步当前亮度
                lightValueChangeSupport.firePropertyChange("lightValue", LIGHT_VALUE, lightValue);
                LIGHT_VALUE = lightValue;
            }
        } catch (IOException e) {
            Console.error("IOException: " + e.getMessage());
        }
    }

}
