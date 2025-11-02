import java.util.*;
public class Elevator {

    enum ElevatorState {
        MOVING_UP,
        MOVING_DOWN,
        STOPPED,
        IDLE
    }
    enum DoorState {
        OPEN,
        CLOSED
    }

    private int curr_floor;
    private DoorState door;
    public ElevatorState state;

    private PriorityQueue<Integer> queueUp = new PriorityQueue<>();
    private PriorityQueue<Integer> queueDown = new PriorityQueue<>();

    private final Map<Integer, List<Integer>> floorPickups = new HashMap<>();
    private Set<Integer> upSet = new HashSet<>();
    private Set<Integer> downSet = new HashSet<>();

    public Elevator(int curr_floor, DoorState door, ElevatorState state) {
        this.curr_floor = curr_floor;
        this.door = door;
        this.state = ElevatorState.STOPPED;
        this.queueUp = new PriorityQueue<>();
        this.queueDown = new PriorityQueue<>((a, b) -> b - a);
        System.out.println("Elevator initialized at floor " + this.curr_floor + ", door=" + this.door + ", state=" + this.state);
    }

    public void addPickup(Request request) {
        int startFloor = request.getStartFloor();
        int endFloor = request.getEndFloor();

        floorPickups.computeIfAbsent(startFloor, k -> new ArrayList<>()).add(endFloor);

        if (startFloor == this.curr_floor) {
            this.door = DoorState.OPEN;
            List<Integer> dests = floorPickups.remove(curr_floor);
            if (dests != null) {
                for (int dest : dests) {
                    addFloor(dest, false);
                }
            }
            this.door = DoorState.CLOSED;
            return;
        }

        boolean wantsUp = endFloor > startFloor;
        if (wantsUp) {
            if (upSet.add(startFloor)) {
                queueUp.add(startFloor);
                System.out.println("Added pickup at floor " + startFloor + " to UP queue");
            }
        } else if (endFloor < startFloor) {
            if (downSet.add(startFloor)) {
                queueDown.add(startFloor);
                System.out.println("Added pickup at floor " + startFloor + " to DOWN queue");
            }
        } else {
            System.out.println("Request at floor " + startFloor + " has same start and end — no movement.");
        }
    }

    public void addFloor(int floor, boolean urgent) {
        if (floor > this.curr_floor) {
            if (upSet.add(floor)) {
                queueUp.add(floor);
                System.out.println("Added floor " + floor + " to UP queue");
            } 
        } else if (floor < this.curr_floor) {
            if (downSet.add(floor)) {
                queueDown.add(floor);
                System.out.println("Added floor " + floor + " to DOWN queue");
            } 
        } else {
            this.door = DoorState.OPEN;
            System.out.println("Already at floor " + floor + " — opening doors.");
            this.door = DoorState.CLOSED;
        }
    }

    public void elevatorAction() {
        if (this.state == ElevatorState.MOVING_UP && !queueUp.isEmpty()) {
            moveUp();
        } else if (this.state == ElevatorState.MOVING_DOWN && !queueDown.isEmpty()) {
            moveDown(); 
        } else if (!queueUp.isEmpty()) {
            this.state = ElevatorState.MOVING_UP;
            System.out.println("State -> MOVING_UP");
            moveUp();
        } else if (!queueDown.isEmpty()) {
            this.state = ElevatorState.MOVING_DOWN;
            System.out.println("State -> MOVING_DOWN");
            moveDown();
        } else {
            this.state = ElevatorState.IDLE;
            System.out.println("No pending requests — elevator is IDLE at floor " + this.curr_floor);
        }
    }

    public void moveUp() {
        Integer next = queueUp.poll();
        if (next == null) {
            this.state = queueDown.isEmpty() ? ElevatorState.IDLE : ElevatorState.MOVING_DOWN;
            upSet.remove(next);
            queueUp.remove(next);
            return;
        }
        upSet.remove(next);
        System.out.println("Moving up to floor " + next + " from " + this.curr_floor);
        this.curr_floor = next;
        System.out.println("Arrived at floor " + this.curr_floor);
        System.out.println("----Opening doors at floor " + this.curr_floor + "----");
        this.door = DoorState.OPEN;
        List<Integer> destinations = floorPickups.remove(next);
        if (destinations != null) {
            for (int dest : destinations) {
                addFloor(dest, false);
            }
        }
        System.out.println("----Closing doors at floor " + this.curr_floor + "----");
        this.door = DoorState.CLOSED;

        if (queueUp.isEmpty() && queueDown.isEmpty()) {
            this.state = ElevatorState.STOPPED;
            System.out.println("No more requests — state -> STOPPED");
        } else if (queueUp.isEmpty() && !queueDown.isEmpty()) {
            this.state = ElevatorState.MOVING_DOWN;
        }
    }

    public void moveDown() {
        Integer next = queueDown.poll();
        if (next == null) {
            this.state = queueUp.isEmpty() ? ElevatorState.IDLE : ElevatorState.MOVING_UP;
            upSet.remove(next);
            queueUp.remove(next);
            return;
        }
        downSet.remove(next);
        System.out.println("Moving down to floor " + next + " from " + this.curr_floor);
        this.curr_floor = next;
        System.out.println("Arrived at floor " + this.curr_floor);
        System.out.println("----Opening doors at floor " + this.curr_floor + "----");
        this.door = DoorState.OPEN;
        List<Integer> destinations = floorPickups.remove(next);
        if (destinations != null) {
            for (int dest : destinations) {
                addFloor(dest, false);
            }
        }
        System.out.println("----Closing doors at floor " + this.curr_floor + "----");
        this.door = DoorState.CLOSED;

        if (queueUp.isEmpty() && queueDown.isEmpty()) {
            this.state = ElevatorState.STOPPED;
            System.out.println("No more requests — state -> STOPPED");
        } else if (queueDown.isEmpty() && !queueUp.isEmpty()) {
            this.state = ElevatorState.MOVING_UP;
        }
    }

    public ElevatorState getState() {
        return this.state;
    }

    public int getCurrentFloor() {
        return this.curr_floor;
    }
}