dsets = {'pdx352','pdx861','pdx184','pdx1058','pdx856'};
genes = {'PLIN2','ADM','AKT1','ALDOA','ARNT','BNIP3','CA9','CP','CREB1','CREBBP','EDN1','ENG','ENO1','EP300','EPO','ETS1','FECH','FOS','GATA2','GCK','HIF1A','HK1','HK2','HMOX1','HNF4A','ID2','IGFBP1','ITGB2','JUN','LDHA','LEP','SMAD3','SMAD4','MCL1','NOS2','NPM1','NT5E','FURIN','SERPINE1','PFKFB3','PFKL','PGK1','PGM1','ABCB1','PKM','CXCL12','SLC2A1','SP1','TERT','TF','TFF3','TFRC','VEGFA','CXCR4','BHLHE40','NCOA1','ABCG2','CITED2','NDRG1','NCOA2','COPS5','HDAC7','EGLN1','BHLHE41','EGLN3'};

% tab_all = importdata('pdxa.txt');
% data_all = tab_all.data;
% gene_names = tab_all.textdata(2:end,1);
% sample_names = tab_all.textdata(1,2:end);

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


mn = mean(data_all,2);
for k=1:length(dsets)
    means{k} = mean(dat{k},2);
end

for i=1:length(genes)
    irx = find(strcmp(gene_names,genes{i}));    
    glob_mean = 0;
    count = 0;
    for k=1:length(dsets)
        it = find(strcmp(gnames{k},genes{i}));
        if length(it)>0
            count = count+1;
            temp = means{k};
            glob_mean = glob_mean+temp(it);
        end
    end
    if count>0
        glob_mean = glob_mean/count;
    end
    disp(sprintf('%s\t%f\t%i\t%f',char(genes{i}),mn(irx),count,glob_mean));
end