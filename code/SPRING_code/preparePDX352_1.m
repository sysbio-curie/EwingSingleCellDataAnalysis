%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_u10kf_MAGIC.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_u13kf.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_u10kf_POOL.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_u10kf.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_u10kf_more3000genes.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_u10kf_midquartgenes.txt');
%ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_u10kf_midquartgenes_COL6A1_COL6A2.txt');
ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx352_nufp10k.txt');

EC = ECt.data';

%EC = table2array(tdp)';

% genes = sum(EC'>0);
% genesf = genes/mean(genes);
% gg = repmat(genesf',1,13000);
% EC = EC./gg;

gene_list_EC = ECt.textdata(2:end,1);

[~,is] = sort(gene_list_EC);
EC = EC(:,is);
gene_list_EC = gene_list_EC(is);

gene_list_EC   = struct_field_qualified(gene_list_EC);
%cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_IC10+clusters_prolif.txt');
%cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_IC10+clusters_prolif.txt');
%cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_IC10+clusters_prolif_midquartgenes.txt');
%cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_IC10+clusters_prolif_more3000genes.txt');
cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx352_nufp_clusters.txt');
cell_groups_EC = cell_groups_EC';
cell_groups_EC   = struct_field_qualified(cell_groups_EC);
%[~,gene_filter_EC] = filter_genes(EC,0.1,0.2); % for raw
%[~,gene_filter_EC] = filter_genes(EC,0.0,0.005); % for MAGIC 
%[~,gene_filter_EC] = filter_genes(EC,0.1,0.05); % for POOL10
%[~,gene_filter_EC] = filter_genes(EC,0,0); % for raw

%[~,gene_filter_EC] = filter_genes(EC,0.02,0.02); % for raw counts
[~,gene_filter_EC] = filter_genes(EC,0.01,0.05); % for pool10 counts
display(sprintf('Number of genes selected = %i',sum(gene_filter_EC))); 



%c = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_scores.txt','%c',3573);
%c = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352TMM_MRscores.txt','%c',3573); 
c = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx352_nufp2k_modulescores.txt','%c',3573); 

clear cs; for i=1:size(c,1) cs(i,:) = strsplit(char(c(i))); end;
clear custom_colors_EC;
for j=1:size(cs,1)
    cd = str2num(char(cs(j,2:end)))';
    cd = cd-min(cd);
    if max(cd)<1000 
        cd = cd*10000;
    end
    display(sprintf('%f %f %f %f %f',cd(1),cd(2),cd(3),cd(4),cd(5)));
    clear cdc; cdc = {char(cs(j,1))}; for i=1:size(EC,1) cdc = [cdc cd(i)]; end;
    custom_colors_EC(j,:)  = cdc(:)';
end
custom_colors_EC  = struct_field_qualified(custom_colors_EC);

%     selection = randsample(size(EC,1),500);
%     EC = EC(selection,:);
%     cell_groups_EC = cell_groups_EC(:,[1;selection]);
%     custom_colors_EC_all = custom_colors_EC;
%     custom_colors_EC = custom_colors_EC(:,[1;selection]);

EC = row_normalize(EC);
EC = EC*100;
[coeff,score,latent] = pca(zscore(EC(:,gene_filter_EC)));
Epca = score(:,1:min(20,sum(gene_filter_EC)));
D = pdist2(Epca,Epca);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352_pool5','cell_groupings',cell_groups_EC,'custom_colors',custom_colors_EC);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352','cell_groupings',cell_groups_EC);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352TMM_more3000genes','cell_groupings',cell_groups_EC);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352TMM_midquartergenes','cell_groupings',cell_groups_EC);
%save_spring_dir(EC,D,10,gene_list_EC,'../datasets/PDX352','cell_groupings',cell_groups_EC);
save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352_pool','cell_groupings',cell_groups_EC,'custom_colors',custom_colors_EC);