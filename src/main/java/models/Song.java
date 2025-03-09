package models;


public final class Song extends MusicItem {

    private String artist;
    private String genre;
    private int duration;


    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }




    public Song(int id, String title, int releaseYear, String artist, String genre, int duration) {
        super(id, title, releaseYear);

        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
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