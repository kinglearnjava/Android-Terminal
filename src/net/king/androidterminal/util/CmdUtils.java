package net.king.androidterminal.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CMD ������ִ�й���
 */
public class CmdUtils {
    public static final String COMMAND_SU = "su"; // ��ʾ��ȡrootȨ�ޣ�APP������root��
    public static final String COMMAND_LINE_END = "\n"; 
    public static final String COMMAND_EXIT = "exit\n";
    
    /**
     * Android�ֻ���Wifi���ϵ���ADB����
     * �����ֻ��ն�������������
     * ���ն˱����Ѿ�Root
     */
    public static final String[] wifiConnectToComputer = {
        "setprop service.adb.tcp.port 5555",
        "stop adbd",
        "start adbd"
    };
    
    public static Result execute(String[] commands) {
        //----------------- ��д�������ֻ��Ƿ��Ѿ�Root-------------
        
        Runtime runtime = Runtime.getRuntime();
        
        Process process = null;
        DataOutputStream output = null; // �������ն˽�����������
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        
        try {
            process = runtime.exec(COMMAND_SU);
            output = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                
                output.write(command.getBytes());
                output.writeBytes(COMMAND_LINE_END); // ����һ������Ҫ���س�
                output.flush();
            }
            
            output.writeBytes(COMMAND_EXIT);
            output.flush();
            process.waitFor(); // ��ǰ�̵߳ȴ���ֱ��process�߳�ִ�н���
            
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String s;
            while ( (s = successResult.readLine()) != null) {
                successMsg.append(s).append("\n");
            }
            while ( (s = errorResult.readLine()) != null) {
                errorMsg.append(s).append("\n");
            }
            
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally { // ������Դ
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (successResult != null) {
                        successResult.close();
                    }
                    if (errorResult != null) {
                        errorResult.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                if (process != null) {
                    process.destroy();
                }
        }
        return new Result(successMsg == null ? null : successMsg.toString()
                , errorMsg == null ? null : errorMsg.toString());
    }
    
    public static class Result {
        public String successMsg; 
        public String errorMsg; 
        
        public Result(String successMsg, String errorMsg) {
            super();
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
        
    }
}
