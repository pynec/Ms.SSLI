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


Update 10/27/2020

miss_anlaysis_opt.java can now retrieve percentile information (SSLT) from SSLT_iterations.java.

Went back and walked through miss_analysis_opt.java code and realized that separate_groups_by_zeroes() is not actually determining which loci are sex specific. It is just determining the loci that has a few zeroes in one sex and none in the other. This means that the loci is technically in both sexes. So, in order for it to be sex specific it should be not at all in one sex - so there must be 0's in ALL individuals in one sex.

Next time: change if-then parameters in separate_groups_by_zeroes() so it is only adding loci to the final list if there are zeroes in all of one sex. Then, can apply the statistical threshold. So, once the final list is done - can exclude all loci in that list that fall below the SSLT.


Update 1/26/2021
Had a few updates that didn't get uploaded to github - whoops. The main this here is that I have to make sure the loci that are being added to the list are all zeroes in one sex (indicating that it is missing) and a few or no zeroes in the other (indicating that it is present). Right now, it is only picking up on a few zeroes.

Update 2/2/2021


Good progess. Fixed the if-then parameters of the separate_groups_by_zeroes() so that it is only adding loci to the final list if there are zeroes in all of one sex. One thing to note here is that these are pretty strict restrictions, so it will be difficult to find loci that are in sex but not another because all of one sex would have to have a zero at that loci. Makes me think. Why include it if that many individuals are missing it? Will need to go back and check filtering parameters once I test this will the larger dataset. Statistical threshold is being applied (but it is basically no threshold because the probability of a loci missing in a random group is pretty much zero all of the time) but this will change with different datasets. 

Assumptions made:
1. Add pair of allele depth values together for each individual/loci in create_matrix()
2. basing presence/absence of loci on allele depth
3. A loci is only sex specific if it is zero in all individuals of a sex (so a zero at index 8 (so one individual female)) is not se
x specific - See file separate_by_sexes.txt in github folder on computer for more information


Things to change eventually:
1. change int matrix[][] to int[][] temp_zero (since that is its name in the method where it is initalized)
2. make number of individuals a global variable earlier
3. Change sex_specific_list name to a matrix based name in separate_by_sexes


Next steps:
1. Go through SSLT_iterations.java (check logic again)
2. Finish the last step of the program: the exclude() method in the miss_analysis_opt (excluding all loci in the list that fall below the SSLT) -> not hard for this practice dataset since they are both zero
3. Test with simulated data (to make sure new changes work)
4. Test with larger real dataset 
If I don't find anything with the larger real dataset (but do with the simulated data) then may want to go back to filtering steps and see how much missing data I am allowing - may want to re-filter a few steps (esp. missingness).

Update 2/3/2021
Double checked the SSLT_iterations.java script. It is doing what it should do, however it is important to note that the restrictions are strict. The loci must be missing in all individuals of one group (which in some cases is 45 individuals). So far, no group has had any loci that hit that mark. Something to think about, but the code is logical (and does what Stovall describes)

Started working on the exclude() method but realized it is hard to write since there are no significant loci right now. So, working on missingness optimzied simulation data to test the program with. Using sal_sim.java as a basis for writing this simualtion. Basically, want it in the same format as variant_test.txt. This will be good for when other people use it because all they have to do is enter how many individuals, loci, and sex specific loci they want - can even be command line information.

Simulation update (missingness_opt_sim.java):
- sex_info.txt file is complete

Next step (simulation): make the variants file
- Re-do the initiate_genotypes method--> will have to change basically the whole thing so that there is a SNP ID, location, then random allele depth values (but zeroes for the sex specific loci) **remmeber that the file should have double the allele depth values (2 for each individual because they will get combined in the actual miss_analysis_opt.java program)

Also remember that the sal_sim.java file is making simulation files for GWAS so make sure that I am only putting zeroes in individuals of one sex.

Once I am done withe simulation file, finish up the exclude() method


Update 2/4/2021

Continued working on simulation script (missingness_opt_sim.java). Finished it! 
1. Wrote a new method that takes the sex list and determines the indices of the females, then transfers it to a new list for the AD matrix. Basically, it determines where the females will be in the AD matrix (remember that the AD matrix contains two values per individual)
2. Wrote a new method that creates a matrix with AD values

The simulation script fully imitates the variants file, so it should work with the missingness script!

Next step:
1. Test the simulation script
2. Finish the exclude() method in the optimized missingness script

Update 2/5/2021

Very exciting progress! Tested the simulation script on the optimized missingness script, and it worked!! It outputs the 5 loci (with all of the AD values) that are sex specific.


Also added a method to the missingness script that outputs the actual loci, not just a matrix of AD values (method = get_SNP_IDs). 

Next step:
1. Finish exclude() method in the optimized missingness script.
2. Try testing with larger real dataset 

Update 2/8/2021

Started thinking about how to implement the exclude() method and realized that I should write another simulation script. This one would have sex specific loci in both sexes (so loci A is missing in males and loci B is missing in females). Should be easy to write, just a variation of missingness_opt_sim.java. This way, I can make sure that the missingness script works in all ways. But first, should finish writing the exclude() method.

The exclude() method: excluding all candidate loci at individual frequencies lower than the SSLT. So basically have to get the information for the loci that are returned as sex specific. Go into final_sex_specific and assess the individual frequency of that loci in the individuals of the sex that did not have all of the zeroes (because that sex is missing the sex specific loci).

So if the SSLT is 0, the diagram below shows 3 individuals so the individual frequency of Loci A would be 2 and since 2 is above the SSLT (0) it is deemed significantly sex specific. 

Loci A:   0    	 1	3 

Next steps:
1. Finish exclude() method
2. Write another simulation script as described above
3. Try testing with a larger dataset.


Update 2/9/2021
Finished writing the exclude() method. The script is now basically finished (it will obviously be worked on as it is tested extensively).

Tried testing with a larger dataset (the full one). Takes a while (>10min) so it will have to be added to graham to run with super big datasets. It is the iterate() method in the SSLT_iterations.java that is taking most the time. Everything else runs in seconds.


Update 2/26 and 3/1
Working on updating the simulation script.
Goal: Have sex specific loci in both sexes --> so example: Loci A is missing in males and loci B is missing in females (it is not missing in both sexes!!)

Idea on updating the missingness_opt_sim.java script ; add back in the diffsex parameter. Basically, this parameter takes 2 (or 3) different values. Right now, the way the script is set up is that it only would allow a 1 (so females have the sex specific loci). I am proposing to add a 0 which would mean that both sexes have sex specific loci. There is an option to add a 2 which would mean that males have the sex specific loci. However, the code would just be the same if I had both 1 and 2 as options because it would just be duplicated. Plus, this is simulated code so it won't make much of a difference.

So, the idea is just to add the 0 option. So, these are the changes that need to be made.
1. sex_info() method stays the same
2. fem_AD_index(): this method returns a list of females indices for the allele dpeth matrix (each individual gets 2 AD values)
UPDATE: write a method male_AD_index() which does the same thing for males
3. AD_matrix(): this method creates the AD matrix
UPDATE: change the if, then statement to include values from the male_AD_index()

Next steps:
1. Make the changes to the missingness_opt_sim.java script as described above
2. Add the script to Graham, test the large real dataset
3. Test with other datasets