package il.ac.mta.zuli.evolution.engine;

public interface Engine {
    void loadXML(String path);

    //shows details re the algorithm and schedule received via file
    void showDetails();

    void executeEvolutionAlgo();

    void showBestSolution();

    void showEvolutionProcess();

    void leaveSystem();
}
