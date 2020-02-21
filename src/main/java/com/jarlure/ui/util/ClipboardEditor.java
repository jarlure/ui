package com.jarlure.ui.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import com.jme3.system.JmeSystem;
import com.jme3.system.android.JmeAndroidSystem;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClipboardEditor {

    private static final Logger LOG = Logger.getLogger(ClipboardEditor.class.getName());

    /**
     * 从系统剪贴板中读取字符串
     *
     * @return 剪贴板中的字符串。若无返回空字符串
     */
    public static String getTextFromClipboard() {
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                return WindowsHelper.getTextFromClipboard();
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                return AndroidHelper.getTextFromClipboard();
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * 将字符串设置进系统的剪贴板中
     *
     * @param text 要设置进剪贴板的字符串
     */
    public static void setTextToClipboard(String text) {
        switch (JmeSystem.getPlatform()) {
            case Windows32:
            case Windows64:
                WindowsHelper.setTextToClipboard(text);
                break;
            case Android_X86:
            case Android_ARM5:
            case Android_ARM6:
            case Android_ARM7:
            case Android_ARM8:
                AndroidHelper.setTextToClipboard(text);
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static class WindowsHelper{

        private static String getTextFromClipboard() {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String text = (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
                return text;
            } catch (Exception e) {
                LOG.log(Level.WARNING, "无法从剪贴板中获取文本", e);
            }
            return "";
        }

        private static void setTextToClipboard(String text){
            Transferable tText = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(tText, null);
        }

    }

    private static class AndroidHelper {

        private static String getTextFromClipboard() {
            final Object[] result = new Object[1];
            JmeAndroidSystem.getView().getHandler().post(()->{
                try{
                    result[0]=JmeAndroidSystem.getView().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                }catch (Exception e){
                    LOG.log(Level.WARNING, "无法从剪贴板中获取文本", e);
                    result[0]=false;
                }
            });
            while (result[0]==null){}//JME3线程循环等待直至Android线程返回剪贴板对象
            if (result[0] instanceof ClipboardManager){
                ClipData data = ((ClipboardManager)result[0]).getPrimaryClip();
                if (data.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    return data.getItemAt(0).getText().toString();
                }
            }
            return "";
        }

        private static void setTextToClipboard(String text){
            JmeAndroidSystem.getView().getHandler().post(() -> {
                ClipboardManager clipboardManager = (ClipboardManager) JmeAndroidSystem.getView().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Label",text);
                clipboardManager.setPrimaryClip(data);
            });
        }

    }

}
