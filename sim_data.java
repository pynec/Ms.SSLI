package missingness;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

public class sim_data {
	List <Integer> sex; 
	private int nind;
	int nloci;
	private int nsexloci;
	int nummales; 
	int numfemales;
	
	
	public sim_data(int numind, int numloci, int numsexloci){
		sex = new ArrayList<Integer>();
		nind = numind;
		nloci = numloci;
		nsexloci = numsexloci;
	}
	
	public sim_data() {}
	
	//method that returns a list of male specific loci and assigns a sex for each individual 
	public List<Integer> sex_assignment() {
		//initialize list with two sexes (1 = male & 2 = female) 
		List<Integer> list = new ArrayList<>(); 
		list.add(1); list.add(2);
		//number of individuals (need to assign sex for each individual) 
		int numElements = nind;
		sim_data ind_sex_obj = new sim_data();
		//call getRandomElement function to return a list of random 1 and 2s (randomize sex)  
		sex = ind_sex_obj.getRandomElement(list, numElements, 0);
		//count number of males and females from the list 
		nummales = Collections.frequency(sex, 1); numfemales = Collections.frequency(sex, 2);
		
		//list of potential male specific loci (parameter for getRandomElement)
		List<Integer> maleSpecificList = new ArrayList<>();
		for(int i = 1; i < nloci; i++) {
			maleSpecificList.add(i); }
		
		List<Integer> maleLoci = new ArrayList<Integer>();
		sim_data male_sex_assn= new sim_data();
		//the list of actual loci that are male specific 
		maleLoci = male_sex_assn.getRandomElement(maleSpecificList, nsexloci, 1);
		Collections.sort(maleLoci);
		return maleLoci;
		
	}
	
	//method that takes a list and number of elements and returns that number of random elements
	//also takes an integer (0 or 1) indicating the allowance of duplicates in the list (0 = duplicates, 1 = no duplicates) 
	public List<Integer> getRandomElement(List<Integer> list, int numElements, int duplicates){
		List<Integer> return_list = new ArrayList<>();
		//random list of males and females (1 and 0) and allows duplicates 
		if(duplicates == 0) {
		Random r = new Random();
		List<Integer> tempList = new ArrayList<>();
		for(int i = 0; i < numElements; i++) {	
			int rIndex = r.nextInt(list.size());	
			tempList.add(list.get(rIndex)); }
		return_list = tempList;
		}
		//random list of male specific loci with no duplicates 
		if(duplicates == 1) {
			Random rand_var = new Random();
			List<Integer> temp = new ArrayList<>();
			for(int i = 0; i < numElements; i++){
				int randIndex = rand_var.nextInt(list.size());
				temp.add(list.get(randIndex));
				list.remove(list.get(randIndex));
			}
			return_list = temp; }
		return return_list;
	}

	//method that creates an "allele depth" matrix to be fed into missingness script 
	public int[][] populate_matrix(List<Integer> maleLoci) {
		int nrow = nloci; 
		int[][] matrix_males = new int[nrow][nummales]; int[][]	matrix_females = new int[nrow][numfemales];

		//loop through each loci - if it is maleLoci than make that loci a 0 in females (indicating that it is male specific) 
		for(int i = 0; i < nloci; i++) {
			if(maleLoci.contains(i)) {
				for(int j = 0; j < numfemales; j++) {
					matrix_females[i][j] = 0; }}
			else { //if it is not in maleLoci than assign a random number for that loci in females 
				for(int k = 0; k < numfemales; k++) {
					Random randNum = new Random();
					matrix_females[i][k] = randNum.nextInt((40 - 0) + 1) + 0; } }}
		
		//loop through each loci and assign a depth value for each male individual 
		for(int i = 0; i < nloci; i++) {
			for(int j = 0; j < nummales; j++) {
				Random rand = new Random();
				matrix_males[i][j] = rand.nextInt((40 - 0) + 1) + 0;  } }
		
		//create a list with sex information (1 = male, 2 = female) 
		int[] sexInfo = new int[nind];
		for(int i = 0; i < nummales; i++) {
			sexInfo[i] = 1; }
		for(int j = nummales; j < nind; j++) {
			sexInfo[j] = 2; }
		
		//add sexInfo list to the bottom of the matrix as a row to identify sex of each individual 
		int[][] out_matrix = new int[nrow+1][nind];
		
		//putting the two sex-specific matrices together 
		for(int i = 0; i < (nloci); i++) {
			for(int j = 0; j < nummales; j++) {
				out_matrix[i][j] = matrix_males[i][j]; }
			int index = 0;
			for(int k = nummales; k < nind; k++) {
				out_matrix[i][k] = 	matrix_females[i][index];
				index++; 
			} }
		for(int t = 0; t < nind; t++) {
			out_matrix[nloci][t] = sexInfo[t]; }
		return out_matrix;
		}

	public static void main(String[] args) throws Exception{
		sim_data output = new sim_data(10, 10, 3);		
		List<Integer> maleLoci = output.sex_assignment();
		int[][] out = output.populate_matrix(maleLoci);
		System.out.println(maleLoci);
		

for(int[] row : out) {
			System.out.println(Arrays.toString(row));	}
		
	}
	
}
