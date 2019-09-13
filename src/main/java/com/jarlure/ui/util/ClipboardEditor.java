package com.jarlure.ui.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClipboardEditor {

    private static final Logger LOG = Logger.getLogger(ClipboardEditor.class.getName());

    /**
     * 从系统剪贴板中读取字符串
     *
     * @return  剪贴板中的字符串。若无返回空字符串
     */
    public static String getTextFromClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String text = (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
            return text;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "无法从剪贴板中获取文本", e);
        }
        return "";
    }

    /**
     * 将字符串设置进系统的剪贴板中
     *
     * @param text  要设置进剪贴板的字符串
     */
    public static void setTextToClipboard(String text) {
        Transferable tText = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(tText, null);
    }

}
