package com.uestc.Indoorguider;

/**
 * manager the user data and preference
 */
public abstract class IndoorGuiderManagerModel {
	public abstract String getUsername();
	public abstract String getPassword();
	public abstract boolean setUsername(String username);
	public abstract boolean setPassword(String password);
	public abstract boolean setScore(int score);
	public abstract int getScore();
    // �����½��Ϣ
    public abstract boolean saveAlreadyLogin(boolean alreadyLogin);
    // ��ȡ��½��Ϣ
    public abstract boolean getAlreadyLogin();
    
	
	
	
	
	

}
