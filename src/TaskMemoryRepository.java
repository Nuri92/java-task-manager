import java.util.ArrayList;
import java.util.List;

public class TaskMemoryRepository implements TaskRepository {
	private final List<Task> tasks = new ArrayList<>();
	
	@Override
	public void save(Task task) {
		tasks.add(task);
	}
	
	@Override
	public List<Task> findAll() {
		return tasks;
	}
	
	@Override
	public void delete(Task task) {
		tasks.remove(task);
	}
	
	@Override
	public void saveAll(List<Task> loadedTasks) {
		tasks.addAll(loadedTasks);
	}
}
