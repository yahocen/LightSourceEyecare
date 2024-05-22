package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.LightSourceEyecare;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * @author YahocenMiniPC
 */
public class HelpDialog extends JDialog  {

    public HelpDialog() {
        super(LightSourceEyecare.APP, "快捷键", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(padding);

        panel.add(new JLabel("Ctrl + Alt + 5 （全局减少亮度）"));
        panel.add(new JLabel("Ctrl + Alt + 6 （全局增加亮度）"));

        this.add(panel);
        this.setSize(200, 100);
        this.pack();
        this.setLocationRelativeTo(LightSourceEyecare.APP);
        this.setVisible(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

}
