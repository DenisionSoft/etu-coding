import javax.swing.*;
import java.awt.event.ActionListener;

public class GUI_call {
    GUI_call() {
    }

    public String[] show(int section, int floor, int topFloor) {
        section++;
        topFloor++;

        JFrame frame = new JFrame("Вызов лифта на этаж");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel titleLabel = new JLabel("Вызов лифта: подъезд " + section + ", этаж " + floor);
        JLabel titleLabel2 = new JLabel("Введите сколько людей вызывают лифт");
        titleLabel.setBounds(10, 10, 300, 25);
        titleLabel2.setBounds(10, 30, 300, 25);
        frame.add(titleLabel);
        frame.add(titleLabel2);

        JLabel label1 = new JLabel("вверх");
        label1.setBounds(10, 65, 40, 25);
        frame.add(label1);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        spinner.setBounds(60, 65, 120, 25);
        if (floor == topFloor) {
            spinner.setEnabled(false);
        }
        frame.add(spinner);

        JLabel label2 = new JLabel("вниз");
        label2.setBounds(10, 95, 40, 25);
        frame.add(label2);
        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        spinner2.setBounds(60, 95, 120, 25);
        if (floor == 1) {
            spinner2.setEnabled(false);
        }
        frame.add(spinner2);

        JButton button = new JButton("Задать");
        button.setBounds(100, 130, 80, 25);
        String[] call_args = new String[3];
        ActionListener confirmed = e -> {
            call_args[0] = spinner.getValue().toString();
            call_args[1] = spinner2.getValue().toString();
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
        return call_args;
    }
}
