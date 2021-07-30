package il.ac.mta.zuli.evolution.dto;

public class RequirementDTO {
    private final int hours;
    private final SubjectDTO subject;

    public RequirementDTO(int hours, SubjectDTO subject) {
        this.hours = hours;
        this.subject = subject;
    }

    public int getHours() {
        return hours;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "RequirementDTO{" +
                "hours=" + hours +
                ", subject=" + subject +
                '}';
    }
}
