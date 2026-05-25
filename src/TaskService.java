import java.util.ArrayList;
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
	
	public void addTask(String title, TaskPriority priority) {
		Task task = new Task(nextId, title, priority);
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
			System.out.println(
					"[" + currentTask.getId() + "] "
							+ currentTask.getTitle()
							+ " (" + status + ")"
							+ " [" + currentTask.getPriority() + "]"
			);
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
	
	public List<Task> getTasksSortedByPriority() {
		List<Task> tasks = new ArrayList<>(repository.findAll());
		
		tasks.sort(
				Comparator.comparing(Task::getPriority)
				          .reversed()
		);
		
		return tasks;
	}
	
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
		List<Task> filteredTasks = new ArrayList<>();
		
		for (Task currentTask : repository.findAll()) {
			if (currentTask.getPriority() == priority) {
				filteredTasks.add(currentTask);
			}
		}
		return filteredTasks;
	}
}
