package pocholo.coffeejournal;

public class WeatherModel {
    public String base;
    public Integer dt;
    public Integer id;
    public String name;
    public Integer cod;

    public Wind wind;

    public class Wind {
        public float speed;
        public int deg;
    }

}