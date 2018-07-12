package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2016. 12. 11..
 */
public class ConstraintOfPOIType {
    List<CategoryConstraint> categoryConstraintList;


    public ConstraintOfPOIType(){
        categoryConstraintList = new ArrayList<>();
    }
    public void addCategoryConstraint(CategoryConstraint categoryConstraint){
        categoryConstraintList.add(categoryConstraint);
    }

    public String toString(){
        StringBuffer strbuf = new StringBuffer();
        for (CategoryConstraint categoryConstraint : categoryConstraintList){
            strbuf.append(categoryConstraint.toString());
        }
        return strbuf.toString();
    }
}
