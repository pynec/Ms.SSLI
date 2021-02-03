package missingness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SSLT_iterations {
	int numind; 
	int num_first;
	int num_sec;
	List<Integer> group_one_list;
	List<Integer> group_two_list;
	
	
//method that shuffles the rows of a matrix
public static int[][] shuffle_matrix(int[][] matrix) {
	List<int[]> matrix_list = Arrays.asList(matrix);
	Collections.shuffle(matrix_list);
	matrix = matrix_list.toArray(new int[0][0]);
	return matrix;
}

//split the shuffled matrix into 2 groups and determine which loci in each group has multiple zeroes
public void split(int[][] matrix) {
	int nind = matrix.length; numind = nind;
	num_first = numind/2; 
	int[][] first = new int[numind/2][matrix[0].length];	
	int[][] second;
	if(numind % 2 == 1) {
		second = new int[(numind/2)+1][matrix[0].length]; }
	else {
		second = new int[numind/2][matrix[0].length]; }
	//add data to the initialized matrices
	for(int i = 0; i < numind/2; i++) {
		first[i] = matrix[i]; }
	int counter = 0;
	for(int j = numind/2; j< numind; j++) {
		second[counter] = matrix[j];
		counter++; }
	num_sec = counter; 
	List <Integer> col_list = new ArrayList<>();
	List <Integer> col_list_second = new ArrayList<>();
	//find loci with zeroes in the first matrix and add them to the column list (col list)
	//looping through individuals (eg. 44 because halved) 
	for(int i = 0; i < first.length; i++) {
		int[] ind = first[i];
		//loop through loci 
		for(int j = 0; j < matrix[0].length; j++) {
			if(ind[j] == 0) {
					col_list.add(j);
				} } }
	//remember that col_list (group_one_list) is listing the index of loci - not individuals!) 
	//same with group_two_list and col_list_second 
	group_one_list = col_list;
	//find loci with zeroes in the second matrix and add them to the column list (col list)
	for(int i = 0; i < second.length; i++) {
		int[] ind = second[i];
		for(int j = 0; j < matrix[0].length; j++) {
			if(ind[j] == 0) {
				col_list_second.add(j);
			} }	} group_two_list = col_list_second;

	}
	
	
public int group_specific() {
	//These loops determine the frequency of each loci in the lists. Want to count the number of loci that are missing in all individuals of that group 
		//because that would indicate that they are exclusively in one group 

		//Hashset removes the duplicates so that only unique loci indices are left
		Set<Integer> unique = new HashSet(group_one_list);
		Set<Integer> unique_sec = new HashSet(group_two_list);
		int total_loci=0; List<Integer> group_specific_loci = new ArrayList<>();
		for(Integer i: unique) {
			int count =  Collections.frequency(group_one_list, i);
			if(count == num_first) {
				total_loci++; group_specific_loci.add(i);	} }
		for(Integer i: unique_sec) {
			int count_sec = Collections.frequency(group_two_list, i);
				if(count_sec == num_sec) {
					total_loci++; group_specific_loci.add(i);
				} } 
		return total_loci;
}

public List<Integer> iterate(int[][] t) {
	//shuffle the matrix (individuals - rows)
	List<Integer> loci_dist = new ArrayList<>();
	//change the loop to 1000 when actually running it 
	//randomly splitting data into 2 groups and assessing the number of loci that are exclusively in one group
	//add to a list to get distribution information 
	for(int i = 0; i < 100; i++) {
		int[][] shuffled_matrix = shuffle_matrix(t);
		SSLT_iterations object = new SSLT_iterations();
		object.split(shuffled_matrix);
		int iterate_loci = object.group_specific(); loci_dist.add(iterate_loci);

	}
	return loci_dist;
}

public int percentile(List<Integer> distribution) {
	Collections.sort(distribution);
	int value = (int) Math.ceil(99 / 100.0 * distribution.size());
	value = distribution.get(value - 1);
	return value;
			
}

public static void main(String[] args) throws Exception{
	//retrieve transposed matrix from miss_analysis_opt script
	miss_analysis_opt obj = new miss_analysis_opt();
	obj.import_file("/Users/cassandrepyne/Documents/variant_test.txt");
	obj.separate_groups_by_zeroes();
	int[][] t = obj.group_B();
	SSLT_iterations obj_iterate = new SSLT_iterations();
	List<Integer> distribution = new ArrayList<>();
	distribution = obj_iterate.iterate(t);
	int percentile = obj_iterate.percentile(distribution);	
}
}
