import java.util.List;

public class TaskService {
	private final TaskRepository     repository;
	private final TaskFileRepository fileRepository;
	private       int                nextId = 1;
	
	public TaskService(TaskRepository repository, TaskFileRepository fileRepository) {
		this.repository     = repository;
		this.fileRepository = fileRepository;
		loadTasks();
	}
	
	public void loadTasks() {
		List<Task> loadedTasks = fileRepository.loadTask();
		repository.saveAll(loadedTasks);
		
		for (Task task : loadedTasks) {
			if (task.getId() >= nextId) {
				nextId = task.getId() + 1;
			}
		}
	}
	
	public void saveTasks() {
		fileRepository.saveTasks(repository.findAll());
	}
	
	public void addTask(String title) {
		Task task = new Task(nextId, title);
		repository.save(task);
		nextId++;
		System.out.println("Task hinzufügen");
	}
	
	public void showTasks() {
		List<Task> tasks = repository.findAll();
		
		if (tasks.isEmpty()) {
			System.out.println("Keine Tasks vorhanden");
			return;
		}
		
		for (Task currentTask : tasks) {
			String status = formatStatus(currentTask.getStatus());
			System.out.println("[" + currentTask.getId() + "] " + currentTask.getTitle() + " (" + status + ")");
		}
	}
	
	public void markAsInProgress(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		task.markAsInProgress();
	}
	
	public void markTaskAsCompleted(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		task.markAsCompleted();
		System.out.println("Task ist in jetzt in Bearbeitung.");
	}
	
	public void deleteTask(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		repository.delete(task);
		
		System.out.println("Task wurde gelöscht.");
	}
	
	public Task findById(int taskId) {
		for (Task task : repository.findAll()) {
			if (task.getId() == taskId) {
				return task;
			}
		}
		return null;
	}
	
	private String formatStatus(TaskStatus status) {
		if (status == TaskStatus.DONE) {
			return "Erledigt";
		} else if (status == TaskStatus.IN_PROGRESS) {
			return "In Bearbeitung";
		} else {
			return "Offen";
		}
	}
	
	public void searchTask(String searchText) {
		List<Task> tasks = repository.findAll();
		
		boolean found = false;
		
		for (Task currentTask : tasks) {
			if (currentTask.getTitle()
			               .toLowerCase()
			               .contains(searchText.toLowerCase())) {
				
				String status = formatStatus(currentTask.getStatus());
				
				System.out.println(
						"[" + currentTask.getId() + "] "
								+ currentTask.getTitle()
								+ " (" + status + ")"
				);
				
				found = true;
			}
		}
		
		if (!found) {
			System.out.println("Keine passenden Tasks gefunden.");
		}
	}
}
