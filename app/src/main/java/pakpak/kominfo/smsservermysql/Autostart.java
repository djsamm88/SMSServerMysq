package pakpak.kominfo.smsservermysql;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by user on 4/10/2017.
 */

public class Autostart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context, OutboxService.class);
        context.startActivity(intent);

    }
}
