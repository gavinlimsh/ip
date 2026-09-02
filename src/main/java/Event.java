/**
 * Represents an event task that starts and ends at specific times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new Event task.
     *
     * @param description The text description of the event.
     * @param from The start date/time.
     * @param to The end date/time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}