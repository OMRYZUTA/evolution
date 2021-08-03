package il.ac.mta.zuli.evolution.engine.evolutionengine;

public interface Solution extends Comparable<Solution> {

    void calculateTotalScore();

    double getTotalFitnessScore();

    int compareTo(Solution other);

}
