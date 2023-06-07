import javax.swing.*;
import java.awt.event.ActionListener;

public class GUI_intro {
    GUI_intro() {
    }

    public String[] show() {
        JFrame frame = new JFrame("Начальные данные");
        frame.setSize(300, 170);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label1 = new JLabel("Подъезды");
        label1.setBounds(30, 10, 120, 25);
        frame.add(label1);
        JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(2, 1, 6, 1));
        spinner1.setBounds(150, 10, 80, 25);
        frame.add(spinner1);
        JLabel label2 = new JLabel("Этажи");
        label2.setBounds(30, 40, 120, 25);
        frame.add(label2);
        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(9, 2, 14, 1));
        spinner2.setBounds(150, 40, 80, 25);
        frame.add(spinner2);
        JLabel label3 = new JLabel("Грузоподъемность");
        label3.setBounds(30, 70, 120, 25);
        frame.add(label3);
        JSpinner spinner3 = new JSpinner(new SpinnerNumberModel(4, 1, null, 1));
        spinner3.setBounds(150, 70, 80, 25);
        frame.add(spinner3);

        JButton button = new JButton("Задать");
        button.setBounds(100, 100, 80, 25);
        String[] cont_args = new String[3];
        ActionListener confirmed = e -> {
            cont_args[0] = spinner1.getValue().toString();
            cont_args[1] = spinner2.getValue().toString();
            cont_args[2] = spinner3.getValue().toString();
            frame.dispose();
        };
        button.addActionListener(confirmed);
        frame.add(button);

        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.setVisible(true);
        return cont_args;
    }
}
