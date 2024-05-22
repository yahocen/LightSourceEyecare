package red.yhc.lightsourceevecare.service;

import cn.hutool.core.lang.Console;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.NativeInputEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import red.yhc.lightsourceevecare.config.Constant;

/**
 * @author YahocenMiniPC
 */
public class ShortcutKeyService implements NativeKeyListener {

    private final MonitorService monitorService;

    private ShortcutKeyService(MonitorService monitorService) {
        this.monitorService = monitorService;
        unregisterGlobalShortCutsShutdownHook();
        registerGlobalShortCuts();
    }


    public static void monitoring(MonitorService monitorService) {
        new ShortcutKeyService(monitorService);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // Check if Ctrl+Alt+5 is pressed
        if (e.getID() == NativeKeyEvent.NATIVE_KEY_PRESSED && (e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && (e.getModifiers() & NativeInputEvent.ALT_MASK) != 0 && e.getKeyCode() == NativeKeyEvent.VC_5) {
            monitorService.getMonitors().forEach((name, monitor) -> {
                float changeBrightnessTo = calculateNewBrightnessValue(monitor.getBrightnessValue(), -5);
                monitor.changeBrightness(changeBrightnessTo);
            });
        }
        // Check if Ctrl+Alt+6 is pressed
        if (e.getID() == NativeKeyEvent.NATIVE_KEY_PRESSED && (e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && (e.getModifiers() & NativeInputEvent.ALT_MASK) != 0 && e.getKeyCode() == NativeKeyEvent.VC_6) {
            monitorService.getMonitors().forEach((name, monitor) -> {
                float changeBrightnessTo = calculateNewBrightnessValue(monitor.getBrightnessValue(), 5);
                monitor.changeBrightness(changeBrightnessTo);
            });
        }
    }

    public void unregisterGlobalShortCutsShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                GlobalScreen.removeNativeKeyListener(this);
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                Console.error("注销本机钩子时出现问题：{}", ex.getMessage());
                System.exit(1);
            }
        }));
    }

    public void registerGlobalShortCuts() {
        try {
            GlobalScreen.setEventDispatcher(new VoidDispatchService());
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            Console.error("注册本机钩子时出现问题：{}", ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    private float calculateNewBrightnessValue(float currentBrightness, float change) {
        float newBrightness = currentBrightness + change;
        if (newBrightness > Constant.MAX_BRIGHTNESS) {
            newBrightness = Constant.MAX_BRIGHTNESS;
        } else if (newBrightness < Constant.MIN_BRIGHTNESS) {
            newBrightness = Constant.MIN_BRIGHTNESS;
        }
        Console.log("新亮度: {} (旧亮度: {} 新增亮度: {})", newBrightness, currentBrightness, change);
        return newBrightness;
    }

}
