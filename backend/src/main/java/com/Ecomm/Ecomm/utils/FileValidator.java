package com.Ecomm.Ecomm.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.Ecomm.Ecomm.exceptions.BadRequestException;

public class FileValidator {

  public static boolean validateFileSize(byte[] fileBytes, int limitInMb)
      throws Exception {
    double fileSizeInMB = fileBytes.length / 1000000.0;
    if (fileSizeInMB > limitInMb) {
      throw new BadRequestException("File must not exceed " + limitInMb + "MB.");
    }
    return true;
  }

  public static boolean validateImage(byte[] imageBytes) throws Exception {
    if (hasValidHeader(imageBytes, PNG_HEADER)
        || hasValidHeader(imageBytes, JPEG_HEADER)) {
      return true;
    }
    throw new BadRequestException("Only PNG/JPEG image is allowed.");
  }

  public static boolean validateExcel(byte[] excelBytes) throws Exception {
    if (hasValidHeader(excelBytes, XLSX_HEADER)
        || hasValidHeader(excelBytes, XLS_HEADER)) {
      return true;
    }
    throw new BadRequestException("Only XLS(X) file is allowed.");
  }

  public static boolean validatePdf(byte[] pdfBytes) throws Exception {
    // if (!hasValidHeader(pdfBytes, PDF_HEADER)) {
    // System.out.println("in if");
    // throw new BadRequestException("Only PDF file is allowed.");
    // }
    try {
      PDDocument document = Loader.loadPDF(pdfBytes);
      boolean isValid = document.getNumberOfPages() > 0;
      document.close();
      return isValid;
    } catch (Exception e) {
      // System.out.println("in catch" + e.getMessage());
      throw new BadRequestException("Only PDF file is allowed.");
    }
  }

  private static boolean hasValidHeader(byte[] fileBytes, byte[] headerBytes) {
    for (int i = 0; i < headerBytes.length; ++i) {
      if (i >= fileBytes.length || headerBytes[i] != fileBytes[i]) {
        return false;
      }
    }
    return true;
  }

  // private static final byte[] PDF_HEADER =
  // new byte[] {(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46};

  private static final byte[] XLSX_HEADER = new byte[] { (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04 };

  private static final byte[] XLS_HEADER = new byte[] { (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1,
      (byte) 0xB1, (byte) 0x1A, (byte) 0xE1 };

  private static final byte[] JPEG_HEADER = new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF };

  private static final byte[] PNG_HEADER = new byte[] { (byte) 0x89, 'P', 'N', 'G', (byte) 0x0D, (byte) 0x0A,
      (byte) 0x1A,
      (byte) 0x0A };

}
