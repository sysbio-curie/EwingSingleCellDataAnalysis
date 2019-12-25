prefixes = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
%prefixes = {'pdx856'};

for i=1:length(prefixes)

disp(sprintf('%s',char(prefixes(i))));
    
fn = [char(prefixes(i)) '_lasso_selection.txt']
tab = importdata(fn);
data_an = tab.data;
col_names = tab.textdata(1,2:end);
gene_names = tab.textdata(2:end,1);

data_tab = importdata(sprintf('%s_nufp10k.txt',char(prefixes(i))));
data = data_tab.data;

mn = mean(data,2);

fid = fopen([char(prefixes(i)) '_lasso_selection_mean.txt'],'w');

fprintf(fid,'GENE\tMEAN\tVARIANCE\tEXPLAINED_VAR\t');
for j=3:length(col_names)-1
    fprintf(fid,'%s\t',char(col_names(j)));
end
    fprintf(fid,'%s\n',char(col_names(length(col_names))));

for k=1:length(gene_names)
        fprintf(fid,'%s\t%4.4f\t%4.4f\t%4.4f\t',char(gene_names(k)),mn(k),data_an(k,1),data_an(k,2));
        for j=3:length(col_names)-1
            fprintf(fid,'%i\t',data_an(k,j));
        end
        fprintf(fid,'%i\n',data_an(k,length(col_names)));
end

fclose(fid);

end
