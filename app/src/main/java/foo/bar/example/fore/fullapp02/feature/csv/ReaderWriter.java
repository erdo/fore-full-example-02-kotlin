package foo.bar.example.fore.fullapp02.feature.csv;


import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import co.early.fore.core.Affirm;
import co.early.fore.core.WorkMode;
import co.early.fore.core.callbacks.FailureCallback;
import co.early.fore.core.callbacks.SuccessCallback;
import co.early.fore.core.callbacks.SuccessCallbackWithPayload;
import co.early.fore.core.threading.AsyncBuilder;

/**
 * Copyright © 2017-2018 early.co. All rights reserved.
 */
public class ReaderWriter {

    private final WorkMode workMode;
    private final File dataDirectory;

    //for use on android, inject dataDirectory = Environment.getDataDirectory()
    public ReaderWriter(WorkMode workMode, File dataDirectory) {
        this.workMode = Affirm.notNull(workMode);
        this.dataDirectory = Affirm.notNull(dataDirectory);
    }

    public void writeToFileAsync(final String fileName, final String data, final SuccessCallback successCallBack, final FailureCallback failureCallback) {
        writeToFileAsync(fileName, data, Charset.forName("UTF-8"), successCallBack, failureCallback);
    }

    public void writeToFileAsync(final String fileName, final String data, final Charset charset, final SuccessCallback successCallback, final FailureCallback failureCallback) {

        Affirm.notNull(successCallback);
        Affirm.notNull(failureCallback);

        new AsyncBuilder<Void, Boolean>(workMode)
                .doInBackground(voids -> writeToFile(fileName, data, charset))
                .onPostExecute(success -> {
                    if (success) {
                        successCallback.success();
                    } else {
                        failureCallback.fail();
                    }
                })
                .execute((Void[])null);
    }

    public boolean writeToFile(final String fileName, final String data) {
        return writeToFile(fileName, data, Charset.forName("UTF-8"));
    }

    /**
     *
     * - try with resources is spread over three lines to ensure everything gets closed in case of problems
     * - OutputStreamWriter lets us specify the character encoding unlike FileWriter
     * - Buffered should be faster for large amounts of data
     *
     */
    public boolean writeToFile(final String fileName, final String data, final Charset charset){

        Affirm.notNull(fileName);
        Affirm.notNull(data);
        Affirm.notNull(charset);

        File file = getFile(fileName);

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
             BufferedWriter br = new BufferedWriter(osw)){

            if (!file.exists()) {
                file.createNewFile();
            }

            br.write(data);
            br.flush();
            br.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void readFromFileAsync(final String fileName, final SuccessCallbackWithPayload<String> successCallbackWithPayload, final FailureCallback failureCallback) {
        readFromFileAsync(fileName, Charset.forName("UTF-8"), successCallbackWithPayload, failureCallback);
    }

    public void readFromFileAsync(final String fileName, final Charset charset, final SuccessCallbackWithPayload<String> successCallbackWithPayload, final FailureCallback failureCallback) {

        Affirm.notNull(successCallbackWithPayload);
        Affirm.notNull(failureCallback);

        new AsyncBuilder<Void, String>(workMode)
                .doInBackground(voids -> readFromFile(fileName, charset))
                .onPostExecute(result -> {
                    if (result != null) {
                        successCallbackWithPayload.success(result);
                    } else {
                        failureCallback.fail();
                    }
                })
                .execute((Void[])null);
    }


    public String readFromFile(final String fileName) {
        return readFromFile(fileName, Charset.forName("UTF-8"));
    }

    /**
     *
     * - try with resources is spread over three lines to ensure everything gets closed in case of problems
     * - InputStreamReader lets us specify the character encoding unlike FileReader
     * - Buffered should be faster for large amounts of data
     *
     * @return null if the file does not exist
     *
     */
    @Nullable
    public String readFromFile(final String fileName, final Charset charset){

        Affirm.notNull(fileName);
        Affirm.notNull(charset);

        File file = getFile(fileName);

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isw = new InputStreamReader(fis, charset);
             BufferedReader br = new BufferedReader(isw)){

            if (!file.exists()) {
                return null;
            }

            String line;
            StringBuffer stringBuffer = new StringBuffer();

            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append(CSVBuilder.LINE_TERMINATOR);
            }

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean deleteFile(String fileNameToDelete) {

        File file = getFile(fileNameToDelete);

        if (file.exists()) {
            file.delete();
            return true;
        }

        return false;
    }

    private File getFile(String filename){
        return new java.io.File(dataDirectory, filename);
    }
}
