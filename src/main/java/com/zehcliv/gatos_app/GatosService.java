/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zehcliv.gatos_app;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author JHEYSHON
 */
public class GatosService {
    public static void verGatos() throws IOException{
        //1. vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();//.newBuilder().build();
        Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/images/search")
        .method("GET", null) //get() 
        .build();
        Response response = client.newCall(request).execute();
        
        String elJson = response.body().string();
        
        //cortar los corchetes
        elJson = elJson.substring(1,elJson.length());
        elJson = elJson.substring(0,elJson.length()-1);
        
        //crear en objeto de la clase Json
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);
        
        //redimencionar en caso de necesitar
        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);
            
            ImageIcon fondoGato = new ImageIcon(image);
            
            if(fondoGato.getIconWidth()>800){
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(600, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            
            String menu = "Opciones: \n"
                    + " 1. Ver otra imagen \n"
                    + " 2. Favorito \n"
                    + " 3. Volver \n";
            
            String[] botones = {"ver otra imagen","favorito","volver"};
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, 
                    JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);
            int seleccion = -1;
            //validamos que opcion selecciona el usuario
            for(int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    seleccion=i;
                }
            }
            
            switch(seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }
            
        }catch(IOException e){
            System.out.println(e); 
        }
    }
    
    public static void favoritoGato(Gatos gato){
        try{
            OkHttpClient client = new OkHttpClient();//.newBuilder().build();
              MediaType mediaType = MediaType.parse("application/json");
              RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\":\""+gato.getId()+"\"\r\n}");
              Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", gato.getApiKe())
                .build();
              Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);
        }
    }
    
    public static void verFavorito(String apiKey) throws IOException{
        OkHttpClient client = new OkHttpClient();//.newBuilder().build();
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .method("GET", null)
          .addHeader("x-api-key", "4a39b79b-a03d-4639-84bf-6b3376611d7a")
          .build();
        Response response = client.newCall(request).execute();
        
        //guardamos el string con la respuesta
        String elJson = response.body().string();
        
        //creamos el objeto gson
        Gson gson = new Gson();
        GatosFav[] gatosArray = gson.fromJson(elJson,GatosFav[].class);
        
        if(gatosArray.length>0){
            int min =1;
            int max =gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) +min;
            int indice = aleatorio - 1;
            
            GatosFav gatoFav = gatosArray[indice];
            
            //redimencionar en caso de necesitar
            Image image = null;
            try{
                URL url = new URL(gatoFav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if(fondoGato.getIconWidth()>800){
                    //redimensionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(600, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }

                String menu = "Opciones: \n"
                        + " 1. Ver otra imagen \n"
                        + " 2. Eliminar Favorito \n"
                        + " 3. Volver \n";

                String[] botones = {"ver otra imagen","eliminar favorito","volver"};
                String id_gato = gatoFav.getId();
                String opcion = (String) JOptionPane.showInputDialog(null, menu, id_gato, 
                        JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);
                int seleccion = -1;
                //validamos que opcion selecciona el usuario
                for(int i=0;i<botones.length;i++){
                    if(opcion.equals(botones[i])){
                        seleccion=i;
                    }
                }

                switch(seleccion){
                    case 0:
                        verFavorito(apiKey);
                        break;
                    case 1:
                        borrarFavorito(gatoFav);
                        break;
                    default:
                        break;
                }

            }catch(IOException e){
                System.out.println(e); 
            }
        }
    }
    
    public static void borrarFavorito(GatosFav gatoFav){
        try{
            OkHttpClient client = new OkHttpClient();//.newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/"+gatoFav.getId())
              .method("DELETE", body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatoFav.getApiKey())
              .build();
            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
