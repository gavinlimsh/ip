package gigabot;

import java.util.ArrayList;
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

    private static final String BY_MARKER = " /by ";
    private static final String FROM_MARKER = " /from ";
    private static final String TO_MARKER = " /to ";

    /**
     * Formats and prints the confirmation message when a new task is added.
     *
     * @param task The newly created task.
     * @param currentCount The new total number of tasks.
     */
    private static void printTaskAdded(Task task, int currentCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("[GigaBot] >> Got it. I've added this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Formats and prints the confirmation message when a task is deleted.
     *
     * @param task The removed task.
     * @param currentCount The new total number of tasks.
     */
    private static void printTaskDeleted(Task task, int currentCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("[GigaBot] >> Noted. I've removed this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Initializes the application and enters the main listening loop.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        while (true) {
            String userInput = in.nextLine();
            String command = userInput.trim();

            if (command.equals("bye")) {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> Shutting down. Hope to see you again soon!");
                System.out.println(HORIZONTAL_LINE);
                break;
            }

            try {
                if (command.equals("list")) {
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("[GigaBot] >> " + (i + 1) + "." + tasks.get(i).toString());
                    }
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("mark ")) {
                    int index = Integer.parseInt(command.substring(5).trim()) - 1;
                    tasks.get(index).markAsDone();
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> Nice! I've marked this task as done:");
                    System.out.println("[GigaBot] >>   " + tasks.get(index).toString());
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("unmark ")) {
                    int index = Integer.parseInt(command.substring(7).trim()) - 1;
                    tasks.get(index).markAsUndone();
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> OK, I've marked this task as not done yet:");
                    System.out.println("[GigaBot] >>   " + tasks.get(index).toString());
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("delete ")) {
                    int index = Integer.parseInt(command.substring(7).trim()) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new GigaBotException("Task number does not exist.");
                    }
                    Task removedTask = tasks.remove(index);
                    printTaskDeleted(removedTask, tasks.size());

                } else if (command.startsWith("todo")) {
                    if (command.equals("todo")) {
                        throw new GigaBotException("A todo task requires a description. (e.g., todo read book)");
                    }
                    String description = command.substring(5).trim();
                    tasks.add(new Todo(description));
                    printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());

                } else if (command.startsWith("deadline")) {
                    if (!command.contains(BY_MARKER)) {
                        throw new GigaBotException("A deadline requires a '" + BY_MARKER.trim() + "' marker.");
                    }
                    String[] parts = command.substring(9).split(BY_MARKER);
                    if (parts.length < 2 || parts[0].trim().isEmpty()) {
                        throw new GigaBotException("A deadline requires both a description and a time.");
                    }
                    tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
                    printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());

                } else if (command.startsWith("event")) {
                    if (!command.contains(FROM_MARKER) || !command.contains(TO_MARKER)) {
                        throw new GigaBotException("An event requires both '" + FROM_MARKER.trim() + "' and '" + TO_MARKER.trim() + "' markers.");
                    }
                    String[] parts = command.substring(6).split(FROM_MARKER);
                    String[] timeParts = parts[1].split(TO_MARKER);
                    tasks.add(new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()));
                    printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());

                } else {
                    throw new GigaBotException("SYSTEM ERROR: Command not recognized. Please use todo, deadline, event, list, mark, unmark, delete, or bye.");
                }

            } catch (GigaBotException e) {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> " + e.getMessage());
                System.out.println(HORIZONTAL_LINE);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.out.println(HORIZONTAL_LINE);
                System.out.println("[GigaBot] >> SYSTEM ERROR: Invalid task number or format provided.");
                System.out.println(HORIZONTAL_LINE);
            }
        }
        in.close();
    }
}