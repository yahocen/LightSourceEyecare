package red.yhc.lightsourceevecare.ui;

import red.yhc.lightsourceevecare.LightSourceEyecare;

import javax.swing.*;

/**
 * @author YahocenMiniPC
 */
public class MenuBar extends JMenuBar {

    public MenuBar() {
        JMenuItem help = new JMenuItem("快捷键");
        help.addActionListener(e -> new HelpDialog());

        JMenuItem author = new JMenuItem("软件信息");
        author.addActionListener(e -> new AuthorDialog());

        JMenu menu = new JMenu("菜单");
        menu.add(help);
        menu.add(author);
        this.add(menu);
    }

}
