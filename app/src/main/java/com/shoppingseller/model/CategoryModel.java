package com.shoppingseller.model;

public class CategoryModel
{
    String product_id;
    String category_name;
    String category_description;
    String category_price;
    String category_image;
    String choose_category;
    String category_id;
    String choose_status;
    String number_quantity;

    public CategoryModel()
    {
    }

    public CategoryModel(String product_id, String category_name, String category_description, String category_price, String category_image, String choose_category, String category_id, String choose_status, String number_quantity)
    {
        this.product_id = product_id;
        this.category_name = category_name;
        this.category_description = category_description;
        this.category_price = category_price;
        this.category_image = category_image;
        this.choose_category = choose_category;
        this.category_id = category_id;
        this.choose_status = choose_status;
        this.number_quantity = number_quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getCategory_price() {
        return category_price;
    }

    public void setCategory_price(String category_price) {
        this.category_price = category_price;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getChoose_category() {
        return choose_category;
    }

    public void setChoose_category(String choose_category) {
        this.choose_category = choose_category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getChoose_status() {
        return choose_status;
    }

    public void setChoose_status(String choose_status) {
        this.choose_status = choose_status;
    }

    public String getNumber_quantity() {
        return number_quantity;
    }

    public void setNumber_quantity(String number_quantity) {
        this.number_quantity = number_quantity;
    }
}
