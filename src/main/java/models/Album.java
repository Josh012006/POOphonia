package models;




/**
 * This class defines the features of an Album element. It inherits from
 * the MusicItem class.
 *
 * @author Josué Mongan (20290870)
 * @author Kuza Twagiramungu (20317467)
 * @since 2025-03-08
 *
 */
public final class Album extends MusicItem {

    private String artist;
    private int numberOfTracks;
    private String label;





    /**
     * The getting function for the artist
     *
     * @return the artist of the album
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * The function sets the album's artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * The getting function for the numberOfTracks
     *
     * @return the numberOfTracks of the album
     */
    public int getNumberOfTracks() {
        return this.numberOfTracks;
    }

    /**
     * The function sets the album's number of tracks
     */
    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    /**
     * The getting function for the label
     *
     * @return the label of the album
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * The function sets the album's label
     */
    public void setLabel(String label) {
        this.label = label;
    }









    /**
     * A constructor for the album. This one takes directly the
     * album's attributes as parameters.
     *
     * @param id specifies the new album's id
     * @param title specifies the new album's title
     * @param releaseYear specifies the new album's releaseYear
     * @param artist specifies the new album's artist
     * @param numberOfTracks specifies the new album's numberOfTracks
     * @param label specifies the new album's label
     */
    public Album(int id, String title, int releaseYear, String artist, int numberOfTracks, String label) {
        super(id, title, releaseYear);

        this.artist = artist;
        this.numberOfTracks = numberOfTracks;
        this.label = label;
    }

    /**
     * A constructor for the album element. This one takes the attributes
     * as a bunch in an array.
     *
     * @param parts is an array containing all the information about the album
     */
    public Album(String[] parts) {
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));

        this.artist = parts[4];
        this.numberOfTracks = Integer.parseInt(parts[6]);
        this.label = parts[5];
    }







    @Override
    public String toString() {
        return "Album [ID=" + this.id + ", Title=" + this.title + ", Release Year=" + this.releaseYear + ", Artist=" + this.artist + ", Tracks=" + this.numberOfTracks + ", Label=" + this.label + "]";
    }


    @Override
    public String getInfo() {
        return "Album " + this.title + " of " + this.releaseYear + " with " + this.numberOfTracks + " tracks by " + this.artist;
    }


    @Override
    public String toCSV() {
        return "album," + this.id + "," + this.title + "," + this.releaseYear + "," + this.artist + "," + this.label + "," + this.numberOfTracks;
    }

}