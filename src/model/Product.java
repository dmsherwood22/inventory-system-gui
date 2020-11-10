
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Creates class used to create and work with products for inventory system
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Constructor to create product.
     * @param id of product
     * @param name of product
     * @param price of product
     * @param stock or inventory count of product
     * @param min amount of product
     * @param max amount of product
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** 
     @param id Set product ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** 
     @param name Set product name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     @param price Set product price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /** 
     @param stock Set product stock also known as inventory count.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** 
     @param min Set product minimum amount.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /** 
     @param max Set product maximum amount.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /** 
     @return product ID.
     */
    public int getId() {
        return id;
    }

    /** 
     @return product name.
     */    
    public String getName() {
        return name;
    }

    /** 
     @return product price.
     */
    public double getPrice() {
        return price;
    }

    /** 
     @return product stock also known as inventory count.
     */
    public int getStock() {
        return stock;
    }

    /** 
     @return product minimum amount.
     */
    public int getMin() {
        return min;
    }

    /** 
     @return product maximum amount.
     */
    public int getMax() {
        return max;
    }
 
    /** Add associated part to product.  Method called when adding or modifying product.
     @param part Part to add to product.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
     /** Delete associated part from product.  Method called when modifying product.
     @param selectedAssociatedPart Part to delete/dissociate from product.
     @return true or false depending on if part is found associated to product.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (Part searchPart : associatedParts) {
            if (searchPart.equals(selectedAssociatedPart)) {
                associatedParts.remove(selectedAssociatedPart);
                return true;
            }
        }
        return false;
    }
    
     /** Get list of all parts associated with product.
     @return list of associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
