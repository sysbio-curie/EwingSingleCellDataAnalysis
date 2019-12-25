clear all;

prefix = 'pdx352';
prefix = 'pdx861';
prefix = 'pdx184';
prefix = 'pdx1058';
prefix = 'pdx856';
nbins = 20; 
pos_fields = {'IC1+','IC2+','IC3+','IC4+','IC5+','IC6+','IC7+','IC8+','IC9+','IC10+','IC11+','IC12+','IC13+','IC14+','IC15+','IC16+','IC17+','IC18+','IC19+','IC20+','IC21+','IC22+','IC23+','IC24+','IC25+','IC26+','IC27+','IC28+','IC29+','IC30+','TOTAL_COUNTS'};
scores = zeros(length(pos_fields),1);

%for i=1:length(pos_fields)
for i=10:10
    
t = importdata([prefix '_nufp2k.txt.moduleAverages']);
data = t.data;
ic = find(strcmp(t.textdata(1,2:end),char(pos_fields(i))));
ic1 = find(strcmp(t.textdata(1,2:end),'IC1+'));
labels = importdata([prefix '_classes_text.txt']);

iprolif = find(strcmp(labels,'proliferating'));
inonprolif = setdiff(1:size(data,1),iprolif);

x1 = data(iprolif,ic);
x2 = data(inonprolif,ic);

disp(sprintf('Testing prolif against %s',char(pos_fields(i))));
[h,p,ks2stat] = kstest2(x1,x2);

scores(i) = ks2stat;

[f1,x1c] = ecdf(x1); [f2,x2c] = ecdf(x2); 

figure;

set(gcf,'Position',[75         318        1077         300]);

subplot(1,3,1);

h = plot(x1c,f1,'r-'); hold on; plot(x2c,f2,'g-'); legend('prolif',char(pos_fields(i))); xlabel([char(pos_fields(i)) ' score'],'FontSize',14); ylabel('Empirical cumulative distribution','FontSize',14);
title([char(prefix) ' ' num2str(ks2stat)],'FontSize',20);

subplot(1,3,2);
[h1,p1] = hist(x1,nbins); hold on; [h2,p2] = hist(x2,nbins); 
plot(p1,h1,'r-'); hold on; plot(p2,h2,'g-'); legend('prolif',char(pos_fields(i))); xlabel([char(pos_fields(i)) ' score'],'FontSize',14); ylabel('Density','FontSize',14);
title([char(pos_fields(i)) ' ' sprintf('%4.4e',p)],'FontSize',20);

subplot(1,3,3);

plot(data(:,ic),data(:,ic1),'k.'); hold on; plot(data(iprolif,ic),data(iprolif,ic1),'ro','MarkerSize',2);

drawnow;

end

figure;

plot(scores,'ko-');