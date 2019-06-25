package decom.android.db.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import decom.android.db.AppDatabase;
import decom.android.db.dao.ContactDao;
import decom.android.models.Contact;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> contacts;

    public ContactRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        contactDao = db.contactDao();
        contacts = contactDao.getContacts();
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    public void insert(Contact contact){
        new insertAsyncTask(contactDao).execute(contact);
    }

    public void delete(Contact... contact) {
        new deleteAsyncTask(contactDao).execute(contact);
    }

    public void emptyTable() {
        new emptyTableAsyncTask(contactDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao asyncContactDao;

        private deleteAsyncTask(ContactDao contactDao){
            this.asyncContactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            asyncContactDao.deleteAll(contacts);
            return null;
        }
    }

    private static class emptyTableAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao asyncContactDao;

        private emptyTableAsyncTask(ContactDao contactDao){
            this.asyncContactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            asyncContactDao.emptyTable();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao asyncContactDao;

        private insertAsyncTask(ContactDao contactDao){
            this.asyncContactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            asyncContactDao.insert(contacts[0]);
            return null;
        }
    }
}

