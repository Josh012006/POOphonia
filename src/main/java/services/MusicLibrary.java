package services;

import java.util.ArrayList;
import models.MusicItem;

public final class MusicLibrary {

    private ArrayList<MusicItem> items;
    private MusicItem playing;              // Keeps track of the MusicItem currently playing

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
     * This function helps remove a MusicItem from the library
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


    public void listAllItems() {
        for (MusicItem music : items) {
            System.out.println(music.toString());
        }
    }

    public void playItem( int id ) {
        MusicItem toPlay = findItem(id);

        if(toPlay != null) {
            toPlay.play();
            playing = toPlay;
        }
    }

    public void pauseItem() {
        if(playing != null) {
            playing.pause();
        }
    }

    public void stopItem() {
        if(playing != null) {
            playing.stop();
        }
    }

    public void clearAllItems() {
        items.clear();
    }

}
