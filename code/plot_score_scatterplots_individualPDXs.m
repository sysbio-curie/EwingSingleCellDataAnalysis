addpath ../data;

xaxis = 'IC-EwS';
%xaxis = 'CCPtime';
%gene = 'NRN1';
moreopt_threshold = 0.1;
nbins = 100;

%dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
dsets = {'pdx352'};
dsets = {'pdx861'};
dsets = {'pdx184'};
dsets = {'pdx1058'};
dsets = {'pdx856'};
colorseq = {'r','g','b','m','k'};

module1_names = {'IC2+','IC10+','IC10+','IC10+','IC1+','IC10+','HALLMARK_OXIDATIVE_PHOSPHORYLATION','GO0006007_glucose_catabolic_process'};
module2_names = {'IC1+','IC1+','PID138045_HIF1alpha_transcription_factor_network',...
    'HALLMARK_OXIDATIVE_PHOSPHORYLATION','HALLMARK_OXIDATIVE_PHOSPHORYLATION',...
    'GO0006007_glucose_catabolic_process','GO0006007_glucose_catabolic_process','PID138045_HIF1alpha_transcription_factor_network'};

modulename_synonyms = containers.Map;
modulename_synonyms('IC1+') = 'IC-G2/M';
modulename_synonyms('IC2+') = 'IC-G1/S';
modulename_synonyms('IC10+') = 'IC-EwS';
modulename_synonyms('PID138045_HIF1alpha_transcription_factor_network') = 'HIF1A network';
modulename_synonyms('HALLMARK_OXIDATIVE_PHOSPHORYLATION') = 'OXPHOS';
modulename_synonyms('GO0006007_glucose_catabolic_process') = 'GLYCOLYSIS';

% module1_names = {'IC2+','IC10+','IC10+','IC10+','IC1+','IC10+','HALLMARK_OXIDATIVE_PHOSPHORYLATION','GO0006007_glucose_catabolic_process'};
% module2_names = {'IC1+','IC1+','IC14+',...
%     'HALLMARK_OXIDATIVE_PHOSPHORYLATION','HALLMARK_OXIDATIVE_PHOSPHORYLATION',...
%     'GO0006007_glucose_catabolic_process','GO0006007_glucose_catabolic_process','IC14+'};
% 
% modulename_synonyms = containers.Map;
% modulename_synonyms('IC1+') = 'IC-G2/M';
% modulename_synonyms('IC2+') = 'IC-G1/S';
% modulename_synonyms('IC10+') = 'IC-EwS';
% modulename_synonyms('IC14+') = 'HIF1A network';
% modulename_synonyms('HALLMARK_OXIDATIVE_PHOSPHORYLATION') = 'OXPHOS';
% modulename_synonyms('GO0006007_glucose_catabolic_process') = 'GLUCOLYSIS';


prefix = dsets{1};

figure('units','normalized','outerposition',[0 0.05 1 0.9])


    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);


mn = mean(module_scores(:,1:end-2),2);
ind_bad = find(mn<mean(mn)-2*std(mn));
ind_good = find(mn>mean(mn)-2*std(mn));
disp(sprintf('Number of bad samples = %i',length(ind_bad)));
module_scores = module_scores(ind_good,:);    
    
ic1 = module_scores(:,1);
ic2 = module_scores(:,2);
ic10 = module_scores(:,find(strcmp(module_names,'IC10+')));
data_cc = [ic1 ic2];
data_cc = data_cc/prctile(data_cc(:,1)+data_cc(:,2),98);
module_scores(:,1) = data_cc(:,1);
module_scores(:,2) = data_cc(:,2);
ic1 = module_scores(:,1);
ic2 = module_scores(:,2);
inds_proliferating = find((ic1>0.25)|(ic2>0.25));
if strcmp(prefix,'pdx1058')
    inds_proliferating = find((ic1>0.3)|(ic2>0.3));
end
if strcmp(prefix,'pdx856')
    inds_proliferating = find((ic1>0.3)|(ic2>0.3));
end

%ic10_median = median(ic10(inds_proliferating));
%ic10_median = prctile(ic10(inds_proliferating),50);
%ic10_median = mean(ic10(inds_proliferating));
ic10_left = prctile(ic10(inds_proliferating),2.5);
ic10_right = prctile(ic10(inds_proliferating),97.5);
ic10_median = (ic10_left+ic10_right)/2;
if strcmp(prefix,'pdx856')
    ic10_left = prctile(ic10(inds_proliferating),2.5);
    ic10_right = prctile(ic10(inds_proliferating),85);
    ic10_median = (ic10_left+ic10_right)/2;
end

for k=1:length(module1_names)

    par1 = module1_names{k};
    par2 = module2_names{k};
    
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    
    notp = setdiff(1:size(module_scores,1),inds_proliferating);
    
    subplot(2,4,k);

    %dscatter(module_scores(notp,i1),module_scores(notp,i2)); hold on;
    scatter(module_scores(notp,i1),module_scores(notp,i2),1); hold on;
    markersize = 2;
    plot(module_scores(inds_proliferating,i1),module_scores(inds_proliferating,i2),'ro','MarkerSize',markersize,'MarkerFaceColor','r'); hold on;
    par1_s = modulename_synonyms(par1);
    par2_s = modulename_synonyms(par2);
    xlabel(strrep(par1_s,'_',' '),'FontSize',20);
    ylabel(strrep(par2_s,'_',' '),'FontSize',20);
    set(gca,'FontSize',14);

     if ~(strcmp(par1,'IC2+')&strcmp(par2,'IC1+'))
        cut = 0;
        nbins = size(module_scores,1)/30;
        if strcmp(prefix,'pdx861')
            cut = 2;
            nbins = 25;
        end
        if strcmp(prefix,'pdx184')
            nbins = 20;
        end
        if strcmp(prefix,'pdx1058')
            nbins = 30;
        end
        
        [x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth1(module_scores(:,i1),module_scores(:,i2),nbins);        
        plot(x_smoothed(1:end-cut),y_smoothed(1:end-cut),'k-','LineWidth',4,'MarkerSize',2);
    end
    
%     xlim([min(module_scores(:,i1)) max(module_scores(:,i1))]);
%     ylim([min(module_scores(:,i2)) max(module_scores(:,i2))]);

    xlim([prctile(module_scores(:,i1),0) prctile(module_scores(:,i1),100)]);
    ylim([prctile(module_scores(:,i2),0) prctile(module_scores(:,i2),100)]);
    
    if strcmp(par2,'PID138045_HIF1alpha_transcription_factor_network')
        ylim([prctile(module_scores(:,i2),1) prctile(module_scores(:,i2),99)]);
    end
    if strcmp(par1,'HALLMARK_OXIDATIVE_PHOSPHORYLATION')
        xlim([prctile(module_scores(:,i1),1) prctile(module_scores(:,i1),100)]);
    end
    if strcmp(par2,'HALLMARK_OXIDATIVE_PHOSPHORYLATION')
        ylim([prctile(module_scores(:,i2),1) prctile(module_scores(:,i2),100)]);
    end
    
    if strcmp(par1,'IC10+')
        plot([ic10_median ic10_median],[min(module_scores(:,i2)) max(module_scores(:,i2))],'r-');
        plot([ic10_left ic10_left],[min(module_scores(:,i2)) max(module_scores(:,i2))],'r--');
        plot([ic10_right ic10_right],[min(module_scores(:,i2)) max(module_scores(:,i2))],'r--');
    end

end

%set(gcf,'Position',[19         983        1744         618]);
set(gcf,'Color','w');