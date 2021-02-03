package missingness;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class missingness_opt_sim {

	int nloci;
	int nind;
	int nchr;
	int diff_sex;
	List<Integer> sex_list;
	int sex_loci;
	List<String> chr_list;
	List<String> chr_num;
	List<Integer> sex_spec_list;
	
public missingness_opt_sim(int numind, int numloci, int numchr, int diffSex, int sal) {
	nind = numind;
	nloci = numloci;
	nchr = numchr;
	diff_sex = diffSex;
	sex_loci = sal;
}
 
public missingness_opt_sim() {}

//method that generates a matrix with sex specific and random genotypes 
public List<List<String>> initiate_genotypes(){
	List<List<String>> out_data = new ArrayList<>();
	List<String> genotypes = new ArrayList<>();
	genotypes.add("0"); genotypes.add("1"); genotypes.add("2");
	List<Integer> numbered_list = new ArrayList<>();
	//list of loci (ex. 1 to 10)
	for(int j = 0; j < nloci; j++) { 
		numbered_list.add(j); }
	
	missingness_opt_sim obj = new missingness_opt_sim();
	//get random loci that will be sex specific (sex_loci is the num of sex specific loci (int)) 
	List<Integer> sal_list = obj.getRandomElement(numbered_list, sex_loci); sex_spec_list = sal_list;
	for(int i = 0; i < nloci; i++) {
		List<String> temp_list = new ArrayList<>();
		//if the loci is sex specific then loop through individuals and set "0" for the sex that will have the SAL 
		if(sal_list.contains(i)) {
			for(int j = 0; j < nind; j++) {
				if (sex_list.get(j) == diff_sex) {
					temp_list.add("0");
				}
				//if the individual is the other sex, assign a random genotype (1,2) 
				else {
					Random randNum = new Random();
					Integer rand = randNum.nextInt((2-1) + 1) + 1; String val = rand.toString();
					temp_list.add(val);
				} } }
		//if the loci isn't sex specific then assign a random genotype (1,2) 
		else {
			for(int k = 0; k < nind; k++) {
			Random randNum = new Random();
			Integer rand = randNum.nextInt((2-0) + 1) + 0; String val = rand.toString();
			temp_list.add(val);
		} }
		temp_list.toString();
		out_data.add(temp_list);
}
	return out_data;
}

//method that takes the partial genotype matrix from initiate_genotype() and outputs the genotype file matrix 
public List<List<String>> genotype_file(List<List<String>> geno_matrix){
	List<List<String>> out_matrix = new ArrayList<>();
	String snp = "snp";
	//these next few following lists are for the anno file 
	List<String> anno_list = new ArrayList<>(); List<String> chr_num_list = new ArrayList<>();
	//chromosome for sex specific loci 
	Integer chr_sal = (int)(Math.random()*((nchr-1) +1)) +1;
	//loop through loci (rows) and add the beginning information (chromosome etc) 
	for(int i = 0; i < nloci; i++) {
		List<String> nucleotides = new ArrayList<>();	List<String> loci_row = new ArrayList<>();
		nucleotides.add("A"); nucleotides.add("T"); nucleotides.add("C"); nucleotides.add("G");
		String minor = null; String major = null; 
		Integer rand_snp;
		//set chromosome depending on whether the loci is sex specific or not 
		if(sex_spec_list.contains(i)) { rand_snp = chr_sal; }
		else { rand_snp = (int)(Math.random()*((nloci-1) +1)) +1;	}
		chr_num_list.add(rand_snp.toString());
		String snp_info= snp + rand_snp.toString();
		anno_list.add(snp_info);
		//randomly assign major and minor allele nucleotides 
		for(int choose_nuc = 0; choose_nuc < 2; choose_nuc++) {
			Random rand = new Random();
			int index = rand.nextInt(nucleotides.size());
			if(choose_nuc == 0) { minor = nucleotides.get(index); }
			if(choose_nuc == 1) { major = nucleotides.get(index); } 
			nucleotides.remove(index); }
		//combine snp information with genotype information 
		loci_row.add(snp_info); loci_row.add(minor); loci_row.add(major);
		List<String> geno_matrix_row = geno_matrix.get(i);
		for(int j = 0; j < nind; j++) {
			loci_row.add(geno_matrix_row.get(j));
		}
		out_matrix.add(loci_row);
	}
	chr_list = anno_list; chr_num = chr_num_list;
	return out_matrix;
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

//method that takes a list of loci and a number of sex specific loci and returns the list of sex specific loci 
public List<Integer> getRandomElement(List<Integer> loci_list, int sex_loci){
	List<Integer> return_list = new ArrayList<>();
	Random r = new Random();
	List<Integer> tempList = new ArrayList<>();
	for(int i = 0; i < sex_loci; i++) {
		int randIndex = r.nextInt(loci_list.size());
		tempList.add(loci_list.get(randIndex));
		loci_list.remove(loci_list.get(randIndex));
	}
	return_list = tempList;
	return return_list;
}

//method that writes the output to a file 
public static void write_to_file(List<List<String>> output) throws IOException {
	FileWriter writer = new FileWriter("test.txt");
	for(List<String> row : output) {
		writer.write(row + System.lineSeparator());
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
//method that writes the annotation output to a file 
public static void main(String[] args) throws Exception{
	//numind, numloci, numchr, diffsex, sal 
	missingness_opt_sim output = new missingness_opt_sim(30, 100, 10, 1, 5);
	List<Integer> sex_list = output.sex_info();
	List<List<String>> genotypes = output.initiate_genotypes();
	List<List<String>> geno_file = output.genotype_file(genotypes);

	//write_to_file(geno_file);
	write_sex_file(sex_list);
	
}


}
