package models;





public abstract class MusicItem {

    private int id;
    private String title;
    private int releaseYear;
    private boolean isPlaying;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }




    public void play() {
        isPlaying = true;
    }


    public void pause() {
        isPlaying = false;
    }

    public void stop() {
        isPlaying = false;
    }


    public abstract String toString();
    public abstract String getInfo();
    public abstract String toCSV();



    public MusicItem(int id, String title, int releaseYear) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;

        isPlaying = false;
    }


}