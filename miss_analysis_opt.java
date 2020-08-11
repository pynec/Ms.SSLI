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
	int[][] both_sexes;
	
	public miss_analysis_opt(){}
	
	
	
	public int[][] import_file(String file) throws IOException{
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
		int[][] dat_matrix  = file_obj.create_matrix(filelines,sex_info);
	
		
		return null;
		
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
	
	

	
	public int[][] create_matrix(List<List<String>> filelines, List<String> sex_info){
	
		//initialize the temporary version of both_sexes global matrix with an arbitrary dimension (will most likely be less than filelines.size() but need to initialize it with something)
		//int[][] group_both_sexes = new int[filelines.size()][];
		
		//again, initializing this matrix with an arbitrary dimension size 
	
		int[][] matrix = new int[filelines.size()][sex_info.size()];
		
		//I think group A and group B are the same as zero list and non zero list? 
		//List<Integer> temp_groupA = new ArrayList<>();
		//List<Integer> temp_groupB = new ArrayList<>();
		List<String> SNP_IDs = new ArrayList<>();
		List<Integer> zero_list = new ArrayList<>();
		List<Integer> non_zero_list = new ArrayList<>();
		
		for(int i = 0; i < filelines.size(); i++) {
			
			//adding an extra column for SNP ID 
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
		
		System.out.println(zero_list.size());
		System.out.println(non_zero_list.size());
//	
//		for(int[] row : matrix) {
//			System.out.println(Arrays.toString(row));	
//		
//		}
		

		//System.out.println(temp_groupB);
		
		//both_sexes = group_both_sexes;
		return null;
		
	}
	
	
	
	
	public static void main(String[] args) throws Exception{
		miss_analysis_opt obj = new miss_analysis_opt();
		
		int[][] matrix = obj.import_file("/Users/cassandrepyne/Documents/variant_test.txt");
		
		
		
	
	}
	
}



