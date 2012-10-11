package wlv.mt.features.impl;

import wlv.mt.features.util.Sentence;
import wlv.mt.tools.*;
import java.util.*;

/**
 * Feature is an abstract class which models a sentence feature. Typiclally, a
 * Feature consist of a value, a procedure for calculating the value and a set
 * of dependencies, i.e., resources that need to be available in order to be
 * able to compute the feature value. <br> Classes extending Feature will have
 * to provide their own method for computing the feature value by implementing
 *
 * @see Feature.run
 */
public abstract class Feature {

    private float value = 0;
    private boolean computable;
    private String index;
    private String description;
    private HashSet<String> resources;

    /**
     * returns the value
     */
    public float getValue() {
        return value;
    }

    /**
     * sets the feature value
     *
     * @param value the new value
     */
    public void setValue(float value) {
        this.value = value;
    }

    /*
     * returns a string representation of the Feature value
     */
    public String toString() {
        return new String(value + "");
    }

    /**
     * abstract method which should be overloaded in order to define the feature
     * value computation procedure
     *
     * @param source the source sentence
     * @param target the target sentence
     */
    public abstract void run(Sentence source, Sentence target);

    /**
     * check whether the Feature dependencies are registered and therefore the
     * feature value is computable returns true if the feature value is
     * computable and false otherwise
     */
    public boolean isComputable() {
        boolean result = true;
        if (resources == null || resources.size() == 0) {
            return true;
        }
        Iterator<String> it = resources.iterator();
        while (result && it.hasNext()) {
            result &= ResourceManager.isRegistered(it.next());
        }
        return result;
    }

    /**
     * sets the description
     *
     * @param d the new description
     */
    public void setDescription(String d) {
        description = d;
    }

    public String getDescription() {
        return description;
    }

    /**
     * returns the Feature index
     *
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * sets the index
     *
     * @param index the new index as integer
     */
    public void setIndex(int index) {
        this.index = index + "";
    }

    /**
     * sets the index
     *
     * @param index the new index
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * sets the resources (dependencies)
     *
     * @param resources a HashSet containing the resources this feature
     * computation depends on
     */
    public void setResources(HashSet<String> resources) {
        this.resources = resources;
    }

    public void addResource(String resource) {
        if (resources == null) {
            resources = new HashSet<String>();
        }
        resources.add(resource);
    }
}
