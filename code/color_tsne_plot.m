x = mr;

ind_plushigh = find(strcmp(labels,'ic10plushigh'));
ind_plus_low = find(strcmp(labels,'ic10plus_low'));
ind_prolif = find(strcmp(labels,'proliferating'));

plot(x(ind_prolif,1),x(ind_prolif,2),'r.'); xlabel('tSNE1','FontSize',20); ylabel('tSNE2','FontSize',20); title('Initital data','FontSize',20); hold on;
plot(x(ind_plushigh,1),x(ind_plushigh,2),'g.'); 
plot(x(ind_plus_low,1),x(ind_plus_low,2),'b.'); 

legend('Proliferating','IC10+ high','IC10+ low');

