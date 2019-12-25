function [] = cluster_in_IC10plus_space(prefix,input_file,score_file)

tab = importdata(input_file);
data = tab.data;

tabmodules = importdata(score_file);
module_scores = tabmodules.data;

nReps = 10;

gene_names = tab.textdata(1,2:end);
module_names = tabmodules.textdata(1,2:end);

ind_CCK = find(strcmp(gene_names,'CCK'));

ind_prolif = find((module_scores(:,1)>0.6)|(module_scores(:,2)>0.6));
display(sprintf('Number of proliferating cells = %i',length(ind_prolif)));
%ind_prolif = [];

ind_noprolif = setdiff(1:size(data,1),ind_prolif);

[v,u,s] = pca(data(ind_noprolif,:),'NumComponents',3);

%u=-u;

display(sprintf('Explained variances %f,%f',s(1)/sum(s),s(2)/sum(s)));

[idx, cent] = kmeans(data(ind_noprolif,:), 2, 'Replicates', nReps);

names(1,:) = {'ic10plusmedium'};
names(2,:) = {'ic10plusmedium'};
names(3) = {'proliferating'};

data1 = data(ind_noprolif,:);

meanexp(1) = mean(mean(data1(idx==1,:)));
meanexp(2) = mean(mean(data1(idx==2,:)));

[~,indic10low] = min(meanexp);
names(indic10low) = {'ic10plus_low'};

[~,indic10high] = max(meanexp);
names(indic10high) = {'ic10plushigh'};

idx_full = zeros(size(data,1),1);
idx_full(ind_prolif) = 3;

idx_full(ind_noprolif(idx==1)) = 1;
idx_full(ind_noprolif(idx==2)) = 2;

 cluster_names(1) = {'CLUSTER_IC10plus'};
 
 for i=1:size(data,1)
     cluster_names(i+1) = names(idx_full(i));
 end
 
 
 fid = fopen(sprintf('%s_clusters2',input_file), 'w');
 for i=1:length(cluster_names)
     fprintf(fid, '%s\n', char(cluster_names(i)));
 end
 fclose(fid) ;

plot(u(idx==find(strcmp(names,'ic10plus_low')),1),u(idx==find(strcmp(names,'ic10plus_low')),2),'bo','MarkerFaceColor','b','MarkerSize',4); hold on;
plot(u(idx==find(strcmp(names,'ic10plushigh')),1),u(idx==find(strcmp(names,'ic10plushigh')),2),'go','MarkerFaceColor','g','MarkerSize',4); hold on;
%plot(u(idx==find(strcmp(names,'proliferating')),1),u(idx==find(strcmp(names,'proliferating')),2),'ro','MarkerFaceColor','r','MarkerSize',4); hold on;

prlf = data(ind_prolif,:);

uprolif = (prlf*v);
plot(uprolif(:,1),uprolif(:,2),'ro','MarkerFaceColor','r','MarkerSize',4); hold on;

colormap = hot;
 
ind_IC10plus = find(strcmp(module_names,'IC10+'));

%  sc = winsor(module_scores(:,ind_IC10plus),[5 95]);
%  sc = (sc-min(sc))/(max(sc)-min(sc));
%  scatter(u(:,1),u(:,2),20,colormap(round(sc(ind_noprolif)*50)+1,:));

done = 1;
doneF = sprintf('%s_done',input_file);
save(doneF,'done','-ascii');
