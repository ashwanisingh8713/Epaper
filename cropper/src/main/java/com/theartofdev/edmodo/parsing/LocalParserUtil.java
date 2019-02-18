package com.theartofdev.edmodo.parsing;

import android.content.Context;

import com.github.chrisbanes.VertexData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class LocalParserUtil {

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static void parseVertexFile(final RequestCallback callback, final Context context, String fileName) {
        Observable.just(fileName)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<VertexData>>() {
                    @Override
                    public List<VertexData> apply(String fileName) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(loadJSONFromAsset(context, fileName),
                                new TypeToken<List<VertexData>>(){}.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VertexData>>() {
                    @Override
                    public void accept(List<VertexData> o) throws Exception {
                        if(callback != null) {
                            callback.onNext(o);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(callback != null) {
                            callback.onError(throwable, "parseVertexFile");
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if(callback != null) {
                            callback.onComplete("parseVertexFile");
                        }
                    }
                });
    }



    public static void parseVertexFile2(final RequestCallback callback, final Context context,
                                        String fileName, final int imgWidth, final int imgHeight) {
        Observable.just(fileName)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<VertexData>>() {
                    @Override
                    public List<VertexData> apply(String fileName) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(loadJSONFromAsset(context, fileName),
                                new TypeToken<List<VertexData>>(){}.getType());
                    }
                })
                .map(new Function<List<VertexData>, List<VertexData>>() {
                    @Override
                    public List<VertexData> apply(List<VertexData> vertexData) throws Exception {
                        int serverImgWidth = 1600;
                        int serverImgHeight = 2469;

                        List<VertexData> updatedVertex = new ArrayList<>();

                        for(VertexData data : vertexData) {
                            int x = data.getX();
                            int y = data.getY();
                            int width = data.getWidth();
                            int height = data.getHeight();

                            int newX = (x*imgWidth)/serverImgWidth;
                            int newY = (y*imgHeight)/serverImgHeight;

                            int newWidth = (width*imgWidth)/serverImgWidth;
                            int newHeight = (height*imgHeight)/serverImgHeight;

                            VertexData newData = new VertexData();
                            newData.setArticleID(data.getArticleID());
                            newData.setReference(data.getReference());
                            newData.setX(newX);
                            newData.setY(newY);
                            newData.setWidth(newWidth);
                            newData.setHeight(newHeight);

                            updatedVertex.add(newData);



                        }

                            return updatedVertex;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VertexData>>() {
                    @Override
                    public void accept(List<VertexData> o) throws Exception {
                        if(callback != null) {
                            callback.onNext(o);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(callback != null) {
                            callback.onError(throwable, "parseVertexFile");
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if(callback != null) {
                            callback.onComplete("parseVertexFile");
                        }
                    }
                });
    }
}
