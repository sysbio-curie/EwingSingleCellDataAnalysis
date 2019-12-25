addpath ../data;
addpath C:/Datas/BIODICA_GUI/bin/fastica++/;

%prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861';
%prefix = 'pdx184';
%prefix = 'pdx1058';
prefix = 'pdx856';

compute_regression = 1;
write_residuals = 0;
if compute_regression

module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);


%pos_fields = {'IC1+','IC2+','IC3+','IC4+','IC5+','IC6+','IC7+','IC8+','IC9+','IC10+','IC11+','IC12+','IC13+','IC14+','IC15+','IC16+','IC17+','IC18+','IC19+','IC20+','IC21+','IC22+','IC23+','IC24+','IC25+','IC26+','IC27+','IC28+','IC29+','IC30+'};
pos_fields = {'IC1+','IC2+','IC4+','IC6+','IC9+','IC10+','IC14+'};
%pos_fields = {'IC1+','IC2+','IC10+','IC4+','IC6+','IC14+','IC12+','IC13+','IC25+'};
%pos_fields = module_names(1:55);
inds = find(ismember(module_names,pos_fields));

data_tab = importdata(sprintf('%s_nufp10k.txt',prefix));
data = data_tab.data;
cell_names = data_tab.textdata(1,2:end);
gene_names = data_tab.textdata(2:end,1);

[v,u,s] = pca(data');
[broken_stick_dimension] = brokenstickdim(s);
disp(sprintf('Broken stick dimension = %f',broken_stick_dimension));
corr_dim = 1;
for i=1:length(s)
    if s(1)/s(i)<10
        corr_dim = i;
    else
        break;
    end
end
disp(sprintf('Correlation dime = %f',corr_dim));
pause(2);




% i10p = find(strcmp(module_names,'IC10+'));
% ic10score = module_scores(:,i10p);
% prolif_zone_ind = find((ic10score>thresh_prolif_begin)&(ic10score<thresh_ichighhigh));
% module_scores = module_scores(prolif_zone_ind,:);
% data = data(:,prolif_zone_ind);


data_mean = mean(data,2);
data_centered = bsxfun(@minus, data, data_mean);

% ii = find(mean(module_scores)==0);
% ii = [ii 243];
% inds = setdiff(1:244,ii);
% inds = 1:55;
%inds = [1:27,29:55];

%ms = module_scores(:,inds);
ms = module_scores(:,inds);
msz = zscore(ms);

%for j=1:30

%ms1 = ms(:,[1 2 10 14]);
%ms1 = ms(:,[1:30]);
ms1 = msz;
%ms1 = ms(:,[j]);
%ms1 = rand(size(data,2),1);
X = [ones(size(ms1,1),1) ms1];

fid = fopen([prefix '_regression_coeffs.txt'],'w');

residuals = zeros(size(data));
r2 = zeros(length(gene_names),1);

for i=1:length(gene_names)
%for i=1:100
    Y = data(i,:); Y = Y';
    [b,bint,r,rint,stats] = regress(Y,X);
    %[b] = lasso(X,Y);
    residuals(i,:) = r;
    r2(i) = stats(1);
    %disp(sprintf('%s\t%f\t%f\t%f\t%f',char(gene_names(i)),b(1),b(2),b(3),stats(1)));
    %fprintf(fid,'%s\t%f\t%f\t%f\t%f\n',char(gene_names(i)),b(1),b(2),b(3),stats(1));
    fprintf(fid,'%s\t',char(gene_names(i))); 
        for k=1:length(b) 
               %fprintf(fid,'%f\t',b(k)); 
               nonzero = bint(k,1)>0.05;
               if nonzero
                   fprintf(fid,'1\t'); 
               else
                   fprintf(fid,'0\t'); 
               end
        end; 
        fprintf(fid,'\n');
    disp(sprintf('%i %f\t',i,b));
end

fclose(fid);

if write_residuals
fid = fopen(sprintf('%s_nufp10k_residuals.txt',prefix),'w');
fprintf(fid,'GENE\t'); for i=1:length(cell_names) fprintf(fid,'%s\t',char(cell_names{i})); end; fprintf(fid,'\n');
for i=1:length(gene_names)
    fprintf(fid,gene_names{i}); fprintf(fid,'\t'); fprintf(fid,'%2.2f\t',residuals(i,:)); fprintf(fid,'\n');
end
fclose(fid);
end

explained_variance = 1-sum(var(residuals'))/sum(var(data'));
disp(sprintf('Explained variance=%f',explained_variance));
disp(sprintf('Mean R2 = %f',mean(r2)));
figure;
hist(r2,50); title(sprintf('%s,Exp.var=%f',prefix,explained_variance),'FontSize',20);
set(gcf,'Color','w');

end



%end

figure;

rcoeff_tab = importdata([prefix '_regression_coeffs.txt']);
rcoeff = rcoeff_tab.data;
rcoeff = rcoeff(:,2:end);
rcoeff_bin = zeros(size(rcoeff,1),size(rcoeff,2));
rcoeff_bin(abs(rcoeff)>0.1) = 1;
srcoeff = sum(rcoeff_bin);
plot(srcoeff,'ko-');
hold on;
plot([10 10],[0 max(srcoeff)],'b--');
plot([1 1],[0 max(srcoeff)],'r--');
plot([2 2],[0 max(srcoeff)],'r--');
plot([4 4],[0 max(srcoeff)],'g--');
plot([6 6],[0 max(srcoeff)],'g--');


ylabel('Number of genes','FontSize',20);
title(prefix,'FontSize',20);
%xticklabel(pos_fields);
set(gca,'XTick',1:length(pos_fields));
set(gca,'XTickLabel',pos_fields);
set(gca,'XTickLabelRotation',90);
set(gcf,'Color','w');





