package il.ac.mta.zuli.evolution.engine;

public interface Engine {
    void loadXML(String path); //in impl of method - use other method to check if a valid file is already loaded

    void showDetails();

    void executeEvolutionAlgo();

    void showBestSolution();

    void showEvolutionProcess();

    void leaveSystem();
}
