addpath C:\Datas\MOSAIC\expression\chromium_data\final\data\; 
addpath C:/Datas/BIODICA_GUI/bin/fastica++/;

prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861';
%prefix = 'pdx184';
%prefix = 'pdx1058';
%prefix = 'pdx856';

load_data = 1

if load_data

module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);


pos_fields = {'IC1+','IC2+','IC3+','IC4+','IC5+','IC6+','IC7+','IC8+','IC9+','IC10+','IC11+','IC12+','IC13+','IC14+','IC15+','IC16+','IC17+','IC18+','IC19+','IC20+','IC21+','IC22+','IC23+','IC24+','IC25+','IC26+','IC27+','IC28+','IC29+','IC30+','TOTAL_COUNTS'};
%pos_fields = {'IC1+','IC2+','IC10+','IC4+','IC6+','IC14+','IC12+','IC13+','IC25+'};
%pos_fields = module_names(1:55);
inds = find(ismember(module_names,pos_fields));
indic10 = find(strcmp(module_names,'IC10+'));

data_tab = importdata(sprintf('%s_nufp10k.txt',prefix));
data = data_tab.data;
cell_names = data_tab.textdata(1,2:end);
gene_names = data_tab.textdata(2:end,1);


ms = module_scores(:,inds);
msz = zscore(ms);
msic10 = module_scores(:,indic10);

end

onegene_test = 0;

if onegene_test

%i_point = 5941;
point_name = 'ICMT';
i_point = find(strcmp(gene_names,point_name))
%i_point = 15;
disp(gene_names(i_point))

X = msz;
y = data(i_point,:);
y = y';

[B,FitInfo] = lasso(X,y,'CV',10,'Alpha',0.9,'PredictorNames',pos_fields,'DFmax',5);

idxLambdaMinMSE = FitInfo.IndexMinMSE;
minMSEModelPredictors = FitInfo.PredictorNames(B(:,idxLambdaMinMSE)~=0)

idxLambda1SE = FitInfo.Index1SE;
nonzero_inds = B(:,idxLambda1SE)~=0;
sparseModelPredictors = FitInfo.PredictorNames(nonzero_inds)
coef = B(nonzero_inds,idxLambda1SE);

% Now, we will select only dominant coeffs
abs_coef = abs(coef);
max_coef = max(abs_coef);
dominant_inds = find(abs_coef>max_coef/5);
sparseModelPredictors(dominant_inds)
dominant_coef = coef(dominant_inds)

coef = B(:,idxLambdaMinMSE);
coef0 = FitInfo.Intercept(idxLambdaMinMSE);

yhat = X*coef + coef0;
yic10 = X(:,10)*coef(10)+coef0;
hold on
scatter(y,yhat)
plot(y,y)
xlabel('Actual')
ylabel('Predicted')
scatter(y,yic10,'rx')

explained_var = 1-var(y-yhat)/var(y);
disp(sprintf('Explained var = %f',explained_var));

end

% Now, real computation

%for i=1:length(gene_names)

frequency_selected = zeros(length(inds),1);

fid = fopen([prefix '_lasso_selection_mean.txt'],'w');
fprintf(fid,'GENE\tMEAN\tVARIANCE\tEXPLAINED_VAR\t');
for j=1:length(pos_fields)-1
    fprintf(fid,'%s\t',char(pos_fields(j)));
end
    fprintf(fid,'%s\n',char(pos_fields(length(pos_fields))));

%for i=1:100
for i=1:length(gene_names)
    
    i_point = i;
    gn = gene_names(i_point);
    
    X = msz;
    y = data(i_point,:);
    y = y';

    [B,FitInfo] = lasso(X,y,'CV',10,'Alpha',0.9,'PredictorNames',pos_fields,'DFmax',5);
    
    idxLambda1SE = FitInfo.Index1SE;
    nonzero_inds = B(:,idxLambda1SE)~=0;
    sparseModelPredictors = FitInfo.PredictorNames(nonzero_inds);
    coef = B(:,idxLambda1SE);

    abs_coef = abs(coef);
    max_coef = max(abs_coef);
    dominant_inds = find(abs_coef>max_coef/5);
    dominant_coef = coef(dominant_inds);
    
    coef0 = FitInfo.Intercept(idxLambda1SE);
    yhat = X*coef + coef0;
    
    explained_var = 1-var(y-yhat)/var(y);
    %disp(sprintf('Explained var = %f',explained_var));
    if(explained_var>=0.1)
        %sparseModelPredictors
        selected_predictors = '';
        for j=1:length(dominant_inds)
            selected_predictors = [selected_predictors ',' char(FitInfo.PredictorNames(dominant_inds(j)))];
        end
        disp(sprintf('%i:%s\tExp_var=%3.3f -> %s',i,char(gn),explained_var,selected_predictors));
        frequency_selected(dominant_inds) = frequency_selected(dominant_inds)+1;
        
plot(frequency_selected,'ko-');
hold on;
plot([10 10],[0 max(frequency_selected)],'b--');
plot([1 1],[0 max(frequency_selected)],'r--');
plot([2 2],[0 max(frequency_selected)],'r--');
plot([4 4],[0 max(frequency_selected)],'g--');
plot([6 6],[0 max(frequency_selected)],'g--');
plot([14 14],[0 max(frequency_selected)],'m--');


ylabel('Number of genes','FontSize',20);
title(prefix,'FontSize',20);
%xticklabel(pos_fields);
set(gca,'XTick',1:length(pos_fields));
set(gca,'XTickLabel',pos_fields);
set(gca,'XTickLabelRotation',90);
set(gcf,'Color','w');
hold off;
drawnow;        
        
    else
        disp(sprintf('%i:%s\tExp_var=%3.3f -> BAD regression: no predictors selected',i,char(gn),explained_var)); 
    end
    
    fprintf(fid,'%s\t%4.4f\t%4.4f\t%4.4f\t',char(gn),mean(y),var(y),explained_var);
    for j=1:length(pos_fields)-1
        fprintf(fid,'%i\t',length(find(dominant_inds==j)));
    end
    fprintf(fid,'%i\n',length(find(dominant_inds==length(pos_fields))));
    
end

fclose(fid);


