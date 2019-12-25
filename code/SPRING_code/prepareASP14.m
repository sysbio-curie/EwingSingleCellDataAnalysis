ECt = importdata('C:\Datas\MOSAIC\expression\aggregated_data\ewing\ASP14_TS_ul_MAGIC.txt');
EC = ECt.data';
gene_list_EC = ECt.textdata(2:end,1);

[~,is] = sort(gene_list_EC);
EC = EC(:,is);
gene_list_EC = gene_list_EC(is);

gene_list_EC   = struct_field_qualified(gene_list_EC);
cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\aggregated_data\ewing\ASP14_TS_labels.txt');
cell_groups_EC = cell_groups_EC';
cell_groups_EC   = struct_field_qualified(cell_groups_EC);
%[~,gene_filter_EC] = filter_genes(EC,0.1,0.2); 
[~,gene_filter_EC] = filter_genes(EC,0.1,0.01); 
display(sprintf('Number of genes selected = %i',sum(gene_filter_EC))); 

 c = importdata('C:\Datas\MOSAIC\expression\aggregated_data\ewing\ASP14_TS_ul_scores.txt','%c',383);
 clear cs; for i=1:size(c,1) cs(i,:) = strsplit(char(c(i))); end;
 cs = cs(:,1:384);
 for j=1:size(cs,1)
     cd = str2num(char(cs(j,2:end)))';
     cd = cd-min(cd);
     cd = cd*10000;
     display(sprintf('%f %f %f %f %f',cd(1),cd(2),cd(3),cd(4),cd(5)));
     clear cdc; cdc = {char(cs(j,1))}; for i=1:size(EC,1) cdc = [cdc cd(i)]; end;
     custom_colors_EC(j,:)  = cdc(:)';
 end
 custom_colors_EC  = struct_field_qualified(custom_colors_EC);

%   selection = randsample(size(EC,1),300);
%   EC = EC(selection,:);
%   cell_groups_EC = cell_groups_EC(:,[1;selection]);
%   custom_colors_EC_all = custom_colors_EC;
%   custom_colors_EC = custom_colors_EC(:,[1;selection]);

EC = row_normalize(EC);
[coeff,score,latent] = pca(zscore(EC(:,gene_filter_EC)));
Epca = score(:,1:20);
D = pdist2(Epca,Epca);
save_spring_dir(EC,D,10,gene_list_EC,'../datasets/asp14_ts_magic','cell_groupings',cell_groups_EC,'custom_colors',custom_colors_EC);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/asp14_ts','cell_groupings',cell_groups_EC);