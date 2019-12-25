function drawSpringGraph(prefix,drawedges,mode,par1,par2)

coordinates_file = sprintf('%s_nufp10k_SPRING_coordinates.txt',prefix);

%coordinates_file = sprintf('%s_nufp10k_tsne_coordinates.txt',prefix);

edges_file = sprintf('%s_nufp10k_SPRING_edges.txt',prefix);

coords = load(coordinates_file);
edges = load(edges_file);
edges = edges+1;

if drawedges
%for i=1:size(edges,1)
%    display(sprintf('%i',i));
%    plot([coords(edges(i,1),1) coords(edges(i,2),1)],[coords(edges(i,1),2) coords(edges(i,2),2)],'k-'); hold on;
%end
    coords(:,2) = max(coords(:,2))-coords(:,2);
%plot([coords(edges(:,1),1)';coords(edges(:,2),1)'],[coords(edges(:,1),2)';coords(edges(:,2),2)'],'k-','Color',[0.4 0.4 0.4]); hold on;
plot([coords(edges(:,1),1)';coords(edges(:,2),1)'],[coords(edges(:,1),2)';coords(edges(:,2),2)'],'k-','Color',[0.3 0.3 0.3]); hold on;
end

markersize = 4;
if(strcmp(prefix,'pdx184f'))
    markersize=markersize*2;
end
if(strcmp(prefix,'pdx856'))
    markersize=markersize*2;
end
if(strcmp(prefix,'pdx861'))
    markersize=markersize*2;
end

    %clusters_tab = importdata(sprintf('%s_nufp_clusters2.txt',prefix));
    clusters_tab = importdata(sprintf('%s_nufp_IC10+_T.txt_clusters2',prefix));
    clusters = clusters_tab(2:end);
    inds_ic10plus_low = find(strcmp(clusters,'ic10plus_low'));
    inds_ic10plushigh_tnc = find(strcmp(clusters,'ic10plushigh_tnc'));
    inds_ic10plushigh_igfbp2 = find(strcmp(clusters,'ic10plushigh_igfbp2'));
    inds_ic10plushigh = find(strcmp(clusters,'ic10plushigh'));
    inds_ic10plusmedium = find(strcmp(clusters,'ic10plusmedium'));
    inds_proliferating = find(strcmp(clusters,'proliferating'));
    
%     module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
%     module_scores = module_tab.data;
%     module_names = module_tab.textdata(1,2:end);
%     i1 = find(strcmp(module_names,'IC10+'));
%     sc = module_scores(:,i1);
%     inds_ic10plus_low = find(sc<0.65);
%     inds_ic10plus_low = setdiff(inds_ic10plus_low,inds_proliferating);
%     inds_ic10plushigh = find(sc>0.65);
%     inds_ic10plushigh = setdiff(inds_ic10plushigh,inds_proliferating);
    
    display(sprintf('number of proliferating cells = %i(%2.2f%%)',length(inds_proliferating),length(inds_proliferating)/length(clusters)*100));

if strcmp(mode,'cluster')
    clusters_tab = importdata(sprintf('%s_nufp_clusters.txt',prefix));
    clusters = clusters_tab(2:end);
    inds = find(strcmp(clusters,'proliferating'));
    plot(coords(inds,1),coords(inds,2),'ro','MarkerSize',markersize,'MarkerFaceColor','r'); hold on;
    inds = find(strcmp(clusters,'ic10plus_low'));
    plot(coords(inds,1),coords(inds,2),'bo','MarkerSize',markersize,'MarkerFaceColor','b'); hold on;
    inds = find(strcmp(clusters,'ic10plushigh_tnc'));
    if(length(inds)>0)
        plot(coords(inds,1),coords(inds,2),'mo','MarkerSize',markersize,'MarkerFaceColor','m'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plushigh_igfbp2'));
    if(length(inds)>0)    
        plot(coords(inds,1),coords(inds,2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plushigh'));
    if(length(inds)>0)    
        plot(coords(inds,1),coords(inds,2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plusmedium'));
    if(length(inds)>0)    
        plot(coords(inds,1),coords(inds,2),'co','MarkerSize',markersize,'MarkerFaceColor','c'); hold on;
    end
    
    axis equal;
    axis off;

end

if strcmp(mode,'ic10pca')
    pca_tab = importdata(sprintf('%s_nufp_IC10+_T.txt',prefix));
    %pca_tab = importdata(sprintf('%s_nufp_IC10+EXPRESSEDINTUMORS_T.txt',prefix));
    data = pca_tab.data;
    %data = zscore(data);
    
    clusters_tab = importdata(sprintf('%s_nufp_clusters.txt',prefix));
    clusters = clusters_tab(2:end);
    
    ind_prolif = find(strcmp(clusters,'proliferating'));
    ind_noprolif = setdiff(1:size(data,1),ind_prolif);

    data1 = data(ind_noprolif,:);
    [v,u,s] = pca(data1);
    u=-u*par1;

    display(sprintf('Explained variance %f,%f',s(1)/sum(s),s(2)/sum(s)));

    clusters = clusters(ind_noprolif);
    
    inds = find(strcmp(clusters,'ic10plus_low'));

    plot(u(inds,1),u(inds,2),'bo','MarkerSize',markersize,'MarkerFaceColor','b'); hold on;
    inds = find(strcmp(clusters,'ic10plushigh_tnc'));
    if(length(inds)>0)
        plot(u(inds,1),u(inds,2),'mo','MarkerSize',markersize,'MarkerFaceColor','m'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plushigh_igfbp2'));
    if(length(inds)>0)    
        plot(u(inds,1),u(inds,2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plushigh'));
    if(length(inds)>0)    
        plot(u(inds,1),u(inds,2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
    end
    inds = find(strcmp(clusters,'ic10plusmedium'));
    if(length(inds)>0)    
        plot(u(inds,1),u(inds,2),'co','MarkerSize',markersize,'MarkerFaceColor','c'); hold on;
    end

    mn = mean(data1);
    rp = repmat(mn,length(ind_prolif),1);
    uprolif = (data(ind_prolif,:)-rp)*v;
    uprolif = -uprolif*par1;
    plot(uprolif(:,1),uprolif(:,2),'ro','MarkerFaceColor','r','MarkerSize',4); hold on;

    
    %inds = find(strcmp(clusters,'proliferating'));
    %plot(u(inds,1),u(inds,2),'ro','MarkerSize',markersize*1.5,'MarkerFaceColor','r','MarkerEdgeColor','k'); hold on;

    axis equal;
    axis off;
    
end

if strcmp(mode,'modulescoreplots')
    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    i2 = find(strcmp(module_names,par2));
    
%     plot(module_scores(inds_ic10plus_low,i1),module_scores(inds_ic10plus_low,i2),'bo','MarkerSize',markersize,'MarkerFaceColor','b'); hold on;
%     plot(module_scores(inds_ic10plushigh,i1),module_scores(inds_ic10plushigh,i2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
%     plot(module_scores(inds_ic10plushigh_tnc,i1),module_scores(inds_ic10plushigh_tnc,i2),'mo','MarkerSize',markersize,'MarkerFaceColor','m'); hold on;
%     plot(module_scores(inds_ic10plushigh_igfbp2,i1),module_scores(inds_ic10plushigh_igfbp2,i2),'go','MarkerSize',markersize,'MarkerFaceColor','g'); hold on;
%     plot(module_scores(inds_proliferating,i1),module_scores(inds_proliferating,i2),'ro','MarkerSize',markersize,'MarkerFaceColor','r'); hold on;
%     plot(module_scores(inds_ic10plusmedium,i1),module_scores(inds_ic10plusmedium,i2),'co','MarkerSize',markersize,'MarkerFaceColor','c'); hold on;
    
%     close all;
    notp = setdiff(1:size(module_scores,1),inds_proliferating);
    dscatter(module_scores(notp,i1),module_scores(notp,i2)); hold on;
    markersize = 4;
    plot(module_scores(inds_proliferating,i1),module_scores(inds_proliferating,i2),'ro','MarkerSize',markersize,'MarkerFaceColor','r'); hold on;

    %axis equal;
    if(strcmp(par2,'ECM_HYPOXIA_ATTRACTOR'))
        par2 = 'EGH_ATTRACTOR';
    end
    xlabel(strrep(par1,'_',' '),'FontSize',20);
    ylabel(strrep(par2,'_',' '),'FontSize',20);
    set(gca,'FontSize',14);
    %set(gcf,'Position',[360   256   475   362]);
    %set(gcf,'Color','None');
    n = sum((module_scores(:,i1)<0.5)&(module_scores(:,i2)>0.65));
    n/size(module_scores,1);
end



if strcmp(mode,'modulescorespring')
    cm = load('colormap.txt');
    %colormap = hot;
    %t = (0:31)/32;
    %p1 = [0 0 1]; p2 = [1 1 0]; p3 = [1 0 0];
    %for i=1:32 cm(i,:) = p1+(p2-p1)*t(i); end; for i=33:64 cm(i,:) = p2+(p3-p2)*t(i-32); end;
    %t = (0:31)/32;
    %p1 = [0 0 1]; p2 = [1 1 0]; p3 = [1 0 0];
    %for i=1:32 cm(i,:) = p1+(p2-p1)*t(i); end; for i=33:64 cm(i,:) = p2+(p3-p2)*t(i-32); end;

    %p1 = [0 0 1]; p2 = [1 1 1]; p3 = [1 1 0]; p4 = [1 0 0];
    %for i=1:16 cm2(i,:)=p1+(p2-p1)*(i-1)/16; end; for i=1:16 cm2(i+16,:) = p2+(p3-p2)*(i-1)/16; end; for i=1:32 cm2(32+i,:)=p3+(p4-p3)*(i-1)/32; end;
    %cm = cm2;
    
    colormap = cm;
    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));
    sc = module_scores(:,i1);
    sc = winsor(sc,[par2 100-par2]);
    sc = (sc-min(sc))/(max(sc)-min(sc));
    %scatter(coords(:,1),coords(:,2),markersize*4,colormap(round(sc*50)+1,:),'filled');
    scatter(coords(:,1),coords(:,2),markersize*6,colormap(round(sc*63)+1,:),'filled');
    %title(strrep(par1,'_',' '),'FontSize',30);
    axis equal;
    axis off;
    %set(gcf,'Color',[0.7 0.7 0.7]);
    set(gcf,'Color','None');
    %set(gcf,'Color','w');
end

if strcmp(mode,'genespring')
    colormap = hot;
    cm = load('colormap.txt');
    colormap = cm;
    
    tab = importdata(sprintf('%s_nufp10k.txt',prefix));
    scores = tab.data;
    gene_names = tab.textdata(2:end,1);
    
    inds = zeros(1,length(par1));
    for i=1:length(par1)
        inds(i) = find(strcmp(gene_names,par1(i)));
    end
    if length(inds)==1
        sc = scores(inds,:);
    else
        sc = mean(scores(inds,:));
    end
    sc = winsor(sc,[par2 100-par2]);
    sc = (sc-min(sc))/(max(sc)-min(sc));
    scatter(coords(:,1),coords(:,2),markersize*4,colormap(round(sc*50)+1,:),'filled');

    tit = par1(1);
    if length(inds)>1
        for i=2:length(par1)
            tit = strcat(tit,strcat('+',par1(i)));
        end
    end
        
    title(strrep(tit,'_',' '),'FontSize',30);
    
    axis equal;
    axis off;
    set(gcf,'Color','w');
end



if strcmp(mode,'bar')
    
    addpath ./visualization/

    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);
    i1 = find(strcmp(module_names,par1));

    clusters_tab = importdata(sprintf('%s_nufp_clusters.txt',prefix));
    clusters = clusters_tab(2:end);
    inds_prolif = find(strcmp(clusters,'proliferating'));
    inds_low = find(strcmp(clusters,'ic10plus_low'));
    inds_high = find(strcmp(clusters,'ic10plushigh'));
    inds_medium = find(strcmp(clusters,'ic10plusmedium'));

    data1 = module_scores(inds_prolif,i1); 
    data2 = module_scores(inds_low,i1); 
    data3 = module_scores(inds_medium,i1); 
    data4 = module_scores(inds_high,i1);
    
    %distributionPlot({data1,data2,data3,data4},'globalNorm',2,'histOpt',2,'xNames',{'Prolif','Low','Med','High'},'color',{[1,0,0],[0,0,1],[0,1,1],[0,1,0]});
%    distributionPlot({data1,data2,data4},'globalNorm',2,'histOpt',2,'xNames',{'Prolif','Low','High'},'color',{[1,0,0],[0,0,1],[0,1,0]});
     distributionPlot({data1,data2,data4},'globalNorm',2,'histOpt',1,'xNames',{'Prolif','Low','High'},'color',{[1,0,0],[0,0,1],[0,1,0]});
   %figure; hist(data1,50); title('Prolif');
   %figure; hist(data2,50); title('Low');
   %figure; hist(data4,50); title('High');
     
     
    set(gca,'FontSize',14);
    title(strrep(par1,'_',' '),'FontSize',24);
end


%plot(coords(:,1),coords(:,2),'ro'); hold on;


end
