package models;





/**
 * This class defines the features of a Podcast element. It inherits from
 * the MusicItem class.
 *
 * @author Josué Mongan (20290870)
 * @author Kuza Twagiramungu (20317467)
 * @since 2025-03-08
 *
 * © 2025 POOphonia. All rights reserved.
 */
public final class Podcast extends MusicItem {

    private String host;
    private int episodeNumber;
    private String topic;





    /**
     * The getting function for the host
     *
     * @return the host of the podcast
     */
    public String getHost() {
        return this.host;
    }

    /**
     * The function sets the podcast's host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * The getting function for the episodeNumber
     *
     * @return the episode number of the podcast
     */
    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    /**
     * The function sets the podcast's episodeNumber
     */
    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    /**
     * The getting function for the topic
     *
     * @return the topic of the podcast
     */
    public String getTopic() {
        return this.topic;
    }

    /**
     * The function sets the podcast's topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }


    /**
     * A constructor for the podcast. This one takes directly the
     * podcast's attributes as parameters.
     *
     * @param id specifies the new podcast's id
     * @param title specifies the new podcast's title
     * @param releaseYear specifies the new podcast's releaseYear
     * @param host specifies the new podcast's host
     * @param episodeNumber specifies the new podcast's episodeNumber
     * @param topic specifies the new podcast's topic
     */
    public Podcast(int id, String title, int releaseYear, String host, int episodeNumber, String topic) {
        super(id, title, releaseYear);

        this.host = host;
        this.episodeNumber = episodeNumber;
        this.topic = topic;
    }

    /**
     * A constructor for the podcast element. This one takes the attributes
     * as a bunch in an array.
     *
     * @param parts is an array containing all the information about the podcast
     */
    public Podcast(String[] parts) {
        super(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]));

        this.host = parts[4];
        this.episodeNumber = Integer.parseInt(parts[6]);
        this.topic = parts[5];
    }







    @Override
    public String toString() {
        return "Podcast [ID=" + this.id + ", Title=" + this.title + ", Release Year=" + this.releaseYear + ", Host=" + this.host + ", Episode=" + this.episodeNumber + ", Topic=" + this.topic + "]";
    }


    @Override
    public String getInfo() {
        return "Podcast " + this.title + " episode " + this.episodeNumber + " of " + this.releaseYear + " on " + this.topic + " by " + this.host;
    }


    @Override
    public String toCSV() {
        return "podcast," + this.id + "," + this.title + "," + this.releaseYear + "," + this.host + "," + this.topic + "," + this.episodeNumber;
    }

}