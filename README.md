Respository for all scripts for Ms.SSLI 

Ms.SSLI.jar is the .jar file needed to run on a high performance computing cluser (and on a local computer if the input files are small).
***AS OF OCTOBER 2021, THIS PROGRAM IS STILL A WORK IN PROGRESS!*** 


Other scripts: sim_data.java, missingness_opt_sim.java are for simulating data for testing of Ms.SSLI

USER MANUAL
Ms.SSLI is a program that aims to detect sex-specific loci from reduced-representation genomic data. In comparison to many other sex determination tools, Ms.SSLI uses the presence of missing data within a data set to identify loci that are exclusively in one sex. For example, if a locus is missing in one sex but present in the other, then it is likely that the locus is sex-specific and may contribute to sex determination in that organism.  Missing data are measured using allele depth values. If a locus has an allele depth value of 0 at an individual, then it would be deemed absent in that individual. In addition to using missing data, Ms.SSLI uses the statistical threshold outline in Stovall2018. 

INSTALLING Ms.SSLI
There are two ways to run Ms.SSLI. The first and recommended way is to use a high performance computing cluster. Large data sets may take a long time to run, therefore it is recommended to run Ms.SSLI on a compute cluster. To do this, first download the .jar file from Github (https://github.com/pynec/Missingness). The .jar file contains both the miss_analysis_opt.java and the SSLT_iterations.java files. The downloaded .jar file can then be moved to your computing cluster. The second option is to run Ms.SSLI locally on a computer using the terminal (Mac) or command prompt (Windows). This option is only recommended with small data sets. Similar to the first option, the first step is to download the .jar file from Github. Running Ms.SSLI on a local computer has the same commands as running on a computing cluster, which can be seen below. 

FILE INPUTS

Ms.SSLI is relatively simple to run on a high performance computing cluster or on a local computer. However, the input files have to be in a particular setup.  
There are two files that are necessary - the variants and phenotype files. I would recommend running Ms.SSLI with simulated data first to determine if there are any issues before using your real data. There is a Java script to simulate data for the two files to test simple Ms.SSLI commands. This script can be found on my Github page (https://github.com/pynec/Missingness). 

VARIANTS FILE
This file will most likely be the largest file of the two - especially with real data. The variants file is a text file with extracted information from a VCF file using  vcftools. It includes SNP ids, position, and two allele depth values per individual, for each locus. Below is an example of a variants file for 6 SNPs and 10 individuals. 

Asm23,51947,7,0,7,0,2,0,9,0,7,1,9,0,3,0,7,0,1,0,4,0 
Asm26,53872,0,0,0,0,0,0,11,0,8,0,11,0,8,0,10,0,1,0,4,1
Asm72,53897,0,0,0,0,0,0,0,11,0,8,0,11,0,8,0,10,0,1,3,0
Asm39,8170,7,0,5,0,2,0,0,4,0,9,0,9,0,2,0,4,0,4,0,1
Asm46,18814,6,0,3,0,0,0,5,0,9,0,5,0,2,0,2,0,3,0,6,0
Asm20,51974,7,0,7,0,2,0,9,0,7,1,9,0,3,0,7,0,0,0,4,0

To create this file, a VCF is needed. When filtering, it is important to retain the most amount of data possible, as Ms.SSLI relies on missing data. In order to do this, the missingness criteria (--max-missing) should be more than 50 percent. Ideally, more than 70 percent of missing data should be allowed. 

To convert from a VCF to the variants file, follow these commands: 
1. extract allele depth from the VCF
vcftools --vcf  FILE_NAME.vcf --extract-FORMAT-info AD --out variants

2. convert to text file 
sed 's/\t/,/g' variants.AD.FORMAT > variants.txt

Depending on the formatting of your data, you may need to use Unix commands to remove extra characters or line breaks. 

PHENOTYPE FILE
The phenotype file includes sex information for each individual. These values must be numeric, not characters. Males should be denoted using a 1 and 2 for females. The phenotype file includes sexed individuals with variable reliability, as researchers' confidence in the phenotypic identification can vary depending on the organism and field technician. An example file can be seen below, with 10 individuals.

2 
2
2 
1 
1 
2 
1 
2 
1 
2 


CODE
##When running on a computing cluster, load modules 
module load nixpkgs/16.09 
module load java/13.0.1

##Command to run Ms.SSLI
java -jar MsSSLI.jar phenotype_file.txt variants_file.txt


OUTPUT
There is one output file of Ms.SSLI. It contains the SNP IDs and positions of all of the significantly sex-specific loci. 

Questions and Answers
Q: What if my data does not have all individuals sexed? 

A: Those individuals would have to be removed, as  Ms.SSLI can only use individuals that have been sexed. 
