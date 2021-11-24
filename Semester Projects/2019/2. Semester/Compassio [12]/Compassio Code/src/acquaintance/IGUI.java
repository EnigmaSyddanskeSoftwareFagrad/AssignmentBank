package acquaintance;

/**
 * Defines methods that the presentation layer must implement in order to function 
 * with the system start class.
 * @author Peter Brændgaard
 */
public interface IGUI {
    
    /**
     * Injects the business logic into the GUI layer.
     * @param LogicLayer object that implements the business logic
     */
    public void injectLogic(ILogic LogicLayer);
    
    /**
     * Launches the application with GUI
     * @param args arguments from the command line
     */
    public void startApplication(String[] args);

}
