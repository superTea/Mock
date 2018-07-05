package pocketgems.mud;

import pocketgems.mud.entity.Entity;

import java.util.Scanner;

/*
 * Game is a top level class that contains the game loop and the game state. Nothing related to
 * game logic and state should retain a reference to Game. Instead pass a reference to Game as
 * a method parameter, as needed.
 */
public class Game {

	public boolean isRunning;
	private World world;
	private InputProcessor inputProcessor;
	
	public Game(InputProcessor inputProcessor, String gameStateFileName) {
		this.inputProcessor = inputProcessor;
		world = World.getInstance();
		
		if (!gameStateFileName.equals("")) {
			inputProcessor.processInput("load " + gameStateFileName, this);
		}

		isRunning = false;
	}
	
	public void run() {
		inputProcessor.processInput("look", this);

		isRunning = true;
		Scanner scanner = new Scanner(System.in);
		
		// Game loop.
		while (isRunning) {
			System.out.print("> ");
			String input = scanner.nextLine();
			inputProcessor.processInput(input, this);
		}

		scanner.close();
	}

	public World getWorld() {
		return world;
	}
	
	public InputProcessor getInputProcessor() {
		return inputProcessor;
	}
}
