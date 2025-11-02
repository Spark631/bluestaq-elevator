public class Main {
    public static void main(String[] args) {
        Elevator elevator = new Elevator(0, Elevator.DoorState.CLOSED, Elevator.ElevatorState.STOPPED);
        Controller controller = new Controller(elevator);
        Request request1 = new Request(2, 5); 
        Request request2 = new Request(7, 4);
        Request request3 = new Request(7, 9);
        Request request4 = new Request(3, 0);
        
        controller.processRequest(request1);
        controller.processRequest(request2);
        controller.processRequest(request3);
        controller.processRequest(request4);
        controller.driver();
        System.out.println("Elevator final floor: " + elevator.getCurrentFloor());
    }
}