package missingness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SSLT_iterations {
	
	


//method that shuffles the rows of a matrix
public static int[][] shuffle_matrix(int[][] matrix) {
	List<int[]> matrix_list = Arrays.asList(matrix);
	Collections.shuffle(matrix_list);
	matrix = matrix_list.toArray(new int[0][0]);
	return matrix;

}

//split the shuffled matrix into 2 groups and determine which loci in each group has multiple zeroes
public void split(int[][] matrix) {
	int numind = matrix.length;
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
//for(int[] row : first) {
//	System.out.println(Arrays.toString(row));	
//	}
	List <Integer> col_list = new ArrayList<>();
	List <Integer> col_list_second = new ArrayList<>();
	//find loci with zeroes in the first matrix and add them to the column list (col list)
	for(int i = 0; i < first.length; i++) {
		int[] ind = first[i];
		for(int j = 0; j < matrix[0].length; j++) {
			if(ind[j] == 0) {
					col_list.add(j);
				} } }
	//find loci with zeroes in the second matrix and add them to the column list (col list)
	for(int i = 0; i < second.length; i++) {
		int[] ind = second[i];
		for(int j = 0; j < matrix[0].length; j++) {
			if(ind[j] == 0) {
				col_list_second.add(j);
			} }	}
	
	//remove unique values (so loci that just had one zero) so the column lists will only have loci with multiple zeroes 
	HashSet<Integer> unique = new HashSet(col_list);
	HashSet<Integer> unique_sec = new HashSet(col_list_second);
	for(Integer val : unique) {
		col_list.remove(val);
	}
	for(Integer val_sec : unique_sec) {
		col_list_second.remove(val_sec);
	}
	//System.out.println(col_list);
	//System.out.println(col_list_second);
	
	}
	

public static void main(String[] args) throws Exception{
	//retrieve transposed matrix from miss_analysis_opt script
	miss_analysis_opt obj = new miss_analysis_opt();
	obj.import_file("/Users/cassandrepyne/Documents/variant_test.txt");
	obj.separate_groups_by_zeroes();
	int[][] t = obj.group_B();
	
	//shuffle the matrix (individuals - rows)
	int[][] shuffled_matrix = shuffle_matrix(t);
//	for(int[] row : shuffled_matrix) {
//	System.out.println(Arrays.toString(row));	}
	
	SSLT_iterations object = new SSLT_iterations();
	object.split(shuffled_matrix);

}


}
