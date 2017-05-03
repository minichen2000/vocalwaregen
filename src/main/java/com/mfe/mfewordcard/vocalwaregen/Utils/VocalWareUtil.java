package com.mfe.mfewordcard.vocalwaregen.Utils;

import com.mfe.mfewordcard.vocalwaregen.JettyHttpClientService.HttpCall;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoader;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoaderException;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfigKey;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chenmin on 5/3/2017.
 */
public class VocalWareUtil {
    static public String genUrl(int EID, int LID, int VID, String TXT, String EXT, String FX_TYPE, String FX_LEVEL, String ACC, String API, String SESSION, String HTTP_ERR, String SECRET) throws ConfLoaderException {
        String url= ConfLoader.getInstance().getConf(ConfigKey.vocalware_url);
        return HttpCall.instance().getCall(url, null, genQueryParams(EID, LID, VID, TXT, EXT, FX_TYPE, FX_LEVEL, ACC, API, SESSION, HTTP_ERR, SECRET), true);
    }

    static public boolean downloadVoiceStream(int EID, int LID, int VID, String TXT, String EXT, String FX_TYPE, String FX_LEVEL, String ACC, String API, String SESSION, String HTTP_ERR, String SECRET, String dest_file) throws ConfLoaderException {
        String url= ConfLoader.getInstance().getConf(ConfigKey.vocalware_url);
        return HttpCall.instance().getAsFile(url, null, genQueryParams(EID, LID, VID, TXT, EXT, FX_TYPE, FX_LEVEL, ACC, API, SESSION, HTTP_ERR, SECRET),
                dest_file);
    }
    static private List<KeyValuePair> genQueryParams(int EID, int LID, int VID, String TXT, String EXT, String FX_TYPE, String FX_LEVEL, String ACC, String API, String SESSION, String HTTP_ERR, String SECRET){
        List<KeyValuePair> params=new LinkedList<KeyValuePair>();
        params.add(new KeyValuePair("EID", ""+EID));
        params.add(new KeyValuePair("LID", ""+LID));
        params.add(new KeyValuePair("VID", ""+VID));
        params.add(new KeyValuePair("TXT", null==TXT ? "" : TXT));
        params.add(new KeyValuePair("EXT", null==EXT ? "" : EXT));
        params.add(new KeyValuePair("FX_TYPE", null==FX_TYPE ? "" : FX_TYPE));
        params.add(new KeyValuePair("FX_LEVEL", null==FX_LEVEL ? "" : FX_LEVEL));
        params.add(new KeyValuePair("ACC", null==ACC ? "" : ACC));
        params.add(new KeyValuePair("API", null==API ? "" : API));
        params.add(new KeyValuePair("SESSION", null==SESSION ? "" : SESSION));
        params.add(new KeyValuePair("HTTP_ERR", null==HTTP_ERR ? "" : HTTP_ERR));
        String cs=genCS(EID, LID, VID, TXT, EXT, FX_TYPE, FX_LEVEL, ACC, API, SESSION, HTTP_ERR, SECRET);
        params.add(new KeyValuePair("CS", null==cs ? "" : cs));
        return params;
    }
    static public String genCS(int EID, int LID, int VID, String TXT, String EXT, String FX_TYPE, String FX_LEVEL, String ACC, String API, String SESSION, String HTTP_ERR, String SECRET){
        String s="";
        s+=EID;
        s+=LID;
        s+=VID;
        s+=(null==TXT ? "" : TXT);
        s+=(null==EXT ? "" : EXT);
        s+=(null==FX_TYPE ? "" : FX_TYPE);
        s+=(null==FX_LEVEL ? "" : FX_LEVEL);
        s+=(null==ACC ? "" : ACC);
        s+=(null==API ? "" : API);
        s+=(null==SESSION ? "" : SESSION);
        s+=(null==HTTP_ERR ? "" : HTTP_ERR);
        s+=(null==SECRET ? "" : SECRET);
        return MD5.md5(s);
    }
}
