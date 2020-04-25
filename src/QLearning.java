import java.util.ArrayList;

/**
 * Created by Alex on 4/25/20.
 */
public class QLearning {
    private char[][] track;
    private double[][][] q;
    private Action[] actions;

    public void loadTrack(char[][] track){
        this.track = track;
    }

    public void startLearning(int numberOfEpisodes, int wallMode){
        q = new double[track.length][track[0].length][9];
        ArrayList<int[]> startIndexes = findStartIndices();
        ArrayList<int[]> finishIndexes = findFinishIndices();
        initializeQ();
        initializeActions();
        for (int e = 0; e < 1; e++) {
            boolean crossedFinish = false;
            int randomStart = (int)(Math.random()*startIndexes.size());
            QCar car = new QCar(startIndexes.get(randomStart)[0], startIndexes.get(randomStart)[1], track);
            while(crossedFinish == false){
                int currentCarI = car.getI();
                int currentCarJ = car.getJ();
                int actionToTake = findBestAction(currentCarI, currentCarJ);
                double probability = Math.random();
                if(probability >= 0.8){
                    System.out.println("Not taking the best action");
                    actionToTake = (int)(Math.random()*9);
                }
                System.out.println("Action to take: "+actionToTake);
                int taken = car.takeAction(actions[actionToTake]);
                if(taken != 0){
                    q[currentCarI][currentCarJ][actionToTake] = 0;
                } if(taken == -1 && wallMode == -1) {
                    // here is where we will implement hit-wall -> back to start
                    car.setI(3);
                    car.setJ(33);
                    car.setIVel(0);
                    car.setJVel(0);
                } else {

                }
                printTrack(car.getI(), car.getJ());
                if(track[car.getI()][car.getJ()] == 'F') {
                    crossedFinish = true;
                    System.out.println("were finished!");
                }
            }
        }
    }







    private int findBestAction(int currentCarI, int currentCarJ) {
        int currentIndexOfBestAction = 0;
        double currentBestAction = 0;
        for (int i = 0; i < 9; i++) {
            if(currentBestAction < q[currentCarI][currentCarJ][i]){
                currentBestAction = q[currentCarI][currentCarJ][i];
                currentIndexOfBestAction = i;
            }
        }
        return currentIndexOfBestAction;
    }

    private void initializeActions(){
        actions = new Action[9];
        actions[0] = new Action(0,0);
        actions[1] = new Action(0,1);
        actions[2] = new Action(0,-1);
        actions[3] = new Action(1,0);
        actions[4] = new Action(1,1);
        actions[5] = new Action(1,-1);
        actions[6] = new Action(-1,0);
        actions[7] = new Action(-1,1);
        actions[8] = new Action(-1,-1);
    }

    private void initializeQ(){
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q[0].length; j++) {
                if(track[i][j] == 'F'){
                    for (int k = 0; k < 9; k++) {
                        q[i][j][k] = 0;
                    }

                } else {
                    for (int k = 0; k < 9; k++) {
                        q[i][j][k] = Math.random();
                    }
                }
            }
        }
    }

    private ArrayList<int[]> findStartIndices(){
        ArrayList<int[]> startIndexesList = new ArrayList<>();
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if(track[i][j] == 'S'){
                    int[] temp = {i, j};
                    startIndexesList.add(temp);
                }
            }
        }

        return startIndexesList;
    }

    private ArrayList<int[]> findFinishIndices(){
        ArrayList<int[]> finishIndexesList = new ArrayList<>();
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if(track[i][j] == 'F'){
                    int[] temp = {i, j};
                    finishIndexesList.add(temp);
                }
            }
        }

        return finishIndexesList;
    }

    private void printTrack(int i, int j){
        for(int a = 0; a < track.length; a++){
            for(int b = 0; b < track[0].length; b++){
                if(a == i && b == j) {
                    System.out.print('C');
                } else {
                    System.out.print(track[a][b]);
                }
            }
            System.out.println();
        }
    }

    private void printQ() {
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < q[0].length; j++) {
                for (int k = 0; k < 9; k++) {
                    System.out.println(q[i][j][k]);
                }
            }
        }
    }

}
