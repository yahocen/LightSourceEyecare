package red.yhc.lightsourceevecare.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YahocenMiniPC
 */
public class BrightnessBak {

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
    private Map<String, Double> calibrationFactors;

    public BrightnessBak() {
        // 初始模型参数可以根据实际情况进行调整
        logBase = 1.0;
        logOffset = 0.0;
        calibrationFactors = new HashMap<>();
    }

    /**
     * 根据传感器数值计算显示器亮度值的方法
     * @param monitorId
     * @param lightValue
     * @return
     */
    public int calculateBrightness(String monitorId, int lightValue) {
        // 根据对数模型计算亮度值
        double brightness = logBase * Math.log(lightValue + logOffset);

        // 应用校准因子
        Double calibrationFactor = calibrationFactors.get(monitorId);
        if (calibrationFactor != null) {
            brightness *= calibrationFactor;
        }

        // 确保亮度值在0到100之间
        brightness = Math.max(0, brightness);
        brightness = Math.min(100, brightness);

        return (int) Math.round(brightness);
    }

    /**
     * 用户校准方法
     * @param monitorId
     * @param lightValue
     * @param userPreferredBrightness
     */
    public void calibrate(String monitorId, int lightValue, int userPreferredBrightness) {
        // 假设用户已经找到了他们认为舒适的亮度值
        double calculatedBrightness = logBase * Math.log(lightValue + logOffset);
        calibrationFactors.put(monitorId, (double) userPreferredBrightness / calculatedBrightness);
    }

    public void learnFromUser(String monitorId, int lightValue, int userPreferredBrightness) {
        // 计算当前亮度
        double currentBrightness = logBase * Math.log(lightValue + logOffset);

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

        // 更新校准因子
        calibrate(monitorId, lightValue, userPreferredBrightness);
    }

    public static void main(String[] args) {
        BrightnessBak brightness = new BrightnessBak();
        brightness.learnFromUser("A1", 500, 33);
        brightness.learnFromUser("A2", 500, 40);

        brightness.learnFromUser("A1", 543, 35);
        brightness.learnFromUser("A2", 500, 39);

        System.out.println(brightness.calculateBrightness("A1", 300));
        System.out.println(brightness.calculateBrightness("A2", 300));
    }

}