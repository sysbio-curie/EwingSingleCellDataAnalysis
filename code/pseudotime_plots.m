dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
colorseq = {'r','g','b','m','k'};

coords = importdata('../data/cellcycle_pseudotime/cell_cycle_pseudotime.txt');
pdxs = coords.textdata(2:end,2);

nbins = 30;

for num = 1:length(dsets)

%inds = 1:size(coords.data,1);
inds = find(strcmp(pdxs,dsets(num)));

cc_pseudotime = coords.data(inds,1);
ic10 = coords.data(inds,2);
g1s = coords.data(inds,3);
g2m = coords.data(inds,4);
pdxs1 = pdxs(inds);

ind_prolif = find(g1s>0.1 | g2m>0.1);

for i=1:length(dsets)
    ind = find(strcmp(pdxs1,dsets(i)));
    plot(ic10(ind),g2m(ind),'k.','Color',colorseq{i}); hold on;
end


[x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth(ic10,g2m,nbins);
%plot(x_smoothed,y_smoothed,'k-','LineWidth',3);
bin_tot = [];
bin_prolif = [];
prolif_perc = [];
for i=1:max(intervals_ind)
    bin_tot(i) = sum(intervals_ind==i);
    bin_prolif(i) = length(intersect(find(intervals_ind==i),ind_prolif));
    prolif_perc(i) = bin_prolif(i)/(bin_tot(i));
    if(bin_tot(i)<5) prolif_perc(i)=0; end;
end
%plot(x_smoothed,prolif_perc,'k-','LineWidth',3,'Color',colorseq{num});

end