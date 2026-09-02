/**
 * Represents a basic task with a description and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new Task with the given description.
     * By default, the task is marked as not done.
     * @param description The text description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the completion status icon of the task.
     *
     * @return "X" if the task is done, or a blank space if it is not done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the string representation of the task.
     *
     * @return Formatted string of the task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
