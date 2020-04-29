import java.util.ArrayList;

/**
 * Created by Alex on 4/26/20.
 */
public class Timetrial extends LearningBase {

    private char[][] track;
    private double[][][][][] model;
    private Action[] actions;
    private int maxSteps = 500;

    public Timetrial(double[][][][][] model, char[][] track){
        this.model = model;
        this.track = track;
        actions = initializeActions();
    }

    public void runTrack(int numberOfRaces, int wallMode){
        ArrayList<int[]> startIndexes = findStartIndices(track);
        ArrayList<int[]> finishIndexes = findFinishIndices(track);
        for (int e = 0; e < numberOfRaces; e++) {
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
                int actionToTake = findBestAction(model, trackCar.getI(), trackCar.getJ(), currentCarIVel, currentCarJVel);
                int taken = trackCar.takeAction(actions[actionToTake]);
                if(wallMode == -1 && taken == -1){
                    trackCar.setI(startIndexes.get(randomStart)[0]);
                    trackCar.setJ(startIndexes.get(randomStart)[1]);
                    trackCar.setIVel(0);
                    trackCar.setJVel(0);
                } else if(wallMode == -1){
                    trackCar.setIVel(0);
                    trackCar.setJVel(0);
                }
                if(trackSteps == maxSteps){
                    break;
                }


                if(track[trackCar.getI()][trackCar.getJ()] == 'F') {
                    printTrack(track, trackCar.getI(), trackCar.getJ());
                    crossedFinish = true;
                    System.out.println("Car was sucsessfull!!!!");
                    System.out.println("the number of steps was: " + trackSteps);
                }
            }
        }

    }
}
