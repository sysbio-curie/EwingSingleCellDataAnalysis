    module_tab = importdata(sprintf('%s_nufp2k.txt.moduleAverages',prefix));
    module_scores = module_tab.data;
    module_names = module_tab.textdata(1,2:end);

    %for i=length(module_names):length(module_names)
    for i=1:length(module_names)
        display(sprintf('%i/%i: %s',i,length(module_names),char(module_names(i))));
        if(~isnan(module_scores(1,i)))
            drawSpringGraph(prefix,1,'modulescorespring',module_names(i),5);
            set(gcf,'Color',[0.85 0.85 0.85]);
            fig = gcf;
            fig.InvertHardcopy = 'off';
            saveas(gcf,sprintf('%s.png',char(module_names(i))),'png');
            close all;
        end
    end