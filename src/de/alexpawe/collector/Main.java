package de.alexpawe.collector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.json.*;

import de.alexpawe.collector.objects.Table;
import de.alexpawe.collector.objects.Task;

public class Main {

	public static void main(String[] args) {
		
		// Read tasks.json file and create an JSON Object
		String jsonString = null;
		try {
			jsonString = Files.readString(Paths.get("results\\tasks.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(jsonString);
		
		// Collect all the data of every Task id
		String[] taskIDs = obj.getNames(obj);
		
		Map<String, Task> Tasks = new HashMap<String, Task>();
		
		for (String taskID : taskIDs) {
			JSONObject taskObj = obj.getJSONObject(taskID);
			Task task = new Task(taskID, taskObj);
			Tasks.put(taskID, task);
		}
		
		// TODO: Create a table and output it as a csv file
		
		// Get the header
		Set<String> header = new TreeSet<String>();
		header.add("TASKID");
		for (Entry<String, Task> entry : Tasks.entrySet()) {
			for (String pkey : entry.getValue().getParameterKeys()) {
				header.add(pkey);
			}
			String[] metricsheader = entry.getValue().getMetricsheader();
			if (metricsheader != null) {
				for (String metric : metricsheader) {
					header.add(metric);
				}
			}
		}
		header.add("Iteration");
		System.out.println("Header is " + header.toString());
		
		// Create table
		Table table = new Table(header);
		for (String taskID : taskIDs) {
			List<Map<String, String>> rows = Tasks.get(taskID).getRows();
			table.addRows(rows);
		}
		
		// Print table
		System.out.println(table.toString());
		
		// Export table as csv
		
		System.out.println("Done!");
	}

}
