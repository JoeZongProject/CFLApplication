package com.joecorelibrary.mvvm.bindingadapter.listview;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joecorelibrary.mvvm.command.ReplyCommand;
import com.joecorelibrary.mvvm.widget.pulltorefresh.XListView;

import java.util.concurrent.TimeUnit;

import rx.subjects.PublishSubject;

/**
 * Created by kelin on 16-3-24.
 */
public final class ViewBindingAdapter {

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final ListView listView,
                                             final ReplyCommand<ListViewScrollDataWrapper> onScrollChangeCommand,
                                             final ReplyCommand<Integer> onScrollStateChangedCommand) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
                if (onScrollStateChangedCommand != null) {
                    onScrollChangeCommand.equals(scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ListViewScrollDataWrapper(scrollState, firstVisibleItem, visibleItemCount, totalItemCount));
                }
            }
        });
    }


    @BindingAdapter(value = {"onListViewItemClickCommand"}, requireAll = false)
    public static void onItemClickCommand(final ListView listView, final ReplyCommand<Integer> onItemClickCommand) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickCommand != null) {
                    int count = listView.getAdapter().getCount();
                    Class itemClass = listView.getAdapter().getItem(position).getClass();
                    if (position < count && !itemClass.equals(Object.class)) {
                        onItemClickCommand.execute(position);
                    }
                }
            }
        });
    }

    @BindingAdapter(value = {"onItemLongClickCommand"}, requireAll = false)
    public static void onItemLongClickCommand(final ListView listView, final ReplyCommand<Integer> onItemLongClickCommand) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemLongClickCommand != null) {
                    int count = listView.getAdapter().getCount();
                    Class itemClass = listView.getAdapter().getItem(position).getClass();
                    if (position < count && !itemClass.equals(Object.class)) {
                        onItemLongClickCommand.execute(position);
                    }
                }
                return true;
            }
        });
    }

    @BindingAdapter(value = {"onXListViewItemClickCommand"}, requireAll = false)
    public static void onXListViewItemClickCommand(final XListView listView, final ReplyCommand<Integer> onItemClickCommand) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (onItemClickCommand != null) {
                int count = listView.getAdapter().getCount();
                Class itemClass = listView.getAdapter().getItem(position).getClass();
                if (count != 0 && (position-1) < count && !itemClass.equals(Object.class)) {
                    onItemClickCommand.execute(position - 1);
                }
            }
        });
    }

    @BindingAdapter(value = {"xListViewLoadEnable"}, requireAll = false)
    public static void setXListViewLoadEnable(final XListView listView, boolean enable) {
        listView.setPullLoadEnable(enable);
    }



    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(final ListView listView, final ReplyCommand<Integer> onLoadMoreCommand) {
        listView.setOnScrollListener(new OnScrollListener(listView, onLoadMoreCommand));
    }


    public static class OnScrollListener implements AbsListView.OnScrollListener {
        private PublishSubject<Integer> methodInvoke = PublishSubject.create();
        private ReplyCommand<Integer> onLoadMoreCommand;
        private ListView listView;

        public OnScrollListener(ListView listView, ReplyCommand<Integer> onLoadMoreCommand) {
            this.onLoadMoreCommand = onLoadMoreCommand;
            this.listView = listView;
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(c -> this.onLoadMoreCommand.execute(c));
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != listView.getHeaderViewsCount()
                    + listView.getFooterViewsCount()) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(totalItemCount);
                }
            }
        }
    }

    public static class ListViewScrollDataWrapper {
        public int firstVisibleItem;
        public int visibleItemCount;
        public int totalItemCount;
        public int scrollState;

        public ListViewScrollDataWrapper(int scrollState, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
            this.scrollState = scrollState;
        }
    }
}
