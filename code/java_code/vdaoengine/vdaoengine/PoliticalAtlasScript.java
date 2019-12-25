package vdaoengine;

import java.io.*;
import java.util.*;

import vdaoengine.data.*;
import vdaoengine.analysis.*;
import vdaoengine.utils.*;
import vdaoengine.data.io.*;

import java.awt.*;

public class PoliticalAtlasScript {
  public static void main(String[] args) {

    try{

    int nint = 5;

    // Generate color table
    HashMap colors = new HashMap();
    int n = 1;
    for(int i=0;i<=nint;i++)
      for(int j=0;j<=nint;j++)
        for(int k=0;k<=nint;k++){
           int r = (int)((float)i/nint*255f);
           int g = (int)((float)j/nint*255f);
           int b = (int)((float)k/nint*255f);
           //System.out.print("<symbol val=\""+n+"\" fill=\""+r+":"+g+":"+b+"\" outline=\"0:128:0\"/>\n");
           colors.put(""+r+":"+g+":"+b,""+n);
           n++;
        }

     String fn = "c:/datas/atlas/indexes3.dat";
     VDataTable vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fn);
     VDataSet vd = vdaoengine.utils.VSimpleProcedures.SimplyPreparedDatasetWithoutNormalization(vt,-1);
     vd.calcStatistics();

     VDataTable vto = new VDataTable();
     vto.addNewColumn("ABBREV","","",vto.STRING,"");
     vto.rowCount = vt.rowCount;
     vto.stringTable = new String[vt.rowCount][vto.colCount];
     for(int i=0;i<vt.rowCount;i++)
       vto.stringTable[i][0] = vt.stringTable[i][vt.fieldNumByName("ABBREV")];

     Vector fields = new Vector();
     fields.add("QL"); fields.add("PW"); fields.add("MN"); fields.add("SO"); fields.add("DM");

     Vector codes = new Vector();

     for(int i1=0;i1<fields.size();i1++)
       for(int i2=i1+1;i2<fields.size();i2++)
         for(int i3=i2+1;i3<fields.size();i3++){
           Vector sel = new Vector();
           sel.add(fields.get(i1)); sel.add(fields.get(i2)); sel.add(fields.get(i3));
           Collections.sort(sel);
           String code = "R_"+(String)sel.get(0)+"_G_"+(String)sel.get(1)+"_B_"+(String)sel.get(2);
           if(codes.indexOf(code)<0){
             System.out.println(code);
             codes.add(code);
             vto.addNewColumn(code,"","",vto.NUMERICAL,"");
             int ir = vd.getCoordNumByTableName((String)sel.get(0));
             int ig = vd.getCoordNumByTableName((String)sel.get(1));
             int ib = vd.getCoordNumByTableName((String)sel.get(2));

             for(int i=0;i<vt.rowCount;i++){
               float vr = (vd.massif[i][ir]-vd.simpleStatistics.getMin(ir))/(vd.simpleStatistics.getMax(ir)-vd.simpleStatistics.getMin(ir));
               float vg = (vd.massif[i][ig]-vd.simpleStatistics.getMin(ig))/(vd.simpleStatistics.getMax(ig)-vd.simpleStatistics.getMin(ig));
               float vb = (vd.massif[i][ib]-vd.simpleStatistics.getMin(ib))/(vd.simpleStatistics.getMax(ib)-vd.simpleStatistics.getMin(ib));
               vr = 0.8f*vr+0.2f; vg = 0.8f*vg+0.2f; vb = 0.8f*vb+0.2f;
               int vri = (int)((((int)(vr*nint))*1f/nint)*255);
               int vgi = (int)((((int)(vg*nint))*1f/nint)*255);
               int vbi = (int)((((int)(vb*nint))*1f/nint)*255);
               /*if(sel.get(0).equals("MN")) vri = 255 - vri;
               if(sel.get(1).equals("MN")) vgi = 255 - vgi;
               if(sel.get(2).equals("MN")) vbi = 255 - vbi;*/
               String num = (String)colors.get(""+vri+":"+vgi+":"+vbi);
               //System.out.print("<symbol val=\""+n+"\" fill=\""+vri+":"+vgi+":"+vbi+"\" outline=\"0:128:0\"/>\n");
               vto.stringTable[i][vto.fieldNumByName(code)] = num;
             }
           }
     }

     Vector indf = new Vector();
     indf.add(new Integer(vt.fieldNumByName("QL")));
     indf.add(new Integer(vt.fieldNumByName("PW")));
     indf.add(new Integer(vt.fieldNumByName("MN")));
     indf.add(new Integer(vt.fieldNumByName("SO")));
     indf.add(new Integer(vt.fieldNumByName("DM")));
     VDataSet vdp = new VDataSet();
     VTableSelector VS = new VTableSelector();
     VS.selectColumns(indf);
     vdp.loadFromDataTable(vt,VS);
     vdp.standardPreprocess();
     vdp.table = vt;

     PCAMethod pca = new PCAMethod();
     pca.setDataSet(vdp);
     pca.calcBasis(5);
     VDataSet vdpca = pca.getProjectedDataset();
     System.out.println(pca.linBasis.toString());
     for(int i=0;i<5;i++){
       vto.addNewColumn("PC"+(i+1),"","",vto.NUMERICAL,"");
       for(int r=0;r<vt.rowCount;r++)
         vto.stringTable[r][vto.colCount-1] = ""+vdpca.massif[r][i];
     }
     vdpca.calcStatistics();
     vto.addNewColumn("PC","","",vto.NUMERICAL,"");
     int ir = 0; int ig = 1; int ib = 2;
     for(int r=0;r<vt.rowCount;r++){
       float vr = (vdpca.massif[r][ir]-vdpca.simpleStatistics.getMin(ir))/(vdpca.simpleStatistics.getMax(ir)-vdpca.simpleStatistics.getMin(ir));
       float vg = (vdpca.massif[r][ig]-vdpca.simpleStatistics.getMin(ig))/(vdpca.simpleStatistics.getMax(ig)-vdpca.simpleStatistics.getMin(ig));
       float vb = (vdpca.massif[r][ib]-vdpca.simpleStatistics.getMin(ib))/(vdpca.simpleStatistics.getMax(ib)-vdpca.simpleStatistics.getMin(ib));
       vr = 0.8f*vr+0.2f; vg = 0.8f*vg+0.2f; vb = 0.8f*vb+0.2f;
       int vri = (int)((((int)(vr*nint))*1f/nint)*255);
       int vgi = (int)((((int)(vg*nint))*1f/nint)*255);
       int vbi = (int)((((int)(vb*nint))*1f/nint)*255);
       String num = (String)colors.get(""+vri+":"+vgi+":"+vbi);
       vto.stringTable[r][vto.fieldNumByName("PC")] = num;
     }
     /*for(int i=0;i<vt.rowCount;i++)
     for(int j=0;j<5;j++){
       vtr.stringTable[i][j+numOfStringFields] = "" + vdp.massif[i][j];
     }*/

    vdaoengine.data.io.VDatReadWrite.saveToVDatFile(vto,"c:/datas/atlas/triples.dat");

     fn = "c:/datas/atlas/immap1.dat";
     vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fn);
     FileWriter fw = new FileWriter("c:/datas/atlas/immap");
     for(int i=0;i<vt.rowCount;i++) if(!vt.stringTable[i][vt.fieldNumByName("MBR_XMIN")].equals("_")){
       float xmin = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MBR_XMIN")]);
       float xmax = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MBR_XMAX")]);
       float ymin = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MBR_YMIN")]);
       float ymax = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("MBR_YMAX")]);

       float xmn = -180f, xmx = +180f, ymn = -90f, ymx = +90f;
       int ixmin = (int)((xmin-xmn)/(xmx-xmn)*802);
       int ixmax = (int)((xmax-xmn)/(xmx-xmn)*802);
       int iymin = 40+411-(int)((ymin-ymn)/(ymx-ymn)*411);
       int iymax = 40+411-(int)((ymax-ymn)/(ymx-ymn)*411);

       int xcentr = (int)(0.5f*(ixmin+ixmax));
       int ycentr = (int)(0.5f*(iymin+iymax));
       int rad = (int)(Math.min(Math.abs(ixmax-ixmin),Math.abs(iymax-iymin))*0.3);

       if(rad==0) rad = 1;

       String name = vt.stringTable[i][vt.fieldNumByName("NAME")];
       name = name.replace('_',' ');
       String namer = vt.stringTable[i][vt.fieldNumByName("NAMER")];
       namer = namer.replace('_',' ');

       if(!name.equals(" ")){
       String txt = namer+"\\n"+
                    vt.stringTable[i][vt.fieldNumByName("ABBREV")]+" , "+vt.stringTable[i][vt.fieldNumByName("FIPS")]
                    +"\\n\\n"+
                    "QL:"+vt.stringTable[i][vt.fieldNumByName("QL")]+"\t("+vt.stringTable[i][vt.fieldNumByName("QL_R")]+")\\n"+
                    "PW:"+vt.stringTable[i][vt.fieldNumByName("PW")]+"\t("+vt.stringTable[i][vt.fieldNumByName("PW_R")]+")\\n"+
                    "MN:"+vt.stringTable[i][vt.fieldNumByName("MN")]+"\t("+vt.stringTable[i][vt.fieldNumByName("MN_R")]+")\\n"+
                    "SO:"+vt.stringTable[i][vt.fieldNumByName("SO")]+"\t("+vt.stringTable[i][vt.fieldNumByName("SO_R")]+")\\n"+
                    "DM:"+vt.stringTable[i][vt.fieldNumByName("DM")]+"\t("+vt.stringTable[i][vt.fieldNumByName("DM_R")]+")\\n"+
                    "\\n\\n"+
                    "PC1:"+vt.stringTable[i][vt.fieldNumByName("PC1")]+"\\n"+
                    "PC2:"+vt.stringTable[i][vt.fieldNumByName("PC2")]+"\\n"+
                    "PC3:"+vt.stringTable[i][vt.fieldNumByName("PC3")]+"\\n"+
                    "PC4:"+vt.stringTable[i][vt.fieldNumByName("PC4")]+"\\n"+
                    "PC5:"+vt.stringTable[i][vt.fieldNumByName("PC5")]+"\\n"
                ;
       String href = "http://ru.wikipedia.org/wiki/"+namer;
       //fw.write("<area shape=\"CIRCLE\" alt=\""+name+"\" coords=\""+ixmin+","+iymin+","+ixmax+","+iymax+"\" onMouseOver=\"showtext('"+txt+"');\" onMouseOut=\"showtext('');\" href=\""+href+"\">\r\n");
       fw.write("<area shape=\"CIRCLE\" alt=\""+namer+"\" coords=\""+xcentr+","+ycentr+","+rad+"\" onMouseOver=\"showtext('"+txt+"');\" onMouseOut=\"showtext('');\" href=\""+href+"\">\r\n");
       }

     }
     fw.close();

     fn = "c:/datas/atlas/immapdata.dat";
     vt = vdaoengine.data.io.VDatReadWrite.LoadFromVDatFile(fn);
     fw = new FileWriter("c:/datas/atlas/immapdata");
     for(int i=0;i<vt.rowCount;i++) {
       String name = vt.stringTable[i][vt.fieldNumByName("NAME_ENG")];
       name = name.replace('_',' ');
       String namer = vt.stringTable[i][vt.fieldNumByName("NAME")];
       namer = namer.replace('_',' ');

       float x = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("X")]);
       float y = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("Y")]);

       // 29, 11
       // 476, 459
       int ix = (int)(29f+(x+1f)/2f*(476-29));
       int iy = 466-(int)(11f+(y+1f)/2f*(459-11));


       String txt = namer+"\\n"+
                    vt.stringTable[i][vt.fieldNumByName("ABBREV")]
                    +"\\n\\n"+
                    "QL:"+vt.stringTable[i][vt.fieldNumByName("QL")]+"\t("+vt.stringTable[i][vt.fieldNumByName("QL_R")]+")\\n"+
                    "PW:"+vt.stringTable[i][vt.fieldNumByName("PW")]+"\t("+vt.stringTable[i][vt.fieldNumByName("PW_R")]+")\\n"+
                    "MN:"+vt.stringTable[i][vt.fieldNumByName("MN")]+"\t("+vt.stringTable[i][vt.fieldNumByName("MN_R")]+")\\n"+
                    "SO:"+vt.stringTable[i][vt.fieldNumByName("SO")]+"\t("+vt.stringTable[i][vt.fieldNumByName("SO_R")]+")\\n"+
                    "DM:"+vt.stringTable[i][vt.fieldNumByName("DM")]+"\t("+vt.stringTable[i][vt.fieldNumByName("DM_R")]+")\\n"+
                    "\\n\\n"
                ;
       String href = "http://ru.wikipedia.org/wiki/"+namer;
       //fw.write("<area shape=\"CIRCLE\" alt=\""+name+"\" coords=\""+ixmin+","+iymin+","+ixmax+","+iymax+"\" onMouseOver=\"showtext('"+txt+"');\" onMouseOut=\"showtext('');\" href=\""+href+"\">\r\n");
       fw.write("<area shape=\"CIRCLE\" alt=\""+namer+"\" coords=\""+ix+","+iy+","+5+"\" onMouseOver=\"showtext('"+txt+"');\" onMouseOut=\"showtext('');\" href=\""+href+"\">\r\n");
     }
     fw.close();

     fw = new FileWriter("c:/datas/atlas/spektrum.xls");
     for(int i=0;i<vt.rowCount;i++){
       String abbrev = vt.stringTable[i][vt.fieldNumByName("ABBREV")];
       String name = vt.stringTable[i][vt.fieldNumByName("NAME_ENG")];
       name = name.replace('_',' ');
       String namer = vt.stringTable[i][vt.fieldNumByName("NAME")];
       namer = namer.replace('_',' ');
       float x = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("X")]);
       float y = Float.parseFloat(vt.stringTable[i][vt.fieldNumByName("Y")]);
       x = (x+1f)/2f;  y = (y+1f)/2f;
       Color col = Spectrum2D(x,y);
       float vr = 1f/255f*col.getRed();
       float vg = 1f/255f*col.getGreen();
       float vb = 1f/255f*col.getBlue();
       /*int vri = (int)((((int)(vr*nint))*1f/nint)*255);
       int vgi = (int)((((int)(vg*nint))*1f/nint)*255);
       int vbi = (int)((((int)(vb*nint))*1f/nint)*255);
       String code = (String)colors.get(""+vri+":"+vgi+":"+vbi);*/
       int vri = (int)(vr*255);
       int vgi = (int)(vg*255);
       int vbi = (int)(vb*255);
       System.out.print("<symbol val=\""+(i+1)+"\" fill=\""+vri+":"+vgi+":"+vbi+"\" outline=\"0:0:0\"/>\n");
       fw.write(abbrev+"\t"+(i+1)+"\r\n");
     }
     fw.close();

    }catch(Exception e){
      e.printStackTrace();
    }

  }

  public static Color Spectrum2D(float x, float y){
    Color res = null;
    int r = Math.round((1f-x/2f-y/2f)*280f);
    int g = Math.round((1f/2f-x/2f+y/2f)*280f);
    int b = Math.round(x*255f);
    if(r>255) r=255;
    if(g>255) g=255;
    if(b>255) b=255;
    res = new Color(r,g,b);
    return res;
  }

  public static Color Spectrum1DRadial(float x){
    float st = 1.0f/6;
    int r=0,g=0,b=0;
    if (x<=st)  {
     r=0;
     g=Math.round(255/st*x);
     b=Math.round(255);
    }
    if ((x>st)&&(x<=2*st))  {
     r=0;
     g=Math.round(255);
     b=Math.round(255-(x-st)*255/st);
    }
    if ((x>2*st)&&(x<=3*st))  {
     r=Math.round(255/st*(x-2*st));
     g=Math.round(255);
     b=0;
    }
    if ((x>3*st)&&(x<=4*st))  {
     r=Math.round(255);
     g=Math.round(255-255/st*(x-3*st));
     b=0;
    }
    if ((x>4*st)&&(x<=5*st))  {
     r=Math.round(255);
     g=0;
     b=Math.round(255/st*(x-4*st));
    }
    if ((x>5*st)&&(x<=6*st))  {
     r=Math.round(255-255/st*(x-5*st));;
     g=0;
     b=Math.round(255);
    }
    return new Color(r,g,b);
  }

  public static Color Spectrum2DRadial(float x, float y){
    x=x-0.5f;
    y=y-0.5f;
    float rad=(float)Math.sqrt(x*x+y*y);
    float phi = 0f;
    if((x>0)&&(y>0))  phi = -(float)Math.atan(y/x);
    if((x>0)&&(y<0))  phi = (float)Math.atan(-y/x);
    if((x<0)&&(y<0))  phi = (float)(3.141926/2+Math.atan(x/y));
    if((x<0)&&(y>0))  phi = (float)(3.141926+Math.atan(-y/x));
    phi+=3.141926/4;
    if(phi<0)  phi=3.141926f*2f+phi;
    phi=phi/2f/3.1415926f;
    Color col = Spectrum1DRadial(phi);
    int r = col.getRed();
    int g = col.getGreen();
    int b = col.getBlue();
    rad=(float)(1.0/(1.0+Math.pow(2.0*rad,2.0)));
    r=Math.round(r*(1-rad)+85*rad);
    g=Math.round(g*(1-rad)+85*rad);
    b=Math.round(b*(1-rad)+85*rad);
    return new Color(r,g,b);
  }

}