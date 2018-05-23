package pocketgems.mud;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pocketgems.mud.components.*;
import pocketgems.mud.exceptions.*;

/*
 * InputProcessor
 * ==============
 * Processes input, which is either entered into the command line during game play, or read
 * from a text file when loading a game state. An input usually consists of a single token
 * command, sometimes followed by arguments. For example, "go north".
 * 
 * Both world creation and game play are handled through inputs.
 */
public class InputProcessor {
	
	public void processInput(String input, Game game) {
		if (input.equals("")) {
			return;
		} else if (input.equals("exit")) {
			game.isRunning = false;
		} else {
			String regex = "\"([^\"]*)\"|(\\S+)";

			ArrayList<String> tokens = new ArrayList<String>();
			Matcher matcher = Pattern.compile(regex).matcher(input);
			while (matcher.find()) {
				if (matcher.group(1) != null) {
					tokens.add(matcher.group(1));
				} else {
					tokens.add(matcher.group(2));
				}
			}

			if (tokens.size() > 0) {
				String command = tokens.get(0);
				tokens.remove(0);
				
				try {
					if (!processCommand(command, tokens, game)) {
						System.out.println("Unrecognized command: " + command);
						System.out.println("Type 'help' for a full list of commands");
						System.out.println();
					}
				} catch (EntityNotFoundException e) {
					System.out.println("The entity '" + e.getEntityId() + "' could not be understood");
					System.out.println();
				} catch (ComponentNotFoundException e) {
					IdentityComponent identityComponent = e.getEntity().getComponentOrNull(IdentityComponent.class);
					String entityId = identityComponent != null ? identityComponent.id : "<unknown>";
					String componentName = e.getComponentType().getSimpleName();
					System.out.println("The entity '" + entityId + "' is missing '" + componentName + "'");
					System.out.println();
				} catch (Exception exception) {
					System.out.println("Something unexpected happened processing command: " + command);
					System.out.println();
				}
			}
		}
	}

	protected boolean processCommand(String command, ArrayList<String> arguments, Game game)
		throws ComponentNotFoundException, EntityNotFoundException {
		World world = game.getWorld();
		
		// Print list of commands
		if (command.equals("help")) {
			if (arguments.size() > 0 && arguments.get(0).equals("admin")) {
				System.out.print("Create elements in the world with:\n  ");
				String[] adminCommands = {
					"createroom", "createexit", "creatething", "setname", "setdescription",
					"addexit", "setdestination", "addkeyword"
				};
				System.out.println(String.join("\n  ", adminCommands));
				System.out.println();
			} else {
				System.out.println("Useful commands:\n" +
					"  help                       This output\n" +
					"  movething <thing> <place>  Used to place an object in the world, including the player\n" +
					"  look [keyword]             Look at the current room or named object\n" +
					"  go <exit>                  Go to the named exit\n" +
					"  exit                       Quit the game\n" +
					"\ntry \"help admin\" for a list of world-building commands");
				System.out.println();
			}
		} else if (command.equals("load")) {
			try {
				FileReader fileReader = new FileReader(arguments.get(0));
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String input;
				while ((input = bufferedReader.readLine()) != null) {
					processInput(input, game);
				}

				bufferedReader.close();
			} catch (FileNotFoundException exception) {
				System.out.println("File not found: " + arguments.get(0));
			} catch (IOException exception) {
				System.out.println("Error reading file: " + arguments.get(0));
			}
		} else if (command.equals("createroom")) {
			Entity room = EntityFactory.createRoom();
			room.getIdentityComponent().id = arguments.get(0);
			world.AddEntity(room);
		} else if (command.equals("createexit")) {
			Entity exit = EntityFactory.createExit();
			exit.getIdentityComponent().id = arguments.get(0);
			world.AddEntity(exit);
		} else if (command.equals("creatething")) {
			Entity thing = EntityFactory.createThing();
			thing.getIdentityComponent().id = arguments.get(0);
			world.AddEntity(thing);
		} else if (command.equals("setname")) {
			Entity entity = world.GetEntity(arguments.get(0));
			entity.getDescriptionComponent().name = arguments.get(1);
		} else if (command.equals("setdescription")) {
			Entity entity = world.GetEntity(arguments.get(0));
			entity.getDescriptionComponent().description = arguments.get(1);
		} else if (command.equals("addexit")) {
			Entity room = world.GetEntity(arguments.get(0));
			Entity exit = world.GetEntity(arguments.get(1));
			room.getRoomComponent().exitIds.add(exit.getIdentityComponent().id);
		} else if (command.equals("setdestination")) {
			Entity exit = world.GetEntity(arguments.get(0));
			Entity room = world.GetEntity(arguments.get(1));
			exit.getPortalComponent().destinationRoomId = room.getIdentityComponent().id;
		} else if (command.equals("addkeyword")) {
			Entity entity = world.GetEntity(arguments.get(0));
			entity.getDescriptionComponent().keywords.add(arguments.get(1));
		} else if (command.equals("movething")) {
			Entity thing = world.GetEntity(arguments.get(0));
			Entity room = world.GetEntity(arguments.get(1));
			moveToRoom(world, thing, room.getIdentityComponent().id);
		} else if ((command.equals("look")) || (command.equals("l"))) {
			if (arguments.size() == 0) {
				Entity room = world.GetPlayer().getLocationComponent().room(world);
				if (room != null) {
					System.out.println(room.getDescriptionComponent().name);
					System.out.println();
					System.out.println(room.getDescriptionComponent().description);
					System.out.println();
	
					RoomComponent roomComponent = room.getRoomComponent();
					if (roomComponent.inhabitantIds.size() > 1) {
						// There is at least one thing here that isn't the player.
						System.out.println("You see the following:");
	
						for (String inhabitantId : roomComponent.inhabitantIds) {
							Entity inhabitant = world.GetEntity(inhabitantId);
							if (inhabitant != world.GetPlayer()) {
								System.out
										.println("  - " + inhabitant.getDescriptionComponent().description);
							}
						}
					}
					System.out.println();
	
					System.out.print("Exits:");
					for (String exitId : roomComponent.exitIds) {
						Entity exit = world.GetEntity(exitId);
						System.out.print(" " + exit.getDescriptionComponent().name);
					}
					System.out.println();
				} else {
					System.out.println("You float in the void. Join a room with the command 'movething player <room-id>'.");
				}
				
				System.out.println();
			} else {
				Entity target = entityInRoom(world, arguments.get(0));
				if (target == null) {
					System.out.println("You do not see that here.");
				} else {
					System.out.println("You look at the " + target.getDescriptionComponent().name + ".");
				}
				
				System.out.println();
			}
		} else if ((command.equals("go")) || (command.equals("move"))) {
			Entity room = world.GetPlayer().getLocationComponent().room(world);
			if (room != null) {
				RoomComponent roomComponent = room.getRoomComponent();
				for (String exitId : roomComponent.exitIds) {
					Entity exit = world.GetEntity(exitId);
					if (exit.getDescriptionComponent().keywords.contains(arguments.get(0))) {
						moveToRoom(world, world.GetPlayer(), exit.getPortalComponent().destinationRoomId);
						processInput("look", game);
						return true;
					}
				}
			}
			// Room not found
			throw new EntityNotFoundException(arguments.get(0));
		} else {
			return false;
		}
		
		return true;
	}

	protected void moveToRoom(World world, Entity entity, String destinationRoomId)
			throws ComponentNotFoundException, EntityNotFoundException {
		IdentityComponent identityComponent = entity.getIdentityComponent();
		LocationComponent locationComponent = entity.getLocationComponent();

		Entity room = locationComponent.room(world);
		if (room != null) {
			room.getRoomComponent().inhabitantIds.remove(identityComponent.id);
		}

		locationComponent.roomId = destinationRoomId;

		RoomComponent destinationRoomComponent = world.GetEntity(destinationRoomId).getRoomComponent();
		destinationRoomComponent.inhabitantIds.add(identityComponent.id);
	}

	

	protected Entity entityInRoom(World world, String keyword)
			throws ComponentNotFoundException, EntityNotFoundException {
		Entity room = world.GetPlayer().getLocationComponent().room(world);

		// Make sure the player is in a room
		for (String inhabitantId : room.getRoomComponent().inhabitantIds) {
			Entity inhabitant = world.GetEntity(inhabitantId);
			DescriptionComponent descriptionComponent = inhabitant.getDescriptionComponent();
			if (descriptionComponent != null) {
				if (descriptionComponent.keywords.contains(keyword)) {
					return inhabitant;
				}
			}
		}

		throw new EntityNotFoundException(keyword);
	}
}
