prefixes = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
%prefixes = {'pdx352','pdx861','pdx184'};

%min_var = 0.02;
min_expvar = 0.0;

%max_var = 0.1;
max_expvar = 1;
min_var_perc = 20;
max_var_perc = 100;

ranks = zeros(30,length(prefixes));

for i=1:length(prefixes)

fn = [char(prefixes(i)) '_lasso_selection_mean.txt']

tab = importdata(fn);
data = tab.data;
col_names = tab.textdata(1,2:end);
gene_names = tab.textdata(2:end,1);
ic10 = find(strcmp(col_names,'IC10+'));
ic14 = find(strcmp(col_names,'IC14+'));
ic1 = find(strcmp(col_names,'IC1+'));
ic2 = find(strcmp(col_names,'IC2+'));
idxic10 = find(data(:,ic10));
idx_prolif = find(data(:,ic1)|data(:,ic2));

min_var = prctile(data(:,2),min_var_perc);
max_var = prctile(data(:,2),max_var_perc);


subplot(2,5,i);

scatter(data(:,1),data(:,2),2); hold on;
plot(data(idx_prolif,1),data(idx_prolif,2),'g.');
plot(data(idxic10,1),data(idxic10,2),'r.');

% plot([min_var min_var],[min_expvar max_expvar],'k-');
% plot([min_var max_var],[min_expvar min_expvar],'k-');
% plot([max_var max_var],[min_expvar max_expvar],'k-');
% plot([min_var max_var],[max_expvar max_expvar],'k-');


subplot(2,5,5+i);

idx = find((data(:,2)>min_var)&(data(:,3)>min_expvar)&(data(:,2)<max_var)&(data(:,3)<max_expvar));
sums = sum(data(idx,4:end));

plot(sums,'ko-'); hold on;
plot([ic10-3 ic10-3],[0 max(sums)],'b--');
plot([ic14-3 ic14-3],[0 max(sums)],'m--');

title(char(prefixes(i)),'FontSize',14);

[~,p] = sort(sums,'descend');
r = 1:30;
r(p) = r;

ranks(:,i) = r;

end

set(gcf,'Position',[21         172        1228         446]);

for i=1:length(prefixes)

figure;
    
fn = [char(prefixes(i)) '_lasso_selection_mean.txt']

tab = importdata(fn);
data = tab.data;
col_names = tab.textdata(1,2:end);
gene_names = tab.textdata(2:end,1);
min_var = prctile(data(:,2),min_var_perc);
max_var = prctile(data(:,2),max_var_perc);

ic10 = find(strcmp(col_names,'IC10+'));
ic14 = find(strcmp(col_names,'IC14+'));
ic1 = find(strcmp(col_names,'IC1+'));
ic2 = find(strcmp(col_names,'IC2+'));
ic4 = find(strcmp(col_names,'IC4+'));
ic6 = find(strcmp(col_names,'IC6+'));

idx = find((data(:,2)>min_var)&(data(:,3)>min_expvar)&(data(:,2)<max_var)&(data(:,3)<max_expvar));
sums = sum(data(idx,4:end));

plot(sums,'ko-'); hold on;
plot([ic10-3 ic10-3],[0 max(sums)],'b--');
plot([ic14-3 ic14-3],[0 max(sums)],'m--');
plot([ic4-3 ic4-3],[0 max(sums)],'g--');
plot([ic6-3 ic6-3],[0 max(sums)],'g--');
plot([ic1-3 ic1-3],[0 max(sums)],'r--');
plot([ic2-3 ic2-3],[0 max(sums)],'r--');

ylabel('Number of genes','FontSize',20);
title(prefixes(i),'FontSize',20);
%xticklabel(pos_fields);
set(gca,'XTick',1:length(col_names(4:end)));
set(gca,'XTickLabel',col_names(4:end));
set(gca,'XTickLabelRotation',90);
set(gcf,'Color','w');

end

figure;

mr = mean((30-ranks)');
[~,p] = sort(mr,'descend');

bar(mr(p));

ylabel('Mean rank of the IC','FontSize',20);
title('Explaining heterogeneity','FontSize',20);
%xticklabel(pos_fields);
icnames = col_names(4:end);
set(gca,'XTick',1:length(icnames(p)));
set(gca,'YTick',1:length(icnames(p)));
set(gca,'XTickLabel',icnames(p));
set(gca,'YTickLabel',30:-1:1);
set(gca,'XTickLabelRotation',90);
set(gcf,'Color','w');


