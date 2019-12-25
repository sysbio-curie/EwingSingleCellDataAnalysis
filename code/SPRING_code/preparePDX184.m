ECt = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx184f_nufp10k.txt');

EC = ECt.data';

gene_list_EC = ECt.textdata(2:end,1);

[~,is] = sort(gene_list_EC);
EC = EC(:,is);
gene_list_EC = gene_list_EC(is);

 gene_list_EC   = struct_field_qualified(gene_list_EC);
 cell_groups_EC = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx184f_nufp_clusters3.txt');
 cell_groups_EC = cell_groups_EC';
 cell_groups_EC   = struct_field_qualified(cell_groups_EC);

%[~,gene_filter_EC] = filter_genes(EC,0.02,0.02); % for raw counts
[~,gene_filter_EC] = filter_genes(EC,0.01,0.05); % for pool10 counts
display(sprintf('Number of genes selected = %i',sum(gene_filter_EC))); 


 c = importdata('C:\Datas\MOSAIC\expression\chromium_data\clean_count_data\pdx184f_nufp2k_modulescores.txt','%c',1294); 
 
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
 

EC = row_normalize(EC);
EC = EC*100;
[coeff,score,latent] = pca(zscore(EC(:,gene_filter_EC)));
Epca = score(:,1:min(20,sum(gene_filter_EC)));
D = pdist2(Epca,Epca);
save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx184f_pool','cell_groupings',cell_groups_EC,'custom_colors',custom_colors_EC);
%save_spring_dir(EC,D,5,gene_list_EC,'../datasets/pdx184q_pool');