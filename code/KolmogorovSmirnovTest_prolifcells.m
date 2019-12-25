clear all;
prefix = 'pdx352';
nbins = 30; 

t = importdata([prefix '_nufp2k.txt.moduleAverages']);
data = t.data;
ic10 = find(strcmp(t.textdata(1,2:end),'IC5+'));
ic1 = find(strcmp(t.textdata(1,2:end),'IC1+'));
labels = importdata([prefix '_classes_text.txt']);

iprolif = find(strcmp(labels,'proliferating'));
iic10high = find(strcmp(labels,'ic10plushigh'));
iic10low = find(strcmp(labels,'ic10plus_low'));

x1 = data(iprolif,ic10);
x2 = data(iic10high,ic10);
x3 = data(iic10low,ic10);

disp('Testing prolif against ic10+high');
[h,p,ks2stat] = kstest2(x1,x2)

disp('Testing ic10+low against ic10+high');
[h,p,ks2stat] = kstest2(x3,x2)


[f1,x1c] = ecdf(x1); [f2,x2c] = ecdf(x2); [f3,x3c] = ecdf(x3);

subplot(1,4,1);

h = plot(x1c,f1,'r-'); hold on; plot(x2c,f2,'g-'); plot(x3c,f3,'b-'); legend('prolif','ic10high','ic10low'); xlabel('ic10+ score','FontSize',14); ylabel('Empirical cumulative distribution','FontSize',14);
title(prefix,'FontSize',20);
%figure;

[h1,p1] = hist(x1,nbins); hold on; [h2,p2] = hist(x2,nbins); [h3,p3] = hist(x3,nbins);

subplot(1,4,2);
plot(p1,h1,'r-'); hold on; plot(p2,h2,'g-'); plot(p3,h3,'b-'); legend('prolif','ic10high','ic10low'); xlabel('ic10+ score','FontSize',14); ylabel('Density','FontSize',14);
title(prefix,'FontSize',20);
k=1;
for thresh = min(data(:,ic10)):0.01:max(data(:,ic10))*0.9
    ind = find(data(:,ic10)>thresh);
    ind1 = setdiff(ind,iprolif);
    x2p = data(ind1,ic10);
    [h_,p_,ks2stat_] = kstest2(x1,x2p);
    if h_
        h(k) = 1;
    else
        h(k) = 0;
    end
    p(k) = p_;
    ks2stat(k) = ks2stat_;
    thrsh(k) = thresh;
    k = k+1;
end

subplot(1,4,3);
plot(thrsh,ks2stat,'r-'); hold on; plot(thrsh,-log10(p)/100,'g-'); hold on; plot(thrsh,h,'b-'); legend('stat','-log10(p)/100','h');
xlabel('Threshold in IC10+','FontSize',14);
ylabel('Value','FontSize',14);
title(prefix,'FontSize',20);

subplot(1,4,4);
plot(data(iprolif,ic1),data(iprolif,ic10),'r.'); hold on;
plot(data(iic10high,ic1),data(iic10high,ic10),'g.');
plot(data(iic10low,ic1),data(iic10low,ic10),'b.');
xlabel('IC1+','FontSize',14);
xlabel('IC10+','FontSize',14); legend('prolif','ic10high','ic10low');

set(gcf,'Position',[51         284        1175         322]);