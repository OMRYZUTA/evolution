package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private String username;
    private final Map<Integer, TimeTableEngine> userAlgorithmRuns; //key of Map is the timetableID (unique per User)
    private TimeTableEngine currDisplayedRun; //currently, displayed on screen3,
    // multiple descriptors can run in back, but only one is displayed on the front?

    public User(String username) throws IOException {
        setUsername(username);
        userAlgorithmRuns = new HashMap<>();
    }

    //if user picks a certain timetable in screen2, we'll add it the user's map,
    // and later assign it with an engine accordingly

    //adding a TimeTable to the app will add it to the common collection and add the reference to the user who uploaded it
    //adding an engine only happens at the user level

    public void setUsername(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            throw new IOException("username cannot be  null");
        } else if (username.trim().isEmpty()) {
            throw new IOException("username cannot be empty string");
        }
        //normalize the username value
        this.username = username.trim();
    }

    public String getUsername() {
        return username;
    }

    public void addNewRun(Descriptor descriptor) {
        TimeTableEngine newRun = new TimeTableEngine(descriptor);
        //TODO implement
    }

    public boolean isSolvingProblem(int ttID) {
        //if currently solving or previously solved a certain timetable
        return userAlgorithmRuns.containsKey(ttID);
    }

    public double getBestScore(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getBestScore();
        }

        return 0;
    }

    public TimeTableSolution getBestSolution(int ttID) {
        if (isSolvingProblem(ttID)) {
            return userAlgorithmRuns.get(ttID).getBestSolution();
        }

        return null;
    }

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
