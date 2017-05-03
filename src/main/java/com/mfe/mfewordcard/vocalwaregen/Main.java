package com.mfe.mfewordcard.vocalwaregen;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfe.mfewordcard.vocalwaregen.Utils.FileUtils;
import com.mfe.mfewordcard.vocalwaregen.Utils.MfeUnit.MfeUnit;
import com.mfe.mfewordcard.vocalwaregen.Utils.MfeUtils;
import com.mfe.mfewordcard.vocalwaregen.Utils.VocalWareUtil;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoader;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoaderException;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfigKey;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenmin on 2017/3/30.
 */
public class Main {
    private static Logger log;
    public static void main(String[] args) throws Exception{
        /*System.out.println(MfeUtils.word2AudioName("I have a pencil.", null));
        System.out.println(MfeUtils.word2AudioName("Stand up, Joy. Show me your pencil. Sit down, please.", null));
        System.out.println(MfeUtils.word2AudioName("Open your eyes, what's missing?", null));
        System.out.println(MfeUtils.word2AudioName("Hi! I'm Angel.", null));
        System.out.println(MfeUtils.word2AudioName("book", null));
        MfeUnit u=MfeUtils.genUnit("/Users/minichen/gitrepo/bitbucket/mfewordcard2_textbook_scan/textbook/pep-1/g-1-1/u-1/u.json");
        System.out.println((new ObjectMapper()).writeValueAsString(u));
        System.exit(0);*/
        loadConf(args);
        confLog();
        log = LogManager.getLogger(Main.class);
        configProxy();
        if(!ConfLoader.getInstance().getConf(ConfigKey.search_dir).isEmpty() &&
                !ConfLoader.getInstance().getConf(ConfigKey.output_dir).isEmpty()){
            if(MfeUtils.genVoicesFromSearchPathUnitJsonFile(ConfLoader.getInstance().getConf(ConfigKey.search_dir),
                    ConfLoader.getInstance().getConf(ConfigKey.output_dir), false)){
                System.out.println("\n=====Done=====\n");
            }

        }else if(!ConfLoader.getInstance().getConf(ConfigKey.text).isEmpty()){
            if(ConfLoader.getInstance().getBoolean(ConfigKey.url_only)){
                String url= VocalWareUtil.genUrl(
                        ConfLoader.getInstance().getInt(ConfigKey.engine_id),
                        ConfLoader.getInstance().getInt(ConfigKey.language_id),
                        ConfLoader.getInstance().getInt(ConfigKey.voice_id),
                        ConfLoader.getInstance().getConf(ConfigKey.text),
                        ConfLoader.getInstance().getConf(ConfigKey.ext),
                        "",
                        "",
                        ConfLoader.getInstance().getConf(ConfigKey.account_id),
                        ConfLoader.getInstance().getConf(ConfigKey.api_id),
                        "",
                        "1",
                        ConfLoader.getInstance().getConf(ConfigKey.secret_phrase)
                );
                System.out.println("url:\n"+url);
                System.exit(0);
            }else{
                String fn=ConfLoader.getInstance().getConf(ConfigKey.file_name);
                fn= FileUtils.genAbsFilename(fn);
                boolean rlt=VocalWareUtil.downloadVoiceStream(
                        ConfLoader.getInstance().getInt(ConfigKey.engine_id),
                        ConfLoader.getInstance().getInt(ConfigKey.language_id),
                        ConfLoader.getInstance().getInt(ConfigKey.voice_id),
                        ConfLoader.getInstance().getConf(ConfigKey.text),
                        ConfLoader.getInstance().getConf(ConfigKey.ext),
                        "",
                        "",
                        ConfLoader.getInstance().getConf(ConfigKey.account_id),
                        ConfLoader.getInstance().getConf(ConfigKey.api_id),
                        "",
                        "1",
                        ConfLoader.getInstance().getConf(ConfigKey.secret_phrase),
                        fn
                );
                if(rlt){
                    System.out.println("Saved to "+fn);
                    System.out.println("\n=====Done=====\n");
                }else{
                    System.out.println("Generate voice failed.");
                }
            }
        }
        System.exit(0);
    }

    private static void confLog() throws Exception{
        String log_conf_file= ConfLoader.getInstance().getConf(ConfigKey.log_conf_file, null);
        if((new File(log_conf_file).canRead())){
            System.setProperty("log4j.configurationFile", log_conf_file);
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            loggerContext.reconfigure();
        }else{
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            Level ll=Level.ERROR;
            switch(ConfLoader.getInstance().getInt(ConfigKey.log_level, 0)){
                case 1:
                    ll=Level.ERROR;
                    break;
                case 2:
                    ll=Level.WARN;
                    break;
                case 3:
                    ll=Level.INFO;
                    break;
                case 4:
                    ll=Level.DEBUG;
                    break;
            }
            loggerContext.getConfiguration().getRootLogger().setLevel(ll);
        }
    }

    private static void loadConf(String[] args) {
        parseCommandLine(args);
        if(!ConfLoader.getInstance().getConf(ConfigKey.conf_file, "").isEmpty()){
            try {
                ConfLoader.getInstance().loadConf(ConfLoader.getInstance().getConf(ConfigKey.conf_file, ""));
            } catch (ConfLoaderException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseCommandLine(String[] _args){

        class Args{
            @Parameter(names={"-"+ConfigKey.conf_file}, order = 1, description = "Configuation file")
            private String conf_file="";

            @Parameter(names={"-"+ConfigKey.log_conf_file}, order = 2, description ="Log4j2 configuration file")
            private String log_conf_file="";

            @Parameter(names={"-"+ConfigKey.proxy_host}, order = 3, description="Http proxy host")
            private String proxy_host="";

            @Parameter(names={"-"+ConfigKey.proxy_port}, order = 4, description = "Http proxy port")
            private int proxy_port=0;

            @Parameter(names={"-"+ConfigKey.engine_id}, order = 5, description = "Engine id.")
            private int engine_id=3;

            @Parameter(names={"-"+ConfigKey.language_id}, order = 6, description = "Language id.")
            private int language_id=1;

            @Parameter(names={"-"+ConfigKey.voice_id}, order = 7, description = "Voice id.")
            private int voice_id=3;

            @Parameter(names={"-"+ConfigKey.ext}, order = 8, description = "Ext: (mp3, swf")
            private String ext="mp3";

            @Parameter(names={"-"+ConfigKey.account_id}, order = 8, description = "Account id.")
            private String account_id="6329825";

            @Parameter(names={"-"+ConfigKey.api_id}, order = 9, description = "Api id.")
            private String api_id="2555026";

            @Parameter(names={"-"+ConfigKey.secret_phrase}, order = 10, description = "Secret phrase.")
            private String secret_phrase="";

            @Parameter(names={"-"+ConfigKey.url_only}, order = 11, description = "Generate url only.")
            private boolean url_only=false;

            @Parameter(names={"-"+ConfigKey.vocalware_url}, order = 12, description = "Vocalware url.")
            private String vocalware_url="http://www.vocalware.com/tts/gen.php";

            @Parameter(names={"-"+ConfigKey.file_name}, order = 13, description = "Saved file name")
            private String file_name="gen.mp3";

            @Parameter(names={"-"+ConfigKey.output_dir}, order = 14, description = "Output directory")
            private String output_dir="";

            @Parameter(names={"-"+ConfigKey.search_dir}, order = 15, description = "u.json file searching directory")
            private String search_dir="";

            @Parameter(names={"-"+ConfigKey.log_level}, order = 99, description = "1: error, 2: warn, 3: info, 4: debug")
            private int log_level=1;

            @Parameter(names={"-help"}, order = 100, help=true, description = "Show this help")
            private boolean help=false;

            @Parameter(description = "Text to generate voice.")
            private List<String> text = new ArrayList<>();
        }
        Args args=new Args();
        JCommander jc=new JCommander(args, _args);
        if(args.help){
            jc.usage();
            System.exit(0);
        }

        ConfLoader.getInstance().setConf(ConfigKey.conf_file, args.conf_file);
        ConfLoader.getInstance().setConf(ConfigKey.log_conf_file, args.log_conf_file);
        ConfLoader.getInstance().setConf(ConfigKey.proxy_host, args.proxy_host);
        ConfLoader.getInstance().setInt(ConfigKey.proxy_port, args.proxy_port);
        ConfLoader.getInstance().setInt(ConfigKey.log_level, args.log_level);

        ConfLoader.getInstance().setInt(ConfigKey.engine_id, args.engine_id);
        ConfLoader.getInstance().setInt(ConfigKey.language_id, args.language_id);
        ConfLoader.getInstance().setInt(ConfigKey.voice_id, args.voice_id);
        ConfLoader.getInstance().setConf(ConfigKey.account_id, args.account_id);
        ConfLoader.getInstance().setConf(ConfigKey.ext, args.ext);
        ConfLoader.getInstance().setConf(ConfigKey.api_id, args.api_id);
        ConfLoader.getInstance().setConf(ConfigKey.secret_phrase, args.secret_phrase);
        ConfLoader.getInstance().setConf(ConfigKey.vocalware_url, args.vocalware_url);
        ConfLoader.getInstance().setConf(ConfigKey.file_name, args.file_name);
        ConfLoader.getInstance().setConf(ConfigKey.search_dir, args.search_dir);
        ConfLoader.getInstance().setConf(ConfigKey.output_dir, args.output_dir);
        ConfLoader.getInstance().setBoolean(ConfigKey.url_only, args.url_only);
        ConfLoader.getInstance().setConf(ConfigKey.text, args.text.size()>0 ? args.text.get(0) : "");

    }

    private static void configProxy() throws Exception{
        String host = ConfLoader.getInstance().getConf(ConfigKey.proxy_host);
        int port = ConfLoader.getInstance().getInt(ConfigKey.proxy_port);
        if(null!=host && !host.isEmpty() && 0!=port){
            log.info("http proxy enabled: "+host+':'+port);
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", ""+port);
        }
    }
}
