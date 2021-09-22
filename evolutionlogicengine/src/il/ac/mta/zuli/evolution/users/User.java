package il.ac.mta.zuli.evolution.users;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private  String username;
    private final List<EngineSettings> userEngines;
    private final List<Descriptor> userDescriptors; //descriptor is the pair of (TimeTable, EngineSettings)

    //a collection of pairs (TimeTable, EngineSettings)
    // only we want to be able initially to create a descriptor only the Timetable without the enginesettings


    public User(String username) throws IOException {
        setUsername(username);
        userEngines = new ArrayList<>();
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
