/**
 * Created by Alex on 4/25/20.
 */
public class QCar {
    private char[][] track;
    private int i;
    private int j;
    private int iVel;
    private int jVel;

    // Car used for QLearning
    public QCar(int i, int j, char[][] track){
        this.track = track;
        this.i = i;
        this.j = j;
        iVel = 0;
        jVel = 0;
    }

    /**
     * Parameters:
     * aI: acceleration in the I direction
     * aJ: acceleration in the J direction
     *
     * changeVelocity: Changes the velocity of the car
     *
     * Returns:
     * boolean: true if all velocity could be changed false otherwise
     */
    private boolean changeVelocity(int aI, int aJ){
        // set new velocities
        int tempiV = iVel+aI;
        int tempjV = jVel+aJ;
        if(tempjV > 5 || tempjV < -5 || tempiV > 5 || tempiV < -5){
            return false;
        } else {
            iVel += aI;
            jVel += aJ;
        }
        return true;
    }

    /**
     * Parameters:
     * Action a: action to be taken
     *
     * takeAction: Loads evidence into the network
     *
     * Returns:
     * int: if action could be taken or not. If not returns the reason
     */
    public int takeAction(Action a) {
        if(changeVelocity(a.getIAcceleration(), a.getJAcceleration())){
            i += iVel;
            j += jVel;

            if(i < 0 || j < 0 || i >= track.length || j >= track[0].length || track[i][j] == '#'){
                i -=iVel;
                j -=jVel;
                changeVelocity((-1)*a.getIAcceleration(), (-1)*a.getJAcceleration());

                return -1;
            }

            return 0;
        } else {
            return 1;
        }

    }

    // Getters and setters for various instance variables
    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getiVel(){
        return iVel;
    }

    public int getjVel(){
        return jVel;
    }

    public void setI(int newI){
        i = newI;
    }

    public void setJ(int newJ){
        j = newJ;
    }

    public void setIVel(int newIVel){
        iVel = newIVel;
    }

    public void setJVel(int newJVel){
        jVel = newJVel;
    }
}
