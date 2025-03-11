package models;






/**
 * This class defines the features of a Song element. It inherits from
 * the MusicItem class.
 *
 * @author Josué Mongan (20290870)
 * @author Kuza Twagiramungu (20317467)
 * @since 2025-03-08
 *
 * © 2025 POOphonia. All rights reserved.
 */
public final class Song extends MusicItem {

    private String artist;
    private String genre;
    private int duration;





    /**
     * The getting function for the artist
     *
     * @return the artist of the song
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * The function sets the song's artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * The getting function for the genre
     *
     * @return the genre of the song
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * The function sets the song's genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * The getting function for the duration
     *
     * @return the duration of the song
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * The function sets the song's duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }









    /**
     * A constructor for the song. This one takes directly the
     * song's attributes as parameters.
     *
     * @param id specifies the new song's id
     * @param title specifies the new song's title
     * @param releaseYear specifies the new song's releaseYear
     * @param artist specifies the new song's artist
     * @param genre specifies the new song's genre
     * @param duration specifies the new song's duration
     */
    public Song(int id, String title, int releaseYear, String artist, String genre, int duration) {
        super(id, title, releaseYear);

        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    /**
     * A constructor for the song element. This one takes the attributes
     * as a bunch in an array.
     *
     * @param parts is an array containing all the information about the song
     */
    public Song(String[] parts) {
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));

        this.artist = parts[4];
        this.genre = parts[5];
        this.duration = Integer.parseInt(parts[6]);
    }







    @Override
    public String toString() {
        return "Song [ID=" + this.id + ", Title=" + this.title + ", Release Year=" + this.releaseYear + ", Artist=" + this.artist + ", Genre=" + this.genre + ", Duration=" + this.duration + "s]";
    }


    @Override
    public String getInfo() {
        return "Song of " + this.releaseYear + " " + this.title + " by " + this.artist;
    }


    @Override
    public String toCSV() {
        return "song," + this.id + "," + this.title + "," + this.releaseYear + "," + this.artist + "," + this.genre + "," + this.duration;
    }

}