package missingness;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class miss_analysis_opt {

	List<Integer> male_index_global;
	List<Integer> female_index_global; 
	static int[][] both_sexes;
	static int[][] zeroes; 
	static int[][] final_sex_specific;
	static int[][] final_both_sexes;
	int numind;
	
	public miss_analysis_opt(){}

	
	//method that takes a file name as input and creates a matrix 
	public void import_file(String file) throws IOException{
		Scanner s = new Scanner(new File(file));
		List<List<String>> filelines = new ArrayList<>();
		String line = new String();
		while(s.hasNextLine()) {
			List<String> list = new ArrayList<>();
			line = (s.nextLine());
			list.add(line);
			filelines.add(list);
		}
		s.close();
		//create and object to call other methods on
		miss_analysis_opt file_obj = new miss_analysis_opt();
		//import sex info and use that file to create lists of the male and female indices (global variables) 
		List<String> sex_info = file_obj.import_sex_info("/Users/cassandrepyne/Documents/sex_info.txt");
		List<Integer> male_index = file_obj.male_index_list(sex_info); male_index_global = male_index;
		List<Integer> female_index = file_obj.female_index_list(sex_info); female_index_global = female_index; 
		file_obj.create_matrix(filelines,sex_info);
	}
	
	//method to import sex information text file 
	public List<String> import_sex_info (String file) throws IOException{
		Scanner s = new Scanner(new File(file));
		List<String> list = new ArrayList<>();
		while(s.hasNext()) {
			list.add(s.next());
		}
		s.close();
		return list;
	}
	
	//method to find the index of every male in the sex information list 
	public List<Integer> male_index_list (List<String> sex_list) throws IOException{
		List<Integer> male_index = new ArrayList<>();
		for(int i = 0; i < sex_list.size(); i++) {
			String val = sex_list.get(i);
			if(val.matches("2")) {
				male_index.add(i);
			} 
		}
		return male_index;
	}
	
	//method to find the index of every female in the sex information list 
	public List<Integer> female_index_list (List<String> sex_list) throws IOException{
		List<Integer> female_index = new ArrayList<>();
		for(int i = 0; i < sex_list.size(); i++) {
			String val = sex_list.get(i);
			if(val.matches("1")) {
				female_index.add(i);
			}
		}
		return female_index;
	}
	
	//method that returns a matrix 
	public void create_matrix(List<List<String>> filelines, List<String> sex_info){
		//loci x individuals 
		int[][] matrix = new int[filelines.size()][sex_info.size()];
		
		List<String> SNP_IDs = new ArrayList<>();
		List<Integer> zero_list = new ArrayList<>();
		List<Integer> non_zero_list = new ArrayList<>();
		
		//loop through loci and separate into zero_list and non_zero_list 
		for(int i = 0; i < filelines.size(); i++) {
			int[] matrix_row = new int[sex_info.size()];
			List<String> list = new ArrayList<>();
			list = filelines.get(i);
			String str_list = list.get(0); String[] split_str = str_list.split(",");
			int index = 0;
			SNP_IDs.add(split_str[0]); //list of all SNP IDs
		
			//add pair of allele depth values together for each individual/loci 
			for(int j = 2; j < split_str.length; j += 2) {
				int sum = Integer.parseInt(split_str[j]) + Integer.parseInt(split_str[j+1]);
				matrix_row[index] = sum;
				//check to see if the sum of pair of allele depths is equal to 0 - if it is then add it to zero_list 
				//(so these are loci that contain at least one zero so there is a possibility that they are in one sex but not another 
				if(sum == 0 && !zero_list.contains(i)) {
					zero_list.add(i);
				}
				index++;
			}
			//add all other loci to the non_zero_list. So these are loci that are for sure non-sex specific 
			if(!zero_list.contains(i)) {
				non_zero_list.add(i);
			}
			matrix[i] = matrix_row;
		}
		
		//make matrices out of the non_zero_list and zero_list (will help make final matrices of group A and B (those that are sex specific and those that are not) 
		int[][] group_both_sexes = new int[non_zero_list.size()][sex_info.size()];
		int[][] temp_zero = new int[zero_list.size()][sex_info.size()];
	
		for(int i = 0; i < non_zero_list.size(); i++) {
			group_both_sexes[i] = matrix[non_zero_list.get(i)];
		}
		for(int i = 0; i < zero_list.size(); i++) {
			temp_zero[i] = matrix[zero_list.get(i)];
		}
		both_sexes = group_both_sexes;
		zeroes = temp_zero;
	}
	
	//method that determines which of the loci in the zero_list are actually sex specific and adds all other ones to the in_both_sexes matrix
	public void separate_groups_by_zeroes() {
		int nind = male_index_global.size() + female_index_global.size();
		
		//matrix = zero_list from create_matrix() method 
		int[][] matrix = miss_analysis_opt.zeroes;
	
		List<Integer> final_zero_list = new ArrayList<>();
		List<Integer> add_to_both_sexes = new ArrayList<>();
	
		for(int i= 0; i < matrix.length; i++) {
			List<Integer> zero_index = new ArrayList<>();
			int[] row_temp = new int[nind];
			row_temp = matrix[i];
			
			for(int j = 0; j < nind; j++) {
				if(row_temp[j] == 0) {
					zero_index.add(j);
				}
			}
			//boolean T or F if the index of 0 in male or female matches the full list of zero indicies 
			Boolean compare_fem = zero_index.retainAll(female_index_global);
			Boolean compare_male = zero_index.retainAll(male_index_global);
			//if the zeroes do match (a F) then it gets added to the sex specific list 
			if(compare_fem == false || compare_male == false) {
				final_zero_list.add(i);
			}
			else {
				add_to_both_sexes.add(i);
			}
		}
		int[][] sex_specific_list = new int[final_zero_list.size()][nind];
		
		for(int i = 0; i < final_zero_list.size(); i++) {
			sex_specific_list[i] = matrix[final_zero_list.get(i)];
		}
		
		//making final both_sexes_list (loci that are not sex specific) 
		int[][] both_sexes_list = new int[miss_analysis_opt.both_sexes.length + add_to_both_sexes.size()][nind];
		for(int i = 0; i < miss_analysis_opt.both_sexes.length; i++) {
			both_sexes_list[i] = miss_analysis_opt.both_sexes[i];
			
		}
		//making final sex_specific_list
		int index = 0;
		for(int i = miss_analysis_opt.both_sexes.length; i < (miss_analysis_opt.both_sexes.length + add_to_both_sexes.size()); i++) {
			both_sexes_list[i] = matrix[add_to_both_sexes.get(index)];
			index++;
		}
		final_sex_specific = sex_specific_list; final_both_sexes = both_sexes_list; numind = nind;
	}
	
	
	public void group_B() {
		transpose(final_both_sexes, numind);
			
	}
	

	public int[][] transpose(int[][] original_matrix, int nind) {
		int[][] transposed_matrix = new int[nind][original_matrix.length];
		for(int i = 0; i < original_matrix.length; i++) {
			for(int j = 0; j < nind; j++) {
				transposed_matrix[j][i] = original_matrix[i][j];
			}
		}
		return null;
		
	}
	
	public static void main(String[] args) throws Exception{
		miss_analysis_opt obj = new miss_analysis_opt();
		obj.import_file("/Users/cassandrepyne/Documents/variant_test.txt");
		obj.separate_groups_by_zeroes();	
		obj.group_B();
	}	
}



