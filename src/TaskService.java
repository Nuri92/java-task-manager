import java.util.Comparator;
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
		List<Task> loadedTasks = fileRepository.loadTasks();
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
	
	
	/**
	 * Creates and stores a new task.
	 *
	 * @param title title of the task
	 * @param priority priority of the task
	 * @return created task
	 * @throws InvalidTaskTitleException if title is empty
	 */
	public Task addTask(String title, TaskPriority priority) {
		
		if (title == null || title.isBlank()) {
			throw new InvalidTaskTitleException();
		}
		
		Task task = new Task(nextId, title, priority);
		repository.save(task);
		nextId++;
		
		return task;
	}
	
	public void showTasks() {
		List<Task> tasks = repository.findAll();
		
		if (tasks.isEmpty()) {
			System.out.println("Keine Tasks vorhanden");
			return;
		}
		
		for (Task currentTask : tasks) {
			String status = formatStatus(currentTask.getStatus());
			System.out.println(
					"[" + currentTask.getId() + "] "
							+ currentTask.getTitle()
							+ " (" + status + ")"
							+ " [" + currentTask.getPriority() + "]"
			);
		}
	}
	
	public Task markAsInProgress(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		task.markAsInProgress();
		return task;
	}
	
	/**
	 * Marks a task as completed.
	 *
	 * @param taskId ID of the task
	 * @return updated task
	 * @throws TaskNotFoundException if task does not exist
	 */
	public Task markTaskAsCompleted(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		task.markAsCompleted();
		return task;
	}
	
	/**
	 * Deletes a task by ID.
	 *
	 * @param taskId ID of the task
	 * @return deleted task
	 * @throws TaskNotFoundException if task does not exist
	 */
	public Task deleteTask(int taskId) {
		Task task = findById(taskId);
		
		if (task == null) {
			throw new TaskNotFoundException(taskId);
		}
		
		repository.delete(task);
		
		return task;
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
		List<Task> tasks = getTasksBySearchText(searchText);
		
		if (tasks.isEmpty()) {
			System.out.println("Keine passenden Tasks gefunden.");
			return;
		}
		
		for (Task currentTask : tasks) {
			printTask(currentTask);
		}
	}
	
	public void showTasksSortedByPriority() {
		List<Task> tasks = getTasksSortedByPriority();
		
		if (tasks.isEmpty()) {
			System.out.println("Keine Tasks vorhanden.");
			return;
		}
		
		for (Task currentTask : tasks) {
			printTask(currentTask);
		}
	}
	
	private void printTask(Task task) {
		String status = formatStatus(task.getStatus());
		
		System.out.println(
				"[" + task.getId() + "] "
						+ task.getTitle()
						+ " (" + status + ")"
						+ " [" + task.getPriority() + "]"
		);
	}
	
	/**
	 * Returns tasks sorted by priority.
	 *
	 * Order:
	 * HIGH -> MEDIUM -> LOW
	 *
	 * @return sorted task list
	 */
	public List<Task> getTasksSortedByPriority() {
		return repository.findAll()
		                 .stream()
		                 .sorted(Comparator.comparing(Task::getPriority).reversed())
		                 .toList();
	}
	
	/**
	 * Returns all tasks with the given priority.
	 *
	 * @param priority priority to filter by
	 * @return filtered task list
	 */
	public void showTasksByPriority(TaskPriority priority) {
		List<Task> tasks = getTasksByPriority(priority);
		
		if (tasks.isEmpty()) {
			System.out.println("Keine Tasks gefunden.");
			return;
		}
		
		for (Task currentTask : tasks) {
			printTask(currentTask);
		}
	}
	
	public List<Task> getTasksByPriority(TaskPriority priority) {
		return repository.findAll()
		                 .stream()
		                 .filter(task -> task.getPriority() == priority)
		                 .toList();
	}
	
	public List<String> getTaskTitles() {
		return repository.findAll()
		                 .stream()
		                 .map(Task::getTitle)
		                 .toList();
	}
	
	public List<Task> getTasksBySearchText(String searchText) {
		return repository.findAll()
		                 .stream()
		                 .filter(task -> task.getTitle().toLowerCase().contains(searchText.toLowerCase()))
		                 .toList();
	}
}
