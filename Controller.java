public class Controller {
    private Elevator elevator;

    public Controller(Elevator elevator) {
        this.elevator = elevator;
    }

    public void processRequest(Request request) {
        System.out.println("Processing request: from " + request.getStartFloor() + " to " + request.getEndFloor() + "\n");
        elevator.addPickup(request);        
    }

    public void driver() {
        System.out.println("Driver starting — entering elevator loop\n");
        while (elevator.getState() != Elevator.ElevatorState.IDLE) {
            elevator.elevatorAction();
        }
        System.out.println("Driver finished — elevator is IDLE at floor " + elevator.getCurrentFloor());
    }


}