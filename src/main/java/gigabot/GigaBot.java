package gigabot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE_PATH = DATA_DIRECTORY + File.separator + "gigabot.txt";

    /**
     * Saves the current list of tasks to the hard disk.
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        try {
            File dir = new File(DATA_DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileWriter fw = new FileWriter(DATA_FILE_PATH);
            for (Task task : tasks) {
                fw.write(task.toSaveFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Could not save tasks to disk.");
        }
    }

    /**
     * Loads tasks from the hard disk on startup.
     */
    private static void loadTasks(ArrayList<Task> tasks) {
        try {
            File f = new File(DATA_FILE_PATH);
            if (!f.exists()) {
                return;
            }
            Scanner fileScanner = new Scanner(f);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");
                Task loadedTask = null;

                if (parts[0].equals("T")) {
                    loadedTask = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    loadedTask = new Deadline(parts[2], parts[3]);
                } else if (parts[0].equals("E")) {
                    loadedTask = new Event(parts[2], parts[3], parts[4]);
                }

                if (loadedTask != null) {
                    if (parts[1].equals("1")) {
                        loadedTask.markAsDone();
                    }
                    tasks.add(loadedTask);
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Data file not found.");
        } catch (Exception e) {
            System.out.println("[GigaBot] >> SYSTEM ERROR: Data file corrupted.");
        }
    }

    private static void printTaskAdded(Task task, int currentCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("[GigaBot] >> Got it. I've added this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    private static void printTaskDeleted(Task task, int currentCount) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("[GigaBot] >> Noted. I've removed this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
    }

    public static void main(String[] args) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);

        ArrayList<Task> tasks = new ArrayList<>();
        loadTasks(tasks);

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
                    saveTasks(tasks);
                    System.out.println(HORIZONTAL_LINE);
                    System.out.println("[GigaBot] >> Nice! I've marked this task as done:");
                    System.out.println("[GigaBot] >>   " + tasks.get(index).toString());
                    System.out.println(HORIZONTAL_LINE);

                } else if (command.startsWith("unmark ")) {
                    int index = Integer.parseInt(command.substring(7).trim()) - 1;
                    tasks.get(index).markAsUndone();
                    saveTasks(tasks);
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
                    saveTasks(tasks);
                    printTaskDeleted(removedTask, tasks.size());

                } else if (command.startsWith("todo")) {
                    if (command.equals("todo")) {
                        throw new GigaBotException("A todo task requires a description. (e.g., todo read book)");
                    }
                    String description = command.substring(5).trim();
                    tasks.add(new Todo(description));
                    saveTasks(tasks);
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
                    saveTasks(tasks);
                    printTaskAdded(tasks.get(tasks.size() - 1), tasks.size());

                } else if (command.startsWith("event")) {
                    if (!command.contains(FROM_MARKER) || !command.contains(TO_MARKER)) {
                        throw new GigaBotException("An event requires both '" + FROM_MARKER.trim() + "' and '" + TO_MARKER.trim() + "' markers.");
                    }
                    String[] parts = command.substring(6).split(FROM_MARKER);
                    String[] timeParts = parts[1].split(TO_MARKER);
                    tasks.add(new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()));
                    saveTasks(tasks);
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