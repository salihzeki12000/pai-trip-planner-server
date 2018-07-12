package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Path {

    @Expose
    private String pathType;
    @Expose
    private List<SubPath> subPath = new ArrayList<SubPath>();
    @Expose
    private Info info;

    /**
     * @return The pathType
     */
    public String getPathType() {
        return pathType;
    }

    /**
     * @param pathType The pathType
     */
    public void setPathType(String pathType) {
        this.pathType = pathType;
    }

    /**
     * @return The subPath
     */
    public List<SubPath> getSubPath() {
        return subPath;
    }

    /**
     * @param subPath The subPath
     */
    public void setSubPath(List<SubPath> subPath) {
        this.subPath = subPath;
    }

    /**
     * @return The info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * @param info The info
     */
    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}