/**
 * Represents a deadline task that needs to be completed before a specific date/time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a new Deadline task.
     *
     * @param description The text description of the deadline.
     * @param by The date or time the task must be completed by.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}