package core.game;



public class Test {
	public static void main(String[] args) throws InterruptedException {
		Timer blackTimer=new Timer();
		Timer whiteTimer=new Timer();
		boolean flag=false;
		
		while(true) {
			if(flag) {
				blackTimer.restartTime();
				Thread.sleep(2000);
				blackTimer.stopTime();
				if(blackTimer.getCountTime()>5) {
					System.out.println("�ڷ���ʱ");
				}
				flag=false;
			}else {
				whiteTimer.restartTime();
				Thread.sleep(2000);
				whiteTimer.stopTime();
				if(whiteTimer.getCountTime()>5) {
					System.out.println("�׷���ʱ");
				}
				flag=true;
			}
		}
		
	}
}
