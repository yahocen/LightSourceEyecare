package red.yhc.lightsourceevecare.config;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.setting.Setting;

/**
 * @author YahocenMiniPC
 */
public class Config {

    private static Setting SETTING = new Setting();

    static {
        try{
            SETTING = new Setting("config.setting", true);
            //SETTING.autoLoad(true);
        } catch (Exception e){
            Console.error(e, "读取配置文件错误！");
        }
    }

    public synchronized static void save(String key, String group, Object value) {
        Console.log("保存 {} {} {}", key, group, value);
        SETTING.setByGroup(key, group, ObjUtil.toString(value));
        SETTING.store("config.setting");
    }

    public static String getStr(String key, String group) {
        return SETTING.getByGroup(key, group);
    }

    public static Double getDouble(String key, String group, Double defVal) {
        return NumberUtil.parseDouble(SETTING.getByGroup(key, group), defVal);
    }

    public static Float getFloat(String key, String group, Float defVal) {
        return NumberUtil.parseFloat(SETTING.getByGroup(key, group), defVal);
    }

}
