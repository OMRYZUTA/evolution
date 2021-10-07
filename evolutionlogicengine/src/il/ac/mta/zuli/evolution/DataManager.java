package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.engine.StrideData;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.exceptions.InvalidOperationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataManager {
    private final Map<String, User> users;
    private final List<TimeTable> timetables; // the index in the array will serve as the ID of the timetable

    public DataManager() throws IOException {
        timetables = new ArrayList<>();
        users = new HashMap<>();
    }

    //synchronized methods
    public synchronized void addTimetable(TimeTable timeTable, String userName) {
        timeTable.setUploadedBy(userName);
        timeTable.setID(timetables.size()); //the index in the list is the ID of the tt
        timetables.add(timeTable);
    }

    //add user only called after exist check and within a synchronized block (so the function is NON-sync)
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    //#region Algorithm Flow methods (these are single-User-Methods, non-synchronized)
    //TODO should these algo-flow methods be synchronized?
    public void startAlgorithmRunForUser(String userName,
                                         int timetableID,
                                         Map<String, Object> engineSettingsMap,
                                         Map<String, Object> endPredicatesMap,
                                         int generationStride) {
        TimeTable timetable = timetables.get(timetableID);

        if (timetable != null) {
            users.get(userName).startEvolutionAlgorithm(
                    timetable,
                    engineSettingsMap,
                    endPredicatesMap,
                    generationStride);
        } else {
            throw new InvalidOperationException("Timetable (ID " + timetableID + ") not found in dataManager");
        }
    }

    public String stopAlgorithmRunForUser(String userName, int ttID) {
        users.get(userName).stopEvolutionAlgorithm(ttID);
        return "Algorithm stopped";//TODO change message later
    }

    public String pauseAlgorithmRunForUser(String userName, int ttID) {
        users.get(userName).pauseEvolutionAlgorithm(ttID);
        return "Algorithm paused";//TODO change message later
    }

    public void resumeAlgorithmRunForUser(
            String userName,
            int timetableID,
            Map<String, Object> engineSettingsMap,
            Map<String, Object> endPredicatesMap,
            int generationStride) {

        TimeTable timetable = timetables.get(timetableID);

        users.get(userName).resumeEvolutionAlgorithm(
                timetableID,
                engineSettingsMap,
                endPredicatesMap,
                generationStride);
    }
    //#endregion

    //#region getters
    public GenerationProgressDTO getProgressData(String userName, int ttID) {
        return users.get(userName).getProgressData(ttID);
    }

    //we might nor be using the stride updates (depends on the bonus we implement)
    public StrideData getStrideData(String userName, int ttID) {
        return users.get(userName).getStrideData(ttID);
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
            TimetableSolution bestSolution = usersSolvingProblem.get(0).getBestSolution(ttID);
            userWithBestSolution = usersSolvingProblem.get(0);

            for (User user : usersSolvingProblem) {
                TimetableSolution currUserSolution = user.getBestSolution(ttID);

                if (currUserSolution.getFitnessScore() > bestSolution.getFitnessScore()) {
                    bestSolution = currUserSolution;
                    userWithBestSolution = user;
                }
            }
        }

        return userWithBestSolution;
    }

    //return value might be null
    public TimeTable getTimetable(int ttID) {

        if (doesTimetableExist(ttID)) {
            return timetables.get(ttID);
        } else {
            throw new RuntimeException("couldn't find timetable");
        }
    }

    public List<TimetableSummaryDTO> getTimetableSummaries() {
        List<TimetableSummaryDTO> newList = new ArrayList<>();

        if (!timetables.isEmpty()) {
            for (TimeTable timetable : timetables) {
                int ttID = timetable.getID();
                int numOfUsers = getNumOfUsersSolvingProblem(ttID);
                double bestScore = getBestScoreForProblem(ttID);

                newList.add(new TimetableSummaryDTO(timetable, bestScore, numOfUsers));
            }
        }

        return newList;
    }

    //return value might be NULL
    public TimeTableEngine getTimetableEngine(String userName, int ttID) {
        //TODO figure out what will I receive if there are no users or no ttID for that user?
        return users.get(userName).getTimetableEngine(ttID);
    }

    public AlgorithmConfigDTO getAlgoConfig(String userName, int ttID) {
        TimeTableEngine ttEngine = getTimetableEngine(userName, ttID);

        if (ttEngine != null) {
            return new AlgorithmConfigDTO(
                    ttID,
                    ttEngine.getGenerationsStride(),
                    ttEngine.getEndPredicates(),
                    ttEngine.getEngineSettings());
        }

        return null;
    }

    //This function returns currGeneration and Best-Score-So-Far (and NOT bestScoreInGeneration like getProgress() does)
    public List<OtherUserSolutionDTO> getOtherSolutionsInfo(int ttID) {
        List<OtherUserSolutionDTO> otherSolutionsInfo = new ArrayList<>();

        if (isSomeoneSolvingProblem(ttID)) {
            List<User> usersSolving = getUsersSolvingProblem(ttID);

            for (User user : usersSolving) {
                TimeTableEngine userTTEngine = user.getTimetableEngine(ttID);
                otherSolutionsInfo.add(
                        new OtherUserSolutionDTO(
                                user.getUsername(),
                                userTTEngine.getBestScore(),
                                userTTEngine.getCurrGenerationNum())
                );
            }
        }

        return otherSolutionsInfo;
    }

    private double getBestScoreForProblem(int ttID) {
        User user = getUserWithBestSolutionOfProblem(ttID);
        double score = 0;

        if (user != null) {
            score = user.getBestScore(ttID);
        }

        return score;
    }

    //return value might be NULL
    public TimetableSolutionDTO getBestSolutionOfProblem(int ttID) {
        User userWithBestSolution = getUserWithBestSolutionOfProblem(ttID);
        TimetableSolutionDTO bestSolution = null;

        if (userWithBestSolution != null) {
            bestSolution = new TimetableSolutionDTO(userWithBestSolution.getBestSolution(ttID));
        }

        return bestSolution;
    }
    //#endregion

    public boolean doesUserExist(String username) {
        //User has a unique-name field so that's what we use in equals()
        if (!users.isEmpty()) {
            return users.containsKey(username);
        } else {
            return false; //map is empty
        }
    }

    public boolean doesTimetableExist(int ttID) {
        return timetables.size() > ttID;
    }

    //return true if list isn't empty
    private boolean isSomeoneSolvingProblem(int ttID) {
        return !getUsersSolvingProblem(ttID).isEmpty();
    }

    //when do we remove users? delete later
    public synchronized void removeUser(String username) {
        users.remove(username);
    }
}
