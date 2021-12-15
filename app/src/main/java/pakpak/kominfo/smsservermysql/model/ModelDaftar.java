package pakpak.kominfo.smsservermysql.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 31/03/18.
 */

public class ModelDaftar {

    private String email,nama_member,telepon,photoURL,status;
    private int id_member;

    public ModelDaftar()
    {

    }

    //id_member, nama_member, telepon, email, photoURL
    public ModelDaftar(int id_member, String nama_member, String telepon, String email, String photoURL,String status)
    {
        this.id_member      = id_member;
        this.email          = email;
        this.nama_member    = nama_member;
        this.telepon        = telepon;
        this.photoURL       = photoURL;
        this.status         = status;

    }

    public void setId_member(int id_member)
    {
        this.id_member  = id_member;
    }

    public int getId_member()
    {
        return id_member;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setNama_member(String nama_member)
    {
        this.nama_member = nama_member;
    }

    public String getNama_member()
    {
        return nama_member;
    }

    public void setTelepon(String telepon)
    {
        this.telepon = telepon;
    }

    public String getTelepon()
    {
        return telepon;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    public String getPhotoURL()
    {
        return photoURL;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status=status;
    }

}
