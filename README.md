# Missingness
Missingness files 

Update on miss_analysis_opt.java 8/11/2020

Currently working on create_matrix() method
Just made two lists: the index of loci that have zeroes and those that do not
The next step is to make a matrix of the loci in zero list and those in non zero list (group_both_sexes is non_zero)

will need to make a matrix for the group a loci (for zero_list) 

**important: can now initialize those matricies with the size of the zero list and non-zero list 

**both of these matrices should probably be global variables. probably don't even need a return (can be a void method?) 


Update on miss_anlysis_opt.java 8/12/2020


Finished splitting the original matrix into potential zero matrix and initial loci in both sexes

made new method (separate_groups_by_zeroes) that takes the potential zero matrix and determines whether these loci are zero in both sexes or just in one (made 2 lists = final_zero_list and add_to_both_sexes)

The next step is to put these both bavck into matrices (will have to make a new one for final_zero_list and add the add_to_both_sexes to both_sexes (global matrix)

At this point, I will have 2 matrcies: GROUP A and GROUP B

After that, the next step is to deal with the both_sexes matrix and split into 2 random mixed sex subgroups and asses frequency of loci occuring in just one subgroup


Update on miss_analysis_opt.java 8/13/2020
Made two new matricies (one is final in both sexes matrix and the other is the final in one sex matrix)
These are the final GROUP A and GROUP B matricies 


Update on miss_analysis_opt.java 9/15/2020

Started working on the determining the SSLT (99th percentile of chance distribution).
Using group B (non-sex specific loci) but matrix needs to be transposed in order to split matrix by individuals.
So wrote method transpose() that transposes matrices
Idea: shuffle transposed matrix rows (so by individual) then take the first half and determine frequency of loci then do the same with the second half. Will have to do this for multiple iterations.
Next step: Try to split the matrix in half.

Update on miss_analysis_opt.java 9/16/2020

Wrote a method to shuffle the rows of the transposed matrix (method = shuffle_matrix()). Next step: can now recursively shuffle the matrix and take the first half the matrix as mixed sex group 1 and assess frequency of loci.


Update on miss_analysis_opt.java 10/22/2020
Added the SSLT_iteration.java script. This script's goal is to shuffle the transposed matrix multiple times and split the matrix into 2 random groups each time. In addition, it will estimate the loci that end up in just one group. So far, the script can split the dataset and determine how many zeroes are in each group (the zeroes indicate that the loci is missing in that group so theoretically if all individuals are missing that loci (all zeroes) and it is in the other group than it would be exclusively in one group). So, the next step is to compare the lists and determine which loci are in just one of the lists. The thing to note though is that it is going to be pretty unlikely that all individuals are missing a loci so will have to determine a threshold for how many individuals must have a zero for it to be in the list.

So next time: Finish comparing the two lists (only keep loci that are in just one list) and then run that script multiple times. The goal is to add the number of loci that are in just on group to a list and then take the 99th percentile of that list. (so add the loci but also how many individual it occurs in) - see page 6 of Stovall paper 

Do remember that the Stovall paper does say that it is an "estimate" of the number of loci.

Update on miss_analysis_opt.java 10/26/2020
Stovall paper= find SSLT and that is how you determine which loci are actually sex specific. This means that just because you have loci that are just in one sex it does not mean that they are actually sex specific. So, by determining the SSLT, you now have a threshold by which you can determine which loci are actually sex specific. This is becasue the SSLT is the number of loci that appear in just one sex by chance so if you have a sex specific loci that occurs in more individuals than by "chance (SSLT)" then it should be significanly sex specific.

So, the issue that I am having is fine. This is because even if there are no loci that occur exclusively in one sex by chance than the SSLT would be super low (or 0) so that would indicate that to be sex specific in this system, it doesn't have a minimum number of individuals is must be in.

Finished the SSLT script. So, I added three  methods. group_specific() finds the loci that are just in one sex by counting the number of loci that appear in ALL individuals (will most likely be zero for the small datasets). iterate() is the method that gets the distribution of the number of loci that are exclusively in one group (can adjust the number of times it iterates - should be 1000). percentile() determines the 99th percentile of the distribution.

So next time: double check the percentile method - make sure it is the most efficient. Then, go back to miss_analysis_opt and get the percentile from SSLT_iterations. Then, exclude all loci in GROUP A that fall below the SSLT.