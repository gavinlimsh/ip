package gigabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    // OS-Independent File Paths
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE_PATH = DATA_DIRECTORY + File.separator + "gigabot.txt";

    /**
     * Saves the current list of tasks to the hard disk.
     * Creates the directory and file if they do not exist.
     */
    private static void saveTasks(Task[] tasks, int tasksCounter) {
        try {
            File dir = new File(DATA_DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileWriter fw = new FileWriter(DATA_FILE_PATH);
            for (int i = 0; i < tasksCounter; i++) {
                fw.write(tasks[i].toSaveFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Could not save tasks to disk.");
        }
    }

    /**
     * Loads tasks from the hard disk on startup.
     *
     * @param tasks The array to populate with loaded tasks.
     * @return The number of tasks successfully loaded.
     */
    private static int loadTasks(Task[] tasks) {
        int loadedCount = 0;
        try {
            File f = new File(DATA_FILE_PATH);
            if (!f.exists()) {
                return 0; // No previous data to load
            }
            Scanner fileScanner = new Scanner(f);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                if (parts[0].equals("T")) {
                    tasks[loadedCount] = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    tasks[loadedCount] = new Deadline(parts[2], parts[3]);
                } else if (parts[0].equals("E")) {
                    tasks[loadedCount] = new Event(parts[2], parts[3], parts[4]);
                }

                if (parts[1].equals("1")) {
                    tasks[loadedCount].markAsDone();
                }
                loadedCount++;
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Data file not found.");
        } catch (Exception e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Data file corrupted.");
        }
        return loadedCount;
    }

    private static int printTaskAdded(Task task, int currentCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("[GigaBot] >> Got it. I've added this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        currentCount++;
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        return currentCount;
    }

    public static void main(String[] args) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        Task[] tasks = new Task[100];
        // Load data on startup
        int tasksCounter = loadTasks(tasks);

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
                    for (int i = 0; i < tasksCounter; i++) {
                        System.out.println("[GigaBot] >> " + (i + 1) + "." + tasks[i].toString());
                    }
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("mark ")) {
                    int index = Integer.parseInt(command.substring(5)) - 1;
                    tasks[index].markAsDone();
                    saveTasks(tasks, tasksCounter); // Save on update
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> Nice! I've marked this task as done:");
                    System.out.println("[GigaBot] >>   " + tasks[index].toString());
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("unmark ")) {
                    int index = Integer.parseInt(command.substring(7)) - 1;
                    tasks[index].markAsUndone();
                    saveTasks(tasks, tasksCounter); // Save on update
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> OK, I've marked this task as not done yet:");
                    System.out.println("[GigaBot] >>   " + tasks[index].toString());
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("todo")) {
                    if (command.equals("todo")) {
                        throw new GigaBotException("A todo task requires a description. (e.g., todo read book)");
                    }
                    String description = command.substring(5).trim();
                    tasks[tasksCounter] = new Todo(description);
                    tasksCounter = printTaskAdded(tasks[tasksCounter], tasksCounter);
                    saveTasks(tasks, tasksCounter); // Save on add

                } else if (command.startsWith("deadline")) {
                    if (!command.contains(BY_MARKER)) {
                        throw new GigaBotException("A deadline requires a '" + BY_MARKER.trim() + "' marker.");
                    }
                    String[] parts = command.substring(9).split(BY_MARKER);
                    if (parts.length < 2 || parts[0].trim().isEmpty()) {
                        throw new GigaBotException("A deadline requires both a description and a time.");
                    }
                    tasks[tasksCounter] = new Deadline(parts[0].trim(), parts[1].trim());
                    tasksCounter = printTaskAdded(tasks[tasksCounter], tasksCounter);
                    saveTasks(tasks, tasksCounter); // Save on add

                } else if (command.startsWith("event")) {
                    if (!command.contains(FROM_MARKER) || !command.contains(TO_MARKER)) {
                        throw new GigaBotException("An event requires both '" + FROM_MARKER.trim() + "' and '" + TO_MARKER.trim() + "' markers.");
                    }
                    String[] parts = command.substring(6).split(FROM_MARKER);
                    String[] timeParts = parts[1].split(TO_MARKER);
                    tasks[tasksCounter] = new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim());
                    tasksCounter = printTaskAdded(tasks[tasksCounter], tasksCounter);
                    saveTasks(tasks, tasksCounter); // Save on add

                } else {
                    throw new GigaBotException("SYSTEM ERROR: Command not recognized. Please use todo, deadline, event, list, mark, unmark, or bye.");
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