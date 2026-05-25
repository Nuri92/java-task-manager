public class Task {
	private int          id;
	private String       title;
	private TaskStatus   status;
	private TaskPriority priority;
	
	public Task(int id, String title) {
		this.id     = id;
		this.title  = title;
		this.status = TaskStatus.OPEN;
	}
	
	public Task(int id, String title, TaskPriority priority) {
		this.id       = id;
		this.title    = title;
		this.priority = priority;
		this.status = TaskStatus.OPEN;
	}
	
	public Task(int id, String title, TaskStatus status) {
		this.id     = id;
		this.title  = title;
		this.status = status;
	}
	
	public Task(int id, String title, TaskStatus status, TaskPriority priority) {
		this.id       = id;
		this.title    = title;
		this.status   = status;
		this.priority = priority;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public TaskStatus getStatus() {
		return status;
	}
	
	public TaskPriority getPriority() {
		return priority;
	}
	
	public void markAsInProgress() {
		status = TaskStatus.IN_PROGRESS;
	}
	
	public void markAsCompleted() {
		this.status = TaskStatus.DONE;
	}
}
