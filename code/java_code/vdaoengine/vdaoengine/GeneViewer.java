package vdaoengine;

import vdaoengine.data.*;
import vdaoengine.utils.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.BufferedImage;

import com.borland.jbcl.layout.*;

public class GeneViewer extends Applet {
  private boolean isStandalone = false;
  String dataset1;
  String annotation;
  String Title = "";
  String LabelId = "CHIP";
  String LabelName = "GeneSymbol";
  String HashName = "SYNONYM";
  String XPoints = "";
  String XLabels = "";
  String Columns = "";
  String Suffixes = "";
  String YRange = "";
  String Log10 = "";
  String BaseLines = "";
  VDataTable expressionTable = null;
  Vector geneList = new Vector();

  private TextField geneNames = new TextField();
  private Label label1 = new Label();
  private TextArea resultText = new TextArea();
  private Button submitButton = new Button();
  private Choice collapseIdMethodChoice = new Choice();
  private Checkbox logValues = new Checkbox("Log10 scale");
  private Checkbox scaleYAxis = new Checkbox("Scale Y axis");
  private FlowLayout flowLayout1 = new FlowLayout();
  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public GeneViewer() {
  }
  //Initialize the applet
  public void init() {
    try {
      dataset1 = this.getParameter("dataset1", "");
      //dataset1 = "http://bioinfo-out.curie.fr/projects/sitcon/tsc1nas1.dat";
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      annotation = this.getParameter("annotation", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      LabelId = this.getParameter("labelid", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      LabelName = this.getParameter("labelname", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      HashName = this.getParameter("hashname", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      XPoints = this.getParameter("xpoints", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try{
        XLabels = this.getParameter("xlabels", "");
     }
    catch(Exception e) {
        e.printStackTrace();
    }
    try {
        YRange = this.getParameter("yrange", "");
      }
      catch(Exception e) {
        e.printStackTrace();
    }
    /*try {
        Log10 = this.getParameter("log10", "");
      }
      catch(Exception e) {
        e.printStackTrace();
    }*/   
    try {
      Columns = this.getParameter("columns", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      Suffixes = this.getParameter("suffixes", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
      Title = this.getParameter("title", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    try {
        BaseLines = this.getParameter("baselines", "");
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
    
    
    /*dataset1 = "file:///C:/Datas/SGA/dataview/sga_colsize.dat";
    LabelId = "ORF";
    LabelName = "NAME";
    HashName = "NAME";
    XPoints = "1;2;3;4;5;6";
    XLabels = "MMS;MMS02;Zeo;Zeo60;Hu;Hu15";
    YRange = "0;1";
    Columns = "N_MMS;N_MMS0.02;N_Zeo;N_Zeo60;N_HU;N_HU15/Q_MMS;Q_MMS0.02;Q_Zeo;Q_Zeo60;Q_HU;Q_HU15/R_MMS;R_MMS0.02;R_Zeo;R_Zeo60;R_HU;R_HU15";
    Suffixes = "N/Q/R";*/
    
        
    /*dataset1 = "file:///C:/Datas/EWING/Kinetics/kinetic_data/geneviewer/tsc1na.dat";
    LabelId = "CHIP";
    LabelName = "GeneSymbol";
    HashName = "GeneTitle";
    XPoints = "0;1;2;3;6;9;11;13;15;17";
    Columns = "D0;D1;D2;D3;D6;D9;D11;D13;D15;D17";
    Suffixes = "_";*/
    
    
    /*dataset1 = "file:///C:/Datas/Ewing/Kinetics/karineClones/A673_1.dat";
    LabelId = "Probeset";
    LabelName = "GeneSymbol";
    HashName = "SYNONYM";
    XPoints = "0;1;2;3;6;9;11;13;15;17";
    Columns = "A673_1CT0;A673_1CT1;A673_1CT2;A673_1CT3;A673_1CT6;A673_1CT9;A673_1CT11;A673_1CT13;A673_1CT15;A673_1CT17/A673_1CT0;A673_1CT1;A673_1CT2;A673_1CT3;A673_1CT6;A673_1CT9;A673_1CT11;A673_1CT13T3;A673_1CT15T5;A673_1CT17T7";
    Suffixes = "inhibition/inh_react";*/
    
    /*dataset1 = "file:///C:/Datas/Ewing/Kinetics/karineClones/C22.dat";
    LabelId = "Probeset";
    LabelName = "GeneSymbol";
    HashName = "SYNONYM";
    XPoints = "0;1;2;3;6;9;11;13;15;17;20";
    Columns = "C22_T0;C22_T1;C22_T2;C22_T3;C22_T6;C22_T9;C22_T11;C22_T13;C22_T15;C22_T17;C22_T20/C22_T0;C22_T1;C22_T2;C22_T3;C22_T6;C22_T9;C22_T11;C22_T13T3;C22_T15T5;C22_T17T7;C22_T20T10/C22_T0;C22nc_T1;C22nc_T2;C22nc_T3;C22nc_T6;C22nc_T9;C22nc_T11;C22nc_T13;C22nc_T15;C22nc_T17;C22nc_T20";
    Suffixes = "inhibition/inh_react/neg_cntrl";*/
    
    /*dataset1 = "file:///C:/Datas/Ewing/Kinetics/karineClones/E17.dat";
    LabelId = "Probeset";
    LabelName = "GeneSymbol";
    HashName = "SYNONYM";
    XPoints = "0;1;2;3;6;9;11;13;15;17;20";
    Columns = "E17_T0;E17_T1;E17_T2;E17_T3;E17_T6;E17_T9;E17_T11;E17_T13;E17_T15;E17_T17;E17_T20/E17_T0;E17_T1;E17_T2;E17_T3;E17_T6;E17_T9;E17_T11;E17_T13T3;E17_T15T5;E17_T17T7;E17_T20T10/E17_T0;E17nc_T1;E17nc_T2;E17nc_T3;E17nc_T6;E17nc_T9;E17nc_T11;E17nc_T13;E17nc_T15;E17nc_T17;E17nc_T20";
    Suffixes = "inhibition/inh_react/neg_cntrl";*/    
   
    /*dataset1 = "file:///C:/Datas/EWING/Kinetics/kinetic_data/local/dat/A673_2.dat";
    LabelId = "Probeset";
    LabelName = "GeneSymbol";
    HashName = "SYNONYM";
    XPoints = "0;1;2;3;6;9;11;13;15;17";
    Columns = "A673_2CT0;A673_2CT1;A673_2CT2;A673_2CT3;A673_2CT6;A673_2CT9;A673_2CT11;A673_2CT13;A673_2CT15;A673_2CT17/A673_2CT0;A673_2CT1;A673_2CT2;A673_2CT3;A673_2CT6;A673_2CT9;A673_2CT11;A673_2CT13T3;A673_2CT15T5;A673_2CT17T7";
    Suffixes = "inhibition/inh_react";*/    
    
    /*dataset1 = "file:///C:/Datas/MOSAIC/viewer/ts_rescue_complete_log.zip";
    LabelId = "GENE";
    LabelName = "GENE";
    HashName = "GENE";
    XPoints = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40;41;42;43;44;45;46;47;48/49;50;51;52;53;54;55;56;57;58;59;60;61;62;63;64;65;66;67;68;69;70;71;72;73;74;75;76;77;78;79;80;81;82;83;84;85;86;87;88;89;90;91;92;93;94;95;96/97;98;99;100;101;102;103;104;105;106;107;108;109;110;111;112;113;114;115;116;117;118;119;120/121;122;123;124;125;126;127;128;129;130;131;132;133;134;135;136;137;138;139;140;141;142;143;144/145;146;147;148;149;150;151;152;153;154;155;156;157;158;159;160;161;162;163;164;165;166;167;168/169;170;171;172;173;174;175;176;177;178;179;180;181;182;183;184;185;186;187;188;189;190;191;192";
    YRange = "0;4";
    Columns = "day0_01;day0_02;day0_03;day0_04;day0_05;day0_06;day0_07;day0_08;day0_09;day0_10;day0_11;day0_12;day0_13;day0_14;day0_15;day0_16;day0_17;day0_18;day0_19;day0_20;day0_21;day0_22;day0_23;day0_24;day0_25;day0_26;day0_27;day0_28;day0_29;day0_30;day0_31;day0_32;day0_33;day0_34;day0_35;day0_36;day0_37;day0_38;day0_39;day0_40;day0_41;day0_42;day0_43;day0_44;day0_45;day0_46;day0_47;day0_48/day7_49;day7_50;day7_51;day7_52;day7_53;day7_54;day7_55;day7_56;day7_57;day7_58;day7_59;day7_60;day7_61;day7_62;day7_63;day7_64;day7_65;day7_66;day7_67;day7_68;day7_69;day7_70;day7_71;day7_72;day7_73;day7_74;day7_75;day7_76;day7_77;day7_78;day7_79;day7_80;day7_81;day7_82;day7_83;day7_84;day7_85;day7_86;day7_87;day7_88;day7_89;day7_90;day7_91;day7_92;day7_93;day7_94;day7_95;day7_96/day7r_1;day7r_2;day7r_3;day7r_4;day7r_5;day7r_6;day7r_7;day7r_8;day7r_9;day7r_10;day7r_11;day7r_12;day7r_13;day7r_14;day7r_15;day7r_16;day7r_17;day7r_18;day7r_19;day7r_20;day7r_21;day7r_22;day7r_23;day7r_24/day9_25;day9_26;day9_27;day9_28;day9_29;day9_30;day9_31;day9_32;day9_33;day9_34;day9_35;day9_36;day9_37;day9_38;day9_39;day9_40;day9_41;day9_42;day9_43;day9_44;day9_45;day9_46;day9_47;day9_48/day10_49;day10_50;day10_51;day10_52;day10_53;day10_54;day10_55;day10_56;day10_57;day10_58;day10_59;day10_60;day10_61;day10_62;day10_63;day10_64;day10_65;day10_66;day10_67;day10_68;day10_69;day10_70;day10_71;day10_72/day11_73;day11_74;day11_75;day11_76;day11_77;day11_78;day11_79;day11_80;day11_81;day11_82;day11_83;day11_84;day11_85;day11_86;day11_87;day11_88;day11_89;day11_90;day11_91;day11_92;day11_93;day11_94;day11_95;day11_96";
    Suffixes = "day0/day7/dayr7/dayr7+2/dayr7+3/dayr7+4";
    BaseLines = "AV0/AV7/AVR7/AVR9/AVR10/AVR11";*/

    /*dataset1 = "file:///C:/Datas/MOSAIC/viewer/day00_07.dat";
    LabelId = "SYMBOL";
    LabelName = "SYMBOL";
    HashName = "SYMBOL";
    XPoints = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40;41;42;43;44;45;46;47;48/49;50;51;52;53;54;55;56;57;58;59;60;61;62;63;64;65;66;67;68;69;70;71;72;73;74;75;76;77;78;79;80;81;82;83;84;85;86;87;88;89;90;91;92;93;94;95;96";
    YRange = "0;4";
    Columns = "day0_01;day0_02;day0_03;day0_04;day0_05;day0_06;day0_07;day0_08;day0_09;day0_10;day0_11;day0_12;day0_13;day0_14;day0_15;day0_16;day0_17;day0_18;day0_19;day0_20;day0_21;day0_22;day0_23;day0_24;day0_25;day0_26;day0_27;day0_28;day0_29;day0_30;day0_31;day0_32;day0_33;day0_34;day0_35;day0_36;day0_37;day0_38;day0_39;day0_40;day0_41;day0_42;day0_43;day0_44;day0_45;day0_46;day0_47;day0_48/day7_01;day7_02;day7_03;day7_04;day7_05;day7_06;day7_07;day7_08;day7_09;day7_10;day7_11;day7_12;day7_13;day7_14;day7_15;day7_16;day7_17;day7_18;day7_19;day7_20;day7_21;day7_22;day7_23;day7_24;day7_25;day7_26;day7_27;day7_28;day7_29;day7_30;day7_31;day7_32;day7_33;day7_34;day7_35;day7_36;day7_37;day7_38;day7_39;day7_40;day7_41;day7_42;day7_43;day7_44;day7_45;day7_46;day7_47;day7_48";
    Suffixes = "day0/day7";*/
    //BaseLines = "AV0/AV7/AVR7/AVR9/AVR10/AVR11";

    /*dataset1 = "file:///C:/Datas/MOSAIC/viewer/day_fullcompleteseries_stat.dat";
    LabelId = "GENE";
    LabelName = "GENE";
    HashName = "GENE";
    XPoints = "0;7/7;9;10;11;14;17;23/7;9;10;11/10.8;11.2";
    YRange = "0;4";
    Columns = "day0__av;day7__av/day7r__av;day9__av;day10__av;day11__av;day14__av;day17__av;day23__av/day7rb__av;day9b__av;day10b__av;day11b__av/day11c__av;day11c__av";
    Suffixes = "2metastable/rescue/reseq/control";*/
    
    /*for(int i=0;i<9;i++){
    	for(int j=1;j<=48;j++)
    		if(j<48)
    			System.out.print((i*48+j)+";");
    		else
    			System.out.print((i*48+j));
    	System.out.print("/");
    }
    System.out.println();

    String pref[] = {"0","7","7r","9","10","11","14","17","23"};
    for(int i=0;i<9;i++){
    	for(int j=1;j<=48;j++){
    	String js = ""+j;
    	if(js.length()<2) js = "0"+js;
    		if(j<48)
    			System.out.print("day"+pref[i]+"_"+js+";");
    		else
    			System.out.print("day"+pref[i]+"_"+js);
    	}
    	System.out.print("/");
    }
    System.out.println();*/

    
    //dataset1 = "file:///C:/Datas/MOSAIC/viewer/day_fullseries.dat";
    //dataset1 = "file:///C:/Datas/MOSAIC/viewer/test1.zip";
    /*dataset1 = "http://bioinfo-out.curie.fr/projects/sitcon/mosaic/viewer/test1.zip";
    LabelId = "SYMBOL";
    LabelName = "SYMBOL";
    HashName = "SYMBOL";
    XPoints = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40;41;42;43;44;45;46;47;48/49;50;51;52;53;54;55;56;57;58;59;60;61;62;63;64;65;66;67;68;69;70;71;72;73;74;75;76;77;78;79;80;81;82;83;84;85;86;87;88;89;90;91;92;93;94;95;96/97;98;99;100;101;102;103;104;105;106;107;108;109;110;111;112;113;114;115;116;117;118;119;120;121;122;123;124;125;126;127;128;129;130;131;132;133;134;135;136;137;138;139;140;141;142;143;144/145;146;147;148;149;150;151;152;153;154;155;156;157;158;159;160;161;162;163;164;165;166;167;168;169;170;171;172;173;174;175;176;177;178;179;180;181;182;183;184;185;186;187;188;189;190;191;192/193;194;195;196;197;198;199;200;201;202;203;204;205;206;207;208;209;210;211;212;213;214;215;216;217;218;219;220;221;222;223;224;225;226;227;228;229;230;231;232;233;234;235;236;237;238;239;240/241;242;243;244;245;246;247;248;249;250;251;252;253;254;255;256;257;258;259;260;261;262;263;264;265;266;267;268;269;270;271;272;273;274;275;276;277;278;279;280;281;282;283;284;285;286;287;288/289;290;291;292;293;294;295;296;297;298;299;300;301;302;303;304;305;306;307;308;309;310;311;312;313;314;315;316;317;318;319;320;321;322;323;324;325;326;327;328;329;330;331;332;333;334;335;336/337;338;339;340;341;342;343;344;345;346;347;348;349;350;351;352;353;354;355;356;357;358;359;360;361;362;363;364;365;366;367;368;369;370;371;372;373;374;375;376;377;378;379;380;381;382;383;384/385;386;387;388;389;390;391;392;393;394;395;396;397;398;399;400;401;402;403;404;405;406;407;408;409;410;411;412;413;414;415;416;417;418;419;420;421;422;423;424;425;426;427;428;429;430;431;432";
    YRange = "0;4";
    Columns = "day0_01;day0_02;day0_03;day0_04;day0_05;day0_06;day0_07;day0_08;day0_09;day0_10;day0_11;day0_12;day0_13;day0_14;day0_15;day0_16;day0_17;day0_18;day0_19;day0_20;day0_21;day0_22;day0_23;day0_24;day0_25;day0_26;day0_27;day0_28;day0_29;day0_30;day0_31;day0_32;day0_33;day0_34;day0_35;day0_36;day0_37;day0_38;day0_39;day0_40;day0_41;day0_42;day0_43;day0_44;day0_45;day0_46;day0_47;day0_48/day7_01;day7_02;day7_03;day7_04;day7_05;day7_06;day7_07;day7_08;day7_09;day7_10;day7_11;day7_12;day7_13;day7_14;day7_15;day7_16;day7_17;day7_18;day7_19;day7_20;day7_21;day7_22;day7_23;day7_24;day7_25;day7_26;day7_27;day7_28;day7_29;day7_30;day7_31;day7_32;day7_33;day7_34;day7_35;day7_36;day7_37;day7_38;day7_39;day7_40;day7_41;day7_42;day7_43;day7_44;day7_45;day7_46;day7_47;day7_48/day7r_01;day7r_02;day7r_03;day7r_04;day7r_05;day7r_06;day7r_07;day7r_08;day7r_09;day7r_10;day7r_11;day7r_12;day7r_13;day7r_14;day7r_15;day7r_16;day7r_17;day7r_18;day7r_19;day7r_20;day7r_21;day7r_22;day7r_23;day7r_24;day7r_25;day7r_26;day7r_27;day7r_28;day7r_29;day7r_30;day7r_31;day7r_32;day7r_33;day7r_34;day7r_35;day7r_36;day7r_37;day7r_38;day7r_39;day7r_40;day7r_41;day7r_42;day7r_43;day7r_44;day7r_45;day7r_46;day7r_47;day7r_48/day9_01;day9_02;day9_03;day9_04;day9_05;day9_06;day9_07;day9_08;day9_09;day9_10;day9_11;day9_12;day9_13;day9_14;day9_15;day9_16;day9_17;day9_18;day9_19;day9_20;day9_21;day9_22;day9_23;day9_24;day9_25;day9_26;day9_27;day9_28;day9_29;day9_30;day9_31;day9_32;day9_33;day9_34;day9_35;day9_36;day9_37;day9_38;day9_39;day9_40;day9_41;day9_42;day9_43;day9_44;day9_45;day9_46;day9_47;day9_48/day10_01;day10_02;day10_03;day10_04;day10_05;day10_06;day10_07;day10_08;day10_09;day10_10;day10_11;day10_12;day10_13;day10_14;day10_15;day10_16;day10_17;day10_18;day10_19;day10_20;day10_21;day10_22;day10_23;day10_24;day10_25;day10_26;day10_27;day10_28;day10_29;day10_30;day10_31;day10_32;day10_33;day10_34;day10_35;day10_36;day10_37;day10_38;day10_39;day10_40;day10_41;day10_42;day10_43;day10_44;day10_45;day10_46;day10_47;day10_48/day11_01;day11_02;day11_03;day11_04;day11_05;day11_06;day11_07;day11_08;day11_09;day11_10;day11_11;day11_12;day11_13;day11_14;day11_15;day11_16;day11_17;day11_18;day11_19;day11_20;day11_21;day11_22;day11_23;day11_24;day11_25;day11_26;day11_27;day11_28;day11_29;day11_30;day11_31;day11_32;day11_33;day11_34;day11_35;day11_36;day11_37;day11_38;day11_39;day11_40;day11_41;day11_42;day11_43;day11_44;day11_45;day11_46;day11_47;day11_48/day14_01;day14_02;day14_03;day14_04;day14_05;day14_06;day14_07;day14_08;day14_09;day14_10;day14_11;day14_12;day14_13;day14_14;day14_15;day14_16;day14_17;day14_18;day14_19;day14_20;day14_21;day14_22;day14_23;day14_24;day14_25;day14_26;day14_27;day14_28;day14_29;day14_30;day14_31;day14_32;day14_33;day14_34;day14_35;day14_36;day14_37;day14_38;day14_39;day14_40;day14_41;day14_42;day14_43;day14_44;day14_45;day14_46;day14_47;day14_48/day17_01;day17_02;day17_03;day17_04;day17_05;day17_06;day17_07;day17_08;day17_09;day17_10;day17_11;day17_12;day17_13;day17_14;day17_15;day17_16;day17_17;day17_18;day17_19;day17_20;day17_21;day17_22;day17_23;day17_24;day17_25;day17_26;day17_27;day17_28;day17_29;day17_30;day17_31;day17_32;day17_33;day17_34;day17_35;day17_36;day17_37;day17_38;day17_39;day17_40;day17_41;day17_42;day17_43;day17_44;day17_45;day17_46;day17_47;day17_48/day23_01;day23_02;day23_03;day23_04;day23_05;day23_06;day23_07;day23_08;day23_09;day23_10;day23_11;day23_12;day23_13;day23_14;day23_15;day23_16;day23_17;day23_18;day23_19;day23_20;day23_21;day23_22;day23_23;day23_24;day23_25;day23_26;day23_27;day23_28;day23_29;day23_30;day23_31;day23_32;day23_33;day23_34;day23_35;day23_36;day23_37;day23_38;day23_39;day23_40;day23_41;day23_42;day23_43;day23_44;day23_45;day23_46;day23_47;day23_48";
    Suffixes = "day0/day7/day7r/day9/day10/day11/day14/day17/day23";*/
                
    /*dataset1 = "http://bioinfo-out.curie.fr/projects/sitcon/mosaic/viewer/test2.zip";
    LabelId = "SYMBOL";
    LabelName = "SYMBOL";
    HashName = "SYMBOL";
    XPoints = "1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40;41;42;43;44;45;46;47;48";
    YRange = "0;4";
    Columns = "day0_01;day0_02;day0_03;day0_04;day0_05;day0_06;day0_07;day0_08;day0_09;day0_10;day0_11;day0_12;day0_13;day0_14;day0_15;day0_16;day0_17;day0_18;day0_19;day0_20;day0_21;day0_22;day0_23;day0_24;day0_25;day0_26;day0_27;day0_28;day0_29;day0_30;day0_31;day0_32;day0_33;day0_34;day0_35;day0_36;day0_37;day0_38;day0_39;day0_40;day0_41;day0_42;day0_43;day0_44;day0_45;day0_46;day0_47;day0_48";
    Suffixes = "day0";*/
    
    /*dataset1 = "http://bioinfo-out.curie.fr/projects/sitcon/A673_1.dat";
    LabelId = "Probeset";
    LabelName = "GeneSymbol";
    HashName = "SYNONYM";
    XPoints = "0;1;2;3;6;9;11;13;15;17/0;1;2;3;6;9;11;13;15;17";
    YRange = "0;4";
    Columns = "A673_1CT0;A673_1CT1;A673_1CT2;A673_1CT3;A673_1CT6;A673_1CT9;A673_1CT11;A673_1CT13;A673_1CT15;A673_1CT17/A673_1CT0;A673_1CT1;A673_1CT2;A673_1CT3;A673_1CT6;A673_1CT9;A673_1CT11;A673_1CT13T3;A673_1CT15T5;A673_1CT17T7";
    Suffixes = "inhibition/inh_react";
    Log10 = "1";*/
    
  }
  //Component initialization
  private void jbInit() throws Exception {
    //geneNames.setCaretPosition(100);
    this.setLayout(flowLayout1);
    geneNames.setColumns(35);
    geneNames.setFont(new java.awt.Font("Dialog", 1, 10));
    label1.setText("Enter gene name(s) or any gene ids here (use \'_\' instead of spaces)");
    resultText.setColumns(45);
    resultText.setRows(10);
    submitButton.setLabel("Submit");
    submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        submitButton_mouseClicked(e);
      }
    });
    collapseIdMethodChoice.add("Show probeset with the maximal variation");
    collapseIdMethodChoice.add("Show all probesets");
    collapseIdMethodChoice.add("Show median expression");
    //collapseIdMethodChoice.add("Show mean expression");
    this.add(geneNames, null);
    this.add(label1, null);
    this.add(submitButton, null);
    this.add(collapseIdMethodChoice, null);
    this.add(logValues,null);
    this.add(scaleYAxis,null);
    scaleYAxis.setState(true);
    this.add(resultText, null);
  }
  //Start the applet
  public void start() {
	  logValues.setState(!Log10.equals(""));
  }
  //Stop the applet
  public void stop() {
  }
  //Destroy the applet
  public void destroy() {
  }
  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    String[][] pinfo =
      {
      {"dataset1", "String", ""},
      {"annotation", "String", ""},
      };
    return pinfo;
  }
  //Main method
  public static void main(String[] args) {
    GeneViewer applet = new GeneViewer();
    applet.isStandalone = true;
    Frame frame;
    frame = new Frame() {
      protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
          System.exit(0);
        }
      }
      public synchronized void setTitle(String title) {
        super.setTitle(title);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      }
    };
    frame.setTitle("Applet Frame");
    frame.add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(400,320);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }

  void submitButton_mouseClicked(MouseEvent e) {
    resultText.setText(""); resultText.validate();
    String gn = geneNames.getText().trim();

    if(!gn.equals("")){
      try{
      LoadData(false);
      resultText.append("Hashing table...");
      HashTable(expressionTable);
      geneList.clear();
      StringTokenizer st = new StringTokenizer(gn," \t;,");
      while(st.hasMoreTokens()) geneList.add(st.nextToken().toLowerCase());
      resultText.append("Selecting rows from table...");
      VDataTable vt = MakeSelectionFromTable();
      //System.out.println(vt.rowCount+" rows selected.");
      resultText.append("Processing table...");
      VDataTable vtp = ProcessTable(vt);
      resultText.setText(vtp.toString());
      resultText.append("Making plot...");
      MakePlot(vtp);
      }catch(Exception ex){
        resultText.append(ex.toString()); 
        //resultText.append(ex.getMessage());
        resultText.validate();
        ex.printStackTrace();
      }
    }
  }

  public void LoadData(boolean verbose) throws Exception{
    if(expressionTable==null){
      resultText.append("Loading  data...\n"); resultText.validate();
      if(verbose) System.out.println("Loading data...");
      String dat = VDownloader.downloadURL(dataset1);
      if(dataset1.endsWith(".zip")){
    	  UnZip unzip = new UnZip();
    	  dat = unzip.unZipIt(dat);
      }
      resultText.append("Loaded "+dat.length()+" bytes\n"+dat.substring(0,Math.min(100,dat.length()))+"\n"); resultText.validate();
      if(verbose) System.out.println("Loaded "+dat.length()+" bytes\n"+dat.substring(0,Math.min(100,dat.length())));
      resultText.append("Parsing data...\n"); resultText.validate();
      if(verbose) System.out.println("Parsing data...");
      expressionTable = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFileAsString(dat,"expressionTable");
      resultText.append("Parsed "+expressionTable.rowCount+" rows,"+expressionTable.colCount+" columns\n"); resultText.validate();
      if(verbose) System.out.println("Parsed "+expressionTable.rowCount+" rows,"+expressionTable.colCount+" columns");
    }
  }

  public VDataTable MakeSelectionFromTable(){
    /*int k = expressionTable.fieldNumByName(LabelName);
    Vector rows = new Vector();
    for(int i=0;i<expressionTable.rowCount;i++){
      String gs = expressionTable.stringTable[i][k];
      if(geneList.indexOf(gs)>=0){
        rows.add(new Integer(i));
      }
    }
    String s = "";
    for(int m=0;m<expressionTable.colCount;m++)
      s+=expressionTable.fieldNames[m]+"\t";
    s+="\n";
    for(int i=0;i<rows.size();i++){
      int j = ((Integer)rows.get(i)).intValue();
      for(int m=0;m<expressionTable.colCount;m++)
        s+=expressionTable.stringTable[j][m]+"\t";
      s+="\n";
    }*/
    //VDataTable vt1 = vdaoengine.utils.VSimpleProcedures.selectRowsFromList(expressionTable,geneList,LabelName);
    Vector rows = new Vector();
    for(int i=0;i<geneList.size();i++){
      String gene = (String)geneList.get(i);
      Vector v = (Vector)expressionTable.tableHashPrimary.get(gene);
      if(v!=null){
      for(int j=0;j<v.size();j++)
        rows.add(v.get(j));
      }else{
        v = (Vector)expressionTable.tableHashSecondary.get(gene);
        if(v!=null){
          for(int j=0;j<v.size();j++)
            rows.add(v.get(j));
        }
      }
    }
    VDataTable vt1 = new VDataTable();
    vt1.copyHeader(expressionTable);
    vt1.rowCount = rows.size();
    vt1.colCount = expressionTable.colCount;
    vt1.stringTable = new String[vt1.rowCount][vt1.colCount];
    for(int i=0;i<rows.size();i++)
      vt1.stringTable[i] = (String[])expressionTable.stringTable[((Integer)rows.get(i)).intValue()];
    return vt1;
  }

  public void MakePlot(VDataTable vt){
    VPlot vp = new VPlot();

    StringTokenizer st = new StringTokenizer(Columns,"/");
    while(st.hasMoreTokens())
      vp.columnsVector.add(st.nextToken());

    vp.Xpoints = XPoints;
    vp.XLabels = XLabels;
    if(!scaleYAxis.getState())
    	vp.YRange = YRange;
    else
    	vp.YRange = "";
    vp.logValues = logValues.getState();


    st = new StringTokenizer(Suffixes,"/");
    while(st.hasMoreTokens())
      vp.suffixes.add(st.nextToken());
    
    vp.title = Title;
    vp.LabelId = LabelId;
    vp.LabelName = LabelName;
    vp.table = vt;
    vp.BaseLines = BaseLines;
    resultText.append("Creating plot...");
    vp.createPlot();
    resultText.append("Showing plot...");
    vp.showPlot(500,300);
    /*BufferedImage bi = new BufferedImage(400,300,BufferedImage.TYPE_INT_RGB);
    Graphics g = bi.getGraphics();
    this.getGraphics().drawImage(bi,400,0,null);*/

    /*Frame fr = new Frame();
    fr.setSize(400,300);
    BufferedImage bi = new BufferedImage(400,300,BufferedImage.TYPE_INT_RGB);
    Graphics g = bi.getGraphics();
    vp.gv.paint(g);

    fr.show();*/

    //vp.gv.ExportToGIF("temp.gif");


  }

  public void HashTable(VDataTable vt){
	resultText.append("Hashing... "+LabelName+" "+HashName);
	for(int i=0;i<vt.colCount;i++)
		resultText.append(vt.fieldNames[i]+"\t");
	resultText.append("\n");
	resultText.validate();
    if(vt.tableHashPrimary==null){
    vt.tableHashPrimary = new HashMap();
    vt.tableHashSecondary = new HashMap();
    for(int i=0;i<vt.rowCount;i++){
      String label = vt.stringTable[i][vt.fieldNumByName(LabelName)].toLowerCase();
      String hash = vt.stringTable[i][vt.fieldNumByName(HashName)];
      Vector vl = (Vector)vt.tableHashPrimary.get(label);
      if(vl==null) vl = new Vector();
      vl.add(new Integer(i));
      vt.tableHashPrimary.put(label,vl);
      if(hash.length()>1){
        StringTokenizer st = new StringTokenizer(hash,";");
        while(st.hasMoreTokens()){
          String s = st.nextToken().toLowerCase();
          Vector vh = (Vector)vt.tableHashSecondary.get(s);
          if(vh==null) vh = new Vector();
          vh.add(new Integer(i));
          vt.tableHashSecondary.put(s,vh);
        }
      }
    }
    }
  }

  public VDataTable ProcessTable(VDataTable vt){
    VDataTable res = vt;
    if(collapseIdMethodChoice.getSelectedIndex()==0)
      res = VSimpleProcedures.substituteRowsByTheMostVariable(vt,LabelName);
    if(collapseIdMethodChoice.getSelectedIndex()==2)
      res = VSimpleProcedures.substituteRowsByStatistics(vt,LabelName,4);
    if(collapseIdMethodChoice.getSelectedIndex()==3)
      res = VSimpleProcedures.substituteRowsByStatistics(vt,LabelName,0);
    System.out.println(logValues.getState());
    if(!logValues.getState()){
    	for(int i=0;i<res.rowCount;i++){
    		for(int j=0;j<res.colCount;j++)if(res.fieldTypes[j]==vt.NUMERICAL){
    			float f = Float.parseFloat(res.stringTable[i][j]);
    			f = (float)Math.pow(10, f);
    			res.stringTable[i][j] = ""+f;
    		}
    	}
    }
    return res;
  }

}