
package model;

/**
 * Creates class that is used to work with outsourced parts.
 * This class is a subclass of the abstract Parts class
 */
public class Outsourced extends Part {
    
    private String companyName;

     /** Constructor to create outsource part.
      * @param companyName who is providing part
      * @param id of part
      * @param name of part
      * @param price of part
      * @param stock or inventory count of part
      * @param min amount of part
      * @param max amount of part
     */
    public Outsourced(String companyName, int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Set company name for outsourced part.
     @param companyName Name of company providing part.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** Get the company name.
     @return Name of company providing part.
     */
    public String getCompanyName() {
        return companyName;
    }
   
    
}
