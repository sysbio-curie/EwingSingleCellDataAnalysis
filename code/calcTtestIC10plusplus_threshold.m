addpath ../data/lists
addpath ../data/


prefix = 'pdx352'; threshold_low = 0.658; threshold_high = 0.8775;
%prefix = 'pdx861'; threshold_low = 0.62; threshold_high = 0.8517;
%prefix = 'pdx184'; nbins = 30; threshold_low = 0.47; threshold_high = 0.6453;  
%prefix = 'pdx856'; nbins = 30; threshold_low = 0.65; threshold_high = 0.8;
%prefix = 'pdx1058'; nbins = 30; threshold_low = 0.55; threshold_high = 0.78;

t = importdata([prefix '_nufp10k.txt']);
%t = importdata([prefix '_nufp_ic10corrected.txt']); 
gene_list = t.textdata(2:end,1);
samples = t.textdata(1,2:end);

mod_tab = importdata([prefix '_nufp2k.txt.moduleAverages']);
module_scores = mod_tab.data;
module_names = mod_tab.textdata(1,2:end);
sample_names_modules = mod_tab.textdata(2:end,1);
ic10_score = module_scores(:,find(strcmp(module_names,'IC10+')));
g1s_score = module_scores(:,find(strcmp(module_names,'IC2+')));
g2m_score = module_scores(:,find(strcmp(module_names,'IC1+')));

%slow = importdata([prefix '_IC10HIGH.txt']);
%shigh = importdata([prefix '_IC10HIGH_HIGH.txt']);
%slow = importdata([prefix '_BAD_ALLLOW.txt']);
%shigh = importdata([prefix '_ALLBUTBAD.txt']);

slow = sample_names_modules(find(ic10_score<threshold_low));
shigh = sample_names_modules(find(ic10_score>threshold_high));
smid = sample_names_modules(find(ic10_score<threshold_high & ic10_score>threshold_low));
sprolif = sample_names_modules(find(g1s_score>0.6 | g2m_score>0.6));

mn = mean(module_scores(:,1:end-2),2);
ind_good = find(mn>mean(mn)-3*std(mn));
good_samples = sample_names_modules(ind_good);
slow = intersect(slow,good_samples);
shigh = intersect(shigh,good_samples);
smid = intersect(smid,good_samples);
sbad = setdiff(sample_names_modules,good_samples);
sout = union(shigh,slow);
slow_nonprolif = setdiff(slow,sprolif);

fid = fopen(sprintf('%s_CELL_LOW.txt',prefix),'w'); for i=1:length(slow), fprintf(fid,sprintf('%s\n',char(slow(i)))); end; fclose(fid);
fid = fopen(sprintf('%s_CELL_HIGH.txt',prefix),'w'); for i=1:length(shigh), fprintf(fid,sprintf('%s\n',char(shigh(i)))); end; fclose(fid);
fid = fopen(sprintf('%s_CELL_MID.txt',prefix),'w'); for i=1:length(smid), fprintf(fid,sprintf('%s\n',char(smid(i)))); end; fclose(fid);
fid = fopen(sprintf('%s_CELL_BAD.txt',prefix),'w'); for i=1:length(sbad), fprintf(fid,sprintf('%s\n',char(sbad(i)))); end; fclose(fid);
fid = fopen(sprintf('%s_CELL_OUT.txt',prefix),'w'); for i=1:length(sout), fprintf(fid,sprintf('%s\n',char(sout(i)))); end; fclose(fid);
fid = fopen(sprintf('%s_CELL_PROLIF.txt',prefix),'w'); for i=1:length(sprolif), fprintf(fid,sprintf('%s\n',char(sprolif(i)))); end; fclose(fid);


disp(sprintf('Number of LOW cells = %i',length(slow)));
disp(sprintf('Number of HIGH cells = %i',length(shigh)));
disp(sprintf('Number of MID cells = %i',length(smid)));
disp(sprintf('Number of BAD cells = %i',length(sample_names_modules)-length(ind_good)));
disp(sprintf('Number of PROLIF cells = %i',length(sprolif)));
disp(sprintf('Number of OUT cells = %i',length(sout)));

% ind = find(ismember(samples,slow));
% slow_ = samples(ind);
% fid = fopen(sprintf('%s_CELL_LOW_.txt',prefix),'w'); for i=1:length(slow_), fprintf(fid,sprintf('%s\n',char(slow_(i)))); end; fclose(fid);

gelow = t.data(:,find(ismember(samples,slow)));
gelow_nonprolif = t.data(:,find(ismember(samples,slow_nonprolif)));
gehigh = t.data(:,find(ismember(samples,shigh)));
gemid = t.data(:,find(ismember(samples,smid)));
geout = t.data(:,find(ismember(samples,sout)));
geprolif = t.data(:,find(ismember(samples,sprolif)));

% first, mid vs low
disp('Mid vs low...');
for i=1:length(gene_list)
    %disp(sprintf('%i',i));
    [h,p,ci,stats] = ttest2(gemid(i,:),gelow(i,:),'Vartype','unequal');
    ttests(i) = stats.tstat;
    pval(i) = p;
    mlow(i) = mean(gelow(i,:));
    mmid(i) = mean(gemid(i,:));
    mhigh(i) = mean(gehigh(i,:));
end
[~,ind] = sort(ttests);
fid = fopen([prefix '_COMPARISON_MID_LOW.txt'],'w')
fprintf(fid,sprintf('GENE\t%s_MIDLOW_TTEST\tMIN_LOG10PVAL\t%s_MID\t%s_LOW\t%s_LOWMID_FC\t%s_LOWMID_RANK\n',prefix,prefix,prefix,prefix,prefix));
for i=1:length(gene_list)
    disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mlow(ind(i)),mlow(ind(i))-(mmid(ind(i))+0.0)));
    fprintf(fid,'%s\t%f\t%f\t%f\t%f\t%f\t%f\n',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mlow(ind(i)),mlow(ind(i))-(mmid(ind(i))+0.0),round(i-length(gene_list)/2));
end
fclose(fid);

% second, mid vs high
disp('Mid vs high...');
for i=1:length(gene_list)
    %disp(sprintf('%i',i));
    [h,p,ci,stats] = ttest2(gemid(i,:),gehigh(i,:),'Vartype','unequal');
    ttests(i) = stats.tstat;
    pval(i) = p;
    mlow(i) = mean(gelow(i,:));
    mmid(i) = mean(gemid(i,:));
end
[~,ind] = sort(ttests);
fid = fopen([prefix '_COMPARISON_MID_HIGH.txt'],'w');
fprintf(fid,sprintf('GENE\t%s_MIDHIGH_TTEST\tMIN_LOG10PVAL\t%s_MID\t%s_HIGH\t%s_HIGHMID_FC\t%s_HIGHMID_RANK\n',prefix,prefix,prefix,prefix,prefix));
for i=1:length(gene_list)
    disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mhigh(ind(i)),mhigh(ind(i))-(mmid(ind(i))+0.0)));
    fprintf(fid,'%s\t%f\t%f\t%f\t%f\t%f\t%f\n',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mhigh(ind(i)),mhigh(ind(i))-(mmid(ind(i))+0.0),round(i-length(gene_list)/2));
end
fclose(fid);

% third, mid vs rest
disp('Mid vs out...');
for i=1:length(gene_list)
    %disp(sprintf('%i',i));
    [h,p,ci,stats] = ttest2(gemid(i,:),geout(i,:),'Vartype','unequal');
    ttests(i) = stats.tstat;
    pval(i) = p;
    mout(i) = mean(geout(i,:));
    mmid(i) = mean(gemid(i,:));
end
[~,ind] = sort(ttests);
fid = fopen([prefix '_COMPARISON_MID_OUT.txt'],'w');
fprintf(fid,sprintf('GENE\t%s_MIDOUT_TTEST\tMIN_LOG10PVAL\t%s_MID\t%s_OUT\t%s_OUTMID_FC\t%s_OUTMID_RANK\n',prefix,prefix,prefix,prefix,prefix));
for i=1:length(gene_list)
    disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mout(ind(i)),mout(ind(i))-(mmid(ind(i))+0.0)));
    fprintf(fid,'%s\t%f\t%f\t%f\t%f\t%f\t%f\n',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mmid(ind(i)),mout(ind(i)),mout(ind(i))-(mmid(ind(i))+0.0),round(i-length(gene_list)/2));
end
fclose(fid);

% fourth, low vs prolif
disp('Low vs prolif...');
for i=1:length(gene_list)
    %disp(sprintf('%i',i));
    [h,p,ci,stats] = ttest2(gelow_nonprolif(i,:),geprolif(i,:),'Vartype','unequal');
    ttests(i) = stats.tstat;
    pval(i) = p;
    mlow_nonprolif(i) = mean(gelow_nonprolif(i,:));
    mprolif(i) = mean(geprolif(i,:));
end
[~,ind] = sort(ttests);
fid = fopen([prefix '_COMPARISON_LOW_PROLIF.txt'],'w');
fprintf(fid,sprintf('GENE\t%s_LOWPROLIF_TTEST\tMIN_LOG10PVAL\t%s_LOW\t%s_PROLIF\t%s_PROLIFLOW_FC\t%s_PROLIFLOW_RANK\n',prefix,prefix,prefix,prefix,prefix));
for i=1:length(gene_list)
    disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mlow_nonprolif(ind(i)),mprolif(ind(i)),mprolif(ind(i))-(mlow_nonprolif(ind(i))+0.0)));
    fprintf(fid,'%s\t%f\t%f\t%f\t%f\t%f\t%f\n',char(gene_list(ind(i))),ttests(ind(i)),-log10(pval(ind(i))),mlow_nonprolif(ind(i)),mprolif(ind(i)),mprolif(ind(i))-(mlow_nonprolif(ind(i))+0.0),round(i-length(gene_list)/2));
end
fclose(fid);
