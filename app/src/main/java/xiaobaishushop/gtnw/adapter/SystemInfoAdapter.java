package xiaobaishushop.gtnw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.bean.BeanKeyValue;

public class SystemInfoAdapter extends ArrayAdapter<BeanKeyValue>{
    private final int resourceId;
    public SystemInfoAdapter(@NonNull Context context, int resource, @NonNull List<BeanKeyValue> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BeanKeyValue beanKeyValue=getItem(position);//获取 kw示例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);//实例化一个对象
        TextView tv_key=view.findViewById(R.id.tv_key);
        TextView tv_value=view.findViewById(R.id.tv_value);
        tv_key.setText(beanKeyValue.getKey());
        tv_value.setText(beanKeyValue.getValue());
        return view;
    }
}
