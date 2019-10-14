package com.criteo.storetailsdk.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.criteo.storetailsdk.view.managing.StoFormatsPositions;
import com.criteo.storetailsdk.view.models.StoBanner;
import com.criteo.storetailsdk.view.models.StoButterfly;
import com.criteo.storetailsdk.view.models.StoFormat;
import com.criteo.storetailsdk.view.models.StoVignette;
import com.criteo.storetailsdk.view.viewholders.BannerViewHolderInterface;
import com.criteo.storetailsdk.view.viewholders.BannerViewHolderRecycler;
import com.criteo.storetailsdk.view.viewholders.ButterflyPhoneViewHolderRecycler;
import com.criteo.storetailsdk.view.viewholders.ButterflyTabletViewHolderRecycler;
import com.criteo.storetailsdk.view.viewholders.ButterflyViewHolderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.criteo.storetailsdk.view.StoFormatType.stoBanner;

public class StoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StoFormatsListener, StoAdapterTrackEvents, StoImageManagerListener {
    private String TAG = "StoRecyclerViewAdapter";

    private Context                 context;
    private RecyclerView.Adapter    adapter;
    private StoFormatsPositions     positions;
    private StoAdapterListener      adapterListener;
    private StoLayoutCreator        layoutCreator;

    private List<StoFormat>         formats;

    private List<StoFormatType>     acceptedFormats;

    private final int               TYPE_BANNER = 1000;
    private final int               TYPE_VIGNETTE = 1001;
    private final int               TYPE_BUTTERFLY = 1002;

    private RecyclerView            parent;
    private int                     parentWidth;
    private int                     nativeCellHeight;
    private int                     spanCount;

    private StoImageManager         imageManager;

    public StoRecyclerViewAdapter(Context context, RecyclerView.Adapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.positions = StoFormatsPositions.defaultPosition();
        this.layoutCreator = null;
        this.adapterListener = null;
        this.formats = new ArrayList<>();
        this.acceptedFormats = new ArrayList<>();
        this.parentWidth = -1;
        this.nativeCellHeight = -1;
        this.spanCount = 1;

        this.imageManager = new StoImageManager(context.getApplicationContext(), this);

        acceptedFormats.add(stoBanner);
        acceptedFormats.add(StoFormatType.stoButterfly);
        acceptedFormats.add(StoFormatType.stoVignette);
    }

    public void onPause() {
        StoEventCommunicator.removeStoFormatsListener(this);
        clear();
    }

    public void onResume() {
        StoEventCommunicator.addStoFormatsListener(this);
    }

    public StoRecyclerViewAdapter setAdapterListener(StoAdapterListener listener) {
        this.adapterListener = listener;
        return this;
    }

    public StoRecyclerViewAdapter setPositions(StoFormatsPositions positions) {
        this.positions = positions;
        return this;
    }

    public StoRecyclerViewAdapter setLayoutCreator(StoLayoutCreator layoutCreator) {
        this.layoutCreator = layoutCreator;
        return this;
    }

    public void clear() {
        formats.clear();
        imageManager.clear();
    }

    @Override
    public int getItemCount() {
        int maxFormats = formats.size();

        if (maxFormats == 0)
            return adapter.getItemCount();

        return maxFormats + adapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {

        for (int i = 0; i < this.formats.size(); ++i) {
            StoFormat format = this.formats.get(i);
            if (format.getPosition() == position) {
                if (format.getFormatType() == stoBanner)
                    return TYPE_BANNER;
                else if (format.getFormatType() == StoFormatType.stoButterfly)
                    return TYPE_BUTTERFLY;
                else if (format.getFormatType() == StoFormatType.stoVignette)
                    return TYPE_VIGNETTE;
                else {
                    StoLog.e(TAG, "getItemViewType type is not handled");
                    // TODO
                    return -1;
                }
            }
        }

        int nbFormatPositionBefore = positions.getFormatPositionsBefore(this.formats, position);
        return adapter.getItemViewType(position - nbFormatPositionBefore);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.parent == null && parent instanceof RecyclerView) {
            this.parent = (RecyclerView)parent;
            parentWidth = parent.getMeasuredWidth();
            if (this.parent.getLayoutManager() instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) this.parent.getLayoutManager();
                spanCount = gridLayoutManager.getSpanCount();
            }
        }

        switch (viewType) {
            case TYPE_BANNER:
                View bannerView = LayoutInflater.from(context).inflate(R.layout.sto_banner, parent, false);
                return new BannerViewHolderRecycler(bannerView);
            case TYPE_VIGNETTE:
                View vignetteView = LayoutInflater.from(context).inflate(R.layout.sto_banner, parent, false);
                vignetteView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                Log.e(TAG, "measuredHeight " + vignetteView.getMeasuredHeight());
                return new BannerViewHolderRecycler(vignetteView);

            case TYPE_BUTTERFLY:
                int layoutResId = (!context.getResources().getBoolean(R.bool.isTablet)) ? R.layout.sto_butterfly_list : (spanCount == 1 ? R.layout.sto_butterfly_list : R.layout.sto_butterfly_grid);
                View butterflyView = LayoutInflater.from(context).inflate(layoutResId, parent, false);

                if (layoutResId == R.layout.sto_butterfly_grid)
                    return new ButterflyTabletViewHolderRecycler(butterflyView);
                else
                    return new ButterflyPhoneViewHolderRecycler(butterflyView);
        }

        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        StoFormat finalFormat = null;
        for (StoFormat stoFormat: this.formats) {
            if (stoFormat.getPosition() == position) {
                finalFormat = stoFormat;
            }
        }
        StoFormatType stoFormatType = (finalFormat != null) ? finalFormat.getFormatType() : StoFormatType.None;
        switch (stoFormatType) {
            case stoBanner:
                StoBanner bannerFormat = (StoBanner)finalFormat;
                BannerViewHolderInterface bannerViewHolder = (BannerViewHolderInterface) holder;
                String path = bannerFormat.getBackgroundImageURL(context.getResources().getBoolean(R.bool.isTablet));
                StoBannerViewCreator.getInstance(context).buildBannerView(bannerViewHolder, parentWidth, bannerFormat, adapterListener, imageManager.getImage(path));
                break;
            case stoVignette:
                StoVignette vignetteFormat = (StoVignette)finalFormat;
                BannerViewHolderInterface vignetteViewHolder = (BannerViewHolderInterface) holder;
                String path2 = vignetteFormat.getBackgroundImageURL(spanCount > 1);
                Log.e(TAG, "Before buildVignette " + path2);
                StoVignetteViewCreator.getInstance(context).buildView(vignetteViewHolder, vignetteFormat, adapterListener, imageManager.getImage(path2), spanCount > 1, nativeCellHeight);
                break;
            case stoButterfly:
                StoButterfly butterflyFormat = (StoButterfly)finalFormat;
                ButterflyViewHolderInterface butterflyHolder = (ButterflyViewHolderInterface) holder;
                boolean isBuild = StoButterflyViewCreator.getInstance(context).buildButterflyView(
                        butterflyHolder,
                        butterflyFormat,
                        layoutCreator,
                        adapterListener,
                        this,
                        parentWidth,
                        spanCount);
                if (!isBuild) {
                    butterflyHolder.getParent().setLayoutParams(new LinearLayout.LayoutParams(1, 1));
                }
                break;

            default:
                int nbFormatPositionBefore = positions.getFormatPositionsBefore(this.formats, position);

                holder.itemView.measure(0, 0);
                if (this.nativeCellHeight == -1) {
                    this.nativeCellHeight = holder.itemView.getMeasuredHeight();
                }
                adapter.onBindViewHolder(holder, position - nbFormatPositionBefore);
        }

        if (stoFormatType != StoFormatType.stoCustom && stoFormatType != StoFormatType.None) {
            if (!StoTracker.getInstance(context).isImpressionEqualToView())
                StoTracker.getInstance(context).imp(finalFormat);
        }
    }

    @Override
    public long getItemId(int i) {
        for (StoFormat format : this.formats) {
            if (format.getPosition() == i)
                return -1;
        }
        return adapter.getItemId(i - positions.getFormatPositionsBefore(this.formats, i));
    }

    @Override
    public void onStoFormatsReceived(List<StoFormat> formatList, String identifier) {

        StoLog.d(TAG, "onStoFormatsReceived " + formatList + " for identifier " + identifier);
        // Need to be cleared ?
        this.formats.clear();

        for (StoFormat format: formatList) {
            StoFormatType formatType = format.getFormatType();
            if (acceptedFormats.contains(formatType)) {
                this.formats.add(format);
            }
        }

        if (spanCount == 1)
            positions.generatePositions(this.formats, adapter.getItemCount());
        else
            positions.generatePositions(this.formats, adapter.getItemCount(), spanCount);

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
            if (spanCount == 1)
                positions.generatePositions(this.formats, adapter.getItemCount());
            else
                positions.generatePositions(this.formats, adapter.getItemCount(), spanCount);
        }

        Log.e(TAG, "onStoFormatsReceived END " + this.formats);

        imageManager.downloadImagesBeforeReload(this.formats, spanCount > 1);
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
        // Check span size and modify spanSizeLookup
        if (spanCount > 1) {
            int spanAdd = 0;
            final HashMap<Integer, Integer> spanSize = new HashMap<>();
            for (StoFormat format : this.formats) {
                if (format.getFormatType() == StoFormatType.stoButterfly) {
                    spanSize.put(format.getPosition(), 2);
                } else if (format.getFormatType() == StoFormatType.stoBanner) {
                    spanSize.put(format.getPosition(), spanCount);
                }
            }

            GridLayoutManager gridLayoutManager = (GridLayoutManager) this.parent.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (spanSize.keySet().contains(position))
                        return spanSize.get(position);
                    else
                        return 1;
                }
            });
        }

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager && getItemCount() > 0) {
                    final LinearLayoutManager llm = (LinearLayoutManager) manager;
                    int firstVisiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    int lastVisiblePosition = llm.findLastCompletelyVisibleItemPosition();
                    if (firstVisiblePosition > -1) {
                        for (StoFormat format: formats) {
                            if (format.getPosition() >= firstVisiblePosition && format.getPosition() <= lastVisiblePosition) {
                                if (StoTracker.getInstance(context).isImpressionEqualToView())
                                    StoTracker.getInstance(context).imp(format);
                                StoTracker.getInstance(context).view(format);
                            }
                        }
                    }
                }
            }
        });
    }
}
