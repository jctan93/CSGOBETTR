package chancetool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class CSGO_BET_ANALYZE {
	
	//TODO: PARSE ALL EVENT NAMES, FIGURE OUT HOW TO STOP AT 2013 EVENTS, THEN SAVE THEM

	public static void main(String[] args) 
	{
		Document doc;
		try
		{
			//Hashmap to store event, keys are event names and values you get are the hrefs
			HashMap<String,String> events_hashmap = new HashMap<String,String>();
			ArrayList<String> events_array = new ArrayList<String>();
			ArrayList<Event> stored_events = new ArrayList<Event>();
			
			doc = Jsoup.connect("http://wiki.teamliquid.net/counterstrike/Premier_Tournaments").get();
			//Elements event_name_rows = doc.select("table.sortable.wikitable.smwtable.jquery-tablesorter").select("tbody").select("tr").select("td").select("a[href]");
			Elements event_name_rows = doc.select("table.sortable.wikitable.smwtable.jquery-tablesorter").select("tbody").select("tr");
			Elements table_2012 = doc.select("table.sortable.wikitable.smwtable.jquery-tablesorter");
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			//We only want to get tournaments held >= the year 2013, CSGO ONLY tourneys
			int no_of_tables_toread = year - 2012;
			Element events_in2012 = table_2012.get(no_of_tables_toread);
			Elements list_of_2012_events = events_in2012.select("tr").select("td").select("a[href]");
			String last_event_2012 = list_of_2012_events.first().text();
			
			for(Element row : event_name_rows)
			{
				//Gets the elements in column 3 since it is the Name column
				Elements col = row.getElementsByIndexEquals(2);
				String to_print = col.last().text();
				
				//Only parses till the very first event of 2013
				if(to_print.equals(last_event_2012))
				{
					break;
				}
				
				//Ignores the first row since it is just the header
				if(!row.equals(event_name_rows.get(0)))
				{
					Element link = row.select("a").first();
					String link_string = link.attr("abs:href");
					
					//Print event names
					//System.out.println(to_print);
					
					//Saves the data in the hashmap
					events_hashmap.put(to_print, link_string);
					events_array.add(to_print);
				}
					

			}
			
			/*
			//TODO: Save all names in an array and create a new one which consists of every other name starting at 0
			ArrayList<String> temp_names = new ArrayList<String>();
			int counter = 0;
			for(Element event_name : event_name_rows)
			{
				String names_of_events = event_name.text();
				if(!StringUtils.isBlank(names_of_events))
				{
					System.out.println(names_of_events);
					if(names_of_events.equals(last_event_2012))
					{
						break;
					}
					
					if(counter%2 == 0)
					{
						temp_names.add(names_of_events);
					}
					

					counter++;
				}
			}
			*/
			
			
			//getEventGroupMatches();

			//for(String event : events_array)
			//{
			//	Event to_store = getEventDetails(events_hashmap.get(event), event);
			//}
			Event to_store = getEventDetails(events_hashmap.get(events_array.get(16)), events_array.get(16));
			System.out.println();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	//TODO: dummy test method
	/**
	 * 
	 */
	static void getEventGroupMatches()
	{
		Document doc;
		try {
			doc = Jsoup.connect("http://wiki.teamliquid.net/counterstrike/ESL/Major_Series_One/2014/Katowice").get();
			Elements info = doc.select("table.oldtable.table.table-bordered.grouptable");
			
			info.size();
			for(Element e: info)
			{
				String to_split = e.text();
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
	static Event getEventDetails(String event_name_href, String event_name)
	{
		Document doc;
		try {
			doc = Jsoup.connect(event_name_href).get();
			String yet_tobe_played = doc.body().text();
			
			if(!(yet_tobe_played.toLowerCase().contains("tbd")))
			{
				ArrayList<Team> all_teams = new ArrayList<Team>();
				//Selects all the games played after group stages
				Elements elements = doc.body().select(".bracket-game");
				
				//Selects all teams participating in this tournament
				Elements teamname_elements = doc.body().select(".teamcard");
				
				//Selects info about group stage matches
				Elements group_stages = doc.select(".match-row");
				
				//Size of this is the number of groups
				Elements info = doc.select("table.oldtable.table.table-bordered.grouptable");
				
				//Name of event
				Elements event_name_info = doc.select(".firstHeading");
				String name_of_event = event_name_info.text();

				ArrayList<Game> all_games = new ArrayList<Game>();
				
				//Creates new event object and sets the name of the event
				Event event_toreturn = new Event();
				event_toreturn.setEventName(name_of_event);
				
				
				
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
					    		String team_name = element_inner.child(0).text();
					    		temp_team.addTeamName(team_name);
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
				
				//Sets the teams participating in the event
				event_toreturn.setTeams(all_teams);
				
				//TODO: GROUP MATCHES
				//GROUP STAGE MATCHES
				for(Element e: group_stages)
				{
					Element map_name_element = e.child(1).child(0).child(2).child(0).child(4);
					String map_name = map_name_element.text();
					String to_split = e.text();
					String[] tokens = to_split.split(" +");
					String team_a = tokens[0];
					String team_b = tokens[tokens.length-1];
					
					//REMOVES TEAM NAMES FROM STRING
					to_split = to_split.replace(tokens[0], "");
					to_split = to_split.replace(tokens[tokens.length-1], "");
					tokens = to_split.trim().split(" +");
					int tokens_length = tokens.length;
					
					//TODO: parse match results, beginning and end of string, time[actually screw the time] and map details
					int a_score = Integer.parseInt(tokens[0]);
					int b_score = Integer.parseInt(tokens[tokens_length-1]);
					
					//Sets map details
					Map temp_group_match = new Map();
					temp_group_match.setMapName(map_name);
					temp_group_match.setRounds(a_score, "a");
					temp_group_match.setRounds(b_score, "b");
					
					//Since the names are abbreviated, we need to find the actual matching team_names
					team_a = compareNames(team_a.replaceAll("[^A-Za-z0-9]", ""), all_teams);
					team_b = compareNames(team_b.replaceAll("[^A-Za-z0-9]", ""), all_teams);
					
					//Set teams for the map
					temp_group_match.setTeam(team_a, "a");
					temp_group_match.setTeam(team_b, "b");
					
					
					Game temp_game = new Game();
					temp_game.setMap(temp_group_match);
					
					//Set teams for the game
					for(Team to_add_team: all_teams)
					{
						if(to_add_team.getName().equals(team_a))
						{
							temp_game.setTeam(to_add_team, "a");
							break;
						}
					}
					
					for(Team to_add_team: all_teams)
					{
						if(to_add_team.getName().equals(team_b))
						{
							temp_game.setTeam(to_add_team, "b");
							break;
						}
					}
					
					temp_game.setScore(temp_group_match.getRounds("a"), "a");
					temp_game.setScore(temp_group_match.getRounds("b"), "b");
					temp_game.setStage("group");
					//System.out.println("TEAM A: "+ team_a);
					//System.out.println("TEAM B: "+ team_b);
					all_games.add(temp_game);
				}
				
				//TODO: Split teams into their respective groups, assumes that there are only 4 groups
				int matches_per_group  = all_games.size() / info.size(); 
				int inner = 0;
				
				for(int i = 0; i < info.size(); i++)
				{
					switch(i)
					{
					case 0: event_toreturn.setGroup("a", splitGroup(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 1: event_toreturn.setGroup("b", splitGroup(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 2: event_toreturn.setGroup("c", splitGroup(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 3: event_toreturn.setGroup("d", splitGroup(all_games, inner,matches_per_group));
							break;
					default: System.out.println("NO GROUPS?");
							break;
					}
				}
				
				//Resets counter
				inner = 0;
				//TODO: FIGURE OUT HOW TO SPLIT MATCHES INTO THEIR RESPECTIVE GROUPS
				for(int i = 0; i < info.size(); i++)
				{
					switch(i)
					{
					case 0: event_toreturn.addGames("group_a", splitGroupMatches(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 1: event_toreturn.addGames("group_b", splitGroupMatches(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 2: event_toreturn.addGames("group_c", splitGroupMatches(all_games, inner,matches_per_group));
							inner+=matches_per_group;
							break;
					case 3: event_toreturn.addGames("group_d", splitGroupMatches(all_games, inner,matches_per_group));
							break;
					default: System.out.println("NO GROUPS?");
							break;
					}
				}
				
				
				//TODO: REWORK ALL THE CODE THAT PARSES BRACKET GAMES, CURRENT CODE IS TOO MESSY AND DOESN'T WORK RIGHT
				//PRINT BRACKET GAME TEST
				for (Element element : elements) 
				{
				    //System.out.println(element.text());
					Game temp_game = new Game();
				    Elements inner_elements = element.children();
				    
				    Elements bracket_teamname_elements = element.select(".team-template-text");
				    String name_a = bracket_teamname_elements.get(0).text();
				    String name_b = bracket_teamname_elements.get(bracket_teamname_elements.size()-1).text();
				    		
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
							    		

							    		if(length != 1 && to_split.matches(".*\\d.*"))
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
				    		
				    		//System.out.println();
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
				    temp_game.setStage("post_group");
				    all_games.add(temp_game);
				   
				}
				
				for(int i = inner; i < all_games.size(); i++)
				{
					
				}

				System.out.println();
				return event_toreturn;
			}
			else
				System.out.println(event_name + " NOT PLAYED YET!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new Event();
	}
	
	//TODO: DESCRIBE HELPER METHOD
	static String compareNames(String team, ArrayList<Team> all_teams)
	{
		for(Team compare_team: all_teams)
		{
			String name_to_compare = compare_team.getName().toLowerCase();
			int abbreviated_start = 0;
			int compare_pos = 0;
			//Numbers of letters that match, possible that it's not a 100% match eg. mouz, mousesports
			int no_of_matches = 0;
			
			//Compares each char in the abbreviated name to the full team names and tries to find a pattern
			while(abbreviated_start != (team.length()) && compare_pos != (name_to_compare.length()))
			{
				if(	team.toLowerCase().charAt(abbreviated_start) == name_to_compare.charAt(compare_pos)	)
				{
					abbreviated_start++;
					no_of_matches++;
				}
				compare_pos++;
			}
			
			
			//Percentage of chars that match
			double hit_rate = (double)no_of_matches/(double)team.length();
			if(	hit_rate >= 0.75)
			{
				team = compare_team.getName();
				return team;
			}
			
			
		}
		
		return "";
	}
	
	/**
	 * Helper method to split all_teams into their respective groups
	 * @param all_teams
	 * @param start_pos
	 * @param matches_per_group
	 * @return
	 */
	static ArrayList<Team> splitGroup(ArrayList<Game> all_games, int start_pos, int matches_per_group)
	{
		ArrayList<Team> to_return = new ArrayList<Team>();
		
		for(int i = start_pos ; i < start_pos+2; i++)
		{
			to_return.add(all_games.get(i).getTeam("a"));
			to_return.add(all_games.get(i).getTeam("b"));
		}
		
		return to_return;
	}
	
	/**
	 * Helper method to split all_games into their respective groups
	 * @param all_teams
	 * @param start_pos
	 * @param matches_per_group
	 * @return
	 */
	static ArrayList<Game> splitGroupMatches(ArrayList<Game> all_games, int start_pos, int matches_per_group)
	{
		ArrayList<Game> to_return = new ArrayList<Game>();
		
		for(int i = start_pos ; i < start_pos+matches_per_group; i++)
		{
			to_return.add(all_games.get(i));
		}
		
		return to_return;
	}

}
