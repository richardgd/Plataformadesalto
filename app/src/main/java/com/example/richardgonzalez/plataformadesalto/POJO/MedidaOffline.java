package com.example.richardgonzalez.plataformadesalto.POJO;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */
public class MedidaOffline {

    private String OffTV;
    private String OffTR;
    private String OffH;
    private String OffID;
    private String OffTS;
    private String OffP;


    public MedidaOffline(String offTV, String offTR, String offH, String offID, String offTS, String offP) {
        OffTV = offTV;
        OffTR = offTR;
        OffH = offH;
        OffID = offID;
        OffTS = offTS;
        OffP = offP;
    }

    public String getOffTV() {
        return OffTV;
    }

    public void setOffTV(String offTV) {
        OffTV = offTV;
    }

    public String getOffTR() {
        return OffTR;
    }

    public void setOffTR(String offTR) {
        OffTR = offTR;
    }

    public String getOffH() {
        return OffH;
    }

    public void setOffH(String offH) {
        OffH = offH;
    }

    public String getOffID() {
        return OffID;
    }

    public void setOffID(String offID) {
        OffID = offID;
    }

    public String getOffTS() {
        return OffTS;
    }

    public void setOffTS(String offTS) {
        OffTS = offTS;
    }

    public String getOffP() {
        return OffP;
    }

    public void setOffP(String offP) {
        OffP = offP;
    }
}
