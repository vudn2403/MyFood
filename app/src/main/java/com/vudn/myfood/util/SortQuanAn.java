package com.vudn.myfood.util;

import com.vudn.myfood.model.restaurant.QuanAnModel;

import java.util.Collections;
import java.util.List;

public class SortQuanAn {
    public SortQuanAn() {
    }

    public static List<QuanAnModel> sortByDistant(List<QuanAnModel> quanAnModelList) {
        sortByName(quanAnModelList);
        for (int i = 0; i < quanAnModelList.size() - 1; i++) {
            for (int j = i; j < quanAnModelList.size(); j++) {
                if (quanAnModelList.get(i).getKhoangcach() > quanAnModelList.get(j).getKhoangcach()) {
                    QuanAnModel tg = quanAnModelList.get(i);
                    quanAnModelList.set(i, quanAnModelList.get(j));
                    quanAnModelList.set(j, tg);
                }
            }
        }
        return quanAnModelList;
    }

    public static List<QuanAnModel> sortByPoint(List<QuanAnModel> quanAnModelList){
        sortByName(quanAnModelList);
        for (int i = 0; i < quanAnModelList.size() - 1; i++) {
            for (int j = i; j < quanAnModelList.size(); j++) {
                if (quanAnModelList.get(i).getDiemdanhgia() < quanAnModelList.get(j).getDiemdanhgia()) {
                    QuanAnModel tg = quanAnModelList.get(i);
                    quanAnModelList.set(i, quanAnModelList.get(j));
                    quanAnModelList.set(j, tg);
                }
            }
        }
        return quanAnModelList;
    }

    public static List<QuanAnModel> sortByName(List<QuanAnModel> quanAnModelList){
        Collections.sort(quanAnModelList, QuanAnModel.comparator);
        return quanAnModelList;
    }
}
