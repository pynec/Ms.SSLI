# Missingness
Missingness files 

Update on miss_analysis_opt.java 8/11/2020

Currently working on create_matrix() method
Just made two lists: the index of loci that have zeroes and those that do not
The next step is to make a matrix of the loci in zero list and those in non zero list (group_both_sexes is non_zero)

will need to make a matrix for the group a loci (for zero_list) 

**important: can now initialize those matricies with the size of the zero list and non-zero list 

**both of these matrices should probably be global variables. probably don't even need a return (can be a void method?) 
