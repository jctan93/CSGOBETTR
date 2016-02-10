package chancetool;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class CSGO_BET {
	
	

	public static void main(String[] args) {
		Document doc;
		try {
			doc = Jsoup.connect("http://wiki.teamliquid.net/counterstrike/ESL/Major_Series_One/2014/Katowice").get();
			
			//Selects all the games played after group stages
			Elements elements = doc.body().select(".bracket-game");
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
			    
			    Elements inner_elements = element.children();
			    for (Element element_inner : inner_elements)
			    {
			    	
			    	
			    	if(element_inner.className().equals("bracket-game-details"))
			    	{
			    		System.out.println(element_inner.className());
			    		Elements game_deets = element_inner.children();
			    		
			    		for(Element details : game_deets)
			    		{
			    			System.out.println(details.className());
					    	System.out.println(details.text());
			    		}
			    	}
			    	
			    	else
			    	{
				    	System.out.println(element_inner.className());
				    	System.out.println(element_inner.text());
			    	}
			    }
			   
			    System.out.println();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}

}
