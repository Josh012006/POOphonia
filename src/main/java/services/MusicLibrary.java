package services;

import java.util.ArrayList;
import models.MusicItem;
import ui.Message;


/**
 * This class defines the components and the features
 * of the library. It is used to manage MusicItems.
 *
 * @author Josué Mongan
 * @since 2025-03-06
 *
 */
public final class MusicLibrary {

    private ArrayList<MusicItem> items;     // The data structure to contain all MusicItem elements
    private MusicItem playing = null;              // Keeps track of the MusicItem currently playing

//    public MusicLibrary(ArrayList<MusicItem> items) {
//
//    }

    /**
     * A custom function to find an item in the library based on its id
     *
     * @param id an integer that is the id of the searched item
     */
    public MusicItem findItem( int id ) {
        for(MusicItem item : items) {
            if(item.id == id) {
                return item;
            }
        }
        return null;    // Returns null if no such item is found
    }


    /**
     * Used to add an item to the library
     *
     * @param item specifies the MusicItem object to add
     */
    public void addItem( MusicItem item) {
        items.add(item);
    }


    /**
     * This method helps remove a MusicItem from the library
     * based on the id.
     *
     * @param id specifies the id of the item to remove
     */
    public void removeItem( int id ) {
        MusicItem toRemove = findItem(id);

        if(toRemove != null) {
            items.remove(id);
        }
    }

    /**
     * This function tells if the library is empty
     * @return true if the library is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }


    /**
     * This method lists all the items contained in the library
     */
    public void listAllItems() {
        for (MusicItem music : items) {
            Message.send(music.toString());
        }
    }


    /**
     * This method plays a MusicItem from the library
     * based on the id.
     *
     * @param id specifies the id of the item to remove
     */
    public void playItem( int id ) {
        MusicItem toPlay = findItem(id);

        if(toPlay != null) {
            toPlay.play();
            playing = toPlay;
        }
    }


    /**
     * This method pauses the MusicItem that is currently playing
     */
    public void pauseItem() {
        if(playing != null) {
            playing.pause();
        }
    }


    /**
     * This method stops the MusicItem that is currently playing
     */
    public void stopItem() {
        if(playing != null) {
            playing.stop();
            playing = null;
        }
    }


    /**
     * This method clears all the items in the library
     */
    public void clearAllItems() {
        items.clear();
    }


    /**
     * This method saves the items of the library in a CSV file. If libraryName is empty
     * the default file POOphonia.csv is used.
     *
     * @param libraryName specifies the name of the CSV file we want to save the items to
     */
    public void save(String libraryName) {
        MusicLibraryFileHandler.saveLibrary(items, libraryName);
    }

}
