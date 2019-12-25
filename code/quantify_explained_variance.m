addpath ../data;
prefix = 'pdx352';
%prefix = 'pdx861';
%prefix = 'pdx184';

module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);

pos_fields = {'IC1+','IC2+','IC3+','IC4+','IC5+','IC6+','IC7+','IC8+','IC9+','IC10+','IC11+','IC12+','IC13+','IC14+','IC15+','IC16+','IC17+','IC18+','IC19+','IC20+','IC21+','IC22+','IC23+','IC24+','IC25+','IC26+','IC27+','IC28+','IC29+','IC30+'};
inds = find(ismember(module_names,pos_fields));

%data_tab = importdata(sprintf('%s_nufp2k.txt',prefix));
%data_tab = importdata(sprintf('%s_nufp.txt',prefix));
data_tab = importdata(sprintf('%s_nufp10k.txt',prefix));
data = data_tab.data;
cell_names = data_tab.textdata(1,2:end);
gene_names = data_tab.textdata(2:end,1);

data_mean = mean(data,1);
data_centered = bsxfun(@minus, data, data_mean);

msz = zscore(module_scores);

sc1 = data_centered*msz;
std_sc1 = std(sc1);
s2_sc1 = mean(sc1.^2);


%plot(std_sc1(inds),'ko-'); set(gca,'XTick',1:length(inds)); set(gca,'XTickLabel',module_names(inds));
plot(s2_sc1(inds),'ko-'); set(gca,'XTick',1:length(inds)); set(gca,'XTickLabel',module_names(inds));


figure;
[v,u,s] = pca(data','NumComponents',5);
umsz = u'*msz;
plot(umsz(1,inds),'bo-'); set(gca,'XTick',1:length(inds)); set(gca,'XTickLabel',module_names(inds));
hold on;
plot(umsz(2,inds),'ro-'); 
plot(umsz(3,inds),'go-'); 
legend(['PC1','PC2','PC3']);