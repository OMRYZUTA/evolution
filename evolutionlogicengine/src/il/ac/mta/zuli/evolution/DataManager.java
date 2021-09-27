package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private final Map<String, User> users;
    private final List<TimeTable> timetables; // the index in the array will serve as the ID of the timetable

    public DataManager() throws IOException {
        timetables = new ArrayList<>();
        users = new HashMap<>();
        //TODO delete later, also delete all throws IOExceptions
        users.put("Gary12432", new User("Gary12432"));
        users.put("Cupcake12321", new User("Cupcake12321"));
    }

    public synchronized void addTimetable(TimeTable timeTable, String userName) {
        timeTable.setUploadedBy(userName);
        timeTable.setID(timetables.size()); //the index in the list is the ID of the tt
        timetables.add(timeTable);
    }

    public synchronized void addUser(User user) {
        //TODO throw exception if user already exists? (add user only called after exist check)
        users.put(user.getUsername(), user);
    }

    public synchronized void addAlgoRunToUser(String userName,
                                              int timetableID,
                                              Map<String, Object> engineSettingsMap,
                                              List<Map<String, Object>> endPredicatesMap,
                                              Object generationStride) {

        TimeTableEngine timetableEngine = new TimeTableEngine(
                timetables.get(timetableID),
                engineSettingsMap,
                endPredicatesMap,
                generationStride);

        users.get(userName).runTimetableEngine(timetableEngine);
    }

    //return value might be an empty list
    public List<String> getUserNames() {
        if (!users.isEmpty()) {
            return users.values()
                    .stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public boolean doesUserExist(String username) {
        //User has a unique-name field so that's what we use in equals()
        if (!users.isEmpty()) {
            return users.containsKey(username);
        } else {
            return false; //map is empty
        }
    }

    //return true if list isn't empty
    private boolean isSomeoneSolvingProblem(int ttID) {
        return !getUsersSolvingProblem(ttID).isEmpty();
    }

    //return value might be an empty list
    public List<User> getUsersSolvingProblem(int ttID) {
        if (!users.isEmpty()) {
            return users.values().stream()
                    .filter(user -> user.isSolvingProblem(ttID))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public int getNumOfUsersSolvingProblem(int ttID) {
        if (isSomeoneSolvingProblem(ttID)) {
            return getUsersSolvingProblem(ttID).size();
        } else {
            return 0;
        }
    }

    //return value might be NULL if no users are trying to solve
    public User getUserWithBestSolutionOfProblem(int ttID) {
        User userWithBestSolution = null;

        if (isSomeoneSolvingProblem(ttID)) {
            List<User> usersSolvingProblem = getUsersSolvingProblem(ttID);
            TimeTableSolution bestSolution = usersSolvingProblem.get(0).getBestSolution(ttID);

            for (User user : usersSolvingProblem) {
                TimeTableSolution currUserSolution = user.getBestSolution(ttID);

                if (currUserSolution.getFitnessScore() > bestSolution.getFitnessScore()) {
                    bestSolution = currUserSolution;
                    userWithBestSolution = user;
                }
            }
        }

        return userWithBestSolution;
    }

    //return value might be NULL
    public TimeTableSolution getBestSolutionOfProblem(int ttID) {
        User userWithBestSolution = getUserWithBestSolutionOfProblem(ttID);
        TimeTableSolution bestSolution = null;

        if (userWithBestSolution != null) {
            bestSolution = userWithBestSolution.getBestSolution(ttID);
        }

        return bestSolution;
    }

    private double getBestScoreForProblem(int ttID) {
        User user = getUserWithBestSolutionOfProblem(ttID);
        double score = 0;

        if (user != null) {
            score = user.getBestScore(ttID);
        }

        return score;
    }

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    private List<TimeTable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }

    public boolean doesTimetableExist(int ttID) {
        return timetables.size() > ttID;
    }

    //return value might be null
    public TimeTable getTimetable(int ttID) {

        if (doesTimetableExist(ttID)) {
            return timetables.get(ttID);
        } else {
            throw new RuntimeException("couldn't find timetable");
        }
    }

    public List<TimetableSummary> getTimetableSummaries() {
        List<TimetableSummary> newList = new ArrayList<>();

        if (!timetables.isEmpty()) {
            for (TimeTable tt : timetables) {
                int ttID = tt.getID();
                int numOfUsers = getNumOfUsersSolvingProblem(ttID);
                double bestScore = getBestScoreForProblem(ttID);

                newList.add(new TimetableSummary(tt, bestScore, numOfUsers));
            }
        }

        return newList;
    }

    //when do we remove users? delete later
    public synchronized void removeUser(String username) {
        users.remove(username);
    }
}
