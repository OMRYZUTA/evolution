package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.timetable.TimetableSummary;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private final Set<User> usersSet;
    // the index in the array will serve as the ID of the timetable
    private final List<TimeTable> timetables; //for each valid xml uploaded, add a timetable to the collection

    public DataManager() throws IOException {
        timetables = new ArrayList<>();
        usersSet = new HashSet<>();
        usersSet.add(new User("Gary12432")); //TODO delete later, also delete all throws IOExceptions
        usersSet.add(new User("Cupcake12321"));
    }

    public List<TimeTable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }

    public synchronized void addUser(User user) { //TODO throw exception if user already exists? (add user only called after isexist check)
        usersSet.add(user);
    }

    //when do we remove users? delete later
    public synchronized void removeUser(User user) {
        usersSet.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public List<String> getUserNames() {
        return usersSet.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public boolean isUserExists(User user) {
        //User has a unique-name field so that's what we use in equals()
        return usersSet.contains(user);
    }

    public List<TimetableSummary> getTimetableSummaries() {
        List<TimetableSummary> newList = new ArrayList<>();

        for (TimeTable t : timetables) {
            newList.add(new TimetableSummary(t));
            //TODO set maxFitness and how many users are solving
        }

        return newList;
    }
}
