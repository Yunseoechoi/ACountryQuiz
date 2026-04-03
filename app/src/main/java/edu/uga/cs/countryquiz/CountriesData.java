package edu.uga.cs.countryquiz;

/**
 * This class (a POJO) represents a single job lead, including the id, country name,
 * city and continent.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class CountriesData {
    private long   id;
    private String name;
    private String capital;
    private String continent;

    public CountriesData()
    {
        this.id = -1;
        this.name = null;
        this.capital = null;
        this.continent = null;
    }

    public CountriesData(long id, String name, String capital, String continent ) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.continent = continent;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCapital()
    {
        return capital;
    }

    public void setCapital(String capital)
    {
        this.capital = capital;
    }

    public String getContinent()
    {
        return continent;
    }

    public void setContinent(String continent)
    {
        this.continent = continent;
    }

    public String toString()
    {
        return id + ": " + name + " " + capital + " " + continent;
    }
}
