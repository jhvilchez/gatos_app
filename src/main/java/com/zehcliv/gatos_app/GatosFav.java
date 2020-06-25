/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zehcliv.gatos_app;

import java.awt.Image;

/**
 *
 * @author JHEYSHON
 */
public class GatosFav {
    String id;
    String image_id;
    String apiKey="4a39b79b-a03d-4639-84bf-6b3376611d7a";
    ImageX image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ImageX getImage() {
        return image;
    }

    public void setImage(ImageX image) {
        this.image = image;
    }
    
}
