package ru.levelup.qa.at.test.work.page.objects;

public class Product {

    private String name;
    private double price;
    private String category;
    private Boolean outlet;
    private Boolean isNew;
    private String locationCountry;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getOutlet() {
        return outlet;
    }

    public void setOutlet(Boolean outlet) {
        this.outlet = outlet;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }


    public Product(String name, double price, String category, Boolean outlet, String locationCountry) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.outlet = outlet;
        this.locationCountry = locationCountry;
    }


    public double getMaxPriceFromRange(String priceRange) {
        double maxPrice = -999;

        if(priceRange.contains("до")) {
            String price = priceRange.substring(priceRange.indexOf("до "))
                    .replace("до ", "")
                    .replace("руб.", "")
                    .replace(",", ".")
                    .replace(" ", "")
                    .trim();

            maxPrice = (Double.parseDouble(price));
        }

        return maxPrice;
    }

    public boolean equalsByName(Product obj) {
        if (!(obj instanceof Product))
            return false;
        Product ref = (Product)obj;

        return  this.name.equals(ref.name);
    }

    public boolean equalsByParams(Product obj) {
       if (!(obj instanceof Product))
            return false;
        Product ref = (Product)obj;

      return  this.name.contains(ref.name) &&
              this.isNew == ref.isNew &&
              this.price <= ref.price &&
              this.category.equals(ref.category) &&
              this.outlet == ref.outlet &&
              this.locationCountry.equals(ref.locationCountry);
    }
}

