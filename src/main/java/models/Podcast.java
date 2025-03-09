package models;


public final class Podcast extends MusicItem {

    private String host;
    private int episodeNumber;
    private String topic;


    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getEpisodeNumber() {
        return this.episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }



    public Podcast(int id, String title, int releaseYear, String host, int episodeNumber, String topic) {
        super(id, title, releaseYear);

        this.host = host;
        this.episodeNumber = episodeNumber;
        this.topic = topic;
    }

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