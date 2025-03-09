package models;


public final class Album extends MusicItem {

    private String artist;
    private int numberOfTracks;
    private String label;


    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNumberOfTracks() {
        return this.numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }



    public Album(int id, String title, int releaseYear, String artist, int numberOfTracks, String label) {
        super(id, title, releaseYear);

        this.artist = artist;
        this.numberOfTracks = numberOfTracks;
        this.label = label;
    }

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