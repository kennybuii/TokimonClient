package sample;


public class Tokimon {
    static private int total = 0;
    private String name;
    private double weight;
    private double height;
    private int privateId;
    private String ability;
    private int strength;
    private String colour;

    public Tokimon(String name, double weight, double height, String ability, int strength, String colour) {
        total++;
        this.privateId = total;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.ability = ability;
        this.strength = strength;
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "Tokimon{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", privateId=" + privateId +
                ", ability='" + ability + '\'' +
                ", strength=" + strength +
                ", colour='" + colour + '\'' +
                '}';
    }

    public int getPrivateId() {
        return privateId;
    }

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        Tokimon.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setPrivateId(int privateId) {
        this.privateId = privateId;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }




}
