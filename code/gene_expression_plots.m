addpath ../data;
%prefix = 'pdx352'; nbins = 30; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0*0.81+0.8775; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861'; nbins = 20; thresh_prolif_begin = 0.65; thresh_ichighhigh = 0.78*0+0.8; thresh_ichigh3 = 0.8517;
%prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434*0+0.47; thresh_ichighhigh = 0.61; thresh_ichigh3 = 0.6453; 
%prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546*0+0.62; thresh_ichighhigh = 0.8237*0+0.94; thresh_ichigh3 = 0.945;
prefix = 'pdx1058'; nbins = 40; thresh_prolif_begin = 0.55; thresh_ichighhigh = 0.78; thresh_ichigh3 = 0.945;
labels = importdata(sprintf('%s_classes_text.txt',prefix));


showborders = 1;

modulex = 'IC10+';
%moduley = 'PID138045_HIF1alpha_transcription_factor_network';
%moduley = 'PID138045_HIF1alpha_transcription_factor_network_filtered';
%genes_hypoxia = {'ALDOA','CA9','SLC2A1','ENO1','HK1','VEGFA','NRN1','LDHA','PKM','MCL1','NDRG1','EGLN3'};
genes_hypoxia = {'ADM';'ALDOA';'BHLHE40';'BNIP3';'CA9';'CREB1';'CREBBP';'EGLN3';'GATA2';'HMOX1';'MCL1';'NDRG1';'PGM1';'SERPINE1';'SMAD3';'TFRC';'VEGFA'};
genes_keggapoptosis_pdx352 = {'BAX';'BID';'CASP6';'CASP7';'CYCS';'FAS';'IL1RAP';'NFKBIA';'PPP3CA';'PRKAR2A';'TNFRSF1A';'TP53'};
genes_keggapoptosis_pdx861 = {'BCL2';'CAPN1';'CASP3';'CASP6';'NFKBIA';'PIK3R1';'PPP3CA';'PPP3CB';'TNFRSF10B';'TNFRSF10D';'TP53'};
genes_oxphos_pdx352 = {'IDH1';'GPI';'ALDH6A1';'MRPS11';'SDHA';'NDUFS8';'HSD17B10';'DLD';'CASP7';'NDUFS1';'ACADM';'AFG3L2';'CYB5A';'FDX1';'IDH2';'SLC25A4';'ATP6AP1';'MRPL34';'ETFA';'COX17';'CPT1A';'PHYH';'TIMM8B';'ACAT1';'LDHB';'POR';'FH';'NDUFA9';'SLC25A5';'BAX';'NDUFA3';'NDUFS6';'CYCS';'GLUD1'};
moduley = genes_hypoxia(3);
moduley = 'MIF';
%moduley = 'IC14+';
%moduley = 'GO0006007_glucose_catabolic_process';
%moduley = 'IC30+';
nintervals = nbins;

module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);
sample_names = module_tab.textdata(2:end,1);

mn = mean(module_scores(:,1:end-2),2);
ind_bad = find(mn<mean(mn)-3*std(mn));
ind_good = find(mn>mean(mn)-3*std(mn));
bad_samples = sample_names(ind_bad);
disp(sprintf('Number of bad samples = %i',length(bad_samples)));
module_scores = module_scores(ind_good,:);
labels = labels(ind_good);
ind_prolif = find(strcmp(labels,'proliferating'));


ge_tab = importdata(sprintf('%s_nufp10k.txt',prefix));
%ge_tab = importdata(sprintf('%s_nufp_ic10corrected.txt',prefix));
ge_val = ge_tab.data;
ge_val = ge_val(:,ind_good);
gene_names = ge_tab.textdata(2:end,1);
iy = find(strcmp(gene_names,moduley))


ix = find(strcmp(module_names,modulex));
%iy = find(strcmp(module_names,moduley));
ic10 = find(strcmp(module_names,'IC10+'));

xv = module_scores(:,ix);
yv = ge_val(iy,:);
yv = yv';
ic10v = module_scores(:,ic10);


scatter(xv,yv,'ko','filled','MarkerFaceColor',[0.5 0.5 0.5]); hold on;

scatter(xv(find(ic10v<thresh_prolif_begin)),yv(find(ic10v<thresh_prolif_begin)),'go','filled');
scatter(xv(find(ic10v>thresh_ichighhigh)),yv(find(ic10v>thresh_ichighhigh)),'mo','filled');


scatter(xv(ind_prolif),yv(ind_prolif),'ro','filled');



xlabel(strrep(modulex,'_',' '),'FontSize',14);
ylabel(strrep(moduley,'_',' '),'FontSize',14);

dx = (max(xv)-min(xv))/nintervals;
intervals = min(xv):dx:max(xv);

[~,intervals_ind] = histc(xv,intervals);

for i=1:nintervals+1
    yv_smooth(i) = median(yv(find(intervals_ind==i)));
    yv_stds(i) = std(yv(find(intervals_ind==i)));
end



plot(intervals,yv_smooth,'g-','LineWidth',4,'Color',[0 0 1]);
plot(intervals,yv_smooth+yv_stds,'g--','LineWidth',1,'Color',[0 0 1]);
plot(intervals,yv_smooth-yv_stds,'g--','LineWidth',1,'Color',[0 0 1]);

if(showborders)
    plot([thresh_prolif_begin thresh_prolif_begin],[min(yv) max(yv)],'r--');
    plot([thresh_ichighhigh thresh_ichighhigh],[min(yv) max(yv)],'r--');
end
set(gcf,'Color','w');

title(prefix,'FontSize',20);