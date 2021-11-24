/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.data_access;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * A class to store and load strings delimited by '\n'
 * 
 */
public class StorageImpl implements Storage {

    /**
     * The file to be written to and read from.
     */
    private final File file;

    /**
     * Constructs a new Storage Implementation Object. This constructor ensures
     * that the File is created if non-existent.
     *
     * @param fileName The name of the storage File.
     * @throws IOException This is thrown if the file couldn't be created or if
     *                     the File is a directory.
     */
    public StorageImpl(String fileName) throws IOException {
        this.file = new File("./", fileName + ".csv");
        if (!this.file.exists()) {
            this.file.createNewFile();
        } else if (this.file.isDirectory()) {
            throw new IOException("The file(" + fileName + ") currently exists as a directory!");
        }
    }

    /**
     * Append a result String to the currently stored results in the File.
     *
     * @param result The result String.
     * @throws FileNotFoundException Thrown if the File wasn't found.
     * @throws IOException           Thrown if the file can't be opened.
     */
    @Override
    public void save(String result) throws FileNotFoundException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, true))) {
            writer.write((this.file.length() == 0 ? "" : "\n") + result);
        }
    }

    /**
     * Loads all currently stored results.
     *
     * @return A List of unordered strings containing all of the stored results.
     * @throws FileNotFoundException Thrown if the file wasn't found.
     */
    @Override
    public List<String> load() throws FileNotFoundException {
        List<String> results = new ArrayList<>();
        try (Scanner scanner = new Scanner(this.file).useDelimiter("\n")) {
            while (scanner.hasNext()) {
                results.add(scanner.next());
            }
        }
        return results;
    }
}
