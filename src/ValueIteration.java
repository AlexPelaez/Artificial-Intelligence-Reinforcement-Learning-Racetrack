import java.util.ArrayList;
import java.util.Objects;

public class ValueIteration extends LearningBase {

    private char[][] track;
    private double[][][][][] q;
    private double[][][][]v;
    private Action[] actions;
    double learningRate = .2;
    double dr = .9;
    int reward = -1;
    int maxSteps = 500;

    int numberOfCompletions = 0;

    public void loadTrack(char[][] track){
        this.track = track;
    }

    public void startLearning(int numOfEpisodes, int wallMode){
        q = new double[track.length][track[0].length][11][11][9];
        v = new double[track.length][track[0].length][11][11];
        q = initializeQ(track,q);
        initializeV();
        actions = initializeActions();


        for (int e = 0; e < numOfEpisodes; e++) {

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

                            if (track[i][j] == '#'){
                                v[i][j][vi][vj] = -10;
                                continue;
                            }

                            for (int k = 0; k < actions.length; k++) {//for loop looping through all actions

                                //data structure to hold our next state for the one step look ahead.
                                //sPrime[0] = newi;
                                //sPrime[1] = newj;
                                //sPrime[2] = newvi;
                                //sprime[3] = newvj;
                                int[] sPrimeSuccess = new int[4];//Calculate value for successful acceleration
                                sPrimeSuccess = getNextAction(i, j, vi, vj, actions[k].getIAcceleration(),actions[k].getJAcceleration(), wallMode);

                                double valAccelerationSuccess = prev_v[sPrimeSuccess[0]][sPrimeSuccess[1]][sPrimeSuccess[2]][sPrimeSuccess[3]];

                                int[] sPrimeFail = new int[4];//Calculate value for failed acceleration
                                sPrimeFail = getNextAction(i, j, vi, vj, 0,0, wallMode);

                                double valAccelerationFail = prev_v[sPrimeSuccess[0]][sPrimeSuccess[1]][sPrimeSuccess[2]][sPrimeSuccess[3]];

                                double value = (valAccelerationSuccess * 0.8) + (valAccelerationFail * 0.2);

                            }

                        }



                    }


                }

            }



        }


    }
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

    //calculate the next position
    private int[] getNextAction(int previ, int prevj, int prevvi, int prevvj, int ai, int aj, int crash){
        int [] newNew = new int[4];
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

        try {
            if (track[newi][newj] != '.') {//a crash has occurred
                if (crash != -1) {//if crash does not restart car
                    newi = previ;
                    newj = prevj;

                } else {
                    int startFresh[] = getNewStart();
                    newi = startFresh[0];
                    newj = startFresh[1];

                }
                newvi = 0;
                newvj = 0;
            }
        }catch (IndexOutOfBoundsException e){
            if (crash != -1) {//if crash does not restart car
                newi = previ;
                newj = prevj;

            } else {
                int startFresh[] = getNewStart();
                newi = startFresh[0];
                newj = startFresh[1];

            }
            newvi = 0;
            newvj = 0;


        }
        newNew[0] = newi;
        newNew[1] = newj;
        newNew[2] = newvi;
        newNew[3] = newvj;

        return newNew;
    }








}
