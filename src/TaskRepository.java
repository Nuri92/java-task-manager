import java.util.List;

public interface TaskRepository
{
	void save(Task task);
	
	List<Task> findAll();
	
	void delete(Task task);
	
	void saveAll(List<Task> tasks);
}