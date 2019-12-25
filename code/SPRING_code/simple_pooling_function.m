function [] = simple_pooling_function(input_file,output_file,k,var_threshold)


lib_size_norm = true;
dim = 20;
normalize_pooled = true;



tab = importdata(input_file); 


gene_names = tab.textdata(2:end,1);
sample_names = tab.textdata(1,2:end);

%sample_names = tab.textdata(1,:);
%s1 = strsplit(char(sample_names),'\t');
%sample_names = s1(2:end);


data = tab.data;
data = data';

if lib_size_norm
    disp 'Library size normalization'
    libsize  = sum(data,2);
    display(sprintf('Median library size=%f',median(libsize)));
    data = bsxfun(@rdivide, data, libsize) * median(libsize);
end

data_log = log10(data+1);

st = var(data_log);
inds = find(st>var_threshold);

display(sprintf('Number of selected genes: %i/%i',length(inds),size(data_log,2)));

disp 'Computing PCA'
tic
[v,u,s] = pca(data_log(:,inds));
toc;

display(sprintf('First %i dimensions explain %f percents of variance',dim,sum(s(1:dim))/sum(s)*100));

disp 'Computing knn graph'
tic
[idx,dist] = knnsearch(u(:,1:dim),u(:,1:dim),'k',k);
toc

data_pooled = zeros(size(data,1),size(data,2));

for i=1:size(idx,1)
	%display(sprintf('%i',i));
	tmp = data(idx(i,:),:);
	%tmp_pow = power(2,tmp);
	tmp_pld = sum(tmp);
	data_pooled(i,:) = tmp_pld(:);
end

if normalize_pooled
    libsize_pooled  = sum(data_pooled,2);
    display(sprintf('Median library size after pooling=%f',median(libsize_pooled)));
    data_pooled = bsxfun(@rdivide, data_pooled, libsize_pooled) * median(libsize_pooled);
    data_pooled = log10(data_pooled+1);
end

    
display('Replacing tires');
for i=1:length(sample_names)
    sample_names(i) = strrep(sample_names(i),'-','_');
end

display('Saving');
tdp = array2table(data_pooled','RowNames',gene_names,'VariableNames',sample_names);
writetable(tdp,output_file,'Delimiter','\t','WriteRowNames',true);

done = 1;
doneF = sprintf('%s_done',output_file);
save(doneF,'done','-ascii');
