addpath ../data;
addpath C:/Datas/BIODICA_GUI/bin/fastica++/;
dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};


%preload data and then comment
%  tic;
%    for i=1:length(dsets)
%        disp(sprintf('%s',char(dsets(i))));
%        tab{i} = importdata(sprintf('%s_nufp10k.txt',char(dsets(i))));
%        dat{i} = tab{i}.data;
%        gnames{i} = tab{i}.textdata(2:end,1);
%        snames{i} = tab{i}.textdata(1,2:end);
%    end
%  toc;
%  tic;
%    for i=1:length(dsets)
%         prefix = dsets{i};
%         module_tabs{i} = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
%         module_score_tables{i} = module_tabs{i}.data;
%         module_name_tables{i} = module_tabs{i}.textdata(1,2:end);
%    end
%  toc;

n=5;

if 0
k = 1;
for i=1:n
    for j=1:n
        if i~=  j 
        ds1 = dat{i};
        ds2 = dat{j};
        gn1 = gnames{i};
        gn2 = gnames{j};
        common_gns = intersect(gn1,gn2);
        ind1 = find(ismember(gn1,common_gns));
        ind2 = find(ismember(gn2,common_gns));
        ds1_sel = ds1(ind1,:);
        ds2_sel = ds2(ind2,:);
        mn1 = mean(ds1_sel,2);
        mn2 = mean(ds2_sel,2);
        subplot(n,n,k);
        plot(mn1,mn2,'k.');
        drawnow;
        xlabel(dsets{i},'FontSize',8);ylabel(dsets{j},'FontSize',8);
        title(sprintf('Corr=%2.2f',corr(mn1,mn2)),'FontSize',8);
        set(gca,'FontSize',8);
        end
        if i==j
            %subplot(n,n,k);
            %hist(ds1(:),100);
        end
        k = k+1;
    end
end
end
set(gcf,'Color','w');

figure;
for i=1:n
    ms = module_score_tables{i};
    subplot(1,n,i);
    data_cc = [ms(:,1) ms(:,2)];
    data_cc = data_cc/prctile(data_cc(:,1)+data_cc(:,2),98);
    plot(data_cc(:,2),data_cc(:,1),'k.');
    xlabel('G1/S','FontSize',8);ylabel('G2/M','FontSize',8);
    title(sprintf('%s',char(dsets{i})),'FontSize',8);
    xlim([0 0.6]); ylim([0 0.7]);
end
set(gcf,'Position',[174   364   888   163]);
set(gcf,'Color','w');

figure;
for i=1:n
    subplot(1,n,i);
    gene1 = 'AURKA';
    gene2 = 'CCNB2';
    i1 = find(strcmp(gnames{i},gene1));
    i2 = find(strcmp(gnames{i},gene2));
    ds = dat{i};
    d1 = ds(i1,:);
    d2 = ds(i2,:);
    plot(d1,d2,'k.');
    xlabel(gene1,'FontSize',8);ylabel(gene2,'FontSize',8);
    cr = corr(d1',d2','Type','Spearman');
    title(sprintf('Corr = %2.2f',cr),'FontSize',8);
end
set(gcf,'Position',[174   364   888   163]);
set(gcf,'Color','w');