package com.mfe.mfewordcard.vocalwaregen.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit.*;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoader;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoaderException;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfigKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by minichen on 2017/5/3.
 */
public class MfeUtils {
    private static Logger log= LogManager.getLogger(MfeUtils.class);
    public static String word2AudioName(String word, String suffix){
        String lc=word.toLowerCase();
        lc=lc.replaceAll(" ", "_");
        lc=lc.replaceAll("!", "__");
        lc=lc.replaceAll("\\?", "___");
        lc=lc.replaceAll("\\W", ".");
        String sfx=(null==suffix ? ".mp3" : "."+suffix);
        return lc+sfx;
    }
    public static MfeUnit genUnit(String jsonFile){
        //String json=FileUtils.readFile2String(jsonFile, null);
        try {
            return (new ObjectMapper()).readValue(new File(jsonFile), MfeUnit.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean genVoicesFromSearchPathUnitJsonFile(String searchDir, String output_dir, boolean overwrite){
        List<String> uFiles=new LinkedList<String>();
        FileUtils.searchFiles(FileUtils.genAbsFilename(searchDir), "^u\\.json$", uFiles);
        for(String uf: uFiles){
            System.out.println("\n\nUNIT FILE: "+uf);
            boolean rlt=genVoicesFromUnitJsonFile(uf, output_dir, overwrite);
            if(!rlt){
                System.out.println("Generate failed, unit file:"+uf);
                return false;
            }
        }
        return true;
    }
    public static boolean genVoicesFromUnitJsonFile(String uFile, String output_dir, boolean overwrite){
        uFile=FileUtils.genAbsFilename(uFile);
        MfeUnit u=genUnit(uFile);
        if(null==u) return false;
        List<String> w_list=new LinkedList<String>();
        for(Vocabulary v: u.vocabulary){
            w_list.add(v.title);
        }
        for(Sentence s: u.sentence){
            w_list.add(s.title);
        }
        for(Page p: u.pages){
            for(Annotation a: p.annotations){
                w_list.add(a.words);
            }
        }
        try {
            for(String w: w_list){
                if(null==w || w.isEmpty()){
                    log.warn("Word is null or empty, skip.");
                    continue;
                }
                String fullname=FileUtils.genAbsFilename(output_dir+'/'+MfeUtils.word2AudioName(w, null));
                log.debug("fullname:"+fullname);
                if(!overwrite && (new File(fullname)).isFile()){
                    System.out.println("Already exits, skip: "+fullname);
                    continue;
                }else{
                    boolean rlt=VocalWareUtil.downloadVoiceStream(
                            ConfLoader.getInstance().getInt(ConfigKey.engine_id),
                            ConfLoader.getInstance().getInt(ConfigKey.language_id),
                            ConfLoader.getInstance().getInt(ConfigKey.voice_id),
                            w,
                            "mp3",
                            "",
                            "",
                            ConfLoader.getInstance().getConf(ConfigKey.account_id),
                            ConfLoader.getInstance().getConf(ConfigKey.api_id),
                            "",
                            "1",
                            ConfLoader.getInstance().getConf(ConfigKey.secret_phrase),
                            fullname
                    );
                    if(rlt){
                        System.out.println("Generated word: "+w);
                        System.out.println("Generated fullname: "+fullname);
                    }else{
                        System.out.println("Generated failed, word: "+w);
                        System.out.println("Generated failed fullname: "+fullname);
                        return false;
                    }
                }
            }
            return true;
        } catch (ConfLoaderException e) {
            e.printStackTrace();
            return false;
        }
    }
}
