public class Action {
    private int xAcceleration;
    private int yAcceleration;

    public Action(int xAcceleration, int yAcceleration){
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;

    }

    public int getIAcceleration() {
        return xAcceleration;
    }

    public int getJAcceleration() {
        return yAcceleration;
    }
}
