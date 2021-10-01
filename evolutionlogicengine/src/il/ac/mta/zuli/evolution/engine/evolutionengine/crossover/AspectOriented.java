package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Double;
import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class AspectOriented<S extends Solution> extends Crossover<S> {
    private final Orientation orientation; //TEACHER or CLASS

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

    //In the following generic methods: T is either Teacher or SchoolClass
    private <T> List<S> aspectOrientedCrossover(List<S> selectedParents) {
        //organize every solution in selectedParents as a map of dh-matrix-per-aspect (map<aspect, list <list<q>>>)
        List<Map<T, List<List<Quintet>>>> parentsAsAspectMatrix = organizeSolutionsPerAspect(selectedParents);

        List<Double> newGeneration = new ArrayList<>();
        Map<T, List<List<Quintet>>> parent1;
        Map<T, List<List<Quintet>>> parent2;
        List<List<Quintet>> child1ToUnite;
        List<List<Quintet>> child2ToUnite;
        List<T> teachersOrClasses = getRelevantList(); //list of timetable teachers or classes

        while (parentsAsAspectMatrix.size() >= 2) {
            parent1 = randomlySelectParent(parentsAsAspectMatrix);
            removeParentFromPoolOfParents(parentsAsAspectMatrix, parent1);

            parent2 = randomlySelectParent(parentsAsAspectMatrix);
            removeParentFromPoolOfParents(parentsAsAspectMatrix, parent2);

            //crossover between the 2 randomly selected parents
            //begin with crossover-per-aspect, after which, every teacher/class will have 2 children
            //we'll unite half of the children to one mega-child and the other half to another, so that every 2 parent have 2 children
            List<List<List<Quintet>>> twoChildrenPerAspect = new ArrayList<>(Arrays.asList(createEmptyDHMatrix(), createEmptyDHMatrix())); //2 child matrix
            child1ToUnite = createEmptyDHMatrix();
            child2ToUnite = createEmptyDHMatrix();

            for (T teacherOrClass : teachersOrClasses) {
                List<List<Quintet>> matrix1 = parent1.get(teacherOrClass);
                List<List<Quintet>> matrix2 = parent2.get(teacherOrClass);

                if ((matrix1 != null) || (matrix2 != null)) {
                    subSolutionCrossover(twoChildrenPerAspect, matrix1, matrix2);
                    fillChildMatrix(twoChildrenPerAspect.get(0), child1ToUnite);
                    fillChildMatrix(twoChildrenPerAspect.get(1), child2ToUnite);
                }
            }//end of for loop

            newGeneration.addAll(convertMatrixToSolutions(child1ToUnite, child2ToUnite));
        }

        // if there is one parent left, need to add it to new generations
        if (parentsAsAspectMatrix.size() == 1) {
            List<List<Quintet>> onlyChild = handleLastParent(parentsAsAspectMatrix, teachersOrClasses);

            newGeneration.add(convertSingleMatrixToSolution(onlyChild));
        }

        return (List<S>) newGeneration;
    }

    @NotNull
    private <T> List<List<Quintet>> handleLastParent(List<Map<T, List<List<Quintet>>>> parentsAsAspectMatrix, List<T> teachersOrClasses) {
        Map<T, List<List<Quintet>>> lastParent = parentsAsAspectMatrix.get(0);
        List<List<Quintet>> onlyChild = createEmptyDHMatrix();

        for (T teacherOrClass : teachersOrClasses) {
            fillChildMatrix(lastParent.get(teacherOrClass), onlyChild);
        }

        return onlyChild;
    }

    void subSolutionCrossover(
            List<List<List<Quintet>>> twoChildrenPerAspect,
            List<List<Quintet>> matrix1,
            List<List<Quintet>> matrix2) {
        if ((matrix1 != null) && (matrix2 != null)) {
            twoChildrenPerAspect = crossoverBetween2Parents(
                    matrix1,
                    matrix2);
        } else if ((matrix1 == null) && (matrix2 != null)) {
            twoChildrenPerAspect.set(0, matrix2);
            twoChildrenPerAspect.set(1, matrix2);

        } else if ((matrix1 != null) && (matrix2 == null)) {
            twoChildrenPerAspect.set(0, matrix1);
            twoChildrenPerAspect.set(1, matrix1);
        }
    }

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

    ////This function organizes every solution in selectedParents as a Map<aspect, List<List<Quintet>> >
    private <T> List<Map<T, List<List<Quintet>>>> organizeSolutionsPerAspect(List<S> selectedParents) {
        List<Map<T, List<List<Quintet>>>> parentsAsAspectMatrix = new ArrayList();

        for (S solution : selectedParents) {

            if (!(solution instanceof Double)) {
                throw new RuntimeException("solution must be TimeTableSolution");
            }
            Double timeTableSolution = (Double) solution;

            //grouping solution-quintets by aspect (either teacher or class)
            Map<T, List<Quintet>> solutionQuintetsGroupedByAspect;

            if (orientation == Orientation.TEACHER) {
                Map<Teacher, List<Quintet>> solutionQuintetsGroupedByTeacher = timeTableSolution.getSolutionQuintets()
                        .stream()
                        .collect(Collectors.groupingBy(Quintet::getTeacher));

                solutionQuintetsGroupedByAspect = (Map<T, List<Quintet>>) solutionQuintetsGroupedByTeacher;
            } else { //grouped by CLASS
                Map<SchoolClass, List<Quintet>> solutionQuintetsGroupedByClass = timeTableSolution.getSolutionQuintets()
                        .stream()
                        .collect(Collectors.groupingBy(Quintet::getSchoolClass));
                solutionQuintetsGroupedByAspect = (Map<T, List<Quintet>>) solutionQuintetsGroupedByClass;
            }

            //for each teacher/class in solution: convert List<Quintet> to DH-Matrix(List<List<Quintet>>)
            Map<T, List<List<Quintet>>> solutionMatrixPerAspect = new HashMap();

            for (Map.Entry<T, List<Quintet>> entry : solutionQuintetsGroupedByAspect.entrySet()) {
                List<Quintet> solutionQuintets = entry.getValue();
//                System.out.println("organizeSolutionsPerAspect: numOfQuintets in solutionSubGroup " + solutionQuintets.size());
                solutionMatrixPerAspect.put(entry.getKey(), convertQuintetListToMatrix(entry.getValue()));
            }
            //add converted-solution to the collection of parents
            parentsAsAspectMatrix.add(solutionMatrixPerAspect);
        }

        return parentsAsAspectMatrix;
    }

    private <T> Map<T, List<List<Quintet>>> randomlySelectParent(
            List<Map<T, List<List<Quintet>>>> selectedSolutionsAsMatrix) {

        int randomIndex = new Random().nextInt(selectedSolutionsAsMatrix.size());

        return selectedSolutionsAsMatrix.get(randomIndex);
    }

    //generic method - T is either Teacher or SchoolClass
    private <T> void removeParentFromPoolOfParents(
            List<Map<T, List<List<Quintet>>>> selectedSolutionsAsMatrix,
            Map<T, List<List<Quintet>>> itemToRemove) {

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

    //this method combines a subSolution into a mega-solution
    private void fillChildMatrix(List<List<Quintet>> source, List<List<Quintet>> destination) {
        if (source != null) {
            for (int i = 0; i < days * hours; i++) {
                if (source.get(i) != null) {
                    //select a single quintet from source-list to add to mega-child
                    int randomElementIndex = ThreadLocalRandom.current().nextInt(source.get(i).size());
                    List<Quintet> singleQuintetList = new ArrayList();
                    singleQuintetList.add(source.get(i).get(randomElementIndex));

                    if (destination.get(i) == null) {
                        destination.set(i, singleQuintetList);
                    } else {
                        //adding source List<Quintet> to destination list<Quintet>.
//                        System.out.println("fillChildMatrix, numofQuintets already in destination i: " + destination.get(i).size());
                        (destination.get(i)).addAll(singleQuintetList);
                    }
                }
            }
        }
    }
}
