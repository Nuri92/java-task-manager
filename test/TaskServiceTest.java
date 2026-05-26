import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
		TaskRepository     repository     = new TaskMemoryRepository();
		TaskService        service        = new TaskService(repository, fileRepository);
		
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
	
	@Test
	void shouldSortTasksByPriority() {
		service.addTask("Low Task", TaskPriority.LOW);
		service.addTask("High Task", TaskPriority.HIGH);
		service.addTask("Medium Task", TaskPriority.MEDIUM);
		
		List<Task> sortedTasks = service.getTasksSortedByPriority();
		
		assertEquals(TaskPriority.HIGH, sortedTasks.get(0).getPriority());
		assertEquals(TaskPriority.MEDIUM, sortedTasks.get(1).getPriority());
		assertEquals(TaskPriority.LOW, sortedTasks.get(2).getPriority());
	}
	
	@Test
	void shouldFilterTasksByPriority() {
		service.addTask("Low Task", TaskPriority.LOW);
		service.addTask("High Task", TaskPriority.HIGH);
		service.addTask("Another High Task", TaskPriority.HIGH);
		
		List<Task> highTasks = service.getTasksByPriority(TaskPriority.HIGH);
		
		assertEquals(2, highTasks.size());
		assertEquals(TaskPriority.HIGH, highTasks.get(0).getPriority());
		assertEquals(TaskPriority.HIGH, highTasks.get(1).getPriority());
	}
	
	@Test
	void shouldReturnTaskTitles() {
		
		service.addTask("Java lernen", TaskPriority.HIGH);
		service.addTask("Git lernen", TaskPriority.LOW);
		
		List<String> titles = service.getTaskTitles();
		
		assertEquals(
				"Java lernen",
				titles.get(0)
		);
		
		assertEquals(
				"Git lernen",
				titles.get(1)
		);
	}
	
	@Test
	void shouldFindTasksBySearchText() {
		service.addTask("Java lernen", TaskPriority.HIGH);
		service.addTask("Git lernen", TaskPriority.MEDIUM);
		service.addTask("Einkaufen", TaskPriority.LOW);
		
		List<Task> results = service.getTasksBySearchText("lernen");
		
		assertEquals(2, results.size());
		assertEquals("Java lernen", results.get(0).getTitle());
		assertEquals("Git lernen", results.get(1).getTitle());
	}
	
	@Test
	void shouldThrowExceptionWhenTitleIsBlank() {
		assertThrows(
				InvalidTaskTitleException.class,
				() -> service.addTask("   ", TaskPriority.MEDIUM)
		);
	}
}