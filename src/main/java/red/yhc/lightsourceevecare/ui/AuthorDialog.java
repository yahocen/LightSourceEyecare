package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.LightSourceEyecare;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author YahocenMiniPC
 */
public class AuthorDialog extends JDialog  {

    public AuthorDialog() {
        super(LightSourceEyecare.APP, "软件信息", true);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        panel.setBorder(padding);

        panel.add(new JLabel("光源护眼 1.0-develop"));
        panel.add(new JLabel("作者：Yahocen"));
        panel.add(new JLabel("联系：yahocen@hotmail.com"));
        panel.add(new JLabel("感谢：https://github.com/wasteofserver/monitor-brightness-control"));

        this.add(panel);
        this.setSize(200, 100);
        this.pack();
        this.setLocationRelativeTo(LightSourceEyecare.APP);
        this.setVisible(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

}
