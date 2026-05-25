import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		Scanner            scanner        = new Scanner(System.in);
		TaskRepository     taskRepository = new TaskMemoryRepository();
		TaskFileRepository fileRepository = new TaskFileRepository("task.txt");
		TaskService        taskService    = new TaskService(taskRepository, fileRepository);
		
		boolean running = true;
		
		while (running) {
			
			System.out.println();
			System.out.println("=== TASK MANAGER ===");
			System.out.println("1 - Task hinzufügen");
			System.out.println("2 - Alle Task anzeigen");
			System.out.println("3 - Task in Bearbeitung setzen");
			System.out.println("4 - Task erledigen");
			System.out.println("5 - Task löschen");
			System.out.println("6 - Task suchen");
			System.out.println("7 - Tasks sortieren");
			System.out.println("8 - Beenden");
			System.out.println("Auswahl: ");
			
			int choice = readInt(scanner);
			
			
			switch (choice) {
				case 1 -> addTask(scanner, taskService);
				case 2 -> showTasks(taskService);
				case 3 -> {
					try {
						markTaskAsInProgress(scanner, taskService);
					} catch (TaskNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
				case 4 -> {
					try {
						markTaskAsCompleted(scanner, taskService);
					} catch (TaskNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
				case 5 -> {
					try {
						deleteTask(scanner, taskService);
					} catch (TaskNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
				case 6 -> searchTask(scanner, taskService);
				case 7 -> taskService.showTasksSortedByPriority();
				case 8 -> {
					taskService.saveTasks();
					System.out.println("Tasks wurden gespeichert.");
					System.out.println("Programm wird beendet");
					running = false;
				}
				default -> System.out.println("Ungültige Eingabe");
			}
		}
		
		scanner.close();
	}
	
	private static void searchTask(Scanner scanner, TaskService taskService) {
		scanner.nextLine();
		System.out.println("Suchbegriff: ");
		
		String searchText = scanner.nextLine();
		taskService.searchTask(searchText);
	}
	
	public static void addTask(Scanner scanner, TaskService taskService) {
		scanner.nextLine();
		System.out.println("Titel eingeben: ");
		String title = scanner.nextLine();
		
		if (title.isBlank()) {
			System.out.println("Titel darf nicht leer dein-");
			return;
		}
		
		System.out.println("Priorität wählen:");
		System.out.println("1 - LOW");
		System.out.println("2 - MEDIUM");
		System.out.println("3 - HIGH");
		System.out.print("Auswahl: ");
		
		int priorityChoice = readInt(scanner);
		
		TaskPriority priority;
		
		switch (priorityChoice) {
			case 1:
				priority = TaskPriority.LOW;
				break;
			case 2:
				priority = TaskPriority.MEDIUM;
				break;
			case 3:
				priority = TaskPriority.HIGH;
				break;
			default:
				System.out.println("Ungültige Priorität. Standard: MEDIUM");
				priority = TaskPriority.MEDIUM;
		}
		
		taskService.addTask(title, priority);
	}
	
	public static void showTasks(TaskService taskService) {
		taskService.showTasks();
	}
	
	public static void markTaskAsInProgress(Scanner scanner, TaskService taskService) {
		System.out.println("Task ID eingeben: ");
		int taskId = readInt(scanner);
		taskService.markAsInProgress(taskId);
	}
	
	public static void markTaskAsCompleted(Scanner scanner, TaskService taskService) {
		
		System.out.print("Task ID eingeben: ");
		int taskId = readInt(scanner);
		taskService.markTaskAsCompleted(taskId);
	}
	
	public static int readInt(Scanner scanner) {
		while (true) {
			try {
				return scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Ungültige Eingabe. Bitte Zahl eingeben.");
				scanner.nextLine();
			}
		}
	}
	
	public static void deleteTask(Scanner scanner, TaskService taskService) {
		System.out.println("Task ID zum löschen eingeben: ");
		int deleteId = readInt(scanner);
		taskService.deleteTask(deleteId);
	}
}