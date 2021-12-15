package pakpak.kominfo.smsservermysql.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 11/08/18.
 */

public class ModelConfig {
    private String id,url_inbox,url_outbox;

    public ModelConfig()
    {

    }

    public ModelConfig(String id,String url_inbox,String url_outbox)
    {
        this.id = id;
        this.url_inbox = url_inbox;
        this.url_outbox = url_outbox;

    }




    public String getId()
    {
        return this.id;
    }


    public String getUrl_inbox()
    {
        return this.url_inbox;
    }

    public String getUrl_outbox()
    {
        return this.url_outbox;
    }



    public void setId(String id)
    {
        this.id = id;
    }

    public void setUrl_inbox(String url_inbox)
    {
        this.url_inbox = url_inbox;
    }

    public void setUrl_outbox(String url_outbox)
    {
        this.url_outbox = url_outbox;
    }


}
