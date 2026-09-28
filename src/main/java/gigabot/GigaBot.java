package gigabot;

import java.io.File;

/**
 * GigaBot is an interactive conversational assistant that tracks and manages user tasks.
 */
public class GigaBot {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public GigaBot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (GigaBotException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                isExit = Parser.parseAndExecute(fullCommand, tasks, ui, storage);
            } catch (GigaBotException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                ui.showError("SYSTEM ERROR: Invalid task number or format provided.");
            } finally {
                if (!isExit) {
                    ui.showLine();
                }
            }
        }
    }

    public static void main(String[] args) {
        new GigaBot("data" + File.separator + "gigabot.txt").run();
    }
}