package pocketgems.mud;

public class Main {

	/*
	 * Main
	 * ====
	 * Optionally takes one argument, which is the file path for the file defining the initial game state.
	 */
	public static void main(String[] args) {
		String gameStateFileName = "";
		if (args.length > 0) {
			gameStateFileName = args[0];
		}
		
		Game game = new Game(new InputProcessor(), gameStateFileName);
		game.run();
	}
}
