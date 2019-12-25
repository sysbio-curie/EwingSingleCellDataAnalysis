ECt = importdata('C:\Datas\CAFibroblasts\expression\calys\10x\calys10x.txt');
EC = ECt.data';
gene_list_EC = ECt.textdata(2:end,1);
gene_list_EC   = struct_field_qualified(gene_list_EC);

%cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\PDX352_IC10+_clusters.txt');
%cell_groups_EC = cell_groups_EC';
%cell_groups_EC   = struct_field_qualified(cell_groups_EC);

%[~,gene_filter_EC] = filter_genes(EC,0.05,0.1); 
[~,gene_filter_EC] = filter_genes(EC,0.05,0.1); 
display(sprintf('Number of genes selected = %i',sum(gene_filter_EC))); 

c = importdata('C:\Datas\CAFibroblasts\expression\calys\10x\calys10x_scores.txt','%c',2373);
clear cs; for i=1:size(c,1) cs(i,:) = strsplit(char(c(i))); end;
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

%   selection = randsample(size(EC,1),300);
%   EC = EC(selection,:);
%   cell_groups_EC = cell_groups_EC(:,[1;selection]);
%   custom_colors_EC_all = custom_colors_EC;
%   custom_colors_EC = custom_colors_EC(:,[1;selection]);

EC = row_normalize(EC);

%usual distance matrix
%[coeff,score,latent] = pca(zscore(EC(:,gene_filter_EC)));
%Epca = score(:,1:20);
%D = pdist2(Epca,Epca);

%diffusion maps-based distance
dfmp = importdata('C:\Datas\CAFibroblasts\analysis\calys10x_diffusion_maps\dm50.txt');
df = dfmp.data;
dims = [1:5];
D = pdist2(df(:,dims),df(:,dims));


%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx352','cell_groupings',cell_groups_EC,'custom_colors',custom_colors_EC);
%save_spring_dir(EC,D,3,gene_list_EC,'../datasets/calys10x_k3');
%save_spring_dir(EC,D,10,gene_list_EC,'../datasets/calys10x_diffmapk10');
save_spring_dir(EC,D,10,gene_list_EC,'../datasets/calys10x_diffmap50_5_k10','custom_colors',custom_colors_EC);