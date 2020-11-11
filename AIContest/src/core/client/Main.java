package core.client;

import core.match.GameEvent;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {

    	GameEvent oucChampion = new GameEvent();
    	
    	//oucChampion.addPlayer(new g02.player.AI());   		//�ų���
    	oucChampion.addPlayer(new baseline.player.AI());		//�콨��
    	//oucChampion.addPlayer(new s17020031086.player.AI()); 	//�����   	
    	oucChampion.addPlayer(new s17020031094.player.AI());	//��־��    		
    	//oucChampion.addPlayer(new g17020031102.player.AI());	//���
    	//oucChampion.addPlayer(new s17020032009.player.AI());	//�Ȼ
    	oucChampion.addPlayer(new s17020032011.player.AI());	//������
    	oucChampion.arrangeMatches(3);
    	
    	oucChampion.run();
    	
    	oucChampion.showResults();
    }
}
