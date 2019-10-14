package com.criteo.storetailsdk.view.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.datatransfert.values.StoTrackEventValue;
import com.criteo.storetailsdk.extras.StoEventCommunicator;
import com.criteo.storetailsdk.extras.StoFormatsListener;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.StoTracker;
import com.criteo.storetailsdk.view.StoFormatType;
import com.criteo.storetailsdk.view.constructions.StoBannerViewCreator;
import com.criteo.storetailsdk.view.constructions.StoButterflyViewCreator;
import com.criteo.storetailsdk.view.constructions.StoVignetteViewCreator;
import com.criteo.storetailsdk.view.managing.StoImageManager;
import com.criteo.storetailsdk.view.managing.StoViewType;
import com.criteo.storetailsdk.view.managing.StoFormatsPositions;
import com.criteo.storetailsdk.view.models.StoBanner;
import com.criteo.storetailsdk.view.models.StoButterfly;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.criteo.storetailsdk.view.models.StoVignette;
import com.criteo.storetailsdk.view.viewholders.BannerViewHolderListview;
import com.criteo.storetailsdk.view.viewholders.ButterflyPhoneViewHolderListview;
import com.criteo.storetailsdk.view.viewholders.ButterflyViewHolderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoListViewAdapter extends BaseAdapter implements StoFormatsListener, StoAdapterTrackEvents, StoImageManagerListener {
    private String TAG = "StoListViewAdapter";

    private Context                     context;
    private BaseAdapter                 adapter;
    private StoFormatsPositions         positions;
    private StoAdapterListener          adapterListener;
    private StoLayoutCreator            layoutCreator;

    private List<StoFormat>             formats;
    private Map<StoFormatType, Integer> formatTypes;

    private List<StoFormatType>         acceptedFormats;

    private StoImageManager             imageManager;
    private AbsListView.OnScrollListener    scrollListener;

    public StoListViewAdapter(Context context, BaseAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.layoutCreator = null;
        this.adapterListener = null;
        this.positions = null;
        this.formats = new ArrayList<>();
        this.formatTypes = new HashMap<>();
        this.acceptedFormats = new ArrayList<>();

        this.imageManager = new StoImageManager(context, this);

        this.positions = StoFormatsPositions.defaultPosition();

        acceptedFormats.add(StoFormatType.stoBanner);
        acceptedFormats.add(StoFormatType.stoButterfly);
        acceptedFormats.add(StoFormatType.stoVignette);
    }

    public StoListViewAdapter setAdapterListener(StoAdapterListener listener) {
        this.adapterListener = listener;
        return this;
    }

    public StoListViewAdapter setPositions(StoFormatsPositions positions) {
        this.positions = positions;
        return this;
    }

    public StoListViewAdapter setLayoutCreator(StoLayoutCreator layoutCreator) {
        this.layoutCreator = layoutCreator;
        return this;
    }

    public void clear() {
        formats.clear();
        imageManager.clear();
    }

    public void onPause() {
        StoEventCommunicator.removeStoFormatsListener(this);
        clear();
    }

    public void onResume() {
        StoEventCommunicator.addStoFormatsListener(this);
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();

        super.notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        StoLog.d(TAG, "getViewTypeCount " + adapter.getClass().getName());
        return adapter.getViewTypeCount() + this.formatTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        StoLog.d(TAG, "getItemViewType "+ adapter.getClass().getName()+ " " + position);

        for (StoFormat format: this.formats) {
            if (format.getPosition() == position) {
                for (Map.Entry<StoFormatType, Integer> entry : this.formatTypes.entrySet()) {
                    if (entry.getKey() == format.getFormatType()) {
                        return entry.getValue();
                    }
                }
            }
        }

        int nbFormatPositionBefore = positions.getFormatPositionsBefore(this.formats, position);
        return adapter.getItemViewType(position - nbFormatPositionBefore);
    }

    @Override
    public int getCount() {
        int maxFormats = formats.size();

        if (maxFormats == 0)
            return adapter.getCount();

        return maxFormats + adapter.getCount();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int itemViewType = getItemViewType(i);
        int nbFormatPositionBefore = positions.getFormatPositionsBefore(this.formats, i);
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if (viewGroup instanceof ListView && scrollListener == null) {
            scrollListener = new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalItems) {
                    for (int i = firstVisible; i < firstVisible + visibleCount; ++i) {
                        int childPosition  = i - firstVisible;
                        View view = absListView.getChildAt(childPosition);
                        if (view != null && isViewVisible(absListView, view)) {
                            for (StoFormat format: StoListViewAdapter.this.formats) {
                                if (format.getPosition() == i) {
                                    if (StoTracker.getInstance(context).isImpressionEqualToView())
                                        StoTracker.getInstance(context).imp(format);
                                    StoTracker.getInstance(context).view(format);
                                }
                            }
                        }
                    }
                }

                private boolean isViewVisible(AbsListView listView, View view) {
                    Rect scrollBounds = new Rect();
                    listView.getDrawingRect(scrollBounds);

                    float top = view.getY();
                    float bottom = top + view.getHeight();

                    if (scrollBounds.top <= top && scrollBounds.bottom >= bottom) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            ((ListView)viewGroup).setOnScrollListener(scrollListener);
        }

        if (itemViewType < adapter.getViewTypeCount()) {
            StoLog.d(TAG, "getView "+ adapter.getClass().getName()+" at position " + i + " is not a StoFormat - real position = " + (i - nbFormatPositionBefore));
            return adapter.getView(i - nbFormatPositionBefore, view, viewGroup);
        } else {
            StoFormat format = this.formats.get(nbFormatPositionBefore);

            if (!StoTracker.getInstance(context).isImpressionEqualToView()) {
                StoTracker.getInstance(context).imp(format);
            }
            StoLog.d(TAG, "getView "+ adapter.getClass().getName()+" at position " + i + " itemViewType " + itemViewType);

            if (format.getFormatType() == StoFormatType.stoBanner) {
                final BannerViewHolderListview viewHolder;
                if (view == null || !(view.getTag() instanceof BannerViewHolderListview)) {
                    StoLog.d(TAG, "getView isNull");
                    view = LayoutInflater.from(context).inflate(R.layout.sto_banner, viewGroup, false);
                    viewHolder = new BannerViewHolderListview();
                    viewHolder.parent = view.findViewById(R.id.sto_banner_parent);
                    viewHolder.background = view.findViewById(R.id.sto_banner_background);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (BannerViewHolderListview)view.getTag();
                }
                String path = ((StoBanner) format).getBackgroundImageURL(isTablet);
                StoBannerViewCreator.getInstance(context.getApplicationContext()).buildBannerView(viewHolder, viewGroup.getMeasuredWidth(), (StoBanner) format, adapterListener, imageManager.getImage(path));
            } else if (format.getFormatType() == StoFormatType.stoVignette) {
                final BannerViewHolderListview viewHolder;
                if (view == null || !(view.getTag() instanceof BannerViewHolderListview)) {
                    StoLog.d(TAG, "getView isNull");
                    view = LayoutInflater.from(context).inflate(R.layout.sto_banner, viewGroup, false);
                    viewHolder = new BannerViewHolderListview();
                    viewHolder.parent = view.findViewById(R.id.sto_banner_parent);
                    viewHolder.background = view.findViewById(R.id.sto_banner_background);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (BannerViewHolderListview)view.getTag();
                }
                String path = ((StoVignette) format).getBackgroundImageURL(false);
                StoVignetteViewCreator.getInstance(context.getApplicationContext()).buildView(viewHolder, (StoVignette) format, adapterListener, imageManager.getImage(path), false, -1);
            } else if (format.getFormatType() == StoFormatType.stoButterfly) {

                ButterflyViewHolderInterface butterflyViewHolderInterface;
                final ButterflyPhoneViewHolderListview viewHolder;

                if (view == null || !(view.getTag() instanceof ButterflyPhoneViewHolderListview)) {
                    StoLog.d(TAG, "buildButterflyView Phone isNull");
                    view = LayoutInflater.from(context).inflate(R.layout.sto_butterfly_list, viewGroup, false);
                    viewHolder = new ButterflyPhoneViewHolderListview();
                    viewHolder.parent = view.findViewById(R.id.sto_butterfly_parent);
                    viewHolder.background = view.findViewById(R.id.sto_butterfly_bg);
                    viewHolder.background_img = view.findViewById(R.id.sto_butterfly_bg_img);
                    viewHolder.logo = view.findViewById(R.id.sto_butterfly_logo);
                    viewHolder.leftContainer = view.findViewById(R.id.sto_butterfly_left_container);
                    viewHolder.rightContainer = view.findViewById(R.id.sto_butterfly_right_container);
                    viewHolder.leftSeparator = view.findViewById(R.id.sto_butterfly_left_separator);
                    viewHolder.rightSeparator = view.findViewById(R.id.sto_butterfly_right_separator);
                    viewHolder.button1 = view.findViewById(R.id.sto_butterfly_btn1);
                    viewHolder.button2 = view.findViewById(R.id.sto_butterfly_btn2);
                    viewHolder.button3 = view.findViewById(R.id.sto_butterfly_btn3);
                    viewHolder.button4 = view.findViewById(R.id.sto_butterfly_btn4);
                    view.setTag(viewHolder);
                } else
                    viewHolder = (ButterflyPhoneViewHolderListview) view.getTag();

                butterflyViewHolderInterface = viewHolder;

                boolean isBuild = StoButterflyViewCreator.getInstance(context.getApplicationContext()).buildButterflyView(
                        butterflyViewHolderInterface,
                        (StoButterfly)format,
                        layoutCreator,
                        adapterListener,
                        this,
                        viewGroup.getMeasuredWidth(),
                        1);
                if (!isBuild) {
                    butterflyViewHolderInterface.getParent().setLayoutParams(new LinearLayout.LayoutParams(1, 1));
                }
            }
            return view;
        }
    }

    @Override
    public Object getItem(int i) {
        if (getItemViewType(i) < adapter.getViewTypeCount()) {
            return adapter.getItem(i - positions.getFormatPositionsBefore(this.formats, i));
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        if (getItemViewType(i) < adapter.getViewTypeCount()) {
            return adapter.getItemId(i - positions.getFormatPositionsBefore(this.formats, i));
        }

        return -1;
    }

    @Override
    public void onStoFormatsReceived(List<StoFormat> formatList, String identifier) {

        StoLog.d(TAG, "onStoFormatsReceived " + formatList + " for identifier " + identifier);
        // Need to be cleared ?
        this.formatTypes.clear();
        this.formats.clear();

        int viewTypeCount = adapter.getViewTypeCount();

        for (StoFormat format: formatList) {
            StoFormatType formatType = format.getFormatType();
            if (acceptedFormats.contains(formatType)) {
                this.formats.add(format);
                if (!this.formatTypes.containsKey(formatType)) {
                    this.formatTypes.put(formatType, viewTypeCount);
                    viewTypeCount++;
                }
            }
        }

        positions.generatePositions(this.formats, adapter.getCount());

        boolean hasRemovedFormat = false;
        for (int j = 0; j < this.formats.size(); j++) {
            if (this.formats.get(j).getFormatType() == StoFormatType.stoButterfly) {
                if (!StoTracker.getInstance(context).isFormatAvailable((StoButterfly)this.formats.get(j))) {
                    this.formats.remove(j);
                    hasRemovedFormat = true;
                    j--;
                }
            }
        }

        if (hasRemovedFormat) {
            positions.generatePositions(this.formats, adapter.getCount());
        }

        imageManager.downloadImagesBeforeReload(this.formats, false);
    }

    @Override
    public void onStoFailure(String message, String identifier) {
        StoLog.d(TAG, "onStoFailure " + message + " for identifier " + identifier);
    }

    /*
     * Implementation of StoFormat Event Listener
     *
     * These functions should be called by App when the product view is created.
     */
    @Override
    public void openProductPage(StoFormat format) {
        StoLog.d(TAG, "openProductPage " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).openProductPage(format, ((StoButterfly)format).getProductIdSelected());
        }
    }

    @Override
    public void addToWishList(StoFormat format) {
        StoLog.d(TAG, "addToWishList " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).addToList(format, ((StoButterfly)format).getProductIdSelected());
        }
    }

    @Override
    public void addToBasket(StoFormat format) {
        StoLog.d(TAG, "addToBasket " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).basket(format, ((StoButterfly)format).getProductIdSelected(), StoTrackEventValue.AbkBtn);
        }
    }

    @Override
    public void addToBasketMore(StoFormat format) {
        StoLog.d(TAG, "addToBasketMore " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).basket(format, ((StoButterfly)format).getProductIdSelected(), StoTrackEventValue.QuantityMore);
        }
    }

    @Override
    public void addToBasketLess(StoFormat format) {
        StoLog.d(TAG, "addToBasketLess " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).basket(format, ((StoButterfly)format).getProductIdSelected(), StoTrackEventValue.QuantityLess);
        }
    }

    @Override
    public void basketQuantityChange(StoFormat format) {
        StoLog.d(TAG, "basketQuantityChange " + format);
        if (format.getFormatType() == StoFormatType.stoButterfly) {
            StoTracker.getInstance(context).basket(format, ((StoButterfly)format).getProductIdSelected(), StoTrackEventValue.QuantityChange);
        }
    }

    @Override
    public void reloadView() {
        this.notifyDataSetChanged();
    }
}
