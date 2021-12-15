package pakpak.kominfo.smsservermysql;

/**
 * Created by dinaskominfokab.pakpakbharat on 20/03/18.
 */


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import pakpak.kominfo.smsservermysql.database.DbToken;
import pakpak.kominfo.smsservermysql.model.ModelToken;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/03/18.
 */

public class DapatToken extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {

        //For registration of token

        /****************disni token notif**********/
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            /****** to database ********/


                DbToken dbToken = new DbToken(this);
                int id =1;
                try{
                    dbToken.insert(new ModelToken(id,refreshedToken));
                }catch (Exception e)
                {
                    dbToken.update(new ModelToken(id,refreshedToken));
                }

            /****** to database ********/



    }
}
