function corrrected_gene_profile = IC10plus_gene_subtract_trend(prefix,genename,module_tab,data_tab,nbins,thresh_prolif_begin,thresh_ichighhigh)

addpath ../data/

    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);
    i10p = find(strcmp(module_names,'IC10+'));

    data = data_tab.data;
    gene_names = data_tab.textdata(2:end,1);
    i_gene = find(strcmp(gene_names,genename));
    gene_profile = data(i_gene,:)';
    
    ic10score = module_scores(:,i10p);

min10 = min(ic10score)*1;
max10 = max(ic10score)*1;
delta = (max10-min10)/nbins;
intervals10 = min10:delta:max10;

prolif_zone_ind = find((ic10score>thresh_prolif_begin)&(ic10score<thresh_ichighhigh));

x = ic10score(prolif_zone_ind);
y = gene_profile(prolif_zone_ind);
p = polyfit(x,y,1);
estimated = polyval(p,ic10score);

%plot(x,y,'ro'); hold on;
%plot(ic10score,estimated,'bo');

corrrected_gene_profile = gene_profile-estimated+mean(gene_profile);

%plot(ic10score,corrrected_gene_profile,'r.'); hold on;
%plot(ic10score,gene_profile,'b.');



end