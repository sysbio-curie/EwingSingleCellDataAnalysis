addpath ../data/
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\core_algorithm\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\utils\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\visualization\

dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
colorseq = {'r','g','b','m','k'};
% code to compute library size
% tab = importdata('pdx861_u1.txt'); x = tab.data; ls = mean(sum(x)); disp(ls); 
lsizes = [14547,15625,3486,9984,5844];



% par1 = 'IC10+';
% par2 = 'PID138045_HIF1alpha_transcription_factor_network';
 par1 = 'IC2+';
 par2 = 'IC1+';

%dsets = {'pdx1058'}

cell_names_all = {};

for i = 1:length(dsets)
    disp(sprintf('%s',char(dsets(i))));
    module_tabs(i) = importdata(sprintf('%s_nufp2k.txt.moduleAverages',char(dsets(i))));
    cell_names = module_tabs(i).textdata(2:end,1);
    scores = module_tabs(i).data;
    mn = mean(scores(:,1:end-2),2);
    ind_good = find(mn>mean(mn)-3*std(mn));
    disp(sprintf('Threshold: %f, %i/%i good cells\n',mean(mn)-3*std(mn),length(ind_good),size(scores,1)));
    scores = scores(ind_good,:);
    module_scores_all{i} = scores;
    cell_names = cell_names(ind_good);
    for k=1:length(cell_names)
        cell_names(k) = {sprintf('%s\t%s',char(cell_names(k)),char(dsets(i)))};
    end
    cell_names_all = [cell_names_all;cell_names];
end

data_cc_global = [];

k =1;
for pref=dsets
    disp(sprintf('%s',char(pref)));
    %module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',char(pref)));
    module_tab = module_tabs(k);
    %module_scores = module_tab.data;
    module_scores = module_scores_all{k};
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    data_cc = [ module_scores(:,i1) module_scores(:,i2)];
    int_non_prolif = data_cc(:,1)<0.6 & data_cc(:,2)<0.6;
    shiftx = median(data_cc(int_non_prolif,1));
    shifty = median(data_cc(int_non_prolif,2));
    data_cc = data_cc-repmat([shiftx shifty],size(data_cc,1),1);
    %data = data/log10(lsizes(k));
    data_cc = data_cc/prctile(data_cc(:,1)+data_cc(:,2),98);
    plot(data_cc(:,1),data_cc(:,2),'k.','Color',colorseq{k}); hold on;
    data_cc_global = [data_cc_global;data_cc];
    %plot(,median(data(int_non_prolif,2)),'ro','Color',colorseq{k},'MarkerSize',40);
    plot([0 0],[-0.2 0.8],'k--');
    plot([-0.2 0.8],[0 0],'k--');
    k = k+1;
end
xlabel('IC\_G1/S','FontSize',20); ylabel('IC\_G2/M','FontSize',20);
axis equal; 
xlim([-0.2 0.6])
ylim([-0.2 0.8])
set(gca,'FontSize',12);
set(gcf,'Color','w');

% ============================================================

figure;
k =1;
tot = 0;
 par1 = 'IC10+';
 par2 = 'IC2+';
 data_ic10_global = [];
for pref=dsets
    disp(sprintf('%s',char(pref)));
    module_tab = module_tabs(k);
    %module_scores = module_tab.data;
    module_scores = module_scores_all{k};
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    
    npoints = size(module_scores,1);
    g1s = data_cc_global(tot+1:tot+npoints,1);
    g2m = data_cc_global(tot+1:tot+npoints,2);
    tot = npoints;

    ind_prolif = find(g1s>0.2 | g2m>0.2);
    data_ic10 = module_scores(:,i1);
    md = median(data_ic10(ind_prolif));
    data_ic10 = data_ic10 - md;
    data_ic10_global = [data_ic10_global;data_ic10];
    
    plot(data_ic10,g2m,'k.','Color',colorseq{k}); hold on;
    plot([0 0],[-0.2 0.8],'k-');
    plot([-0.6 0.8],[0 0],'k-');
    
    k = k+1;
end

ind_prolif = find(data_cc_global(:,1)>0.2 | data_cc_global(:,2)>0.2);
stp = std(data_ic10_global(ind_prolif));
plot([-stp*2 -stp*2],[-0.2 0.8],'k--');
plot([stp*2 stp*2],[-0.2 0.8],'k--');


xlabel('IC\_EWS','FontSize',20); ylabel('IC\_G2/M','FontSize',20); 
xlim([-0.4 0.3])
ylim([-0.2 0.8])
set(gca,'FontSize',12);
set(gcf,'Color','w');


% ============================================================

figure;
k =1;
tot = 0;
  par1 = 'IC10+';
  par2 = 'PID138045_HIF1alpha_transcription_factor_network';
%  par1 = 'IC10+';
%  par2 = 'HALLMARK_OXIDATIVE_PHOSPHORYLATION';

 data_hypoxia_global = [];
for pref=dsets
    disp(sprintf('%s',char(pref)));
    module_tab = module_tabs(k);
    %module_scores = module_tab.data;
    module_scores = module_scores_all{k};
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    
    npoints = size(module_scores,1);
    data_hypoxia = module_scores(:,i2);
    ic10 = data_ic10_global(tot+1:tot+npoints,1);    
    g1s = data_cc_global(tot+1:tot+npoints,1);
    g2m = data_cc_global(tot+1:tot+npoints,2);
    tot = tot+npoints;
    ind_non_prolif = find(g1s<0.2 & g2m<0.2);
    
    data_hypoxia = data_hypoxia - median(data_hypoxia(ind_non_prolif));
    data_hypoxia_global = [data_hypoxia_global;data_hypoxia];    
    
    plot(ic10,data_hypoxia,'k.','Color',colorseq{k}); hold on;
    plot([0 0],[-0.6 0.8],'k-');
    plot([-0.6 0.8],[0 0],'k-');

    
    
    k = k+1;
end

ind_prolif = find(data_cc_global(:,1)>0.2 | data_cc_global(:,2)>0.2);
stp = std(data_ic10_global(ind_prolif));
plot([-stp*2 -stp*2],[-0.8 0.8],'k--');
plot([stp*2 stp*2],[-0.8 0.8],'k--');
ylim([-0.3 0.6]);

% ind_ic10low = find(data_ic10_global<-stp*2);
% ind_ic10high = find(data_ic10_global>=-stp*2 & data_ic10_global<=stp*2);
% ind_ic10highhigh = find(data_ic10_global>stp*3);
% hypoxia_low = median(data_hypoxia_global(ind_ic10low));
% hypoxia_high = median(data_hypoxia_global(ind_ic10high));
% hypoxia_highhigh = median(data_hypoxia_global(ind_ic10highhigh));
% plot([-0.6 -stp*2],[hypoxia_low hypoxia_low],'k-','LineWidth',5);
% plot([-stp*2 stp*2],[hypoxia_high hypoxia_high],'k-','LineWidth',5);
% plot([stp*2 0.3],[hypoxia_highhigh hypoxia_highhigh],'k-','LineWidth',5);

xv = data_ic10_global;
yv = data_hypoxia_global;
nintervals = 30;
dx = (max(xv)-min(xv))/nintervals;
intervals = min(xv):dx:max(xv);
[~,intervals_ind] = histc(xv,intervals);
yv_smooth = [];
yv_stds = [];
for i=1:nintervals+1
    yv_smooth(i) = median(yv(find(intervals_ind==i)));
    yv_stds(i) = std(yv(find(intervals_ind==i)));
end
plot(intervals,yv_smooth,'g-','LineWidth',4,'Color',[0 0.5 0]);
plot(intervals,yv_smooth+yv_stds,'g--','LineWidth',2,'Color',[0 0.5 0]);
plot(intervals,yv_smooth-yv_stds,'g--','LineWidth',2,'Color',[0 0.5 0]);


xlabel('IC\_EWS','FontSize',20); ylabel('HIF1A Transcriptional network','FontSize',20); 
xlim([-0.4 0.3])
%ylim([-0.2 0.8])
set(gca,'FontSize',12);
set(gcf,'Color','w');


% ============================================================

figure;
k =1;
tot = 0;
  par1 = 'IC10+';
  %par2 = 'GO0022904_respiratory_electron_transport_chain';
 % par2 = 'IC6+';
%  par1 = 'IC10+';
  par2 = 'HIF1A_REGULATED';

 data_hypoxia_global = [];
for pref=dsets
    disp(sprintf('%s',char(pref)));
    module_tab = module_tabs(k);
    %module_scores = module_tab.data;
    module_scores = module_scores_all{k};
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    
    npoints = size(module_scores,1);
    data_hypoxia = module_scores(:,i2);
    ic10 = data_ic10_global(tot+1:tot+npoints,1);    
    g1s = data_cc_global(tot+1:tot+npoints,1);
    g2m = data_cc_global(tot+1:tot+npoints,2);
    tot = tot+npoints;
    ind_non_prolif = find(g1s<0.2 & g2m<0.2);
    
    data_hypoxia = data_hypoxia - median(data_hypoxia(ind_non_prolif));
    data_hypoxia_global = [data_hypoxia_global;data_hypoxia];    
    
    plot(ic10,data_hypoxia,'k.','Color',colorseq{k}); hold on;
    plot([0 0],[-0.4 0.8],'k-');
    plot([-0.6 0.8],[0 0],'k-');

    
    
    k = k+1;
end

ind_prolif = find(data_cc_global(:,1)>0.2 | data_cc_global(:,2)>0.2);
stp = std(data_ic10_global(ind_prolif));
plot([-stp*2 -stp*2],[-0.2 0.8],'k--');
plot([stp*2 stp*2],[-0.2 0.8],'k--');

% ind_ic10low = find(data_ic10_global<-stp*2);
% ind_ic10high = find(data_ic10_global>=-stp*2 & data_ic10_global<=stp*2);
% ind_ic10highhigh = find(data_ic10_global>stp*3);
% hypoxia_low = median(data_hypoxia_global(ind_ic10low));
% hypoxia_high = median(data_hypoxia_global(ind_ic10high));
% hypoxia_highhigh = median(data_hypoxia_global(ind_ic10highhigh));
% plot([-0.6 -stp*2],[hypoxia_low hypoxia_low],'k-','LineWidth',5);
% plot([-stp*2 stp*2],[hypoxia_high hypoxia_high],'k-','LineWidth',5);
% plot([stp*2 0.3],[hypoxia_highhigh hypoxia_highhigh],'k-','LineWidth',5);

xv = data_ic10_global;
yv = data_hypoxia_global;
nintervals = 30;
dx = (max(xv)-min(xv))/nintervals;
intervals = min(xv):dx:max(xv);
[~,intervals_ind] = histc(xv,intervals);
yv_smooth = [];
yv_stds = [];
for i=1:nintervals+1
    yv_smooth(i) = median(yv(find(intervals_ind==i)));
    yv_stds(i) = std(yv(find(intervals_ind==i)));
end
plot(intervals,yv_smooth,'g-','LineWidth',4,'Color',[0 0.5 0]);
plot(intervals,yv_smooth+yv_stds,'g--','LineWidth',2,'Color',[0 0.5 0]);
plot(intervals,yv_smooth-yv_stds,'g--','LineWidth',2,'Color',[0 0.5 0]);


xlabel('IC\_EWS','FontSize',20); ylabel(strrep(par2,'_',' '),'FontSize',20); 
xlim([-0.4 0.3])
%ylim([-0.2 0.8])
set(gca,'FontSize',12);
set(gcf,'Color','w');

