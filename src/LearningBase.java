import java.util.ArrayList;

public abstract class LearningBase {

    /**
     * Parameters:
     * char[][] track: data structure holding the track
     * int i: car location i
     * int j: car location j
     *
     * printTrack: Prints track with a given car location
     *
     */
    public void printTrack(char[][] track, int i, int j){
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


    /**
     * Parameters:
     * double[][][][][] table: model data struct
     * int currentCarI: car position I
     * int currentCarJ: car position J
     * int currentCarIVel: car velocity I
     * int currentCarJVel: car velocity J
     *
     * findBestAction: finds the index of the best action
     *
     * Returns:
     * int: index of best action
     */
    public int findBestAction(double[][][][][] table, int currentCarI, int currentCarJ, int currentCarIVel, int currentCarJVel) {
        int currentIndexOfBestAction = 0;
        double currentBestAction = 0;
        for (int i = 0; i < 9; i++) {
            if(currentBestAction < table[currentCarI][currentCarJ][currentCarIVel][currentCarJVel][i]){
                currentBestAction = table[currentCarI][currentCarJ][currentCarIVel][currentCarJVel][i];
                currentIndexOfBestAction = i;
            }
        }
        return currentIndexOfBestAction;
    }


    /**
     * Parameters:
     * char[][] track: track data struct
     *
     * findStartIndices: finds all of the start indices
     *
     * Returns:
     * ArrayList<int[]>: list of the start indices
     */
    public ArrayList<int[]> findStartIndices(char[][] track){
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

    /**
     * Parameters:
     * char[][] track: track data struct
     *
     * findFinishIndices: finds all of the start indices
     *
     * Returns:
     * ArrayList<int[]>: list of the finish indices
     */
    public ArrayList<int[]> findFinishIndices(char[][] track){
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


    /**
     * findStartIndices: finds all of the start indices
     *
     * Returns:
     * Action[]: array of the actions
     */
    public Action[] initializeActions(){
        Action[] actions = new Action[9];
        actions[0] = new Action(0,0);
        actions[1] = new Action(0,1);
        actions[2] = new Action(0,-1);
        actions[3] = new Action(1,0);
        actions[4] = new Action(1,1);
        actions[5] = new Action(1,-1);
        actions[6] = new Action(-1,0);
        actions[7] = new Action(-1,1);
        actions[8] = new Action(-1,-1);
        return actions;
    }

    /**
     * Parameters:
     * char[][] track: track data struct
     * double[][][][][] model: model data struct
     *
     * initializeQ: finds all of the start indices
     *
     * Returns:
     * double[][][][][]: model data struct
     */
    public double[][][][][] initializeQ(char[][] track, double[][][][][]model){
        for (int i = 0; i < model.length; i++) {
            for (int j = 0; j < model[0].length; j++) {
                for (int a = 0; a < 11; a++) {
                    for (int b = 0; b < 11; b++) {
                        if(track[i][j] == 'F'){
                            for (int k = 0; k < 9; k++) {
                                model[i][j][a][b][k] = 0;
                            }

                        } else {
                            for (int k = 0; k < 9; k++) {
                                model[i][j][a][b][k] = Math.random();
                            }
                        }
                    }
                }

            }
        }
        return model;
    }

}
