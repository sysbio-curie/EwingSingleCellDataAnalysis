## Procedure for analyzing single cell RNASeq data from "Transcriptional programs define intratumoral heterogeneity of Ewing sarcoma at single cell resolution" paper (Aynaud et al, Cell Reports, 2020)

### Requirements

One need MATLAB installation on the computer in order to complete the --doSPRING command and --doAnalysis command.

For other commands installation of Java (minimum version 1.8) is required.

WARNING: 

You first have to download the data files using the instructions contained in 'data/readme.txt' file.

The calculations uses the getools.jar file which should be downloaded from ''.

### Raw data

Raw count data for Patient Derived Xenografts of Ewing sarcoma tumors can be found in the 'data' folder, as tab-delimited files.

Raw cound and logged data for the inducible cell line can be found in the 'inducible_cell_line.zip' archive.

Before the analysis or running the MATLAB scripts the corresponding files must be unzipped.

### Source code used to perform the analysis

All source codes are located in the 'code' folder.


### Analysis of Patient Derived Xenografts

The complete analysis of PDX data for Ewing Sarcoma, incuding the generation of the SPRING visualizations, is performed by running the following command line:

	java -cp code/getools.jar ESPDX --dataFolder [complete_path_for_the_data_folder]  --prefix [name_of_the_pdx_file] --doQC --doAnalysis --doSPRING --doModuleScores --matlabCodeFolder ./code/  --kNeighbours 10  --mit_perc_score 0.07  --minReadCounts 6000  --maxReadCounts 40000  --signatureDefinitionFile [complete_path_to_the_signature_definition_file]

Here is complete list of options for the script:

#### Commands:

--doQC  - perform basic Quality Check (removal of low/large number of reads cells, removal of cells with high mitochondrian read fraction)

--doAnalysis - perform all necessary table transformations

--doSPRING - perform SPRING visualization of the data (generating html+javascript files)

--doModuleScores - compute the module score table based on the definition of the modules

#### Parameters, paths:

--dataFolder [complete_path_for_the_data_folder]  

--prefix [complete_name_of_the_pdx_file] 

--signatureDefinitionFile [complete_path_to_the_signature_definition_file]

--matlabCodeFolder ./code/  

#### Parameters of the analysis

--kNeighbours 10  - number of neighbours used to construct the kNN graph

--mit_perc_score 0.07  - maximum tolerable percentage of read counds contained in the mitochondrial genes

--minReadCounts 6000  - minimum number of reads to consider to analyse a cell

--maxReadCounts 40000 - maximum number of reads to consider to analyse a cell

Analysis of all the PDX datasets can be run through 1) unzipping the data file in the data folder and 2) launching the corresponding .bat file.

#### Produced files:

[prefix] is the name of the file without .txt extension (e.g., 'pdx352')

[prefix]_u.txt - file containing only unique gene names

[prefix]_nu.txt - '*_u.txt' file after normalization (equal library size and log(x+1) transformation)

[prefix]_nu2k.txt - '*_nu.txt' file after selecting 2000 most variable genes

[prefix]_nu2k.txt.moduleAverages - module scores computed before filtering cells

[prefix]_nu_qcdata.txt - QC data table containing mitochondrial percentage values and the total read counts

[prefix]_goodcells.txt - list of selected cells passed the quality control

[prefix]_uf.txt - '*_u.txt' file after QC and selecting cells passed the quality control

[prefix]_nuf.txt  - '*_nu.txt' file after QC and selecting cells passed the quality control

[prefix]_nuf2k.txt - '*_nuf.txt' file after selecting 2000 most variable genes

[prefix]_nuf10k.txt - '*_nuf.txt' file after selecting 10000 most variable genes

[prefix]_nuf2k.txt.moduleAverages  - computation of module scores using 2000 most variable genes

[prefix]_nufp.txt  - '*_nuf.txt' file after application of read count pooling in neighbouring cells



