
import java.util.ArrayList;

public class C4dot5 {
	
	
	public static double calculateGainRatio(double infoD, ArrayList<Double> infoPs, ArrayList<Integer> setSizes, int tot){
		double gainRatio = 0.0;
		double gain = infoD; 
		double splitInfo = 0.0;
		double p=0.0;
		
		for(int i = 0; i < infoPs.size(); i++) {
			p = setSizes.get(i) / (double)tot;
			gain = gain+(-(p * infoPs.get(i)));
			splitInfo = splitInfo+(-p * (Math.log(p)/Math.log(2)));		
		}		
		gainRatio = gain/splitInfo;
		
		return gainRatio;
	}
	
	public static double calculateInfo(ArrayList <Mushroom> mushroomSet){
		double info = 0.0;
		if (mushroomSet.size() == 0 ) {
			return 0.0;
		}
			
		double p = 0.0;
		int i, j, count;
		Mushroom m;
		String sVal;
		for (i=0; i< 2; i++){
			count =0;
			
			if (i==0) {
				sVal="p";
			}else{
				sVal = "e";
			}
			for (j=0; j<mushroomSet.size(); j++) {
				m = mushroomSet.get(j);
				
				if (m.getAttributes().get(0).getValue().equals(sVal)) {
					count++;
				}
			}
			p=count / (double)mushroomSet.size();
			if(count > 0) {
				info = info+(-p * (Math.log(p) / Math.log(2)));
			}
			
		}
		return info;
		
	}
	
}

