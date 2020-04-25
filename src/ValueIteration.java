import java.util.ArrayList;
import java.util.Objects;

public class ValueIteration {

    private char[][] raceTrack;
    private double[][] value;
    private ArrayList <Action>  actionlist = new ArrayList<Action>();
    private final double REWARD = -1;
    private final double GAMMA = .9;

    public ValueIteration(Racetrack r){
        this.raceTrack = r.getRacetrack();
        value = new double[raceTrack.length][raceTrack[0].length];
        initValueTable();
        initActionList();
        printTable();

    }

    public void valueIteration(){

    }

    public void initValueTable(){
        for(int i = 0; i < value.length;i++){
            for (int j = 0; j < value[0].length; j++){
                if(raceTrack[i][j] == '.'){
                    value[i][j] = 0.0;
                }
                else if(raceTrack[i][j] == 'S'){
                    value[i][j] =-1.0;
                }
                else if(raceTrack[i][j] == 'F'){
                    value[i][j] = 1.0;
                }
                else{
                    value[i][j] = -0.5;
                }
            }
        }

    }

    public void initActionList(){
        actionlist.add(new Action(0,0));
        actionlist.add(new Action(0,1));
        actionlist.add(new Action(0,-1));
        actionlist.add(new Action(1,0));
        actionlist.add(new Action(1,1));
        actionlist.add(new Action(1,-1));
        actionlist.add(new Action(-1,0));
        actionlist.add(new Action(-1,1));
        actionlist.add(new Action(-1,-1));

    }

    public void policy(){
        int t = 0;
        while(t <= 1000){
            t++;
            for(int i = 0; i < value.length; i++){
                for (int j = 0; j < value[0].length; j++){
                    double V = Double.NEGATIVE_INFINITY;
                    if(raceTrack[i][j] != 'F' && raceTrack[i][j] != 'S' && raceTrack[i][j] != '#') {
                        int maxXPos = 0;
                        int maxYPos = 0;
                        for(Action a : actionlist){

                            if(a.getxAcceleration() != 0 && a.getyAcceleration() != 0){
                                if(value[i + a.getxAcceleration()][j + a.getyAcceleration()] > V){
                                    maxXPos = i + a.getxAcceleration();
                                    maxYPos = j + a.getyAcceleration();
                                    V = value[i + a.getxAcceleration()][j + a.getyAcceleration()];
                                }

                            }

                        }
                        for(Action a : actionlist){

                            if(a.getxAcceleration() != 0 && a.getyAcceleration() != 0){
                                if(maxXPos != i + a.getxAcceleration() && maxYPos != j + a.getyAcceleration()){
                                    maxXPos = i + a.getxAcceleration();
                                    maxYPos = j + a.getyAcceleration();
                                    V = value[i + a.getxAcceleration()][j + a.getyAcceleration()];
                                }

                            }

                        }

                    }







                }

            }



        }

    }



    private void printTable() {
        for(int i = 0; i < value.length; i++){
            for(int j = 0; j < value[0].length; j++){
                System.out.print(value[i][j] + " ");
            }
            System.out.println();
        }
    }
}
