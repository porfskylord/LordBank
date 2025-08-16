import app_ui.AppUi;

public class Main {
    public static void main(String[] args) {
        try {
            AppUi appUi = new AppUi();
            appUi.showAppUi();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}