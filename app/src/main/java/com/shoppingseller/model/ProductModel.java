package com.shoppingseller.model;

public class ProductModel
{

    String product_id;
    String category_id;
    String choose_category;


    public ProductModel()
    {
    }

    public ProductModel(String product_id, String category_id, String choose_category)
    {
        this.product_id = product_id;
        this.category_id = category_id;
        this.choose_category = choose_category;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getChoose_category() {
        return choose_category;
    }

    public void setChoose_category(String choose_category) {
        this.choose_category = choose_category;
    }
}
