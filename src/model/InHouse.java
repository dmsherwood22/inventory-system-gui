package model;

/**
 * Creates class that is used to work with in-house parts.  
 * This class is a subclass of the abstract Parts class
 */
public class InHouse extends Part {
    private int machineId;

    /** Constructor to create in-house part.
     * @param machineId ID number for in-house machine part
     * @param id number of part
     * @param name of part
     * @param price of part
     * @param stock or inventory count of part
     * @param min amount of part
     * @param max amount of part
     */
    public InHouse(int machineId, int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Set machine ID number.  
     @param machineId Number for in-house part machine identification.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /** Get the machine ID number. 
     @return In-house part machine ID number
     */
    public int getMachineId() {
        return machineId;
    }
    
}
