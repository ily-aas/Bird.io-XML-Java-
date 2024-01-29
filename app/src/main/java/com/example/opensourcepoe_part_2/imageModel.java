package com.example.opensourcepoe_part_2;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

    public class imageModel {


        private String currentUsr;
        private String capImageData;

        // Default constructor required for Firebase
        public imageModel() {
        }

        public imageModel(String user, Bitmap image) {
            this.currentUsr = user;
            this.capImageData = convertBitmapToBase64(image);
        }

        private String convertBitmapToBase64(Bitmap image) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        public String getCurrentUsr() {
            return currentUsr;
        }

        public void setCurrentUsr(String currentUsr) {
            this.currentUsr = currentUsr;
        }

        public String getCapImageData() {
            return capImageData;
        }

        public void setCapImageData(String capImageData) {
            this.capImageData = capImageData;
        }
    }


