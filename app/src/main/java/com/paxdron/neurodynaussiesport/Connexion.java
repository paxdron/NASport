package com.paxdron.neurodynaussiesport;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Antonio on 28/03/2016.
 */
public class Connexion{

    private class Paquete{
        int Lenght;
        byte[] Buffer;

        public Paquete(){
            Lenght = 0;
            Buffer = new byte[Connexion.Max_Length_Buff];
        }
    }

    private class Frame{
        Boolean Escape;
        Boolean TrueFrame;
        int Lenght;
        byte[] Data;

        public Frame(){
            Escape = false;
            TrueFrame = false;
            Lenght = 0;
            Data = new byte[Connexion.Max_Length_Buff];
        }
    }

    /*Protocolo*/
    public static final byte Inicio_Frame = 1;
    public static final byte Final_Frame = 4;
    public static final byte Link_Data = 10;
    public static final int Max_Length_Buff = 512000;
    private static SSLSocket c;
    private static Frame frame1;
    DataOutputStream out;

    /*Comandos*/
    public static final byte Init_Write_Mem = 0x0A;
    public static final byte Cont_Write_Mem = 0x0B;
    public static final byte Finish_Write_Mem = 0x0C;
    public static final byte Erase_Proyecto = 0x0D;
    public static final byte Init_User = 0x0E;
    public static final byte Command_OK = 0x0F;
    public static final byte Command_Nok = 0x10;

    public boolean Crear_Conn(int Port, String Servidor, Context context){
        frame1 = new Frame();
        try {
            KeyStore trustStore = KeyStore.getInstance("BKS");
            InputStream trustStoreStream = context.getResources().openRawResource(R.raw.kinnov_key);
            trustStore.load(trustStoreStream, "Kinnov2016".toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            SSLSocketFactory factory = sslContext.getSocketFactory();
            c = (SSLSocket) factory.createSocket();
            c.connect(new InetSocketAddress(Servidor, Port), 9000);
            c.startHandshake();
            new Thread(new Escuchar()).start();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  boolean Cerrar_Conn(){
        if(c.isConnected()){
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public  boolean Enviar_Conn(byte[] Buff, int len, byte Comando){
        if(c.isConnected()){
            Paquete paquete1 = Empaquetar_Frame(Buff, len, Comando);
            try {
                out = new DataOutputStream(c.getOutputStream());
                out.write(paquete1.Buffer,0,paquete1.Lenght);
            }catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    private Paquete Empaquetar_Frame(byte[] Buff, int len, byte Comando){
        byte[] Nuevo_Frame = new byte[Max_Length_Buff];
        int BuffLen, Temp;
        Paquete paquete1 = new Paquete();

        BuffLen = 0;

        if (len > Max_Length_Buff) {
            paquete1.Buffer = Nuevo_Frame;
            paquete1.Lenght = BuffLen;
            return paquete1;
        }

        Nuevo_Frame[BuffLen++] = Inicio_Frame;
        if((Comando == Inicio_Frame) || (Comando == Final_Frame) || (Comando == Link_Data)){
            Nuevo_Frame[BuffLen++] = Link_Data;
        }
        Nuevo_Frame[BuffLen++] = Comando;

        for(Temp=0;Temp<len;Temp++){
            if((Buff[Temp] == Inicio_Frame) || (Buff[Temp] == Final_Frame) || (Buff[Temp] == Link_Data)){
                Nuevo_Frame[BuffLen++] = Link_Data;
            }
            Nuevo_Frame[BuffLen++] = Buff[Temp];
        }
        Nuevo_Frame[BuffLen++] = Final_Frame;

        paquete1.Buffer = Nuevo_Frame;
        paquete1.Lenght = BuffLen;

        return paquete1;
    }

    class Escuchar implements Runnable {
        @Override
        public void run() {
            byte Temp;
            try {
                InputStream inServer = c.getInputStream();
                DataInputStream in = new DataInputStream(inServer);
                while (c.isConnected()){
                    Temp = in.readByte();
                    if (Temp > -1){
                        Desempaquetar_Frame(Temp,frame1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Desempaquetar_Frame(byte Data, Frame frameT){

        if(frameT.Lenght > Max_Length_Buff - 1){
            frameT.Lenght = 0;
        }
        switch (Data) {
            case Inicio_Frame:
                if(frameT.Escape){
                    frameT.Data[frameT.Lenght++] = Data;
                    frameT.Escape = false;
                }else{
                    frameT.Lenght = 0;
                }
                break;

            case Final_Frame:
                if(frameT.Escape){
                    frameT.Data[frameT.Lenght++] = Data;
                    frameT.Escape = false;
                }else{
                    frameT.TrueFrame = true;
                }
                break;

            case Link_Data:
                if(frameT.Escape){
                    frameT.Data[frameT.Lenght++] = Data;
                    frameT.Escape = false;
                }else{
                    frameT.Escape = true;
                }
                break;

            default:
                frameT.Data[frameT.Lenght++] = Data;
                frameT.Escape = false;
                break;
        }
    }

    public Boolean Data_Disponible(){
        return frame1.TrueFrame;
    }

    public byte[] Leer_Data(){
        if (frame1.TrueFrame){
            frame1.TrueFrame = false;
            return frame1.Data;
        }
        return null;
    }

    public int Leer_TamañoData(){
        return frame1.Lenght;
    }
}