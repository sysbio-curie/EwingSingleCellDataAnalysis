addpath ../data;
prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0*0.81+0.8775; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861'; nbins = 20; thresh_prolif_begin = 0.65; thresh_ichighhigh = 0.78*0+0.8; thresh_ichigh3 = 0.8517;
%prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434*0+0.47; thresh_ichighhigh = 0.61; thresh_ichigh3 = 0.6453; 
%prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546*0+0.62; thresh_ichighhigh = 0.8237*0+0.94; thresh_ichigh3 = 0.945;
%prefix = 'pdx1058'; nbins = 40; thresh_prolif_begin = 0.55; thresh_ichighhigh = 0.78; thresh_ichigh3 = 0.945;
labels = importdata(sprintf('%s_classes_text.txt',prefix));


showborders = 1;

modulex = 'IC10+';
%moduley = 'PID138045_HIF1alpha_transcription_factor_network';
moduley = 'PID138045_HIF1alpha_transcription_factor_network_filtered';
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

ix = find(strcmp(module_names,modulex));
iy = find(strcmp(module_names,moduley));
ic10 = find(strcmp(module_names,'IC10+'));

xv = module_scores(:,ix);
yv = module_scores(:,iy);
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