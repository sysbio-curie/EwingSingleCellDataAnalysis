function [x_smoothed, y_smoothed, y_stdvs, intervals_ind] = smooth1(x,y,nbins)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
dx = (max(x)-min(x))/nbins;
intervals = min(x):dx:max(x);
[~,intervals_ind] = histc(x,intervals);
y_smoothed = [];
y_stdvs = [];
for i=1:nbins+1
    y_smoothed(i) = mean(y(find(intervals_ind==i)));
    y_stdvs(i) = std(y(find(intervals_ind==i)));
end
x_smoothed = intervals;

end

