import java.util.ArrayList;

public class Elevator {
    private int id;
    private int currentFloor;
    private boolean moving;
    private int direction; // 0 for up, 1 for down
    ArrayList<Passenger> passengers;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.moving = false;
        this.direction = 0;
        this.passengers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int amountPassengers() {
        return passengers.size();
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
    }

    public void motion() {

        if (this.direction == 0) {
            this.currentFloor++;
        } else {
            this.currentFloor--;
        }
    }

}
