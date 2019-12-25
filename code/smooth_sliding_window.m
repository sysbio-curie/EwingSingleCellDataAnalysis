function [x_smoothed, y_smoothed] = smooth_sliding_window(x,y,npoints)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
[~,p] = sort(x);
x_smoothed = x;
y1 = y(p);
smoother_matrix = NaN(length(y),npoints+1);
smoother_matrix(:,1) = y1;
for i=1:npoints
    smoother_matrix(i+1:end,i+1) = y1(1:end-i);
end
y_smoothed1 = mean(smoother_matrix,2);
for i=1:npoints
    y_smoothed1(i) = mean(smoother_matrix(i,find(~isnan(smoother_matrix(i,:)))),2);
end

y_smoothed(p) = y_smoothed1;

end

