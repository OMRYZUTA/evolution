package il.ac.mta.zuli.evolution.engine.predicates;

public class TimePredicate extends PredicateClass<Double> {
    private final double totalMinutes;

    public TimePredicate(PredicateType type, double totalMinutes) {
        super(type);
        this.totalMinutes = totalMinutes;
    }

    @Override
    public boolean test(double currMinutes) {
        return currMinutes <= totalMinutes;
    }

    public double getTotalMinutes() {
        return totalMinutes;
    }
}
