import java.util.Scanner;

/**
 * GigaBot is an interactive conversational assistant that tracks and manages user tasks.
 */
public class GigaBot {

    private static final String BANNER = "   _____ _             ____        _    \n"
            + "  / ____(_)           |  _ \\      | |   \n"
            + " | |  __ _  __ _  __ _| |_) | ___ | |_  \n"
            + " | | |_ | |/ _` |/ _` |  _ < / _ \\| __| \n"
            + " | |__| | | (_| | (_| | |_) | (_) | |_  \n"
            + "  \\_____|_|\\__, |\\__,_|____/ \\___/ \\__| \n"
            + "            __/ |                       \n"
            + "           |___/                        \n";

    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    /**
     * Initializes the application and enters the main listening loop.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        // Initialize Variables
        Task[] tasks = new Task[100];
        int tasksCounter = 0;
        Scanner in = new Scanner(System.in);

        // Listen in on new input
        while (true) {
            String userInput = in.nextLine();

            // End conversation condition
            if (userInput.equals("bye")) {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Shutting down. Hope to see you again soon!");
                System.out.println(HORIZONTAL_LINE);
                break;
            } else if (userInput.equals("list")) {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Here are the tasks in your list:");
                for (int i = 0; i < tasksCounter; i++) {
                    System.out.println("[GigaBot] >> " + (i + 1) + "." + tasks[i].toString());
                }
                System.out.println(HORIZONTAL_LINE);
            } else if (userInput.startsWith("mark ")) {
                int index = Integer.parseInt(userInput.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Nice! I've marked this task as done:");
                System.out.println("[GigaBot] >>   " + tasks[index].toString());
                System.out.println(HORIZONTAL_LINE);
            } else if (userInput.startsWith("unmark ")) {
                int index = Integer.parseInt(userInput.substring(7)) - 1;
                tasks[index].markAsUndone();
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> OK, I've marked this task as not done yet:");
                System.out.println("[GigaBot] >>   " + tasks[index].toString());
                System.out.println(HORIZONTAL_LINE);
            } else if (userInput.startsWith("todo ")) {
                String description = userInput.substring(5).trim();
                tasks[tasksCounter] = new Todo(description);

                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Got it. I've added this task:");
                System.out.println("[GigaBot] >>   " + tasks[tasksCounter].toString());
                tasksCounter++;
                System.out.println("[GigaBot] >> Now you have " + tasksCounter + " tasks in the list.");
                System.out.println(HORIZONTAL_LINE);
            } else if (userInput.startsWith("deadline ")) {
                String[] parts = userInput.substring(9).split(" /by ");
                tasks[tasksCounter] = new Deadline(parts[0], parts[1]);

                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Got it. I've added this task:");
                System.out.println("[GigaBot] >>   " + tasks[tasksCounter].toString());
                tasksCounter++;
                System.out.println("[GigaBot] >> Now you have " + tasksCounter + " tasks in the list.");
                System.out.println(HORIZONTAL_LINE);
            } else if (userInput.startsWith("event ")) {
                String[] parts = userInput.substring(6).split(" /from ");
                String[] timeParts = parts[1].split(" /to ");
                tasks[tasksCounter] = new Event(parts[0], timeParts[0], timeParts[1]);

                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Got it. I've added this task:");
                System.out.println("[GigaBot] >>   " + tasks[tasksCounter].toString());
                tasksCounter++;
                System.out.println("[GigaBot] >> Now you have " + tasksCounter + " tasks in the list.");
                System.out.println(HORIZONTAL_LINE);
            } else {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> OOPS!!! I'm sorry, but I don't know what that means :-(");
                System.out.println(HORIZONTAL_LINE);
            }
        }
        in.close();
    }
}