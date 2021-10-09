package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.io.IOException;
import java.util.*;

public class User {
    private String username;
    private final Map<Integer, TimeTableEngine> userAlgorithmRuns; //key of Map is the timetableID (unique per User)
    private TimeTableEngine currDisplayedRun; //currently, displayed on screen3,
    // multiple descriptors can run in back, but only one is displayed on the front?

    public User(String username) throws IOException {
        setUsername(username);
        userAlgorithmRuns = new HashMap<>();
    }

    //#region Algo-Flow Methods
    public void startEvolutionAlgorithm(TimeTable timetable,
                                        Map<String, Object> engineSettingsMap,
                                        Map<String, Object> endPredicatesMap,
                                        int generationStride) {
        int timetableID = timetable.getID();

        if (userAlgorithmRuns.containsKey(timetableID)) {
            throw new RuntimeException("User " + username + " already solved this problem");
            //TODO if the run already completed for this ttID maybe allow another run, overriding the previous run and solution
        } else {
            TimeTableEngine timetableEngine = new TimeTableEngine(
                    timetable,
                    engineSettingsMap,
                    endPredicatesMap,
                    generationStride);

            userAlgorithmRuns.put(timetableID, timetableEngine);
            currDisplayedRun = timetableEngine;

            timetableEngine.startEvolutionAlgorithm();
        }
    }

    public void resumeEvolutionAlgorithm(int ttID,
                                         Map<String, Object> engineSettingsMap,
                                         Map<String, Object> endPredicatesMap,
                                         int generationStride) {

        if (userAlgorithmRuns.containsKey(ttID)) {
            //in timetableEngine.resumeEvolutionAlgorithm() we'll check if it's indeed paused
            TimeTableEngine timetableEngine = userAlgorithmRuns.get(ttID);
            timetableEngine.setNewAlgorithmConfiguration(engineSettingsMap, endPredicatesMap, generationStride);
            timetableEngine.resumeEvolutionAlgorithm();
        } else {
            throw new InvalidOperationException("The user is not solving timetable " + ttID);
        }
    }

    public void pauseEvolutionAlgorithm(int ttID) {
        if (userAlgorithmRuns.containsKey(ttID)) {
            TimeTableEngine timetableEngine = userAlgorithmRuns.get(ttID);
            timetableEngine.pauseEvolutionAlgorithm();
        }
    }

    public void stopEvolutionAlgorithm(int ttID) {
        if (userAlgorithmRuns.containsKey(ttID)) {
            TimeTableEngine timetableEngine = userAlgorithmRuns.get(ttID);
            timetableEngine.stopEvolutionAlgorithm();
        }
    }
    //#endregion

    public void setUsername(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            throw new IOException("username cannot be  null");
        } else if (username.trim().isEmpty()) {
            throw new IOException("username cannot be empty string");
        }
        //normalize the username value
        this.username = username.trim();
    }

    public boolean isSolvingProblem(int ttID) {
        //if currently solving or previously solved a certain timetable
        return userAlgorithmRuns.containsKey(ttID);
    }

    //#region getters
    public String getUsername() {
        return username;
    }

    public TimeTableEngine getTimetableEngine(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID);
        }

        return null;
    }

    public GenerationProgressDTO getProgressData(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getProgressData();
        }

        return null;
    }

    public double getBestScore(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getBestScore();
        }

        return 0;
    }

    public TimetableSolution getBestSolution(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getBestSolution();
        }

        return null;
    }

    public List<StrideDataDTO> getStrideData(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getStrideData();
        }

        return new ArrayList<>();
    }
    //#endregion

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User that = (User) obj;
        return username.equals(that.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
