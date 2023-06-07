import javax.swing.*;
import java.awt.event.ActionListener;

public class GUI_target {
    GUI_target() {
    }

    public String[] show(int section, int floor, int direction, int topFloor) {
        section++;
        floor++;
        topFloor++;

        JFrame frame = new JFrame("Ввод назначения");
        frame.setSize(300, 165);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel titleLabel = new JLabel("Выбор этажа: подъезд " + section + ", этаж " + floor);
        JLabel titleLabel2 = new JLabel("Введите назначение в направлении " + (direction == 0 ? "вверх" : "вниз"));
        titleLabel.setBounds(10, 10, 280, 25);
        titleLabel2.setBounds(10, 30, 280, 25);
        frame.add(titleLabel);
        frame.add(titleLabel2);
        JLabel label1 = new JLabel("Этаж");
        label1.setBounds(10, 60, 40, 25);
        frame.add(label1);

        int spinner_value, spinner_min, spinner_max;
        if (direction == 0) {
            spinner_value = floor + 1;
            spinner_min = floor + 1;
            spinner_max = topFloor;
        } else {
            spinner_value = floor - 1;
            spinner_min = 1;
            spinner_max = floor - 1;
        }

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(spinner_value, spinner_min, spinner_max, 1));
        spinner.setBounds(50, 60, 40, 25);
        frame.add(spinner);

        JButton button = new JButton("Задать");
        button.setBounds(100, 95, 80, 25);
        String[] return_arg = new String[1];
        ActionListener confirmed = e -> {
            return_arg[0] = spinner.getValue().toString();
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
        return return_arg;
    }
}
