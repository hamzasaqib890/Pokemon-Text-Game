/**
 * Attack.java
 *
 * Contains the Attack class which stores attacks for Pokemon objects. Doesn't include many useful methods
 * but just makes storing Pokemon attacks easier.
 *
 * Hamza Saqib
 */


class Attack {
	private String name; // attack name
	private int cost, damage, special; // variables for different stats of the attack
	public static final String[] specials= {"None", "Stun", "Wild Card", "Wild Storm", "Disable", "Recharge"}; // attack specials
	
	public Attack(String name, String cost, String damage, String special){
		// simple contstructor method
		this.name  = name;
		this.cost = Integer.parseInt(cost);
		this.damage = Integer.parseInt(damage);
		for (int i = 0; i < 6; i++){
			if (specials[i].toLowerCase().equals(special.toLowerCase())){
				this.special = i; // checking if given special matches any of the specials
			}
		}
	}
	
	// a bunch of getter methods
	public String getName(){
		return name;
	}
	
	public int getCost(){
		return cost;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public int getSpecial(){
		return special;
	}
	
	public void displayStats(int i){
		// method used to display the stats for the attack in clean format
		// Takes in an integer i in order to number the attack for the user to choose
		System.out.printf("(%d) %-20s%-15s%-15s%s\n", i + 1, name, "Cost: " + cost, "Damage: " + damage, "Special: " + specials[special]);
	}
}