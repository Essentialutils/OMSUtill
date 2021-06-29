package com.oms.utill.views.ccs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oms.utill.R;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CountryCodeSelect extends RelativeLayout {
  private static String TAG = CountryCodeSelect.class.getSimpleName();

  private final String DEFAULT_COUNTRY = Locale.getDefault().getCountry();
  private static final String DEFAULT_ISO_COUNTRY = "ID";
  private static final int DEFAULT_TEXT_COLOR = 0;
  private static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;

  private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;

  private int mDefaultCountryCode;
  private String mDefaultCountryNameCode;

  //Util
  private PhoneNumberUtil mPhoneUtil;
  private PhoneNumberWatcher mPhoneNumberWatcher;
  PhoneNumberInputValidityListener mPhoneNumberInputValidityListener;

  private TextView mTvSelectedCountry;
  private TextView mRegisteredPhoneNumberTextView;
  private RelativeLayout mRlyHolder;
  private ImageView mImvArrow;
  private ImageView mImvFlag;
  private LinearLayout mLlyFlagHolder;
  private Country mSelectedCountry;
  private Country mDefaultCountry;
  private RelativeLayout mRlyClickConsumer;
  private View.OnClickListener mCountryCodeHolderClickListener;

  private boolean mHideNameCode = false;
  private boolean mShowFlag = true;
  private boolean mShowFullName = false;
  private boolean mUseFullName = false;
  private boolean mSelectionDialogShowSearch = true;

  private List<Country> mPreferredCountries;
  //this will be "AU,ID,US"
  private String mCountryPreference;
  private List<Country> mCustomMasterCountriesList;
  //this will be "AU,ID,US"
  private String mCustomMasterCountries;
  private boolean mKeyboardAutoPopOnSearch = true;
  private boolean mIsClickable = true;
  private CountryCodeDialog mCountryCodeDialog;

  private boolean mHidePhoneCode = false;

  private int mTextColor = DEFAULT_TEXT_COLOR;

  private int mDialogTextColor = DEFAULT_TEXT_COLOR;

  // Font typeface
  private Typeface mTypeFace;

  private boolean mIsHintEnabled = true;
  private boolean mIsEnablePhoneNumberWatcher = true;

  private boolean mSetCountryByTimeZone = true;

  private OnCountryChangeListener mOnCountryChangeListener;

  public interface OnCountryChangeListener {
    void onCountrySelected(Country selectedCountry);
  }

  public interface PhoneNumberInputValidityListener {
    void onFinish(CountryCodeSelect ccs, boolean isValid);
  }

  public CountryCodeSelect(Context context) {
    super(context);
    //if (!isInEditMode())
      init(null);
  }

  public CountryCodeSelect(Context context, AttributeSet attrs) {
    super(context, attrs);
    //if (!isInEditMode())
      init(attrs);
  }

  public CountryCodeSelect(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    //if (!isInEditMode())
      init(attrs);
  }

  @SuppressWarnings("unused")
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CountryCodeSelect(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    //if (!isInEditMode())
      init(attrs);
  }

  private void init(AttributeSet attrs) {
    inflate(getContext(), R.layout.country_code_select_layout_code_picker, this);

    mTvSelectedCountry = findViewById(R.id.selected_country_tv);
    mRlyHolder = findViewById(R.id.country_code_holder_rly);
    mImvArrow = findViewById(R.id.arrow_imv);
    mImvFlag = findViewById(R.id.flag_imv);
    mLlyFlagHolder = findViewById(R.id.flag_holder_lly);
    mRlyClickConsumer = findViewById(R.id.click_consumer_rly);

    applyCustomProperty(attrs);

    mCountryCodeHolderClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isClickable()) {
          showCountryCodeSelectDialog();
        }
      }
    };

    mRlyClickConsumer.setOnClickListener(mCountryCodeHolderClickListener);
  }

  private void applyCustomProperty(AttributeSet attrs) {
    mPhoneUtil = PhoneNumberUtil.createInstance(getContext());
    Resources.Theme theme = getContext().getTheme();
    TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.CountryCodeSelect, 0, 0);

    try {
      mHidePhoneCode = a.getBoolean(R.styleable.CountryCodeSelect_ccs_hidePhoneCode, false);
      mShowFullName = a.getBoolean(R.styleable.CountryCodeSelect_ccs_showFullName, false);
      mHideNameCode = a.getBoolean(R.styleable.CountryCodeSelect_ccs_hideNameCode, false);

      mIsHintEnabled = a.getBoolean(R.styleable.CountryCodeSelect_ccs_enableHint, true);

      // enable auto formatter for phone number input
      mIsEnablePhoneNumberWatcher =
          a.getBoolean(R.styleable.CountryCodeSelect_ccs_enablePhoneAutoFormatter, true);

      setKeyboardAutoPopOnSearch(
          a.getBoolean(R.styleable.CountryCodeSelect_ccs_keyboardAutoPopOnSearch, true));

      mCustomMasterCountries = a.getString(R.styleable.CountryCodeSelect_ccs_customMasterCountries);
      refreshCustomMasterList();

      mCountryPreference = a.getString(R.styleable.CountryCodeSelect_ccs_countryPreference);
      refreshPreferredCountries();

      applyCustomPropertyOfDefaultCountryNameCode(a);

      showFlag(a.getBoolean(R.styleable.CountryCodeSelect_ccs_showFlag, true));

      applyCustomPropertyOfColor(a);

      // text font
      String fontPath = a.getString(R.styleable.CountryCodeSelect_ccs_textFont);
      if (fontPath != null && !fontPath.isEmpty()) setTypeFace(fontPath);

      //text size
      int textSize = a.getDimensionPixelSize(R.styleable.CountryCodeSelect_ccs_textSize, 0);
      if (textSize > 0) {
        mTvSelectedCountry.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        setFlagSize(textSize);
        setArrowSize(textSize);
      } else { //no text size specified
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int defaultSize = Math.round(18 * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        setTextSize(defaultSize);
      }

      //if arrow arrow size is explicitly defined
      int arrowSize = a.getDimensionPixelSize(R.styleable.CountryCodeSelect_ccs_arrowSize, 0);
      if (arrowSize > 0) setArrowSize(arrowSize);

      mSelectionDialogShowSearch =
          a.getBoolean(R.styleable.CountryCodeSelect_ccs_selectionDialogShowSearch, true);
      setClickable(a.getBoolean(R.styleable.CountryCodeSelect_ccs_clickable, true));

      mSetCountryByTimeZone =
          a.getBoolean(R.styleable.CountryCodeSelect_ccs_setCountryByTimeZone, true);

      // Set to default phone code if no country name code set in attribute.
      if (mDefaultCountryNameCode == null || mDefaultCountryNameCode.isEmpty()) {
        setDefaultCountryFlagAndCode();
      }
    } catch (Exception e) {
      Log.d(TAG, "exception = " + e.toString());
      if (isInEditMode()) {
        mTvSelectedCountry.setText(
            getContext().getString(R.string.phone_code,
                getContext().getString(R.string.country_indonesia_number)));
      } else {
        mTvSelectedCountry.setText(e.getMessage());
      }
    } finally {
      a.recycle();
    }
  }

  private void applyCustomPropertyOfDefaultCountryNameCode(TypedArray tar) {
    //default country
    mDefaultCountryNameCode = tar.getString(R.styleable.CountryCodeSelect_ccs_defaultNameCode);
//    if (BuildConfig.DEBUG) {
//      Log.d(TAG, "mDefaultCountryNameCode from attribute = " + mDefaultCountryNameCode);
//    }

    if (mDefaultCountryNameCode == null || mDefaultCountryNameCode.isEmpty()) return;

    if (mDefaultCountryNameCode.trim().isEmpty()) {
      mDefaultCountryNameCode = null;
      return;
    }

    setDefaultCountryUsingNameCode(mDefaultCountryNameCode);
    setSelectedCountry(mDefaultCountry);
  }

  private void applyCustomPropertyOfColor(TypedArray arr) {
    //text color
    int textColor;
    if (isInEditMode()) {
      textColor = arr.getColor(R.styleable.CountryCodeSelect_ccs_textColor, DEFAULT_TEXT_COLOR);
    } else {
      textColor = arr.getColor(R.styleable.CountryCodeSelect_ccs_textColor,
          getColor(getContext(), R.color.defaultTextColor));
    }
    if (textColor != 0) setTextColor(textColor);

    mDialogTextColor =
        arr.getColor(R.styleable.CountryCodeSelect_ccs_dialogTextColor, DEFAULT_TEXT_COLOR);

    // background color of view.
    mBackgroundColor =
        arr.getColor(R.styleable.CountryCodeSelect_ccs_backgroundColor, Color.TRANSPARENT);

    if (mBackgroundColor != Color.TRANSPARENT) mRlyHolder.setBackgroundColor(mBackgroundColor);
  }

  private Country getDefaultCountry() {
    return mDefaultCountry;
  }

  private void setDefaultCountry(Country defaultCountry) {
    mDefaultCountry = defaultCountry;
  }

  @SuppressWarnings("unused") private Country getSelectedCountry() {
    return mSelectedCountry;
  }

  protected void setSelectedCountry(Country selectedCountry) {
    mSelectedCountry = selectedCountry;

    Context ctx = getContext();

    //as soon as country is selected, textView should be updated
    if (selectedCountry == null) {
      selectedCountry = CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode);
    }

    if (mRegisteredPhoneNumberTextView != null) {
      String ISO = selectedCountry.getIso().toUpperCase();
      setPhoneNumberWatcherToTextView(mRegisteredPhoneNumberTextView, ISO);
    }

    if (mOnCountryChangeListener != null) {
      mOnCountryChangeListener.onCountrySelected(selectedCountry);
    }

    mImvFlag.setImageResource(CountryUtils.getFlagDrawableResId(selectedCountry));

    if (mIsHintEnabled) setPhoneNumberHint();

    setSelectedCountryText(ctx, selectedCountry);
  }

  private void setSelectedCountryText(Context ctx, Country selectedCountry) {
    if (mHideNameCode && mHidePhoneCode && !mShowFullName) {
      mTvSelectedCountry.setText("");
      return;
    }

    String phoneCode = selectedCountry.getPhoneCode();
    if (mShowFullName) {
      String countryName = selectedCountry.getName().toUpperCase();

      if (mHidePhoneCode && mHideNameCode) {
        mTvSelectedCountry.setText(countryName);
        return;
      }

      if (mHideNameCode) {
        mTvSelectedCountry.setText(ctx.getString(R.string.country_full_name_and_phone_code,
            countryName, phoneCode));
        return;
      }

      String ISO = selectedCountry.getIso().toUpperCase();
      if (mHidePhoneCode) {
        mTvSelectedCountry.setText(ctx.getString(R.string.country_full_name_and_name_code,
            countryName, ISO));
        return;
      }

      mTvSelectedCountry.setText(ctx.getString(R.string.country_full_name_name_code_and_phone_code,
          countryName, ISO, phoneCode));

      return;
    }

    if (mHideNameCode && mHidePhoneCode) {
      String countryName = selectedCountry.getName().toUpperCase();
      mTvSelectedCountry.setText(countryName);
      return;
    }

    if (mHideNameCode) {
      mTvSelectedCountry.setText(ctx.getString(R.string.phone_code, phoneCode));
      return;
    }

    if (mHidePhoneCode) {
      String iso = selectedCountry.getIso().toUpperCase();
      mTvSelectedCountry.setText(iso);
      return;
    }

    String iso = selectedCountry.getIso().toUpperCase();
    mTvSelectedCountry.setText(ctx.getString(R.string.country_code_and_phone_code, iso, phoneCode));
  }

  boolean isKeyboardAutoPopOnSearch() {
    return mKeyboardAutoPopOnSearch;
  }

  /**
   * By default, keyboard is poped every time ccs is clicked and selection dialog is opened.
   *
   * @param keyboardAutoPopOnSearch true: to open keyboard automatically when selection dialog is
   * opened
   * false: to avoid auto pop of keyboard
   */
  public void setKeyboardAutoPopOnSearch(boolean keyboardAutoPopOnSearch) {
    mKeyboardAutoPopOnSearch = keyboardAutoPopOnSearch;
  }

  /**
   * Get status of phone number formatter.
   *
   * @return enable or not.
   */
  @SuppressWarnings("unused") public boolean isPhoneAutoFormatterEnabled() {
    return mIsEnablePhoneNumberWatcher;
  }

  /**
   * Enable or disable auto formatter for phone number inserted to TextView.
   * You need to set an EditText for phone number with `registerPhoneNumberTextView()`
   * to make use of this.
   *
   * @param isEnable return if phone auto formatter enabled or not.
   */
  @SuppressWarnings("unused") public void enablePhoneAutoFormatter(boolean isEnable) {
    mIsEnablePhoneNumberWatcher = isEnable;
    if (isEnable) {
      if (mPhoneNumberWatcher == null) {
        mPhoneNumberWatcher = new PhoneNumberWatcher(getSelectedCountryNameCode());
      }
    } else {
      mPhoneNumberWatcher = null;
    }
  }

  @SuppressWarnings("unused") private OnClickListener getCountryCodeHolderClickListener() {
    return mCountryCodeHolderClickListener;
  }

  /**
   * this will load mPreferredCountries based on mCountryPreference
   */
  void refreshPreferredCountries() {
    if (mCountryPreference == null || mCountryPreference.length() == 0) {
      mPreferredCountries = null;
      return;
    }

    List<Country> localCountryList = new ArrayList<>();
    for (String nameCode : mCountryPreference.split(",")) {
      Country country =
          CountryUtils.getByNameCodeFromCustomCountries(getContext(), mCustomMasterCountriesList,
              nameCode);
      if (country == null) continue;
      //to avoid duplicate entry of country
      if (isAlreadyInList(country, localCountryList)) continue;
      localCountryList.add(country);
    }

    if (localCountryList.size() == 0) {
      mPreferredCountries = null;
    } else {
      mPreferredCountries = localCountryList;
    }
  }

  /**
   * this will load mPreferredCountries based on mCountryPreference
   */
  void refreshCustomMasterList() {
    if (mCustomMasterCountries == null || mCustomMasterCountries.length() == 0) {
      mCustomMasterCountriesList = null;
      return;
    }

    List<Country> localCountries = new ArrayList<>();
    String[] split = mCustomMasterCountries.split(",");
    for (int i = 0; i < split.length; i++) {
      String nameCode = split[i];
      Country country = CountryUtils.getByNameCodeFromAllCountries(getContext(), nameCode);
      if (country == null) continue;
      //to avoid duplicate entry of country
      if (isAlreadyInList(country, localCountries)) continue;
      localCountries.add(country);
    }

    if (localCountries.size() == 0) {
      mCustomMasterCountriesList = null;
    } else {
      mCustomMasterCountriesList = localCountries;
    }
  }

  List<Country> getCustomCountries() {
    return mCustomMasterCountriesList;
  }

  /**
   * Get custom country by preference
   *
   * @param codePicker picker for the source of country
   * @return List of country
   */
  List<Country> getCustomCountries(@NonNull CountryCodeSelect codePicker) {
    codePicker.refreshCustomMasterList();
    if (codePicker.getCustomCountries() == null || codePicker.getCustomCountries().size() <= 0) {
      return CountryUtils.getAllCountries(codePicker.getContext());
    } else {
      return codePicker.getCustomCountries();
    }
  }

  @SuppressWarnings("unused")
  public void setCustomMasterCountriesList(@Nullable List<Country> customMasterCountriesList) {
    mCustomMasterCountriesList = customMasterCountriesList;
  }

  @SuppressWarnings("unused") public String getCustomMasterCountries() {
    return mCustomMasterCountries;
  }

  public List<Country> getPreferredCountries() {
    return mPreferredCountries;
  }

  @SuppressWarnings("unused")
  public void setCustomMasterCountries(@Nullable String customMasterCountries) {
    mCustomMasterCountries = customMasterCountries;
  }

  private boolean isAlreadyInList(Country country, List<Country> countries) {
    if (country == null || countries == null) return false;

    for (int i = 0; i < countries.size(); i++) {
      if (countries.get(i).getIso().equalsIgnoreCase(country.getIso())) {
        return true;
      }
    }

    return false;
  }

  private String detectCarrierNumber(String fullNumber, Country country) {
    String carrierNumber;
    if (country == null || fullNumber == null) {
      carrierNumber = fullNumber;
    } else {
      int indexOfCode = fullNumber.indexOf(country.getPhoneCode());
      if (indexOfCode == -1) {
        carrierNumber = fullNumber;
      } else {
        carrierNumber = fullNumber.substring(indexOfCode + country.getPhoneCode().length());
      }
    }
    return carrierNumber;
  }

  @Deprecated public void setDefaultCountryUsingPhoneCode(int defaultCountryCode) {
    Country defaultCountry =
        CountryUtils.getByCode(getContext(), mPreferredCountries, defaultCountryCode);

    if (defaultCountry == null) return;

    //if correct country is found, set the country
    mDefaultCountryCode = defaultCountryCode;
    setDefaultCountry(defaultCountry);
  }

  public void setDefaultCountryUsingPhoneCodeAndApply(int defaultCountryCode) {
    Country defaultCountry =
        CountryUtils.getByCode(getContext(), mPreferredCountries, defaultCountryCode);

    if (defaultCountry == null) return;

    //if correct country is found, set the country
    mDefaultCountryCode = defaultCountryCode;
    setDefaultCountry(defaultCountry);

    resetToDefaultCountry();
  }

  public void setDefaultCountryUsingNameCode(@NonNull String countryIso) {
    Country defaultCountry = CountryUtils.getByNameCodeFromAllCountries(getContext(), countryIso);

    if (defaultCountry == null) return;

    //if correct country is found, set the country
    mDefaultCountryNameCode = defaultCountry.getIso();
    setDefaultCountry(defaultCountry);
  }

  public void setDefaultCountryUsingNameCodeAndApply(@NonNull String countryIso) {
    Country defaultCountry = CountryUtils.getByNameCodeFromAllCountries(getContext(), countryIso);

    if (defaultCountry == null) return;

    //if correct country is found, set the country
    mDefaultCountryNameCode = defaultCountry.getIso();
    setDefaultCountry(defaultCountry);

    //TODO: This part of code need to be optimized!!.

    setEmptyDefault(null);
  }

  public String getDefaultCountryCode() {
    return mDefaultCountry.getPhoneCode();
  }

  @SuppressWarnings("unused") public int getDefaultCountryCodeAsInt() {
    int code = 0;
    try {
      code = Integer.parseInt(getDefaultCountryCode());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return code;
  }

  @SuppressWarnings("unused") public String getDefaultCountryCodeWithPlus() {
    return getContext().getString(R.string.phone_code, getDefaultCountryCode());
  }

  @SuppressWarnings("unused")
  public String getDefaultCountryName() {
    return mDefaultCountry.getName();
  }

  public String getDefaultCountryNameCode() {
    return mDefaultCountry.getIso().toUpperCase();
  }

  @SuppressWarnings("unused") public void resetToDefaultCountry() {
    setEmptyDefault();
  }

  public String getSelectedCountryCode() {
    return mSelectedCountry.getPhoneCode();
  }


  @SuppressWarnings("unused")
  public String getSelectedCountryCodeWithPlus() {
    return getContext().getString(R.string.phone_code, getSelectedCountryCode());
  }

  @SuppressWarnings("unused") public int getSelectedCountryCodeAsInt() {
    int code = 0;
    try {
      code = Integer.parseInt(getSelectedCountryCode());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return code;
  }

  @SuppressWarnings("unused")
  public String getSelectedCountryName() {
    return mSelectedCountry.getName();
  }

  public String getSelectedCountryNameCode() {
    return mSelectedCountry.getIso().toUpperCase();
  }

  @SuppressWarnings("unused")
  public void setCountryForPhoneCode(int countryCode) {
    Context ctx = getContext();
    Country country = CountryUtils.getByCode(ctx, mPreferredCountries, countryCode);
    if (country == null) {
      if (mDefaultCountry == null) {
        mDefaultCountry = CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode);
      }
      setSelectedCountry(mDefaultCountry);
    } else {
      setSelectedCountry(country);
    }
  }

  @SuppressWarnings("unused")
  public void setCountryForNameCode(@NonNull String countryNameCode) {
    Context ctx = getContext();
    Country country = CountryUtils.getByNameCodeFromAllCountries(ctx, countryNameCode);
    if (country == null) {
      if (mDefaultCountry == null) {
        mDefaultCountry = CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode);
      }
      setSelectedCountry(mDefaultCountry);
    } else {
      setSelectedCountry(country);
    }
  }

  @SuppressWarnings("unused")
  public void registerPhoneNumberTextView(@NonNull TextView textView) {
    setRegisteredPhoneNumberTextView(textView);
    if (mIsHintEnabled) setPhoneNumberHint();
  }

  @SuppressWarnings("unused") public TextView getRegisteredPhoneNumberTextView() {
    return mRegisteredPhoneNumberTextView;
  }

  void setRegisteredPhoneNumberTextView(@NonNull TextView phoneNumberTextView) {
    mRegisteredPhoneNumberTextView = phoneNumberTextView;
    if (mIsEnablePhoneNumberWatcher) {
      if (mPhoneNumberWatcher == null) {
        mPhoneNumberWatcher = new PhoneNumberWatcher(getDefaultCountryNameCode());
      }
      mRegisteredPhoneNumberTextView.addTextChangedListener(mPhoneNumberWatcher);
    }
  }

  private void setPhoneNumberWatcherToTextView(TextView textView, String countryNameCode) {
    if (!mIsEnablePhoneNumberWatcher) return;

    if (mPhoneNumberWatcher == null) {
      mPhoneNumberWatcher = new PhoneNumberWatcher(countryNameCode);
      textView.addTextChangedListener(mPhoneNumberWatcher);
    } else {
      if (!mPhoneNumberWatcher.getPreviousCountryCode().equalsIgnoreCase(countryNameCode)) {
        textView.removeTextChangedListener(mPhoneNumberWatcher);
        mPhoneNumberWatcher = new PhoneNumberWatcher(countryNameCode);
        textView.addTextChangedListener(mPhoneNumberWatcher);
      }
    }
  }
  public String getFullNumber() {
    String fullNumber = mSelectedCountry.getPhoneCode();
    if (mRegisteredPhoneNumberTextView == null) {
      Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number));
    } else {
      fullNumber += mRegisteredPhoneNumberTextView.getText().toString();
    }
    return fullNumber;
  }

  @SuppressWarnings("unused")
  public void setFullNumber(@NonNull String fullNumber) {
    Country country = CountryUtils.getByNumber(getContext(), mPreferredCountries, fullNumber);
    setSelectedCountry(country);
    String carrierNumber = detectCarrierNumber(fullNumber, country);
    if (mRegisteredPhoneNumberTextView == null) {
      Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number));
    } else {
      mRegisteredPhoneNumberTextView.setText(carrierNumber);
    }
  }

  @SuppressWarnings("unused")
  public String getFullNumberWithPlus() {
    return getContext().getString(R.string.phone_code, getFullNumber());
  }

  @SuppressWarnings("unused")
  public int getTextColor() {
    return mTextColor;
  }

  public int getDefaultContentColor() {
    return DEFAULT_TEXT_COLOR;
  }

  public void setTextColor(int contentColor) {
    mTextColor = contentColor;
    mTvSelectedCountry.setTextColor(contentColor);
    mImvArrow.setColorFilter(contentColor, PorterDuff.Mode.SRC_IN);
  }

  public int getBackgroundColor() {
    return mBackgroundColor;
  }

  public void setBackgroundColor(int backgroundColor) {
    mBackgroundColor = backgroundColor;
    mRlyHolder.setBackgroundColor(backgroundColor);
  }

  public int getDefaultBackgroundColor() {
    return DEFAULT_BACKGROUND_COLOR;
  }

  public void setTextSize(int textSize) {
    if (textSize > 0) {
      mTvSelectedCountry.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      setArrowSize(textSize);
      setFlagSize(textSize);
    }
  }

  public void setArrowSize(int arrowSizeInDp) {
    if (arrowSizeInDp > 0) {
      LayoutParams params = (LayoutParams) mImvArrow.getLayoutParams();
      params.width = arrowSizeInDp;
      params.height = arrowSizeInDp;
      mImvArrow.setLayoutParams(params);
    }
  }

  @SuppressWarnings("unused") public void hideNameCode(boolean hide) {
    mHideNameCode = hide;
    setSelectedCountry(mSelectedCountry);
  }


  @SuppressWarnings("unused")
  public void setCountryPreference(@NonNull String countryPreference) {
    mCountryPreference = countryPreference;
  }

  @SuppressWarnings("unused") public void setTypeFace(@NonNull Typeface typeFace) {
    mTypeFace = typeFace;
    try {
      mTvSelectedCountry.setTypeface(typeFace);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void setTypeFace(@NonNull String fontAssetPath) {
    try {
      Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), fontAssetPath);
      mTypeFace = typeFace;
      mTvSelectedCountry.setTypeface(typeFace);
    } catch (Exception e) {
      Log.d(TAG, "Invalid fontPath. " + e.toString());
    }
  }


  @SuppressWarnings("unused") public void setTypeFace(@NonNull Typeface typeFace, int style) {
    try {
      mTvSelectedCountry.setTypeface(typeFace, style);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Typeface getTypeFace() {
    return mTypeFace;
  }


  @SuppressWarnings("unused")
  public void setOnCountryChangeListener(@NonNull OnCountryChangeListener onCountryChangeListener) {
    mOnCountryChangeListener = onCountryChangeListener;
  }


  public void setFlagSize(int flagSize) {
    mImvFlag.getLayoutParams().height = flagSize;
    mImvFlag.requestLayout();
  }

  public void showFlag(boolean showFlag) {
    mShowFlag = showFlag;
    mLlyFlagHolder.setVisibility(showFlag ? VISIBLE : GONE);
  }


  @SuppressWarnings("unused") public void showFullName(boolean show) {
    mShowFullName = show;
    setSelectedCountry(mSelectedCountry);
  }


  public boolean isSelectionDialogShowSearch() {
    return mSelectionDialogShowSearch;
  }


  @SuppressWarnings("unused")
  public void setSelectionDialogShowSearch(boolean selectionDialogShowSearch) {
    mSelectionDialogShowSearch = selectionDialogShowSearch;
  }

  @Override public boolean isClickable() {
    return mIsClickable;
  }


  public void setClickable(boolean isClickable) {
    mIsClickable = isClickable;
    mRlyClickConsumer.setOnClickListener(isClickable ? mCountryCodeHolderClickListener : null);
    mRlyClickConsumer.setClickable(isClickable);
    mRlyClickConsumer.setEnabled(isClickable);
  }

  public boolean isHidePhoneCode() {
    return mHidePhoneCode;
  }

  public boolean isHideNameCode() {
    return mHideNameCode;
  }


  @SuppressWarnings("unused") public boolean isHintEnabled() {
    return mIsHintEnabled;
  }


  @SuppressWarnings("unused") public void enableHint(boolean hintEnabled) {
    mIsHintEnabled = hintEnabled;
    if (mIsHintEnabled) setPhoneNumberHint();
  }


  @SuppressWarnings("unused") public void hidePhoneCode(boolean hide) {
    mHidePhoneCode = hide;
    setSelectedCountry(mSelectedCountry);
  }

  private void setPhoneNumberHint() {
    // don't set phone number hint for null textView and country.
    if (mRegisteredPhoneNumberTextView == null
        || mSelectedCountry == null
        || mSelectedCountry.getIso() == null) {
      return;
    }

    String iso = mSelectedCountry.getIso().toUpperCase();
    PhoneNumberUtil.PhoneNumberType mobile = PhoneNumberUtil.PhoneNumberType.MOBILE;
    Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.getExampleNumberForType(iso, mobile);
    if (phoneNumber == null) {
      mRegisteredPhoneNumberTextView.setHint("");
      return;
    }

//    if (BuildConfig.DEBUG) {
//      Log.d(TAG, "setPhoneNumberHint called");
//      Log.d(TAG, "mSelectedCountry.getIso() = " + mSelectedCountry.getIso());
//      Log.d(TAG,
//          "hint = " + mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
//    }

    String hint = mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
    //if (mRegisteredPhoneNumberTextView.getHint() != null) {
    //  mRegisteredPhoneNumberTextView.setHint("");
    //}
    mRegisteredPhoneNumberTextView.setHint(hint);
  }

  private class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {
    private boolean lastValidity;
    private String previousCountryCode = "";

    String getPreviousCountryCode() {
      return previousCountryCode;
    }

    @SuppressWarnings("unused") public PhoneNumberWatcher() {
      super();
    }

    //TODO solve it! support for android kitkat
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhoneNumberWatcher(String countryCode) {
      super(countryCode);
      previousCountryCode = countryCode;
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      super.onTextChanged(s, start, before, count);
      try {
        String iso = null;
        if (mSelectedCountry != null) iso = mSelectedCountry.getPhoneCode().toUpperCase();
        Phonenumber.PhoneNumber phoneNumber = mPhoneUtil.parse(s.toString(), iso);
        iso = mPhoneUtil.getRegionCodeForNumber(phoneNumber);
        if (iso != null) {
          //int countryIdx = mCountries.indexOfIso(iso);
          //mCountrySpinner.setSelection(countryIdx);
        }
      } catch (NumberParseException ignored) {
      }

      if (mPhoneNumberInputValidityListener != null) {
        boolean validity = isValid();
        if (validity != lastValidity) {
          mPhoneNumberInputValidityListener.onFinish(CountryCodeSelect.this, validity);
        }
        lastValidity = validity;
      }
    }
  }


  @SuppressWarnings("unused") public String getNumber() {
    Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();

    if (phoneNumber == null) return null;

    if (mRegisteredPhoneNumberTextView == null) {
      Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number));
      return null;
    }

    return mPhoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
  }


  @SuppressWarnings("unused") public Phonenumber.PhoneNumber getPhoneNumber() {
    try {
      String iso = null;
      if (mSelectedCountry != null) iso = mSelectedCountry.getIso().toUpperCase();
      if (mRegisteredPhoneNumberTextView == null) {
        Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number));
        return null;
      }
      return mPhoneUtil.parse(mRegisteredPhoneNumberTextView.getText().toString(), iso);
    } catch (NumberParseException ignored) {
      return null;
    }
  }

  @SuppressWarnings("unused") public boolean isValid() {
    Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
    return phoneNumber != null && mPhoneUtil.isValidNumber(phoneNumber);
  }

  @SuppressWarnings("unused")
  public void setPhoneNumberInputValidityListener(PhoneNumberInputValidityListener listener) {
    mPhoneNumberInputValidityListener = listener;
  }
  private void setDefaultCountryFlagAndCode() {
    Context ctx = getContext();
    TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
    if (manager == null) {
      Log.e(TAG, "Can't access TelephonyManager. Using default county code");
      setEmptyDefault(getDefaultCountryCode());
      return;
    }

    try {
      String simCountryIso = manager.getSimCountryIso();
      if (simCountryIso == null || simCountryIso.isEmpty()) {
        String iso = manager.getNetworkCountryIso();
        if (iso == null || iso.isEmpty()) {
          enableSetCountryByTimeZone(true);
        } else {
          setEmptyDefault(iso);
//          if (BuildConfig.DEBUG) Log.d(TAG, "isoNetwork = " + iso);
        }
      } else {
        setEmptyDefault(simCountryIso);
//        if (BuildConfig.DEBUG) Log.d(TAG, "simCountryIso = " + simCountryIso);
      }
    } catch (Exception e) {
      Log.e(TAG, "Error when getting sim country, error = " + e.toString());
      setEmptyDefault(getDefaultCountryCode());
    }
  }

  private void setEmptyDefault() {
    setEmptyDefault(null);
  }

  private void setEmptyDefault(String countryCode) {
    if (countryCode == null || countryCode.isEmpty()) {
      if (mDefaultCountryNameCode == null || mDefaultCountryNameCode.isEmpty()) {
        if (DEFAULT_COUNTRY == null || DEFAULT_COUNTRY.isEmpty()) {
          countryCode = DEFAULT_ISO_COUNTRY;
        } else {
          countryCode = DEFAULT_COUNTRY;
        }
      } else {
        countryCode = mDefaultCountryNameCode;
      }
    }

    if (mIsEnablePhoneNumberWatcher && mPhoneNumberWatcher == null) {
      mPhoneNumberWatcher = new PhoneNumberWatcher(countryCode);
    }

    setDefaultCountryUsingNameCode(countryCode);
    setSelectedCountry(getDefaultCountry());
  }

  public void enableSetCountryByTimeZone(boolean isEnabled) {
    if (isEnabled) {
      if (mDefaultCountryNameCode != null && !mDefaultCountryNameCode.isEmpty()) return;
      if (mRegisteredPhoneNumberTextView != null) return;
      if (mSetCountryByTimeZone) {
        TimeZone tz = TimeZone.getDefault();

//        if (BuildConfig.DEBUG) Log.d(TAG, "tz.getID() = " + tz.getID());
        List<String> countryIsos = CountryUtils.getCountryIsoByTimeZone(getContext(), tz.getID());

        if (countryIsos == null) {
          // If no iso country found, fallback to device locale.
          setEmptyDefault();
        } else {
          setDefaultCountryUsingNameCode(countryIsos.get(0));
          setSelectedCountry(getDefaultCountry());
        }
      }
    }
    mSetCountryByTimeZone = isEnabled;
  }

  public int getDialogTextColor() {
    return mDialogTextColor;
  }

  @SuppressWarnings("unused")
  public void setDialogTextColor(int dialogTextColor) {
    mDialogTextColor = dialogTextColor;
  }

  public static int getColor(Context context, int id) {
    final int version = Build.VERSION.SDK_INT;
    if (version >= 23) {
      return context.getColor(id);
    } else {
      return context.getResources().getColor(id);
    }
  }

  public void showCountryCodeSelectDialog() {
    if (mCountryCodeDialog == null) mCountryCodeDialog = new CountryCodeDialog(this);
    mCountryCodeDialog.show();
  }
}
