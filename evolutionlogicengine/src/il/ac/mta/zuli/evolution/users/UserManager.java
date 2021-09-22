package il.ac.mta.zuli.evolution.users;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {
    private final Set<User> usersSet;

    public UserManager() throws IOException {
        usersSet = new HashSet<>();
        usersSet.add(new User("Gary12432")); //TODO delete later, also delete all throws IOExceptions
        usersSet.add(new User("Cupcake12321"));
    }

    public synchronized void addUser(User user) { //TODO throw exception if user already exists? (add user only called after isexist check)
        usersSet.add(user);
    }

    public synchronized void removeUser(User user) {
        usersSet.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(User user) {
        return usersSet.contains(user);
    }

    public List<String> getUserNames() {
        //gets only the one field from every user
        return usersSet.stream().map(User::getUsername).collect(Collectors.toList());
    }
}