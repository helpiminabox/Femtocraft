package femtocraft.managers.research;

public enum TechLevel {
		MACRO("macro", 0), 			//Vanilla level
		MICRO("micro", 1), 			//1st Tier
		NANO("nano", 2),  			//2nd Tier
		FEMTO("femto", 3), 			//3rd Tier
		TEMPORAL("temporal", 4),		//Specialty Tier 1
		DIMENSIONAL("dimensional", 4);	//Specialty Tier 2
		
		public String key;
		public int tier;
		
		TechLevel(String key, int tier)
		{
			this.key = key;
			this.tier = tier;
		}
		
		public static TechLevel getTech(String key)
		{
			if(key.equals("macro"))
			{
				return MACRO;
			}
			else if (key.equals("micro"))
			{
				return MICRO;
			}
			else if (key.equals("nano"))
			{
				return NANO;
			}
			else if(key.equals("femto"))
			{
				return FEMTO;
			}
			else if(key.equals("temporal"))
			{
				return TEMPORAL;
			}
			else if (key.equals("dimensional"))
			{
				return DIMENSIONAL;
			}
			else
			{
				return null;
			}
		}
		
}