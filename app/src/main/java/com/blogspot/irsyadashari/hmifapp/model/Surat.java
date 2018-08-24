package com.blogspot.irsyadashari.hmifapp.model;

public class Surat {


    private String jenisSurat;
    private String perihalSurat;
    private String tanggalSurat;
    private String keteranganSurat;
    private String userId;
    private String displayName;
    private String fileSizeValue;

    public Surat() {
    }

    public Surat(String jenisSurat, String perihalSurat, String tanggalSurat, String keteranganSurat, String userId,String displayName,String fileSizeValue) {
        this.userId = userId;
        this.jenisSurat = jenisSurat;
        this.perihalSurat = perihalSurat;
        this.tanggalSurat = tanggalSurat;
        this.keteranganSurat = keteranganSurat;
        this.displayName = displayName;
        this.fileSizeValue = fileSizeValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileSizeValue() {
        return fileSizeValue;
    }

    public void setFileSizeValue(String fileSizeValue) {
        this.fileSizeValue = fileSizeValue;
    }

    public String getSuratID() {
        return userId;
    }

    public void setSuratID(String suratID) {
        this.userId = suratID;
    }

    public String getJenisSurat() {
        return jenisSurat;
    }

    public void setJenisSurat(String jenisSurat) {
        this.jenisSurat = jenisSurat;
    }

    public String getPerihalSurat() {
        return perihalSurat;
    }

    public void setPerihalSurat(String perihalSurat) {
        this.perihalSurat = perihalSurat;
    }

    public String getTanggalSurat() {
        return tanggalSurat;
    }

    public void setTanggalSurat(String tanggalSurat) {
        this.tanggalSurat = tanggalSurat;
    }

    public String getKeteranganSurat() {
        return keteranganSurat;
    }

    public void setKeteranganSurat(String keteranganSurat) {
        this.keteranganSurat = keteranganSurat;
    }
}
