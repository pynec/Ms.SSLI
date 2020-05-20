package missingness;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import missingness.sim_data;


public class miss_analysis {
	List<String> lines;
	
	public miss_analysis(){
		lines = null;
	}
	
	//method that returns a list of sex specific loci 
	public List<Integer> in_both(int nummales, int numfemales, int nloci, int[][] sim_data) {
		//set up matrix for each sex 
		int[][] males = new int[nloci][nummales];
		int[][] females = new int[nloci][numfemales];
		int nind = nummales + numfemales;
	
		//temp_zero = list of potential sex specific loci, sex_specific = list of sex specific loci 
		List<Integer> temp_zero = new ArrayList<>(); List<Integer> sex_specific = new ArrayList<>();
	
		//if an individual has a "0" for that loci than add it to the list - so this is a list of POTENTIAL sex specific loci 
		for(int i = 0; i < nloci; i++) {
			for(int j = 0; j < nind; j++) {
				if(sim_data[i][j] == 0) {
					if(!temp_zero.contains(i)) {
					temp_zero.add(i); }	} } }

		//make sure that these loci are actually sex specific 
		for(int i = 0; i < nloci; i++) {
			//list of SEX of individuals with 0 at loci in temp_zero (so just 1 and 2s) 
			List<Integer> ind_sex = new ArrayList<>(); 
			//if the temp_zero (containing potential sex specific loci) has the indexed loci than further investigate 
			if(temp_zero.contains(i)) {
				//loop through individuals and get sex information from the individuals with a 0 for that loci 
			for(int j = 0; j < nind; j++) {
					if(sim_data[i][j] == 0) {
						int sex = sim_data[nloci][j];
						ind_sex.add(sex);
				}}
			
			//count how many males and females are in the ind_sex list (the list for ind with 0 at loci) 
			int count_males = 0; int count_females = 0;
			int size = ind_sex.size();
			for(int t = 0; t < size; t++) {
				if(ind_sex.get(t) == 1) {
					count_males++; }
				else {
					count_females++; }}
		//add to final sex_specific list if all individuals of one sex do not have the loci and the other sex does 
		if((ind_sex.size() >= nummales || ind_sex.size() >= numfemales)) {
			sex_specific.add(i);
		} } }		
		return sex_specific;
		
	}
	
	//method that imports a matrix file
	public void import_file (String file) throws IOException{
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);
		List<String> filelines = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			filelines.add(line);
		}
		br.close();
		this.lines = filelines; 
	}
	
	
	
	public static void main(String[] args) throws Exception{
		
		sim_data output = new sim_data(40, 40, 10);		
		List<Integer> maleLoci = output.sex_assignment();
		int[][] out = output.populate_matrix(maleLoci);
		
		miss_analysis obj = new miss_analysis();
		
		int nummales = output.nummales;
		int numfemales = output.numfemales;
		int nloci = output.nloci;
		
		
		List<Integer> sex_specific = obj.in_both(nummales, numfemales, nloci, out);
		System.out.println(sex_specific);
		
		obj.import_file("/Users/cassandrepyne/Documents/variants.txt");
	

}
}
