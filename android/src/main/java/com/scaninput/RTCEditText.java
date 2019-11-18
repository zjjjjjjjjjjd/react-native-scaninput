package com.scaninput;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RTCEditText extends SimpleViewManager<EditText> {
  private ThemedReactContext mContext;
  private static final String EVENT_NAME_ONCLICK_NATIVE1 = "nativeEvent1";
  private static final String EVENT_NAME_ONCLICK_NATIVE2 = "nativeEvent2";
  private static final String EVENT_NAME_ONCHANGE_JS = "onNativeChange";
  private static final String EVENT_NAME_ONENTER_JS = "onNativeEnter";
  private static final String HANDLE_METHOD_NAME = "setValue"; // 交互方法名
  private static final int HANDLE_METHOD_ID = 1; // 交互命令ID
  private static final String HANDLE_BLUR_METHOD_NAME = "onBlur"; // 交互方法名
  private static final int HANDLE_BLUR_METHOD_ID = 2; // 交互命令ID
  private static final String HANDLE_FOCUS_METHOD_NAME = "onFocus"; // 交互方法名
  private static final int HANDLE_FOCUS_METHOD_ID = 3; // 交互命令ID
  private static final String HANDLE_ENABLED_METHOD_NAME = "setEnabled"; // 交互方法名
  private static final int HANDLE_ENABLED_METHOD_ID = 4; // 交互命令ID

  @Override
  public String getName() {
    return "NativeInput";
  }

  @Override
  protected EditText createViewInstance(ThemedReactContext reactContext) {
    this.mContext = reactContext;
    EditText text = new EditText(reactContext);
    return text;
  }

  @Override
  protected void addEventEmitters(final ThemedReactContext reactContext,final EditText input) {
    super.addEventEmitters(reactContext, input);
    if (android.os.Build.VERSION.SDK_INT <= 10) {
      input.setInputType(InputType.TYPE_NULL);
    } else {
      Class<EditText> cls = EditText.class;
      Method method;
      try {
        method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
        method.setAccessible(true);
        method.invoke(input, false);
      } catch (Exception e) {}
      try {
        method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
        method.setAccessible(true);
        method.invoke(input, false);
      } catch (Exception e) { }
    }
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    input.setLines(1);
    input.setMaxLines(1);
    input.setSingleLine(true);

    input.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length() == 0) {
          input.setSelection(0);
        }else if(i2 > 1){ // 扫描输入
          input.setSelection(charSequence.length());
        }else if(i1 == 0) { // 增加字符
          input.setSelection(i+1);
        }else { // 删除字符
          input.setSelection(i);
        }
        WritableMap data = Arguments.createMap();
        data.putString("text", charSequence.toString());
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                input.getId(),
                EVENT_NAME_ONCLICK_NATIVE1,
                data
        );
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    input.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER){
          if(keyEvent.getAction()==KeyEvent.ACTION_UP) {
            WritableMap data = Arguments.createMap();
            data.putString("text", input.getText().toString());
            reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    input.getId(),
                    EVENT_NAME_ONCLICK_NATIVE2,
                    data
            );
          }

          return true;
        }
        return false;
      }
    });
    input.requestFocus();
  }

  /*
      name="text" : name对应的值是在js代码中使用该封装组件时的属性名。
       */
  @ReactProp(name = "placeholder")
  public void setHint(EditText editText, String text) {
    editText.setHint(text);
  }

  @ReactProp(name = "underline")
  public void setBackground(EditText editText, Boolean hasUnderline) {
    if(hasUnderline == false) {
      editText.setBackground(null);
    }
  }

//  @ReactProp(name = "keyboardType")
//  public void setInputType(EditText editText, String keyboardType) {
//    if(keyboardType == "numeric") { // 数字
//      editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//    }else if(keyboardType == "phone") { // 电话
//      editText.setInputType(InputType.TYPE_CLASS_PHONE);
//    }else if(keyboardType == "password") { // 密码
//      editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
//    }else if(keyboardType == "decimal") { // 小数点
//      editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//    }else { // 普通
//      editText.setInputType(InputType.TYPE_CLASS_TEXT);
//    }
//  }

  @ReactProp(name = "enabled")
  public void setEnabled(EditText editText, Boolean enabled) {
    editText.setEnabled(enabled);
  }

  @ReactProp(name = "multiline")
  public void setSingleLine(EditText editText, Boolean single) {
    if(single) {
      editText.setSingleLine(false);
    }else {
      editText.setSingleLine(true);
    }
  }

  @ReactProp(name = "maxLength")
  public void setMaxLength(EditText editText, int maxLength) {
    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
  }

  @Override
  public void receiveCommand(@Nonnull EditText root, int commandId, @Nullable ReadableArray args) {
    switch (commandId){
      case HANDLE_METHOD_ID: // 设置value
        if(args != null) {
          String name = args.getString(0);//获取第一个位置的数据
          root.setText(name);
        }
        break;
      case HANDLE_BLUR_METHOD_ID: // 失去焦点
        if(args != null) {
          root.clearFocus();
        }

        break;
      case HANDLE_FOCUS_METHOD_ID: // 获得焦点
        if(args != null) {
          root.requestFocus();
        }

        break;
      case HANDLE_ENABLED_METHOD_ID: // 开启关闭
        if(args != null) {
          Boolean enabled = args.getBoolean(0);//获取第一个位置的数据
          root.setEnabled(enabled);
        }

        break;
      default:
        break;
    }
  }

  @Nullable
  @Override
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.<String, Object>builder()
            .put(
                    EVENT_NAME_ONCLICK_NATIVE1,
                    MapBuilder.of(
                            "registrationName",
                            EVENT_NAME_ONCHANGE_JS))
            .put(
                    EVENT_NAME_ONCLICK_NATIVE2,
                    MapBuilder.of(
                            "registrationName",
                            EVENT_NAME_ONENTER_JS))
            .build();

  }

  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.<String, Integer>builder()
            .put(
                    HANDLE_METHOD_NAME,
                    HANDLE_METHOD_ID)
            .put(
                    HANDLE_BLUR_METHOD_NAME,
                    HANDLE_BLUR_METHOD_ID)
            .put(
                    HANDLE_FOCUS_METHOD_NAME,
                    HANDLE_FOCUS_METHOD_ID)
            .put(
                    HANDLE_ENABLED_METHOD_NAME,
                    HANDLE_ENABLED_METHOD_ID)
            .build();
  }
}
