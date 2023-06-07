import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI {

    private JFrame frame = new JFrame("Симулятор системы управления лифтом");
    private JPanel allSectionPanels = new JPanel();
    private JPanel allSectionPanelUpdatables = new JPanel();
    private ArrayList<JPanel> listofSectionPanel = new ArrayList<>();
    private ArrayList<JPanel> listofSectionPanelUpdatable = new ArrayList<>();
    private String[] callMessage = new String[2];
    private boolean callFlag = false;
    private Integer[] lockData = new Integer[3]; // 0 - section, 1 - floor, 2 - direction
    private boolean lockFlag = false;
    private Integer[] GOData = new Integer[1]; // 0 - section
    private boolean GOFlag = false;
    public boolean restartFlag = false;

    public GUI() {
    }

    public void init(String[] params) {

        // Set the section panels settings

        allSectionPanels.setLayout(null);
        allSectionPanels.setBounds(0, 20, 1500, 750);
        allSectionPanels.setOpaque(false);

        allSectionPanelUpdatables.setLayout(null);
        allSectionPanelUpdatables.setBounds(0, 20, 1500, 750);

        // Unpack the params

        int sections = Integer.parseInt(params[0]);
        int floors = Integer.parseInt(params[1]);
        int capacity = Integer.parseInt(params[2]);

        // Set the main frame settings

        frame.setSize(1550, 800);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title

        JLabel title = new JLabel("Симулятор системы управления лифтом");
        title.setBounds(10, 8, 300, 25);
        title.setFont(new Font("Serif", Font.BOLD, 16));
        frame.add(title);

        // Main window control buttons

        JButton buttonParams = new JButton("Параметры");
        buttonParams.setBounds(10, 40, 120, 25);
        buttonParams.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUI_param gui_param = new GUI_param();
                gui_param.show(sections, floors, capacity);
            }
        });
        frame.add(buttonParams);

        JButton buttonRestart = new JButton("Перезапуск");
        buttonRestart.setBounds(140, 40, 120, 25);
        buttonRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                restartFlag = true;
            }
        });
        frame.add(buttonRestart);

        JButton buttonExit = new JButton("Выход");
        buttonExit.setBounds(270, 40, 120, 25);
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.add(buttonExit);

        // Non-updatable section panels elements

        for (int i = 0; i < sections; i++) {
            JPanel sectionPanel = new JPanel();
            sectionPanel.setLocation( (i * 255) + 5, 50);
            sectionPanel.setLayout(null);
            sectionPanel.setSize(250, 750);
            sectionPanel.setOpaque(false);

            JPanel sectionPanelUpdatable = new JPanel();
            sectionPanelUpdatable.setLocation( (i * 255) + 5, 50);
            sectionPanelUpdatable.setLayout(null);
            sectionPanelUpdatable.setSize(250, 750);

            JLabel sectionTitle = new JLabel();
            sectionTitle.setText("Подъезд " + (i + 1));
            sectionTitle.setLocation(90, 5);
            sectionTitle.setSize(70, 25);
            sectionPanel.add(sectionTitle);

            JLabel callNote = new JLabel("Вызов");
            callNote.setBounds(73, 75, 50, 25);
            sectionPanel.add(callNote);

            listofSectionPanel.add(sectionPanel);
            allSectionPanels.add(sectionPanel);

            listofSectionPanelUpdatable.add(sectionPanelUpdatable);
            allSectionPanelUpdatables.add(sectionPanelUpdatable);
        }

        frame.add(allSectionPanels);
        frame.add(allSectionPanelUpdatables);
        frame.setVisible(true);
    }

    public void render(Controller controller) {

        // Parse the parameters from the controller

        int sections = controller.getNumSections();
        int floors = controller.getTopFloor();
        int capacity = controller.getMaxCapacity();

        // Render - Clear

        for (JPanel sectionPanelUpdatable : listofSectionPanelUpdatable) {
            sectionPanelUpdatable.removeAll();
        }

        // Getting data from controller

        int counter = 0;
        for (JPanel sectionPanelUpdatable : listofSectionPanelUpdatable) {
            int direction = controller.getDirectionOf(counter);
            int floor = controller.getFloorOf(counter);
            ArrayList<Integer[]> floorsData = controller.getFloors(counter);

            // Check if elevator of this section is locked

            if (controller.getLoadingLock(counter) == 1) {
                lockFlag = true;
                lockData[0] = counter;
                lockData[1] = floor;
                lockData[2] = direction;
            }

            // Current capacity label

            JLabel currentCapacity = new JLabel();
            currentCapacity.setText("В лифте " + controller.getPassengersOf(counter) + "/" + capacity);
            currentCapacity.setBounds(140, 35, 150, 25);
            sectionPanelUpdatable.add(currentCapacity);

            // GO button

            JButton goButton = new JButton("Движение");
            goButton.setBounds(20, 35, 100, 25);
            goButton.setName(counter+"");
            goButton.setEnabled(controller.getGOStatusOf(counter, 0));
            goButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    GOData[0] = Integer.parseInt(button.getName());
                    GOFlag = true;
                }
            });
            sectionPanelUpdatable.add(goButton);

            for (int j = floors; j >= 0; j--) {
                Integer[] floorData = floorsData.get(j);

                // Floor graphics

                JLabel floorGraphic = new JLabel();
                floorGraphic.setBorder(BorderFactory.createLineBorder(Color.black));
                if (controller.getFloorOf(counter) == j) {
                    if (!(controller.getStatusOf(counter))) {
                        floorGraphic.setIcon(controller.getDirectionOf(counter) == 0 ? new ImageIcon("stop_up.png") : new ImageIcon("stop_down.png"));
                    }
                    else {
                        floorGraphic.setIcon(controller.getDirectionOf(counter) == 0 ? new ImageIcon("move_up.png") : new ImageIcon("move_down.png"));
                    }
                }
                floorGraphic.setBounds(20, (floors - j) * 40 + 100, 40, 40);
                sectionPanelUpdatable.add(floorGraphic);

                // Floor people labels

                if (floorData[0] > 0 || floorData[1] > 0) {
                    JLabel floorPeople = new JLabel();
                    String people = "↑ " + floorData[0] + "   ↓ " + floorData[1];
                    floorPeople.setText(people);
                    floorPeople.setBounds(130, (floors - j) * 40 + 100, 100, 40);
                    sectionPanelUpdatable.add(floorPeople);
                }

                // Call buttons

                JButton callButton = new JButton((j+1)+"");
                callButton.setBounds(70, (floors - j) * 40 + 105, 50, 30);
                callButton.setName(counter+"");
                callButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        callFlag = true;
                        JButton button = (JButton) e.getSource();
                        callMessage[0] = button.getName();
                        callMessage[1] = button.getActionCommand();
                    }
                });
                sectionPanelUpdatable.add(callButton);
            }
            counter++;
        }

        // Render - Draw

        frame.add(allSectionPanelUpdatables);
        frame.revalidate();
        frame.repaint();

        // Passing data to controller

        if (callFlag) {
            GUI_call gui_call = new GUI_call();
            String[] call_args = gui_call.show(Integer.parseInt(callMessage[0]), Integer.parseInt(callMessage[1]), floors);

            while (call_args[0] == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            controller.makeCall(Integer.parseInt(callMessage[0]), Integer.parseInt(callMessage[1]), Integer.parseInt(call_args[0]), Integer.parseInt(call_args[1]));
            callFlag = false;
        }

        if (lockFlag) {
            GUI_target gui_target = new GUI_target();
            String[] targetFloor = gui_target.show(lockData[0], lockData[1], lockData[2], floors);

            while (targetFloor[0] == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            controller.setFloorOfPassenger(lockData[0], Integer.parseInt(targetFloor[0]));
            lockFlag = false;
        }

        if (GOFlag) {
            controller.setGOStatusOf(GOData[0], 1, 1);
            GOFlag = false;
        }
    }

}
