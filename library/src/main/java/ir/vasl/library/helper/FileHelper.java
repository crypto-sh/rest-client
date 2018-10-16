package ir.vasl.library.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class FileHelper {

    private static LogHelper logHelper = new LogHelper(FileHelper.class);

    public static String getAppFolderPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public String getMimeType(Context context, Uri url) {
        String type = null;
        try {
            if (url.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = context.getContentResolver();
                type = cr.getType(url);
            } else {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url.toString());
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
            }
        } catch (Exception e) {
            logHelper.e("getMimeType", e.getMessage());
        }
        return type;
    }

    public void RemoveFile(String filePath) {
        try {
            if (!general.StringIsEmptyOrNull(filePath)) {
                File fdelete = new File(filePath);
                if (fdelete.exists()) {
                    fdelete.delete();
                }
            }
        } catch (Exception e) {
            logHelper.e("File not Exist", e.getMessage());
        }
    }

    public static ArrayList<File> getImageFilePath() {
        ArrayList<File> arrayList = new ArrayList<>();
        try {
            arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
            arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                arrayList.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));
            }
        } catch (Exception e) {
            logHelper.e("getImageFilePath", e.getMessage());
        }
        return arrayList;
    }

    public static String getFileName(String filepath) {
        try {
            return filepath.substring(filepath.lastIndexOf("/") + 1);
        } catch (Exception e) {
            logHelper.e("getFileName", e.getMessage());
            return "";
        }
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            logHelper.e("getFileMD5", e.getMessage());
            return null;
        }
        BigInteger bigint = new BigInteger(1, digest.digest());
        return bigint.toString(16);
    }

    public String SaveImage(Bitmap finalBitmap) {
        try {
            if (finalBitmap != null) {
                String DIRECTORY_NAME_IMAGE = "/Image";
                File dir = new File(Environment.getExternalStorageDirectory() + DIRECTORY_NAME_IMAGE);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory() + DIRECTORY_NAME_IMAGE, System.currentTimeMillis() + "." + Bitmap.CompressFormat.PNG);
                if (file.exists()) file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    return file.getPath();
                } catch (Exception e) {
                    logHelper.e("SaveImage FileOutputStream", e.getMessage());
                    return "";
                }
            }
        } catch (Exception e) {
            logHelper.e("SaveImage", e.getMessage());
        }
        return "";
    }

    public void parseDir(File dir, ArrayList<File> files) {
        try {
            final File[] sortedByDate = dir.listFiles();
            if (sortedByDate != null && sortedByDate.length > 1) {
                Arrays.sort(sortedByDate, (object1, object2) -> Long.compare((object2).lastModified(), (object1).lastModified()));
            }
            if (sortedByDate != null) {
                for (File file : sortedByDate) {
                    if (file.isDirectory()) {
                        if (!file.getName().toLowerCase().startsWith(".")) {
                            parseDir(file, files);
                        }
                    } else {
                        String EXTENSION_JPG = ".jpg";
                        String EXTENSION_JPEG = ".jpeg";
                        String EXTENSION_PNG = ".png";
                        if (file.getName().toLowerCase().endsWith(EXTENSION_JPG) || file.getName().toLowerCase().endsWith(EXTENSION_JPEG) || file.getName().toLowerCase().endsWith(EXTENSION_PNG)) {
                            files.add(file);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logHelper.e(ex);
        }
    }

    public ArrayList<File> parseImageFromDirectory(File dir) {
        ArrayList<File> arrayList = new ArrayList<>();
        parseDir(dir, arrayList);
        return arrayList;
    }

//    public void pickImage(Activity activity, int updateMode) {
//        try {
//            if (new PermissionHelper(activity).checkAccessStoragePermission()) {
//                Intent intent;
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//                    intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    activity.startActivityForResult(intent, updateMode);
//                } else {
//                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    activity.startActivityForResult(intent, updateMode);
//                }
//            }
//        } catch (Exception e) {
//            logHelper.e("pickImage", e.getMessage());
//        }
//    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

}
