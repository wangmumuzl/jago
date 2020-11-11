package core.client;

import core.match.GameEvent;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {

    	GameEvent oucChampion = new GameEvent();
    	
    	//oucChampion.addPlayer(new g02.player.AI());   		//’≈≥˛∑Â
    	oucChampion.addPlayer(new baseline.player.AI());		//–ÏΩ®¡º
    	//oucChampion.addPlayer(new s17020031086.player.AI()); 	//—Óπ˙ˆŒ   	
    	oucChampion.addPlayer(new s17020031094.player.AI());	//‘¿÷æ∫∆    		
    	//oucChampion.addPlayer(new g17020031102.player.AI());	//’≈ËØ
    	//oucChampion.addPlayer(new s17020032009.player.AI());	//¿Ó≥¨»ª
    	oucChampion.addPlayer(new s17020032011.player.AI());	//≈À–«”Ó
    	oucChampion.arrangeMatches(3);
    	
    	oucChampion.run();
    	
    	oucChampion.showResults();
    }
}
