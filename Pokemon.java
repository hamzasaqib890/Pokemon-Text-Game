/**
 * Pokemon.java
 *
 * Contains the Pokemon class which stores Pokemon for PokemonArena.java game. Includes useful methods which
 * allow Pokemon to attack one another, use special abilities, display stats etc.
 *
 * Hamza Saqib
 */

class Pokemon {
	public static final String[] types = {"None", "Earth", "Fire", "Grass", "Water", "Fighting", "Electric"}; // Pokemon types
	private String name; // Pokemon name
	private int maxhp, hp, type, res, weak; // different variables for Pokemon stats
	private int energy = 50; // max energy
	private int disabled = 0; // used to see if a Pokemon is disabled
	private boolean stunned = false; // stunned boolean
	private Attack[] attacks; // used to store the Pokemon's attacks
	
	
	public Pokemon(String stats){
		// contsructor method for Pokemon class. Takes in a single string and splits it by "," and
		// goes through the contents and sets the correct variables by order
		String[] statsArray = stats.split(",");
		name = statsArray[0];
		maxhp = Integer.parseInt(statsArray[1]);
		hp = maxhp; // Pokemon starts with max HP
		for (int i = 0; i < 7; i++){
			if (types[i].toLowerCase().equals(statsArray[2].toLowerCase())){
				type = i; // looping through Pokemon types and checking if there is a match
			}
		}
		for (int i = 0; i < 7; i++){
			if (types[i].toLowerCase().equals(statsArray[3].toLowerCase())){
				res = i; // resistance type
			}
		}
		for (int i = 0; i < 7; i++){
			if (types[i].toLowerCase().equals(statsArray[4].toLowerCase())){
				weak = i; // weakness type
			}
		}
		attacks = new Attack[Integer.parseInt(statsArray[5])]; // array of attacks
		int c = 6; // used to keep track of the index as the program loops through the rest of the array
		for (int i = 0; i < Integer.parseInt(statsArray[5]); i++){
			// creating each Attack object and adding to attacks array
			attacks[i] = new Attack(statsArray[c], statsArray[c + 1], statsArray[c + 2], statsArray[c + 3]);
			c += 4;
		}
	}
	
	//a bunch of getter and setter methods for the variables
	public String getName(){
		return name;
	}
	
	public int getHp(){
		return hp;
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public int getType(){
		return type;
	}
	
	public int getRes(){
		return res;
	}
	
	public int getWeak(){
		return weak;
	}
	
	public Attack getAttack(int n){
		return attacks[n];
	}
	
	public int attacksNum(){
		// returns the number of attacks the Pokemon has
		return attacks.length;
	}
	
	public boolean getStunned(){
		return stunned;
	}
	
	public void setStunned(boolean stun){
		stunned = stun;
	}
	
	public boolean useAttack(int num, Pokemon pokemon, String playerName, boolean wildStorm){
		// method which allows a Pokemon to use an attack on another Pokemon
		int attackCost = attacks[num - 1].getCost(); // cost of the attack
		int damage = Math.max(attacks[num - 1].getDamage() - disabled, 0); // attack damage with a minimum of 0 damage (-10 damage if Pokemon is disabled)
		if (wildStorm || attackCost <= energy){ // no energy required if its an attack that was activated by the wild storm special
			if (!wildStorm){
				energy -= attackCost;
				System.out.printf("%s: %s, use %s!\n", playerName, name, attacks[num - 1].getName());
			}
			if (attacks[num - 1].getSpecial() == 1){
				// stun special
				pokemon.attack(damage, type); // attack function is called with the attack damage and the Pokemon type
				if (Math.random() < 0.5){
					pokemon.setStunned(true); // 50% chance of enemy Pokemon being stunned
					System.out.printf("%s is stunned!\n", pokemon.getName());
				}
				return true;
			}
			else if (attacks[num - 1].getSpecial() == 2){
				// wild card special
				if (Math.random() < 0.5){ // 50% chance of sucessful attack
					pokemon.attack(damage, type);
				}
				else{
					System.out.println("The attack failed!");
				}
				return true;
			}
			else if (attacks[num - 1].getSpecial() == 3){
				// wild storm special
				if (Math.random() < 0.5){
					// 50% chance of wild storm being activated
					pokemon.attack(damage, type);
					System.out.printf("Wild storm activated! %s uses %s again!\n", name, attacks[num - 1].getName());
					useAttack(num, pokemon, playerName, true); // useAttack method being called with wildStorm boolean set to true
				}
				else{
					System.out.println("The attack failed!");
				}
				return true;
			}
			else if (attacks[num - 1].getSpecial() == 4){
				// disable special
				pokemon.attack(damage, type);
				pokemon.disable(); // disables enemy pokemon
				return true;
			}
			else if (attacks[num - 1].getSpecial() == 5){
				// recharge special
				pokemon.attack(damage, type);
				recharge(20); // grants 20 energy
				System.out.printf("%s used Recharge and gained 20 Energy!\n", name);
				return true;
			}
			else{
				// no special
				pokemon.attack(damage, type);
				return true;
			}
		}
		else{
			return false;
		}
	}
	
	public void attack(int damage, int type){
		// attack method called by the Pokemon being attacked. subtracts correct amount of HP from the Pokemon
		if (damage != 0){ // prevents the computer from outputting "0 damage points inflicted onto [Pokemon]!"
			if (type != 0 && type == res){
				// Pokemon is resistant
				hp -= damage/2; // half damage
				System.out.printf("It's not very effective! %d damage points inflicted onto %s!\n", damage / 2, name);
			}
			else if (type != 0 && type == weak){
				hp -= damage*2; // double damage
				System.out.printf("It's super effective! %d damage points inflicted onto %s!\n", damage * 2, name);
			}
			else{
				// normal damage if resistance and weakness don't match
				hp -= damage;
				System.out.printf("%d damage points inflicted onto %s!\n", damage, name);
			}
		}
	}
	
	public void heal(int amount){
		// method which is used to grant a Pokemon HP and ensures that it doesn't surpass the max HP for that Pokemon
		hp = Math.min(hp + amount, maxhp);
	}
	
	public void recharge(int amount){
		// method which is used to grant a Pokemon energy with a maximum of 50 energy
		energy = Math.min(energy + amount, 50);
	}
	
	public void displayStats(){
		// method which displays the stats for the Pokemon in a cleanly formatted look
		System.out.println(name); // name
		System.out.printf("%-25s%s\n", "HP: " + hp, "Energy: " + energy); // HP and energy
		System.out.printf("%-25s%s\n", "Type: " + types[type], "Resistance: " + types[res]); // type and resistance
		System.out.printf("%-25sAttacks: ", "Weakness: " + types[weak]); // weakness
		for (int i = 0; i < attacks.length; i++){
			// prints all the attacks for that Pokemon (with commas)
			System.out.print(attacks[i].getName());
			if (i < attacks.length - 1){
				System.out.print(", ");
			}
			else{
				System.out.println("");
			}
		}
	}
	
	public void disable(){
		// method called to disable a Pokemon
		if (disabled == 0){ // if not already disabled
			System.out.printf("%s has been disabled!\n", name);
			disabled = 10; // set to 10 since that's how much is subtract from the attack damage
		}
	}
}