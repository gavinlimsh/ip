import java.util.Scanner;

public class GigaBot {
    public static void main(String[] args) {
        String banner = "   _____ _             ____        _    \n"
                      + "  / ____(_)           |  _ \\      | |   \n"
                      + " | |  __ _  __ _  __ _| |_) | ___ | |_  \n"
                      + " | | |_ | |/ _` |/ _` |  _ < / _ \\| __| \n"
                      + " | |__| | | (_| | (_| | |_) | (_) | |_  \n"
                      + "  \\_____|_|\\__, |\\__,_|____/ \\___/ \\__| \n"
                      + "            __/ |                       \n"
                      + "           |___/                        \n";

        String horizontalLine = "____________________________________________________________";

        System.out.println(horizontalLine);
        System.out.println(banner);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);

        // Initialize Scanner
        Scanner in = new Scanner(System.in);

        // Listen in on new input
        while (true) {
            String userInput = in.nextLine();

            // End conversation condition
            if (userInput.equals("bye")) {
                System.out.println(horizontalLine);
                System.out.println("[GigaBot] >> Shutting down. Hope to see you again soon!");
                System.out.println(horizontalLine);
                break;
            }

            System.out.println(horizontalLine);
            System.out.println("[GigaBot] >> " + userInput);
            System.out.println(horizontalLine);
        }
        in.close();
    }
}
