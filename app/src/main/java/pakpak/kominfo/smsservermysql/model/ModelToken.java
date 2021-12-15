package pakpak.kominfo.smsservermysql.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 17/04/18.
 */

public class ModelToken {
    int id;
    String token;

    public ModelToken()
    {

    }

    public ModelToken(int id, String token)
    {
        this.id = id;
        this.token = token;
    }

    public void setId(int id)
    {
        this.id=id;
    }

    public void setToken(String token)
    {
        this.token=token;
    }

    public int getId()
    {
        return id;
    }

    public String getToken()
    {
        return token;
    }

}
