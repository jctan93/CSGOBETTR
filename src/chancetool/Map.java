package chancetool;

public class Map 
{
	private String map_name;
	private String team_a;
	private String team_b;
	private int team_a_rounds;
	private int team_b_rounds;
	
	public Map()
	{
		
	}
	
	public void setMapName(String map)
	{
		map_name = map;
	}
	
	public void setTeam(String teamname, String whichteam)
	{
		switch (whichteam)
		{
			case "a":	team_a = teamname;
						break;
			case "A": 	team_a = teamname;
						break;
			case "b": 	team_b = teamname;
						break;
			case "B":	team_b = teamname;
						break;
			default:	System.out.println("WRONG INPUT, EXPECTED a/A/b/B, GOT: " + whichteam);
						break;
		}
	}
	
	public void setRounds(int rounds, String whichteam)
	{
		switch (whichteam)
		{
			case "a":	team_a_rounds = rounds;
						break;
			case "A": 	team_a_rounds = rounds;
						break;
			case "b": 	team_b_rounds = rounds;
						break;
			case "B":	team_b_rounds = rounds;
						break;
			default:	System.out.println("WRONG INPUT, EXPECTED a/A/b/B, GOT: " + whichteam);
						break;
		}
	}
	
	/**
	 * 
	 * @return Name of the current map
	 */
	public String getMapName()
	{
		return map_name;
	}
	
	/**
	 * 
	 * @param whichteam Team A or B
	 * @return Name of the team chosen, empty string if wrong input (which should not happen)
	 */
	public String getTeam(String whichteam)
	{
		String toReturn  = "";
		switch (whichteam)
		{
			case "a":	toReturn = team_a;
						break;
			case "A": 	toReturn = team_a;
						break;
			case "b": 	toReturn = team_b;
						break;
			case "B":	toReturn = team_b;
						break;
			default:	System.out.println("WRONG INPUT, EXPECTED a/A/b/B, GOT: " + whichteam);
						break;
		}
		
		return toReturn;
	}
	
	/**
	 * 
	 * @param rounds
	 * @param whichteam
	 */
	public int getRounds(String whichteam)
	{
		int toReturn  = 0;
		switch (whichteam)
		{
			case "a":	toReturn = team_a_rounds;
						break;
			case "A": 	toReturn = team_a_rounds;
						break;
			case "b": 	toReturn = team_b_rounds;
						break;
			case "B":	toReturn = team_b_rounds;
						break;
			default:	System.out.println("WRONG INPUT, EXPECTED a/A/b/B, GOT: " + whichteam);
						break;
		}
		
		return toReturn;
	}
	
}
