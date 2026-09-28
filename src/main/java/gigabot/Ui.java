package gigabot;

import java.util.Scanner;

public class Ui {
    private Scanner in;
    private static final String BANNER = "   _____ _             ____        _    \n"
            + "  / ____(_)           |  _ \\      | |   \n"
            + " | |  __ _  __ _  __ _| |_) | ___ | |_  \n"
            + " | | |_ | |/ _` |/ _` |  _ < / _ \\| __| \n"
            + " | |__| | | (_| | (_| | |_) | (_) | |_  \n"
            + "  \\_____|_|\\__, |\\__,_|____/ \\___/ \\__| \n"
            + "            __/ |                       \n"
            + "           |___/                        \n";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    public Ui() {
        this.in = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    public String readCommand() {
        return in.nextLine().trim();
    }

    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    public void showError(String message) {
        System.out.println("[GigaBot] >> " + message);
    }

    public void showLoadingError() {
        System.out.println("[GigaBot] >> SYSTEM ERROR: Could not load existing data. Starting fresh.");
    }

    public void showMessage(String message) {
        System.out.println("[GigaBot] >> " + message);
    }

    public void showTaskAdded(Task task, int currentCount) {
        System.out.println("[GigaBot] >> Got it. I've added this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
    }

    public void showTaskDeleted(Task task, int currentCount) {
        System.out.println("[GigaBot] >> Noted. I've removed this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
    }
}