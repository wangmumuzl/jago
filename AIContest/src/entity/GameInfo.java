/**
 * 
 */
package entity;

/**
 * @author ÷Ï¡’
 *
 */
public class GameInfo {
	private int id;
	private String whiteName;
	private String blackName;
	private String resultPath;
	private String winerName;
	private String date;
	private int step;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getWinerName() {
		return winerName;
	}
	public void setWinerName(String winerName) {
		this.winerName = winerName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
}
