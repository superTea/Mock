package pocketgems.mud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pocketgems.mud.components.DescriptionComponent;
import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.components.PortalComponent;
import pocketgems.mud.components.RoomComponent;

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
				
				if (!processCommand(command, tokens, game)) {
					System.out.println("Unrecognized command: " + command);
					System.out.println();
				}
			}
		}
	}

	protected boolean processCommand(String command, ArrayList<String> arguments, Game game) {
		World world = game.getWorld();
		
		if (command.equals("load")) {
			try {
				FileReader fileReader = new FileReader(arguments.get(0));
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				String input;
				while ((input = bufferedReader.readLine()) != null) {
					processInput(input, game);
				}

				bufferedReader.close();
			} catch (IOException exception) {
				System.out.println(exception);
			}
		} else if (command.equals("createroom")) {
			Entity room = EntityFactory.createRoom();
			room.getComponent(IdentityComponent.class).id = arguments.get(0);
			world.AddEntity(room);
		} else if (command.equals("createexit")) {
			Entity exit = EntityFactory.createExit();
			exit.getComponent(IdentityComponent.class).id = arguments.get(0);
			world.AddEntity(exit);
		} else if (command.equals("creatething")) {
			Entity thing = EntityFactory.createThing();
			thing.getComponent(IdentityComponent.class).id = arguments.get(0);
			world.AddEntity(thing);
		} else if (command.equals("setname")) {
			Entity entity = world.GetEntity(arguments.get(0));
			DescriptionComponent descriptionComponent = entity.getComponent(DescriptionComponent.class);
			descriptionComponent.name = arguments.get(1);
		} else if (command.equals("setdescription")) {
			Entity entity = world.GetEntity(arguments.get(0));
			DescriptionComponent descriptionComponent = (DescriptionComponent) entity
					.getComponent(DescriptionComponent.class);
			descriptionComponent.description = arguments.get(1);
		} else if (command.equals("addexit")) {
			Entity room = world.GetEntity(arguments.get(0));
			Entity exit = world.GetEntity(arguments.get(1));
			room.getComponent(RoomComponent.class).exitIds.add(exit.getComponent(IdentityComponent.class).id);
		} else if (command.equals("setdestination")) {
			Entity exit = world.GetEntity(arguments.get(0));
			Entity room = world.GetEntity(arguments.get(1));
			exit.getComponent(PortalComponent.class).destinationRoomId = room.getComponent(IdentityComponent.class).id;
		} else if (command.equals("addkeyword")) {
			Entity entity = world.GetEntity(arguments.get(0));
			DescriptionComponent descriptionComponent = (DescriptionComponent) entity
					.getComponent(DescriptionComponent.class);
			descriptionComponent.keywords.add(arguments.get(1));
		} else if (command.equals("movething")) {
			Entity thing = world.GetEntity(arguments.get(0));
			Entity room = world.GetEntity(arguments.get(1));
			moveToRoom(world, thing, room.getComponent(IdentityComponent.class).id);
		} else if ((command.equals("look")) || (command.equals("l"))) {
			if (arguments.size() == 0) {
				Entity room = world.GetPlayer().getComponent(LocationComponent.class).room(world);
				if (room != null) {
					System.out.println(room.getComponent(DescriptionComponent.class).name);
					System.out.println();
					System.out.println(room.getComponent(DescriptionComponent.class).description);
					System.out.println();
	
					RoomComponent roomComponent = room.getComponent(RoomComponent.class);
					if (roomComponent.inhabitantIds.size() > 1) {
						// There is at least one thing here that isn't the player.
						System.out.println("You see the following:");
	
						for (String inhabitantId : roomComponent.inhabitantIds) {
							Entity inhabitant = world.GetEntity(inhabitantId);
							if (inhabitant != world.GetPlayer()) {
								System.out
										.println("  - " + inhabitant.getComponent(DescriptionComponent.class).description);
							}
						}
					}
					System.out.println();
	
					System.out.print("Exits:");
					for (String exitId : roomComponent.exitIds) {
						Entity exit = world.GetEntity(exitId);
						System.out.print(" " + exit.getComponent(DescriptionComponent.class).name);
					}
					System.out.println();
				} else {
					System.out.println("You float in the void. Join a room with the command 'movething player <room-id>'.");
				}
				
				System.out.println();
			} else {
				Entity target = EntityInRoom(world, arguments.get(0));
				if (target == null) {
					System.out.println("You do not see that here.");
				} else {
					DescriptionComponent descriptionComponent = target.getComponent(DescriptionComponent.class);
					System.out.println("You look at the " + descriptionComponent.name + ".");
				}
				
				System.out.println();
			}
		} else if ((command.equals("go")) || (command.equals("move"))) {
			Entity room = world.GetPlayer().getComponent(LocationComponent.class).room(world);
			if (room != null) {
				RoomComponent roomComponent = room.getComponent(RoomComponent.class);
				for (String exitId : roomComponent.exitIds) {
					Entity exit = world.GetEntity(exitId);
					if (exit.getComponent(DescriptionComponent.class).keywords.contains(arguments.get(0))) {
						moveToRoom(world, world.GetPlayer(), exit.getComponent(PortalComponent.class).destinationRoomId);
						processInput("look", game);
						break;
					}
				}
			}
		} else {
			return false;
		}
		
		return true;
	}

	protected void moveToRoom(World world, Entity entity, String destinationRoomId) {
		IdentityComponent identityComponent = entity.getComponent(IdentityComponent.class);
		LocationComponent locationComponent = entity.getComponent(LocationComponent.class);

		Entity room = locationComponent.room(world);
		if (room != null) {
			room.getComponent(RoomComponent.class).inhabitantIds.remove(identityComponent.id);
		}

		locationComponent.roomId = destinationRoomId;

		RoomComponent destinationRoomComponent = world.GetEntity(destinationRoomId).getComponent(RoomComponent.class);
		destinationRoomComponent.inhabitantIds.add(identityComponent.id);
	}

	

	protected Entity EntityInRoom(World world, String keyword) {
		Entity room = world.GetPlayer().getComponent(LocationComponent.class).room(world);
		for (String inhabitantId : room.getComponent(RoomComponent.class).inhabitantIds) {
			Entity inhabitant = world.GetEntity(inhabitantId);
			DescriptionComponent descriptionComponent = inhabitant.getComponent(DescriptionComponent.class);
			if (descriptionComponent != null) {
				if (descriptionComponent.keywords.contains(keyword)) {
					return inhabitant;
				}
			}
		}

		return null;
	}
}
