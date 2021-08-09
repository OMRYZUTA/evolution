package il.ac.mta.zuli.evolution.ui;

public class Program {
    public static void main(String[] args) {
        try {
            UI ui = new UI();
            ui.operateMenu();
        } catch (Exception e) {
            System.out.println("Failed running program. " + e.getMessage());
        }
    }
}
