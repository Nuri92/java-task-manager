import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskFileRepository {
	private static final String FILE_NAME = "tasks.txt";
	
	public void saveTasks(List<Task> tasks) {
		try {
			FileWriter writer = new FileWriter(FILE_NAME);
			
			for (Task task : tasks) {
				writer.write(task.getId() + ";"
						+ task.getTitle()
						+ ";"
						+ task.getStatus()
						+ "\n");
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Fehler beim Speichern.");
		}
	}
	
	public ArrayList<Task> loadTask() {
		ArrayList<Task> tasks = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			
			String line;
			
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				
				int        id     = Integer.parseInt(parts[0]);
				String     title  = parts[1];
				TaskStatus status = TaskStatus.valueOf(parts[2]);
				
				Task task = new Task(id, title, status);
				tasks.add(task);
				
			}
		} catch (IOException e) {
			System.out.println("Keine gespeicherten Tasks gefunden.");
		}
		
		return tasks;
	}
}
