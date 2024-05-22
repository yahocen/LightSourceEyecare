package red.yhc.lightsourceevecare.pojo;

import cn.hutool.core.lang.Console;
import com.sun.jna.platform.win32.*;
import red.yhc.lightsourceevecare.config.Config;
import red.yhc.lightsourceevecare.config.Constant;
import red.yhc.lightsourceevecare.service.MonitorService;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Optional;

/**
 * @author YahocenMiniPC
 */
public class Monitor {

    private final MonitorService service;

    private final String name;

    private final Boolean primary;

    private final WinUser.HMONITOR hmonitor;

    private Float brightnessValue;

    private Brightness brightness;

    private final PropertyChangeSupport brightnessChangeSupport = new PropertyChangeSupport(this);

    public Monitor(MonitorService service, String name, Boolean primary, WinUser.HMONITOR hmonitor) {
        this.service = service;
        this.name = name;
        this.primary = primary;
        this.hmonitor = hmonitor;
        //获取当前亮度
        currentBrightness().ifPresentOrElse(b -> this.brightnessValue = b, () -> {
            this.brightnessValue = Config.getFloat("brightness", this.getName(), 50F);
        });
        //初始化计算模型
        this.brightness = new Brightness(this);
    }

    public String getName() {
        return name;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public WinUser.HMONITOR getHmonitor() {
        return hmonitor;
    }

    public Float getBrightnessValue() {
        return brightnessValue;
    }

    public Brightness getBrightness() {
        return brightness;
    }

    public void addBrightnessChangeListener(PropertyChangeListener listener) {
        brightnessChangeSupport.addPropertyChangeListener(listener);
    }

    public Optional<Float> changeBrightness(Float brightness) {
        if (brightness > Constant.MAX_BRIGHTNESS || brightness < Constant.MIN_BRIGHTNESS) {
            Console.error("亮度应介于 0 和 100 之间");
            return Optional.empty();
        }
        if (this.getBrightnessValue().equals(brightness)) {
            Console.log("{} 亮度已达到 {}", this.getName(), brightness);
            return Optional.of(this.getBrightnessValue());
        }
        PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR[] physMons = new PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR[1];
        if (Dxva2.INSTANCE.GetPhysicalMonitorsFromHMONITOR(this.getHmonitor(), 1, physMons).booleanValue()) {
            WinNT.HANDLE monitor = physMons[0].hPhysicalMonitor;
            Dxva2.INSTANCE.SetMonitorBrightness(monitor, brightness.intValue());
            Dxva2.INSTANCE.DestroyPhysicalMonitor(monitor);
            //同步当前亮度
            brightnessChangeSupport.firePropertyChange("brightness", this.brightnessValue, brightness);
            //保存当前亮度
            Config.save("brightness", this.getName(), brightness);
            this.brightnessValue = brightness;
        } else {
            Console.error("屏幕 {} 无法将亮度设置为 {}，重新获取屏幕信息", this.getHmonitor(), brightness);
            this.service.initMonitors();
            return Optional.empty();
        }
        return Optional.of(brightness);
    }

    public Optional<Float> currentBrightness() {
        Float brightness = null;
        PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR[] physMons = new PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR[1];
        if (Dxva2.INSTANCE.GetPhysicalMonitorsFromHMONITOR(this.getHmonitor(), 1, physMons).booleanValue()) {
            WinNT.HANDLE monitorHandle = physMons[0].hPhysicalMonitor;
            WinDef.DWORDByReference pdwMinimumBrightness = new WinDef.DWORDByReference();
            WinDef.DWORDByReference pdwCurrentBrightness = new WinDef.DWORDByReference();
            WinDef.DWORDByReference pdwMaximumBrightness = new WinDef.DWORDByReference();
            if (Dxva2.INSTANCE.GetMonitorBrightness(monitorHandle, pdwMinimumBrightness, pdwCurrentBrightness, pdwMaximumBrightness).booleanValue()) {
                Dxva2.INSTANCE.DestroyPhysicalMonitor(monitorHandle);
                brightness = pdwCurrentBrightness.getValue().floatValue();
            } else {
                Console.error("屏幕 {} 无法获得亮度，重新获取屏幕信息：{}", this.getName(), Kernel32.INSTANCE.GetLastError());
                return Optional.empty();
            }
        }
        return Optional.ofNullable(brightness);
    }

}
