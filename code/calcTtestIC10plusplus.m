addpath ../data/lists

%prefix = 'pdx352';
prefix = 'pdx861';
%prefix = 'pdx184';

t = importdata([prefix '_nufp10k.txt']);
%t = importdata([prefix '_nufp_ic10corrected.txt']); 
gene_list = t.textdata(2:end,1);
samples = t.textdata(1,2:end);

%slow = importdata([prefix '_IC10HIGH.txt']);
%shigh = importdata([prefix '_IC10HIGH_HIGH.txt']);

slow = importdata([prefix '_BAD_ALLLOW.txt']);
shigh = importdata([prefix '_ALLBUTBAD.txt']);

gelow = t.data(:,find(ismember(samples,slow)));
gehigh = t.data(:,find(ismember(samples,shigh)));

for i=1:length(gene_list)
    disp(sprintf('%i',i));
    [h,p,ci,stats] = ttest2(gelow(i,:),gehigh(i,:),'Vartype','unequal');
    ttests(i) = stats.tstat;
    pval(i) = p;
    mlow(i) = mean(gelow(i,:));
    mhigh(i) = mean(gehigh(i,:));
end

[~,ind] = sort(ttests);

%fid = fopen([prefix '_COMPARISON_IC10plusplus.txt'],'w')
fid = fopen([prefix '_COMPARISON_BADALLLOW.txt'],'w')

for i=1:length(gene_list)
    disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mlow(ind(i)),mhigh(ind(i))));
    fprintf(fid,'%s\t%f\t%f\t%f\t%f\n',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mlow(ind(i)),mhigh(ind(i)));
end

fclose(fid);