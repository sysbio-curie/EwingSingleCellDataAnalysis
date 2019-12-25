addpath ../data/

%prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861'; nbins = 30; thresh_prolif_begin = 0.612; thresh_ichighhigh = 0.78; thresh_ichigh3 = 0.8517;
prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434; thresh_ichighhigh = 0.61; thresh_ichigh3 = 0.6453; 
%prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546; thresh_ichighhigh = 0.945; thresh_ichigh3 = 0.8237;

disp('Loading data...');
data_tab = importdata(sprintf('%s_nufp.txt',prefix));
module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
disp('Loaded')

corrected_data = zeros(size(data_tab.data,1),size(data_tab.data,2));

gene_names = data_tab.textdata(2:end,1);
sample_names = data_tab.textdata(1,2:end);


i = 1; 
while i<=length(gene_names)

genename = gene_names(i);
ik = find(strcmp(gene_names,genename));    
disp(sprintf('%s: %i',char(genename),i));
gene_profile_corrected = IC10plus_gene_subtract_trend(prefix,genename,module_tab,data_tab,nbins,thresh_prolif_begin,thresh_ichighhigh);

corrected_data(i,:) = gene_profile_corrected;

i = i + 1;

end

fid2 = fopen(sprintf('../data/%s_nufp_ic10corrected.txt',prefix),'w');
fprintf(fid2,'GENE\t'); 
fprintf(fid2,'%s\t',sample_names{1:end-1}); 
fprintf(fid2,'%s\n',sample_names{end}); 

for i=1:length(gene_names)
    disp(sprintf('%s: %i',char(gene_names(i)),i));
    fprintf(fid2,'%s\t',char(gene_names(i))); 
    fprintf(fid2,'%2.2f\t',corrected_data(i,1:end-1)); 
    fprintf(fid2,'%2.2f\n',corrected_data(i,end)); 
end

fclose(fid2);


