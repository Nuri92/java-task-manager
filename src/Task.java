public class Task {
	private int        id;
	private String     title;
	private TaskStatus status;
	
	public Task(int id, String title) {
		this.id     = id;
		this.title  = title;
		this.status = TaskStatus.OPEN;
	}
	
	public Task(int id, String title, TaskStatus status) {
		this.id     = id;
		this.title  = title;
		this.status = status;
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
	
	public void markAsInProgress() {
		status = TaskStatus.IN_PROGRESS;
	}
	
	public void markAsCompleted() {
		this.status = TaskStatus.DONE;
	}
}
