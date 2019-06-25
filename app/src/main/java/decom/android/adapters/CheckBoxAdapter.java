package decom.android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import decom.android.R;
import decom.android.models.Contact;
import decom.android.models.CreateGroupItem;

/**
 * Created by Jerry on 1/21/2018.
 */

public class CheckBoxAdapter extends BaseAdapter {

    private ArrayList<Contact> contactArrayList;

    private Context ctx;

    public CheckBoxAdapter(Context ctx, ArrayList<Contact> contactArrayList) {
        this.ctx = ctx;
        this.contactArrayList = contactArrayList;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(contactArrayList!=null)
        {
            ret = contactArrayList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(contactArrayList!=null) {
            ret = contactArrayList.get(itemIndex);
        }
        return ret;
    }

    @Override
    public long getItemId(int itemIndex) {
        return itemIndex;
    }

    @Override
    public View getView(int itemIndex, View convertView, ViewGroup viewGroup) {

        CreateGroupItem viewHolder;

        convertView = View.inflate(ctx, R.layout.checkbox_item, null);

        CheckBox listItemCheckbox = convertView.findViewById(R.id.group_checkbox);

        TextView listItemText = convertView.findViewById(R.id.name_checkbox_item);

        viewHolder = new CreateGroupItem(convertView);

        viewHolder.setItemCheckbox(listItemCheckbox);

        viewHolder.setItemTextView(listItemText);

        convertView.setTag(viewHolder);


        Contact contact = contactArrayList.get(itemIndex);
        viewHolder.getItemCheckbox().setChecked(contact.isChecked());
        viewHolder.getItemTextView().setText(contact.getName());

        return convertView;
    }
}