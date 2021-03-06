import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class ValueIteration extends LearningBase {

    private char[][] track;
    private double[][][][][] q;
    private double[][][][]v;
    private Action[] actions;
    double dr = .9;
    int reward;
    int wallMode;

    int learningStep = 0;


    int numberOfCompletions = 0;


    /**
     * Parameters:
     * char[][] track: racetrack data struct
     *
     * loadTrack: loads the given track into the value iteration algorithm
     *
     */
    public void loadTrack(char[][] track){
        this.track = track;
    }


    /**
     * Parameters:
     * int numOfEpisodes: number of episodes to run
     * int wallMode: collision mode
     *
     * startLearningValueIteration: generates a model using value iteration
     *
     */
    public void startLearningValueIteration(int numOfEpisodes, int wallMode){
        this.wallMode = wallMode;
        q = new double[track.length][track[0].length][11][11][9];
        v = new double[track.length][track[0].length][11][11];
        q = initializeQ(track,q);
        initializeV();
        actions = initializeActions();


        while(learningStep <= numOfEpisodes){
            learningStep++;

           double[][][][]prev_v = new double[track.length][track[0].length][11][11];

            for (int i = 0; i < track.length; i++) { //copy table to hold previous values for calculation of look ahead
                for (int j = 0; j < track[0].length; j++) {
                    for (int vi = 0; vi < 11; vi++) {
                        for (int vj = 0; vj < 11; vj++) {
                            prev_v[i][j][vi][vj] = v[i][j][vi][vj];

                        }
                    }
                }
            }

            for (int i = 0; i < track.length ; i++) {//For loop looping through all states
                for (int j = 0; j < track[0].length ; j++) {
                    for (int vi = 0; vi < 11; vi++) {
                        for (int vj = 0; vj < 11; vj++) {

                            if (track[i][j] == '#'){//train not to hit walls
                                v[i][j][vi][vj] = -10;
                                continue;
                            }

                            for (int k = 0; k < actions.length; k++) {//for loop looping through all actions

                                if(track[i][j] == 'F'){//Goal states have a reward of 0; every other state has a reward of -1
                                    reward = 0;

                                }else{
                                    reward = -1;
                                }

                                //data structure to hold our next state for the one step look ahead.
                                //sPrime[0] = newi;
                                //sPrime[1] = newj;
                                //sPrime[2] = newvi;
                                //sprime[3] = newvj;//Calculate value for successful acceleration
//                                System.out.println("I: " + i + " J: " + j + " VI: " + vi + " VJ: " + vj + " ai: " + actions[k].getIAcceleration() + " aj: " + actions[k].getJAcceleration());
                                int []sPrimeSuccess = getNextAction(i, j, vi, vj, actions[k].getIAcceleration(),actions[k].getJAcceleration(), wallMode);

                                //Debugging out of bounds error
//                                System.out.println(sPrimeSuccess[0]);
//                                System.out.println(sPrimeSuccess[1]);
//                                System.out.println(sPrimeSuccess[2]);
//                                System.out.println(sPrimeSuccess[3];




                                double valAccelerationSuccess = prev_v[sPrimeSuccess[0]][sPrimeSuccess[1]][sPrimeSuccess[2]][sPrimeSuccess[3]];

                                int[] sPrimeFail = new int[4];//Calculate value for failed acceleration
                                sPrimeFail = getNextAction(i, j, vi, vj, 0,0, wallMode);

                                double valAccelerationFail = prev_v[sPrimeFail[0]][sPrimeFail[1]][sPrimeFail[2]][sPrimeFail[3]];

                                double value = (valAccelerationSuccess * 0.8) + (valAccelerationFail * 0.2);

                                q[i][j][vi][vj][k] = reward + (dr * value);


                            }

                            double bestAction = Double.NEGATIVE_INFINITY;
                            int move = -1;

                            for (int k = 0; k < actions.length; k++) {
                                double action = q[i][j][vi][vj][k];

                                if (action > bestAction){
                                    bestAction = action;
                                    move = k;

                                }
                            }

                            v[i][j][vi][vj] = q[i][j][vi][vj][move];

                        }
                    }


                }

            }
            for (int i = 0; i < track.length ; i++) {//For loop looping through all states
                for (int j = 0; j < track.length; j++) {
                    if(track[i][j] == 'F'){
                        for (int vi = 0; vi < 11; vi++) {
                            for (int vj = 0; vj < 11; vj++) {
                                v[i][j][vi][vj] = 0;

                            }
                        }

                    }
                }
            }




        }


    }


    /**
     *
     * initializeV: populates the V array with 0 at finish linea and
     * random values elsewhere
     *
     */
    public void initializeV(){
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                for (int vi = 0; vi < 11; vi++) {
                    for (int vj = 0; vj < 11; vj++) {
                        if(track[i][j] == 'F'){
                            for (int k = 0; k < 9; k++) {
                                v[i][j][vi][vj] = 0;
                            }

                        } else {
                            for (int k = 0; k < 9; k++) {
                                v[i][j][vi][vj] = Math.random();
                            }
                        }
                    }
                }

            }
        }

    }

    /**
     *
     * getNewStart: get the start array
     *
     * Returns:
     * int[]: array containing the new starting locations
     */
    public int[] getNewStart(){
        int newStart[] = new int[2];
        ArrayList<int[]> startIndexes = findStartIndices(track);
        int randomStart = (int)(Math.random()*startIndexes.size());
        int starti = startIndexes.get(randomStart)[0];
        int startj = startIndexes.get(randomStart)[1];
        newStart[0] = starti;
        newStart[1] = startj;
        return  newStart;
    }


    /**
     * Parameters:
     * int previ: previous I position
     * int prevj: previous J position
     * int prevvj: previous I velocity
     * int prevvj: previous J velocity
     * int ai: acceleration I
     * int aj: acceleration J
     * int crash: type of crash mode
     *
     * getNextAction: finds the next action for the car
     *
     * Returns:
     * int[]: list of the new position and velocity of the car
     */
    private int[] getNextAction(int previ, int prevj, int prevvi, int prevvj, int ai, int aj, int crash){
        //calculate the next position
        int [] newNew = new int[4];

        if (prevvi > 5){
            prevvi = (previ - 5) * (-1);
        }
        if (prevvj > 5){
            prevvj = (prevj - 5) * (-1);
        }

        double accelerationFactor = Math.random();

        if (accelerationFactor > 0.8){
            ai = 0;
            aj = 0;
        }

        int newvi = prevvi + ai;
        int newvj = prevvj + aj;

        if(newvi > 5){
            newvi = 5;
        }

        if(newvi < -5){
            newvi = -5;
        }

        if(newvj > 5){
            newvj = 5;
        }

        if(newvj < -5){
            newvj = -5;
        }

        int newi = previ + newvi;
        int newj = prevj + newvj;





        if (newi >= track.length || newi < 0 || newj >= track[0].length || newj < 0 || track[newi][newj] == '#' ) {//a crash has occurred
//                System.out.println("crash occurred");
            if (crash != -1) {//if crash does not restart car
                    newi = previ;
                    newj = prevj;

            } else {
                    int startFresh[] = getNewStart();
                    newi = startFresh[0];
                    newj = startFresh[1];
                    newvi = 0;
                    newvj = 0;

            }


        }


        if(newvi < 0){
            newvi =(-1) *  newvi + 5;

        }
        if(newvj< 0){
            newvj =(-1) *  newvj + 5;

        }
        newNew[0] = newi;
        newNew[1] = newj;
        newNew[2] = newvi;
        newNew[3] = newvj;


        return newNew;
    }


    /**
     *
     * doTimeTrial: runs the timeTrial for the model generated
     *
     */
    public void doTimeTrial(){
        int numSteps = 0;




        char [][]printableTrack = new char[track.length][track[0].length];

        for (int i = 0; i < track.length ; i++) {// Copy track to printable track
            for (int j = 0; j <track[0].length ; j++) {
                printableTrack[i][j] = track[i][j];
            }

        }

        int []start = getNewStart();

        int posI = start[0];
        int posJ = start[1];

        int iVel = 0;
        int jVel = 0;


        while(numSteps < 500){
            System.out.println(numSteps);
            numSteps++;

            double bestAction = Double.NEGATIVE_INFINITY;
            int move = -1;

            if(track[posI][posJ] == 'F'){

                System.out.println("FUCK YES");
                System.out.println(numSteps);
                break;
            }

            printableTrack[posI][posJ] = 'X';

            for (int i = 0; i < printableTrack.length ; i++) {// Copy track to printable track
                for (int j = 0; j <printableTrack[0].length ; j++) {
                    System.out.print(printableTrack[i][j]);
                }
                System.out.println();

            }

            for (int k = 0; k < actions.length; k++) {
                double action = q[posI][posJ][iVel][jVel][k];

                if (action > bestAction){
                    bestAction = action;
                    move = k;

                }
            }



            int [] nextMove = getNextAction(posI, posJ, iVel, jVel,actions[move] .getIAcceleration(),actions[move].getJAcceleration(), wallMode);

            posI = nextMove[0];
            posJ = nextMove[1];
            iVel = nextMove[2];
            jVel = nextMove[3];

        }


    }

    /**
     *
     * getModel: gets the model generated by value iteration
     *
     * Returns:
     * double[][][][][]: the model to be returned
     */
    public double[][][][][] getModel(){
        return q;
    }


}
