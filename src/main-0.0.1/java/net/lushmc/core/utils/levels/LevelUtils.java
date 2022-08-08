package net.lushmc.core.utils.levels;

import java.util.HashMap;
import java.util.Map;

public class LevelUtils {
	
	private static Map<String,LevelWorker> workers = new HashMap<>();
	
	
	public static void start(){
		LevelWorker main = new LevelWorker();
		workers.put("main",main);
	}
	
	public static LevelWorker getWorker(String worker){
		return workers.get(worker);
	}
	
	@Deprecated
	public static LevelWorker createLevelWorker(long threshhold, long multiplier){
		return new LevelWorker(threshhold,multiplier);
	}
	
	public static LevelWorker createLevelWorker(String key, long threshhold, long multiplier){
		LevelWorker worker = new LevelWorker(threshhold,multiplier);
		workers.put(key, worker);
		return worker;
	}
	
	public static LevelWorker getMainWorker(){
		return workers.get("main");
	}
	
	
	

}
