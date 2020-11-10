
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Creates static class that is used to collect, manipulate, and access 
 * products and parts in the inventory system.
 * 
 * <p> For future versions of this application, we could expand upon the functionality
 * of the app by creating a user interface that would allow employees of the company
 * to check out products and/or parts to users for loaning or renting equipment.  This would
 * allow employees an easy system to check out products and track how many are checked out
 * and how many are still available in stock or warehouse. </p>
 * 
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    private static int partIdTracker = 0;
    private static int productIdTracker = -1;

    /** Generates unique, even part ID number.  
     This method is called when you create a new part. 
     @return Unique part ID integer.
     */
    public static int generatePartId() {
        partIdTracker += 2;
        return 100 + partIdTracker;
    }
    

    /** Generates unique, odd product ID number.  
     This method is called when you create a new product. 
     @return Unique product ID integer.
     */
    public static int generateProductId() {
        productIdTracker += 2;
        return 300 + productIdTracker;
    }
    
    /** Adds part to inventory. 
     This method is called when adding new part to inventory.
     @param newPart Part to add to inventory.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /** Adds product to inventory. 
     This method is called when adding new product to inventory.
     @param newProduct Product to add to inventory.
     */    
    public static void addProduct(Product newProduct) {
        
        allProducts.add(newProduct);
        
    }
    
   /** Searches for part in inventory with matching ID number. 
     This method is called when performing search on parts and when trying to identify part.
     @param partId Part number to search.
     @return Part with matching ID number.  Returns null if no match found.
     */
    public static Part lookupPart(int partId) {
        for (Part p : allParts) {
            if(p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

     /** Searches for product in inventory with matching ID number. 
     This method is called when performing search on product and when trying to identify product.
     @param productId Product number to search.
     @return Product with matching ID number.  Returns null if no match found.
     */
    public static Product lookupProduct(int productId) {
        for(Product product : Inventory.getAllProducts()) {
            if(product.getId() == productId) {
                 return product;
            }   
        }
        return null;
    }
    
    /** Searches for part(s) in inventory with matching full or partial name. 
     This method is called when performing search on list of parts.
     @param partName Search phrase/term for product name.
     @return List containing any parts with matching name (full or partial).
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partNameMatch = FXCollections.observableArrayList();
        
        for(Part p : allParts) {
            if(p.getName().contains(partName)) {
                partNameMatch.add(p);
            }
        }
        
        return partNameMatch;
    }
    
    /** Searches for product(s) in inventory with matching full or partial name. 
     This method is called when performing search on list of products.
     @param productName Search phrase/term for product name.
     @return List containing any products with matching name (full or partial).
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> productNameMatch = FXCollections.observableArrayList();
        
        for(Product p : allProducts) {
            if(p.getName().contains(productName)) {
                productNameMatch.add(p);
            }
        }
        
        return productNameMatch;
    }
    
    /** Updates existing part in inventory, replacing it with new part.
     This method is called when modifying or replacing part.
     @param index Index of part to replace in inventory part list.
     @param newPart Part to replace/update in inventory part list.
     */       
    public static void updatePart(int index, Part newPart) {
        for(Part part : Inventory.getAllParts()) {
            if(part.getId() == newPart.getId()) {
                 allParts.set(index, newPart);
            }   
        }
    }
    
    /** Updates existing part in inventory, replacing it with new product.
     This method is calling when modifying or replacing product.
     @param index Index of product to replace in inventory product list.
     @param newProduct Part to replace/update in inventory product list.
     */    
    public static void updateProduct(int index, Product newProduct) {
       
        for(Product product : Inventory.getAllProducts()) {
            if(product.getId() == newProduct.getId()) {
                 allProducts.set(index, newProduct);
            }   
        }
    }
    
    /** Deletes part from inventory.
     @param selectedPart Part to be removed from inventory.
     @return Returns true if part deleted, returns false if part not found in inventory.
     */        
    public static boolean deletePart(Part selectedPart) {
        for(Part part : Inventory.getAllParts() ) {
            if(part.equals(selectedPart)) {
                return allParts.remove(part);
            }
        }
        return false;
    }
    
    /** Deletes product from inventory.
     @param selectedProduct Product to be removed from inventory.
     @return Returns true if product deleted, returns false if product not found in inventory.
     */      
    public static boolean deleteProduct(Product selectedProduct) {
        for(Product product : Inventory.getAllProducts() ) {
            if(product.equals(selectedProduct)) {
                return allProducts.remove(product);
            }
        }
        return false;
    }
    
    /** Retrieve all parts in inventory.
     @return List containing all parts in inventory.
     */      
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** Retrieve all products in inventory.
     @return List containing all products in inventory.
     */  
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    /** Get the location of part in inventory list. 
     This method is called before updating/replacing part in list.
     @param part Part to search for in list.
     @return Index/location of part in inventory list.  Returns -1 if not found in list.
     */  
    public static int getPartIndex(Part part) {
        int index = -1;
        for(Part sp : allParts ) {
            ++index;
            if(sp.getId() == part.getId()) {
                 return index;
            }   
        }
        return index;
    }
    
    /** Get the location of product in inventory list. 
     This method is called before updating/replacing product in list.
     @param product Product to search for in list.
     @return Index/location of product in inventory list.  Returns -1 if not found in list.
     */  
    public static int getProductIndex(Product product) {
        int index = 0;
        for(Product searchProduct : allProducts ) {
            if(product.getId() == searchProduct.getId()) {
                 return index;
            }   
            ++index;
        }
        return -1;
    }
    
}
