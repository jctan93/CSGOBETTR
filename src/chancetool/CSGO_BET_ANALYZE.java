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

			for(String event : events_array)
			{
				Event to_store = getEventDetails(events_hashmap.get(event), event);
				if (to_store.getPlayed() == true)
				{
					stored_events.add(to_store);
				}
			}
			//Event to_store = getEventDetails(events_hashmap.get(events_array.get(events_array.size()-4)), events_array.get(events_array.size()-4));
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
			String has_teamcards = doc.body().text().toLowerCase();
			
			Elements has_been_played = doc.body().select(".table-responsive");
			
			String tbd = has_been_played.text().toLowerCase();
			
			for(Element info_table : has_been_played)
			{
				if(info_table.text().contains("tbd"))
				{
					tbd = "tbd";
					break;
				}
			}
			
			//TODO: MODIFY THIS, TBD could be in a teamcard
			if(!(tbd.toLowerCase().contains("tbd")))
			{
				ArrayList<Team> all_teams = new ArrayList<Team>();
				HashMap<String, Team> team_hashmap = new HashMap<String,Team>();
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
				
				
				//TODO: Alt version for tournaments missing teamcards, get names from groups instead of teamcards
				if(has_teamcards.toLowerCase().contains("tournaments missing teamcards"))
				{
					System.out.println(event_name + " MISSING TEAMCARDS");
					Elements teams = info.select("table.oldtable.table.table-bordered.grouptable").select(".team-template-text");
					for(Element team : teams)
					{
						//System.out.println(team.text());
						Team toadd_team = new Team();
						toadd_team.addTeamName(team.text());
						all_teams.add(toadd_team);
					    team_hashmap.put(toadd_team.getName(), toadd_team);
					}
				}
				
				else
				{
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
						    team_hashmap.put(temp_team.getName(), temp_team);
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
				}

				
				//Sets the teams participating in the event
				event_toreturn.setTeams(all_teams);
				
				//TODO: GROUP MATCHES Fix broken messy code
				//GROUP STAGE MATCHES
				for(Element e: group_stages)
				{
					
					Element team_a_score = e.child(1);
					Element team_b_score = e.child(2);
					
					String teamascore_string = team_a_score.ownText().trim();
					String teambscore_string = team_b_score.ownText().trim();

					//Ignores the row if the match was forfeited
					if(teamascore_string.equals("W") || teamascore_string.equals("FF"))
					{
						continue;
					}
					
					//TOO MESSY TO CONTINUE THE IF STATEMENT
					//If both teams have a score of 0 then the match wasn't played
					try
					{
						int a_score = Integer.parseInt(teamascore_string);
						int b_score = Integer.parseInt(teambscore_string);
						if(a_score == 0 && b_score == 0)
						{
							continue;
						}
					}
					catch (NumberFormatException nfe)
					{
						continue;
					}
					
					
					Elements group_row_results = e.select(".matchlistslot");
					String row_results = group_row_results.text();
					
					Element map_name_element;
					
					//Tries to get the map name, I came across two different types tables, hence the different methods to get it.
					try
					{
						map_name_element = e.child(1).child(0).child(2).child(0).child(4);
					}
					catch(IndexOutOfBoundsException iobe)
					{
						map_name_element = e.child(1).child(0).child(1).child(0).child(4);
					}
					
					String map_name = map_name_element.text();
					String to_split = e.text();
					String[] tokens = to_split.split(" +");
					String team_a = tokens[0];
					String team_b = tokens[tokens.length-1];
					
					int a_score = Integer.parseInt(teamascore_string);
					int b_score = Integer.parseInt(teambscore_string);
					
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
				
				//If the tournament has no group matches, skip this
				if(!all_games.isEmpty())
				{
					//Split teams into their respective groups, assumes that there are only 4 groups
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
						default: System.out.println(event_name + " NO GROUPS?");
								break;
						}
					}
					
					//Resets counter
					inner = 0;
					//SPLITS MATCHES INTO THEIR RESPECTIVE GROUPS
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
						default: System.out.println(event_name + " NO GROUPS?");
								break;
						}
					}
				}
				
				
				//TODO: REWORK ALL THE CODE THAT PARSES BRACKET GAMES, CURRENT CODE IS TOO MESSY AND DOESN'T WORK RIGHT
				//PRINT BRACKET GAME TEST
				
				//elements is ".bracket-game"
				ArrayList<Game> bracket_games_array = new ArrayList<Game>();
				for (Element element : elements) 
				{
				    //System.out.println(element.text());
					Game temp_game = new Game();
					temp_game.setStage("post_group");
				    Elements inner_elements = element.children();
				    
				    Elements bracket_teamname_elements = element.select(".team-template-text");
				    String whatiswrong = bracket_teamname_elements.text();
				    
				    //Skips the game if it is missing some info, I implemented this because there are dummy games in certain tables
				    if(whatiswrong.isEmpty())
				    {
				    	continue;
				    }
				    String name_a = compareNames(bracket_teamname_elements.get(0).text(), all_teams);
				    String name_b = compareNames(bracket_teamname_elements.get(bracket_teamname_elements.size()-1).text(), all_teams);
				    
				    if(name_a.isEmpty() || name_b.isEmpty())
				    {
				    	continue;
				    }
				    Elements bracket_scores = element.select(".bracket-score");
				    if(bracket_scores.get(0).text().isEmpty() || !bracket_scores.get(0).text().matches(".*\\d.*"))
				    {
				    	continue;
				    }
				    
				    
				    String text = bracket_scores.get(0).text();
				    int score_a = Integer.parseInt(bracket_scores.get(0).text());
				    int score_b = Integer.parseInt(bracket_scores.get(bracket_scores.size()-1).text());
				    
				    Team team_a = team_hashmap.get(name_a);
				    Team team_b = team_hashmap.get(name_b);
				    temp_game.setTeam(team_a, "a");
				    temp_game.setTeam(team_b, "b");
				    
				    temp_game.setScore(score_a, "a");
				    temp_game.setScore(score_b, "b");
				    //
				    Elements game_details_block = element.select(".bracket-game-details");
				    for(Element temp: game_details_block)
				    {
				    	
				    	Elements game_details_div = temp.children();
				    	//System.out.println(game_details_div.size());
				    	
				    	//What we want is in the third div
				    	Element results_block = game_details_div.get(2);
				    	
				    	//System.out.println(results_block.children().size());
				    	
				    	//Each row represents a map that was played
				    	Elements game_details_rows = results_block.children();
				    	
				    	//I'm not using a for each loop because the loop number will be used to determine whether the row is blank or not since every other row(odd rows) is a blank div
				    	for(int i = 0; i < game_details_rows.size(); i++)
				    	{
				    		Element row = game_details_rows.get(i);
				    		//If the string has no numbers in it then the map wasn't played
				    		String empty = row.text().replaceAll("\\s+", "");
				    		if( (i == 0 || i%2 == 0) && row.text().matches(".*\\d.*"))
				    		{
				    			boolean has_numbers = row.text().matches(".*\\d.*");
				    			//System.out.println(row.text());
				    			String row_text = row.text();
				    			String map_name = row.child(4).text();
				    			row_text = row_text.replaceAll(map_name, "");
				    			String[] tokens = row_text.split("\\s+");
				    			
				    			if(!tokens[0].matches(".*\\d.*"))
				    			{
				    				continue;
				    			}
				    			int a_score_string = Integer.parseInt(tokens[0]);
				    			
				    			int b_score_string = 0;
				    			
				    			
				    			//Older pages didn't have the score per side eg. 12 CT + 4 T = 16, therefore there are less tokens in the string (else branch)
				    			if(tokens.length > 2)
				    			{
				    				try
				    				{
				    					b_score_string = Integer.parseInt(tokens[tokens.length -2]);
				    				}
				    				catch (NumberFormatException e)
				    				{
				    					continue;
				    				}
				    			}
				    			else
				    			{
				    				try
				    				{
				    					b_score_string = Integer.parseInt(tokens[tokens.length -1]);
				    				}
				    				catch (NumberFormatException e)
				    				{
				    					continue;
				    				}
				    			}
				    				
				    			Map temp_map = new Map();
				    			temp_map.setMapName(map_name);
				    			temp_map.setRounds(a_score_string, "a");
				    			temp_map.setRounds(b_score_string, "b");
				    			temp_map.setTeam(name_a, "a");
				    			temp_map.setTeam(name_b, "b");
				    			
				    			temp_game.setMap(temp_map);
				    			//System.out.println();
				    		}
				    	}
				    }
				    all_games.add(temp_game);
	    			bracket_games_array.add(temp_game);
				}
				
				event_toreturn.addGames("post_group", bracket_games_array);
				event_toreturn.setAllGames(all_games);
				event_toreturn.setPlayed();
				//System.out.println();
				return event_toreturn;
			}
			else
			{
				System.out.println(event_name + " NOT PLAYED YET!");
			}
			
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
