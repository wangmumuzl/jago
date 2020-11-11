/**
 * 
 */
package entity;

/**
 * @author ÷Ï¡’
 *
 */
public class Game {
	private int gameId;
	private String whiteName;
	private String blackName;
	private String resultPath;
	private String date;
	private String winerName;
	private int step;
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getWhiteName() {
		return whiteName;
	}
	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}
	public String getBlackName() {
		return blackName;
	}
	public void setBlackName(String blackName) {
		this.blackName = blackName;
	}
	public String getResultPath() {
		return resultPath;
	}
	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWinerName() {
		return winerName;
	}
	public void setWinerName(String winerName) {
		this.winerName = winerName;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	
}
