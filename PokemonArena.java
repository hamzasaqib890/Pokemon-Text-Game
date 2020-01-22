/**
 * PokemonArena.java
 * 
 * Simple text-based Pokemon game. User starts by choosing 4 Pokemon and the rest of the game is a series of battles
 * where the user's Pokemon fight against the remaining Pokemon which are randomly controlled by the computer.
 * The game ends when eiter side loses all their Pokemon.
 *
 * Hamza Saqib
 */
 
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PokemonArena {
	private static Scanner kb = new Scanner(System.in); // for input
	private static ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>(); // enemy Pokemon will be stored here
	private static ArrayList<Pokemon> playerPoke = new ArrayList<Pokemon>(); // player Pokemon
	private static String name; //player name
	
	public static void main(String[] args) {
		//main class which calls the necessary methods in the correct order until the game has concluded
    	logo("logo.txt"); // check method comments
    	load("pokemon.txt");
    	setName();
    	chooseFour();
    	while (playerPoke.size() != 0 && pokemons.size() != 0){ // keeps the game running until either side has no Pokemon
    		battle(); // method comment
    		for (Pokemon poke : playerPoke){
    			poke.heal(20); // user Pokemon get healed 20 HP at the end of each battle
    		}
    		if (playerPoke.size() != 0 && pokemons.size() != 0){
    			// prevents computer from displaying this message unnecessarily right before the game concludes
    			System.out.println("All of your Pokemon that are awake have been granted 20 HP!");
    		}
    	}
    	if (playerPoke.size() > 0){
    		// user wins
    		System.out.printf("Congratulations %s! You beat all of the enemy Pokemon! You are now Trainer Supreme!", name);
    	}
    	else{
    		// user loses
    		System.out.println("git gud skrub");
    	}    	
    }
	
	public static void setName(){
		// welcome message and gets the user's name
		System.out.println("Welcome to Pokemon Arena! What's your name?");
		name = kb.nextLine();
		printLine(); // method comment
	}
	
    public static boolean load(String fileName){
    	// loads the data file which contains all the stats for each Pokemon
    	try{
	    	Scanner inFile = new Scanner(new BufferedReader(new FileReader(fileName)));
	    	int n = Integer.parseInt(inFile.nextLine());
	    	while (inFile.hasNextLine()){
	    		// creating each Pokemon object and adding to pokemons array list
	    		pokemons.add(new Pokemon(inFile.nextLine()));
	    	}
	    	return true;
    	}
    	catch(IOException ex){
    		// no data file error
    		System.out.println("Data file not found.");
    		return false;
    	}
    }
    
    public static boolean logo(String fileName){
    	// prints the Pokemon Arena logo from the .txt file
    	try{
    		Scanner inFile = new Scanner(new BufferedReader(new FileReader(fileName)));
    		while(inFile.hasNextLine()){
    			// loops thorugh each line and prints it
    			System.out.println(inFile.nextLine());
    		}
    		return true;
    	}
    	catch(IOException ex){
    		System.out.println("Data file not found.");
    		return false;
    	}
    }
    
    public static void chooseFour(){
    	// allows the intial choosing of the 4 Pokemon by the user
    	displayPokemon(); // method comment
    	String inp; // used to store the input
    	System.out.printf("Hello %s, start by choosing four Pokemon of your choice from the list above. ", name);
    	ArrayList<Integer> chosen = new ArrayList<Integer>(); // array list to store the selected pokemon temporarily
		while (playerPoke.size() < 4){
			// keeps running until the user has chosen his/her 4 pokemon
			System.out.printf("Select a Pokemon (1 - %d) (%d/4):\n", pokemons.size(), playerPoke.size() + 1);
			boolean error = false; // boolean used to print error message later if required
			int n = -1; // also used to store input, default value of -1
			try{
				n = Integer.parseInt(kb.nextLine());
				n--;
			}
			catch (NumberFormatException ex){
				error = true;
			}
			printLine();
			if (n <= pokemons.size() - 1 && n >= 0 && !(chosen.contains(n))){
				// input n has to be within the range of the number of Pokemon and couldn't have been chosen yet
				pokemons.get(n).displayStats();
				System.out.printf("Would you like to choose this Pokemon?\n(1) Choose %s\n(2) Select another Pokemon\n", pokemons.get(n).getName());
				// Pokemon selection confirmation
				while (true){
					// while loops allows user to enter as many incorrect inputs until they enter a valid one
					inp = kb.nextLine();
					printLine();
					if (isInt(inp) && Integer.parseInt(inp) == 1){
						playerPoke.add(pokemons.get(n));
						chosen.add(n);
						break;
					}
					else if(isInt(inp) && Integer.parseInt(inp) == 2){
						break;
					}
					else{
						System.out.println("Please choose option (1) or (2)!");
					}
				}
			}
			else{
				error = true;
			}
			
			if (error == true){
				// error message
				System.out.println("Please enter a number between 1 and 28 that hasn't been chosen yet!");
			}
		}
		Collections.sort(chosen);
		for (int i = 3; i >= 0; i--){
			// going backwards through the sorted chosen array list so there is no complications with the index
			pokemons.remove((int) chosen.get(i));
		}
		System.out.print("You chose");
		for (int i = 0; i < playerPoke.size(); i ++){
			System.out.print(" " + playerPoke.get(i).getName());
			if (i == playerPoke.size() - 1){
				System.out.println(".");
			}
			else if (i == playerPoke.size() - 2){
				System.out.print(" and");
			}
			else{
				System.out.print(",");
			}
		}
		
    }
    
    public static void displayPokemon(){
    	// Called upon at the beginning to display all the Pokemon in order with an index so the user can easily choose
    	for (int i = 1; i < pokemons.size() + 1; i += 3){
    		if (pokemons.size() - i >= 3){
    			// prints 3 Pokemon per row
    			System.out.printf("%-30s%-30s%-30s\n", i + ". " + pokemons.get(i - 1).getName(), i + 1 + ". " + pokemons.get(i).getName(), i + 2 + ". " + pokemons.get(i + 1).getName());
    		}
    		else if (pokemons.size() - i == 2){
    			// last 2 Pokemon (if needed)
    			System.out.printf("%-30s%-30s\n", i + ". " + pokemons.get(i - 1).getName(), i + 1 + ". " + pokemons.get(i).getName());
    		}
    		else{
    			// last pokemon (id needed)
    			System.out.println(i + ". " + pokemons.get(i - 1).getName());
    		}
    	}
    }
    
    public static int choose(){
    	// method which allows user to choose a Pokemon to fight
    	String inp;
    	String selected;
    	int selectedInt;
    	int chosen = -1;
    	boolean breakFlag = false;
    	for (int i = 1; i < playerPoke.size() + 1; i ++){
    		// prints all currently available Pokemon for the user
	    	System.out.println(i + ". " + playerPoke.get(i - 1).getName());
	    }
    	while (true){
	    	System.out.printf("Select a Pokemon (1 - %d):\n", playerPoke.size());
	    	selected = kb.nextLine();
	    	printLine();
	    	if (isInt(selected) && Integer.parseInt(selected) >= 1 && Integer.parseInt(selected) <= playerPoke.size()){
	    		selectedInt = Integer.parseInt(selected) - 1;
	    		playerPoke.get(selectedInt).displayStats(); // displays stats for selected Pokemon
	    		System.out.println("Choose this Pokemon to fight (1)"); // confirming selection
	    		System.out.println("Select another (2)");
	    		
	    		while (true){
	    			inp = kb.nextLine();
	    			printLine();
	    			if (isInt(inp) && Integer.parseInt(inp) == 1){
	    				// if user confirms
	    				chosen = selectedInt; // chosen variable used to show user's selected Pokemon
	    				breakFlag = true;
	    				break;
	    			}
	    			else if (isInt(inp) && Integer.parseInt(inp) == 2){
	    				// user selects another
	    				for (int i = 1; i < playerPoke.size() + 1; i ++){
	    					System.out.println(i + ". " + playerPoke.get(i - 1).getName());
	    				}
	    				break;
	    			}
	    			else{
	    				// invalid input
	    				System.out.print("Please choose option (1) or (2)!");
	    			}	    			
	    		}
	    	}
	    	else{
	    		System.out.println("Please select a valid Pokemon!");
	    	}
	    	
	    	if (breakFlag){
	    		break;
	    	}
    	}
    	System.out.printf("%s: %s, I choose you!\n", name, playerPoke.get(chosen).getName());
	    return chosen;
    }
    
    public static void battle(){
    	// method which allows two Pokemon to battle
    	int selectedPoke = choose(); // user selects a Pokemon to fight
    	int selectedAttack; // used later to store attack
    	boolean breakFlag = false;
    	boolean pass = false; // boolean pass variable to pass certain if statements if needed
    	Set<Integer> enemyAttacks = new HashSet<Integer>(); // used to store attacks that the enemy has selected
    	int enemyAttack;
    	String inp;
    	int enemy = ThreadLocalRandom.current().nextInt(0, pokemons.size()); // randomly choosing enemy Pokemon
    	System.out.println("The enemy chose " + pokemons.get(enemy).getName());
    	boolean turn = Math.random() < 0.5; // randomly deciding who starts
    	while (true){
	    	if (turn){
	    		// user's turn
	    		while (true){
	    			breakFlag = false; // default value
	    			if (!pass){
	    				// pass variable ensures too much isn't printed unnecessarily
	    				// displays the user's Pokemon
	    				System.out.printf("%s, it is your turn! Your selected Pokemon:\n", name);
	    				playerPoke.get(selectedPoke).displayStats();
	    			}
	    			pass = false; // default
	    			System.out.print("Would you like to:\n(1) Attack\n(2) Retreat\n(3) View enemy Pokemon\n(4) Pass\n");
	    			inp = kb.nextLine();
	    			printLine();
	    			if (isInt(inp) && Integer.parseInt(inp) == 1){
	    				// if user attacks
	    				if (playerPoke.get(selectedPoke).getStunned() == true){
	    					// if Pokemon is stunned
	    					System.out.println("Your Pokemon can't attack since it is stunned!");
	    					pass = true;
	    				}
	    				else{
	    					System.out.println("Which attack would you like to perform?");
		    				for (int i = 0; i < playerPoke.get(selectedPoke).attacksNum(); i++){
		    					// printing attacks and their stats
				    			playerPoke.get(selectedPoke).getAttack(i).displayStats(i);
			    			}
			    			System.out.printf("(%d) Go back\n", playerPoke.get(selectedPoke).attacksNum() + 1); // additional option to allow user to go back
			    			while (true){
			    				inp = kb.nextLine();
			    				printLine();
			    				if (isInt(inp) && Integer.parseInt(inp) >= 1 && Integer.parseInt(inp) <= playerPoke.get(selectedPoke).attacksNum()){
			    					// user selects an attack
			    					if (playerPoke.get(selectedPoke).useAttack(Integer.parseInt(inp), pokemons.get(enemy), name, false)){
			    						// if user's Pokemon has enough energy to perform the attack
			    						breakFlag = true;
			    						break;
			    					}
			    					else{
			    						System.out.println("You don't have enough energy to use this attack!");
			    					}
			    				}
			    				else if(isInt(inp) && Integer.parseInt(inp) == playerPoke.get(selectedPoke).attacksNum() + 1){
			    					// user goes back
			    					pass = true;
			    					break;
			    				}
			    				else{
			    					// invalid input
			    					System.out.println("Please choose a valid option!");
			    				}
			    			}
	    				}
	    			}
	    			else if (isInt(inp) && Integer.parseInt(inp) == 2){
	    				// retreat
	    				if (playerPoke.get(selectedPoke).getStunned() == true){
	    					// if Pokemon is stunned
	    					System.out.println("Your Pokemon can't retreat since it is stunned!");
	    					pass = true;
	    				}
	    				else{
	    					System.out.printf("%s retreated! Choose another Pokemon to fight!\n", playerPoke.get(selectedPoke).getName());
		    				int newSelect = choose(); // user chooses another Pokemon
		    				if (newSelect == selectedPoke){
		    					// if user chooses the same Pokemon again the loop doesn't get broken so it's still his/her turn
		    					System.out.println("You chose the same Pokemon so it's still your turn!");
		    				}
		    				else{
		    					// new user Pokemon
		    					selectedPoke = newSelect;
		    					break;
		    				}
	    				}
	    			}
	    			else if (isInt(inp) && Integer.parseInt(inp) == 3){
	    				// prints enemy Pokemon stats
	    				System.out.println("Enemy Pokemon:");
	    				pokemons.get(enemy).displayStats();
	    				pass = true;
	    			}
	    			else if (isInt(inp) && Integer.parseInt(inp) == 4){
	    				// pass turn
	    				System.out.printf("%s passed his turn!\n", name);
	    				playerPoke.get(selectedPoke).setStunned(false); // unstunning Pokemon
	    				break;
	    			}
	    			else{
	    				// invalid option
	    				System.out.println("Please choose a valid option!");
	    				pass = true;
	    			}
	    			if (breakFlag){
	    				break;
	    			}
	    		}
	    	}
	    	else{
	    		System.out.println("It's the enemy's turn!");
	    		if (pokemons.get(enemy).getStunned() == true){
	    			// computer passes its turn when its Pokemon is stunned
	    			System.out.println("The enemy passed his turn!");
	    			pokemons.get(enemy).setStunned(false); // unstunning Pokemon
	    		}
    			else{
    				while (true){
    					enemyAttack = ThreadLocalRandom.current().nextInt(0, pokemons.get(enemy).attacksNum()); // choosing random attack
		    			if (pokemons.get(enemy).useAttack(enemyAttack + 1, playerPoke.get(selectedPoke), "Enemy", false)){
		    				// if Pokemon can afford enough energy to perform the attack
		    				break;
		    			}
		    			else{
		    				// if Pokemon doesn't have not enough energy, attack is added to a set
		    				enemyAttacks.add(enemyAttack);
		    			}
		    			if (enemyAttacks.size() == pokemons.get(enemy).attacksNum()){
		    				// if the size of the set reaches total number of attacks the computer passes its turn
		    				System.out.println("The enemy passed his turn!");
		    				break;
		    			}
    				}
    			}
	    	}
	    	
	    	if (playerPoke.get(selectedPoke).getHp() <= 0){
	    		// if user Pokemon faints
	    		System.out.println("Your Pokemon fainted!");
	    		playerPoke.remove(selectedPoke); // removing from array list
	    		if (playerPoke.size() != 0){
	    			// prevents the computer from unnecessarily printing stuff right before the game concludes
	    			pokemons.get(enemy).recharge(10); // enemy Pokemon still gains 10 energy
	    			System.out.println("The enemy Pokemon gained 10 Energy!");
	    		}
	    		break;
	    	}
	    	else if(pokemons.get(enemy).getHp() <= 0){
	    		// if enemy Pokemon faints
	    		System.out.println("The enemy Pokemon fainted!");
	    		pokemons.remove(enemy);
	    		if (pokemons.size() != 0){
	    			playerPoke.get(selectedPoke).recharge(10);
	    			System.out.println("Your Pokemon gained 10 Energy!");
	    		}
	    		break;
	    	}
			
			pokemons.get(enemy).recharge(10); // both Pokemon gain 10 energy
	    	playerPoke.get(selectedPoke).recharge(10);
	    	System.out.println("Both Pokemon gained 10 Energy!");
	    	
	    	turn = !turn; // alternate turns
    	}
    }  
  
    public static boolean isInt(String str){
    	// method which checks if a string is an integer
    	for (int i = 0; i < str.length(); i++){
    		if (!Character.isDigit(str.charAt(i))) {
    			// if a character of the string isn't a character
            	return false;
        	}
    	}
    	
    	return str.length() > 0; // if the string isn't a blank string
    }
    
    public static void printLine(){
    	// method which prints a line made of dashes and is called after the user inputs something to seperate
    	// old input and output from new output
    	System.out.println("-------------------------------------------------");
    }	
    
}