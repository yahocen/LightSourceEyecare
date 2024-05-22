package red.yhc.lightsourceevecare.service;

import com.sun.jna.platform.win32.*;
import red.yhc.lightsourceevecare.pojo.Monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YahocenMiniPC
 */
public class MonitorService {

    public Map<String, Monitor> getMonitors() {
        return monitors;
    }

    private final Map<String, Monitor> monitors = new ConcurrentHashMap<>();

    public MonitorService() {
        initMonitors();
    }

    /**
     * 获取当前所有屏幕
     */
    public void initMonitors() {
        monitors.clear();
        //所有所有屏幕参数
        User32.INSTANCE.EnumDisplayMonitors(null, null, (hmonitor, hdc, rect, lparam) -> {
            //获取屏幕参数
            WinUser.MONITORINFOEX monitorInfo = new WinUser.MONITORINFOEX();
            User32.INSTANCE.GetMonitorInfo(hmonitor, monitorInfo);
            //显示器名称
            String monitorName = new String(monitorInfo.szDevice).trim();
            //记录内存
            monitors.put(monitorName, new Monitor(this, monitorName, monitorInfo.dwFlags == 1, hmonitor));
            return 1;
        }, null);
    }

}
