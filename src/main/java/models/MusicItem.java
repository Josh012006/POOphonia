package models;




/**
 * This class defines the features of a Music item
 *
 * @author Josué Mongan (20290870)
 * @author Kuza Twagiramungu (20317467)
 * @since 2025-03-08
 *
 */
public abstract class MusicItem {

    protected int id;
    protected String title;
    protected int releaseYear;
    protected boolean isPlaying;





    /**
     * The getting function for the id
     *
     * @return the id of the music item
     */
    public int getId() {
        return id;
    }

    /**
     * The function sets the item's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The getting function for the release year
     *
     * @return the releaseYear of the music item
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * The function sets the item's releaseYear
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * The getting function for the title
     *
     * @return the title of the music item
     */
    public String getTitle() {
        return title;
    }

    /**
     * The function sets the item's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This function tells if the element is playing
     *
     * @return true if the item is playing
     */
    public boolean getIsPlaying() {
        return isPlaying;
    }

    /**
     * The function sets the playing state of the element
     */
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }


    /**
     * Launches the music item.
     */
    public void play() {
        isPlaying = true;
    }

    /**
     * Pauses the music item.
     */
    public void pause() {
        isPlaying = false;
    }

    /**
     * Stops the music item.
     */
    public void stop() {
        isPlaying = false;
    }







    // Abstract methods that the child classes must redefine

    /**
     * This function gives the music item as a string
     * with all the attributes listed.
     *
     * @return the string value containing all the information on the item's attributes
     */
    public abstract String toString();

    /**
     * This function gives information on the musical aspect of the
     * item. It only gives pertinent information.
     *
     * @return the string containing the pertinent information about the corresponding
     * music element.
     */
    public abstract String getInfo();

    /**
     * This function renders the information of the music item as
     * CSV line.
     *
     * @return the CSV line containing the information on the music item.
     */
    public abstract String toCSV();









    /**
     * The constructor of the MusicItem. Initialises the id, title and releaseYear variable.
     * It also puts isPlaying a false.
     *
     * @param id is the id of the new music item.
     * @param title is the title of the new music item.
     * @param releaseYear is the releaseYear of the new music item.
     */
    public MusicItem(int id, String title, int releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;

        isPlaying = false;
    }


}