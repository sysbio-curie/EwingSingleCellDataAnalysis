coords = importdata('../data/cellcycle_pseudotime/cell_cycle_pseudotime.txt');

coordinates = coords.data(:,1);
ic10 = coords.data(:,2);
g1s = coords.data(:,3);
g2m = coords.data(:,4);

ic10_genes = importdata('IC10+_genes.txt');

good_cell_names = coords.textdata(2:end,1);
pdxs = coords.textdata(2:end,2);

%plot(coordinates,[ic10 g1s g2m],'o');

dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};

if(0)
for i = 1:length(dsets)
    disp(sprintf('%s',char(dsets(i))));
    module_tabs(i) = importdata(sprintf('%s_nufp_IC10+.txt',char(dsets(i))));
    cell_names = module_tabs(i).textdata(1,2:end);
    ind_cells = find(strcmp(pdxs,dsets(i)));
    good_cells = good_cell_names(ind_cells);
    ind_cells = find(ismember(cell_names,good_cells));
    data = module_tabs(i).data;
    data = data(:,ind_cells);
    ind_cells_all{i} = ind_cells;
    datasets{i} = data;
end
end

gn = 'CD99';

for k=1:length(dsets)
pdx = dsets(k);
cell_names = cell_names_all{k};
dataset = datasets{k};
tab = module_tabs(k);
ind_cells = ind_cells_all{k};

cell_names = tab.textdata(1,2:end);
cell_names = cell_names(ind_cells);
gene_names = tab.textdata(2:end,1);
ik = find(strcmp(gene_names,gn));
ind_good_cell_names = find(strcmp(pdxs,pdx));
pt = coordinates(ind_good_cell_names);
gcn = good_cell_names(ind_good_cell_names);
inds = find(ismember(cell_names,gcn));
profile = dataset(ik,inds);



subplot(2,3,k);
plot(pt,profile,'ko'); title(pdx,'FontSize',14);

end;
