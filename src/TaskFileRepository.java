import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskFileRepository {
	
	private final String fileName;
	
	public TaskFileRepository(String fileName) {
		this.fileName = fileName;
	}
	
	public void saveTasks(List<Task> tasks) {
		try {
			FileWriter writer = new FileWriter(fileName);
			
			for (Task task : tasks) {
				writer.write(task.getId() + ";"
						+ task.getTitle()
						+ ";"
						+ task.getStatus()
						+ ";"
						+ task.getPriority()
						+ "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Fehler beim Speichern.");
		}
	}
	
	public ArrayList<Task> loadTasks() {
		
		ArrayList<Task> tasks = new ArrayList<>();
		
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(fileName)
			);
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				if (line.isBlank()) {
					continue;
				}
				
				String[] parts = line.split(";");
				
				int          id       = Integer.parseInt(parts[0]);
				String       title    = parts[1];
				TaskStatus   status   = TaskStatus.valueOf(parts[2]);
				TaskPriority priority = TaskPriority.valueOf(parts[3]);
				
				Task task = new Task(id, title, status, priority);
				
				if (status == TaskStatus.IN_PROGRESS) {
					task.markAsInProgress();
				}
				
				if (status == TaskStatus.DONE) {
					task.markAsCompleted();
				}
				
				tasks.add(task);
			}
			
			reader.close();
			
		} catch (IOException e) {
			System.out.println("Datei konnte nicht geladen werden.");
		}
		
		return tasks;
	}
}
