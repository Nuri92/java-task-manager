public class TaskNotFoundException extends RuntimeException {
	public TaskNotFoundException(int taskId) {
		super("Task mit der ID " + taskId + " wurde nicht gefunden.");
	}
}
