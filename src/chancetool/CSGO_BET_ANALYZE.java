package chancetool;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class CSGO_BET_ANALYZE {
	
	

	public static void main(String[] args) 
	{
		getEventGroupMatches();
		getEventPostGroupMatches();
		
	}
	
	//TODO: add string parameter to choose event
	/**
	 * 
	 */
	static void getEventGroupMatches()
	{
		Document doc;
		try {
			doc = Jsoup.connect("http://wiki.teamliquid.net/counterstrike/ESL/Major_Series_One/2014/Katowice").get();
			Elements info = doc.select(".match-row");
			
			for(Element e: info)
			{
				String to_split = e.text();
				String[] tokens = to_split.split(" +");
				
				to_split = to_split.replace(tokens[0], "");
				to_split = to_split.replace(tokens[tokens.length-1], "");
				tokens = to_split.split(" +");
				
				//TODO: parse match results, beginning and end of string, time and map details
				System.out.println(to_split);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	//TODO: add string parameter to choose event
	/**
	 * 
	 */
	static void getEventPostGroupMatches()
	{
		Document doc;
		try {
			doc = Jsoup.connect("http://wiki.teamliquid.net/counterstrike/ESL/Major_Series_One/2014/Katowice").get();
			
			//Selects all the games played after group stages
			Elements elements = doc.body().select(".bracket-game");
			
			//TEST
			Elements group_c_elements = doc.select("#collapsibleTable1");
			for (Element element : group_c_elements) 
			{
				System.out.println(element.text());
			}

			
			//Selects all teams participating in this tournament
			Elements teamname_elements = doc.body().select(".teamcard");

			ArrayList<Team> all_teams = new ArrayList<Team>();
			
			
			//PRINT PARTICIPATING TEAMS TEST
			for (Element element : teamname_elements) 
			{
				 	Elements inner_elements = element.children();
				 	
				 	Team temp_team = new Team();
				 	Boolean addedName = false;
				 	
				    for (Element element_inner : inner_elements)
				    {
				    	//This if statement covers the player roster
				    	if((element_inner.className()).equals("wikitable list"))
				    	{
				    		Elements rows = element_inner.select("tr");
				    		
				    		for (int i = 0; i < rows.size(); i++) 
				    		{ 
				    	        Element row = rows.get(i);
				    	        
				    	        //Gets rid of the number in front
				    	        temp_team.addTeamPlayer(row.text().substring(4));
				    	       // System.out.println(row.text().substring(4));
				    		}
				    	}
				    	
				    	//If it is a team name and not the player roster
				    	else if(!addedName)
				    	{
				    		//System.out.println(element_inner.text());
				    		temp_team.addTeamName(element_inner.text());
				    		addedName = true;
				    		
				    		//if(!StringUtils.isBlank(element_inner.className()))
				    	}
				    }
				    
				    
				    all_teams.add(temp_team);
				    /*
				    for(Team temp: all_teams)
				    {
				    	 System.out.println("Team Name: " + temp.getName());
						    System.out.println("Roster: ");
						    
						    for(String player_name : temp.getPlayers())
						    {
						    	System.out.println(player_name);
						    }
						    
						    System.out.println();
				    }
				   */
				    
			}
			
			
			//PRINT BRACKET GAME TEST
			for (Element element : elements) 
			{
			    //System.out.println(element.text());
				Game temp_game = new Game();
			    Elements inner_elements = element.children();
			    for (Element element_inner : inner_elements)
			    {
			    	
			    	
			    	if(element_inner.className().equals("bracket-game-details"))
			    	{
			    		//System.out.println(element_inner.className());
			    		Elements game_deets = element_inner.children();
			    		
			    		//Used to figure out what detail about the match it is on, only 1 and 2 are relevant
			    		int counter = 0;
			    		
			    		for(Element details : game_deets)
			    		{
			    			
			    			//System.out.println(details.className());
			    			//System.out.println("CLASS NAME LENGTH: " + details.className().length());
					    	
					    	//Date and time
					    	if(counter == 1)
					    	{
					    		//System.out.println("NUMBER: " + counter);
					    		//TODO
					    		//System.out.println("MATCH TIME: " + details.text());
					    		temp_game.setTime(details.text());
					    	}
					    	
					    	//Match details
					    	else if(counter > 1 && details.className().length() == 0)
					    	{
					    		//System.out.println("NUMBER: " + counter);
					    		//TODO:
					    		//System.out.println("MATCH RESULTS: ");
					    		
					    		Elements match_details = details.children();
					    		for(Element fine: match_details)
					    		{
					    			String to_split = fine.text();
					    			to_split = to_split.replaceAll("\\s+", " ");
						    		String delimiter = "[ ]+";
						    		String[] tokens = to_split.split(delimiter);
						    		int length = tokens.length;
						    		

						    		if(length != 1)
						    		{
						    			String name_of_map_add = tokens[6];
							    		//tokens[0] is the score of Team A, tokens[4] the score of Team B;
							    		//tokens[6] onwards is the map name;
							    		Map temp_map = new Map();
							    		temp_map.setRounds(Integer.parseInt(tokens[0]), "a");
							    		temp_map.setRounds(Integer.parseInt(tokens[4]), "b");
							    		
							    		for(int i = 7; i < length; i++)
							    		{
							    			name_of_map_add = name_of_map_add.concat(tokens[i]);
							    		}
							    		temp_map.setMapName(name_of_map_add);
							    		
							    		temp_game.setMap(temp_map);
							    		
							    		//TODO:
						    			//System.out.println(to_split);
						    		}
					    		}
					    	}
					    	
					    	counter++;
			    		}
			    		
			    		System.out.println();
			    	}
			    	
			    	//Sets Teams and score of each team
			    	else
			    	{
			    		//EXTRACTS SCORE AND TEAM NAME
			    		String to_split = element_inner.text();
			    		String delimiter = "[ ]+";
			    		String[] tokens = to_split.split(delimiter);
			    		int lengthofarray = tokens.length;
			    		String team_name = tokens[0];
			    		for(int i = 1; i < lengthofarray-1; i++)
			    		{
			    			team_name = team_name.concat(" " + tokens[i]);
			    		}
			    		
			    		//SETS TEAM A DETAILS
			    		if(!temp_game.teamABeenSet())
			    		{
			    			for(int i = 0; i < all_teams.size(); i++)
			    			{
			    				String name_of_team_storage = (all_teams.get(i)).getName();
			    				
			    				//Could be displaying the team names differently, eg. Complexity and Complexity Gaming, currently doesn't account for team intials
			    				if (	name_of_team_storage.equals(team_name)	|| team_name.contains(name_of_team_storage))
			    				{
			    					temp_game.setTeam(all_teams.get(i), "a");
					    			temp_game.setScore(	Integer.parseInt(tokens[lengthofarray-1]), "a");
					    			break;
			    				}
			    			}
			    			
			    		}
			    		
			    		//SETS TEAM B DETAILS
			    		else
			    		{
			    			for(int i = 0; i < all_teams.size(); i++)
			    			{
			    				String name_of_team_storage = (all_teams.get(i)).getName();
			    				if (	name_of_team_storage.equals(team_name)	|| team_name.contains(name_of_team_storage) || name_of_team_storage.contains(team_name))
			    				{
			    					//Updates the team name, since events are parsed in chronological order, the name will always be the latest one
			    					all_teams.get(i).addTeamName(team_name);
			    					temp_game.setTeam(all_teams.get(i), "b");
					    			temp_game.setScore(	Integer.parseInt(tokens[lengthofarray-1]), "b");
					    			break;
			    				}
			    			}
			    		}
			    		
			    		
				    	//System.out.println(element_inner.className());
				    	//System.out.println(element_inner.text());
			    	}
			    }
			   
			    int no_of_maps = temp_game.numberofMaps();
			    int j = 1;
			    for(int i = 0; i < no_of_maps; i++)
			    {
			    	switch(j)
			    	{
			    	case 1:	temp_game.getMap(1).setTeam(temp_game.getTeamName("a"), "a");
			    			temp_game.getMap(1).setTeam(temp_game.getTeamName("b"), "b");
			    			break;
			    	case 2: temp_game.getMap(2).setTeam(temp_game.getTeamName("a"), "a");
			    			temp_game.getMap(2).setTeam(temp_game.getTeamName("b"), "b");
			    			break;
			    	case 3: temp_game.getMap(3).setTeam(temp_game.getTeamName("a"), "a");
			    			temp_game.getMap(3).setTeam(temp_game.getTeamName("b"), "b");
			    			break;
			    	case 4: temp_game.getMap(4).setTeam(temp_game.getTeamName("a"), "a");
			    			temp_game.getMap(4).setTeam(temp_game.getTeamName("b"), "b");
			    			break;
			    	case 5: temp_game.getMap(5).setTeam(temp_game.getTeamName("a"), "a");
			    			temp_game.getMap(5).setTeam(temp_game.getTeamName("b"), "b");
			    			break;
			    	}
			    	
			    	j++;
			    }
			    
			    
			    System.out.println();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
