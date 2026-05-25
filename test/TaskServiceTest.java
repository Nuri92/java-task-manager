import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskServiceTest {
	
	private TaskRepository     repository;
	private TaskFileRepository fileRepository;
	private TaskService        service;
	
	@BeforeEach
	void setUp() {
		
		repository     = new TaskMemoryRepository();
		fileRepository = new TaskFileRepository("test-tasks.txt");
		service        = new TaskService(repository, fileRepository);
	}
	
	
	@Test
	void shouldAddTask() {
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		
		assertEquals(
				1,
				repository.findAll().size()
		);
		
		assertEquals(
				"Java lernen",
				repository.findAll().get(0).getTitle()
		);
		
		assertEquals(
				TaskStatus.OPEN,
				repository.findAll().get(0).getStatus()
		);
	}
	
	@Test
	void shouldDeleteTask() {
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		
		service.deleteTask(1);
		
		assertEquals(0, repository.findAll().size());
	}
	
	@Test
	void shouldThrowExceptionWhenDeletingUnknownTask() {
		assertThrows(
				TaskNotFoundException.class,
				() -> service.deleteTask(999)
		);
	}
	
	@Test
	void shouldMarkTaskAsCompleted() {
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		
		service.markTaskAsCompleted(1);
		
		assertEquals(
				TaskStatus.DONE,
				repository.findAll().get(0).getStatus()
		);
	}
	
	@Test
	void shouldMarkTaskAsInProgress() {
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		
		service.markAsInProgress(1);
		
		assertEquals(
				TaskStatus.IN_PROGRESS,
				repository.findAll().get(0).getStatus()
		);
	}
	
	@Test
	void shouldAssignIncrementingIds() {
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		service.addTask("Git lernen", TaskPriority.MEDIUM);
		
		assertEquals(1, repository.findAll().get(0).getId());
		assertEquals(2, repository.findAll().get(1).getId());
	}
	
	@Test
	void shouldSaveAndLoadTasksFromFile() {
		String testFileName = "test-tasks.txt";
		
		TaskFileRepository fileRepository = new TaskFileRepository(testFileName);
		TaskRepository repository = new TaskMemoryRepository();
		TaskService service = new TaskService(repository, fileRepository);
		
		service.addTask("Java lernen", TaskPriority.MEDIUM);
		service.markTaskAsCompleted(1);
		service.saveTasks();
		
		TaskFileRepository secondFileRepository = new TaskFileRepository(testFileName);
		
		assertEquals(
				1,
				secondFileRepository.loadTasks().size()
		);
		
		assertEquals(
				"Java lernen",
				secondFileRepository.loadTasks().get(0).getTitle()
		);
		
		assertEquals(
				TaskStatus.DONE,
				secondFileRepository.loadTasks().get(0).getStatus()
		);
	}
}