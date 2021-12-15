package pakpak.kominfo.smsservermysql.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 31/03/18.
 */

public class ModelUser {
    // final String url = "http://ujicoba.pakpakbharatkab.go.id/lapak/index.php/android/login/index?email=+"+user.getEmail()+"&nama_member="+user.getDisplayName()+"&telepon="+user.getPhoneNumber()+"&photoURL="+user.getPhotoUrl()+"&firebase_uid="+user.getUid();
    private String email,nama_member,telepon,photoURL;
    private int id_member;

    public ModelUser()
    {

    }

    //id_member, nama_member, telepon, email, photoURL
    public ModelUser(int id_member, String nama_member, String telepon, String email, String photoURL)
    {
        this.id_member      = id_member;
        this.email          = email;
        this.nama_member    = nama_member;
        this.telepon        = telepon;
        this.photoURL       = photoURL;


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

}
