package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AspectOriented<S extends Solution> extends Crossover<S> {
    private Orientation orientation;

    public AspectOriented(int cuttingPoints, Orientation orientation, TimeTable timeTable) {
        super(cuttingPoints, timeTable);
        this.orientation = orientation;
    }


    @Override
    public List<S> crossover(List<S> selectedParents) {
        //TODO implement
        List<Map<Teacher, List<List<Quintet>>>> parentsAsTeacherMatrix = organizeSolutions(selectedParents);

        //for each solution, in selected parents, organize as map of teacher per dh matrix  map<teacher, list <list<q>>>
        //while there are more than 1 solution in general collection
        //randomly select 2 parents
        //crossover between 2 parents
        //for each teacher, crossover between parent 1 and parent 2. so we have 2 new children per teacher
        //combine to create 2 solutions (without conflicts)
        //flatten both matrixes to return 2 solutions

        //return newGeneration
        return null;
    }

    private List<Map<Teacher, List<List<Quintet>>>> organizeSolutions(List<S> selectedParents) {
        List<Map<Teacher, List<List<Quintet>>>> parentsAsTeacherMatrix = new ArrayList();

        for (S solution : selectedParents) {
            if (!(solution instanceof TimeTableSolution)) {
                throw new RuntimeException("solution must be TimeTableSolution");
            }

            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
            //grouping solution quintets by teacher
            Map<Teacher, List<Quintet>> teachersSubSolution = timeTableSolution.getSolutionQuintets()
                    .stream()
                    .collect(Collectors.groupingBy(Quintet::getTeacher));
            //for each teacher from list of quintets to matrix
            Map<Teacher, List<List<Quintet>>> tempMap = new HashMap();
            for (Map.Entry<Teacher, List<Quintet>> entry : teachersSubSolution.entrySet()) {

                tempMap.put(entry.getKey(), convertQuintetListToMatrix(entry.getValue()));
            }
            parentsAsTeacherMatrix.add(tempMap);
        }
        return parentsAsTeacherMatrix;
    }
}
