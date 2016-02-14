package chancetool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Event 
{	
	private String event_name;
	private ArrayList<Team> participating_teams;
	private ArrayList<Team> group_a;
	private ArrayList<Team> group_b;
	private ArrayList<Team> group_c;
	private ArrayList<Team> group_d;
	
	private HashMap<String, ArrayList<Game>> games;
	
	public Event(String name_of_event)
	{
		event_name = name_of_event;
		group_a = new ArrayList<Team>();
		group_b = new ArrayList<Team>();
		group_c = new ArrayList<Team>();
		group_d = new ArrayList<Team>();
		
		games = new HashMap<String, ArrayList<Game>>();
	}
	
	/**
	 * Overloaded constructor
	 */
	public Event()
	{
		group_a = new ArrayList<Team>();
		group_b = new ArrayList<Team>();
		group_c = new ArrayList<Team>();
		group_d = new ArrayList<Team>();
		
		games = new HashMap<String, ArrayList<Game>>();
	}
	
	public void setEventName(String name_of_event)
	{
		event_name = name_of_event;
	}
	
	public void setGroup(String group, ArrayList<Team> teams)
	{
		switch (group)
		{
		case "a":	group_a = teams;
					break;
		case "A":	group_a = teams;
					break;
		case "b":	group_b = teams;
					break;
		case "B":	group_b = teams;
					break;
		case "c":	group_c = teams;
					break;		
		case "C":	group_c = teams;
					break;		
		case "d":	group_d = teams;
					break;		
		case "D":	group_d = teams;
					break;	
		default: 	break;
		}
	}
	
	public void setTeams(ArrayList<Team> teams_to_add)
	{
		participating_teams = teams_to_add;
	}
	
	/**
	 * 
	 * @param stage String detailing which stage the games belong to
	 * 		  values are: a, b, c, d, post_group
	 * @param games_to_add
	 */
	public void addGames(String stage, ArrayList<Game> games_to_add)
	{
		games.put(stage, games_to_add);
	}
	

	/**
	 * 
	 * @return
	 */
	public String getEventName()
	{
		return event_name;
	}
	
	/**
	 * 
	 * @param group_name Name of group to return a,b,c or d
	 * @return
	 */
	public ArrayList<Team> getGroup(String group_name)
	{
		ArrayList<Team> to_return = group_a;
		
		switch (group_name)
		{
		case "a":	to_return = group_a;
					break;
		case "A":	to_return = group_a;
					break;
		case "b":	to_return = group_b;
					break;
		case "B":	to_return = group_b;
					break;
		case "c":	to_return = group_c;
					break;		
		case "C":	to_return = group_c;
					break;		
		case "d":	to_return = group_d;
					break;		
		case "D":	to_return = group_d;
					break;
		default: 	break;
		}
		
		return to_return;
	}
	
	/**
	 * 
	 * @return ArrayList containing teams that are participating
	 */
	public ArrayList<Team> getTeams()
	{
		return participating_teams;
	}
	
	/**
	 * 
	 * @param which_stage Determines which games you want to be returned, group_[a/b/c/d] or post-group
	 * @return
	 */
	public ArrayList<Game> getGames(String which_stage)
	{
		return games.get(which_stage);
	}
}
