public class Request {
    private int start_floor;
    private int end_floor;

    public Request(int start_floor, int end_floor) {
        this.start_floor = start_floor;
        this.end_floor = end_floor;
    }

    public int getStartFloor() {
        return start_floor;
    }

    public int getEndFloor() {
        return end_floor;
    }
}