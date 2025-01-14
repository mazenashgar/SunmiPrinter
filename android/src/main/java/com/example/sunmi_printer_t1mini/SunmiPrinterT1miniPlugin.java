package com.example.sunmi_printer_t1mini;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import com.example.sunmi_printer_t1mini.utils.Aidl;

public class SunmiPrinterT1miniPlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;
  private static SunmiPrinter sunmiPrinter;

  private String TEXT = "printText";
  private String EMPTY_LINES = "emptyLines";
  private String PRINT_ROW = "printRow";
  private String LCD_STRING = "showLCD";
  private String LCD_DOUBLE_STRING = "showDoubleLCD";
  private String PRINTER_STATUS = "printerStatus";
  private String PRINT_IMAGE = "printImage";
  private String CUT_PAPER = "cutPaper";
  private String PRINT_QR = "printQR";
  private String OPEN_DRAWER = "openDrawer";
  private String getPrinterWidth = "getPrinterWidth";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "sunmi_printer_t1mini");
    channel.setMethodCallHandler(this);
    sunmiPrinter = new SunmiPrinter();
    sunmiPrinter.initAidl(flutterPluginBinding.getApplicationContext());
  }


  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "sunmi_printer_t1mini");
    channel.setMethodCallHandler(new SunmiPrinterT1miniPlugin());
    sunmiPrinter = new SunmiPrinter();
    sunmiPrinter.initAidl(registrar.context());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals(TEXT)){
      String text = call.argument("text");
      int align = call.argument("align");
      boolean bold = call.argument("bold");
      int size = call.argument("size");
      sunmiPrinter.text(text, align, bold,size);
      result.success(null);
    }else if(call.method.equals(EMPTY_LINES)){
      int n = call.argument("n");
      sunmiPrinter.emptyLines(n);
      result.success(null);
    }else if (call.method.equals(PRINT_ROW)) {
      String cols = call.argument("cols");
      boolean bold = call.argument("bold");
      int textSize = call.argument("textSize");
      sunmiPrinter.row(cols, bold, textSize);
      result.success(null);
    } else if (call.method.equals(LCD_STRING)) {
      String text = call.argument("text");
      sunmiPrinter.displayText(text);
      result.success(null);
    }else if (call.method.equals(LCD_DOUBLE_STRING)) {
      String upperText = call.argument("upperText");
      String bottomText = call.argument("bottomText");
      sunmiPrinter.displayDoubleText(upperText,bottomText);
      result.success(null);
    }else if (call.method.equals(PRINTER_STATUS)) {
      int printerState = Aidl.getInstance().updatePrinterState();
      result.success(printerState);
    }else if (call.method.equals(PRINT_IMAGE)) {
      String base64 = call.argument("base64");
      int align = call.argument("align");
      sunmiPrinter.printImage(base64, align);
    }else if(call.method.equals(PRINT_QR)){
      String data = call.argument("data");
      int modulesize = call.argument("modulesize"); 
      int errorlevel = call.argument("errorlevel");
      sunmiPrinter.printQRCode(data, modulesize, errorlevel);
      result.success(null);
    } else if(call.method.equals(CUT_PAPER)){
      sunmiPrinter.cutPaper();
      result.success(null);
    } else if(call.method.equals(OPEN_DRAWER)){
      sunmiPrinter.openDrawer();
      result.success(null);
    } else if(call.method.equals(getPrinterWidth)){
      String width = sunmiPrinter.getPrinterWidth();
      result.success(width);
    }else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
