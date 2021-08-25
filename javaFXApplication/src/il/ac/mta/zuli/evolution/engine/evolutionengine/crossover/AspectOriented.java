package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.*;
import java.util.stream.Collectors;

public class AspectOriented<S extends Solution> extends Crossover<S> {
    private Orientation orientation;

    public AspectOriented(int cuttingPoints, Orientation orientation, TimeTable timeTable) {
        super(cuttingPoints, timeTable);
        this.orientation = orientation;
    }


    @Override
    public List<S> crossover(List<S> selectedParents) {
        //for each solution, in selected parents, organize as map of teacher per dh matrix  map<teacher, list <list<q>>>
        List<Map<Teacher, List<List<Quintet>>>> parentsAsTeacherMatrix = organizeSolutions(selectedParents);
        Map<Teacher, List<List<Quintet>>> parent1;
        Map<Teacher, List<List<Quintet>>> parent2;
        List<List<Quintet>> child1;
        List<List<Quintet>> child2;
        List<TimeTableSolution> newGeneration = new ArrayList<>();

        while (parentsAsTeacherMatrix.size() >= 2) {
            parent1 = randomlySelectParent(parentsAsTeacherMatrix);
            removeParentFromPoolOfParents(parentsAsTeacherMatrix, parent1);

            parent2 = randomlySelectParent(parentsAsTeacherMatrix);
            removeParentFromPoolOfParents(parentsAsTeacherMatrix, parent2);

            List<List<List<Quintet>>> twoChildrenPerTeacher;
            child1 = createEmptyDHMatrix();
            child2 = createEmptyDHMatrix();

            List<Teacher> teachers = new ArrayList<>(timeTable.getTeachers().values());
            for (Teacher teacher : teachers) {
                twoChildrenPerTeacher = crossoverBetween2Parents(parent1.get(teacher), parent2.get(teacher));
                fillChildMatrix(twoChildrenPerTeacher.get(0), child1);
                fillChildMatrix(twoChildrenPerTeacher.get(1), child2);
            }
            //flatten both matrixes to return 2 solutions
            List<TimeTableSolution> twoSolutionChildren =convertMatrixToSolutions( child1, child2);
            newGeneration.addAll(twoSolutionChildren);
        }

        return (List<S>)newGeneration;
    }

    private void fillChildMatrix(List<List<Quintet>> source, List<List<Quintet>> destination) {
        //TODO check if we need to avoid conflicts in time slots
        for (int i = 0; i < days * hours; i++) {
            if (destination.get(i) == null) {
                destination.set(i, source.get(i));
            }
            //adding source list<Quintet> to destination list<Quintet>
            (destination.get(i)).addAll(source.get(i));
        }

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

    private Map<Teacher, List<List<Quintet>>> randomlySelectParent(List<Map<Teacher, List<List<Quintet>>>> selectedSolutionsAsMatrix) {
        int randomIndex = new Random().nextInt(selectedSolutionsAsMatrix.size());
        return selectedSolutionsAsMatrix.get(randomIndex);
    }

    private void removeParentFromPoolOfParents(List<Map<Teacher, List<List<Quintet>>>> selectedSolutionsAsMatrix,
                                               Map<Teacher, List<List<Quintet>>> itemToRemove) {
        Iterator<Map<Teacher, List<List<Quintet>>>> itr = selectedSolutionsAsMatrix.iterator();

        while (itr.hasNext()) {
            Map<Teacher, List<List<Quintet>>> inner = itr.next();
            //Map, List, Quintet and teacher implement equals()
            if (inner.equals(itemToRemove)) {
                itr.remove();
                break;
            }
        }
    }


}
