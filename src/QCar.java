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

    public void undoAction(Action a) {

    }


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
