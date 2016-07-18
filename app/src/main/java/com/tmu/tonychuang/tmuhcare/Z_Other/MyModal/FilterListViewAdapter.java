package com.tmu.tonychuang.tmuhcare.Z_Other.MyModal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tmu.tonychuang.tmuhcare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TonyChuang on 2016/7/1.
 */
public class FilterListViewAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    /**
     * Contains the list of objects that represent the data of this Adapter.
     * Adapter数据源
     */
    private List<String> mDatas;

    private LayoutInflater mInflater;

    private boolean isCompany = false;

    //过滤相关
    /**
     * This lock is also used by the filter
     * (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     * 过滤器上的锁可以同步复制原始数据。
     */
    private final Object mLock = new Object();

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    //对象数组的备份，当调用ArrayFilter的时候初始化和使用。此时，对象数组只包含已经过滤的数据。
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;

    public FilterListViewAdapter(Context context, List<String> datas, boolean isCompany) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
        this.isCompany = isCompany;
    }

    @Override
    public int getCount() {
        return mDatas.size() > 0 ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_company, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String cpName = mDatas.get(position);
        int wrapIndex = cpName.indexOf("(");
        if (!isCompany && wrapIndex != -1 && wrapIndex != 0) {
            cpName = cpName.substring(0, wrapIndex) + "\n" + cpName.substring(wrapIndex);
        }
        holder.name.setText(cpName);
        return convertView;
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * 过滤数据的类
     */
    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     * <p/>
     * 一个带有首字母约束的数组过滤器，每一项不是以该首字母开头的都会被移除该list。
     */
    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mDatas);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写
                String[] strAry = prefixString.split("[,，\\s]+");

                ArrayList<String> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<String> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final String value = values.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.toLowerCase();//User对象的name属性作为过滤的参数

                    boolean filterFlag = true;
                    for (String str : strAry) {
                        if (!valueText.startsWith(str) && !valueText.contains(str)) {//第一个字符是否匹配
                            filterFlag = false;//将这个item加入到数组对象中
                        } else {//处理首字符是空格
                            final String[] words = valueText.split(" ");
                            // Start at index 0, in case valueText starts with space(s)
                            for (String word : words) {
                                if (!word.startsWith(str) && !word.contains(str)) {//一旦找到匹配的就break，跳出for循环
                                    filterFlag = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (filterFlag) {
                        newValues.add(value);
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            mDatas = (List<String>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated

                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}
