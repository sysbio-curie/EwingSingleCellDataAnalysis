function [score_negative,score_positive,p,mn,prolif_bias,pb,pb_slope,score_negative1,score_positive1,ic1corr,ic2corr,ic14corr,icEGHcorr,ic30corr] = IC10plus_gene_profiling(prefix,genename,data_tab,nbins,thresh_prolif_begin,thresh_ichighhigh,plotting)

addpath ../data/

%prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81;
%prefix = 'pdx861'; nbins = 30; thresh_prolif_begin = 0.612; thresh_ichighhigh = 0.78;
%prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434; thresh_ichighhigh = 0.61;
%prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546; thresh_ichighhigh = 0.945;

%genename = 'CCND1'; plotting = 1;


clusters_tab = importdata(sprintf('%s_nufp_IC10+_T.txt_clusters2',prefix));
clusters = clusters_tab(2:end);
inds_proliferating = find(strcmp(clusters,'proliferating'));

    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);
    i10p = find(strcmp(module_names,'IC10+'));
    i1p = find(strcmp(module_names,'IC1+'));
    i2p = find(strcmp(module_names,'IC2+'));
    i14p = find(strcmp(module_names,'IC14+'));    
    iEGHp = find(strcmp(module_names,'ECM_HYPOXIA_ATTRACTOR'));    
    i30p = find(strcmp(module_names,'IC30+'));    


%data_tab = importdata(sprintf('%s_nufp.txt',prefix));
data = data_tab.data;
gene_names = data_tab.textdata(2:end,1);
i_gene = find(strcmp(gene_names,genename));
gene_profile = data(i_gene,:)';
    
ic10score = module_scores(:,i10p);
ic1score = module_scores(:,i1p);
ic2score = module_scores(:,i2p);
ic14score = module_scores(:,i14p);
icEGHscore = module_scores(:,iEGHp);
ic30score = module_scores(:,i30p);

min10 = min(ic10score)*1;
max10 = max(ic10score)*1;
delta = (max10-min10)/nbins;
intervals10 = min10:delta:max10;

[p10,h10] = histc(ic10score,intervals10);

for i=1:length(intervals10)
    inds = find(h10==i);
    ind_prof_i = intersect(inds,inds_proliferating);
    p_prolif(i) = length(ind_prof_i)/length(inds);
    num_prolif(i) = length(ind_prof_i);
    smoothed_gene_profile(i) = mean(gene_profile(inds));
end

for i=2:length(intervals10)-1
    if isnan(smoothed_gene_profile(i))
        smoothed_gene_profile(i) = (smoothed_gene_profile(i-1)+smoothed_gene_profile(i+1))/2;
    end
end

count_above=0;
for i=1:length(intervals10)
    inds = find(h10==i);
    inds_pr = intersect(inds,inds_proliferating);
    inds_pr_above = find(gene_profile(inds_pr)>smoothed_gene_profile(i));
    count_above=count_above+length(inds_pr_above);
    if(length(inds_pr)==0)
        prolif_bias(i) = NaN;
        prolif_mean(i) = NaN;
    else
        prolif_bias(i) = length(inds_pr_above)/length(inds_pr);
        prolif_mean(i) = mean(gene_profile(inds_pr));
    end
end
pb = count_above/length(inds_proliferating);

prolif_zone_ind = find((ic10score>thresh_prolif_begin)&(ic10score<thresh_ichighhigh));
prolif_zone_intervals_ind = find((intervals10>thresh_prolif_begin)&(intervals10<thresh_ichighhigh));
below_prolif_ind = find(ic10score<thresh_prolif_begin);
above_prolif_ind = find(ic10score>thresh_ichighhigh);
below_prolif_intervals_ind = find(intervals10<thresh_prolif_begin);
above_prolif_intervals_ind = find(intervals10>thresh_ichighhigh);


shift = 0.1-min(gene_profile);

% Computing the slope for proliferation bias in proliferation zone
x = intervals10(prolif_zone_intervals_ind);
y = prolif_bias(prolif_zone_intervals_ind);
inds = find(~isnan(y));
x = x(inds);
y = y(inds);
pb_par = polyfit(x,y,1);
pb_slope = pb_par(1);


if plotting

rectangle('Position',[thresh_prolif_begin 0 (thresh_ichighhigh-thresh_prolif_begin) 1],'FaceColor',[0.8 0.8 0.8],'EdgeColor',[0.8 0.8 0.8]); hold on;
plot(intervals10,p_prolif,'r-','LineWidth',3); hold on; 
plot(intervals10,p10/max(p10),'b-','LineWidth',1);
plot(intervals10,num_prolif/max(num_prolif),'r-','LineWidth',1);
plot(intervals10(prolif_zone_intervals_ind),prolif_bias(prolif_zone_intervals_ind),'k-','LineWidth',1,'Color',[0.9 0.5 0.5]);
thresh = 5;
plot([min10 max10],[max(p_prolif)/thresh max(p_prolif)/thresh],'r--');
%plot([thresh_ichighhigh thresh_ichighhigh],[0 1],'r--');
%plot([thresh_prolif_begin thresh_prolif_begin],[0 1],'r--');

% figure;
%     i1 = find(strcmp(module_names,'IC1+'));
%     i2 = find(strcmp(module_names,'IC2+'));
%     plot(module_scores(:,i1),module_scores(:,i2),'.'); hold on;
%     plot(module_scores(inds_proliferating,i1),module_scores(inds_proliferating,i2),'r.'); 

dscatter(ic10score,gene_profile+1+shift);
plot(ic10score(inds_proliferating),gene_profile(inds_proliferating)+1+shift,'ro');
plot([min10 max10],[1 1],'k--');

plot([min10 max10],[0.5 0.5],'k--');
plot([min10 max10],[0.2 0.2],'k--');

plot(intervals10,smoothed_gene_profile+1+shift,'g-','LineWidth',6);

end

%%%%%%%%%%%%%%%%%%%%% analyis




x = ic10score(prolif_zone_ind);
y = gene_profile(prolif_zone_ind);

p = polyfit(x,y,1);
ic10score_sorted = sort(ic10score);
yps = polyval(p,ic10score_sorted);
yp = polyval(p,ic10score);
yp_intervals = polyval(p,intervals10);

% projected_negative = mean(yp(below_prolif_ind));
% real_negative = mean(gene_profile(below_prolif_ind));
% projected_positive = mean(yp(above_prolif_ind));
% real_positive = mean(gene_profile(above_prolif_ind));

yp_intervals(find(yp_intervals<0)) = 0;
yps(find(yps<0)) = NaN;

projected_negative = mean(yp_intervals(below_prolif_intervals_ind));
real_negative = mean(smoothed_gene_profile(below_prolif_intervals_ind));
projected_positive = mean(yp_intervals(above_prolif_intervals_ind));
real_positive = mean(smoothed_gene_profile(above_prolif_intervals_ind));

mn = mean(y);
score_negative = (real_negative-projected_negative);
score_positive = (real_positive-projected_positive);

score_negative1 = 0;
for i=1:length(below_prolif_intervals_ind)
    i1 = below_prolif_intervals_ind(i);
    contr = smoothed_gene_profile(i1)-yp_intervals(i1);
    w = length(find(h10==i1))/length(below_prolif_ind);
    score_negative1=score_negative1+contr*w;
end

score_positive1 = 0;
for i=1:length(above_prolif_intervals_ind)
    i1 = above_prolif_intervals_ind(i);
    contr = smoothed_gene_profile(i1)-yp_intervals(i1);
    w = length(find(h10==i1))/length(above_prolif_ind);
    score_positive1=score_positive1+contr*w;
end


if plotting

plot(ic10score_sorted,yps+1+shift,'r--');    
    
plot([min10 thresh_prolif_begin],[projected_negative+1+shift projected_negative+1+shift],'r-','LineWidth',4);
plot([thresh_ichighhigh max10],[projected_positive+1+shift projected_positive+1+shift],'r-','LineWidth',4);

plot([min10 thresh_prolif_begin],[real_negative+1+shift real_negative+1+shift],'g-','LineWidth',4,'Color',[0 0.7 0]);
plot([thresh_ichighhigh max10],[real_positive+1+shift real_positive+1+shift],'g-','LineWidth',4,'Color',[0 0.7 0]);

plot(intervals10(prolif_zone_intervals_ind),prolif_mean(prolif_zone_intervals_ind)+1+shift,'k-','LineWidth',3,'Color',[0.9 0.5 0.5]);

title(sprintf('%s (n=%2.2f p=%2.2f a=%2.2f pb=%2.2f/%2.2f)',char(genename),score_negative,score_positive,p(1),pb,pb_slope),'FontSize',14);
set(gcf,'Position',[360    84   502   534]);
xlabel('IC10+ score','FontSize',16);
end

ic1corr(1) = corr(gene_profile(below_prolif_ind),ic1score(below_prolif_ind),'type','Spearman');
ic1corr(2) = corr(gene_profile(prolif_zone_ind),ic1score(prolif_zone_ind),'type','Spearman');
ic1corr(3) = corr(gene_profile(above_prolif_ind),ic1score(above_prolif_ind),'type','Spearman');
ic1corr(4) = max(abs(ic1corr));

ic2corr(1) = corr(gene_profile(below_prolif_ind),ic2score(below_prolif_ind),'type','Spearman');
ic2corr(2) = corr(gene_profile(prolif_zone_ind),ic2score(prolif_zone_ind),'type','Spearman');
ic2corr(3) = corr(gene_profile(above_prolif_ind),ic2score(above_prolif_ind),'type','Spearman');
ic2corr(4) = max(abs(ic2corr));

ic14corr(1) = corr(gene_profile(below_prolif_ind),ic14score(below_prolif_ind),'type','Spearman');
ic14corr(2) = corr(gene_profile(prolif_zone_ind),ic14score(prolif_zone_ind),'type','Spearman');
ic14corr(3) = corr(gene_profile(above_prolif_ind),ic14score(above_prolif_ind),'type','Spearman');
ic14corr(4) = max(abs(ic14corr));

icEGHcorr(1) = corr(gene_profile(below_prolif_ind),icEGHscore(below_prolif_ind),'type','Spearman');
icEGHcorr(2) = corr(gene_profile(prolif_zone_ind),icEGHscore(prolif_zone_ind),'type','Spearman');
icEGHcorr(3) = corr(gene_profile(above_prolif_ind),icEGHscore(above_prolif_ind),'type','Spearman');
icEGHcorr(4) = max(abs(icEGHcorr));

ic30corr(1) = corr(gene_profile(below_prolif_ind),ic30score(below_prolif_ind),'type','Spearman');
ic30corr(2) = corr(gene_profile(prolif_zone_ind),ic30score(prolif_zone_ind),'type','Spearman');
ic30corr(3) = corr(gene_profile(above_prolif_ind),ic30score(above_prolif_ind),'type','Spearman');
ic30corr(4) = max(abs(ic30corr));

end