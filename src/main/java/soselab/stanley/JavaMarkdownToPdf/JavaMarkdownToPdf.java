package soselab.stanley.JavaMarkdownToPdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
import org.markdown4j.Markdown4jProcessor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaMarkdownToPdf {
    private final Markdown4jProcessor markdown4jProcessor;
    private String title;

    public JavaMarkdownToPdf() {
        markdown4jProcessor = new Markdown4jProcessor();
    }

    public byte[] convert(String markdown) {
        try {
            return getPdf(fixHtml(markdown4jProcessor.process(markdown).trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JavaMarkdownToPdf setTitle(String title) {
        this.title = title;
        return this;
    }

    private byte[] getPdf(String html) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(new FontProvider());
            converterProperties.getFontProvider().addFont("fonts/NotoSansCJKtc-Regular.otf");
            converterProperties.getFontProvider().addFont("fonts/NotoSansCJKtc-Bold.otf");
            HtmlConverter.convertToPdf(input, os, converterProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    private String fixHtml(String html) {
        return
            "<!DOCTYPE html>\n<html>\n" +
            "<head><title>" +
            title +
            "</title></head>" +
            "<body>\n" + html + "\n</body></html>";
    }

    private static String getMarkDownString(String dr, String dh, String ou) {
        String markdown =
                "# PDAS 個人資料授權系統\n" +
                        "\n---\n\n" +
                        "\n- 簽署人 (資料請求者)：" + dr +
                        "\n- 簽署人 (資料保管者)：" + dh +
                        (ou != null ? "\n- 簽署人 (授權人)：" + ou : "") +
                        "\n\n---\n\n"
                ;

        final String contractBody =
                "# 資料授權同意書\n" +
                        "\n" +
                        "### 契約簽署人\n" +
                        "- 資料請求者 (甲方)：SOSELab Co.\n" +
                        "- 資料保管者 (乙方)：台灣電力公司\n" +
                        "- 授權人 (丙方)\n" +
                        "\n" +
                        "### 資料授權範圍\n" +
                        "- 目的：分析用電量，建立用電預測模型。\n" +
                        "- 期間：不限。\n" +
                        "- 地區：不限。\n" +
                        "- 對象：SOSELab Co.\n" +
                        "- 方式：自動化機器或非自動方式。\n" +
                        "\n" +
                        "*依個資法第三條規定，當事人可以行使以下權利，請求閱覽、副本、更正、停止處理利用、刪除。*\n" +
                        "\n" +
                        "### 權利以及關係\n" +
                        "1. 甲方須提供權利金給資料所有人丙方以及資料保管人乙方。\n" +
                        "2. 丙方同意授權上方載述之資料範圍給甲方利用。";


        return markdown + contractBody;
    }
    public static void main(String[] args) {
        final String str = getMarkDownString("SOSELab Co.", "台灣電力公司", "Stanley");
        try {
            Files.write(Paths.get("test.pdf"), new JavaMarkdownToPdf().convert(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
