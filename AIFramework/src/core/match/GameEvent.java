package core.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import core.game.GameResult;
import core.player.Player;

public class GameEvent {
	
	private HashMap<String, Integer> playerOrder = new HashMap<String, Integer>();
	
	public GameEvent() {
		// TODO Auto-generated constructor stub
	}

	public void addPlayer(Player player) {
		players.add(player);
		playerOrder.put(player.name(), players.size());
	}
	
	private ArrayList<Player> players = new ArrayList<>();
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public void arrangeMatches(int gameNumbers) {		
		//µ¥Ñ­»·
		int size = players.size();		
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				matches.add(new Match(gameNumbers, players.get(i), players.get(j)));
			}
		}
	}
	
	private ArrayList<Match> matches = new ArrayList<>();
	
	public void run() {
		Iterator<Match> itr = matches.iterator();
		ArrayList<Thread> games = new ArrayList<>();
		while (itr.hasNext()) {
			Match match = itr.next();
			match.process();

			games.addAll(match.getGames());
		}
		
		for (Thread thread : games) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void showResults() {
		// TODO Auto-generated method stub
		Collections.sort(players);
		for (Player player : players) {
			System.out.print(player.name() + "(" + player.scores() + "), ");
		}
		System.out.println();
		
    	for (Player player : players) {
    		System.out.println();
    		System.out.println(player.name() + "(" + player.scores() + ")");
    		for (GameResult result : player.gameResults()) {
    			System.out.println(result);
    		}
    		System.out.println(player.scores());		
    	}
	}
	
}
