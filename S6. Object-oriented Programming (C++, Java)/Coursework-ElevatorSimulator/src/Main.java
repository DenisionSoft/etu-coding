public class Main {
    public static void main(String[] args) {

        // Intro screen

        GUI_intro gui_intro = new GUI_intro();
        String[] controller_params = gui_intro.show();

        while (controller_params[0] == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Initialize controller and GUI

        Controller controller = new Controller(controller_params);
        GUI gui = new GUI();

        controller.init();
        gui.init(controller_params);

        // Main program loop

        while (true) {
            controller.tick();
            gui.render(controller);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Reset button case

            if (gui.restartFlag) {
                gui_intro = new GUI_intro();
                String[] controller_params1 = gui_intro.show();

                while (controller_params1[0] == null) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                controller = new Controller(controller_params1);
                gui = new GUI();

                controller.init();
                gui.init(controller_params1);
            }
        }

    }
}
