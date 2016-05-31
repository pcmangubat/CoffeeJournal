package pocholo.coffeejournal.Model;

/**
 * Created by bebe on 5/31/2016.
 */
public class SavedImage {
    private String id;
    private String uri;
    private SavedImage(){

    }
    SavedImage(String id, String uri){
        this.id = id;
        this.uri=uri;
    }
    public String getId() {return id;}

    public String getUri() {
        return uri;
    }
}
