package com.kenick.sport.pojo.product;

import java.io.Serializable;

public class BrandQuery extends Brand implements Serializable {
    private Integer startLine; // 起始行
    private Integer maxNum; // 最大数目

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    @Override
    public String toString() {
        return "BrandQuery{" +
                "startLine=" + startLine +
                ", maxNum=" + maxNum +
                "} " + super.toString();
    }
}