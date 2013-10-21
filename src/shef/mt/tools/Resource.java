package shef.mt.tools;

import java.util.*;

import shef.mt.pipelines.ResourceRequirement;

/**
 *
 * @author Catalina Hallett, Eleftherios Avramidis
 *
 */
public abstract class Resource {
	
	protected ArrayList<ResourceRequirement> resourceRequirements;

	public Resource(){
		resourceRequirements = initRequiredResources();
		if (resourceRequirements == null){
			resourceRequirements = new ArrayList<ResourceRequirement>();
		}
	}
	
	/**
	 * This method is fired in the initialization (early) phase of the annotation
	 * process and allows the processor to load the necessary data and resources
	 * @param parameters a dynamic structure of parameters used for initializing this object 
	 */
	public abstract void initialize(HashMap<String, Object> params);
	
	/**
	 * It is a method that needs to be implemented by all subclasses, which 
	 * should provide their requirements on Resources that may be shared 
	 * with other processors, and the exact parametrization of each of them. 
	 * @return a list of objects that specify the name and the parameters of each resource 
	 */
	protected abstract ArrayList<ResourceRequirement> initRequiredResources();

//    public boolean available;
//    HashSet<String> req;
//    ResourceProcessor resProc;
//
//    public Resource() {
//    }
//
//    public Resource(ResourceProcessor resProc) {
//        this.resProc = resProc;
//    }
//
//    public boolean isAvailable() {
//        return available;
//    }
//
//
//	public void addRequisite(String r) {
//        if (req == null) {
//            req = new HashSet<String>();
//        }
//        req.add(r);
//    }
//
//    /**
//     * checks whether the Resource should run, i.e. it is required by at least
//     * one of the currently registered features
//     *
//     * @return true if the Resource is required, false otherwise
//     */
//    public boolean isRequired() {
//        if (req == null) {
//            return true;
//        }
//        Iterator<String> it = req.iterator();
//        while (it.hasNext()) {
//            if (ResourceManager.reqRegistered(it.next())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public ResourceProcessor getProcessor() {
//        return resProc;
//    }
}
