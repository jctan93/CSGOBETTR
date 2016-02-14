package chancetool;

public class Game 
{
	private Team A;
	private Team B;
	private String time;
	private int a_score;
	private int b_score;
	private int map_no;
	private String stage;
	
	//Max possible maps is 5, Best of 5 (BO5), however games are usually a BO3
	private Map one;
	private Map two;
	private Map three;
	private Map four;
	private Map five;
	
	private int numberofmaps;
	private boolean team_a_set;
	
	public Game()
	{
		numberofmaps = 0;
		team_a_set = false;
		map_no = 1;
	}
	
	/**
	 * 
	 * @param tem Team Object to be set
	 * @param teamname String deciding whether it's Team A or Team B
	 */
	public void setTeam(Team tem, String teamname)
	{
		if(teamname.equals("A") || teamname.equals("a"))
		{
			A = tem;
			team_a_set = true;
		}
		else if(teamname.equals("B") || teamname.equals("b"))
		{
			B = tem;
		}
	}
	
	public boolean teamABeenSet()
	{
		return team_a_set;
	}
	
	public void setTime(String taim)
	{
		time = taim;
	}
	
	/**
	 * 
	 * @param which_stage Enter a string: Group or post_group
	 */
	public void setStage(String which_stage)
	{
		stage = which_stage;
	}
	
	/**
	 * 
	 * @param score Score for a particular team
	 * @param team	Self explanatory
	 */
	public void setScore(int score, String team)
	{
		if(team.equals("A") || team.equals("a"))
		{
			a_score = score;
		}
		
		else if(team.equals("B") || team.equals("b"))
			b_score = score;
	}
	
	/**
	 * 
	 * @param map_toadd Map name
	 * @param number Map number out of 3/5, depending on whether it's a BO3/BO5
	 */
	public void setMap(Map map_toadd)
	{
		switch (map_no)
		{
			case 1:	one = map_toadd;
						numberofmaps++;
						break;
			case 2: two = map_toadd;
						numberofmaps++;
						break;
			case 3:three = map_toadd;
						numberofmaps++;
						break;
			case 4:four = map_toadd;
						numberofmaps++;
						break;
			case 5:five = map_toadd;
						numberofmaps++;
						break;	
			default: break;
		}
		
		map_no++;
	}
	
	public Map getMap(int number)
	{
		if (number == 1)
		{
			return one;
		}
		if (number == 2)
		{
			return two;
		}
		if (number == 3)
		{
			return three;
		}
		if (number == 4)
		{
			return four;
		}
		if (number == 5)
		{
			return five;
		}
		
		//WILL NEVER REACH HERE BECAUSE I'M THE ONE PROVIDING INPUT AND NOT THE USER
		else return new Map();
	}
	
	/**
	 * 
	 * @return
	 */
	public int numberofMaps()
	{
		return numberofmaps;
	}

	/**
	 * 
	 * @param teamname Enter A or B
	 */
	public String getTeamName(String teamname)
	{
		String toreturn = "";
		if(teamname.equals("A") || teamname.equals("a"))
		{
			toreturn = A.getName();
		}
		else if(teamname.equals("B") || teamname.equals("b"))
		{
			toreturn = B.getName();
		}
		
		return toreturn;
	}
	
	/**
	 * 
	 * @param which_team enter a or b
	 * @return
	 */
	public Team getTeam(String which_team)
	{
		Team to_return  = new Team();
		switch (which_team)
		{
		case "a": to_return = A;
					break;
		case "A": to_return = A;
					break;
		case "b": to_return = B;
					break;
		case "B": to_return = B;
					break;
		}
		
		
		return to_return;
	}
	
}
