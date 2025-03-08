package services;

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

    // A list that keeps track of the sourcing files
    private static ArrayList<String> sourcing = new ArrayList<String>();

    // The library the command processor is currently working on
    private static MusicLibrary workingLibrary;

    // A process necessary for exiting the program
    private static boolean mustExit = false;








    /**
     * Loads the music items from the default or the specified library.
     *
     * @param lib specifies the library to load the music items from.
     */
    private static void loadCmd(String lib) {

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
    private static void saveCmd(String file) {

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
    private static void addCmd(String item) {
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
    public static void removeCmd(String id) {

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




    /**
     * This command searches for an element in the library based on the
     * parameters provided.
     *
     * @param parameters specifies the parameters for the search command
     */
    public static void searchCmd(String parameters) {

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

                if(!parameters.contains(" by ") || attributes.length > 2) {
                    Message.send("Invalid SEARCH format. Use 'SEARCH <id>' or 'SEARCH <title> by <artist>'");
                    return;
                }

                if(attributes.length != 2) {
                    Message.send("Invalid SEARCH command: SEARCH " + parameters);
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
     * This method play the correct element in the library while
     * managing the possible errors and exceptions.
     *
     * @param parameters specifies the parameters for the play command
     */
    public static void playCmd(String parameters) {

        MusicItem activeItem = workingLibrary.getActive();

        if(parameters.isEmpty()) {

            if(activeItem != null) {
                if(activeItem.getIsPlaying()) {
                    Message.send(activeItem.getInfo() + " is already playing:");
                }
                else {
                    MusicItem nextToPlay = workingLibrary.getNextToPlay();

                    if(nextToPlay == null) {
                        Message.send("No item to PLAY.");
                    }
                    else {
                        workingLibrary.stopItem();

                        workingLibrary.playItem(nextToPlay.getId());
                        workingLibrary.setNextToPlay(null);

                        Message.send("Playing " + nextToPlay.getInfo());
                    }
                }
            }
            else {
                MusicItem nextToPlay = workingLibrary.getNextToPlay();

                if(nextToPlay == null) {
                    Message.send("No item to PLAY.");
                }
                else {

                    workingLibrary.playItem(nextToPlay.getId());
                    workingLibrary.setNextToPlay(null);

                    Message.send("Playing " + nextToPlay.getInfo());
                }
            }
            return;
        }
        else {
            String[] words = parameters.split(" ");

            MusicItem toPlay = null;

            if(words.length == 1) {
                try {
                    int id = Integer.parseInt(words[0]);

                    toPlay = workingLibrary.findItem(id);

                    if(toPlay == null) {
                        Message.send("PLAY item ID " + id + " failed; no such item.");
                        return;
                    }

                } catch (Exception e) {
                    Message.send("Invalid ID for PLAY command: " + words[0]);
                }
            }
            else {

                String[] attributes = parameters.split(" by ");

                if(!parameters.contains(" by ") || attributes.length > 2) {
                    Message.send("Invalid PLAY format. Use 'PLAY', 'PLAY <id>' or 'PLAY <title> by <artist>'");
                    return;
                }

                if(attributes.length != 2) {
                    Message.send("Invalid PLAY command: PLAY " + parameters);
                    return;
                }
                else {
                    String title = attributes[0];
                    String artist = attributes[1];

                    toPlay = workingLibrary.findByTitleAndArtist(title, artist);

                    if(toPlay == null) {
                        Message.send("PLAY item: " + title + " by " + artist + " failed; no such item.");
                        return;
                    }
                }
            }

            if(toPlay != null) {
                if(activeItem == toPlay && activeItem.getIsPlaying()) {
                    Message.send(toPlay.getInfo() + " is already playing.");
                }
                else {
                    if(activeItem != null) {
                        workingLibrary.stopItem();
                    }

                    workingLibrary.playItem(toPlay.getId());

                    Message.send("Playing " + toPlay.getInfo());
                }

                return;
            }
        }

    }




    /**
     * This function manages the pause command. It helps pause the item currently playing
     * in the library all of that while also taking care of eventual exceptions.
     *
     * @param parameters specifies the parameters of the command. Normally there is none
     */
    private static void pauseCmd(String parameters) {

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
     * This method executes the STOp command to stop the
     * element currently playing in the library while managing the possible exceptions.
     *
     * @param parameters specifies the parameters of the STOP command. Normally there are none.
     */
    public static void stopCmd(String parameters) {
        if(!parameters.isEmpty()) {
            Message.send("Invalid STOP command: STOP " + parameters);
        }
        else {
            MusicItem toStop = workingLibrary.getActive();
            if(toStop == null) {
                Message.send("No item to STOP.");
                return;
            }
            else {
                workingLibrary.stopItem();
                Message.send("Stopping " + toStop.getInfo());
                return;
            }
        }
    }




    /**
     * This method removes all the items from the library.
     *
     * @param parameters specifies the parameters for the command
     */
    private static void clearCmd(String parameters) {

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
    private static void listCmd(String parameters) {

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
    private static void sourceCmd(String fileName) {

        // Initial verification to see if the program must continue or stop
        if(mustExit) {
            return;
        }

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
                                sourceCmd(parameters);
                                break;
                            case "LOAD":
                                loadCmd(parameters);
                                break;
                            case "SAVE":
                                saveCmd(parameters);
                                break;
                            case "ADD":
                                addCmd(parameters);
                                break;
                            case "REMOVE":
                                removeCmd(parameters);
                                break;
                            case "SEARCH":
                                searchCmd(parameters);
                                break;
                            case "PLAY":
                                playCmd(parameters);
                                break;
                            case "PAUSE":
                                pauseCmd(parameters);
                                break;
                            case "STOP":
                                stopCmd(parameters);
                                break;
                            case "CLEAR":
                                clearCmd(parameters);
                                break;
                            case "LIST":
                                listCmd(parameters);
                                break;
                            case "EXIT":

                                if(!parameters.isEmpty()) {
                                    Message.send("Invalid EXIT command: " + line);
                                }
                                else {
                                    mustExit = true;
                                    Message.send("Exiting program...");
                                    return;
                                }

                                break;
                            default:
                                Message.send( "Unknown command " + line );
                        }

                    }
                }
            } catch( IOException e ) {
                Message.send( "Error reading command file: " + fileName );
            }

            // Clean the sourcing list
            sourcing.remove( fileName );


        }

    }


    /**
     * This is the main function of the class. It helps execute all the commands
     * from the command file on the specified library. By default, the command file
     * is data/commands.txt.
     *
     * @param library specifies the MusicLibrary to apply the commands to.
     */
    public static void processCommands( MusicLibrary library ) {

        // Reinitialize the mustExit variable before any new process of commands
        mustExit = false;

        // Assign the working library
        workingLibrary = library;

        if(workingLibrary != null) {
            // We read and execute the commands from the default file commands.txt
            sourceCmd("");
        }

    }
}
