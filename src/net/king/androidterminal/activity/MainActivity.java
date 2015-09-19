package net.king.androidterminal.activity;

import net.king.androidterminal.R;
import net.king.androidterminal.util.CmdUtils;
import net.king.androidterminal.util.WifiUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
    
    private Button btnWifiToComputer;
    private TextView tvSuccessMsg;
    private TextView tvErrorMsg;
    
    private WifiUtils wifiUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }
    
    /** ��ʼ�� **/
    private void init() {
        btnWifiToComputer = (Button) findViewById(R.id.btn_wifi_connect_computer);
        tvSuccessMsg = (TextView) findViewById(R.id.tv_result_success);
        tvErrorMsg = (TextView) findViewById(R.id.tv_result_error);
        
        wifiUtils = WifiUtils.getInstance(this);
        
        btnWifiToComputer.setOnClickListener(this);
    }
    
    private void connectToComputerByWifi() {
        String[] command = CmdUtils.wifiConnectToComputer;
        CmdUtils.Result result = CmdUtils.execute(command);
        String successMsg = result.successMsg;
        String errorMsg = result.errorMsg;
        
        
        if (TextUtils.isEmpty(successMsg)) {
            tvSuccessMsg.setVisibility(View.GONE);
        } else {
            tvSuccessMsg.setText("ִ�н����\n" + successMsg);
        }
        
        if (TextUtils.isEmpty(errorMsg)) {
            tvErrorMsg.setVisibility(View.GONE);
        } else {
            tvErrorMsg.setText("������ʾ��\n" + errorMsg);
        }
        
        showAlertDialog();
    }
    
    private void showAlertDialog() {
        String ipAddress = wifiUtils.getIpAddress();
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                            .setTitle("�ɹ������˿�")
                            .setMessage("���ڵ����������������\n" + "adb connect " + ipAddress)
                            .setCancelable(false)
                            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
        alert.show();
    }

    @Override
    public void onClick(View v) { 
        switch (v.getId()) {
        case R.id.btn_wifi_connect_computer:
            connectToComputerByWifi();
            break;

        default:
            break;
        }
    }

}
