package il.ac.mta.zuli.evolution.users;

import il.ac.mta.zuli.evolution.engine.Descriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User {
    private String username;
    //TODO - add in readme: the user configs an engine for each problem he decides to run (and not for several problems together)
    //if user picks a certain timetable in screen2, we'll add it the userDescriptors list, and later assign it with an engine accordingly
    //key of Map is the timetableID (unique per suer)
    private final Map<Integer, Descriptor> userDescriptors; //descriptor is the pair of (TimeTable, EngineSettings)
    private Descriptor currDisplayedDescriptor; //the descriptor of which its state is displayed on screen3
    //multiple descriptors can run in back, but only one is displayed om the front?

    // TODO - think about it: maybe instead of holding a Map<Integer,Descriptor> the user should have a
    //  Map<Integer, TimeTableEngine>
    //  the user will instantiate a timetableEngine which will serve as a wrapper for Descriptor


    public User(String username) throws IOException {
        setUsername(username);
        userDescriptors = new ArrayList<>();
    }

    //adding a TimeTable to the app will add it to the common collection and add the reference to the user who uploaded it
    //adding an engine only happens at the user level


    public void setUsername(String username) throws IOException{
        if (username == null || username.trim().isEmpty()) {
            throw new IOException("username cannot be  null");
        }else if(username.trim().isEmpty()){
            throw new IOException("username cannot be empty string");
        }
        //normalize the username value
        this.username = username.trim();
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

    public String getUsername() {
        return username;
    }

    public List<Descriptor> getUserDescriptors() {
        return userDescriptors;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
