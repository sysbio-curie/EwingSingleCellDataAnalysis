addpath ../data/

prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861'; nbins = 30; thresh_prolif_begin = 0.612; thresh_ichighhigh = 0.78; thresh_ichigh3 = 0.8517;
%prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434; thresh_ichighhigh = 0.61; thresh_ichigh3 = 0.6453; 
%prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546; thresh_ichighhigh = 0.945; thresh_ichigh3 = 0.8237;

disp('Loading data...');
%data_tab = importdata(sprintf('%s_nufp_ic10corrected.txt',prefix));
data_tab = importdata(sprintf('%s_nufp10k.txt',prefix));
disp('Loaded')


%genename = 'CD99';

gene_names = data_tab.textdata(2:end,1);


%list_ic10 = importdata('IC10+_genes.txt');
list_ic10 = importdata('IC10+_genes.temp');
%list_ic10 = importdata('10k_genes_pdx352.txt');
%list_ic10 = importdata('10k_genes_pdx861.txt');
%list_ic10 = importdata('10k_genes_pdx184.txt');
%list_ic10 = importdata(sprintf('10k_genes_%s.txt',char(prefix)));


i = 1; k =1;
while i<=length(list_ic10)

genename = list_ic10(i);
ik = find(strcmp(gene_names,genename));    
if length(ik)>0
disp(sprintf('%s: %i',char(genename),i));
[score_negative(k),score_positive(k),p(k,:),mn(k),pbias(k,:),pb(k),pb_slope(k),...
    score_negative1(k),score_positive1(k),...
    ic1score(k,:),ic2score(k,:),ic14score(k,:),icEGHscore(k,:),ic30score(k,:)] = ...
    IC10plus_gene_profiling(prefix,genename,data_tab,nbins,thresh_prolif_begin,thresh_ichighhigh,1);
saveas(gcf,sprintf('../data/ic10profiling_corrected/%s/%s.png',prefix,char(genename))); close all;
disp(sprintf('regression = %3.3f*x+%3.3f',p(k,1),p(k,2)));
disp(sprintf('mean_expresion = %3.3f',mn(k)));
k = k+1;
end
i = i + 1;

end


fid = fopen(sprintf('../data/ic10profiling_corrected/%s/_%s_summary.txt',prefix,prefix),'w');
fwrite(fid,sprintf(['NAME\tSC_NEGATIVE\tSC_POSITIVE\tA\tB\tMN\tPB\tPB_SLOPE\tSC_NEG_WEIGHTED\tSC_POS_WEIGHTED\t' ...
    'IC1_LOW\tIC1_MID\tIC1_HIGH\tIC1_MAX\tIC2_LOW\tIC2_MID\tIC2_HIGH\tIC2_MAX\tIC14_LOW\tIC14_MID\tIC14_HIGH\tIC14_MAX\t' ...
    'ICEGH_LOW\tICEGH_MID\tICEGH_HIGH\tICEGH_MAX\tIC30_LOW\tIC30_MID\tIC30_HIGH\tIC30_MAX\n']));
i = 1; k=1;
for i=1:length(list_ic10)
genename = list_ic10(i);
ik = find(strcmp(gene_names,genename));    
if length(ik)>0
fwrite(fid,sprintf('%s\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\t%2.2f\n',...
    char(genename),score_negative(k),score_positive(k),p(k,1),p(k,2),mn(k),pb(k),pb_slope(k),score_negative1(k),score_positive1(k),...
    ic1score(k,1),ic1score(k,2),ic1score(k,3),ic1score(k,4),ic2score(k,1),ic2score(k,2),ic2score(k,3),ic2score(k,4),...
    ic14score(k,1),ic14score(k,2),ic14score(k,3),ic14score(k,4),icEGHscore(k,1),icEGHscore(k,2),icEGHscore(k,3),icEGHscore(k,4),...
    ic30score(k,1),ic30score(k,2),ic30score(k,3),ic30score(k,4)));
k = k+1;
end
i = i + 1;
end
fclose(fid);