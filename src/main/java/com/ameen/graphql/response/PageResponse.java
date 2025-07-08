package com.ameen.graphql.response;

import java.io.Serializable;

public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private long totalRecordCount;
    private long totalPageCount;
    boolean hasNext;
    boolean hasPrevious;
    private transient T data;

    public String getPdfData() {
        return pdfData;
    }

    public void setPdfData(String pdfData) {
        this.pdfData = pdfData;
    }

    public String getExcelData() {
        return excelData;
    }

    public void setExcelData(String excelData) {
        this.excelData = excelData;
    }

    private String pdfData;  // For Base64 PDF
    private String excelData;  // For Base64 Excel

    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    public long getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}