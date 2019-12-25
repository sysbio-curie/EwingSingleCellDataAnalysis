%prefix = 'pdx856';
%prefix = 'pdx861';
% prefix = 'pdx184';
%%prefix = 'pdx352';
%prefix = 'pdx471';
%prefix = 'pdx857';
prefix = 'pdx1058';
nbins = 50;

close all;
addpath ../data/;

path = ['../data/' prefix];

qct = importdata([path '_nu_qcdata.txt']);

ls = importdata([path '_goodcells.naive']);
lspool = importdata([path '_goodcells.txt']);

indpool = find(ismember(qct.textdata(2:end,1),lspool));
ind = find(ismember(qct.textdata(2:end,1),ls));

mitscore_pooled_table = importdata([prefix '_nufp2k.txt.moduleAverages']);
mit_score_pooled = mitscore_pooled_table.data(:,find(strcmp(mitscore_pooled_table.textdata(1,2:end),'Ilicic2016')));
mitscore_samples = mitscore_pooled_table.textdata(2:end,1);

subplot(2,3,1);

hist(qct.data(:,1),nbins);  xlabel('MIT SCORE (raw)'); title(prefix,'FontSize',14);

subplot(2,3,2);

dscatter(qct.data(:,3),qct.data(:,1)); title(prefix,'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);

subplot(2,3,3);

dscatter(qct.data(:,3),qct.data(:,1)); title([prefix ' (naive selection)'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);
hold on;
plot(qct.data(ind,3),qct.data(ind,1),'ro','MarkerSize',3);

subplot(2,3,4);

hist(mit_score_pooled,nbins);  xlabel('MIT SCORE (pooled)'); title(prefix,'FontSize',14);

subplot(2,3,5);

dscatter(qct.data(:,3),qct.data(:,1)); title([prefix ' (pooled selection)'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);
hold on;
plot(qct.data(indpool,3),qct.data(indpool,1),'ro','MarkerSize',3);

subplot(2,3,6);

dscatter(qct.data(indpool,3),mit_score_pooled); title([prefix ' (pooled selection)'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (pooled)'); xlim([0 8e4]);
hold on;
plot(qct.data(indpool,3),mit_score_pooled,'ro','MarkerSize',3);


set(gcf,'Position',[22         270        1215         332]);

figure;

raw_module_scores = importdata([prefix '_nu2k.txt.moduleAverages']);
ic1_score = raw_module_scores.data(:,find(strcmp(raw_module_scores.textdata(1,2:end),'IC1+')));
ic2_score = raw_module_scores.data(:,find(strcmp(raw_module_scores.textdata(1,2:end),'IC2+')));
ic10_score = raw_module_scores.data(:,find(strcmp(raw_module_scores.textdata(1,2:end),'IC10+')));
EGH_score = raw_module_scores.data(:,find(strcmp(raw_module_scores.textdata(1,2:end),'ECM_HYPOXIA_ATTRACTOR')));

subplot(1,3,1);
scatter(qct.data(:,3),qct.data(:,1),3*ones(1,length(ic1_score)),winsor(ic1_score,[2 98])); colormap jet; 
title([prefix ' ic1 score'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);

subplot(1,3,2);
scatter(qct.data(:,3),qct.data(:,1),3*ones(1,length(ic1_score)),winsor(ic10_score,[2 98])); colormap jet; 
title([prefix ' ic10 score'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);

subplot(1,3,3);
scatter(qct.data(:,3),qct.data(:,1),3*ones(1,length(ic1_score)),winsor(EGH_score,[2 98])); colormap jet; 
title([prefix ' EGH score'],'FontSize',14); xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]);

set(gcf,'Position',[ 47         336        1173         282]);

figure;

subplot(2,2,1);

 percentage_scores = importdata([prefix '_u.txt.rcPercentage']);
 %mit_percentage = percentage_scores.data(:,find(strcmp(percentage_scores.textdata(1,2:end),'Ilicic2016')));
 mit_percentage = qct.data(:,2);
 ic10_percentage = percentage_scores.data(:,find(strcmp(percentage_scores.textdata(1,2:end),'IC10+')));
% scatter(qct.data(:,2),qct.data(:,1),3*ones(1,length(ic1_score)),winsor(mit_percentage,[2 98])); colormap jet; 
% xlabel('TOTAL COUNT'); ylabel('MIT SCORE (raw)'); xlim([0 8e4]); 
hist(mit_percentage,nbins);
xlabel('MIT PERCENTAGE');
title(prefix,'FontSize',14);

subplot(2,2,2);

hist(qct.data(qct.data(:,3)<100000,3),nbins);
xlabel('TOTAL COUNT'); 
title([prefix ' ' sprintf('Median=%i',median(qct.data(:,3)))]);


subplot(2,2,3);
dscatter(qct.data(:,3),mit_percentage); 
xlabel('TOTAL COUNT'); ylabel('MIT PERCENTAGE'); xlim([0 8e4]); 


subplot(2,2,4);
dscatter(mit_percentage, ic10_percentage); colormap jet; 
xlabel('MIT PERCENTAGE'); ylabel('IC10+ PERCENTAGE'); 

%  dscatter(qct.data(:,3),mit_percentage); hold on;
%  plot(qct.data(indpool,3),mit_percentage(indpool),'ro','MarkerSize',2);
%  xlabel('TOTAL COUNT'); ylabel('MIT PERCENTAGE'); xlim([0 8e4]); 

set(gcf,'Position',[ 53    74   801   559]);