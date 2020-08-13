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
	
	public miss_analysis_opt(){}

	

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
		miss_analysis_opt file_obj = new miss_analysis_opt();
		
		List<String> sex_info = file_obj.import_sex_info("/Users/cassandrepyne/Documents/sex_info.txt");
		List<Integer> male_index = file_obj.male_index_list(sex_info); male_index_global = male_index;
		List<Integer> female_index = file_obj.female_index_list(sex_info); female_index_global = female_index; 
		file_obj.create_matrix(filelines,sex_info);

		
	}
	
	
	public List<String> import_sex_info (String file) throws IOException{
		Scanner s = new Scanner(new File(file));
		List<String> list = new ArrayList<>();
		while(s.hasNext()) {
			list.add(s.next());
		}
		s.close();
		return list;
	}
	
	
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
	
	

	
	public void create_matrix(List<List<String>> filelines, List<String> sex_info){
	
		int[][] matrix = new int[filelines.size()][sex_info.size()];
		
		List<String> SNP_IDs = new ArrayList<>();
		List<Integer> zero_list = new ArrayList<>();
		List<Integer> non_zero_list = new ArrayList<>();
		
		for(int i = 0; i < filelines.size(); i++) {
			int[] matrix_row = new int[sex_info.size()];
			List<String> list = new ArrayList<>();
	
			list = filelines.get(i);
			String str_list = list.get(0); String[] split_str = str_list.split(",");
			int index = 0;
			SNP_IDs.add(split_str[0]);
		
		
			for(int j = 2; j < split_str.length; j += 2) {
				int sum = Integer.parseInt(split_str[j]) + Integer.parseInt(split_str[j+1]);
				matrix_row[index] = sum;
				if(sum == 0 && !zero_list.contains(i)) {
					zero_list.add(i);
				}
				index++;
			}
			if(!zero_list.contains(i)) {
				non_zero_list.add(i);
			}
			matrix[i] = matrix_row;
		}
		
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
	
	public void separate_groups_by_zeroes() {
		
		int nind = male_index_global.size() + female_index_global.size();
		
		//System.out.println(male_index_global); System.out.println(female_index_global);
		
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
			
			Boolean compare_fem = zero_index.retainAll(female_index_global);
			Boolean compare_male = zero_index.retainAll(male_index_global);
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
	
		
		
		
		int[][] both_sexes_list = new int[miss_analysis_opt.both_sexes.length + add_to_both_sexes.size()][nind];
		for(int i = 0; i < miss_analysis_opt.both_sexes.length; i++) {
			both_sexes_list[i] = miss_analysis_opt.both_sexes[i];
			
		}
		int index = 0;
		for(int i = miss_analysis_opt.both_sexes.length; i < (miss_analysis_opt.both_sexes.length + add_to_both_sexes.size()); i++) {
			both_sexes_list[i] = matrix[add_to_both_sexes.get(index)];
			index++;
		}

		final_sex_specific = sex_specific_list; final_both_sexes = both_sexes_list;
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		miss_analysis_opt obj = new miss_analysis_opt();
		obj.import_file("/Users/cassandrepyne/Documents/variant_test.txt");
		obj.separate_groups_by_zeroes();
		
		
		
	
	}
	
}



