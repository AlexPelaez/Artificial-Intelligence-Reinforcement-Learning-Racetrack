import java.util.ArrayList;

/**
 * Created by Alex on 4/25/20.
 */
public class QLearning extends LearningBase {
    private char[][] track;
    private double[][][][][] q;
    private Action[] actions;
    double learningRate = .2;
    double dr = .9;
    int reward = -1;
    int maxSteps = 500;

    int numberOfCompletions = 0;

    public void loadTrack(char[][] track){
        this.track = track;
    }

    public void startLearning(int numberOfEpisodes, int wallMode){
        q = new double[track.length][track[0].length][11][11][9];
        ArrayList<int[]> startIndexes = findStartIndices(track);
        ArrayList<int[]> finishIndexes = findFinishIndices(track);
        q = initializeQ(track, q);
        actions = initializeActions();
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

                int actionToTake = findBestAction(q, currentCarI, currentCarJ, currentCarIVel, currentCarJVel);

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
                                    [findBestAction(q, car.getI(), car.getJ(), tempCurrentCarIVel, tempCurrentCarJVel)]));
//                    System.out.println("here "+ q[currentCarI][currentCarJ][currentCarIVel][currentCarIVel][actionToTake]);
                }
//                System.out.println("VelocityI: "+car.getiVel());
//                System.out.println("VelocityJ: "+car.getjVel());
//                printTrack(car.getI(), car.getJ());

                if(track[car.getI()][car.getJ()] == 'F') {
                    printTrack(track, car.getI(), car.getJ());
                    crossedFinish = true;
                    System.out.println("were finished!");
                    numberOfCompletions++;
                }
            }
        }
        System.out.println("We completed the track # of times : " + numberOfCompletions);
    }


    public double[][][][][] getModel(){
        return q;
    }
}
