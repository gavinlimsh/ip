/**
 * Represents a todo task without any date or time attached.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task.
     *
     * @param description The text description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}