package mainGui;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	DashBoardPage dashBoardPage = new DashBoardPage();
            	
            }
        });
		
	}	
	
}
