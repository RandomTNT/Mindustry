package io.anuke.mindustry.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import io.anuke.arc.Core;
import io.anuke.arc.util.EmptyLogger;
import io.anuke.kryonet.KryoClient;
import io.anuke.kryonet.KryoServer;
import io.anuke.mindustry.net.Net;

public class ServerLauncher extends HeadlessApplication{

    public ServerLauncher(ApplicationListener listener, HeadlessApplicationConfiguration config){
        super(listener, config);

        //don't do anything at all for GDX logging: don't want controller info and such
        Gdx.app.setApplicationLogger(new EmptyLogger());
    }

    public static void main(String[] args){
        try{

            Net.setClientProvider(new KryoClient());
            Net.setServerProvider(new KryoServer());

            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            Core.settings.setPrefHandler((appName) -> Gdx.files.local("config"));

            new ServerLauncher(new MindustryServer(args), config);
        }catch(Throwable t){
            CrashHandler.handle(t);
        }

        //find and handle uncaught exceptions in libGDX thread
        for(Thread thread : Thread.getAllStackTraces().keySet()){
            if(thread.getName().equals("HeadlessApplication")){
                thread.setUncaughtExceptionHandler((t, throwable) -> {
                    try{
                        CrashHandler.handle(throwable);
                        System.exit(-1);
                    }catch(Throwable crashCrash){
                        crashCrash.printStackTrace();
                        System.exit(-1);
                    }
                });
                break;
            }
        }
    }
}