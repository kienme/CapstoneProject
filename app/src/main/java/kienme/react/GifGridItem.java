package kienme.react;

/**
 * Created by ravikiran on 19/1/17.
 *
 */

public class GifGridItem {
    long id;
    String image;
    String title;

    public GifGridItem() {
    }

    public GifGridItem(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
