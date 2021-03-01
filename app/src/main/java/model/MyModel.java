package model;

public class MyModel {

    String idProd;
    String imageProd;
    String titleProd;
    String descProd;
    String priceProd;
    String ratingProd;

    public MyModel() {
    }

    public MyModel( String idProd, String imageProd, String titleProd, String descProd, String priceProd, String ratingProd ) {
        this.idProd=idProd;
        this.imageProd=imageProd;
        this.titleProd=titleProd;
        this.descProd=descProd;
        this.priceProd=priceProd;
        this.ratingProd=ratingProd;
    }

    public String getIdProd() {
        return idProd;
    }

    public void setIdProd( String idProd ) {
        this.idProd=idProd;
    }

    public String getImageProd() {
        return imageProd;
    }

    public void setImageProd( String imageProd ) {
        this.imageProd=imageProd;
    }

    public String getTitleProd() {
        return titleProd;
    }

    public void setTitleProd( String titleProd ) {
        this.titleProd=titleProd;
    }

    public String getDescProd() {
        return descProd;
    }

    public void setDescProd( String descProd ) {
        this.descProd=descProd;
    }

    public String getPriceProd() {
        return priceProd;
    }

    public void setPriceProd( String priceProd ) {
        this.priceProd=priceProd;
    }

    public String getRatingProd() {
        return ratingProd;
    }

    public void setRatingProd( String ratingProd ) {
        this.ratingProd=ratingProd;
    }
}
