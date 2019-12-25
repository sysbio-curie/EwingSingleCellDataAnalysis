addpath ../data/

%prefix = 'pdx352'; nbins = 50; thresh_prolif_begin = 0.658; thresh_ichighhigh = 0.81; thresh_ichigh3 = 0.8775;
%prefix = 'pdx861'; nbins = 30; thresh_prolif_begin = 0.612; thresh_ichighhigh = 0.78; thresh_ichigh3 = 0.8517;
%prefix = 'pdx184'; nbins = 30; thresh_prolif_begin = 0.434; thresh_ichighhigh = 0.61; thresh_ichigh3 = 0.6453; 
prefix = 'pdx856'; nbins = 10; thresh_prolif_begin = 0.546; thresh_ichighhigh = 0.945; thresh_ichigh3 = 0.8237;

module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
module_scores = module_tab.data;
module_names = module_tab.textdata(1,2:end);
cell_ids = module_tab.textdata(2:end,1);
i10p = find(strcmp(module_names,'IC10+'));
clusters_tab = importdata(sprintf('%s_nufp_IC10+_T.txt_clusters2',prefix));
clusters = clusters_tab(2:end);
ic10score = module_scores(:,i10p);

inds_proliferating = find(strcmp(clusters,'proliferating'));
prolif_zone_ind = find((ic10score>thresh_prolif_begin)&(ic10score<thresh_ichighhigh));
below_prolif_ind = find(ic10score<thresh_prolif_begin);
above_prolif_ind = find(ic10score>thresh_ichighhigh);
ic10high3_ind = find(ic10score>thresh_ichigh3);


proliferating_ids = cell_ids(inds_proliferating);
fid = fopen(sprintf('../data/lists/%s_PROLIFERATING.txt',prefix),'w');
for i=1:length(proliferating_ids) fwrite(fid,sprintf('%s\n',char(proliferating_ids(i)))); end
fclose(fid);

prolif_zone_ids = cell_ids(prolif_zone_ind);
fid = fopen(sprintf('../data/lists/%s_IC10HIGH.txt',prefix),'w');
for i=1:length(prolif_zone_ids) fwrite(fid,sprintf('%s\n',char(prolif_zone_ids(i)))); end
fclose(fid);

above_prolif_ids = cell_ids(above_prolif_ind);
fid = fopen(sprintf('../data/lists/%s_IC10HIGH_HIGH.txt',prefix),'w');
for i=1:length(above_prolif_ids) fwrite(fid,sprintf('%s\n',char(above_prolif_ids(i)))); end
fclose(fid);

ic10high3_ids = cell_ids(ic10high3_ind);
fid = fopen(sprintf('../data/lists/%s_IC10HIGH3.txt',prefix),'w');
for i=1:length(ic10high3_ids) fwrite(fid,sprintf('%s\n',char(ic10high3_ids(i)))); end
fclose(fid);

below_prolif_ids = cell_ids(below_prolif_ind);
fid = fopen(sprintf('../data/lists/%s_IC10LOW.txt',prefix),'w');
for i=1:length(below_prolif_ids) fwrite(fid,sprintf('%s\n',char(below_prolif_ids(i)))); end
fclose(fid);


