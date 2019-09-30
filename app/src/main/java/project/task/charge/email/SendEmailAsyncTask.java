package project.task.charge.email;

import android.os.AsyncTask;
import android.os.Message;
import android.se.omapi.Session;
import android.util.Log;

import java.net.PasswordAuthentication;
import java.security.Security;
import java.util.Properties;

import javax.sql.DataSource;

import project.task.charge.BuildConfig;

class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... voids) {
        return null;
    }
//    Mail m = new Mail("from@gmail.com", "my password");
//
//    public SendEmailAsyncTask() {
//        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
//        String[] toArr = {"to mail@gmail.com"};
//        m.setTo(toArr);
//        m.setFrom("from mail@gmail.com");
//        m.setSubject("Email from Android");
//        m.setBody("body.");
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... params) {
//        if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
//        try {
//            m.send();
//            return true;
//        } catch (AuthenticationFailedException e) {
//            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
//            e.printStackTrace();
//            return false;
//        } catch (MessagingException e) {
//            Log.e(SendEmailAsyncTask.class.getName(), m.getTo(null) + "failed");
//            e.printStackTrace();
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}