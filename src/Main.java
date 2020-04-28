import java.sql.Time;

public class Main {

    public static void main(String[] args) {

        // Value Iteration
        Racetrack r = new Racetrack("/Users/ethanmiller/Desktop/CSCI446/Project4/Artificial-Intelligence-Reinforcement-Learning-Racetrack/src/inputs/L-track.txt");
        ValueIteration vi = new ValueIteration();
        vi.loadTrack(r.getRacetrack());
        vi.startLearningValueIteration(1000, 0);
        System.out.println("Done Learning");
        Timetrial tt = new Timetrial(vi.getModel(), r.getRacetrack(), 0);
        tt.runTrack(20, 0);
        //vi.runTimeTrial();


        // QLearning
//        Racetrack r1 = new Racetrack("/Users/Alex/Documents/School/AI/Artificial-Intelligence-Reinforcement-Learning-Racetrack/src/inputs/L-track.txt");
//        QLearning ql = new QLearning();
//        ql.loadTrack(r1.getRacetrack());
//        ql.startLearning(100000, 0);
//        Timetrial tt = new Timetrial(ql.getModel(), r1.getRacetrack(), 0);
//        tt.runTrack(20, 0);
    }
}
