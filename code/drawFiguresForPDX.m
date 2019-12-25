function drawFiguresForPDX(prefix)

addpath ../data/

% drawSpringGraph(prefix,false,'ic10pca',1);

drawSpringGraph(prefix,true,'modulescorespring','IC10+',5);

%figure; drawSpringGraph(prefix,true,'modulescorespring','IC14+',5);

figure; drawSpringGraph(prefix,true,'modulescorespring','IC30+',5);
% 
 figure; drawSpringGraph(prefix,true,'modulescorespring','IC1+',5);
% 
 figure; drawSpringGraph(prefix,true,'modulescorespring','IC2+',5);
% 
 figure; drawSpringGraph(prefix,true,'modulescorespring','HALLMARK_OXIDATIVE_PHOSPHORYLATION',5);
% 
% figure; drawSpringGraph(prefix,true,'modulescorespring','COL6A1_COL6A2',5);
% 
 figure; drawSpringGraph(prefix,true,'modulescorespring','PID138045_HIF1alpha_transcription_factor_network',5);
% 
 figure; drawSpringGraph(prefix,true,'modulescorespring','ECM_HYPOXIA_ATTRACTOR',5);
 
  figure; drawSpringGraph(prefix,true,'modulescorespring','GO0006007_glucose_catabolic_process',5);

%figure; drawSpringGraph(prefix,false,'modulescoreplots','IC1+','IC2+'); ylabel(''); xlabel('');
% figure; drawSpringGraph(prefix,false,'modulescoreplots','IC1+','IC2+'); set(gcf,'Color','w'); %xlabel('IC1+'); ylabel('IC2+'); 
% 
% figure; drawSpringGraph(prefix,false,'modulescoreplots','IC1+','IC10+'); set(gcf,'Color','w'); %xlabel('IC1+'); ylabel('IC10+'); 
% 
% figure; drawSpringGraph(prefix,false,'modulescoreplots','IC10+','ECM_HYPOXIA_ATTRACTOR'); set(gcf,'Color','w'); %xlabel('IC10+'); ylabel('ECM_HYPOXIA_ATTRACTOR'); 
% 
% figure; drawSpringGraph(prefix,false,'modulescoreplots','PID138045_HIF1alpha_transcription_factor_network','ECM_HYPOXIA_ATTRACTOR'); set(gcf,'Color','w'); %xlabel('PID138045 HIF1alpha transcription factor network'); ylabel('ECM_HYPOXIA_ATTRACTOR'); 
% 
% figure; drawSpringGraph(prefix,false,'modulescoreplots','IC10+','HALLMARK_OXIDATIVE_PHOSPHORYLATION'); set(gcf,'Color','w'); %xlabel('IC10+'); ylabel('HALLMARK_OXIDATIVE_PHOSPHORYLATION'); 
% 
% figure;
% 
% %drawSpringGraph(prefix,false,'modulescoreplots','IC10+','GO0006007_glucose_catabolic_process'); set(gcf,'Color','w');
% 
% subplot(1,4,1);
% drawSpringGraph(prefix,false,'modulescoreplots','ECM_HYPOXIA_ATTRACTOR','PID138045_HIF1alpha_transcription_factor_network'); set(gcf,'Color','w'); %xlabel('EGH signature'); ylabel('PID138045 HIF1alpha targets'); 
% subplot(1,4,2);
% drawSpringGraph(prefix,false,'modulescoreplots','ECM_HYPOXIA_ATTRACTOR','GO0006007_glucose_catabolic_process'); set(gcf,'Color','w'); %xlabel('EGH signature'); ylabel('GO0006007 glucose catabolism'); 
% subplot(1,4,3);
% drawSpringGraph(prefix,false,'modulescoreplots','ECM_HYPOXIA_ATTRACTOR','COLLAGENS'); set(gcf,'Color','w'); %xlabel('EGH signature'); ylabel('COLLAGENS'); 
% subplot(1,4,4);
% drawSpringGraph(prefix,false,'modulescoreplots','ECM_HYPOXIA_ATTRACTOR','IC14+'); set(gcf,'Color','w'); %xlabel('EGH signature'); ylabel('IC14+'); 
% 
% figure;
% 
% subplot(1,2,1);
% drawSpringGraph(prefix,false,'modulescoreplots','IC10+','IC14+'); set(gcf,'Color','w'); %xlabel('IC10+'); ylabel('IC14+'); 

%drawSpringGraph(prefix,false,'modulescoreplots','IC10+','IC30+'); set(gcf,'Color','w'); %xlabel('IC10+'); ylabel('IC14+'); 

% subplot(1,2,2);
% drawSpringGraph(prefix,false,'modulescoreplots','PID138045_HIF1alpha_transcription_factor_network','IC14+'); set(gcf,'Color','w'); %xlabel('PID138045 HIF1alpha'); ylabel('IC14+'); 
% 
% drawSpringGraph(prefix,false,'modulescoreplots','PID138045_HIF1alpha_transcription_factor_network','COLLAGENS'); set(gcf,'Color','w'); %xlabel('PID138045 HIF1a targets'); ylabel('COLLAGENS'); 

end

