package com.example.imetu.imet.image;


import android.os.Environment;

import java.io.File;

public class FroyoAlbumDirFactory extends AlbumStorageDirFactory {
    @Override
    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }
}
