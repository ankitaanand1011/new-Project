package com.drinkdrop.drinkdropowner.utils;

import android.app.Activity;
import android.content.Context;

import com.drinkdrop.drinkdropowner.modal.OrderDetailORM;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class DocumentHelper {
    public static String generatePDFNotice(Activity activity, OrderDetailORM orderDetailORM) {
        String genFileName = null;
        try {
            genFileName = "";
            File noticeDirectory = new File(Constants.APP_TEMP_FILE_PATH + "/");
            Document document = new Document(PageSize.A4, 5, 5, 5, 5);
            String fileName = "order_details.pdf";
            File noticeFile = new File(noticeDirectory, fileName);
            if (!noticeDirectory.exists())
                noticeDirectory.mkdirs();
            OutputStream destinationPdf = new FileOutputStream(noticeFile);
            PdfWriter writer = PdfWriter.getInstance(document, destinationPdf);
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(writer, document, new StringReader(readTemplate(activity,"order_details",orderDetailORM)));
            document.close();
            genFileName = fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genFileName;
    }


    private static String readTemplate(Context context,String templateName, OrderDetailORM orderDetailORM) {
        InputStream inputStream = null;
        String templateHtml = null;
        String template_name = templateName+".html";
        String itemLines = "";
        try {
            inputStream = context.getResources().openRawResource(context.getResources().getIdentifier("order_details", "raw", context.getPackageName()));//R.raw.notice;//context.getAssets().open(template_name);
        } catch (Exception e) {
            //LogHelper.writeExceptionLog(e);
        }
        StringBuilder templateContent = new StringBuilder();
        if (inputStream != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    templateContent.append(line);
                }
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                // LogHelper.writeExceptionLog(e);
            }

            for(int i=0; i< orderDetailORM.getOrderItems().size(); i++){
                itemLines += "<tr>" +
                        " <td style=\"border: 1px solid #CDCDCD;width: 20%; text-align: left;padding-left: 5px; padding-top: 5px;padding-bottom: 5px; font-size: 12px;\">" + orderDetailORM.getOrderItems().get(i).getProductName() + "</td>" +
                        " <td style=\"border: 1px solid #CDCDCD;width: 20%; text-align: right;padding-right: 5px; padding-top: 5px;padding-bottom: 5px; font-size: 12px;\">" + orderDetailORM.getOrderItems().get(i).getProductID() + "</td>" +
                        " <td style=\"border: 1px solid #CDCDCD;width: 20%; text-align: right;padding-right: 5px; padding-top: 5px;padding-bottom: 5px; font-size: 12px;\">" + orderDetailORM.getOrderItems().get(i).getProductQuantity() + "</td>" +
                        " <td style=\"border: 1px solid #CDCDCD;width: 20%; text-align: right;padding-right: 5px; padding-top: 5px;padding-bottom: 5px; font-size: 12px;\">" + orderDetailORM.getOrderItems().get(i).getProductPrice() + "</td>" +
                        " </tr>";
            }




            templateHtml = templateContent.toString();
            templateHtml=templateHtml.replaceAll("@OrderID@",orderDetailORM.getOrderID());
            templateHtml=templateHtml.replaceAll("@Name@",orderDetailORM.getAddressORM().getFirstName()+" "+ orderDetailORM.getAddressORM().getLastName());
            templateHtml=templateHtml.replaceAll("@Address@", orderDetailORM.getAddressORM().getAddress());
            templateHtml=templateHtml.replaceAll("@CityState@", orderDetailORM.getAddressORM().getCity()+", "+ orderDetailORM.getAddressORM().getState());
            templateHtml=templateHtml.replaceAll("@CountryZip@", orderDetailORM.getAddressORM().getCountry()+", "+ orderDetailORM.getAddressORM().getZip());
            templateHtml=templateHtml.replaceAll("@Mobile@", orderDetailORM.getAddressORM().getMobile());
            templateHtml=templateHtml.replaceAll("@Date@",orderDetailORM.getOrderDate());
            templateHtml=templateHtml.replaceAll("@PaymentMode@",orderDetailORM.getPaymentMode());
            templateHtml = templateHtml.replace("@InvoiceLines@", itemLines);
            templateHtml = templateHtml.replace("@GrandTotal@", orderDetailORM.getGrandTotal());
/*            templateHtml=templateHtml.replaceAll("@Whom@",noticeListObject.getSendTo());
            templateHtml=templateHtml.replaceAll("@Subject@",noticeListObject.getSubject());
            templateHtml=templateHtml.replaceAll("@User@",noticeListObject.getUserType());
            templateHtml=templateHtml.replaceAll("@Description@",noticeListObject.getDescription());
            templateHtml=templateHtml.replaceAll("@Principal@",noticeListObject.getSd());*/

        }
        return templateHtml;
    }

}
