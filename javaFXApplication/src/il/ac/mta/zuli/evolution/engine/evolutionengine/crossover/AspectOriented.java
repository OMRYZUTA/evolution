package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.*;
import java.util.stream.Collectors;

public class AspectOriented<S extends Solution> extends Crossover<S> {
    private final Orientation orientation;

    public AspectOriented(int cuttingPoints, Orientation orientation, TimeTable timeTable) {
        super(cuttingPoints, timeTable);
        this.orientation = orientation;
    }

    //This implementation of the interface method mainly envelops the class generic method
    @Override
    public List<S> crossover(List<S> selectedParents) {
        if (selectedParents.size() == 0) {
            throw new EmptyCollectionException("Parent-generation is empty");
        }
        if (selectedParents.size() == 1) {
            return selectedParents;
        }

        randomlyGenerateCuttingPoints();

        return aspectOrientedCrossover(selectedParents);
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> List<S> aspectOrientedCrossover(List<S> selectedParents) {
        //organize every solution in selectedParents as map of dh-matrix-per-aspect (map<aspect, list <list<q>>>)
        List<Map<T, List<List<Quintet>>>> parentsAsAspectMatrix = organizeSolutionsPerAspect(selectedParents);

        Map<T, List<List<Quintet>>> parent1;
        Map<T, List<List<Quintet>>> parent2;
        List<List<Quintet>> child1;
        List<List<Quintet>> child2;
        List<TimeTableSolution> newGeneration = new ArrayList<>();
        List<T> teachersOrClasses = getRelevantList();

        while (parentsAsAspectMatrix.size() >= 2) {
            parent1 = randomlySelectParent(parentsAsAspectMatrix);
            removeParentFromPoolOfParents(parentsAsAspectMatrix, parent1);

            parent2 = randomlySelectParent(parentsAsAspectMatrix);
            removeParentFromPoolOfParents(parentsAsAspectMatrix, parent2);

            List<List<List<Quintet>>> twoChildrenPerTeacher = new ArrayList<>(); //2 child matrix
            child1 = createEmptyDHMatrix();
            child2 = createEmptyDHMatrix();

            for (T teacherOrClass : teachersOrClasses) {
                System.out.println("in aspectOrientedCrossover(), start of for loop");
                if ((parent1.get(teacherOrClass) != null) && (parent2.get(teacherOrClass) != null)) {
                    twoChildrenPerTeacher = crossoverBetween2Parents(
                            parent1.get(teacherOrClass),
                            parent2.get(teacherOrClass));
                } else if (parent1.get(teacherOrClass) == null && parent2.get(teacherOrClass) != null) {
                    twoChildrenPerTeacher.set(0, parent2.get(teacherOrClass));
                    twoChildrenPerTeacher.set(1, parent2.get(teacherOrClass));

                } else if (parent1.get(teacherOrClass) != null && parent2.get(teacherOrClass) == null) {
                    twoChildrenPerTeacher.set(0, parent1.get(teacherOrClass));
                    twoChildrenPerTeacher.set(1, parent1.get(teacherOrClass));
                } else {
                    //nothing to add to child-solutions
                    continue;
                }

                fillChildMatrix(twoChildrenPerTeacher.get(0), child1);
                fillChildMatrix(twoChildrenPerTeacher.get(1), child2);
            }

            List<TimeTableSolution> twoSolutionChildren = convertMatrixToSolutions(child1, child2);
            newGeneration.addAll(twoSolutionChildren);
        }

        // if there is one parent left, need to add it to new generations
        if (parentsAsAspectMatrix.size() == 1) {
            Map<T, List<List<Quintet>>> lastParent = parentsAsAspectMatrix.get(0);
            List<List<Quintet>> onlyChild = new ArrayList<>();

            for (T teacherOrClass : teachersOrClasses) {
                fillChildMatrix(lastParent.get(teacherOrClass), onlyChild);
            }

            newGeneration.add(convertSingleMatrixToSolution(onlyChild));
        }

        return (List<S>) newGeneration;
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> List<T> getRelevantList() {
        List<T> teachersOrClasses;

        if (orientation == Orientation.TEACHER) {
            List<Teacher> teachers = new ArrayList<>(timeTable.getTeachers().values());
            teachersOrClasses = (List<T>) teachers;
        } else {
            List<SchoolClass> schoolClasses = new ArrayList<>(timeTable.getSchoolClasses().values());
            teachersOrClasses = (List<T>) schoolClasses;
        }

        return teachersOrClasses;
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> List<Map<T, List<List<Quintet>>>> organizeSolutionsPerAspect(List<S> selectedParents) {
        List<Map<T, List<List<Quintet>>>> parentsAsAspectMatrix = new ArrayList();

        for (S solution : selectedParents) {

            if (!(solution instanceof TimeTableSolution)) {
                throw new RuntimeException("solution must be TimeTableSolution");
            }
            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

            //grouping solution-quintets by orientation
            Map<T, List<Quintet>> solutionQuintetsGroupedByAspect;

            if (orientation == Orientation.TEACHER) {
                //grouped by TEACHER
                Map<Teacher, List<Quintet>> solutionQuintetsGroupedByTeacher = timeTableSolution.getSolutionQuintets()
                        .stream()
                        .collect(Collectors.groupingBy(Quintet::getTeacher));

                solutionQuintetsGroupedByAspect = (Map<T, List<Quintet>>) solutionQuintetsGroupedByTeacher;
            } else {
                //grouped by CLASS
                Map<SchoolClass, List<Quintet>> solutionQuintetsGroupedByClass = timeTableSolution.getSolutionQuintets()
                        .stream()
                        .collect(Collectors.groupingBy(Quintet::getSchoolClass));
                solutionQuintetsGroupedByAspect = (Map<T, List<Quintet>>) solutionQuintetsGroupedByClass;
            }

            //for each teacher/class in solution: convert List<Quintet> to DH-Matrix (List<List<Quintet>>)
            Map<T, List<List<Quintet>>> solutionMatrixPerAspect = new HashMap();

            for (Map.Entry<T, List<Quintet>> entry : solutionQuintetsGroupedByAspect.entrySet()) {
                solutionMatrixPerAspect.put(entry.getKey(), convertQuintetListToMatrix(entry.getValue()));
            }
            //add single-converted-solution to the collection of parents
            parentsAsAspectMatrix.add(solutionMatrixPerAspect);
        }

        return parentsAsAspectMatrix;
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> Map<T, List<List<Quintet>>> randomlySelectParent(
            List<Map<T, List<List<Quintet>>>> selectedSolutionsAsMatrix) {

        int randomIndex = new Random().nextInt(selectedSolutionsAsMatrix.size());

        return selectedSolutionsAsMatrix.get(randomIndex);
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> void removeParentFromPoolOfParents(
            List<Map<T, List<List<Quintet>>>> selectedSolutionsAsMatrix, Map<T, List<List<Quintet>>> itemToRemove) {

        Iterator<Map<T, List<List<Quintet>>>> itr = selectedSolutionsAsMatrix.iterator();

        while (itr.hasNext()) {
            Map<T, List<List<Quintet>>> inner = itr.next();
            //Map, List, Quintet and teacher implement equals()
            if (inner.equals(itemToRemove)) {
                itr.remove();
                break;
            }
        }
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
}
