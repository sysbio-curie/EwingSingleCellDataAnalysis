addpath ../data;
addpath ../data/cellcycle_pseudotime;

xaxis = 'IC-EwS';
%xaxis = 'CCPtime';
%gene = 'NRN1';
moreopt_threshold = 0.1;
nbins = 100;

dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
colorseq = {'r','g','b','m','k'};

pstime_tab = importdata('cell_cycle_pseudotime.txt');
pstime_dat = pstime_tab.data;
pstime_samples = pstime_tab.textdata(2:end,1);
pstime_pdxs = pstime_tab.textdata(2:end,2);
if strcmp(xaxis,'IC-EwS')
    pstime_xvalues = pstime_dat(:,2);
end
if strcmp(xaxis,'CCPtime')
    pstime_xvalues = pstime_dat(:,1);
end

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

factors = zeros(length(dsets),1);
for i=1:length(dsets)
    snames_pdx = snames{i};
    gnames_pdx = gnames{i};
    dat_pdx = dat{i};
    k = find(strcmp(gnames_pdx,gene));
    if length(k)==0
        profiles{i} = [];
        xvalues{i} = [];
        factors(i) = 1;
    else
        prof = dat_pdx(k,:);
        good_samples_ind = find(strcmp(pstime_pdxs,dsets(i)));
        good_samples = pstime_samples(good_samples_ind);
        inds = find(ismember(snames_pdx,good_samples));
        profiles{i} = prof(inds);
        %    temp = pstime_dat(:,2)
        %    profiles{i} = temp(inds)';
        xvalues{i} = pstime_xvalues(good_samples_ind);
        factors(i) = mean(profiles{i});
    end
end

glob_mean = mean(factors);
factors = factors-glob_mean;

%for i=1:length(dsets)
glob_x = [];
glob_y = [];
xvalues
for i=1:5
    y = profiles{i}-factors(i);
    %y = profiles{i};
    plot(xvalues{i},y,'k.','Color',colorseq{i}); hold on;
    glob_x = [glob_x;xvalues{i}];
    glob_y = [glob_y y];
end
glob_y = glob_y';

[x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth1(glob_x,glob_y,nbins);
%f = fit(glob_x,glob_y,'lowess')
%f = smooth(glob_x,glob_y,0.1,'rloess');
plot(x_smoothed,y_smoothed,'ko','LineWidth',2); ylim([min(glob_y) max(glob_y)]);
%plot(x_smoothed,y_smoothed+y_stdvs,'k--','LineWidth',2); 
%plot(x_smoothed,y_smoothed-y_stdvs,'k--','LineWidth',2); 
xlim([-0.45 0.25]);
if strcmp(xaxis,'CCPtime')
    xlim([min(glob_x) max(glob_x)]);
    ylim([min(glob_y(find(glob_x>0.1))) max(glob_y(find(glob_x>0.1)))]);
else
    plot([-moreopt_threshold -moreopt_threshold],[0 max(glob_y)],'k--');
    plot([moreopt_threshold moreopt_threshold],[0 max(glob_y)],'k--');
end
%[~,ind] = sort(glob_x);
%plot(glob_x,f(ind),'k-','LineWidth',4); ylim([0 max(glob_y)]);

set(gcf,'Color','w');
set(gca,'FontSize',14);
xlabel(xaxis,'FontSize',20);
ylabel(gene,'FontSize',20);


figure;

glob_y1 = y_smoothed';
glob_x1 = x_smoothed;

inds_opt = find((glob_x1>-moreopt_threshold)&(glob_x1<moreopt_threshold));
inds_moreopt = find(glob_x1>moreopt_threshold);
inds_lessopt = find(glob_x1<-moreopt_threshold);

% glob_y1 = glob_y;
% glob_x1 = glob_x;

length(inds_moreopt)
length(find(glob_x>moreopt_threshold))
vals = [glob_y1(inds_lessopt)' glob_y1(inds_opt)' glob_y1(inds_moreopt)'];
 g = [1*ones(1,length(inds_lessopt)) ...
      2*ones(1,length(inds_opt)) ... 
      3*ones(1,length(inds_moreopt))];
 h = boxplot(vals,g,'Whisker',1,'Widths',0.8,'Outliers',1); 
 set(h,'LineWidth',2);
 %set(h(7,:),'Visible','off')
 set(gca,'XTick',1:3);
 set(gca,'XTickLabel',{'IC-EWS<OPT','IC-EWS_OPT','IC-EWS>OPT'},'FontSize',14);
 ylabel('Relative gene expression','FontSize',14); set(gca,'XTickLabelRotation',90);
 hold on; plot([0 8],[0 0],'k--'); set(gcf,'Color',[1 1 1]);
 
 figure; 

glob_y1 = y_smoothed;
glob_x1 = x_smoothed;
 
x = glob_x1(inds_opt);
y = glob_y1(inds_opt);
x = x(find(~isnan(y)));
y = y(find(~isnan(y)));
p = polyfit(x,y,1);
[x_sorted,x_p] = sort(glob_x1);
yps = polyval(p,x_sorted);

%plot(glob_x,glob_y,'k.'); hold on;

for i=1:5
    y = profiles{i}-factors(i);
    %y = profiles{i};
    plot(xvalues{i},y,'k.','Color',colorseq{i}); hold on;
end
plot([-moreopt_threshold -moreopt_threshold],[min(glob_y) max(glob_y)],'k--');
plot([moreopt_threshold moreopt_threshold],[min(glob_y) max(glob_y)],'k--');


plot(x_sorted,glob_y1(x_p),'b-','LineWidth',3); hold on;
plot(x_sorted,yps,'r--','LineWidth',5);
xlim([min(x_sorted) max(x_sorted)]);

set(gcf,'Color','w');
set(gca,'FontSize',14);
xlabel(xaxis,'FontSize',20);
ylabel(gene,'FontSize',20);

figure;

%glob_y1 = y_smoothed;
%glob_x1 = x_smoothed;
glob_y1 = glob_y;
glob_x1 = glob_x;
glob_y1 = glob_y1 - polyval(p,glob_x1);

inds_opt = find((glob_x1>-moreopt_threshold)&(glob_x1<moreopt_threshold));
inds_moreopt = find(glob_x1>moreopt_threshold);
inds_lessopt = find(glob_x1<-moreopt_threshold);

  cr = corr(glob_x(inds_opt),glob_y(inds_opt));
  disp(sprintf('==========================='));
  disp(sprintf('CORRELATION WITH IC-EWS = %f',cr));
  disp(sprintf('==========================='));


plot(glob_x1,glob_y1,'k.'); hold on;
plot([-moreopt_threshold -moreopt_threshold],[min(glob_y1) max(glob_y1)],'k--');
plot([moreopt_threshold moreopt_threshold],[min(glob_y1) max(glob_y1)],'k--');

[x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth1(glob_x1,glob_y1,nbins);
plot(x_smoothed,y_smoothed,'b-','LineWidth',4); %ylim([min(glob_y1) max(glob_y1)]);
xlim([-0.45 0.25]);

figure;

 %glob_x1 = x_smoothed;
 %glob_y1 = y_smoothed;
 glob_x1 = glob_x;
 glob_y1 = glob_y;
 glob_y1 = (glob_y1 - polyval(p,glob_x1))';
 
inds_opt = find((glob_x1>-moreopt_threshold)&(glob_x1<moreopt_threshold));
inds_moreopt = find(glob_x1>moreopt_threshold);
inds_lessopt = find(glob_x1<-moreopt_threshold);
 
 vals = [glob_y1(inds_lessopt) glob_y1(inds_opt) glob_y1(inds_moreopt)];
  g = [1*ones(1,length(inds_lessopt)) ...
       2*ones(1,length(inds_opt)) ... 
       3*ones(1,length(inds_moreopt))];
  h = boxplot(vals,g,'Whisker',1,'Widths',0.8,'Outliers',1); 
  set(h,'LineWidth',2);
  %set(h(7,:),'Visible','off')
  
  
  [~,P12,~,~] = ttest2(glob_y1(inds_lessopt),glob_y1(inds_opt));
  [~,P23,~,~] = ttest2(glob_y1(inds_moreopt),glob_y1(inds_opt));
  [~,P13,~,~] = ttest2(glob_y1(inds_lessopt),glob_y1(inds_moreopt));
  
  
  title(sprintf('%s (p_<=%1.0e, p_>=%1.0e, p_{<>}=%1.0e)',gene,P12,P23,P13),'FontSize',20);
  %ylabel(sprintf('Relative expression\n corrected for IC-EwS trend'),'FontSize',14); 
  set(gca,'XTickLabelRotation',90);
  hold on; plot([0 8],[0 0],'k--'); set(gcf,'Color',[1 1 1]);
  set(gca,'XTick',1:3);
  set(gca,'XTickLabel',{'IC-EWS<OPT','IC-EWS_OPT','IC-EWS>OPT'},'FontSize',14);
  set(gca,'XTickLabel',{'','',''},'FontSize',14);

  %============== 