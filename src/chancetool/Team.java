package chancetool;

import java.util.ArrayList;
import java.util.List;

public class Team {
	
	private String name_of_team;
	private ArrayList<String> pastPlayers;
	private ArrayList<String> activePlayers;
	private Double rating;
	
	/**
	 * CONSTRUCTOR
	 * @param teamname
	 * @param Players
	 */
	public Team(String teamname, ArrayList<String> Players)
	{
		name_of_team = teamname;
		activePlayers = new ArrayList<String>(Players);
		pastPlayers = new ArrayList<String>();
		rating = 0.0;
	}
	
	public Team()
	{
		activePlayers = new ArrayList<String>();
		pastPlayers = new ArrayList<String>();
		rating = 0.0;
	}
	
	public void addTeamName(String teamname)
	{
		name_of_team = teamname;
	}
	
	public void addTeamPlayer(String playername)
	{
		activePlayers.add(playername);
	}
	
	public String getName()
	{
		return name_of_team;
	}
	
	/**
	 * 
	 * @return List of team Players
	 */
	public ArrayList<String> getPlayers()
	{
		return activePlayers;
	}

	/**
	 * 
	 * @return Team Player in position 1
	 */
	public String getPlayerOne()
	{
		return activePlayers.get(0);
	}
	
	/**
	 * 
	 * @return Team Player in position 2
	 */
	public String getPlayerTwo()
	{
		return activePlayers.get(1);
	}
	
	/**
	 * 
	 * @return Team Player in position 3
	 */
	public String getPlayerThree()
	{
		return activePlayers.get(2);
	}
	
	/**
	 * 
	 * @return Team Player in position 4
	 */
	public String getPlayerFour()
	{
		return activePlayers.get(3);
	}
	
	/**
	 * 
	 * @return Team Player in position 5
	 */
	public String getPlayerFive()
	{
		return activePlayers.get(4);
	}
}
