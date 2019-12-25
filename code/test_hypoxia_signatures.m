dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};

hyp_sig_name = 'PID138045_HIF1alpha_transcription_factor_network';
%hypoxia_signature = {'CA9','CITED2','NCOA2','BHLHE41','NPM1','ADM','EPO','SP1','JUN','EGLN3','HIF1A','ARNT','BNIP3','HK1','HK2','IGFBP1','NDRG1','NT5E','CXCR4','FOS','FURIN','EGLN1','FECH','SERPINE1','GATA2','ETS1','PKM','ID2','NCOA1','MCL1','CP','AKT1','GCK','ABCG2','PFKFB3','HMOX1','PFKL','ALDOA','LDHA','HNF4A','ENG','ENO1','TERT','BHLHE40','CREB1','ITGB2','TF','CREBBP','NOS2','COPS5','HDAC7','PGK1','LEP','EP300','SLC2A1','EDN1','CXCL12','PGM1','SMAD3','SMAD4','TFF3','PLIN2','ABCB1','TFRC','VEGFA'};
%hypoxia_signature = {'ALDOA','NDRG1','VEGFA','NRN1','PGK1','EGLN3','CA9','ADM'};
%hypoxia_signature = {'ALDOA','NDRG1','VEGFA','NRN1','PGK1','CA9'};
%hypoxia_signature = {'ALDOA'};
hypoxia_signature = {'CD99','TMEM98','JARID2','AASS','IGF1','IKZF2','DAPK2','TNC','BARX2','ZIC2','ADRB1','LMO3','PTGER3','ALX4','CDK14','CTTNBP2','ACTN2','N4BP2','ITM2A','PKP1','CADPS2','CEACAM6','RCOR1','EFNB1','GLG1','CREM','H2AFY2','DNAL4','ATP11C','PCDH11X','FCGRT','DLX5','CAV2','CAV1','CPVL','TSPAN13','SPATA6L','CDC37L1','SH3PXD2A','EBF3','DNAJC12','CCND1','RNF141','SOX6','SH2B3','WASF1','FAM46A','MAN2A1','C7','HES1','FN1','ID2','EPHA4','OLFML3','KMO','PADI2','TXNIP','AKAP7','OLFM3','KDSR','YPEL5','KIAA1217','NPY','EGR2','ZC3H13','NCKAP1L','NMI','TNFAIP6','PARD6B','COL21A1','PCSK2','BCL11B','TSPAN8','TUBA4A','HOXD13','TRPM4','OLFM1','SYT4','TPTE2','GSTM1','SYT6','HOOK1','ANXA1','CAPRIN1','MYC','CPEB2','SLCO5A1','TGS1','FAM117B','GLCE','NDST4','DUSP6','WDFY2','SLAIN1','SORD','CDH11','IFITM3','RERE','HMCN1','RGL1','SCN1A','MARCH1','MYO10','BDP1','TENM2','DCDC2','PRSS35','NLGN4X','TMEM47','AK3','CER1','LRRC4C','FADS1','CDH8','DLG2','FLI1','KCNE4','UHMK1','LONRF1','APCDD1','DKK2','SLC26A2','GDF6','STEAP2','FZD1','KIT','ADAMTS4','CACHD1','EPB41','CSRP1','PRAC','JAK1','RAVER2','NTNG1','CLDN1','ATP1A1','ABHD6','NPY1R','NPY5R','STEAP1','ZNF704','CCDC171','OTUD1','FAM123A','KIAA1462','CACNB2','CYYR1','MFAP4','PRKCB','FAM111A','LOXHD1','RHOH','GFRA2','UGT3A2','FAM84B','GSTM4','ZEB2','LDB2','PCDH7','CHD3','FOS','DLGAP1','GSTA4','MLLT3','ADRA1D','ID4','SLFN11','MYEOV','GLRX','DAB1','SMARCC1','GPR64','NET1','FZD4','CALCB','HOXD8','JAKMIP2','SNX18','SLC17A8','ALX1','TSPYL5','PENK','PAPPA','LHFP','NELL2','ROBO2','FLRT2','RBM11','PBX1','IFITM1','IRS2','WWOX','TRDN','CCK','FNBP1','DCC','LIPI','IL1RAP','AKR1C3','FAT4','MMP1','DAPK1','ENPP1','FAM49A','SIRPA','PAX9','CES1','DCLRE1A','NPTXR','RP13-16H11.2','AC022311.1','RP13-16H11.1','AC073135.3','KIAA1456','RP11-6C14.1','RP11-1258F18.1','RP11-521M14.1','TRAV1-2','RP11-252A24.7','RP11-18I14.10'};

disp(sprintf('The size of the %s signature: %i genes',char(hyp_sig_name),length(hypoxia_signature)));



%preload data and then comment
%  tic;
%    for i=1:length(dsets)
%        disp(sprintf('%s',char(dsets(i))));
%        tab{i} = importdata(sprintf('%s_nufp10k.txt',char(dsets(i))));
%        dat{i} = tab{i}.data;
%        gnames{i} = tab{i}.textdata(2:end,1);
%        snames{i} = tab{i}.textdata(1,2:end);
%    end
%  toc;
%  tic;
%    for i=1:length(dsets)
%         prefix = dsets{i};
%         module_tabs{i} = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
%         module_score_tables{i} = module_tabs{i}.data;
%         module_name_tables{i} = module_tabs{i}.textdata(1,2:end);
%    end
%  toc;


for k=1:length(dsets)
    
dset = dat{k};

nbins = size(dset,1)/200;

gene_names = gnames{k};
vr = var(dset');
[~,p] = sort(vr,'descend');
mostvar_genenames = gene_names(p(1:2000));
selected_sign_genes_inds = find(ismember(hypoxia_signature,mostvar_genenames));
selected_sign_genes = hypoxia_signature(selected_sign_genes_inds);

module_scores = module_score_tables{k};
mn = mean(module_scores(:,1:end-2),2);
ind_bad = find(mn<mean(mn)-2*std(mn));
ind_good = find(mn>mean(mn)-2*std(mn));
disp(sprintf('Number of bad samples = %i',length(ind_bad)));
 module_scores = module_scores(ind_good,:);
 dset = dset(:,ind_good);

disp(sprintf('Most var %i genes',length(selected_sign_genes)));

ind_ic10 = find(strcmp(module_name_tables{k},'IC10+'));
ic10_scores = module_scores(:,ind_ic10);

sign_inds = find(ismember(gene_names,selected_sign_genes));
gene_names(sign_inds)
hypoxia_scores = mean(dset(sign_inds,:),1);
%sel = dset(sign_inds,:);
%[v,hypoxia_scores,s] = pca(sel','NumComponents',1);

% sel = dset(sign_inds,:);
% for j=1:length(sign_inds)
%     [xsm, ysm] = smooth_sliding_window(ic10_scores,sel(j,:),4);
%     sel(j,:) = ysm;
% end
% hypoxia_scores = mean(sel,1);

[x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth1(ic10_scores,hypoxia_scores,nbins);

subplot(2,3,k);
plot(ic10_scores,hypoxia_scores,'b.'); hold on;
plot(x_smoothed,y_smoothed,'r-','MarkerSize',3,'LineWidth',2);
%plot(ic10_scores(ind_bad),hypoxia_scores(ind_bad),'kx');
title(dsets(k));
xlim([min(ic10_scores) max(ic10_scores)]);
%ylim([min(hypoxia_scores) max(hypoxia_scores)]);
ylim([prctile(hypoxia_scores,2) prctile(hypoxia_scores,100)]);
end

set(gcf,'Position',[54    72   814   540]);