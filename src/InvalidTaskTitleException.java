public class InvalidTaskTitleException extends RuntimeException {
	public InvalidTaskTitleException() {
		super("Titel darf nicht leer sein.");
	}
}
