package com.project.stephencao.mymobileguardian.utils;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    /**
     *
     * @param inputStream
     * @return string, null means exception happens
     * @throws IOException
     */
    public static String streamToString(InputStream inputStream){
        StringBuffer stringBuffer = new StringBuffer();
        int len = 0;
        byte[] buffer = new byte[1024];
        try {
            while((len = inputStream.read(buffer))!=-1){
                stringBuffer.append(new String(buffer,0,len));
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                if (inputStream!=null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}
