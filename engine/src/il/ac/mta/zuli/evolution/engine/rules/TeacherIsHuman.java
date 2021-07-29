package il.ac.mta.zuli.evolution.engine.rules;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Collection;

public class TeacherIsHuman extends Rule {
    public TeacherIsHuman(String ruleType) {
        super(ruleType);
    }

    @Override
    public int fitness(Collection<Quintet> solution) {
        int collision = 0;
        //TODO create KEY as String from Day, Hour, Teacher and add to Set, each collision, advance a counter
        return collision; //or other grade?
    }
}
