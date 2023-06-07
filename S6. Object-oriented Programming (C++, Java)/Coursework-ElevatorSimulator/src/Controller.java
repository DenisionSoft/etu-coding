import java.util.ArrayList;
import java.util.Iterator;

public class Controller {
    private int topFloor;
    private int maxCapacity;
    private int numSections;
    private ArrayList<Elevator> elevators = new ArrayList<>();
    private ArrayList<ArrayList<Integer[]>> sections = new ArrayList<>();
    private ArrayList<Integer[]> GObuttons = new ArrayList<>();
    private ArrayList<Integer> loadingLock = new ArrayList<>();

   public Controller(String[] args) {
       this.numSections = args.length > 0 ? Integer.parseInt(args[0]) : 2;
       this.topFloor = args.length > 1 ? Integer.parseInt(args[1]) - 1 : 8;
       this.maxCapacity = args.length > 2 ? Integer.parseInt(args[2]) : 4;
    }

    public int getTopFloor() {
        return topFloor;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getNumSections() {
        return numSections;
    }

    public void lockElevator(int id) {
        loadingLock.set(id, 1);
    }

    public void unlockElevator(int id) {
        loadingLock.set(id, 0);
    }

    public int getLoadingLock(int id) {
        return loadingLock.get(id);
    }

    public void setFloorOfPassenger(int id, int floor) {
        for (Passenger passenger : elevators.get(id).getPassengers()) {
            if (passenger.getTargetFloor() == -1) {
                passenger.setTargetFloor(floor-1);
                break;
            }
        }
    }

    public ArrayList<Integer[]> getFloors(int id) {
        return sections.get(id);
    }

    public int getFloorOf(int id) {
        return elevators.get(id).getCurrentFloor();
    }

    public boolean getStatusOf(int id) {
        return elevators.get(id).isMoving();
    }

    public int getPassengersOf(int id) {
        return elevators.get(id).amountPassengers();
    }

    public int getDirectionOf(int id) {
        return elevators.get(id).getDirection();
    }

    public boolean getGOStatusOf(int id, int value) {
        return GObuttons.get(id)[value] == 1;
    }

    public void setGOStatusOf(int id, int value, int status) {
        GObuttons.get(id)[value] = status;
    }

    public void makeCall(int id, int floor, int up, int down) {
       sections.get(id).get(floor-1)[0] += up;
       sections.get(id).get(floor-1)[1] += down;
    }

    public void init() {
        for (int i = 0; i < numSections; i++) {
            Elevator elevator = new Elevator(i);
            elevators.add(elevator);
        }

        for (int i = 0; i <= numSections; i++) {
            ArrayList<Integer[]> floors = new ArrayList<>();
            for (int j = 0; j <= topFloor; j++) {
                Integer[] floor = new Integer[2];
                floor[0] = 0;
                floor[1] = 0;
                floors.add(floor);
            }
            sections.add(floors);
        }

        for (int i = 0; i < numSections; i++) {
            Integer[] GObutton = new Integer[2];
            GObutton[0] = 0;
            GObutton[1] = 0;
            GObuttons.add(GObutton);
        }

        for (int i = 0; i < numSections; i++) {
            loadingLock.add(0);
        }
    }

    public static int sumfl(ArrayList<Integer[]> floors) {
        int sum = 0;
        for (Integer[] floor : floors) {
            sum += floor[0];
            sum += floor[1];
        }
        return sum;
    }

    public static int sumflslice(ArrayList<Integer[]> floors, int start, int end) {
        if (start > end) {
            return 0;
        }
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += floors.get(i)[0];
            sum += floors.get(i)[1];
        }
        return sum;
    }

    public static String printfloors(ArrayList<Integer[]> floors) {
        String str = "";
        for (int i = 0; i < floors.size(); i++) {
            str += i + ": " + floors.get(i)[0] + " " + floors.get(i)[1] + " | ";
        }
        return str;
    }

    public static String printpassengers(ArrayList<Passenger> passengers) {
        String str = "";
        for (int i = 0; i < passengers.size(); i++) {
            str += passengers.get(i).getTargetFloor() + " ";
        }
        return str;
    }

    public void tick() {

            for (Elevator elevator : elevators) {

                if (getLoadingLock(elevator.getId()) == 1) {
                    boolean unlocktest = true;
                    for (Passenger passenger : elevator.getPassengers()) {
                        if (passenger.getTargetFloor() == -1) {
                            unlocktest = false;
                            break;
                        }
                    }
                    if (unlocktest) {
                        unlockElevator(elevator.getId());
                    }
                    else {
                        continue;
                    }
                }

                // floors for the current elevator
                ArrayList<Integer[]> floors = sections.get(elevator.getId());

                // local variables for the current elevator
                int currentFloor = elevator.getCurrentFloor();
                boolean isFull = elevator.amountPassengers() == maxCapacity;

                // the future button variables
                Integer[] GOButton = GObuttons.get(elevator.getId());

                // first, check if the elevator is moving
                if (elevator.isMoving()) {

                    // elevator moves in its direction
                    elevator.motion();

                    // update the local variables
                    currentFloor = elevator.getCurrentFloor();
                    isFull = elevator.amountPassengers() == maxCapacity;

                    // if the elevator is not full and there are calls on the current floor in the current direction
                    if (!isFull && floors.get(currentFloor)[elevator.getDirection()] > 0) {
                        // stop the elevator
                        elevator.setMoving(false);
                    }

                    else if (elevator.amountPassengers() == 0 && (floors.get(currentFloor)[0] > 0 || floors.get(currentFloor)[1] > 0)) {
                        if (elevator.getDirection() == 0 && sumflslice(floors, currentFloor + 1, topFloor) == 0) {
                            elevator.setMoving(false);
                        }
                        else if (elevator.getDirection() == 1 && sumflslice(floors, 0, currentFloor - 1) == 0) {
                            elevator.setMoving(false);
                        }
                    }

                    // if the elevator is not empty and there are passengers that want to get off here
                    else if (elevator.amountPassengers() > 0){
                        for (Passenger passenger : elevator.getPassengers()) {
                            if (passenger.getTargetFloor() == currentFloor) {
                                elevator.setMoving(false);
                                break;
                            }
                        }
                    }
                }

                // Logging
                System.out.println("Elevator " + elevator.getId() + ":\n");
                System.out.println("Floor: " + currentFloor);
                System.out.println("Status: " + (elevator.isMoving() ? "moving" : "stopped"));
                System.out.println("Direction: " + (elevator.getDirection() == 0 ? "up" : "down"));
                System.out.println("Passengers: " + elevator.amountPassengers() + "/" + maxCapacity);
                System.out.println("Passengers: " + printpassengers(elevator.getPassengers()));
                System.out.println("GO button: " + (GOButton[0] == 1 ? "shown" : "hidden"));
                System.out.println("All floors: " + printfloors(floors) + "\n");


                // second, check if the elevator stopped
                if (!elevator.isMoving()) {

                    // check conditions to change direction
                    if (currentFloor == 0) {
                        elevator.setDirection(0);
                    }
                    else if (currentFloor == topFloor) {
                        elevator.setDirection(1);
                    }

                    // if the elevator is not empty and there are passengers that want to get off here
                    if (elevator.amountPassengers() > 0){
                        Iterator<Passenger> iterator = elevator.getPassengers().iterator();
                        while (iterator.hasNext()) {
                            Passenger passenger = iterator.next();
                            if (passenger.getTargetFloor() == currentFloor) {
                                iterator.remove();
                            }
                        }
                    }

                    // update the local variable
                    isFull = elevator.amountPassengers() == maxCapacity;

                    // if elevator is not full and there are calls on the current floor in the current direction
                    while (!isFull && floors.get(currentFloor)[elevator.getDirection()] > 0) {
                        elevator.addPassenger(new Passenger(-1));
                        floors.get(currentFloor)[elevator.getDirection()]--;
                        isFull = elevator.amountPassengers() == maxCapacity;
                    }

                    // if anyone boarded the elevator here, lock it
                    for (Passenger passenger : elevator.getPassengers()) {
                        if (passenger.getTargetFloor() == -1) {
                            lockElevator(elevator.getId());
                            break;
                        }
                    }

                    // different cases for an empty elevator
                    if (elevator.amountPassengers() == 0) {
                        // two main cases: in direction up and in direction down
                        if (elevator.getDirection() == 0) {
                            // if there's a call on the current floor up
                            if (floors.get(currentFloor)[0] > 0) {
                                continue;
                            }
                            // if there are calls higher than the current floor
                            else if (sumflslice(floors, currentFloor + 1, topFloor) > 0) {
                                // do nothing
                            }
                            // if there's a call on the current floor down
                            else if (floors.get(currentFloor)[1] > 0) {
                                elevator.setDirection(1);
                                continue;
                            }
                            // if there are calls lower than the current floor
                            else if (sumflslice(floors, 0, currentFloor - 1) > 0) {
                                elevator.setDirection(1);
                            }
                        }
                        else if (elevator.getDirection() == 1) {
                            // if there's a call on the current floor down
                            if (floors.get(currentFloor)[1] > 0) {
                                continue;
                            }
                            // if there are calls lower than the current floor
                            else if (sumflslice(floors, 0, currentFloor - 1) > 0) {
                                // do nothing
                            }
                            // if there's a call on the current floor up
                            else if (floors.get(currentFloor)[0] > 0) {
                                elevator.setDirection(0);
                                continue;
                            }
                            // if there are calls higher than the current floor
                            else if (sumflslice(floors, currentFloor + 1, topFloor) > 0) {
                                elevator.setDirection(0);
                            }
                        }
                    }

                    // different cases for starting the elevator
                    // first, if there are passengers inside and button is not shown
                    if (elevator.amountPassengers() > 0 && GOButton[0] == 0 && getLoadingLock(elevator.getId()) == 0) {
                        // show the button
                        GOButton[0] = 1;
                    }
                    // second, if there are no passengers and there are calls anywhere
                    else if (elevator.amountPassengers() == 0 && sumfl(floors) > 0) {
                        // set the elevator in motion
                        elevator.setMoving(true);
                    }
                    // third, if the GO button is shown and pressed
                    else if (GOButton[0] == 1 && GOButton[1] == 1) {
                        // set the elevator in motion
                        elevator.setMoving(true);
                        // reset the button
                        GOButton[0] = GOButton[1] = 0;
                    }
                }
            }
    }
}