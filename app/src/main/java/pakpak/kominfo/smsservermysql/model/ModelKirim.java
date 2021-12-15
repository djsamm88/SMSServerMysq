package pakpak.kominfo.smsservermysql.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 14/06/18.
 */

public class ModelKirim {
    public String id;
    public String nomor;
    public String pesan;
    public String email;
    public String status;
    public String waktu;
    public String generated_id;

    public ModelKirim()
    {

    }

    public ModelKirim(String id,String nomor,String pesan,String email,String status,String waktu,String generated_id)
    {
        this.id=id;
        this.nomor=nomor;
        this.pesan=pesan;
        this.email=email;
        this.status=status;
        this.waktu=waktu;
        this.generated_id=generated_id;

    }

    public void setGenerated_id(String generated_id)
    {
        this.generated_id=generated_id;
    }

    public String getGenerated_id()
    {
        return generated_id;
    }

    public void setWaktu(String waktu)
    {
        this.waktu=waktu;
    }

    public String getWaktu()
    {
        return waktu;
    }


    public void setId(String id)
    {
        this.id=id;
    }

    public String getId()
    {
        return id;
    }


    public void setStatus(String status)
    {
        this.status=status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setNomor(String nomor)
    {
        this.nomor=nomor;
    }

    public void setPesan(String pesan)
    {
        this.pesan=pesan;
    }

    public void setEmail(String email)
    {
        this.email=email;
    }

    public String getEmail()
    {
        return email;
    }

    public String getNomor()
    {
        return nomor;
    }

    public String getPesan()
    {
        return pesan;
    }

}
