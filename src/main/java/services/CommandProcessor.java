package services;

import models.MusicItemFactory;
import ui.Message;
import models.MusicItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public final class CommandProcessor {

    // A stack that keeps track of the sourcing files
    private static ArrayList<String> sourcing = new ArrayList<String>();

    // The library the command processor is currently working on
    private static MusicLibrary workingLibrary;


    /**
     * Loads the music items from the default or the specified library.
     *
     * @param lib specifies the library to load the music items from.
     */
    private static void load(String lib) {

        if(lib.isEmpty()) {
            Message.send("Loading from default library file.");
        }
        else {
            Message.send("Loading from file: " + lib);
        }

        // Load the music items from the library
        ArrayList<MusicItem> loaded = MusicLibraryFileHandler.loadLibrary(lib);

        for(MusicItem item : loaded) {
            workingLibrary.addItem(item);
        }

    }


    /**
     * Saves the music items contained in the library to a CSV file.
     *
     * @param file specifies the file we must save the library to.
     */
    private static void save(String file) {

        if(file.isEmpty()) {
            Message.send("Saving to default library file.");
        }
        else {
            Message.send("Saving to file: " + file);
        }

        workingLibrary.save(file);

    }


    /**
     * The method that manages the source command. It reads the file
     * and executes the commands that it contains.
     *
     * @param fileName indicates the file to read.
     */
    private static void source(String fileName) {

        // Default file path where the commands are stored.
        final String DEFAULT_FILE = "commands";

        if(fileName.isEmpty())
            fileName = DEFAULT_FILE;

        // Prevent infinite loop
        if( sourcing.contains(fileName) ) {
            Message.send( "Currently sourcing " + fileName + "; SOURCE ignored." );
        }
        else {

            String filePath = "data/" + fileName + ".txt";
            File file = new File( filePath );

            // Verify if the file exists
            if( !file.exists() ) {
                // Send an error message
                Message.send( "Sourcing " + fileName + " failed; file not found." );
                return;
            }

            // Start the source operation
            sourcing.add( fileName );
            Message.send( "Sourcing " + fileName + "..." );

            // Attempt to read the command file
            try( BufferedReader reader = new BufferedReader( new FileReader( filePath ) ) ) {
                String line;
                while( ( line = reader.readLine() ) != null ) {

                    if(line.charAt(0) == '#' || line.isBlank()) {
                        continue;
                    }
                    else {

                        String[] words = line.split(" ");

                        // If there is only one word
                        if(words.length < 2) {
                            switch(words[0]) {
                                case "SOURCE":
                                    source("");
                                    break;
                                case "LOAD":
                                    load("");
                                    break;
                                case "SAVE":
                                    save("");
                                    break;
                                case "PLAY":
                                    play("");
                                    break;
                                case "PAUSE":
                                    pause();
                                    break;
                                case "STOP":
                                    stop();
                                    break;
                                case "CLEAR":
                                    clear();
                                    break;
                                case "LIST":
                                    list();
                                    break;
                                default:
                                    Message.send( "Unknown command " + line );
                            }
                        }
                        else {

                            // The information to pass as parameters to the commands' methods
                            String parameters = String.join(" ", Arrays.copyOfRange(words, 1, words.length);

                            switch(words[0]) {
                                case "SOURCE":
                                    source(parameters);
                                    break;
                                case "LOAD":
                                    load(parameters);
                                    break;
                                case "SAVE":
                                    save(parameters);
                                    break;
                                case "ADD":
                                    add(parameters);
                                    break;
                                case "REMOVE":
                                    remove(parameters);
                                    break;
                                case "SEARCH":
                                    search(parameters);
                                    break;
                                case "PLAY":
                                    play(parameters);
                                    break;
                                default:
                                    Message.send( "Unknown command " + line );
                            }
                        }

                    }
                }
            } catch( IOException e ) {
                Message.send( "Error reading command file: " + fileName );
            }

            // Clean the sourcing stack
            sourcing.remove( fileName );

        }

    }


    public static void processCommands( MusicLibrary library ) {

        // Assign the working library for the commands' methods' work to be
        // easier.
        workingLibrary = library;

        // We read the commands from the default file commands.txt
        source("");

    }
}
