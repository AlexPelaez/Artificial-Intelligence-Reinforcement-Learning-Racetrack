public class Action {
    private int xAcceleration;
    private int yAcceleration;

    public Action(int xAcceleration, int yAcceleration){
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;

    }

    public int getxAcceleration() {
        return xAcceleration;
    }

    public int getyAcceleration() {
        return yAcceleration;
    }


    // used for Q learning
    public int getIAcceleration() {
        return xAcceleration;
    }

    public int getJAcceleration() {
        return yAcceleration;
    }
}
