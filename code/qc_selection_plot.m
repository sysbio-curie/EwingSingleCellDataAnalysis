function [total_counts,mit_score] = qc_selection_plot(prefix)

t1 = importdata([prefix '_u' '.txt']);
data = t1.data;
total_counts = sum(data,1);

t2 = importdata([prefix '_nu.txt.moduleAverages']);
data = t2.data;
mit_score = data;

plot(total_counts,mit_score,'k.'); xlabel('Total counts'); ylabel('Mit score'); title(prefix);

end

