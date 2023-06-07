import javax.swing.*;

public class GUI_param {
    GUI_param() {
    }

    public void show(int sections, int floors, int capacity) {
        JFrame frame = new JFrame("Параметры");
        frame.setSize(250, 150);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label1 = new JLabel("Подъезды - " + sections);
        label1.setBounds(30, 10, 180, 25);
        frame.add(label1);
        JLabel label2 = new JLabel("Этажи - " + floors);
        label2.setBounds(30, 40, 180, 25);
        frame.add(label2);
        JLabel label3 = new JLabel("Грузоподъемность - " + capacity);
        label3.setBounds(30, 70, 180, 25);
        frame.add(label3);

        frame.setVisible(true);
    }
}
