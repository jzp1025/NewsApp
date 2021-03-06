package com.example.newsapp.view.briefinfo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.ListViewAdapter;
import com.example.newsapp.singleitem.SingleListItem;
import com.example.newsapp.view.detailinfo.DetailInfoActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junxian on 9/7/2017.
 */

public class PageListFragment extends Fragment {
    public static final String CATEGORY = "CATEGORY";
    private String mCategory;
    private ArrayList<SingleListItem> mListItems;
    private PullToRefreshListView mPullRefreshListView;
    private ListViewAdapter mAdapter;
    private int mItemCount = 9;
    HashMap<String, Object> map;

    public static PageListFragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        PageListFragment pageListFragment = new PageListFragment();
        pageListFragment.setArguments(args);
        return pageListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = getArguments().getString(CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page_list, container, false);
        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);

        initDatas();
        mAdapter = new ListViewAdapter(getActivity(), mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
        // 设置监听事件
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
                {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView)
                    {
                        String label = DateUtils.formatDateTime(
                                getActivity().getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // 显示最后更新的时间
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        // 模拟加载任务
                        new GetDataTask().execute();
                    }
                });

        //设置点击事件
       mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("Category", mCategory);
                            bundle.putString("Title", mListItems.get(position - 1).map.get("Content").toString());
                            intent.putExtras(bundle);

                            startActivity(intent);

//                            int forDetailCode = 1000;
//                            startActivityForResult(intent, forDetailCode);
                        }
                    });

        return view;
    }

    private void initDatas() {
        // 初始化数据和数据源
        mListItems = new ArrayList<SingleListItem>();

        for (int i = 0; i < mItemCount; i++)
        {
            map = new HashMap<String, Object>();
            map.put("Content", mCategory + " " + i);
            if (i != 2)
                mListItems.add(new SingleListItem("normal", map));
            else
                mListItems.add(new SingleListItem("shit", map));
        }
    }

    private void updateDatas() {
        mItemCount ++;
        map = new HashMap<String, Object>();
        map.put("Content", mCategory + " " + mItemCount);
        mListItems.add(new SingleListItem("normal", map));
    }

    private class GetDataTask extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
            }
            return "" + (mItemCount++);
        }

        @Override
        protected void onPostExecute(String result)
        {
            map = new HashMap<String, Object>();
            map.put("Content", mCategory + " " + result);
            if (mItemCount % 4 != 0)
                mListItems.add(new SingleListItem("normal", map));
            else
                mListItems.add(new SingleListItem("shit", map));
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
        }
    }
}
