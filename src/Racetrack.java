import java.io.File;
import java.util.Scanner;

public class Racetrack {
    private char[][] racetrack;
    private File file;

    public Racetrack(String filePath){
        this.file = new File(filePath);
        createTrack();
        printTrack();
    }

    private void createTrack() {
        try{
            Scanner s = new Scanner(file);
            int index = 0;
            while (s.hasNextLine()) {
                String currentLine = s.nextLine();
                if(currentLine.charAt(0)!='#'){
                    String[] firstLineSplit = currentLine.split(",");
                    racetrack = new char[Integer.parseInt(String.valueOf(firstLineSplit[0]))]
                            [Integer.parseInt(String.valueOf(firstLineSplit[1]))];
                } else {
                    racetrack[index] = currentLine.toCharArray();
                    index++;
                }
            }
        } catch (Exception e){

        }

    }
    private void printTrack() {
        for(int i = 0; i < racetrack.length; i++){
            for(int j = 0; j < racetrack[0].length; j++){
                System.out.print(racetrack[i][j]);
            }
            System.out.println();
        }
    }

}
