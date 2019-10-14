package com.criteo.storetailsdk.view.constructions;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.criteo.storetailsdk.R;
import com.criteo.storetailsdk.extras.crawl.StoCrawl;
import com.criteo.storetailsdk.extras.models.StoProduct;
import com.criteo.storetailsdk.logs.StoLog;
import com.criteo.storetailsdk.tracker.StoTracker;
import com.criteo.storetailsdk.view.StoFormatOptionType;
import com.criteo.storetailsdk.view.StoFormatType;
import com.criteo.storetailsdk.view.StoViewUtils;
import com.criteo.storetailsdk.view.adapters.StoAdapterTrackEvents;
import com.criteo.storetailsdk.view.viewholders.ButterflyViewHolderInterface;
import com.criteo.storetailsdk.view.adapters.StoAdapterListener;
import com.criteo.storetailsdk.view.adapters.StoLayoutCreator;
import com.criteo.storetailsdk.view.models.StoButterfly;
import com.criteo.storetailsdk.view.models.StoButtonContent;
import com.criteo.storetailsdk.view.views.StoProductButton;

public class StoButterflyViewCreator {

    private String  TAG = "StoButterflyViewCreator";
    private Context context;
    private boolean isTabletGridView;

    /** Constructeur privé */
    private StoButterflyViewCreator(Context context) {
        this.context = context;
        isTabletGridView = false;
    }

    /** Instance unique non préinitialisée */
    private static StoButterflyViewCreator INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static StoButterflyViewCreator getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StoButterflyViewCreator(context);
        }
        return INSTANCE;
    }

    public boolean buildButterflyView(ButterflyViewHolderInterface butterflyViewHolderInterface,
                                   final StoButterfly format,
                                   final StoLayoutCreator layoutCreator,
                                   final StoAdapterListener adapterListener,
                                   final StoAdapterTrackEvents eventListener,
                                   int width, int spanCount) {

        View productView;
        if (layoutCreator != null && format.getProductIdSelected() != null) {
            productView = layoutCreator.createLayout(eventListener, format, format.getProductIdSelected());
        } else {
            return false;
        }
        configureDimensions(butterflyViewHolderInterface, productView, width, spanCount);

        if (!configureView(butterflyViewHolderInterface, format, layoutCreator, eventListener))
            return false;


        if (butterflyViewHolderInterface.getParent().getChildCount() > 1)
            butterflyViewHolderInterface.getParent().removeViewAt(1);
        butterflyViewHolderInterface.getParent().addView(productView, 1);

        butterflyViewHolderInterface.getParent().setBackgroundColor(format.getBorderColor());
        setOption(butterflyViewHolderInterface, format);
        setOptionButtonListener(butterflyViewHolderInterface, format, adapterListener);

        return true;
    }

    private void configureDimensions(ButterflyViewHolderInterface viewHolder, View productView, int width, int spanCount) {
        // For phone or Tablet simple list
        this.isTabletGridView = context.getResources().getBoolean(R.bool.isTablet) && spanCount > 1;
        if (!this.isTabletGridView) {
            final double RATIO = 4.14;
            // Configure the height of Butterfly view
            double height = (width / RATIO) + 0.5;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)height);
            viewHolder.getBackground().setLayoutParams(layoutParams);

            viewHolder.getLogo().setLayoutParams(new RelativeLayout.LayoutParams((int)((width / 2.7) + 0.5), ViewGroup.LayoutParams.MATCH_PARENT));

            int buttonWidth = (int) ((width / RATIO) + 0.5);
            int buttonHeight = (int) ((height / 3.0) + 0.5);
            int separatorHeight = buttonHeight / 3;
            if (viewHolder.getButton1().getVisibility() == View.VISIBLE) viewHolder.getButton1().setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
            if (viewHolder.getButton2().getVisibility() == View.VISIBLE) viewHolder.getButton2().setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
            if (viewHolder.getButton3().getVisibility() == View.VISIBLE) viewHolder.getButton3().setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
            if (viewHolder.getButton4().getVisibility() == View.VISIBLE) viewHolder.getButton4().setLayoutParams(new LinearLayout.LayoutParams(buttonWidth, buttonHeight));
            viewHolder.getLeftSeparator().setLayoutParams(new LinearLayout.LayoutParams(1, separatorHeight));
            viewHolder.getRightSeparator().setLayoutParams(new LinearLayout.LayoutParams(1, separatorHeight));

            // For tablet
        } else {
            // Configure the height of Butterfly view
            if (productView != null) {
                if (spanCount == 1) {
                    spanCount = 2;
                }
                width = (width / spanCount);
                productView.measure(0, 0);
                int height = productView.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                viewHolder.getBackground().setLayoutParams(layoutParams);
            }
        }
    }

    private boolean configureView(ButterflyViewHolderInterface viewHolder,
                                  final StoButterfly format,
                                  final StoLayoutCreator layoutCreator,
                                  final StoAdapterTrackEvents eventListener) {

        // Background image
        if (format.hasMultiBackground() && this.isTabletGridView) {
            String formatProductIdSelected = format.getProductIdSelected();
            if (formatProductIdSelected != null) {
                int index = -1;
                for (int i = 0; i < format.getButtonContentList().size(); ++i) {
                    StoButtonContent content = format.getButtonContentList().get(i);
                    if (content.getProductId().equalsIgnoreCase(formatProductIdSelected))
                        index = i;
                }
                if (format.getMultiBackgroundImage(index) != null) {
                    Glide.with(context).load(format.getMultiBackgroundImage(index)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.getBackgroundImage());
                }
            } else if (format.getMultiBackgroundImage(0) != null) {
                Glide.with(context).load(format.getMultiBackgroundImage(0)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.getBackgroundImage());
            }
        } else {
            String bgURL = format.getBackgroundImageURL(this.isTabletGridView);
            if (bgURL != null) {
                StoLog.d(TAG, "configurePhoneView bgImg " + bgURL);
                Glide.with(context).load(bgURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.getBackgroundImage());
            } else {
                StoLog.d(TAG, "configurePhoneView bgImg null");
            }
        }

        // Background color
        viewHolder.getBackground().setBackgroundColor(format.getBackgroundColor());

        // Logo background image
        if (viewHolder.getLogo() != null) {
            // Set logo image
            String logoURL = format.getLogoImageURL();
            if (logoURL != null)
                Glide.with(context).load(logoURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.getLogo());
        }

        int nb_products = format.getButtonContentList().size();
        if (nb_products == 1 && !this.isTabletGridView) {
            if (viewHolder.getLeftContainer() != null)
                viewHolder.getLeftContainer().setVisibility(View.GONE);
            if (viewHolder.getRightContainer() != null)
                viewHolder.getRightContainer().setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)viewHolder.getLogo().getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            viewHolder.getLogo().setLayoutParams(layoutParams);
        } else {
            // Configure buttons
            int max_visible_btns = 4;
            if (this.isTabletGridView) {
                max_visible_btns = getMaxButtonsToDisplayOnTablet(viewHolder.getBackground().getLayoutParams().height);
                if (format.getOptionValue() != null) {
                    max_visible_btns--;
                }
            }

            if (max_visible_btns <= 0) {
                if (max_visible_btns < 0) {
                    // We can't show CTA OR Product buttons
                    StoLog.e(TAG, "OUPS");
                    return false;
                }
            }

            if (viewHolder.getButton1() != null)
                viewHolder.getButton1().setVisibility(View.GONE);
            if (viewHolder.getButton2() != null)
                viewHolder.getButton2().setVisibility(View.GONE);
            if (viewHolder.getButton3() != null)
                viewHolder.getButton3().setVisibility(View.GONE);
            if (viewHolder.getButton4() != null) {
                viewHolder.getButton4().setVisibility(View.GONE);
            }
            if (viewHolder.getButton5() != null)
                viewHolder.getButton5().setVisibility(View.GONE);

            if (viewHolder.getButton1() != null && nb_products >= 1) {
                viewHolder.getButton1().setText(format.getButtonContentList().get(0).getButtonName());
                viewHolder.getButton1().setProductId(format.getButtonContentList().get(0).getProductId());
                viewHolder.getButton1().setVisibility(View.VISIBLE);
                setButtonsListener(viewHolder.getButton1(), viewHolder, format, layoutCreator, eventListener);
            }
            if (viewHolder.getButton2() != null && nb_products >= 2 && max_visible_btns >= 2) {
                viewHolder.getButton2().setText(format.getButtonContentList().get(1).getButtonName());
                viewHolder.getButton2().setProductId(format.getButtonContentList().get(1).getProductId());
                viewHolder.getButton2().setVisibility(View.VISIBLE);
                setButtonsListener(viewHolder.getButton2(), viewHolder, format, layoutCreator, eventListener);
            }
            if (viewHolder.getButton3() != null && nb_products >= 3 && max_visible_btns >= 3) {
                viewHolder.getButton3().setText(format.getButtonContentList().get(2).getButtonName());
                viewHolder.getButton3().setProductId(format.getButtonContentList().get(2).getProductId());
                viewHolder.getButton3().setVisibility(View.VISIBLE);
                if (!this.isTabletGridView)
                    viewHolder.getButton4().setVisibility(View.INVISIBLE);
                setButtonsListener(viewHolder.getButton3(), viewHolder, format, layoutCreator, eventListener);
            }
            if (viewHolder.getButton4() != null && nb_products >= 4 && max_visible_btns >= 4) {
                viewHolder.getButton4().setText(format.getButtonContentList().get(3).getButtonName());
                viewHolder.getButton4().setProductId(format.getButtonContentList().get(3).getProductId());
                viewHolder.getButton4().setVisibility(View.VISIBLE);
                setButtonsListener(viewHolder.getButton4(), viewHolder, format, layoutCreator, eventListener);
            }
            if (viewHolder.getButton5() != null && nb_products >= 5 && max_visible_btns >= 5) {
                viewHolder.getButton5().setText(format.getButtonContentList().get(4).getButtonName());
                viewHolder.getButton5().setProductId(format.getButtonContentList().get(4).getProductId());
                viewHolder.getButton5().setVisibility(View.VISIBLE);
                setButtonsListener(viewHolder.getButton5(), viewHolder, format, layoutCreator, eventListener);
            }

            setButtonsColor(viewHolder, format);
        }
        return true;
    }

    private void setButtonsColor(ButterflyViewHolderInterface viewHolder, StoButterfly stoButterfly) {

        String productIdSelected = stoButterfly.getProductIdSelected();

        if (viewHolder.getButton1() != null && viewHolder.getButton1().getVisibility() == View.VISIBLE) {
            if (viewHolder.getButton1().getProductId().compareTo(productIdSelected) == 0) {
                viewHolder.getButton1().setBackgroundColor(stoButterfly.getButtonSelectedColor());
                viewHolder.getButton1().setTextColor(stoButterfly.getButtonSelectedTextColor());
            } else {
                viewHolder.getButton1().setBackgroundColor(stoButterfly.getButtonColor());
                viewHolder.getButton1().setTextColor(stoButterfly.getButtonTextColor());
            }

            if (viewHolder.getButton2() != null && viewHolder.getButton2().getVisibility() == View.VISIBLE) {
                if (viewHolder.getButton2().getProductId().compareTo(productIdSelected) == 0) {
                    viewHolder.getButton2().setBackgroundColor(stoButterfly.getButtonSelectedColor());
                    viewHolder.getButton2().setTextColor(stoButterfly.getButtonSelectedTextColor());
                } else {
                    viewHolder.getButton2().setBackgroundColor(stoButterfly.getButtonColor());
                    viewHolder.getButton2().setTextColor(stoButterfly.getButtonTextColor());
                }

                if (viewHolder.getButton3() != null && viewHolder.getButton3().getVisibility() == View.VISIBLE) {
                    if (viewHolder.getButton3().getProductId().compareTo(productIdSelected) == 0) {
                        viewHolder.getButton3().setBackgroundColor(stoButterfly.getButtonSelectedColor());
                        viewHolder.getButton3().setTextColor(stoButterfly.getButtonSelectedTextColor());
                    } else {
                        viewHolder.getButton3().setBackgroundColor(stoButterfly.getButtonColor());
                        viewHolder.getButton3().setTextColor(stoButterfly.getButtonTextColor());
                    }

                    if (viewHolder.getButton4() != null && viewHolder.getButton4().getVisibility() == View.VISIBLE) {
                        if (viewHolder.getButton4().getProductId().compareTo(productIdSelected) == 0) {
                            viewHolder.getButton4().setBackgroundColor(stoButterfly.getButtonSelectedColor());
                            viewHolder.getButton4().setTextColor(stoButterfly.getButtonSelectedTextColor());
                        } else {
                            viewHolder.getButton4().setBackgroundColor(stoButterfly.getButtonColor());
                            viewHolder.getButton4().setTextColor(stoButterfly.getButtonTextColor());
                        }

                        if (viewHolder.getButton5() != null && viewHolder.getButton5().getVisibility() == View.VISIBLE) {
                            if (viewHolder.getButton5().getProductId().compareTo(productIdSelected) == 0) {
                                viewHolder.getButton5().setBackgroundColor(stoButterfly.getButtonSelectedColor());
                                viewHolder.getButton5().setTextColor(stoButterfly.getButtonSelectedTextColor());
                            } else {
                                viewHolder.getButton5().setBackgroundColor(stoButterfly.getButtonColor());
                                viewHolder.getButton5().setTextColor(stoButterfly.getButtonTextColor());
                            }
                        }
                    }
                }
            }
        }
    }

    private int getMaxButtonsToDisplayOnTablet(int heightDp) {

        StoLog.d(TAG, "getMaxButtonsToDisplay heightDp " + heightDp);
        float heightPx = StoViewUtils.convertDpToPixel(heightDp, context);
        float spaceHeightPx = StoViewUtils.convertDpToPixel(context.getResources().getDimension(R.dimen.butterfly_space_height), context);
        float buttonHeightPx = StoViewUtils.convertDpToPixel(context.getResources().getDimension(R.dimen.butterfly_button_height), context);

        float buttonSpaceHeightPx = StoViewUtils.convertDpToPixel(context.getResources().getDimension(R.dimen.butterfly_space_between_button), context);
        StoLog.d(TAG, "getMaxButtonsToDisplay heightPx " + heightPx + " space " + spaceHeightPx + " buttonHeight " + buttonHeightPx + " buttonSpaceHeight " + buttonSpaceHeightPx);

        float minButtonsHeight = buttonHeightPx + buttonSpaceHeightPx;
        int maxButtonsToDisplay = (int)((heightPx - spaceHeightPx) / minButtonsHeight);
        StoLog.d(TAG, "getMaxButtonsToDisplay minButtonsHeight " + minButtonsHeight);
        StoLog.d(TAG, "getMaxButtonsToDisplay " + maxButtonsToDisplay);
        return maxButtonsToDisplay;
    }

    private void addBorders(View view, StoButterfly stoButterfly) {
        GradientDrawable border = new GradientDrawable();
        int padding = (int) StoViewUtils.convertDpToPixel(5, context);

        border.setStroke(padding, stoButterfly.getBorderColor());
        view.setBackground(border);
    }

    private void setOption(ButterflyViewHolderInterface viewHolder, StoButterfly stoButterfly) {

        StoFormatOptionType optionType = stoButterfly.getOptionType();
        String optionValue = stoButterfly.getOptionValue();

        if (this.isTabletGridView) {
            StoLog.d(TAG, "setButterflyOption: isTablet");
            if (optionValue != null) {
                if (viewHolder.getOptionLayout() != null)
                    viewHolder.getOptionLayout().setVisibility(View.VISIBLE);
                if (optionType == StoFormatOptionType.LEGAL) {
                    if (viewHolder.getOptionButton() != null)
                        viewHolder.getOptionButton().setVisibility(View.GONE);

                    if (viewHolder.getOptionText() != null) {
                        viewHolder.getOptionText().setVisibility(View.VISIBLE);
                        viewHolder.getOptionText().setText(optionValue);
                        viewHolder.getOptionText().setTextColor(stoButterfly.getOptionTextColor());
                    }
                } else {
                    if (viewHolder.getOptionText() != null)
                        viewHolder.getOptionText().setVisibility(View.GONE);

                    if (viewHolder.getOptionButton() != null) {
                        viewHolder.getOptionButton().setVisibility(View.VISIBLE);
                        viewHolder.getOptionButton().setText(stoButterfly.getOptionText());
                        stoButterfly.setOptionButtonColor(viewHolder.getOptionButton());
                    }
                }
            } else {
                if (viewHolder.getOptionLayout() != null)
                    viewHolder.getOptionLayout().setVisibility(View.GONE);
            }
        }
    }

    /**
     * Sets buttons' listeners on the Butterfly
     *
     * @param stoButterfly:
     */
    private void setButtonsListener(final StoProductButton button,
                                    final ButterflyViewHolderInterface viewHolder,
                                    final StoButterfly stoButterfly,
                                    final StoLayoutCreator layoutCreator,
                                    final StoAdapterTrackEvents eventListener) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getProductId() != null) {
                    /*
                     * Gets the stoProduct from the application
                     */
                    StoProduct stoProduct = StoCrawl.getInstance().getFormatProductById(button.getProductId());

                    StoLog.d(TAG,"setButtonListeners: clicked: " + button.getProductId());

                    /*
                     * Verifies if the retrieved product is not null
                     */
                    if (stoProduct != null) {
                        StoLog.d(TAG,"setButtonListeners: button clicked : " + stoProduct.getId());

                        /*
                         * Calls the browse trackAction
                         */
                        StoTracker.getInstance(context).browse(stoButterfly, stoProduct.getId());

                        /*
                         * Updates the ListView
                         */
                        if (layoutCreator != null) {
                            View productView = layoutCreator.createLayout(eventListener, stoButterfly, button.getProductId());
                            viewHolder.getParent().removeViewAt(1);
                            viewHolder.getParent().addView(productView, 1);
                        }

                        stoButterfly.setProductIdSelected(button.getProductId());

                        StoButterflyViewCreator.this.setButtonsColor(viewHolder, stoButterfly);

                        if (StoButterflyViewCreator.this.isTabletGridView) {
                            if (stoButterfly.hasMultiBackground()) {
                                int index = -1;
                                for (int i = 0; i < stoButterfly.getButtonContentList().size(); ++i) {
                                    if (button.getProductId().equalsIgnoreCase(stoButterfly.getButtonContentList().get(i).getProductId()))
                                        index = i;
                                }
                                final String backgroundURL = stoButterfly.getMultiBackgroundImage(index);
                                if (backgroundURL != null) {
                                    StoLog.d(TAG, "hasMultiBackground " + backgroundURL + " for index " + index);
                                    Glide.with(context).load(backgroundURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.getBackgroundImage());
                                } else {
                                    StoLog.d(TAG, "hasMultiBackground ERROR " + " for index " + index);
                                }
                            }
                        }
                    } else {
                        StoLog.e(TAG, "setButtonListeners: retrieved from Application Object product is null!");
                    }
                } else {
                    StoLog.e(TAG, "setButtonListeners: productId is null!");
                }
            }
        });
    }

    private void setOptionButtonListener(final ButterflyViewHolderInterface viewHolder, final StoButterfly stoButterfly, final StoAdapterListener adapterListener) {
        /*
         * Sets listeners to option button
         */
        View option = null;
        if (this.isTabletGridView) {
            option = viewHolder.getOptionButton();
        } else if (viewHolder.getLogo() != null && stoButterfly.getOptionType() != StoFormatOptionType.NONE && stoButterfly.getOptionType() != StoFormatOptionType.LEGAL) {
            option = viewHolder.getLogo();
        }
        if (option != null) {
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StoTracker.getInstance(context).openOption(stoButterfly, stoButterfly.getOptionType());
                    if (adapterListener != null) {
                        if (stoButterfly.getFormatType() == StoFormatType.stoButterfly) {
                            StoFormatOptionType optionType = stoButterfly.getOptionType();
                            String optionValue = stoButterfly.getOptionValue();
                            switch (optionType) {
                                case REDIRECTION:
                                    adapterListener.stoOpenDeeplink(optionValue);
                                    break;
                                case VIDEO:
                                    adapterListener.stoOpenVideo(optionValue);
                                    break;
                                case PDF:
                                    adapterListener.stoOpenPDF(optionValue);
                                    break;
                                default:
                                    StoLog.d(TAG, "setButtonListener -> OPTION onClick ");
                            }
                        }
                    } else {
                        // TODO
                        StoLog.w(TAG, "AdapterListener is null");
                    }
                }
            });
        }
    }
}
