import java.util.ArrayList;

/**
 * Created by Alex on 4/25/20.
 */
public class QLearning {
    private char[][] track;
    private double[][][][][] q;
    private Action[] actions;
    double learningRate = .2;
    double dr = .9;
    int reward = -1;
    int maxSteps = 500;

    int numberOfCompletions = 0;


    // time trial 


    public void runTrack(int numberOfEpisodes, int wallMode){
        ArrayList<int[]> startIndexes = findStartIndices();
        ArrayList<int[]> finishIndexes = findFinishIndices();
        for (int e = 0; e < numberOfEpisodes; e++) {
            boolean crossedFinish = false;
            int trackSteps = 0;
            while(crossedFinish == false){

                trackSteps++;
                int randomStart = (int)(Math.random()*startIndexes.size());
                QCar trackCar = new QCar(startIndexes.get(randomStart)[0], startIndexes.get(randomStart)[1], track);
                int currentCarIVel = trackCar.getiVel();
                int currentCarJVel = trackCar.getjVel();
                if(currentCarIVel < 0){
                    currentCarIVel = (-1)*currentCarIVel+5;
                } if(currentCarJVel < 0){
                    currentCarJVel = (-1)*currentCarJVel+5;
                }
                int actionToTake = findBestAction(trackCar.getI(), trackCar.getJ(), currentCarIVel, currentCarJVel);
                int taken = trackCar.takeAction(actions[actionToTake]);
                if(wallMode == -1 && taken == -1){
                    trackCar.setI(startIndexes.get(randomStart)[0]);
                    trackCar.setJ(startIndexes.get(randomStart)[1]);
                    trackCar.setIVel(0);
                    trackCar.setJVel(0);
                }
                if(trackSteps == maxSteps){
                    break;
                }


                if(track[trackCar.getI()][trackCar.getJ()] == 'F') {
                    printTrack(trackCar.getI(), trackCar.getJ());
                    crossedFinish = true;
                    System.out.println("Car was sucsessfull!!!!");
                    System.out.println("the number of steps was: " + trackSteps);
                }
            }
        }

    }

    public void loadTrack(char[][] track){
        this.track = track;
    }

    public void startLearning(int numberOfEpisodes, int wallMode){
        q = new double[track.length][track[0].length][11][11][9];
        ArrayList<int[]> startIndexes = findStartIndices();
        ArrayList<int[]> finishIndexes = findFinishIndices();
        initializeQ();
        initializeActions();
        for (int e = 0; e < numberOfEpisodes; e++) {
            System.out.println(e);
            boolean crossedFinish = false;
            int randomStart = (int)(Math.random()*startIndexes.size());
            QCar car = new QCar(startIndexes.get(randomStart)[0], startIndexes.get(randomStart)[1], track);
            int steps = 0;
            while(crossedFinish == false){
                steps++;
                if(steps == maxSteps){
                    System.out.println("Failed");
                    break;
                }
                int currentCarI = car.getI();
                int currentCarJ = car.getJ();
                int currentCarIVel = car.getiVel();
                int currentCarJVel = car.getjVel();
                if(currentCarIVel < 0){
                    currentCarIVel = (-1)*currentCarIVel+5;
                } if(currentCarJVel < 0){
                    currentCarJVel = (-1)*currentCarJVel+5;
                }

                int actionToTake = findBestAction(currentCarI, currentCarJ, currentCarIVel, currentCarJVel);

                double probability = Math.random();
                if(probability >= 0.7){
                    actionToTake = (int)(Math.random()*9);
                }
                int taken = car.takeAction(actions[actionToTake]);

                    // if move was not taken because
                if(taken != 0){
                    q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake] = 0;
                } if(taken == -1 && wallMode == -1) {
                    // here is where we will implement hit-wall -> back to start
                    // 33
                    if (e > 10000){
                        car.setI(9);
                        car.setJ(20);
                    } else if (e > 50000){
                        car.setI(startIndexes.get(randomStart)[0]);
                        car.setJ(startIndexes.get(randomStart)[1]);
                    } else {
                        car.setI(9);
                        car.setJ(27);
                    }

                    car.setIVel(0);
                    car.setJVel(0);
                    q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake] = 0;
                } else if (taken == -1) {
                    car.setIVel(0);
                    car.setJVel(0);
                    q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake] = 0;
                } else {
                    int tempCurrentCarIVel = car.getiVel();
                    int tempCurrentCarJVel = car.getjVel();
                    if(tempCurrentCarIVel < 0){
                        tempCurrentCarIVel = (-1)*tempCurrentCarIVel+5;
                    } if(tempCurrentCarJVel < 0){
                        tempCurrentCarJVel = (-1)*tempCurrentCarJVel+5;
                    }
                    q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake] =
                            ((1-learningRate)*q[currentCarI][currentCarJ][currentCarIVel]
                                    [currentCarIVel][actionToTake])+ learningRate*(reward + dr*(q[car.getI()]
                                    [car.getJ()][tempCurrentCarIVel][tempCurrentCarJVel]
                                    [findBestAction(car.getI(), car.getJ(), tempCurrentCarIVel, tempCurrentCarJVel)]));
//                    System.out.println("here "+ q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake]);
                }
//                System.out.println("VelocityI: "+car.getiVel());
//                System.out.println("VelocityJ: "+car.getjVel());
//                printTrack(car.getI(), car.getJ());

                if(track[car.getI()][car.getJ()] == 'F') {
                    printTrack(car.getI(), car.getJ());
                    crossedFinish = true;
                    System.out.println("were finished!");
                    numberOfCompletions++;
                }
            }
        }
        System.out.println("We completed the track # of times : " + numberOfCompletions);
    }







    private int findBestAction(int currentCarI, int currentCarJ, int currentCarIVel, int currentCarJVel) {
        int currentIndexOfBestAction = 0;
        double currentBestAction = 0;
        for (int i = 0; i < 9; i++) {
            if(currentBestAction < q[currentCarI][currentCarJ][currentCarIVel][currentCarJVel][i]){
                currentBestAction = q[currentCarI][currentCarJ][currentCarIVel][currentCarJVel][i];
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
                for (int a = 0; a < 11; a++) {
                    for (int b = 0; b < 11; b++) {
                        if(track[i][j] == 'F'){
                            for (int k = 0; k < 9; k++) {
                                q[i][j][a][b][k] = 0;
                            }

                        } else {
                            for (int k = 0; k < 9; k++) {
                                q[i][j][a][b][k] = Math.random();
                            }
                        }
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
