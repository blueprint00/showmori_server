package com.example.showmori2.Service;

import java.io.*;

public class PostSaveImg {

    private String savedFileName;
    private String path;

    public String getSavedFileName() {
        return savedFileName;
    }

    public String getPath() {
        return path;
    }

    public int saveImgFromUrl(String fileName) throws IOException {
//        URL url = null;
        InputStream in = null;
        OutputStream out = null;
        String path = "/Users/kay/00project/code/showmori_server-master/showmori_server/showmori_xml-master/src/main/resources/images/";

        // 폴더 없으면 만들기
        File Folder = new File(path);
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        try {
//            url = new URL(imgUrl);

//             헤더에서 파일 확장자 가져오기(content-type)
//            URLConnection urlConn = url.openConnection();
//            String conType = urlConn.getContentType();
            String conT[] = fileName.split(".");
            if (conT[1].equals("jpeg")) {
                conT[1] = "jpg";
            }

            // 파일네임 정하기
            if (fileName.equals("")) {
                fileName = "images";
            }

            // 해당 디렉토리에 해당 파일명을 가진 파일이 존재하는 확인 후 파일네임에 넘버링
            File file = new File(path + "/" + fileName);

//            in = urlConn.getInputStream();

            if (file.exists()) {
                int i = 1;
                while (true) {
                    File file2 = new File(path + "/"  + conT[0] + "_" + i + "." + conT[1]);
                    if (file2.exists()) {
                        i++;
                    } else {
                        fileName = conT[0] + "_" + i +"." + conT[1];
                        break;
                    }
                }
            }

            // 여기부터 쓰기시작
            out = new FileOutputStream(path + "/" + fileName);

            while (true) {
                int data = in.read();
                if (data == -1) {
                    break;
                }
                out.write(data);
            }

            in.close();
            out.close();

            this.savedFileName = fileName;// + ".jpg";
            this.path = path;
            return 1;

        } catch (Exception e) {

            e.printStackTrace();
            return -1;

        } finally {

            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }

        }

    }

}
