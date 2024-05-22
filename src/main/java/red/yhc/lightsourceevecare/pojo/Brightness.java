package red.yhc.lightsourceevecare.pojo;

import red.yhc.lightsourceevecare.config.Config;

/**
 * @author YahocenMiniPC
 */
public class Brightness {

    /**
     * 对数模型的缩放系数
     */
    private double logBase;

    /**
     * 对数模型的偏移系数
     */
    private double logOffset;

    /**
     * 键为显示器ID，值为校准因子
     */
    private Double calibrationFactor;

    private final Monitor monitor;

    public Brightness(Monitor monitor) {
        this.monitor = monitor;
        //缩放系数
        this.logBase = Config.getDouble("logBase", monitor.getName(), 1.0);
        //偏移系数
        this.logOffset = Config.getDouble("logOffset", monitor.getName(), 0.1);
        //校准因子
        this.calibrationFactor = Config.getDouble("calibrationFactor", monitor.getName(), 1.0);
    }

    /**
     * 根据传感器数值计算显示器亮度值的方法
     * @param lightValue
     * @return
     */
    public float calculateBrightness(float lightValue) {
        // 根据对数模型计算亮度值
        double brightness = logBase * Math.log(lightValue + logOffset);

        // 应用校准因子
        if (calibrationFactor != null) {
            brightness *= calibrationFactor;
        }

        // 确保亮度值在0到100之间
        brightness = Math.max(0, brightness);
        brightness = Math.min(100, brightness);

        return (float) Math.round(brightness);
    }

    /**
     * 用户校准方法
     * @param lightValue
     * @param userPreferredBrightness
     */
    public void calibrate(float lightValue, float userPreferredBrightness) {
        // 假设用户已经找到了他们认为舒适的亮度值
        double calculatedBrightness = this.logBase * Math.log(lightValue + this.logOffset);
        this.calibrationFactor = (double) userPreferredBrightness / calculatedBrightness;

        //持久化
        Config.save("calibrationFactor", this.monitor.getName(), this.calibrationFactor);
    }

    public void learnFromUser(float lightValue, float userPreferredBrightness) {
        // 计算当前亮度
        double currentBrightness = this.logBase * Math.log(lightValue + this.logOffset);

        // 计算梯度，即用户偏好亮度与当前亮度之间的差值
        double gradient = userPreferredBrightness - currentBrightness;

        // 初始化梯度历史和梯度历史平方
        double m = 0.0;
        double v = 0.0;

        // 计算梯度历史和梯度历史平方的移动平均
        double beta1 = 0.9;
        double beta2 = 0.999;

        m = beta1 * m + (1 - beta1) * gradient;
        v = beta2 * v + (1 - beta2) * gradient * gradient;

        // 计算梯度历史和梯度历史平方的偏差校正的移动平均
        m = m / (1 - Math.pow(beta1, 1));
        v = v / (1 - Math.pow(beta2, 1));

        // 计算梯度平方的平方根
        double sqrtV = Math.sqrt(v);

        // 计算梯度的偏差校正的移动平均
        double biasCorrectedM = m / sqrtV;

        // 计算学习率，初始学习率可以根据需要调整
        double learningRate = 0.001;

        // 更新模型参数
        logBase += learningRate * biasCorrectedM;
        logOffset += learningRate * biasCorrectedM * Math.log(lightValue + logOffset) / (logBase * (Math.log(lightValue + logOffset)));

        //持久化
        Config.save("logBase", this.monitor.getName(), logBase);
        Config.save("logOffset", this.monitor.getName(), logOffset);

        // 更新校准因子
        calibrate(lightValue, userPreferredBrightness);
    }

}