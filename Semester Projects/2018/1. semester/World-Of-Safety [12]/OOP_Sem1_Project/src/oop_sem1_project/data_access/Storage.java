/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.data_access;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * An interface to contract the methods for storing the highscore
 * 
 */
public interface Storage {

    /**
     * Save the result of playthrough.
     *
     * @param result The playthrough result in the form of "Score PlayerName".
     * @throws FileNotFoundException Exception is thrown if it wasn't possible
     *                               to save the result.
     * @throws IOException           Thrown if the file can't be opened.
     */
    void save(String result) throws FileNotFoundException, IOException;

    /**
     * Retrieve all stored results.
     *
     * @return A list of all stored results.
     * @throws FileNotFoundException Exception is thrown if it wasn't possible
     *                               to read the stored results.
     */
    List<String> load() throws FileNotFoundException;
}
