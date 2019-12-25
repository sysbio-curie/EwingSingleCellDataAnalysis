addpath ../data;

addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\core_algorithm\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\utils\
addpath c:\MyPrograms\_github\Elastic-principal-graphs\ElPiGraph\visualization\

asp14_signature_analysis;
close all;

%% Computing pseudotime
%TrimmingRadius = 15;
NumDimensions = 30;
%  [NodePositions, Edges] = computeElasticPrincipalCurve(datan',40,'ReduceDimension',NumDimensions,'TrimmingRadius',TrimmingRadius,'Mu',8);
%  [coordinate,traversal,X_projected] = PrincipalCurvePseudoTime(datan',NodePositions,Edges,1);
 
 ds = datan';
 [v,u,s] = pca(datan','NumComponents',NumDimensions);
 %node_init = ds([40,245,376],:);
 %edge_init = [1 2; 2 3];
 %[NodePositions2, Edges2] = computeElasticPrincipalCircle(ds,30,'ReduceDimension',...
 %    NumDimensions,'TrimmingRadius',TrimmingRadius/2,'Mu',1,'InitGraph',...
 %    struct('InitNodes',node_init,'InitEdges',edge_init));
 [NodePositions_circle, Edges_circle] = computeElasticPrincipalCircle(ds,30,'ReduceDimension',NumDimensions,'Mu',0.5);
 
 %%
 edges2cut = [2 17;4 29];
 new_edges = [];
 k = 1;
 for i=1:size(Edges_circle,1)
     e = Edges_circle(i,:);
     incl = 1;
     if((e(1)==edges2cut(1,1))&(e(2)==edges2cut(1,2)))
         incl = 0;
     end
     if((e(1)==edges2cut(2,1))&(e(2)==edges2cut(2,2)))
         incl = 0;
     end
     if incl
         new_edges(k,:) = e;
         k = k+1;
     end
 end
 
 [~,traversal1,~] = PrincipalCurvePseudoTime(datan',NodePositions_circle,new_edges,4);
 [~,traversal2,~] = PrincipalCurvePseudoTime(datan',NodePositions_circle,new_edges,29);
nodes1 = NodePositions_circle(traversal1,:);
nodes2 = NodePositions_circle(traversal2,:);
edges1 = [1:length(traversal1)-1;2:length(traversal1)]';
edges2 = [1:length(traversal2)-1;2:length(traversal2)]';
 [pseudotime1,traversal1,X_projected1] = PrincipalCurvePseudoTime(datan',nodes1,edges1,1);
 [pseudotime2,traversal2,X_projected2] = PrincipalCurvePseudoTime(datan',nodes2,edges2,1);

    dt = u(:,1:NumDimensions);
    SquaredX = sum(dt.^2, 2);
    MaxBlockSize = 10000;
    nd = [nodes1;nodes2];
    
    means = mean(datan');
    ndc = bsxfun(@minus, nd, means);
    ndcp = ndc*v(:,1:NumDimensions);
    
    [partition] = PartitionData(dt, ndcp, MaxBlockSize,SquaredX);
    irx_pseudotime1 = find(partition<=size(nodes1,1));
    irx_pseudotime2 = find(partition>size(nodes1,1));
 
%% Determine points not participated in the definition of the pseudotime
% means = mean(datan');
% dt = bsxfun(@minus, datan', means);
% Nodes = bsxfun(@minus, NodePositions, means);
% xNodes = Nodes*v(:,1);
% yNodes = Nodes*v(:,2);
% 
%     dt = u(:,1:NumDimensions);
%     SquaredX = sum(dt.^2, 2);
%     MaxBlockSize = 10000;
%     [partition] = PartitionData(dt, [xNodes yNodes], MaxBlockSize,...
%         SquaredX, TrimmingRadius);
%     inds = find(partition > 0);


%% pseudotime vs gene
figure;
gene = 'CDC20';
prof = datan(find(strcmp(gene_names,gene)),:);
for i=1:length(irx_pseudotime1)
    plot(pseudotime1(irx_pseudotime1(i)),prof(irx_pseudotime1(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime1(i)}); hold on;
    if length(find(ismember(ind_prolif,irx_pseudotime1(i))))>0
        plot(pseudotime1(irx_pseudotime1(i)),prof(irx_pseudotime1(i)),'ro','MarkerSize',10);
    end   
end
[ints,prof_smoothed] = smooth1(pseudotime1(irx_pseudotime1),prof(irx_pseudotime1),10);
plot(ints,prof_smoothed,'g-','LineWidth',4);

for i=1:length(irx_pseudotime2)
    plot(pseudotime2(irx_pseudotime2(i)),prof(irx_pseudotime2(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime2(i)}); hold on;
    if length(find(ismember(ind_prolif,irx_pseudotime2(i))))>0
        plot(pseudotime2(irx_pseudotime2(i)),prof(irx_pseudotime2(i)),'ro','MarkerSize',10);
    end   
end
[ints,prof_smoothed] = smooth1(pseudotime2(irx_pseudotime2),prof(irx_pseudotime2),10);
plot(ints,prof_smoothed,'r-','LineWidth',4);


xlabel('Pseudotime','FontSize',20); ylabel(gene,'FontSize',20);
set(gcf,'Color','w');




%% score vs pseudotime
figure;
% score = IC10_score;
% score_name = 'IC-EwS';
% score = IC1_score;
% score_name = 'IC-G2/M';
score_name = 'IC30+';
score = scoreMap(score_name);
score_name = strrep(score_name,'_',' ');

for i=1:length(irx_pseudotime1)
    plot(pseudotime1(irx_pseudotime1(i)),score(irx_pseudotime1(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime1(i)}); hold on;
    %text(IC10_score(i)+0.015,IC1_score(i),sprintf('%i',days(i)),'Color','r');
    if length(find(ismember(ind_prolif,irx_pseudotime1(i))))>0
        plot(pseudotime1(irx_pseudotime1(i)),score(irx_pseudotime1(i)),'ro','MarkerSize',10);
    end
end
 [ints,score_smoothed] = smooth1(pseudotime1(irx_pseudotime1),score(irx_pseudotime1),15);
 plot(ints,score_smoothed,'g-','LineWidth',4);
xlabel('Pseudotime 1','FontSize',20); ylabel(score_name,'FontSize',20);
set(gcf,'Color','w');
%figure;
for i=1:length(irx_pseudotime2)
    plot(pseudotime2(irx_pseudotime2(i)),score(irx_pseudotime2(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime2(i)}); hold on;
    %text(IC10_score(i)+0.015,IC1_score(i),sprintf('%i',days(i)),'Color','r');
    if length(find(ismember(ind_prolif,irx_pseudotime2(i))))>0
        plot(pseudotime2(irx_pseudotime2(i)),score(irx_pseudotime2(i)),'ro','MarkerSize',10);
    end
end
 [ints,score_smoothed] = smooth1(pseudotime2(irx_pseudotime2),score(irx_pseudotime2),15);
 plot(ints,score_smoothed,'r-','LineWidth',4);
xlabel('Pseudotime 2','FontSize',20); 
xlabel('Pseudotime','FontSize',20); 
ylabel(score_name,'FontSize',20);
set(gcf,'Color','w');



%% pseudotime vs days
figure;
for i=1:length(irx_pseudotime1)
    x = days(irx_pseudotime1(i))+rand()*0.5;
    plot(x,pseudotime1(irx_pseudotime1(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime1(i)},'MarkerSize',7); hold on;
    if length(find(ismember(ind_prolif,irx_pseudotime1(i))))>0
        plot(x,pseudotime1(irx_pseudotime1(i)),'ro','MarkerSize',10);
    end
end
dint = [7 9 10 11 14 17 22];
for i=1:length(dint)
    smx(i) = dint(i);
    smy(i) = mean(pseudotime1(intersect(find(days==dint(i)),irx_pseudotime1)));
end
plot(smx,smy,'g-','LineWidth',4);
xlabel('Days','FontSize',20); ylabel('Pseudotime 1','FontSize',20);
set(gcf,'Color','w');
set(gca,'FontSize',16);
%ylim([min(pseudotime1) max(pseudotime1)]);
xlim([6 23]);
figure;
for i=1:length(irx_pseudotime2)
    x = days(irx_pseudotime2(i))+rand()*0.5;
    plot(x,pseudotime2(irx_pseudotime2(i)),'ko','MarkerFaceColor',color_vals{irx_pseudotime2(i)},'MarkerSize',7); hold on;
    if length(find(ismember(ind_prolif,irx_pseudotime2(i))))>0
        plot(x,pseudotime2(irx_pseudotime2(i)),'ro','MarkerSize',10);
    end
end
dint = [7 9 10 11 14 17 22];
for i=1:length(dint)
    smx(i) = dint(i);
    smy(i) = mean(pseudotime2(intersect(find(days==dint(i)),irx_pseudotime2)));
end
plot(smx,smy,'r-','LineWidth',4);
xlabel('Days','FontSize',20); ylabel('Pseudotime 2','FontSize',20);
set(gcf,'Color','w');
set(gca,'FontSize',16);
%ylim([min(pseudotime1) max(pseudotime1)]);
xlim([6 23]);



%% PCA plot
figure;
%[v,u,s] = pca(datan','NumComponents',NumDimensions);
for i=1:length(IC1_score)
    plot(-u(i,1),u(i,2),'ko','MarkerFaceColor',color_vals{i}); hold on;
    if length(find(ismember(ind_prolif,i)))>0
        plot(-u(i,1),u(i,2),'ro','MarkerSize',10);
    end
    text(-u(i,1)+0.1,u(i,2),sprintf('%i',days(i)));
end
%plot(-u(inds,1),u(inds,2),'kx','MarkerSize',10);
%plot(-u([40,245,376],1),u([40,245,376],2),'bx-','MarkerSize',30);

% plot(-u(irx_pseudotime1,1),u(irx_pseudotime1,2),'go','MarkerSize',15);
% plot(-u(irx_pseudotime2,1),u(irx_pseudotime2,2),'ro','MarkerSize',15);

xlabel(sprintf('PC1 (%2.0f%%)',s(1)/sum(s)*100),'FontSize',20); 
ylabel(sprintf('PC2 (%2.0f%%)',s(2)/sum(s)*100),'FontSize',20); 

%PCAView( NodePositions, Edges, datan');

means = mean(datan');
dt = bsxfun(@minus, datan', means);
nd1 = bsxfun(@minus, nodes1, means);
xNodes1 = -nd1*v(:,1);
yNodes1 = nd1*v(:,2);
%plot(xNodes,yNodes,'ro');
drawGraph2D([xNodes1,yNodes1],edges1,'LineWidth',5,'LineColor','g','ShowClusterNumbers',0);

nd2 = bsxfun(@minus, nodes2, means);
xNodes2 = -nd2*v(:,1);
yNodes2 = nd2*v(:,2);
%plot(xNodes,yNodes,'ro');
drawGraph2D([xNodes2,yNodes2],edges2,'LineWidth',5,'LineColor','r','ShowClusterNumbers',0);

%add k-nn connections
KNN = 2;
X = u(:,1:NumDimensions);
[Idx] = knnsearch(X,X,'K',KNN);
for i=1:size(Idx,1)
    for j=1:KNN-1
        plot([-u(i,1) -u(Idx(i,j+1),1)],[u(i,2) u(Idx(i,j+1),2)],'k-'); hold on;
    end
end

set(gcf,'Color','w');

%% difference in pseudotime, most  var genes

for i=1:size(datan_reduced,1)
    prof = datan_reduced(i,:);
    [ints1,prof_smooth1] = smooth1(pseudotime1(irx_pseudotime1),prof(irx_pseudotime1),10); 
    [ints2,prof_smooth2] = smooth1(pseudotime2(irx_pseudotime2),prof(irx_pseudotime2),10); 
    sc_max = max(prof_smooth1-prof_smooth2);
    sc_min = min(prof_smooth1-prof_smooth2);
    disp(sprintf('%s\t%2.2f\t%2.2f',char(mostvar_genenames(i)),sc_max,sc_min));
end

%% difference in pseudotime, scores

keys = scoreMap.keys;
for i=1:length(keys)
    prof = scoreMap(char(keys(i)));
    if length(prof)>1
    [ints1,prof_smooth1] = smooth1(pseudotime1(irx_pseudotime1),prof(irx_pseudotime1),10); 
    [ints2,prof_smooth2] = smooth1(pseudotime2(irx_pseudotime2),prof(irx_pseudotime2),10); 
    sc_max = max(prof_smooth1-prof_smooth2);
    sc_min = min(prof_smooth1-prof_smooth2);
    disp(sprintf('%s\t%2.2f\t%2.2f',char(keys(i)),sc_max,sc_min));
    end
end

%% visualize RNA velocity
% tab_vel = importdata('C:\Datas\MOSAIC\analysis\Pseudotime\compiled_velocyto.tsv');
% data_vel = tab_vel.data;

pca_coord = load('C:\Datas\MOSAIC\analysis\Pseudotime\velocity_pca.txt');
vector_coord = load('C:\Datas\MOSAIC\analysis\Pseudotime\velocity_vectors.txt');
data_vel(:,1:2) = pca_coord(:,1:2);
data_vel(:,3:4) = vector_coord(:,1:2);

sn = importdata('C:\Datas\MOSAIC\analysis\Pseudotime\sample_names.txt');
irx_reorder = [];
for i=1:length(IC1_score)
    name = sn(i);
    k = find(strcmp(sample_names,name));   
    if length(k)>0
        irx_reorder(k) = i;
    end
end

data_vel = data_vel(irx_reorder,:);


for i=1:length(IC1_score)
    plot(data_vel(i,1),data_vel(i,2),'ko','MarkerFaceColor',color_vals{i},'MarkerSize',12); hold on;
    if length(find(ismember(ind_prolif,i)))>0
        plot(data_vel(i,1),data_vel(i,2),'ro','MarkerSize',13,'LineWidth',3);
        %text(data(i,1),data(i,2),sprintf('%i',days(i)));
    end
end
quiver(data_vel(:,1),data_vel(:,2),data_vel(:,3),data_vel(:,4),'Color','k','LineWidth',2); hold on;
% xlabel('PC1','FontSize',30);
% ylabel('PC2','FontSize',30);
%xlabel('IC-EwS','FontSize',30);
%ylabel('IC-G2/M','FontSize',30);
xlabel('IC-G1/S','FontSize',30);
ylabel('IC-G2/M','FontSize',30);

xlim([min(data_vel(:,1)) max(data_vel(:,1))])
ylim([min(data_vel(:,2)) max(data_vel(:,2))])
set(gca,'FontSize',20);
set(gcf,'Color','w');
set(gcf,'Position',[ 360        -224        1043         837]);

% scalex = (max(data_vel(:,1))-min(data_vel(:,1)))/(max(-u(:,1))-min(-u(:,1)));
% scaley = (max(data_vel(:,2))-min(data_vel(:,2)))/(max(-u(:,2))-min(-u(:,2)));
%plot(-u(:,1),u(:,2),'ko');
means = mean(datan');
dt = bsxfun(@minus, datan', means);
nd1 = bsxfun(@minus, nodes1, means);
xNodes1 = -nd1*v(:,1);
yNodes1 = nd1*v(:,2);
%xNodes1 = mean(nodes1(:,irx_IC2),2);
%yNodes1 = mean(nodes1(:,irx_IC1),2);
%plot(xNodes,yNodes,'ro');
%drawGraph2D([xNodes1,yNodes1],edges1,'LineWidth',5,'LineColor','g','ShowClusterNumbers',0);

nd2 = bsxfun(@minus, nodes2, means);
xNodes2 = -nd2*v(:,1);
yNodes2 = nd2*v(:,2);
%xNodes2 = mean(nodes2(:,irx_IC2),2);
%yNodes2 = mean(nodes2(:,irx_IC1),2);
%plot(xNodes,yNodes,'ro');
%drawGraph2D([xNodes2,yNodes2],edges2,'LineWidth',5,'LineColor','r','ShowClusterNumbers',0);



%add k-nn connections
% KNN = 2;
% X = u(:,1:NumDimensions);
% [Idx] = knnsearch(X,X,'K',KNN);
% for i=1:size(Idx,1)
%     for j=1:KNN-1
%         plot([data_vel(i,1) data_vel(Idx(i,j+1),1)],[data_vel(i,2) data_vel(Idx(i,j+1),2)],'k-'); hold on;
%     end
% end


% nint = 10;
% k = 1;
% tx = [];
% ty = [];
% for i=1:nint
%     irx = find((pseudotime1>(i-1)/nint)&(pseudotime1<i/nint));
%     irx = intersect(irx,irx_pseudotime1);
%     if length(irx)>0
%         tx(k) = mean(data_vel(irx,1));
%         ty(k) = mean(data_vel(irx,2));
%         k = k+1;
%     end
% end
% plot(tx,ty,'go-','LineWidth',5);
% 
% tx = [];
% ty = [];
% for i=1:nint
%     irx = find((pseudotime2>(i-1)/nint)&(pseudotime2<i/nint));
%     irx = intersect(irx,irx_pseudotime2);
%     if length(irx)>0
%         tx(k) = mean(data_vel(irx,1));
%         ty(k) = mean(data_vel(irx,2));
%         k = k+1;
%     end
% end
% plot(tx,ty,'ro-','LineWidth',5);
% 

fid = fopen('C:/datas/MOSAIC/analysis/pseudotime/pca.txt','w');
for i=1:size(datan,2)
    temp = sn(i);
    k = find(strcmp(temp,sample_names));
    fprintf(fid,'%2.2f\t',-u(k,1));
    for j=2:NumDimensions-1
        fprintf(fid,'%2.2f\t',u(k,j));
    end
    fprintf(fid,'%2.2f\n',u(k,NumDimensions));
    
end
fclose(fid);


fid = fopen('C:/datas/MOSAIC/analysis/pseudotime/cellcycle.txt','w');
for i=1:size(datan,2)
    temp = sn(i);
    k = find(strcmp(temp,sample_names));
    fprintf(fid,'%2.2f\t%2.2f\n',IC2_score(k),IC1_score(k));
end
fclose(fid);

fid = fopen('C:/datas/MOSAIC/analysis/pseudotime/IC10_IC1.txt','w');
for i=1:size(datan,2)
    temp = sn(i);
    k = find(strcmp(temp,sample_names));
    fprintf(fid,'%2.2f\t%2.2f\n',IC10_score(k),IC1_score(k));
end
fclose(fid);
