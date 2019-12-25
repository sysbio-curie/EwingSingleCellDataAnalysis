%%prefix = 'pdx857';
%%prefix = 'pdx471';
%%prefix = 'pdx1057';


addpath ../data;
%prefix = 'pdx352';
%prefix = 'pdx861';
%prefix = 'pdx184';
%prefix = 'pdx856';
%prefix = 'pdx1058';

pdxs = {'pdx352','pdx861','pdx184','pdx856','pdx1058'};
%pos_fields = {'IC1+','IC2+','IC3+','IC4+','IC5+','IC6+','IC7+','IC8+','IC9+','IC10+','IC11+','IC12+','IC13+','IC14+','IC15+','IC16+','IC17+','IC18+','IC19+','IC20+','IC21+','IC22+','IC23+','IC24+','IC25+','IC26+','IC27+','IC28+','IC29+','IC30+'};
pos_fields = {'IC1+','IC2+','IC4+','IC6+','IC9+','IC10+','IC14+'};


ranks = zeros(length(pos_fields),length(pdxs));
crps = zeros(length(pos_fields),length(pos_fields),length(pdxs));
pvals = zeros(length(pos_fields),length(pos_fields),length(pdxs));

for i=1:length(pdxs)

prefix = pdxs{i};
module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);

inds = find(ismember(module_names,pos_fields));

module_scores = module_scores(:,inds);

%[crp,pval] = partialcorr(module_scores,'type','Spearman');
%crp = partialcorr(module_scores,'type','Pearson');
crp = corr(module_scores,'type','Spearman');
%crp = corr(module_scores,'type','Pearson');
crps(:,:,i) = crp;
pvals(:,:,i) = pval;
%crp0 = crp; crp0(crp<0)=0;
%plot(sum(crp0,1),'ko-');

hm = HeatMap(crp,'RowLabels',pos_fields,'ColumnLabels',pos_fields,'Symmetric',1,'ColorMap','jet');
hm.addTitle(pdxs{i},'FontSize',20);
%plot(hm); colorbar;
%colorbar;
drawnow;
%title(pdxs{i},'FontSize',20);
%figure;
%plot(sum(abs(crp),1),'ko-');
%plot(sum(abs(crp),1),'ko-');

%x = sum(abs(crp),1);

% x = sum(crp0,1);
% 
% [~,p] = sort(x,'descend');
% r = 1:length(x);
% r(p) = r;
% for j=1:length(x)
%     display(sprintf('%s: %i',pos_fields{j},r(j)));
% end
% ranks(:,i) = r;

end

mcrp = mean(crps,3);
hm = HeatMap(mcrp,'RowLabels',pos_fields,'ColumnLabels',pos_fields,'Symmetric',1,'DisplayRange',1,'ColorMap','jet');
hm.addTitle('Mean Spearman correlation for 5 PDXs','FontSize',20);
drawnow;

mpval = mean(-log10(pvals*100+1e-300),3);

hm = HeatMap(mpval,'RowLabels',pos_fields,'ColumnLabels',pos_fields,'Symmetric',0,'ColorMap','hot','DisplayRange',100);
hm.addTitle('Mean of -log10 pvalues, adjusted','FontSize',20);


% mranks = mean(ranks');
% [~,p] = sort(mranks,'ascend');
% p