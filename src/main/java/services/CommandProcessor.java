package services;

import models.MusicItemFactory;
import ui.Message;
import models.MusicItem;
import models.Song;
import models.Album;
import models.Podcast;

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
     * This method adds a new item to the library while managing
     * all kind of exceptions and cases.
     *
     * @param item specifies the attributes of the new item to add
     */
    private static void add(String item) {
        String[] parameters = item.split(",");

        if(parameters.length < 7) {
            Message.send("Invalid ADD command: ADD " + item);
        }
        else if(parameters.length > 7) {
            Message.send("Wrong number of elements: ADD " + item);
        }
        else {
            String itemType = parameters[0];

            if(!itemType.equals("song") && !itemType.equals("album") && !itemType.equals("podcast")) {
                Message.send("Wrong music item: ADD " + item);
            }
            else {
                String id = parameters[1];
                String releaseYear = parameters[3];

                try {
                    int itemId = Integer.parseInt(id); // Verifying it is a number

                    if(itemId < 1) {
                        Message.send("Invalid music ID: ADD " + item);
                    }
                    else if(workingLibrary.findItem(itemId) != null) {
                        Message.send("ADD " + item + " failed; ID already used.");
                    }
                    else {
                        try {
                            int itemReleaseYear = Integer.parseInt(releaseYear); // Verifying it is a number

                            if(itemReleaseYear < 1850 || itemReleaseYear > 2025) {
                                Message.send("Invalid release year: ADD " + item);
                            }
                            else {

                                // Verifications specific to the type of music item
                                try {
                                    // An initialisation of the item to add
                                    MusicItem toAdd = null;

                                    if("song".equals(itemType)) {
                                        int duration = Integer.parseInt(parameters[6]);

                                        if(duration < 1 || duration > 36000) {
                                            Message.send("Invalid duration: ADD " + item);
                                            return;
                                        }

                                        toAdd = new Song(itemId, parameters[2], itemReleaseYear,
                                                        parameters[4], parameters[5], duration);

                                    }
                                    else if("album".equals(itemType)) {
                                        int numOfTracks = Integer.parseInt(parameters[6]);

                                        if(numOfTracks < 1 || numOfTracks > 100) {
                                            Message.send("Invalid number of tracks: ADD " + item);
                                            return;
                                        }

                                        toAdd = new Album(itemId, parameters[2], itemReleaseYear,
                                                parameters[4], parameters[5], numOfTracks);
                                    }
                                    else {
                                        int numOfPodcasts = Integer.parseInt(parameters[6]);

                                        if(numOfPodcasts < 1 || numOfPodcasts > 500) {
                                            Message.send("Invalid episode number: ADD " + item);
                                            return;
                                        }

                                        toAdd = new Podcast(itemId, parameters[2], itemReleaseYear,
                                                parameters[4], parameters[5], numOfPodcasts);
                                    }


                                    if(workingLibrary.existingItem(toAdd)) {
                                        Message.send("ADD " + item + " failed; item already in library.");
                                        return;
                                    }

                                    workingLibrary.addItem(toAdd);          // Adding the new musical item
                                    workingLibrary.save("");     // Saving the library in the default file

                                    Message.send(toAdd.getInfo() + " added to the library successfully.");

                                } catch(Exception e) {
                                    Message.send("ADD item " + item + " failed; no such item.");
                                }
                            }

                        } catch (NumberFormatException e) {
                            Message.send("Invalid release year: ADD " + item);
                        }
                    }

                } catch (NumberFormatException e) {
                    Message.send("Invalid music ID: ADD " + item);
                }
            }
        }

    }


    /**
     * This method takes an id and removes the music item with the
     * corresponding id from the library.
     *
     * @param id specifies the id of the item to remove
     */
    public static void remove(String id) {

        if(id.isEmpty()) {
            Message.send("Invalid REMOVE command: REMOVE " + id);
        }
        else {
            try {
                int itemId =  Integer.parseInt(id);

                MusicItem toRemove = MusicItem.findItem(itemId);

                if(toRemove == null) {
                    Message.send("REMOVE item " + id + " failed; no such item.");
                }
                else {
                    workingLibrary.removeItem(itemId);

                    Message.send("Removed " + toRemove.getInfo() + " successfully.");
                }

            } catch (NumberFormatException e) {
                Message.send("Invalid ID for REMOVE command: " + id);
            }
        }

    }





    public static void search(String parameters) {

        if(parameters.isEmpty()) {
            Message.send("Invalid SEARCH command: SEARCH " + parameters);
            return;
        }
        else {
            String[] words = parameters.split(" ");

            MusicItem searched = null;

            if(words.length == 1) {
                try {
                    int id = Integer.parseInt(words[0]);

                    searched = workingLibrary.findItem(id);

                    if(searched == null) {
                        throw new Exception("No such item found!");
                    }

                } catch (Exception e) {
                    Message.send("SEARCH item ID " + words[0] + " failed; no such item.");
                }
            }
            else {
                String[] attributes = parameters.split(" by ");

                if(attributes.length != 2) {
                    Message.send("Invalid SEARCH format. Use 'SEARCH <id>' or 'SEARCH <title> by <artist>'");
                    return;
                }
                else {
                    String title = attributes[0];
                    String artist = attributes[1];

                    searched = workingLibrary.findByTitleAndArtist(title, artist);

                    if(searched == null) {
                        Message.send("SEARCH " + title + " by " + artist + " failed; no item found.");
                        return;
                    }
                }
            }

            if(searched != null) {

                // If there is already an item playing, just print the searched item's info
                if(workingLibrary.getActive() != null && workingLibrary.getActive().getIsPlaying()) {
                    Message.send(searched.toString());
                    return;
                }
                else {
                    workingLibrary.setNextToPlay(searched);
                    Message.send(searched.getInfo() + " is ready to PLAY.");
                    return;
                }
            }

        }
    }


    /**
     * This function manages the pause command. It helps pause the item currently playing
     * in the library all of that while also taking care of eventual exceptions.
     *
     * @param parameters specifies the parameters of the command. Normally there is none
     */
    private static void pause(String parameters) {

        // Verifying if the command is valid
        if(!parameters.isEmpty()) {
            Message.send("Invalid PAUSE command: PAUSE " + parameters);
        }
        else {

            MusicItem toPause = workingLibrary.getActive();

            if(toPause == null) {
                Message.send("No item to PAUSE.");
                return;
            }
            else {
                if(!toPause.getIsPlaying()) {
                    Message.send(toPause.getInfo() + "; is already on pause.");
                    return;
                }
                else {
                    workingLibrary.pauseItem();
                    Message.send("Pausing " + toPause.getInfo());
                    return;
                }
            }
        }

    }




    /**
     * This method removes all the items from the library.
     *
     * @param parameters specifies the parameters for the command
     */
    private static void clear(String parameters) {

        // Verifying if the command is valid
        if(!parameters.isEmpty()) {
            Message.send("Invalid CLEAR command: CLEAR " + parameters);
        }

        if(workingLibrary.isEmpty()) {
            Message.send("Music library is already empty.");
        }
        else {
            workingLibrary.clearAllItems();
            Message.send("Music library has been cleared successfully.");
        }

    }



    /**
     * This method lists all the music items of the library.
     *
     * @param parameters specifies the parameters for the command
     */
    private static void list(String parameters) {

        // Verifying if the command is valid
        if(!parameters.isEmpty()) {
            Message.send("Invalid LIST command: LIST " + parameters);
        }

        if(workingLibrary.isEmpty()) {
            Message.send("The library is empty.");
        }
        else {
            Message.send("Library:");
            workingLibrary.listAllItems();
        }

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

                        // The information to pass as parameters to the commands' methods
                        String parameters = String.join(" ", Arrays.copyOfRange(words, 1, words.length));

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
                            case "PAUSE":
                                pause(parameters);
                                break;
                            case "STOP":
                                stop(parameters);
                                break;
                            case "CLEAR":
                                clear(parameters);
                                break;
                            case "LIST":
                                list(parameters);
                                break;
                            case "EXIT":
                                break;
                            default:
                                Message.send( "Unknown command " + line );
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
