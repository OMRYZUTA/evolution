package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private final Map<String, User> users;
    // the index in the array will serve as the ID of the timetable
    private final List<TimeTable> timetables; //for each valid xml uploaded, add a timetable to the collection

    public DataManager() throws IOException {
        timetables = new ArrayList<>();
        users = new HashMap<>();
        //TODO delete later, also delete all throws IOExceptions
        users.put("Gary12432", new User("Gary12432"));
        users.put("Cupcake12321", new User("Cupcake12321"));
    }

    public synchronized void addTimetable(TimeTable timeTable, String userName) {
        timeTable.setUploadedBy(users.get(userName));
        timeTable.setID(timetables.size()); //the index in the list is the ID of the tt
        timetables.add(timeTable);
    }

    public synchronized void addUser(User user) { //TODO throw exception if user already exists? (add user only called after exist check)
        users.put(user.getUsername(), user);
    }

    //when do we remove users? delete later
    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    public List<String> getUserNames() {
        return users.values()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public User getUser(String name) {
        return users.get(name);
    }

    public boolean doesUserExist(String username) {
        //User has a unique-name field so that's what we use in equals()
        return users.containsKey(username);
    }

    public int getNumOfUsersSolvingProblem(int ttID) {
        return getUsersSolvingProblem(ttID).size();
    }

    public List<User> getUsersSolvingProblem(int ttID) {
        return users.values().stream()
                .filter(user -> user.isSolvingProblem(ttID))
                .collect(Collectors.toList());
    }

    public User getUserWithBestSolutionOfProblem(int ttID) {
        List<User> usersSolvingProblem = getUsersSolvingProblem(ttID);
        //todo consider the case of nobody is trying to solve this problem
        TimeTableSolution bestSolution = usersSolvingProblem.get(0).getBestSolution(ttID);
        User userWithBestSolution = null;

        for (User user : usersSolvingProblem) {
            TimeTableSolution currUserSolution = user.getBestSolution(ttID);

            if (currUserSolution.getFitnessScore() > bestSolution.getFitnessScore()) {
                bestSolution = currUserSolution;
                userWithBestSolution = user;
            }
        }

        return userWithBestSolution;
    }

    public TimeTableSolution getBestSolutionOfProblem(int ttID) {
        return getUserWithBestSolutionOfProblem(ttID).getBestSolution(ttID);
    }

    public List<TimeTable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }

    public List<TimetableSummary> getTimetableSummaries() {
        List<TimetableSummary> newList = new ArrayList<>();
        List<User> solvingUsers;
        double bestScore;
        int numOfUsers;
        for (TimeTable tt : timetables) {
            solvingUsers= getUsersSolvingProblem(tt.getID());
            bestScore = getBestScoreForTimeTable(tt.getID(),solvingUsers);
            numOfUsers = solvingUsers.size();
            TimetableSummary currTTSummary = new TimetableSummary(tt, bestScore, numOfUsers);
            newList.add(currTTSummary);
        }

        return newList;
    }

    private double getBestScoreForTimeTable(int id, List<User> solvingUsers) {
        double score =0;
        if(solvingUsers.size()>0){
            score = getBestSolutionOfProblem(id).getFitnessScore();
        }
        return score;
    }
}
