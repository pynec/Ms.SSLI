package missingness;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class missingness_opt_sim {

	int nloci;
	int nind;
	int nchr;
	List<Integer> sex_list;
	int sex_loci;
	static int sex_param;
	List<String> chr_list;
	List<String> chr_num;
	List<Integer> sex_spec_list;
	
public missingness_opt_sim(int numind, int numloci, int numchr, int diffsex, int sal) {
	nind = numind;
	nloci = numloci;
	nchr = numchr;
	sex_param = diffsex;
	sex_loci = sal;
}
 
public missingness_opt_sim() {}

public String[][] AD_matrix (List<Integer> fem_AD_index){
	//ind_AD is nind*2 because each individual gets 2 AD values 
	int ind_AD = (nind*2)+2; 
	String[][] ad_matrix = new String[nloci][ind_AD];
	String snp = "Asm"; 
	//loop through sex loci (number given by the user)
	for(int i = 0; i < sex_loci; i++) {
		String[] matrix_row = new String[ind_AD];
		//adding loci information to the beginning of each row (so SNP ID and position) 
		Integer snp_rand = (int)(Math.random()* Math.floor(100)); Integer pos = (int)(Math.random()*Math.floor(100));
		String snp_info = snp + snp_rand.toString(); 
		matrix_row[0] = snp_info; 
		matrix_row[1] = pos.toString(); 
		
		for(int j = 2; j < ind_AD; j++) {
			if(fem_AD_index.contains(j)) {
				Integer index = 0; 
				matrix_row[j] = index.toString();
			}
			else {
				Integer rand = (int)(Math.random()* Math.floor(25));
				matrix_row[j] = rand.toString();
				
			}
		}
		ad_matrix[i] = matrix_row;
	}
	for(int i = sex_loci; i < nloci; i++) {
		String[] matrix_row = new String[ind_AD];
		
		Integer snp_rand = (int)(Math.random()* Math.floor(100)); Integer pos = (int)(Math.random()*Math.floor(100));
		String snp_info = snp + snp_rand.toString(); 
		matrix_row[0] = snp_info; 
		matrix_row[1] = pos.toString(); 
		
		for(int j = 2; j < ind_AD; j++) {
			Integer rand = (int)(Math.random()* Math.floor(25));
			matrix_row[j] = rand.toString();
		}
		ad_matrix[i] = matrix_row;
	}
	return ad_matrix;
}


public String[][] both_AD_matrix (List<Integer> fem_AD_index, List <Integer> male_AD_index){
	//ind_AD is nind*2 because each individual gets 2 AD values 
	int ind_AD = (nind*2)+2; 
	String[][] ad_matrix = new String[nloci][ind_AD];
	String snp = "Asm"; 
	int first_half_index = sex_loci/2;
	//loop through sex loci (number given by the user)
	for(int i = 0; i < first_half_index; i++) {
		String[] matrix_row = new String[ind_AD];
		//adding loci information to the beginning of each row (so SNP ID and position) 
		Integer snp_rand = (int)(Math.random()* Math.floor(100)); Integer pos = (int)(Math.random()*Math.floor(100));
		String snp_info = snp + snp_rand.toString(); 
		matrix_row[0] = snp_info; 
		matrix_row[1] = pos.toString(); 
		
		for(int j = 2; j < ind_AD; j++) {
			if(fem_AD_index.contains(j)) {
				Integer index = 0; 
				matrix_row[j] = index.toString();
			}
			else {
				Integer rand = (int)(Math.random()* Math.floor(25));
				matrix_row[j] = rand.toString();	
			}
		}
		ad_matrix[i] = matrix_row;
	}
	int second_half_index; 
	if(sex_loci % 2 == 1) { second_half_index = (first_half_index * 2)+ 1; } 
	else { second_half_index = first_half_index * 2; }
	
	for(int i = first_half_index; i < second_half_index; i++) {
		String[] matrix_row = new String[ind_AD];
		//adding loci information to the beginning of each row (so SNP ID and position) 
		Integer snp_rand = (int)(Math.random()* Math.floor(100)); Integer pos = (int)(Math.random()*Math.floor(100));
		String snp_info = snp + snp_rand.toString(); 
		matrix_row[0] = snp_info; 
		matrix_row[1] = pos.toString(); 
		for(int j = 2; j < ind_AD; j++) {
			if(male_AD_index.contains(j)) {
				Integer index = 0; 
				matrix_row[j] = index.toString();
			}
			else {
				Integer rand = (int)(Math.random()* Math.floor(25));
				matrix_row[j] = rand.toString();	
			}
		}
		ad_matrix[i] = matrix_row;
	}
	
	for(int i = sex_loci; i < nloci; i++) {
		String[] matrix_row = new String[ind_AD];
		
		Integer snp_rand = (int)(Math.random()* Math.floor(100)); Integer pos = (int)(Math.random()*Math.floor(100));
		String snp_info = snp + snp_rand.toString(); 
		matrix_row[0] = snp_info; 
		matrix_row[1] = pos.toString(); 
		
		for(int j = 2; j < ind_AD; j++) {
			Integer rand = (int)(Math.random()* Math.floor(25));
			matrix_row[j] = rand.toString();
		}
		ad_matrix[i] = matrix_row;
	}
	return ad_matrix;
}

//method that creates the sex information (how many males, how many females) 
//row = nind, col = number of phenotypes (sex = 2) 
public List<Integer> sex_info(){
	List<Integer> out = new ArrayList<>();
	List<Integer> trait_val = new ArrayList<>();
	//sex = binary trait;
	trait_val.add(1); trait_val.add(2);
	for(int i = 0; i < nind; i++) {
		Random r = new Random();
		int rIndex = r.nextInt(trait_val.size());
		out.add(trait_val.get(rIndex));
	}
	sex_list = out;
	return out;	
}

//method that return a list of female indices for the allele depth matrix (each individual gets 2 AD values) 
public List<Integer> fem_AD_index(List<Integer> sex_list){
	List<Integer> fem_AD_index = new ArrayList<>();
	for(int i = 0; i < sex_list.size(); i++) {
		if(sex_list.get(i) == 1) {
			if(i == 0) {
				fem_AD_index.add(2);
				fem_AD_index.add(3);
			}
			else {
				int index = (i * 2) + 2;
				fem_AD_index.add(index);
				fem_AD_index.add(index +1);
			}
		}
	}
	return fem_AD_index;	
}

//method that returns a list of male indices for the allele depth matrix (only used if the diffsex parameter is 0) 
public List<Integer> male_AD_index(List<Integer> sex_list){
	List<Integer> male_AD_index = new ArrayList<>();
	for(int i = 0; i < sex_list.size(); i++) {
		if(sex_list.get(i) == 2) {
			if(i == 0) {
				male_AD_index.add(2);
				male_AD_index.add(3);
			}
			else {
				int index = (i * 2) + 2;
				male_AD_index.add(index);
				male_AD_index.add(index +1);
			}
		}
	}
	return male_AD_index;	
}


//method that writes the output to a file 
public static void write_to_file(String[][] output) throws IOException {
	FileWriter writer = new FileWriter("sim_variants.txt");
	for(String[] row : output) {
		writer.write(Arrays.toString(row).replace("[", "").replace("]","").replace(" ", "") + System.lineSeparator());
	}
	writer.close();	
}
//method that writes the phenotype output to a file 
public static void write_sex_file(List<Integer> output) throws IOException {
	FileWriter writer = new FileWriter("sex_info_sim.txt");
	for(int i = 0; i < output.size(); i++) {
		writer.write(output.get(i)+ " ");
	}
	writer.close();
}



public static void main(String[] args) throws Exception{
	//numind, numloci, numchr, diffsex, sal 
	missingness_opt_sim output = new missingness_opt_sim(30, 100, 10, 1, 5);
	List<Integer> sex_list = output.sex_info();
	List<Integer> fem_sex_specific_AD = output.fem_AD_index(sex_list);
	List<Integer> male_sex_specific_AD = output.male_AD_index(sex_list);
	//System.out.println(fem_sex_specific_AD);
	//System.out.println(male_sex_specific_AD);
	if(sex_param == 1) {
		String[][] ad_matrix = output.AD_matrix(fem_sex_specific_AD);
		write_to_file(ad_matrix);
	}
	if(sex_param == 0) {
		String[][] ad_matrix = output.both_AD_matrix(fem_sex_specific_AD, male_sex_specific_AD);
		write_to_file(ad_matrix);
	}

	
	write_sex_file(sex_list);
	
}


}
