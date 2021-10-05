package il.ac.mta.zuli.evolution.engine;

public enum LogicalRunStatus {
    RUNNING, STOPPED, PAUSED, COMPLETED
}
//RUNNING is either after start or after resume
//COMPLETED is either successful-completion of the algo (hitting one of the endPredicates)
//or unsuccessful completion - with exception
