import java.util.ArrayList;
import java.util.Arrays;

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

    public void startLearning(int numberOfEpisodes){
        ArrayList<int[]> startIndexes = findStartIndicies();
        initializeQ();
        initializeActions();
        for (int e = 0; e < numberOfEpisodes; e++) {
            boolean crossedFinish = false;
            int randomStart = (int)(Math.random()*startIndexes.size());
            Car car = new Car(startIndexes.get(randomStart)[0], startIndexes.get(randomStart)[1]);
            while(crossedFinish == false){
                car.takeAction(actions[findBestAction(car.getI(), car.getJ())]);
                printTrack(car.getX(), car.getY());








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
        q = new double[track.length][track[0].length][9];
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

    private ArrayList<int[]> findStartIndicies(){
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

}
